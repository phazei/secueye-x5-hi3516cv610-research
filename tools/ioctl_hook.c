/*
 * LD_PRELOAD ioctl hook -- captures VI ioctls from superb
 *
 * Intercepts ioctl() calls to /dev/vi and dumps payloads for:
 *   - SET_DEV_ATTR  (0x40784900) -- 120 bytes
 *   - GET_DEV_ATTR  (0x80784901) -- 120 bytes
 *   - REG_DEV       (0x40044961) -- 4 bytes
 *   - SET_PIPE_ATTR (cmd TBD)
 *   - SET_CHN_ATTR  (cmd TBD)
 *
 * Also captures MIPI RX ioctls on /dev/ot_mipi_rx.
 *
 * Build (from WSL):
 *   make -C driver hook
 *
 * Usage on camera:
 *   killall superb
 *   LD_PRELOAD=/progs/rec/00/ioctl_hook.so /usr/superb &
 *   # wait a few seconds for superb to initialize
 *   killall superb
 *   cat /progs/rec/00/ioctl_dump.log
 */

#define _GNU_SOURCE
#include <dlfcn.h>
#include <stdarg.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <errno.h>

/* Log file path (on SD card, survives reboot) */
#define LOG_PATH "/progs/rec/00/ioctl_dump.log"

/* VI ioctl commands */
#define VI_SET_DEV_ATTR   0x40784900
#define VI_GET_DEV_ATTR   0x80784901
#define VI_ENABLE_DEV     0x00004902
#define VI_DISABLE_DEV    0x00004903
#define VI_REG_DEV        0x40044961
#define VI_SET_PIPE_ATTR  0x40544910
#define VI_SET_CHN_ATTR   0x40404920
#define VI_BIND           0x40084960

/* MIPI ioctl type */
#define MIPI_TYPE         0x6d

/* Extract ioctl size from cmd */
#define IOCTL_SIZE(cmd)   (((cmd) >> 16) & 0x3FFF)

/* Real function pointers */
static int (*real_ioctl)(int fd, int request, ...) = NULL;
static int (*real_open)(const char *path, int flags, ...) = NULL;
static int (*real_close)(int fd) = NULL;
static void *(*real_mmap)(void *addr, size_t length, int prot, int flags, int fd, off_t offset) = NULL;

/* fd-to-device tracking */
#define MAX_FDS 256
static char fd_type[MAX_FDS]; /* 'v' = vi, 'm' = mipi, 'i' = isp, 'x' = mpp, 0 = unknown */

static FILE *logfp = NULL;

static void ensure_log(void)
{
    if (!logfp) {
        logfp = fopen(LOG_PATH, "a");
        if (logfp) {
            setbuf(logfp, NULL); /* unbuffered */
            fprintf(logfp, "\n=== ioctl_hook loaded (pid %d) ===\n", getpid());
        }
    }
}

static void hexdump(FILE *f, const void *data, int len)
{
    const unsigned char *p = (const unsigned char *)data;
    for (int i = 0; i < len; i++) {
        if (i > 0 && (i % 16) == 0)
            fprintf(f, "\n");
        else if (i > 0 && (i % 4) == 0)
            fprintf(f, " ");
        fprintf(f, "%02x", p[i]);
    }
    fprintf(f, "\n");

    /* Also print as 32-bit words for easy struct analysis */
    fprintf(f, "  u32: ");
    const unsigned int *w = (const unsigned int *)data;
    int nwords = len / 4;
    for (int i = 0; i < nwords; i++) {
        fprintf(f, "[%3d]=%u(0x%x) ", i * 4, w[i], w[i]);
        if ((i + 1) % 4 == 0)
            fprintf(f, "\n       ");
    }
    fprintf(f, "\n");
}

static const char *vi_cmd_name(unsigned int cmd)
{
    switch (cmd) {
    case VI_SET_DEV_ATTR:  return "SET_DEV_ATTR";
    case VI_GET_DEV_ATTR:  return "GET_DEV_ATTR";
    case VI_ENABLE_DEV:    return "ENABLE_DEV";
    case VI_DISABLE_DEV:   return "DISABLE_DEV";
    case VI_REG_DEV:       return "REG_DEV";
    case VI_SET_PIPE_ATTR: return "SET_PIPE_ATTR";
    case VI_SET_CHN_ATTR:  return "SET_CHN_ATTR";
    case VI_BIND:          return "BIND";
    default:               return NULL;
    }
}

/* Hook open() to track which fds map to which devices */
int open(const char *path, int flags, ...)
{
    if (!real_open) {
        real_open = dlsym(RTLD_NEXT, "open");
    }

    int fd;
    if (flags & O_CREAT) {
        va_list ap;
        va_start(ap, flags);
        int mode = va_arg(ap, int);
        va_end(ap);
        fd = real_open(path, flags, mode);
    } else {
        fd = real_open(path, flags);
    }

    if (fd >= 0 && fd < MAX_FDS) {
        if (strstr(path, "/dev/vi") && !strstr(path, "/dev/video")) {
            fd_type[fd] = 'v';
            ensure_log();
            if (logfp) fprintf(logfp, "open(%s) = fd %d [VI]\n", path, fd);
        } else if (strstr(path, "mipi_rx")) {
            fd_type[fd] = 'm';
            ensure_log();
            if (logfp) fprintf(logfp, "open(%s) = fd %d [MIPI]\n", path, fd);
        } else if (strstr(path, "isp")) {
            fd_type[fd] = 'i';
            ensure_log();
            if (logfp) fprintf(logfp, "open(%s) = fd %d [ISP]\n", path, fd);
        } else if (strstr(path, "vpss") || strstr(path, "venc") ||
                   strstr(path, "sys") || strstr(path, "vb")) {
            fd_type[fd] = 'x';  /* extra MPP device */
            ensure_log();
            if (logfp) fprintf(logfp, "open(%s) = fd %d [MPP]\n", path, fd);
        }
    }

    return fd;
}

/* Hook ioctl() */
int ioctl(int fd, int request, ...)
{
    if (!real_ioctl) {
        real_ioctl = dlsym(RTLD_NEXT, "ioctl");
    }

    /* Extract the arg pointer */
    va_list ap;
    va_start(ap, request);
    void *arg = va_arg(ap, void *);
    va_end(ap);

    /* Check if this is a VI fd */
    int is_vi = (fd >= 0 && fd < MAX_FDS && fd_type[fd] == 'v');
    int is_mipi = (fd >= 0 && fd < MAX_FDS && fd_type[fd] == 'm');
    unsigned int ucmd = (unsigned int)request;

    if (is_vi) {
        ensure_log();
        const char *name = vi_cmd_name(ucmd);
        int size = IOCTL_SIZE(ucmd);

        if (logfp) {
            if (name)
                fprintf(logfp, "\nVI ioctl: %s (0x%08x) size=%d fd=%d\n",
                        name, ucmd, size, fd);
            else
                fprintf(logfp, "\nVI ioctl: UNKNOWN (0x%08x) size=%d fd=%d\n",
                        ucmd, size, fd);

            /* Dump input data for _IOW commands */
            if ((ucmd & 0xE0000000) == 0x40000000 && size > 0 && arg) {
                fprintf(logfp, "  INPUT (%d bytes):\n", size);
                hexdump(logfp, arg, size);
            }
        }

        /* Call real ioctl */
        int ret = real_ioctl(fd, request, arg);

        if (logfp) {
            fprintf(logfp, "  => ret=%d (0x%x) errno=%d\n", ret, ret, errno);

            /* Dump output data for _IOR commands */
            if ((ucmd & 0xE0000000) == 0x80000000 && size > 0 && arg) {
                fprintf(logfp, "  OUTPUT (%d bytes):\n", size);
                hexdump(logfp, arg, size);
            }
        }

        return ret;
    }

    int is_isp = (fd >= 0 && fd < MAX_FDS && fd_type[fd] == 'i');
    int is_mpp = (fd >= 0 && fd < MAX_FDS && fd_type[fd] == 'x');

    if (is_isp) {
        ensure_log();
        int size = IOCTL_SIZE(ucmd);
        if (logfp) {
            fprintf(logfp, "\nISP ioctl: cmd=0x%08x nr=%d size=%d fd=%d\n",
                    ucmd, ucmd & 0xFF, size, fd);
            if ((ucmd & 0xE0000000) == 0x40000000 && size > 0 && arg) {
                fprintf(logfp, "  INPUT (%d bytes):\n", size);
                hexdump(logfp, arg, size > 256 ? 256 : size);
                if (size > 256) fprintf(logfp, "  ... (%d bytes total, truncated)\n", size);
            }
        }
        int ret = real_ioctl(fd, request, arg);
        if (logfp) {
            fprintf(logfp, "  => ret=%d (0x%x) errno=%d\n", ret, ret, errno);
            if ((ucmd & 0xE0000000) == 0x80000000 && size > 0 && arg) {
                fprintf(logfp, "  OUTPUT (%d bytes):\n", size > 256 ? 256 : size);
                hexdump(logfp, arg, size > 256 ? 256 : size);
            }
        }
        return ret;
    }

    if (is_mpp) {
        ensure_log();
        int size = IOCTL_SIZE(ucmd);
        if (logfp) {
            fprintf(logfp, "\nMPP ioctl: cmd=0x%08x nr=%d size=%d fd=%d\n",
                    ucmd, ucmd & 0xFF, size, fd);
            if ((ucmd & 0xE0000000) == 0x40000000 && size > 0 && arg) {
                fprintf(logfp, "  INPUT (%d bytes):\n", size);
                hexdump(logfp, arg, size > 128 ? 128 : size);
            }
        }
        int ret = real_ioctl(fd, request, arg);
        if (logfp) {
            fprintf(logfp, "  => ret=%d (0x%x) errno=%d\n", ret, ret, errno);
            if ((ucmd & 0xE0000000) == 0x80000000 && size > 0 && arg) {
                fprintf(logfp, "  OUTPUT (%d bytes):\n", size > 128 ? 128 : size);
                hexdump(logfp, arg, size > 128 ? 128 : size);
            }
        }
        return ret;
    }

    if (is_mipi) {
        ensure_log();
        int size = IOCTL_SIZE(ucmd);
        int nr = ucmd & 0xFF;

        if (logfp) {
            fprintf(logfp, "\nMIPI ioctl: nr=%d (0x%08x) size=%d fd=%d\n",
                    nr, ucmd, size, fd);
            if ((ucmd & 0xE0000000) == 0x40000000 && size > 0 && arg) {
                fprintf(logfp, "  INPUT (%d bytes):\n", size);
                hexdump(logfp, arg, size);
            }
        }

        int ret = real_ioctl(fd, request, arg);

        if (logfp) {
            fprintf(logfp, "  => ret=%d errno=%d\n", ret, errno);
            if ((ucmd & 0xE0000000) == 0x80000000 && size > 0 && arg) {
                fprintf(logfp, "  OUTPUT (%d bytes):\n", size);
                hexdump(logfp, arg, size);
            }
        }

        return ret;
    }

    /* Not a device we care about -- pass through */
    return real_ioctl(fd, request, arg);
}

/* Hook close() to track fd reuse */
int close(int fd)
{
    if (!real_close) {
        real_close = dlsym(RTLD_NEXT, "close");
    }

    if (fd >= 0 && fd < MAX_FDS && fd_type[fd]) {
        ensure_log();
        if (logfp) fprintf(logfp, "close(fd=%d) [type=%c]\n", fd, fd_type[fd]);
        fd_type[fd] = 0;
    }

    return real_close(fd);
}

/* Hook mmap() to capture ISP vreg memory mapping */
void *mmap(void *addr, size_t length, int prot, int flags, int fd, off_t offset)
{
    if (!real_mmap) {
        real_mmap = dlsym(RTLD_NEXT, "mmap");
    }

    void *result = real_mmap(addr, length, prot, flags, fd, offset);

    /* Log mmap calls on ISP and VI fds */
    if (fd >= 0 && fd < MAX_FDS && fd_type[fd]) {
        ensure_log();
        if (logfp) {
            fprintf(logfp, "\nmmap: fd=%d [%c] addr=%p len=0x%x(%u) prot=%d flags=0x%x offset=0x%lx\n",
                    fd, fd_type[fd], addr, (unsigned)length, (unsigned)length,
                    prot, flags, (unsigned long)offset);
            fprintf(logfp, "  => result=%p errno=%d\n", result, errno);
        }
    }

    return result;
}


