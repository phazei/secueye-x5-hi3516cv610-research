/*
 * vi.c -- Video Input (VI) device, pipe, and channel setup
 *
 * Uses B040 SDK library calls with vi_shim.so LD_PRELOAD to
 * translate the ot_vi_dev_attr struct from B040 to B051 layout.
 * Falls back to raw ioctls with hardcoded B051 struct bytes
 * captured from superb if the B040 library path fails.
 */

#include "vi.h"

static int vi_open_and_register(int dev_id)
{
    int fd = open("/dev/vi", O_RDWR);
    if (fd < 0) {
        printf("[FAIL] open(/dev/vi): %s\n", strerror(errno));
        return -1;
    }
    int ret = ioctl(fd, VI_IOC_REG_DEV, &dev_id);
    if (ret != 0) {
        printf("[FAIL] VI REG_DEV(dev_id=%d): ret=%d errno=%d\n", dev_id, ret, errno);
        close(fd);
        return -1;
    }
    printf("[ OK ] VI REG_DEV (fd=%d, dev_id=%d)\n", fd, dev_id);
    return fd;
}

static hi_s32 vi_init_raw(void)
{
    int ret;

    printf("[INFO] Using raw ioctl VI init (B051 layout)\n");
    vi_used_raw = 1;

    /* ── VI Device ── */
    vi_fd_dev = vi_open_and_register(VI_DEV);
    if (vi_fd_dev < 0) return HI_FAILURE;

    unsigned char dev_attr[120];
    memset(dev_attr, 0, sizeof(dev_attr));
    W32(dev_attr,   0, 4);            /* intf_mode = MIPI */
    W32(dev_attr,   8, 0xFFF00000);   /* comp_mask[0] */
    W32(dev_attr,  20, 0xFFFFFFFF);   /* ad_chn_id[0] = -1 */
    W32(dev_attr,  24, 0xFFFFFFFF);
    W32(dev_attr,  28, 0xFFFFFFFF);
    W32(dev_attr,  32, 0xFFFFFFFF);
    W32(dev_attr,  36, 5);            /* data_seq = YVYU */
    W32(dev_attr,  56, 1);            /* B051 new field */
    W32(dev_attr, 108, SENSOR_WIDTH);
    W32(dev_attr, 112, SENSOR_HEIGHT);

    ret = ioctl(vi_fd_dev, VI_IOC_SET_DEV_ATTR, dev_attr);
    if (ret != 0) { printf("[FAIL] VI SET_DEV_ATTR: ret=%d\n", ret); return HI_FAILURE; }
    printf("[ OK ] VI SET_DEV_ATTR (B051)\n");

    ret = ioctl(vi_fd_dev, VI_IOC_ENABLE_DEV);
    if (ret != 0) { printf("[FAIL] VI ENABLE_DEV: ret=%d\n", ret); return HI_FAILURE; }
    printf("[ OK ] VI ENABLE_DEV\n");

    int pipe_id = VI_PIPE;
    ret = ioctl(vi_fd_dev, VI_IOC_BIND, &pipe_id);
    if (ret != 0) { printf("[FAIL] VI BIND: ret=%d\n", ret); return HI_FAILURE; }
    printf("[ OK ] VI BIND\n");

    unsigned char wdr[28];
    memset(wdr, 0, sizeof(wdr));
    W32(wdr, 8, SENSOR_HEIGHT);
    ret = ioctl(vi_fd_dev, VI_IOC_WDR_FUSION, wdr);
    if (ret != 0) printf("[WARN] VI WDR_FUSION: ret=%d (non-fatal)\n", ret);
    else printf("[ OK ] VI WDR_FUSION\n");

    /* ── VI Pipe ── */
    vi_fd_pipe = vi_open_and_register(VI_DEV);
    if (vi_fd_pipe < 0) return HI_FAILURE;

    unsigned char pa[32];
    memset(pa, 0, sizeof(pa));
    W32(pa,  8, SENSOR_WIDTH);
    W32(pa, 12, SENSOR_HEIGHT);
    W32(pa, 16, 0x18);   /* RGB_BAYER_10BPP */
    W32(pa, 20, 0);      /* compress_mode = NONE */
    W32(pa, 24, -1);
    W32(pa, 28, -1);

    ret = ioctl(vi_fd_pipe, VI_IOC_SET_PIPE_ATTR, pa);
    if (ret != 0) { printf("[FAIL] VI SET_PIPE_ATTR: ret=%d\n", ret); return HI_FAILURE; }
    printf("[ OK ] VI SET_PIPE_ATTR\n");

    unsigned char pc[8];
    W32(pc, 0, 2); W32(pc, 4, 1700);
    ioctl(vi_fd_pipe, VI_IOC_PIPE_CFG, pc);  /* best effort */

    ret = ioctl(vi_fd_pipe, VI_IOC_START_PIPE);
    if (ret != 0) { printf("[FAIL] VI START_PIPE: ret=%d\n", ret); return HI_FAILURE; }
    printf("[ OK ] VI START_PIPE\n");

    /* ── VI Channel ── */
    vi_fd_chn = vi_open_and_register(VI_DEV);
    if (vi_fd_chn < 0) return HI_FAILURE;

    unsigned char ca[44];
    memset(ca, 0, sizeof(ca));
    W32(ca,  0, SENSOR_WIDTH);
    W32(ca,  4, SENSOR_HEIGHT);
    W32(ca,  8, 0x26);   /* YVU_SEMIPLANAR_420 */
    W32(ca, 36, -1);
    W32(ca, 40, -1);

    ret = ioctl(vi_fd_chn, VI_IOC_SET_CHN_ATTR, ca);
    if (ret != 0) { printf("[FAIL] VI SET_CHN_ATTR: ret=%d\n", ret); return HI_FAILURE; }
    printf("[ OK ] VI SET_CHN_ATTR\n");

    ret = ioctl(vi_fd_chn, VI_IOC_ENABLE_CHN);
    if (ret != 0) { printf("[FAIL] VI ENABLE_CHN: ret=%d\n", ret); return HI_FAILURE; }
    printf("[ OK ] VI ENABLE_CHN\n");

    /* ISP algo config on pipe fd */
    unsigned char ac[16];
    memset(ac, 0, sizeof(ac));
    W32(ac, 0, 1); W32(ac, 8, 5);
    ioctl(vi_fd_pipe, VI_IOC_ISP_ALGO_CFG, ac);

    return HI_SUCCESS;
}

hi_s32 vi_init(void)
{
    hi_s32 ret;

    printf("\n=== VI init ===\n");

    /* Try B040 library first (works if vi_shim.so is loaded) */
    hi_vi_dev_attr dev_attr;
    memset(&dev_attr, 0, sizeof(dev_attr));
    dev_attr.intf_mode  = HI_VI_INTF_MODE_MIPI;
    dev_attr.work_mode  = HI_VI_WORK_MODE_MULTIPLEX_1;
    dev_attr.scan_mode  = HI_VI_SCAN_PROGRESSIVE;
    dev_attr.data_type  = HI_VI_DATA_TYPE_RAW;
    dev_attr.in_size.width  = SENSOR_WIDTH;
    dev_attr.in_size.height = SENSOR_HEIGHT;
    dev_attr.data_rate  = HI_DATA_RATE_X1;
    dev_attr.data_reverse = HI_FALSE;
    dev_attr.component_mask[0] = 0xFFF00000;
    dev_attr.ad_chn_id[0] = -1;
    dev_attr.ad_chn_id[1] = -1;
    dev_attr.ad_chn_id[2] = -1;
    dev_attr.ad_chn_id[3] = -1;

    ret = hi_mpi_vi_set_dev_attr(VI_DEV, &dev_attr);
    if (ret != HI_SUCCESS) {
        printf("[WARN] B040 hi_mpi_vi_set_dev_attr failed: 0x%08X\n", (unsigned)ret);
        printf("[INFO] Falling back to raw ioctl approach...\n");
        return vi_init_raw();
    }
    printf("[ OK ] hi_mpi_vi_set_dev_attr (via B040 library + shim)\n");

    ret = hi_mpi_vi_enable_dev(VI_DEV);
    CHECK_RET("hi_mpi_vi_enable_dev", ret);

    ret = hi_mpi_vi_bind(VI_DEV, VI_PIPE);
    CHECK_RET("hi_mpi_vi_bind", ret);

    printf("[INFO] Linear mode -- skipping WDR fusion group config\n");

    hi_vi_pipe_attr pipe_attr;
    memset(&pipe_attr, 0, sizeof(pipe_attr));
    pipe_attr.pipe_bypass_mode = HI_VI_PIPE_BYPASS_NONE;
    pipe_attr.isp_bypass   = HI_FALSE;
    pipe_attr.size.width   = SENSOR_WIDTH;
    pipe_attr.size.height  = SENSOR_HEIGHT;
    pipe_attr.pixel_format = HI_PIXEL_FORMAT_RGB_BAYER_10BPP;
    pipe_attr.compress_mode = OT_COMPRESS_MODE_NONE;
    pipe_attr.frame_rate_ctrl.src_frame_rate = -1;
    pipe_attr.frame_rate_ctrl.dst_frame_rate = -1;

    ret = hi_mpi_vi_create_pipe(VI_PIPE, &pipe_attr);
    CHECK_RET("hi_mpi_vi_create_pipe", ret);

    ret = hi_mpi_vi_start_pipe(VI_PIPE);
    CHECK_RET("hi_mpi_vi_start_pipe", ret);

    hi_vi_chn_attr chn_attr;
    memset(&chn_attr, 0, sizeof(chn_attr));
    chn_attr.size.width    = SENSOR_WIDTH;
    chn_attr.size.height   = SENSOR_HEIGHT;
    chn_attr.pixel_format  = HI_PIXEL_FORMAT_YVU_SEMIPLANAR_420;
    chn_attr.dynamic_range = HI_DYNAMIC_RANGE_SDR8;
    chn_attr.video_format  = HI_VIDEO_FORMAT_LINEAR;
    chn_attr.compress_mode = HI_COMPRESS_MODE_NONE;
    chn_attr.mirror_en     = HI_FALSE;
    chn_attr.flip_en       = HI_FALSE;
    chn_attr.depth         = 0;
    chn_attr.frame_rate_ctrl.src_frame_rate = -1;
    chn_attr.frame_rate_ctrl.dst_frame_rate = -1;

    ret = hi_mpi_vi_set_chn_attr(VI_PIPE, VI_CHN, &chn_attr);
    CHECK_RET("hi_mpi_vi_set_chn_attr", ret);

    ret = hi_mpi_vi_enable_chn(VI_PIPE, VI_CHN);
    CHECK_RET("hi_mpi_vi_enable_chn", ret);

    return HI_SUCCESS;
}

void vi_deinit(void)
{
    if (vi_used_raw) {
        if (vi_fd_chn >= 0) {
            ioctl(vi_fd_chn, VI_IOC_DISABLE_CHN);
            close(vi_fd_chn);
            vi_fd_chn = -1;
        }
        if (vi_fd_pipe >= 0) {
            ioctl(vi_fd_pipe, VI_IOC_STOP_PIPE);
            close(vi_fd_pipe);
            vi_fd_pipe = -1;
        }
        if (vi_fd_dev >= 0) {
            int pipe_id = VI_PIPE;
            ioctl(vi_fd_dev, VI_IOC_UNBIND, &pipe_id);
            ioctl(vi_fd_dev, VI_IOC_DISABLE_DEV);
            close(vi_fd_dev);
            vi_fd_dev = -1;
        }
    } else {
        hi_mpi_vi_disable_chn(VI_PIPE, VI_CHN);
        hi_mpi_vi_stop_pipe(VI_PIPE);
        hi_mpi_vi_destroy_pipe(VI_PIPE);
        hi_mpi_vi_unbind(VI_DEV, VI_PIPE);
        hi_mpi_vi_disable_dev(VI_DEV);
    }
}
