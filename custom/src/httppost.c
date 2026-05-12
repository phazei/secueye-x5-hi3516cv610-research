/*
 * httppost.c -- Minimal HTTP POST client for ARM (static, no dependencies)
 *
 * Sends an HTTP POST request with a JSON body to a given URL.
 * Designed for webhook notifications from the SECUEYE X5 camera.
 *
 * Usage:
 *   ./httppost <host> <port> <path> <json_body>
 *   ./httppost 192.168.1.100 8080 /webhook '{"alarm":1,"time":"12:34:56"}'
 *   ./httppost ntfy.sh 80 /my-camera-alerts 'Camera alarm triggered'
 *
 * Limitations:
 *   - HTTP only (no HTTPS/TLS) -- for HTTPS, use a proxy or ntfy with HTTP
 *   - No DNS resolution beyond what libc provides
 *   - No chunked encoding, no redirects
 *   - Designed to be tiny (<30KB static binary)
 *
 * For ntfy.sh (supports plain HTTP):
 *   ./httppost ntfy.sh 80 /my-topic 'Alarm triggered'
 *
 * For Discord webhooks (requires HTTPS -- won't work directly):
 *   Use an HTTP->HTTPS proxy on your network, or ntfy.sh as intermediary
 *
 * Build:
 *   arm-v01c02-linux-musleabi-gcc -static -Os -o httppost httppost.c
 *
 * Copyright 2026, SECUEYE X5 investigation project
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <errno.h>

#define MAX_RESPONSE 4096
#define CONNECT_TIMEOUT 10
#define READ_TIMEOUT 10

static void usage(const char *prog)
{
    fprintf(stderr,
        "Usage: %s <host> <port> <path> <body>\n"
        "\n"
        "Send an HTTP POST request.\n"
        "\n"
        "Examples:\n"
        "  %s ntfy.sh 80 /my-camera 'Alarm triggered'\n"
        "  %s 192.168.1.100 8080 /webhook '{\"alarm\":1}'\n"
        "\n"
        "Options:\n"
        "  -c <content-type>  Set Content-Type (default: application/json)\n"
        "  -q                 Quiet mode (no output on success)\n"
        "  -h                 Show this help\n",
        prog, prog, prog);
}

int main(int argc, char *argv[])
{
    const char *content_type = "application/json";
    int quiet = 0;
    int opt;

    while ((opt = getopt(argc, argv, "c:qh")) != -1) {
        switch (opt) {
        case 'c':
            content_type = optarg;
            break;
        case 'q':
            quiet = 1;
            break;
        case 'h':
            usage(argv[0]);
            return 0;
        default:
            usage(argv[0]);
            return 1;
        }
    }

    if (argc - optind < 4) {
        usage(argv[0]);
        return 1;
    }

    const char *host = argv[optind];
    int port = atoi(argv[optind + 1]);
    const char *path = argv[optind + 2];
    const char *body = argv[optind + 3];
    int body_len = strlen(body);

    /* Resolve hostname */
    struct hostent *he = gethostbyname(host);
    if (!he) {
        fprintf(stderr, "httppost: cannot resolve host '%s'\n", host);
        return 2;
    }

    /* Create socket */
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("httppost: socket");
        return 3;
    }

    /* Set timeouts */
    struct timeval tv;
    tv.tv_sec = CONNECT_TIMEOUT;
    tv.tv_usec = 0;
    setsockopt(sock, SOL_SOCKET, SO_SNDTIMEO, &tv, sizeof(tv));
    tv.tv_sec = READ_TIMEOUT;
    setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &tv, sizeof(tv));

    /* Connect */
    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    memcpy(&addr.sin_addr, he->h_addr_list[0], he->h_length);

    if (connect(sock, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
        fprintf(stderr, "httppost: connect to %s:%d failed: %s\n",
                host, port, strerror(errno));
        close(sock);
        return 4;
    }

    /* Build HTTP request */
    char request[2048];
    int req_len = snprintf(request, sizeof(request),
        "POST %s HTTP/1.1\r\n"
        "Host: %s\r\n"
        "Content-Type: %s\r\n"
        "Content-Length: %d\r\n"
        "Connection: close\r\n"
        "User-Agent: secueye-httppost/1.0\r\n"
        "\r\n"
        "%s",
        path, host, content_type, body_len, body);

    if (req_len >= (int)sizeof(request)) {
        fprintf(stderr, "httppost: request too large\n");
        close(sock);
        return 5;
    }

    /* Send */
    int sent = 0;
    while (sent < req_len) {
        int n = write(sock, request + sent, req_len - sent);
        if (n <= 0) {
            fprintf(stderr, "httppost: write failed: %s\n", strerror(errno));
            close(sock);
            return 6;
        }
        sent += n;
    }

    /* Read response (just the status line) */
    char response[MAX_RESPONSE];
    int total = 0;
    while (total < (int)sizeof(response) - 1) {
        int n = read(sock, response + total, sizeof(response) - 1 - total);
        if (n <= 0)
            break;
        total += n;
        /* Stop after headers (double CRLF) for speed */
        response[total] = '\0';
        if (strstr(response, "\r\n\r\n"))
            break;
    }
    response[total] = '\0';

    close(sock);

    /* Parse status code */
    int status = 0;
    if (strncmp(response, "HTTP/", 5) == 0) {
        const char *sp = strchr(response, ' ');
        if (sp)
            status = atoi(sp + 1);
    }

    if (!quiet) {
        if (status >= 200 && status < 300) {
            printf("OK %d\n", status);
        } else if (status > 0) {
            /* Print first line of response */
            char *eol = strchr(response, '\r');
            if (eol) *eol = '\0';
            fprintf(stderr, "httppost: %s\n", response);
        } else {
            fprintf(stderr, "httppost: no valid HTTP response\n");
        }
    }

    return (status >= 200 && status < 300) ? 0 : 7;
}
