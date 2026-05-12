/*
 * Minimal VPSS test -- check if VPSS set_chn_attr works
 * without any VI or ISP init.
 *
 * Build: arm-linux-musleabi-gcc ... -o vpss_test vpss_test.c -lhi_mpi ...
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/ioctl.h>

#include "hi_type.h"
#include "hi_common.h"
#include "hi_common_sys.h"
#include "hi_common_vb.h"
#include "hi_common_vpss.h"
#include "hi_common_video.h"
#include "hi_mpi_sys.h"
#include "hi_mpi_vb.h"
#include "hi_mpi_vpss.h"

#define W32(buf, off, val) do { \
    unsigned int _v = (unsigned int)(val); \
    memcpy((buf) + (off), &_v, 4); \
} while (0)

int main(void)
{
    hi_s32 ret;

    printf("=== Minimal VPSS test ===\n");

    /* Test 1: Try VPSS on superb's existing state (no cleanup) */
    printf("\n--- Test 1: VPSS on existing state (no init) ---\n");
    {
        int vpss_fd = open("/dev/vpss", O_RDWR);
        if (vpss_fd >= 0) {
            int chn_id = 0;
            ret = ioctl(vpss_fd, 0x40045000, &chn_id);
            printf("  VPSS REG chn=0: ret=%d\n", ret);

            unsigned char ca[96];
            memset(ca, 0, sizeof(ca));
            W32(ca, 12, 3200);
            W32(ca, 16, 1800);
            W32(ca, 20, 1);
            W32(ca, 36, 0x26);
            W32(ca, 44, 0xFFFFFFFF);
            W32(ca, 48, 0xFFFFFFFF);

            ret = ioctl(vpss_fd, 0x40605008, ca);
            printf("  VPSS SET_CHN_ATTR: ret=0x%08X\n", (unsigned)ret);
            close(vpss_fd);
        }
    }

    /* Test 2: Full init then VPSS */
    printf("\n--- Test 2: Full init then VPSS ---\n");

    /* SYS/VB init */
    hi_mpi_sys_exit();
    hi_mpi_vb_exit();
    usleep(100000);

    hi_vb_cfg vb_cfg;
    memset(&vb_cfg, 0, sizeof(vb_cfg));
    vb_cfg.max_pool_cnt = 1;
    vb_cfg.common_pool[0].blk_size = 3200 * 1800 * 2;
    vb_cfg.common_pool[0].blk_cnt = 2;

    ret = hi_mpi_vb_set_cfg(&vb_cfg);
    printf("vb_set_cfg: 0x%08X\n", (unsigned)ret);
    ret = hi_mpi_vb_init();
    printf("vb_init: 0x%08X\n", (unsigned)ret);
    ret = hi_mpi_sys_init();
    printf("sys_init: 0x%08X\n", (unsigned)ret);

    /* Set VI-VPSS mode */
    hi_vi_vpss_mode vi_vpss_mode;
    memset(&vi_vpss_mode, 0, sizeof(vi_vpss_mode));
    ret = hi_mpi_sys_set_vi_vpss_mode(&vi_vpss_mode);
    printf("set_vi_vpss_mode: 0x%08X\n", (unsigned)ret);

    /* VPSS via B040 library */
    hi_vpss_grp_attr grp_attr;
    memset(&grp_attr, 0, sizeof(grp_attr));
    grp_attr.max_width = 3200;
    grp_attr.max_height = 1800;
    grp_attr.pixel_format = HI_PIXEL_FORMAT_YVU_SEMIPLANAR_420;
    grp_attr.frame_rate.src_frame_rate = -1;
    grp_attr.frame_rate.dst_frame_rate = -1;

    ret = hi_mpi_vpss_create_grp(0, &grp_attr);
    printf("vpss_create_grp: 0x%08X\n", (unsigned)ret);
    ret = hi_mpi_vpss_start_grp(0);
    printf("vpss_start_grp: 0x%08X\n", (unsigned)ret);

    /* VPSS channel via B040 library */
    hi_vpss_chn_attr chn_attr;
    memset(&chn_attr, 0, sizeof(chn_attr));
    chn_attr.width = 3200;
    chn_attr.height = 1800;
    chn_attr.chn_mode = HI_VPSS_CHN_MODE_AUTO;
    chn_attr.pixel_format = HI_PIXEL_FORMAT_YVU_SEMIPLANAR_420;
    chn_attr.depth = 0;  /* B051: depth=0 works, depth=1 requires ISP? */
    chn_attr.frame_rate.src_frame_rate = -1;
    chn_attr.frame_rate.dst_frame_rate = -1;

    ret = hi_mpi_vpss_set_chn_attr(0, 0, &chn_attr);
    printf("vpss_set_chn_attr (B040 lib): 0x%08X\n", (unsigned)ret);

    /* KEY TEST: Full raw ioctl flow matching superb exactly.
     * Use separate fds for group and channel operations. */
    printf("\n--- Test 3: Full raw ioctl (grp fd + chn fd) ---\n");
    {
        hi_mpi_vpss_destroy_grp(0);
        usleep(100000);

        /* GRP fd: create, configure, start */
        int fd_grp = open("/dev/vpss", O_RDWR);
        if (fd_grp >= 0) {
            int grp_id = 0;
            ret = ioctl(fd_grp, 0x40045000, &grp_id); /* VPSS_REG grp=0 */
            printf("  GRP REG grp=0: ret=0x%08X\n", (unsigned)ret);

            /* SET_GRP_ATTR: exact superb bytes */
            unsigned char ga[56];
            memset(ga, 0, sizeof(ga));
            W32(ga, 16, 3200);   /* max_width */
            W32(ga, 20, 1800);   /* max_height */
            W32(ga, 36, 0x26);   /* pixel_format = YUV420 */
            W32(ga, 48, 0xFFFFFFFF); /* src_frame_rate */
            W32(ga, 52, 0xFFFFFFFF); /* dst_frame_rate */
            ret = ioctl(fd_grp, 0x4038500c, ga);
            printf("  GRP SET_GRP_ATTR: ret=0x%08X\n", (unsigned)ret);

            ret = ioctl(fd_grp, 0x00005005); /* START_GRP */
            printf("  GRP START: ret=0x%08X\n", (unsigned)ret);

            /* Try nr=7 on GRP fd (might be CREATE_CHN or prepare) */
            ret = ioctl(fd_grp, 0x00005007);
            printf("  GRP _IO nr=7: ret=0x%08X\n", (unsigned)ret);

            /* CHN fd: register, configure, enable */
            int fd_chn = open("/dev/vpss", O_RDWR);
            if (fd_chn >= 0) {
                int chn_id = 0;
                ret = ioctl(fd_chn, 0x40045000, &chn_id); /* VPSS_REG chn=0 */
                printf("  CHN REG chn=0: ret=0x%08X\n", (unsigned)ret);

                /* SET_CHN_ATTR: exact superb B051 values */
                unsigned char ca[96];
                memset(ca, 0, sizeof(ca));
                W32(ca, 12, 3200);
                W32(ca, 16, 1800);
                /* depth=0 at [20], chn_mode=1 at [24] */
                W32(ca, 24, 1);
                W32(ca, 36, 0x26);
                W32(ca, 44, 0xFFFFFFFF);
                W32(ca, 48, 0xFFFFFFFF);

                ret = ioctl(fd_chn, 0x40605008, ca);
                printf("  CHN SET_CHN_ATTR: ret=0x%08X\n", (unsigned)ret);

                if (ret == 0) {
                    ret = ioctl(fd_chn, 0x0000500a); /* ENABLE_CHN */
                    printf("  CHN ENABLE: ret=0x%08X\n", (unsigned)ret);
                }

                close(fd_chn);
            }
            close(fd_grp);
        }
    }

    /* Test 4: Try nr=7 with _IOW(4) as CREATE_CHN(chn_id) on grp fd */
    printf("\n--- Test 4: _IOW(4) nr=7 on grp fd ---\n");
    {
        int fd_grp = open("/dev/vpss", O_RDWR);
        if (fd_grp >= 0) {
            int grp_id = 0;
            ioctl(fd_grp, 0x40045000, &grp_id);

            unsigned char ga[56];
            memset(ga, 0, sizeof(ga));
            W32(ga, 16, 3200);
            W32(ga, 20, 1800);
            W32(ga, 36, 0x26);
            W32(ga, 48, 0xFFFFFFFF);
            W32(ga, 52, 0xFFFFFFFF);
            ioctl(fd_grp, 0x4038500c, ga);
            ioctl(fd_grp, 0x00005005); /* START */

            /* Try CREATE_CHN via _IOW(4) nr=7 with chn_id=0 */
            unsigned int chn_id = 0;
            ret = ioctl(fd_grp, 0x40045007, &chn_id);
            printf("  GRP _IOW(4) nr=7 chn=0: ret=0x%08X\n", (unsigned)ret);

            /* Now try set_chn_attr on chn fd */
            int fd_chn = open("/dev/vpss", O_RDWR);
            if (fd_chn >= 0) {
                chn_id = 0;
                ioctl(fd_chn, 0x40045000, &chn_id);

                unsigned char ca[96];
                memset(ca, 0, sizeof(ca));
                W32(ca, 12, 3200);
                W32(ca, 16, 1800);
                W32(ca, 24, 1);
                W32(ca, 36, 0x26);
                W32(ca, 44, 0xFFFFFFFF);
                W32(ca, 48, 0xFFFFFFFF);

                ret = ioctl(fd_chn, 0x40605008, ca);
                printf("  CHN SET_CHN_ATTR: ret=0x%08X\n", (unsigned)ret);
                close(fd_chn);
            }

            ioctl(fd_grp, 0x00005006); /* STOP */
            ioctl(fd_grp, 0x0000500d); /* DESTROY */
            close(fd_grp);
        }
    }

    /* Try raw ioctl with different struct sizes and nr values */
    printf("\n--- Probing VPSS SET_CHN_ATTR variations ---\n");

    unsigned char ca[128];
    memset(ca, 0, sizeof(ca));
    W32(ca, 12, 3200);
    W32(ca, 16, 1800);
    W32(ca, 20, 1);
    W32(ca, 36, 0x26);
    W32(ca, 44, 0xFFFFFFFF);
    W32(ca, 48, 0xFFFFFFFF);

    /* Try different nr values (B040 is nr=8, maybe B051 shifted?) */
    unsigned int nr_try[] = {8, 9, 10, 11, 12, 13, 14, 15};
    for (int i = 0; i < 8; i++) {
        int vpss_fd = open("/dev/vpss", O_RDWR);
        if (vpss_fd < 0) break;
        int chn_id = 0;
        ioctl(vpss_fd, 0x40045000, &chn_id);

        /* _IOW('P', nr, 96) = 0x40600000 | (nr << 0) | ('P' << 8) */
        unsigned int cmd = 0x40605000 | nr_try[i];
        ret = ioctl(vpss_fd, cmd, ca);
        printf("  nr=%u cmd=0x%08x: ret=0x%08X\n", nr_try[i], cmd, (unsigned)ret);
        close(vpss_fd);
    }

    /* Try different struct sizes (B040 is 96, maybe B051 is larger?) */
    printf("\n--- Probing VPSS SET_CHN_ATTR sizes ---\n");
    unsigned int sizes[] = {96, 100, 104, 108, 112, 116, 120, 128};
    for (int i = 0; i < 8; i++) {
        int vpss_fd = open("/dev/vpss", O_RDWR);
        if (vpss_fd < 0) break;
        int chn_id = 0;
        ioctl(vpss_fd, 0x40045000, &chn_id);

        /* _IOW('P', 8, size) */
        unsigned int cmd = (0x40000000) | ((sizes[i] & 0x3FFF) << 16) | 0x5008;
        ret = ioctl(vpss_fd, cmd, ca);
        printf("  size=%u cmd=0x%08x: ret=0x%08X\n", sizes[i], cmd, (unsigned)ret);
        close(vpss_fd);
    }

    /* Also try just setting width=0 height=0 (maybe it needs zero-init first?) */
    printf("\n--- Try minimal chn_attr (all zeros except frame_rate) ---\n");
    {
        int vpss_fd = open("/dev/vpss", O_RDWR);
        if (vpss_fd >= 0) {
            int chn_id = 0;
            ioctl(vpss_fd, 0x40045000, &chn_id);

            unsigned char ca_min[96];
            memset(ca_min, 0, sizeof(ca_min));
            W32(ca_min, 44, 0xFFFFFFFF);
            W32(ca_min, 48, 0xFFFFFFFF);

            ret = ioctl(vpss_fd, 0x40605008, ca_min);
            printf("  all zeros: ret=0x%08X\n", (unsigned)ret);
            close(vpss_fd);
        }
    }

    /* Cleanup */
    hi_mpi_vpss_stop_grp(0);
    hi_mpi_vpss_destroy_grp(0);
    hi_mpi_sys_exit();
    hi_mpi_vb_exit();

    printf("=== done ===\n");
    return 0;
}
