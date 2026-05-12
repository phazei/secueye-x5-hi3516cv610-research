/*
 * recv -- Tiny TCP file receiver for Hi3516CV610 camera
 *
 * Listens on a port, receives raw binary data, writes to a file.
 * Designed to be deployed once via base64 TCP, then used forever
 * for fast WiFi file transfers.
 *
 * Usage:
 *   recv <port> <output_file>        -- receive one file, then exit
 *   recv <port> <output_dir> -d      -- daemon mode: loop forever,
 *                                       filename sent as first line
 *
 * Protocol (normal mode):
 *   Client connects, sends raw bytes, closes connection. Done.
 *
 * Protocol (daemon mode -d):
 *   Client connects, sends: "<filename>\n<raw bytes>", closes.
 *   File is written to <output_dir>/<filename>.
 *   Server stays running for more connections.
 *
 * Examples from PC:
 *   # Normal mode (camera side):
 *   ./recv 8888 /tmp/myfile.bin
 *
 *   # Send from PC:
 *   python -c "
 *   import socket,sys
 *   s=socket.socket()
 *   s.connect(('192.168.1.153',8888))
 *   s.sendall(open(sys.argv[1],'rb').read())
 *   s.close()
 *   " myfile.bin
 *
 *   # Daemon mode (camera side):
 *   ./recv 8888 /progs/rec/00 -d
 *
 *   # Send from PC (use tools/send_file.py):
 *   python tools/send_file.py 192.168.1.153 8888 myfile.bin
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/stat.h>

#define BUF_SIZE 4096

static int receive_file_simple(int client_fd, const char *path)
{
    int fd = open(path, O_WRONLY | O_CREAT | O_TRUNC, 0755);
    if (fd < 0) {
        printf("ERR open %s: %s\n", path, strerror(errno));
        return -1;
    }

    unsigned long total = 0;
    char buf[BUF_SIZE];
    ssize_t n;

    while ((n = read(client_fd, buf, sizeof(buf))) > 0) {
        ssize_t written = 0;
        while (written < n) {
            ssize_t w = write(fd, buf + written, n - written);
            if (w < 0) {
                printf("ERR write: %s\n", strerror(errno));
                close(fd);
                return -1;
            }
            written += w;
        }
        total += n;
    }

    close(fd);
    printf("OK %s %lu bytes\n", path, total);
    return 0;
}

static int receive_file_daemon(int client_fd, const char *dir)
{
    /* Read first line as filename */
    char header[512];
    int hlen = 0;
    char c;

    while (hlen < (int)sizeof(header) - 1) {
        ssize_t n = read(client_fd, &c, 1);
        if (n <= 0) return -1;
        if (c == '\n') break;
        header[hlen++] = c;
    }
    header[hlen] = '\0';

    /* Sanitize: strip path components (security) */
    char *fname = strrchr(header, '/');
    fname = fname ? fname + 1 : header;

    if (fname[0] == '\0' || fname[0] == '.') {
        printf("ERR bad filename\n");
        return -1;
    }

    /* Build full path */
    char path[1024];
    snprintf(path, sizeof(path), "%s/%s", dir, fname);

    /* Receive the rest as file data */
    int fd = open(path, O_WRONLY | O_CREAT | O_TRUNC, 0755);
    if (fd < 0) {
        printf("ERR open %s: %s\n", path, strerror(errno));
        return -1;
    }

    unsigned long total = 0;
    char buf[BUF_SIZE];
    ssize_t n;

    while ((n = read(client_fd, buf, sizeof(buf))) > 0) {
        ssize_t written = 0;
        while (written < n) {
            ssize_t w = write(fd, buf + written, n - written);
            if (w < 0) {
                printf("ERR write: %s\n", strerror(errno));
                close(fd);
                return -1;
            }
            written += w;
        }
        total += n;
    }

    close(fd);
    printf("OK %s %lu bytes\n", path, total);
    return 0;
}

int main(int argc, char *argv[])
{
    if (argc < 3) {
        printf("Usage: recv <port> <file|dir> [-d]\n");
        printf("  recv 8888 /tmp/file.bin     -- receive one file\n");
        printf("  recv 8888 /progs/rec/00 -d  -- daemon: loop, filename in stream\n");
        return 1;
    }

    int port = atoi(argv[1]);
    const char *target = argv[2];
    int daemon_mode = (argc > 3 && strcmp(argv[3], "-d") == 0);

    int srv = socket(AF_INET, SOCK_STREAM, 0);
    if (srv < 0) {
        printf("ERR socket: %s\n", strerror(errno));
        return 1;
    }

    int opt = 1;
    setsockopt(srv, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(port);

    if (bind(srv, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
        printf("ERR bind :%d: %s\n", port, strerror(errno));
        close(srv);
        return 1;
    }

    if (listen(srv, 1) < 0) {
        printf("ERR listen: %s\n", strerror(errno));
        close(srv);
        return 1;
    }

    printf("LISTEN :%d %s\n", port, daemon_mode ? "(daemon)" : "(one-shot)");

    do {
        struct sockaddr_in client_addr;
        socklen_t client_len = sizeof(client_addr);
        int client = accept(srv, (struct sockaddr *)&client_addr, &client_len);
        if (client < 0) {
            printf("ERR accept: %s\n", strerror(errno));
            continue;
        }

        printf("CONN %s:%d\n",
               inet_ntoa(client_addr.sin_addr),
               ntohs(client_addr.sin_port));

        if (daemon_mode) {
            receive_file_daemon(client, target);
        } else {
            receive_file_simple(client, target);
        }

        close(client);
    } while (daemon_mode);

    close(srv);
    return 0;
}
