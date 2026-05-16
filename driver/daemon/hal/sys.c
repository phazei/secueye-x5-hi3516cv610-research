/*
 * sys.c -- System init, sensor driver loading, MIPI RX configuration
 *
 * Handles the lowest-level MPP initialization: VB pool allocation,
 * system init, VI-VPSS mode, sensor driver dlopen, and MIPI RX
 * lane/clock configuration for the SC635HAI sensor.
 */

#include "sys.h"

/* ═══════════════════════════════════════════════════════════════
 *  Load sensor driver .so
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 load_sensor_driver(void)
{
    printf("\n=== Loading sensor driver ===\n");

    g_sns_dl = dlopen(SENSOR_LIB, RTLD_LAZY);
    if (g_sns_dl == NULL) {
        printf("[FAIL] dlopen(%s): %s\n", SENSOR_LIB, dlerror());
        return HI_FAILURE;
    }

    g_sns_obj = (hi_isp_sns_obj *)dlsym(g_sns_dl, SENSOR_OBJ_NAME);
    if (g_sns_obj == NULL) {
        printf("[FAIL] dlsym(%s): %s\n", SENSOR_OBJ_NAME, dlerror());
        dlclose(g_sns_dl);
        g_sns_dl = NULL;
        return HI_FAILURE;
    }

    printf("[ OK ] Loaded %s from %s\n", SENSOR_OBJ_NAME, SENSOR_LIB);
    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  System init (VB pools + sys init)
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 sys_init(void)
{
    hi_s32 ret;
    hi_vb_cfg vb_cfg;
    hi_vi_vpss_mode vi_vpss_mode;

    printf("\n=== System init ===\n");

    /* Clean slate -- aggressively tear down any previous state.
     * superb may have left ISP/VI/VPSS/VENC running. */
    printf("[INFO] Cleaning up previous state...\n");

    /* Unbind everything superb left behind */
    hi_mpp_chn src, dst;
    /* vi(0,0) -> vpss(0,0) */
    src.mod_id = HI_ID_VI; src.dev_id = 0; src.chn_id = 0;
    dst.mod_id = HI_ID_VPSS; dst.dev_id = 0; dst.chn_id = 0;
    hi_mpi_sys_unbind(&src, &dst);
    /* vpss(0,3) -> venc(0,0) */
    src.mod_id = HI_ID_VPSS; src.dev_id = 0; src.chn_id = 3;
    dst.mod_id = HI_ID_VENC; dst.dev_id = 0; dst.chn_id = 0;
    hi_mpi_sys_unbind(&src, &dst);
    /* vpss(0,4) -> venc(0,1) */
    src.mod_id = HI_ID_VPSS; src.dev_id = 0; src.chn_id = 4;
    dst.mod_id = HI_ID_VENC; dst.dev_id = 0; dst.chn_id = 1;
    hi_mpi_sys_unbind(&src, &dst);

    /* Stop superb's VENC channels */
    hi_mpi_venc_stop_chn(0);
    hi_mpi_venc_destroy_chn(0);
    hi_mpi_venc_stop_chn(1);
    hi_mpi_venc_destroy_chn(1);

    /* Stop superb's VPSS */
    hi_mpi_vpss_disable_chn(0, 0);
    hi_mpi_vpss_disable_chn(0, 3);
    hi_mpi_vpss_disable_chn(0, 4);
    hi_mpi_vpss_stop_grp(0);
    hi_mpi_vpss_destroy_grp(0);

    /* Stop superb's ISP */
    hi_mpi_isp_exit(0);

    /* Stop superb's VI */
    hi_mpi_vi_disable_chn(0, 0);
    hi_mpi_vi_stop_pipe(0);
    hi_mpi_vi_destroy_pipe(0);
    hi_mpi_vi_unbind(0, 0);
    hi_mpi_vi_disable_dev(0);

    /* Full system teardown */
    hi_mpi_sys_exit();
    hi_mpi_vb_exit();

    sleep(1);
    printf("[INFO] Cleanup done, initializing fresh...\n");

    /* Configure VB common pool.
     * Memory budget (88 MB MMZ total, measured):
     *   Common pool: 3840x2160 YUV420 x3 = ~36 MB
     *   3DNR refs + ISP: ~17 MB (driver internal, allocated at pipe start)
     *   VENC rcn buffers: ~24 MB (h265e0_rcn0 + rcn1, allocated at chn create)
     *   Misc (vca, npu, crypto): ~1 MB
     *   Total: ~78 MB, ~10 MB headroom
     *
     * No RAW pool needed: VI_ONLINE streams sensor data directly to VPSS. */
    memset(&vb_cfg, 0, sizeof(vb_cfg));
    vb_cfg.max_pool_cnt = 1;

    vb_cfg.common_pool[0].blk_size = VB_BLK_SIZE_ENC;  /* 3840x2160 YUV420 */
    vb_cfg.common_pool[0].blk_cnt  = 3;

    printf("[INFO] VB pool: %dx%d YUV420 x%d = %.1f MB\n",
           ENCODE_WIDTH, ENCODE_HEIGHT, 3,
           (float)VB_BLK_SIZE_ENC * 3 / (1024*1024));

    ret = hi_mpi_vb_set_cfg(&vb_cfg);
    CHECK_RET("hi_mpi_vb_set_cfg", ret);

    ret = hi_mpi_vb_init();
    CHECK_RET("hi_mpi_vb_init", ret);

    ret = hi_mpi_sys_init();
    CHECK_RET("hi_mpi_sys_init", ret);

    /* Set VI-VPSS mode: ONLINE-OFFLINE matches superb's working config */
    memset(&vi_vpss_mode, 0, sizeof(vi_vpss_mode));
    vi_vpss_mode.mode[0] = HI_VI_ONLINE_VPSS_OFFLINE;
    ret = hi_mpi_sys_set_vi_vpss_mode(&vi_vpss_mode);
    CHECK_RET("hi_mpi_sys_set_vi_vpss_mode", ret);

    usleep(100000);

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  MIPI RX init
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 mipi_init(void)
{
    hi_s32 ret;
    int fd;
    combo_dev_attr_t combo_attr;
    combo_dev_t devno = 0;
    sns_clk_source_t clk_src = 0;
    sns_rst_source_t rst_src = 0;
    lane_divide_mode_t hs_mode = LANE_DIVIDE_MODE_0;

    printf("\n=== MIPI RX init ===\n");

    fd = open(MIPI_DEV, O_RDWR);
    if (fd < 0) {
        printf("[FAIL] open(%s): %s\n", MIPI_DEV, strerror(errno));
        return HI_FAILURE;
    }

    ret = ioctl(fd, HI_MIPI_SET_HS_MODE, &hs_mode);
    if (ret != 0) {
        printf("[WARN] HI_MIPI_SET_HS_MODE failed: %s (non-fatal)\n", strerror(errno));
    } else {
        printf("[ OK ] HI_MIPI_SET_HS_MODE\n");
    }

    ret = ioctl(fd, HI_MIPI_ENABLE_SENSOR_CLOCK, &clk_src);
    if (ret != 0) printf("[WARN] ENABLE_SENSOR_CLOCK: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_ENABLE_SENSOR_CLOCK\n");

    ret = ioctl(fd, HI_MIPI_RESET_SENSOR, &rst_src);
    if (ret != 0) printf("[WARN] RESET_SENSOR: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_RESET_SENSOR\n");

    ret = ioctl(fd, HI_MIPI_UNRESET_SENSOR, &rst_src);
    if (ret != 0) printf("[WARN] UNRESET_SENSOR: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_UNRESET_SENSOR\n");

    ret = ioctl(fd, HI_MIPI_ENABLE_MIPI_CLOCK, &devno);
    if (ret != 0) printf("[WARN] ENABLE_MIPI_CLOCK: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_ENABLE_MIPI_CLOCK\n");

    ret = ioctl(fd, HI_MIPI_RESET_MIPI, &devno);
    if (ret != 0) printf("[WARN] RESET_MIPI: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_RESET_MIPI\n");

    /* Set MIPI device attributes for SC635HAI */
    memset(&combo_attr, 0, sizeof(combo_attr));
    combo_attr.devno      = 0;
    combo_attr.input_mode = INPUT_MODE_MIPI;
    combo_attr.data_rate  = MIPI_DATA_RATE_X1;
    combo_attr.img_rect.x      = 0;
    combo_attr.img_rect.y      = 0;
    combo_attr.img_rect.width  = SENSOR_WIDTH;
    combo_attr.img_rect.height = SENSOR_HEIGHT;

    combo_attr.mipi_attr.input_data_type = DATA_TYPE_RAW_10BIT;
    combo_attr.mipi_attr.wdr_mode        = HI_MIPI_WDR_MODE_NONE;
    /* Hardware uses lanes 0 and 2 (confirmed via /proc/umap/mipi_rx) */
    combo_attr.mipi_attr.lane_id[0]      = 0;
    combo_attr.mipi_attr.lane_id[1]      = 2;
    combo_attr.mipi_attr.lane_id[2]      = -1;
    combo_attr.mipi_attr.lane_id[3]      = -1;

    ret = ioctl(fd, HI_MIPI_SET_DEV_ATTR, &combo_attr);
    if (ret != 0) {
        printf("[FAIL] HI_MIPI_SET_DEV_ATTR: %s\n", strerror(errno));
        close(fd);
        return HI_FAILURE;
    }
    printf("[ OK ] HI_MIPI_SET_DEV_ATTR (SC635HAI: 2-lane MIPI, RAW10, %dx%d)\n",
           SENSOR_WIDTH, SENSOR_HEIGHT);

    ret = ioctl(fd, HI_MIPI_UNRESET_MIPI, &devno);
    if (ret != 0) printf("[WARN] UNRESET_MIPI: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_UNRESET_MIPI\n");

    close(fd);
    return HI_SUCCESS;
}
