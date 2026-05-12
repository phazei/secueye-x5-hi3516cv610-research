/*
 * Read VPSS state from running superb
 * Run while superb is active to capture B051 chn_attr struct
 */
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>

static void hexdump(const unsigned char *p, int len)
{
    const unsigned int *w = (const unsigned int *)p;
    int nwords = len / 4;
    for (int i = 0; i < nwords; i++) {
        printf("  [%3d] = %10u (0x%08x)\n", i * 4, w[i], w[i]);
    }
}

int main(void)
{
    printf("=== VPSS Read Test ===\n");

    /* Try GET_CHN_ATTR: _IOR('P', 9, 96) = 0x80605009 */
    printf("\n--- Try GET_CHN_ATTR various ways ---\n");
    
    /* Method 1: open, reg as chn=0, GET */
    int fd = open("/dev/vpss", O_RDWR);
    if (fd >= 0) {
        int chn_id = 0;
        int r = ioctl(fd, 0x40045000, &chn_id); /* VPSS_REG */
        printf("REG chn=0: ret=0x%08X\n", (unsigned)r);

        unsigned char buf[128];

        /* Try _IOR(96) nr=9 */
        memset(buf, 0xAA, sizeof(buf));
        r = ioctl(fd, 0x80605009, buf);
        printf("_IOR(96) nr=9: ret=0x%08X\n", (unsigned)r);
        if (r == 0) { printf("  DATA:\n"); hexdump(buf, 96); }

        /* Try _IOR(96) nr=8 */
        memset(buf, 0xAA, sizeof(buf));
        r = ioctl(fd, 0x80605008, buf);
        printf("_IOR(96) nr=8: ret=0x%08X\n", (unsigned)r);
        if (r == 0) { printf("  DATA:\n"); hexdump(buf, 96); }

        /* Try _IOWR(96) nr=8 */
        memset(buf, 0, sizeof(buf));
        r = ioctl(fd, 0xC0605008, buf);
        printf("_IOWR(96) nr=8: ret=0x%08X\n", (unsigned)r);
        if (r == 0) { printf("  DATA:\n"); hexdump(buf, 96); }

        /* Try _IOWR(96) nr=9 */
        memset(buf, 0, sizeof(buf));
        r = ioctl(fd, 0xC0605009, buf);
        printf("_IOWR(96) nr=9: ret=0x%08X\n", (unsigned)r);
        if (r == 0) { printf("  DATA:\n"); hexdump(buf, 96); }

        /* Also try GET_GRP_ATTR: _IOR(56) nr=12 or nr=13 */
        memset(buf, 0xBB, sizeof(buf));
        r = ioctl(fd, 0x8038500c, buf); /* _IOR(56) nr=12 */
        printf("_IOR(56) nr=12 (GET_GRP_ATTR?): ret=0x%08X\n", (unsigned)r);
        if (r == 0) { printf("  DATA:\n"); hexdump(buf, 56); }

        memset(buf, 0xBB, sizeof(buf));
        r = ioctl(fd, 0x8038500d, buf); /* _IOR(56) nr=13 */
        printf("_IOR(56) nr=13: ret=0x%08X\n", (unsigned)r);
        if (r == 0) { printf("  DATA:\n"); hexdump(buf, 56); }

        close(fd);
    }

    /* Also check /proc for VPSS info */
    printf("\n--- /proc VPSS info ---\n");
    FILE *fp = fopen("/proc/umap/vpss", "r");
    if (fp) {
        char line[256];
        int lines = 0;
        while (fgets(line, sizeof(line), fp) && lines < 40) {
            printf("%s", line);
            lines++;
        }
        fclose(fp);
    } else {
        printf("No /proc/umap/vpss\n");
    }

    printf("\n=== done ===\n");
    return 0;
}
