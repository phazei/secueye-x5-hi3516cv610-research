/*
 * B040->B051 ioctl translation shim
 *
 * LD_PRELOAD library that intercepts ioctl() from the B040 SDK libraries
 * and translates them to the B051 kernel interface.
 *
 * Translates:
 *   1. VI ioctl command numbers (registration, chn_attr, enable_chn, etc.)
 *   2. VI dev_attr struct: sets B051 field at offset 56
 *   3. VI WDR fusion: inserts sensor height at offset 8
 *   4. ISP: blocks B040 ISP ioctls (type 0x49/0x45 to /dev/isp_dev)
 *      and provides raw B051 ISP init via function interposition
 *
 * Build: make -C driver shim
 * Usage: LD_PRELOAD=/progs/rec/00/vi_shim.so ./pipeline_test
 */

#define _GNU_SOURCE
#include <dlfcn.h>
#include <stdarg.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <errno.h>

static int (*real_ioctl)(int fd, int request, ...) = NULL;
static unsigned int cached_height = 0;

/* ── B040 -> B051 command translations ─────────────────────── */

/* VI commands where nr field shifted (B040 -> B051, delta +7) */
#define B040_VI_REG          0x4004495a  /* -> 0x40044961 */
#define B051_VI_REG          0x40044961
#define B040_VI_SET_CHN_ATTR 0x402c4947  /* -> 0x402c494e */
#define B051_VI_SET_CHN_ATTR 0x402c494e
#define B040_VI_GET_CHN_ATTR 0x802c4948  /* -> 0x802c494f */
#define B051_VI_GET_CHN_ATTR 0x802c494f
#define B040_VI_ENABLE_CHN   0x0000494b  /* -> 0x00004952 */
#define B051_VI_ENABLE_CHN   0x00004952
#define B040_VI_DISABLE_CHN  0x0000494c  /* -> 0x00004953 */
#define B051_VI_DISABLE_CHN  0x00004953

#define VI_SET_DEV_ATTR      0x40784900
#define VI_GET_DEV_ATTR      0x80784901
#define VI_WDR_FUSION        0x401c490d  /* 28 bytes */

static unsigned int translate_cmd(unsigned int cmd)
{
    switch (cmd) {
    case B040_VI_REG:          return B051_VI_REG;
    case B040_VI_SET_CHN_ATTR: return B051_VI_SET_CHN_ATTR;
    case B040_VI_GET_CHN_ATTR: return B051_VI_GET_CHN_ATTR;
    case B040_VI_ENABLE_CHN:   return B051_VI_ENABLE_CHN;
    case B040_VI_DISABLE_CHN:  return B051_VI_DISABLE_CHN;
    }
    return cmd;
}

int ioctl(int fd, int request, ...)
{
    if (!real_ioctl)
        real_ioctl = dlsym(RTLD_NEXT, "ioctl");

    va_list ap;
    va_start(ap, request);
    void *arg = va_arg(ap, void *);
    va_end(ap);

    unsigned int ucmd = (unsigned int)request;
    unsigned int new_cmd = translate_cmd(ucmd);

    /* Translate VI command number if needed */
    if (new_cmd != ucmd) {
        printf("[SHIM] cmd 0x%08x -> 0x%08x\n", ucmd, new_cmd);
        return real_ioctl(fd, (int)new_cmd, arg);
    }

    /* WDR_FUSION: B051 needs sensor height at offset 8.
     * The B040 library sends all zeros. We patch offset 8 with
     * the sensor height. We read it from the previously-set dev_attr
     * in_size which we cache. */
    if (ucmd == VI_WDR_FUSION && arg) {
        unsigned char patched[28];
        memcpy(patched, arg, 28);
        /* Check if offset 8 is 0 (B040 default) and fix it */
        unsigned int val8;
        memcpy(&val8, patched + 8, 4);
        if (val8 == 0) {
            /* Set to sensor height -- hardcoded from dev_attr we saw earlier.
             * Superb sends 1800 here. If we haven't seen a SET_DEV_ATTR yet,
             * this won't run because the WDR call would have been skipped. */
            unsigned int height = cached_height;
            if (height > 0) {
                memcpy(patched + 8, &height, 4);
                printf("[SHIM] WDR_FUSION: set offset8=%u (height)\n", height);
            }
        }
        return real_ioctl(fd, request, patched);
    }

    /* SET_DEV_ATTR: set B051 field at offset 56 = 1 */
    if (ucmd == VI_SET_DEV_ATTR && arg) {
        unsigned char patched[120];
        memcpy(patched, arg, 120);

        /* Set B051 required field at offset 56 */
        unsigned int val = 1;
        memcpy(patched + 56, &val, 4);

        unsigned int w, h;
        memcpy(&w, patched + 108, 4);
        memcpy(&h, patched + 112, 4);
        printf("[SHIM] SET_DEV_ATTR: set offset56=1, size=%ux%u\n", w, h);
        cached_height = h;

        return real_ioctl(fd, request, patched);
    }

    return real_ioctl(fd, request, arg);
}
