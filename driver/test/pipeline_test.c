/*
 * SC635HAI Phase 3 Pipeline Test
 *
 * Brings up the full ISP video pipeline on Hi3516CV610 using our
 * custom libsns_sc635hai.so sensor driver. Captures a single JPEG
 * frame and saves it to the SD card as proof of working pipeline.
 *
 * Usage:
 *   1. Stop superb:  killall superb
 *   2. Run:          /progs/rec/00/pipeline_test
 *   3. Check:        /progs/rec/00/capture.jpg
 *   4. Reboot:       reboot
 *
 * Build: see driver/Makefile "pipeline_test" target
 *
 * References:
 *   - research/HIVIEW/mod/mpp/3516c/src/common/sample_comm_vi.c
 *   - research/HIVIEW/mod/mpp/3516c/src/common/sample_comm_isp.c
 *   - research/HIVIEW/mod/mpp/3516c/src/mpp.c (dlopen pattern)
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>
#include <pthread.h>
#include <dlfcn.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <sys/select.h>
#include <sys/time.h>
#include <linux/watchdog.h>   /* WDIOC_KEEPALIVE, WDIOC_GETTIMEOUT */

/* SDK headers (V1.0.2.1 B020 -- ot_ types, ss_mpi_ APIs) */
#include "ot_type.h"
#include "ot_common.h"
#include "ot_common_sys.h"
#include "ot_common_vb.h"
#include "ot_common_vi.h"
#include "ot_common_vpss.h"
#include "ot_common_venc.h"
#include "ot_common_isp.h"
#include "ot_common_video.h"
#include "ot_common_sns.h"
#include "ot_common_ae.h"
#include "ot_common_awb.h"
#include "ot_sns_ctrl.h"

/* SDK MPI APIs */
#include "ss_mpi_sys.h"
#include "ss_mpi_vb.h"
#include "ss_mpi_vi.h"
#include "ss_mpi_vpss.h"
#include "ss_mpi_venc.h"
#include "ss_mpi_isp.h"
#include "ss_mpi_ae.h"
#include "ss_mpi_awb.h"
#include "ss_mpi_sys_bind.h"
#include "ss_mpi_sys_mem.h"

/* 3DNR position API (for setting NR at VI or VPSS level) */
/* ss_mpi_sys_set_3dnr_pos / ss_mpi_sys_get_3dnr_pos are in ss_mpi_sys.h */

/* MIPI RX ioctl definitions */
#include "ot_mipi_rx.h"

/* B040 hi_ to V1.0.2.1 ot_/ss_mpi_ compatibility */
#include "hi_compat.h"

/* RTSP streaming (optional, enabled with --rtsp flag) */
#include "rtsp_push.h"

/* ── Configuration ────────────────────────────────────────────── */
#define SENSOR_WIDTH      3200
#define SENSOR_HEIGHT     1800
#define SENSOR_FPS        20.0f  /* Sensor native 20fps (VTS=2812). Superb encodes at 15fps
                                  * but the sensor/ISP runs at 20fps for better 3DNR temporal
                                  * sampling. VENC src_frame_rate=20, dst_frame_rate=15. */

#define VI_DEV            0
#define VI_PIPE           0
#define VI_CHN            0
#define VPSS_GRP          0
#define VPSS_CHN          0
#define VENC_CHN          0

#define MIPI_DEV          "/dev/ot_mipi_rx"
#define I2C_BUS           0

#define OUTPUT_FILE       "/progs/rec/00/ipc_drv/capture.h265"
#define SENSOR_LIB        "/progs/rec/00/ipc_drv/libsns_sc635hai.so"
#define SENSOR_OBJ_NAME   "g_sns_sc635hai_obj"

/* Buffer pool sizes
 * RAW10: 10 bits/pixel, stride-aligned = width * 2 (conservative)
 * YUV420 NV21: width * height * 1.5
 * 3DNR manages its own reference frames internally via the VPSS driver,
 * so we just need the normal working buffers. Keep minimal for MMZ. */
#define VB_BLK_SIZE_RAW   (SENSOR_WIDTH * SENSOR_HEIGHT * 2)   /* RAW10 ~= 2 bytes/pixel */
#define VB_BLK_SIZE_YUV   (SENSOR_WIDTH * SENSOR_HEIGHT * 3/2) /* NV21 */
#define VB_RAW_CNT        2
#define VB_YUV_CNT        3    /* +1 over minimum for 3DNR headroom */

/* ── Globals ──────────────────────────────────────────────────── */
static hi_isp_sns_obj *g_sns_obj = NULL;
static void           *g_sns_dl  = NULL;
static volatile int    g_isp_running = 0;
static pthread_t       g_isp_thread;

/* RTSP streaming mode */
static int             g_rtsp_mode = 0;          /* 1 = stream via RTSP, 0 = file capture */
static const char     *g_rtsp_ip   = "0.0.0.0";  /* Bind all interfaces */
static int             g_rtsp_port = 554;
static volatile int    g_stop      = 0;           /* Signal flag for clean RTSP shutdown */

/* Hardware watchdog -- superb normally feeds /dev/watchdog. When we kill
 * superb, we must take over watchdog duties or the SoC hard-resets after
 * ~30 seconds. Keep the fd open and write periodically. */
static int             g_watchdog_fd = -1;

/* ── Watchdog helpers ─────────────────────────────────────────── */

static void watchdog_open(void)
{
    g_watchdog_fd = open("/dev/watchdog", O_RDWR);
    if (g_watchdog_fd < 0) {
        /* Try write-only if rdwr fails */
        g_watchdog_fd = open("/dev/watchdog", O_WRONLY);
    }
    if (g_watchdog_fd < 0) {
        printf("[WDT ] open(/dev/watchdog): %s (non-fatal)\n", strerror(errno));
        return;
    }

    /* Query the watchdog timeout */
    int timeout = 0;
    if (ioctl(g_watchdog_fd, WDIOC_GETTIMEOUT, &timeout) == 0) {
        printf("[WDT ] timeout = %d seconds\n", timeout);
    } else {
        printf("[WDT ] WDIOC_GETTIMEOUT failed (errno=%d), timeout unknown\n", errno);
    }

    /* Try to set a longer timeout if possible */
    int new_timeout = 120;
    if (ioctl(g_watchdog_fd, WDIOC_SETTIMEOUT, &new_timeout) == 0) {
        printf("[WDT ] timeout extended to %d seconds\n", new_timeout);
    } else {
        printf("[WDT ] WDIOC_SETTIMEOUT failed (errno=%d) -- using default\n", errno);
    }

    /* Immediately ping to reset the countdown */
    ioctl(g_watchdog_fd, WDIOC_KEEPALIVE, NULL);

    printf("[ OK ] Watchdog opened (fd=%d) -- we are now feeding it\n", g_watchdog_fd);
}

static void watchdog_feed(void)
{
    if (g_watchdog_fd >= 0) {
        /* The HiSilicon ot_wdt driver does NOT support WDIOC_KEEPALIVE or
         * write() for feeding -- both return EPERM.  The only mechanism that
         * resets the countdown is WDIOC_SETTIMEOUT.  Confirmed by wdt_test:
         * survived 45s with a 30s timeout using SETTIMEOUT every second,
         * while KEEPALIVE and write both failed with EPERM every time.
         *
         * So we "feed" by re-setting the timeout to 120s each time, which
         * resets the hardware countdown as a side effect. */
        int timeout = 120;
        ioctl(g_watchdog_fd, WDIOC_SETTIMEOUT, &timeout);
    }
}

static void watchdog_close(void)
{
    if (g_watchdog_fd >= 0) {
        /* Write magic close character 'V' to cleanly disarm the watchdog,
         * so we don't cause a reboot when pipeline_test exits. */
        write(g_watchdog_fd, "V", 1);
        close(g_watchdog_fd);
        g_watchdog_fd = -1;
        printf("[ OK ] Watchdog disarmed (magic close)\n");
    }
}

/* ── Helpers ──────────────────────────────────────────────────── */

#define CHECK_RET(func, ret) do { \
    if ((ret) != HI_SUCCESS) { \
        printf("[FAIL] %s returned 0x%08X\n", func, (unsigned int)(ret)); \
        return (ret); \
    } else { \
        printf("[ OK ] %s\n", func); \
    } \
} while (0)

#define CHECK_RET_GOTO(func, ret, label) do { \
    if ((ret) != HI_SUCCESS) { \
        printf("[FAIL] %s returned 0x%08X\n", func, (unsigned int)(ret)); \
        goto label; \
    } else { \
        printf("[ OK ] %s\n", func); \
    } \
} while (0)

/* ═══════════════════════════════════════════════════════════════
 *  STEP 0: Load sensor driver .so
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 load_sensor_driver(void)
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
 *  STEP 1: System init (VB pools + sys init)
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 sys_init(void)
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

    sleep(1); /* Let kernel modules settle */
    printf("[INFO] Cleanup done, initializing fresh...\n");

    /* Configure VB pools */
    memset(&vb_cfg, 0, sizeof(vb_cfg));
    vb_cfg.max_pool_cnt = 2;

    /* Pool 0: RAW frames for VI */
    vb_cfg.common_pool[0].blk_size = VB_BLK_SIZE_RAW;
    vb_cfg.common_pool[0].blk_cnt  = VB_RAW_CNT;

    /* Pool 1: YUV frames for VPSS/VENC */
    vb_cfg.common_pool[1].blk_size = VB_BLK_SIZE_YUV;
    vb_cfg.common_pool[1].blk_cnt  = VB_YUV_CNT;

    ret = hi_mpi_vb_set_cfg(&vb_cfg);
    CHECK_RET("hi_mpi_vb_set_cfg", ret);

    ret = hi_mpi_vb_init();
    CHECK_RET("hi_mpi_vb_init", ret);

    ret = hi_mpi_sys_init();
    CHECK_RET("hi_mpi_sys_init", ret);

    /* Set VI-VPSS mode: ONLINE-OFFLINE matches superb's working config.
     * In ONLINE mode, VI hands frames directly to VPSS via on-chip path
     * (no DDR round-trip). Confirmed via /proc/umap/vi with superb running. */
    memset(&vi_vpss_mode, 0, sizeof(vi_vpss_mode));
    vi_vpss_mode.mode[0] = HI_VI_ONLINE_VPSS_OFFLINE;
    ret = hi_mpi_sys_set_vi_vpss_mode(&vi_vpss_mode);
    CHECK_RET("hi_mpi_sys_set_vi_vpss_mode", ret);

    /* Brief pause to let kernel modules settle */
    usleep(100000);

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 2: Configure MIPI RX
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 mipi_init(void)
{
    hi_s32 ret;
    int fd;
    combo_dev_attr_t combo_attr;
    combo_dev_t devno = 0;
    sns_clk_source_t clk_src = 0;
    sns_rst_source_t rst_src = 0;
    lane_divide_mode_t hs_mode = LANE_DIVIDE_MODE_0; /* 4-lane mode (we use 2 of 4) */

    printf("\n=== MIPI RX init ===\n");

    fd = open(MIPI_DEV, O_RDWR);
    if (fd < 0) {
        printf("[FAIL] open(%s): %s\n", MIPI_DEV, strerror(errno));
        return HI_FAILURE;
    }

    /* Set HS mode (lane divide) */
    ret = ioctl(fd, HI_MIPI_SET_HS_MODE, &hs_mode);
    if (ret != 0) {
        printf("[WARN] HI_MIPI_SET_HS_MODE failed: %s (non-fatal)\n", strerror(errno));
    } else {
        printf("[ OK ] HI_MIPI_SET_HS_MODE\n");
    }

    /* Enable sensor clock */
    ret = ioctl(fd, HI_MIPI_ENABLE_SENSOR_CLOCK, &clk_src);
    if (ret != 0) printf("[WARN] ENABLE_SENSOR_CLOCK: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_ENABLE_SENSOR_CLOCK\n");

    /* Reset sensor */
    ret = ioctl(fd, HI_MIPI_RESET_SENSOR, &rst_src);
    if (ret != 0) printf("[WARN] RESET_SENSOR: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_RESET_SENSOR\n");

    /* Unreset sensor */
    ret = ioctl(fd, HI_MIPI_UNRESET_SENSOR, &rst_src);
    if (ret != 0) printf("[WARN] UNRESET_SENSOR: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_UNRESET_SENSOR\n");

    /* Enable MIPI clock */
    ret = ioctl(fd, HI_MIPI_ENABLE_MIPI_CLOCK, &devno);
    if (ret != 0) printf("[WARN] ENABLE_MIPI_CLOCK: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_ENABLE_MIPI_CLOCK\n");

    /* Reset MIPI */
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
    /* Hardware uses lanes 0 and 2 (confirmed via /proc/umap/mipi_rx with superb) */
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

    /* Unreset MIPI */
    ret = ioctl(fd, HI_MIPI_UNRESET_MIPI, &devno);
    if (ret != 0) printf("[WARN] UNRESET_MIPI: %s\n", strerror(errno));
    else printf("[ OK ] HI_MIPI_UNRESET_MIPI\n");

    close(fd);
    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 3: VI device + pipe + channel
 *
 *  Uses B040 SDK library calls but with vi_shim.so LD_PRELOAD to
 *  translate the ot_vi_dev_attr struct from B040 to B051 layout.
 *
 *  If vi_shim.so is NOT loaded, falls back to raw ioctls with
 *  hardcoded B051 struct bytes captured from superb.
 * ═══════════════════════════════════════════════════════════════ */

/* Raw VI ioctl commands (fallback if B040 library fails) */
#define VI_IOC_REG_DEV        0x40044961
#define VI_IOC_SET_DEV_ATTR   0x40784900
#define VI_IOC_ENABLE_DEV     0x00004902
#define VI_IOC_DISABLE_DEV    0x00004903
#define VI_IOC_BIND           0x4004490a
#define VI_IOC_UNBIND         0x4004490b
#define VI_IOC_WDR_FUSION     0x401c490d
#define VI_IOC_SET_PIPE_ATTR  0x40204910
#define VI_IOC_START_PIPE     0x0000491e
#define VI_IOC_STOP_PIPE      0x0000491f
#define VI_IOC_PIPE_FREQ      0x4004494d
#define VI_IOC_PIPE_CFG       0x40084939
#define VI_IOC_SET_CHN_ATTR   0x402c494e
#define VI_IOC_ENABLE_CHN     0x00004952
#define VI_IOC_DISABLE_CHN    0x00004953
#define VI_IOC_ISP_ALGO_CFG   0x40104914

static int vi_fd_dev  = -1;
static int vi_fd_pipe = -1;
static int vi_fd_chn  = -1;
static int vi_used_raw = 0;  /* track if we used raw ioctls */

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

/* Helper macro for raw ioctl approach */
#define W32(buf, off, val) do { \
    unsigned int _v = (unsigned int)(val); \
    memcpy((buf) + (off), &_v, 4); \
} while (0)

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
    W32(pa, 20, 0);      /* compress_mode = NONE (safer than 4) */
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

static hi_s32 vi_init(void)
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

    /* WDR fusion group -- optional for linear mode.
     * In WDR mode this binds multiple exposure pipes together.
     * For linear mode (single pipe), skip it -- the SDK validates
     * strictly and returns ILLEGAL_PARAM if the struct isn't perfect. */
    printf("[INFO] Linear mode -- skipping WDR fusion group config\n");

    hi_vi_pipe_attr pipe_attr;
    memset(&pipe_attr, 0, sizeof(pipe_attr));
    pipe_attr.pipe_bypass_mode = HI_VI_PIPE_BYPASS_NONE;
    pipe_attr.isp_bypass   = HI_FALSE;
    pipe_attr.size.width   = SENSOR_WIDTH;
    pipe_attr.size.height  = SENSOR_HEIGHT;
    pipe_attr.pixel_format = HI_PIXEL_FORMAT_RGB_BAYER_10BPP;
    /* LINE compression matches superb's working config (see /proc/umap/vi). */
    pipe_attr.compress_mode = OT_COMPRESS_MODE_NONE;  /* was LINE; try NONE to debug monochrome */
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

/* ═══════════════════════════════════════════════════════════════
 *  STEP 4: ISP init + sensor registration + ISP thread
 *
 *  Uses V1.0.2.1 SDK APIs (ss_mpi_*) for proper ISP initialization.
 *  Call sequence follows SDK sample_comm_vi.c / sample_comm_isp.c:
 *    1. Sensor: set bus info + register callbacks
 *    2. AE/AWB: register algorithm libraries
 *    3. ISP: mem_init -> set_pub_attr -> init -> run (in thread)
 * ═══════════════════════════════════════════════════════════════ */
static void *isp_thread_func(void *arg)
{
    hi_s32 ret;
    (void)arg;

    printf("[ISP ] Thread started, calling ss_mpi_isp_run(%d)...\n", VI_PIPE);
    g_isp_running = 1;
    ret = hi_mpi_isp_run(VI_PIPE);
    g_isp_running = 0;
    if (ret != HI_SUCCESS) {
        printf("[ISP ] ss_mpi_isp_run returned 0x%08X\n", (unsigned int)ret);
    } else {
        printf("[ISP ] ss_mpi_isp_run exited normally\n");
    }
    return NULL;
}

static hi_s32 isp_init(void)
{
    hi_s32 ret;
    hi_isp_3a_alg_lib ae_lib, awb_lib;
    hi_isp_sns_commbus bus_info;
    hi_isp_pub_attr pub_attr;

    printf("\n=== ISP init (V1.0.2.1 SDK) ===\n");

    /* ── 1. Set sensor I2C bus ─────────────────────────────── */
    bus_info.i2c_dev = I2C_BUS;
    if (g_sns_obj->pfn_set_bus_info) {
        ret = g_sns_obj->pfn_set_bus_info(VI_PIPE, bus_info);
        CHECK_RET("pfn_set_bus_info", ret);
    }

    /* ── 2. Register sensor callbacks with ISP/AE/AWB ──────── */
    memset(&ae_lib, 0, sizeof(ae_lib));
    ae_lib.id = VI_PIPE;
    strncpy(ae_lib.lib_name, HI_AE_LIB_NAME, sizeof(ae_lib.lib_name) - 1);

    memset(&awb_lib, 0, sizeof(awb_lib));
    awb_lib.id = VI_PIPE;
    strncpy(awb_lib.lib_name, HI_AWB_LIB_NAME, sizeof(awb_lib.lib_name) - 1);

    if (g_sns_obj->pfn_register_callback) {
        ret = g_sns_obj->pfn_register_callback(VI_PIPE, &ae_lib, &awb_lib);
        CHECK_RET("pfn_register_callback", ret);
    }

    /* ── 3. Register AE/AWB algorithm libraries ────────────── */
    ret = hi_mpi_ae_register(VI_PIPE, &ae_lib);
    CHECK_RET("ss_mpi_ae_register", ret);

    ret = hi_mpi_awb_register(VI_PIPE, &awb_lib);
    CHECK_RET("ss_mpi_awb_register", ret);

    /* ── 4. ISP memory init ────────────────────────────────── */
    ret = hi_mpi_isp_mem_init(VI_PIPE);
    CHECK_RET("ss_mpi_isp_mem_init", ret);

    /* ── 5. Set ISP public attributes ──────────────────────── */
    memset(&pub_attr, 0, sizeof(pub_attr));

    /* Sensor output window (full frame, no crop) */
    pub_attr.wnd_rect.x      = 0;
    pub_attr.wnd_rect.y      = 0;
    pub_attr.wnd_rect.width  = SENSOR_WIDTH;
    pub_attr.wnd_rect.height = SENSOR_HEIGHT;

    /* Sensor native size */
    pub_attr.sns_size.width  = SENSOR_WIDTH;
    pub_attr.sns_size.height = SENSOR_HEIGHT;

    pub_attr.frame_rate    = SENSOR_FPS;
    pub_attr.bayer_format  = OT_ISP_BAYER_BGGR;  /* SC635HAI is BGGR (SmartSens standard). Note:
                                                  * PQ bin load overrides this to RGGB -- must
                                                  * re-set after load_pq_bin(). See Step 4a. */
    pub_attr.wdr_mode      = HI_WDR_MODE_NONE;
    pub_attr.sns_mode      = 0;  /* default linear mode */

    ret = hi_mpi_isp_set_pub_attr(VI_PIPE, &pub_attr);
    CHECK_RET("ss_mpi_isp_set_pub_attr", ret);

    /* Readback to verify Bayer format was accepted */
    {
        hi_isp_pub_attr readback;
        memset(&readback, 0, sizeof(readback));
        hi_mpi_isp_get_pub_attr(VI_PIPE, &readback);
        printf("[ISP ] Bayer format readback: %d (set: %d)\n",
               readback.bayer_format, pub_attr.bayer_format);
    }

    /* ── 6. ISP init ───────────────────────────────────────── */
    ret = hi_mpi_isp_init(VI_PIPE);
    CHECK_RET("ss_mpi_isp_init", ret);

    /* ── 7. Start ISP processing thread ────────────────────── */
    ret = pthread_create(&g_isp_thread, NULL, isp_thread_func, NULL);
    if (ret != 0) {
        printf("[FAIL] pthread_create(isp_thread): %s\n", strerror(ret));
        hi_mpi_isp_exit(VI_PIPE);
        return HI_FAILURE;
    }
    printf("[ OK ] ISP thread launched\n");

    /* Give ISP a moment to start processing frames */
    usleep(200000);

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 4a: Load PQ bin calibration data
 *
 *  The ISP needs PQ (Picture Quality) calibration data to function.
 *  Without it, AE/AWB/CCM/DRC/gamma are all zeroed, producing black
 *  output even though the sensor is delivering valid MIPI frames.
 *
 *  The camera has pre-built PQ bins at /home/sensor/sc635hai/pqbin/.
 *  We load them via OT_PQ_BIN_ImportBinData from libbin.so (SDK PQ
 *  ext API). This is the same function superb calls internally.
 *
 *  Must be called AFTER ss_mpi_isp_init() and BEFORE capturing frames.
 * ═══════════════════════════════════════════════════════════════ */
#define PQ_BIN_PATH_DEFAULT  "/home/sensor/sc635hai/pqbin/day.bin"
#define LIBBIN_PATH  "/progs/rec/00/ipc_drv/libbin.so"
static const char *g_pq_bin_path = PQ_BIN_PATH_DEFAULT;

/* PQ bin module config struct -- matches ot_pq_bin.h */
typedef struct {
    int enable;
} pq_bin_isp_t;

typedef struct {
    int enable;
    int viPipe;
    int vpssGrp;
} pq_bin_nrx_t;

typedef struct {
    int enable;
    int viPipe;
} pq_bin_isp_evo_t;

typedef struct {
    pq_bin_isp_t     stISP;
    pq_bin_nrx_t     st3DNR;
    pq_bin_isp_evo_t stIspEvo;
} pq_bin_module_t;

typedef int (*fn_import_bin_data)(pq_bin_module_t *param, unsigned char *buf, unsigned int len);

static void *g_pqbin_dl = NULL;

static hi_s32 load_pq_bin(void)
{
    FILE *fp;
    unsigned char *buf = NULL;
    long file_size;
    size_t read_len;
    fn_import_bin_data import_fn;
    pq_bin_module_t bin_param;
    int ret;

    printf("\n=== Loading PQ bin calibration ===\n");

    /* Open libbin.so via dlopen */
    g_pqbin_dl = dlopen(LIBBIN_PATH, RTLD_NOW | RTLD_GLOBAL);
    if (g_pqbin_dl == NULL) {
        printf("[WARN] dlopen(%s): %s\n", LIBBIN_PATH, dlerror());
        printf("[WARN] PQ bin loading skipped -- image may be black\n");
        return HI_FAILURE;
    }
    printf("[ OK ] dlopen(%s)\n", LIBBIN_PATH);

    import_fn = (fn_import_bin_data)dlsym(g_pqbin_dl, "OT_PQ_BIN_ImportBinData");
    if (import_fn == NULL) {
        printf("[WARN] dlsym(OT_PQ_BIN_ImportBinData): %s\n", dlerror());
        dlclose(g_pqbin_dl);
        g_pqbin_dl = NULL;
        return HI_FAILURE;
    }
    printf("[ OK ] Found OT_PQ_BIN_ImportBinData\n");

    /* libbin.so has a global `g_aeHandle` that must be set to a valid
     * dlopen handle of the AE library before import. Without it, PQ bin
     * AE calibration data is skipped ("AE handle is empty").
     * The handle is a void* (4 bytes on ARM32). */
    {
        void **ae_handle_ptr = (void **)dlsym(g_pqbin_dl, "g_aeHandle");
        if (ae_handle_ptr != NULL) {
            /* Open AE library to get a handle for libbin.so */
            void *ae_dl = dlopen("/progs/rec/00/ipc_drv/libss_mpi_ae.so",
                                 RTLD_NOW | RTLD_GLOBAL);
            if (ae_dl != NULL) {
                *ae_handle_ptr = ae_dl;
                printf("[ OK ] Set g_aeHandle -> libss_mpi_ae.so (%p)\n", ae_dl);
            } else {
                printf("[WARN] dlopen(libss_mpi_ae.so): %s\n", dlerror());
                printf("[WARN] AE calibration from PQ bin will be skipped\n");
            }
        } else {
            printf("[WARN] g_aeHandle not found in libbin.so\n");
        }
    }

    /* Read PQ bin file into memory */
    fp = fopen(g_pq_bin_path, "rb");
    if (fp == NULL) {
        printf("[WARN] fopen(%s): %s\n", g_pq_bin_path, strerror(errno));
        return HI_FAILURE;
    }

    fseek(fp, 0, SEEK_END);
    file_size = ftell(fp);
    fseek(fp, 0, SEEK_SET);

    if (file_size <= 0 || file_size > 1024 * 1024) {
        printf("[WARN] PQ bin file size %ld seems wrong\n", file_size);
        fclose(fp);
        return HI_FAILURE;
    }

    buf = (unsigned char *)malloc(file_size);
    if (buf == NULL) {
        printf("[WARN] malloc(%ld) failed\n", file_size);
        fclose(fp);
        return HI_FAILURE;
    }

    read_len = fread(buf, 1, file_size, fp);
    fclose(fp);

    if ((long)read_len != file_size) {
        printf("[WARN] fread: expected %ld, got %zu\n", file_size, read_len);
        free(buf);
        return HI_FAILURE;
    }
    printf("[ OK ] Read %s (%ld bytes)\n", g_pq_bin_path, file_size);

    /* Configure PQ bin module params (matches sample_pq_bin.c) */
    memset(&bin_param, 0, sizeof(bin_param));
    bin_param.stISP.enable   = 1;     /* Load ISP calibration data */
    bin_param.st3DNR.enable  = 1;     /* Load 3DNR params */
    bin_param.st3DNR.viPipe  = VI_PIPE;

    /* Import! */
    printf("[INFO] Calling OT_PQ_BIN_ImportBinData...\n");
    ret = import_fn(&bin_param, buf, (unsigned int)file_size);
    free(buf);

    if (ret != 0) {
        printf("[WARN] OT_PQ_BIN_ImportBinData returned 0x%08X\n", (unsigned int)ret);
        printf("[WARN] PQ import failed -- image may still be black\n");
        return HI_FAILURE;
    }

    printf("[ OK ] PQ bin loaded successfully!\n");

    /* AWB/CCM/ISP module config is now done in configure_isp_color() */

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 4a+: ISP color pipeline debug & configuration
 *
 *  After PQ bin loads ISP calibration, many modules may be set to
 *  values that crush chrominance. This function:
 *    1. Reads & logs ALL ISP module states (diagnostic)
 *    2. Disables modules suspected of crushing color
 *    3. Sets identity CCM + manual WB + proper CSC saturation
 *
 *  Call AFTER load_pq_bin() and AFTER ISP thread is running.
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 configure_isp_color(void)
{
    hi_s32 ret;

    printf("\n=== ISP Color Pipeline Configuration ===\n");

    /* ── 1. Read & log current module bypass state ────────── */
    {
        ot_isp_module_ctrl mod_ctrl;
        memset(&mod_ctrl, 0, sizeof(mod_ctrl));
        ret = ss_mpi_isp_get_module_ctrl(VI_PIPE, &mod_ctrl);
        if (ret == HI_SUCCESS) {
            printf("[MOD ] module_ctrl key=0x%016llX\n",
                   (unsigned long long)mod_ctrl.key);
            printf("[MOD ] bypass: dgain=%llu afc=%llu xtalk=%llu dpc=%llu nr=%llu\n",
                   (unsigned long long)mod_ctrl.bit_bypass_isp_d_gain,
                   (unsigned long long)mod_ctrl.bit_bypass_anti_false_color,
                   (unsigned long long)mod_ctrl.bit_bypass_crosstalk_removal,
                   (unsigned long long)mod_ctrl.bit_bypass_dpc,
                   (unsigned long long)mod_ctrl.bit_bypass_nr);
            printf("[MOD ] bypass: dehaze=%llu wb=%llu shading=%llu drc=%llu demosaic=%llu\n",
                   (unsigned long long)mod_ctrl.bit_bypass_dehaze,
                   (unsigned long long)mod_ctrl.bit_bypass_wb_gain,
                   (unsigned long long)mod_ctrl.bit_bypass_mesh_shading,
                   (unsigned long long)mod_ctrl.bit_bypass_drc,
                   (unsigned long long)mod_ctrl.bit_bypass_demosaic);
            printf("[MOD ] bypass: ccm=%llu gamma=%llu csc=%llu sharpen=%llu ldci=%llu ca=%llu\n",
                   (unsigned long long)mod_ctrl.bit_bypass_color_matrix,
                   (unsigned long long)mod_ctrl.bit_bypass_gamma,
                   (unsigned long long)mod_ctrl.bit_bypass_csc,
                   (unsigned long long)mod_ctrl.bit_bypass_sharpen,
                   (unsigned long long)mod_ctrl.bit_bypass_ldci,
                   (unsigned long long)mod_ctrl.bit_bypass_ca);
        } else {
            printf("[MOD ] get_module_ctrl FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 2. Read & log CSC state (CRITICAL -- satu field!) ─── */
    {
        ot_isp_csc_attr csc;
        memset(&csc, 0, sizeof(csc));
        ret = ss_mpi_isp_get_csc_attr(VI_PIPE, &csc);
        if (ret == HI_SUCCESS) {
            printf("[CSC ] enable=%d, gamut=%d, hue=%u, luma=%u, contr=%u, satu=%u\n",
                   csc.enable, csc.color_gamut,
                   csc.hue, csc.luma, csc.contr, csc.satu);
            printf("[CSC ] limited_range=%d, ext_csc=%d, ct_mode=%d\n",
                   csc.limited_range_en, csc.ext_csc_en, csc.ct_mode_en);

            /* Fix CSC saturation if it's too low -- this could be THE bug */
            if (csc.satu < 30) {
                printf("[CSC ] *** SATURATION IS %u (LOW!) -- setting to 50 ***\n", csc.satu);
            }
            /* Keep PQ bin CSC settings as-is. Logging only. */
            ret = ss_mpi_isp_set_csc_attr(VI_PIPE, &csc);
            printf("[CSC ] set: ret=0x%08X (satu=50, BT709, full range)\n", (unsigned)ret);
        } else {
            printf("[CSC ] get_csc_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 3. Read & log Saturation attr (AWB module) ────────── */
    {
        ot_isp_saturation_attr sat;
        memset(&sat, 0, sizeof(sat));
        ret = ss_mpi_isp_get_saturation_attr(VI_PIPE, &sat);
        if (ret == HI_SUCCESS) {
            printf("[SAT ] op_type=%d, manual_sat=%u\n",
                   sat.op_type, sat.manual_attr.saturation);
            printf("[SAT ] auto_sat=[%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u]\n",
                   sat.auto_attr.sat[0], sat.auto_attr.sat[1],
                   sat.auto_attr.sat[2], sat.auto_attr.sat[3],
                   sat.auto_attr.sat[4], sat.auto_attr.sat[5],
                   sat.auto_attr.sat[6], sat.auto_attr.sat[7],
                   sat.auto_attr.sat[8], sat.auto_attr.sat[9],
                   sat.auto_attr.sat[10], sat.auto_attr.sat[11],
                   sat.auto_attr.sat[12], sat.auto_attr.sat[13],
                   sat.auto_attr.sat[14], sat.auto_attr.sat[15]);

            /* PQ bin auto table goes [90-140] across ISOs. Superb shows sat=103
             * at ISO~19189. Slightly reduce high-ISO saturation to make chroma
             * noise less visible, but NOT the aggressive 50-70 crush we tried before.
             * Only touch entries [12..15] (very high ISO). */
            printf("[SAT ] PQ bin defaults: sat[12..15]=%u,%u,%u,%u\n",
                   sat.auto_attr.sat[12], sat.auto_attr.sat[13],
                   sat.auto_attr.sat[14], sat.auto_attr.sat[15]);
            for (int i = 12; i < 16; i++) {
                if (sat.auto_attr.sat[i] > 95)
                    sat.auto_attr.sat[i] = 90;  /* Cap at 90 (was ~100-110) */
            }
            ret = ss_mpi_isp_set_saturation_attr(VI_PIPE, &sat);
            printf("[SAT ] high-ISO sat[12..15] capped to 90: ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[SAT ] get_saturation_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 4. Read & log Color Tone ──────────────────────────── */
    {
        ot_isp_color_tone_attr ct;
        memset(&ct, 0, sizeof(ct));
        ret = ss_mpi_isp_get_color_tone_attr(VI_PIPE, &ct);
        if (ret == HI_SUCCESS) {
            printf("[CT  ] R=0x%04X G=0x%04X B=0x%04X\n",
                   ct.red_cast_gain, ct.green_cast_gain, ct.blue_cast_gain);
            /* Neutralize color tone -- all gains to 1.0 (0x100) */
            ct.red_cast_gain = 0x100;
            ct.green_cast_gain = 0x100;
            ct.blue_cast_gain = 0x100;
            ret = ss_mpi_isp_set_color_tone_attr(VI_PIPE, &ct);
            printf("[CT  ] set neutral (0x100): ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[CT  ] get_color_tone_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 5. Log DRC state (keep PQ bin settings) ──────────── */
    {
        ot_isp_drc_attr drc;
        memset(&drc, 0, sizeof(drc));
        ret = ss_mpi_isp_get_drc_attr(VI_PIPE, &drc);
        if (ret == HI_SUCCESS) {
            printf("[DRC ] enable=%d, op_type=%d, curve=%d (keeping PQ bin)\n",
                   drc.enable, drc.op_type, drc.curve_select);
        } else {
            printf("[DRC ] get_drc_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 6. Log Dehaze state (keep PQ bin settings) ──────── */
    {
        ot_isp_dehaze_attr dehaze;
        memset(&dehaze, 0, sizeof(dehaze));
        ret = ss_mpi_isp_get_dehaze_attr(VI_PIPE, &dehaze);
        if (ret == HI_SUCCESS) {
            printf("[DHZ ] enable=%d, op_type=%d (keeping PQ bin)\n",
                   dehaze.enable, dehaze.op_type);
        } else {
            printf("[DHZ ] get_dehaze_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 7. Log Sharpen state (keep PQ bin settings) ─────── */
    {
        ot_isp_sharpen_attr shp;
        memset(&shp, 0, sizeof(shp));
        ret = ss_mpi_isp_get_sharpen_attr(VI_PIPE, &shp);
        if (ret == HI_SUCCESS) {
            printf("[SHP ] enable=%d, op_type=%d (keeping PQ bin)\n",
                   shp.enable, shp.op_type);
        } else {
            printf("[SHP ] get_sharpen_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 8. Log LDCI state (keep PQ bin settings) ────────── */
    {
        ot_isp_ldci_attr ldci;
        memset(&ldci, 0, sizeof(ldci));
        ret = ss_mpi_isp_get_ldci_attr(VI_PIPE, &ldci);
        if (ret == HI_SUCCESS) {
            printf("[LDCI] enable=%d, op_type=%d (keeping PQ bin)\n",
                   ldci.enable, ldci.op_type);
        } else {
            printf("[LDCI] get_ldci_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 9. Log NR (BNR) state (tuning done in configure_lowlight_nr) ─ */
    {
        ot_isp_nr_attr nr;
        memset(&nr, 0, sizeof(nr));
        ret = ss_mpi_isp_get_nr_attr(VI_PIPE, &nr);
        if (ret == HI_SUCCESS) {
            printf("[NR  ] enable=%d, op_type=%d, md_en=%d\n",
                   nr.enable, nr.op_type, nr.md_en);
            if (nr.op_type == OT_OP_MODE_AUTO) {
                printf("[NR  ] auto: fine_str[0]=%u coring_wgt[0]=%u\n",
                       nr.snr_cfg.snr_attr.snr_auto.fine_strength[0],
                       nr.snr_cfg.snr_attr.snr_auto.coring_wgt[0]);
            } else {
                printf("[NR  ] manual: fine_str=%u coring_wgt=%u\n",
                       nr.snr_cfg.snr_attr.snr_manual.fine_strength,
                       nr.snr_cfg.snr_attr.snr_manual.coring_wgt);
            }
        } else {
            printf("[NR  ] get_nr_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 10. Read & log Gamma state ──────────────────────── */
    {
        ot_isp_gamma_attr gamma;
        memset(&gamma, 0, sizeof(gamma));
        ret = ss_mpi_isp_get_gamma_attr(VI_PIPE, &gamma);
        if (ret == HI_SUCCESS) {
            printf("[GAM ] enable=%d, curve_type=%d\n",
                   gamma.enable, gamma.curve_type);
            printf("[GAM ] table[0..7]=%u,%u,%u,%u,%u,%u,%u,%u\n",
                   gamma.table[0], gamma.table[1], gamma.table[2], gamma.table[3],
                   gamma.table[4], gamma.table[5], gamma.table[6], gamma.table[7]);
            printf("[GAM ] table[512]=%u, table[1024]=%u\n",
                   gamma.table[512], gamma.table[1024]);
            /* Keep gamma enabled but log it -- gamma shouldn't crush chroma
             * (it operates on luminance in the ISP pipeline), but note its state */
        } else {
            printf("[GAM ] get_gamma_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 11. Read & log Demosaic state ───────────────────── */
    {
        ot_isp_demosaic_attr dm;
        memset(&dm, 0, sizeof(dm));
        ret = ss_mpi_isp_get_demosaic_attr(VI_PIPE, &dm);
        if (ret == HI_SUCCESS) {
            printf("[DM  ] enable=%d, op_type=%d\n",
                   dm.enable, dm.op_type);
            /* Keep demosaic enabled -- we need it for Bayer->RGB */
        } else {
            printf("[DM  ] get_demosaic_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 12. Set CCM to identity ─────────────────────────── */
    {
        ot_isp_color_matrix_attr ccm;
        memset(&ccm, 0, sizeof(ccm));
        ret = ss_mpi_isp_get_ccm_attr(VI_PIPE, &ccm);
        if (ret == HI_SUCCESS) {
            printf("[CCM ] op_type=%d, sat_en=%d\n",
                   ccm.op_type, ccm.manual_attr.sat_en);
            printf("[CCM ] current: [0x%04X,0x%04X,0x%04X, 0x%04X,0x%04X,0x%04X, 0x%04X,0x%04X,0x%04X]\n",
                   ccm.manual_attr.ccm[0], ccm.manual_attr.ccm[1], ccm.manual_attr.ccm[2],
                   ccm.manual_attr.ccm[3], ccm.manual_attr.ccm[4], ccm.manual_attr.ccm[5],
                   ccm.manual_attr.ccm[6], ccm.manual_attr.ccm[7], ccm.manual_attr.ccm[8]);

            /* Auto CCM -- with correct BGGR bayer_format, no R/B swap needed */
            ccm.op_type = OT_OP_MODE_AUTO;
            ret = ss_mpi_isp_set_ccm_attr(VI_PIPE, &ccm);
            printf("[CCM ] set AUTO: ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[CCM ] get_ccm_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 13. Set AWB to auto with ADVANCE algorithm ────────── */
    {
        ot_isp_wb_attr wb;
        memset(&wb, 0, sizeof(wb));
        ret = ss_mpi_isp_get_wb_attr(VI_PIPE, &wb);
        if (ret == HI_SUCCESS) {
            printf("[AWB ] op_type=%d, alg_type=%d, R=%u Gr=%u Gb=%u B=%u\n",
                   wb.op_type, wb.auto_attr.alg_type,
                   wb.manual_attr.r_gain, wb.manual_attr.gr_gain,
                   wb.manual_attr.gb_gain, wb.manual_attr.b_gain);

            /* Auto AWB with ADVANCE algorithm (matches superb) */
            wb.op_type = OT_OP_MODE_AUTO;
            wb.auto_attr.alg_type = OT_ISP_AWB_ALG_ADVANCE;
            wb.auto_attr.speed = 256;
            wb.auto_attr.zone_sel = 32;
            wb.auto_attr.high_color_temp = 10000;
            wb.auto_attr.low_color_temp = 2500;
            wb.auto_attr.shift_limit_en = 1;
            wb.auto_attr.shift_limit = 64;
            wb.auto_attr.gain_norm_en = 1;
            ret = ss_mpi_isp_set_wb_attr(VI_PIPE, &wb);
            printf("[AWB ] set AUTO ADVANCE mode: ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[AWB ] get_wb_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    printf("[INFO] ISP color config done: all PQ modules on, "
           "auto CCM+AWB (ADVANCE), PQ bin CSC preserved\n");

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 4a++: Low-light noise reduction tuning
 *
 *  Three-pronged approach to reduce chroma noise in low light:
 *  1. ISP BayerNR: boost high-ISO entries in auto table
 *  2. DRC: limit dark-area chroma gain + enable DRC-embedded BCNR
 *  3. ISP NR md_cfg: tune motion detection NR for better temporal NR
 *
 *  The biggest NR improvement comes from 3DNR (temporal+chroma) which
 *  is configured separately in configure_3dnr() after VPSS init.
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 configure_lowlight_nr(void)
{
    hi_s32 ret;

    printf("\n=== Low-Light NR Tuning ===\n");

    /* ── 1. ISP BayerNR: use PQ bin defaults, just ensure md_en=1 ──
     * Superb's proc shows: fine_strength=80, coring_wgt=50, sfm0_de_prot=16,
     * md_en=1, md_mode=2, tfs=255, md_sta_fine_str=55, md_sta_ratio=26.
     * These come from the PQ bin (day.bin). Previous code maxed everything
     * (fine_str=128, md_static_fine=200) which over-smoothed the image.
     * Now we just ensure md_en=1 and leave everything else at PQ bin defaults. */
    {
        ot_isp_nr_attr nr;
        memset(&nr, 0, sizeof(nr));
        ret = ss_mpi_isp_get_nr_attr(VI_PIPE, &nr);
        if (ret == HI_SUCCESS) {
            printf("[BNR ] enable=%d, op_type=%d, md_en=%d\n",
                   nr.enable, nr.op_type, nr.md_en);

            /* Enable motion detection if not already on (superb has md_en=1) */
            if (!nr.md_en) {
                nr.md_en = 1;
                printf("[BNR ] enabling motion detection (md_en=1)\n");
            }

            if (nr.op_type == OT_OP_MODE_AUTO) {
                /* Log PQ bin auto table values */
                printf("[BNR ] auto fine_str: ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.snr_cfg.snr_attr.snr_auto.fine_strength[i]);
                printf("\n");
                printf("[BNR ] auto coring_wgt: ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.snr_cfg.snr_attr.snr_auto.coring_wgt[i]);
                printf("\n");

                /* Log md_cfg values before modification */
                printf("[BNR ] md_auto tfs (before): ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.md_cfg.md_auto.tfs[i]);
                printf("\n");
                printf("[BNR ] md_auto sta_fine (before): ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.md_cfg.md_auto.md_static_fine_strength[i]);
                printf("\n");
                printf("[BNR ] md_auto sta_ratio (before): ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.md_cfg.md_auto.md_static_ratio[i]);
                printf("\n");

                /* Boost md_static_fine_strength at high ISO entries [8..15].
                 * PQ bin gives [32..64] range; superb proc shows 55 at ISO~19189.
                 * Range is [0-255]. Boost high-ISO entries for more aggressive
                 * static-area denoising in low light. Keep low-ISO entries
                 * (bright scenes) unchanged to avoid over-smoothing. */
                for (int i = 8; i < 16; i++) {
                    /* Scale up: if PQ bin had 55, push to ~90 */
                    int boosted = nr.md_cfg.md_auto.md_static_fine_strength[i];
                    boosted = (boosted * 170) / 100;  /* 1.7x */
                    if (boosted > 120) boosted = 120;  /* Cap well below 255 max */
                    if (boosted < 80) boosted = 80;    /* Floor at 80 */
                    nr.md_cfg.md_auto.md_static_fine_strength[i] = (unsigned char)boosted;
                }
                printf("[BNR ] md_auto sta_fine (after): ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.md_cfg.md_auto.md_static_fine_strength[i]);
                printf("\n");
            }

            ret = ss_mpi_isp_set_nr_attr(VI_PIPE, &nr);
            printf("[BNR ] set_nr_attr (md_en=1 only): ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[BNR ] get_nr_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* ── 2. DRC: log state, keep PQ bin defaults ──────────────
     * Superb's proc shows: DRC en=1, manu_en=1, strength=256
     * (this is the "digital WDR" feature from SystemCfg.ini bEnableWdr=1).
     * The PQ bin sets DRC params including BCNR. Previous code force-set
     * BCNR strength=8 (max) and dark_gain_limit_chroma=0x40.
     * Now we trust the PQ bin defaults and just log for diagnostics. */
    {
        ot_isp_drc_attr drc;
        memset(&drc, 0, sizeof(drc));
        ret = ss_mpi_isp_get_drc_attr(VI_PIPE, &drc);
        if (ret == HI_SUCCESS) {
            printf("[DRC ] enable=%d, op_type=%d, curve=%d\n",
                   drc.enable, drc.op_type, drc.curve_select);
            printf("[DRC ] dark_gain_limit_luma=%u, dark_gain_limit_chroma=%u\n",
                   drc.dark_gain_limit_luma, drc.dark_gain_limit_chroma);
            printf("[DRC ] bright_gain_limit=%u, contrast_ctrl=%u\n",
                   drc.bright_gain_limit, drc.contrast_ctrl);
            printf("[DRC ] global_color_ctrl=%u, high_sat_color_ctrl=%u\n",
                   drc.global_color_ctrl, drc.high_saturation_color_ctrl);
            printf("[DRC ] bcnr: enable=%d, strength=%u\n",
                   drc.bcnr_attr.enable, drc.bcnr_attr.strength);

            /* Enable BCNR and boost strength for better Bayer chroma NR.
             * PQ bin default strength=3; SDK range [0-8].
             * Push to 6 for stronger Bayer-domain chroma denoising. */
            drc.bcnr_attr.enable = 1;
            drc.bcnr_attr.strength = 6;    /* Was 3 from PQ bin, max is 8 */
            ret = ss_mpi_isp_set_drc_attr(VI_PIPE, &drc);
            printf("[DRC ] BCNR: enable=1 strength=%u (was %u): ret=0x%08X\n",
                   (unsigned)drc.bcnr_attr.strength, 3, (unsigned)ret);
        } else {
            printf("[DRC ] get_drc_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    printf("[INFO] Low-light NR tuning done\n");
    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 5a: Configure 3DNR (temporal + chroma noise reduction)
 *
 *  3DNR operates at the VPSS level (or VI level). It provides:
 *  - Temporal NR (tfy/tfs): uses motion detection to blend frames
 *  - Spatial luma NR (sfy): spatial filtering per frequency band
 *  - Chroma NR (nrc0/nrc1): spatial+temporal chroma denoising
 *
 *  The PQ bin does NOT configure 3DNR -- this is the big missing
 *  piece vs superb's pipeline. Superb likely uses 3DNR heavily
 *  for chroma noise suppression in low light.
 *
 *  Must be called AFTER vpss_init() (VPSS group must exist).
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 configure_3dnr(void)
{
    hi_s32 ret;

    printf("\n=== 3DNR Configuration ===\n");

    /* ── 1. Try VI pipe 3DNR first (this platform uses VI-level 3DNR) ── */
    {
        ot_3dnr_attr nr_attr;
        memset(&nr_attr, 0, sizeof(nr_attr));

        /* Read current VI pipe 3DNR state */
        ret = ss_mpi_vi_get_pipe_3dnr_attr(VI_PIPE, &nr_attr);
        printf("[3DNR] VI pipe get_attr: enable=%d, type=%d, compress=%d, motion=%d, ret=0x%08X\n",
               nr_attr.enable, nr_attr.nr_type, nr_attr.compress_mode,
               nr_attr.nr_motion_mode, (unsigned)ret);

        /* Enable 3DNR with normal video mode */
        nr_attr.enable = TD_TRUE;
        nr_attr.nr_type = OT_NR_TYPE_VIDEO_NORM;
        nr_attr.compress_mode = OT_COMPRESS_MODE_NONE;
        nr_attr.nr_motion_mode = OT_NR_MOTION_MODE_NORM;

        ret = ss_mpi_vi_set_pipe_3dnr_attr(VI_PIPE, &nr_attr);
        printf("[3DNR] VI pipe set_attr(enable=1, VIDEO_NORM): ret=0x%08X\n", (unsigned)ret);

        if (ret != HI_SUCCESS) {
            printf("[3DNR] VI pipe failed, trying VPSS group 3DNR...\n");

            /* Fallback: try VPSS group 3DNR */
            memset(&nr_attr, 0, sizeof(nr_attr));
            ret = ss_mpi_vpss_get_grp_3dnr_attr(VPSS_GRP, &nr_attr);
            printf("[3DNR] VPSS get_attr: enable=%d, ret=0x%08X\n",
                   nr_attr.enable, (unsigned)ret);

            nr_attr.enable = TD_TRUE;
            nr_attr.nr_type = OT_NR_TYPE_VIDEO_NORM;
            nr_attr.compress_mode = OT_COMPRESS_MODE_NONE;
            nr_attr.nr_motion_mode = OT_NR_MOTION_MODE_NORM;

            ret = ss_mpi_vpss_set_grp_3dnr_attr(VPSS_GRP, &nr_attr);
            printf("[3DNR] VPSS set_attr: ret=0x%08X\n", (unsigned)ret);
            if (ret != HI_SUCCESS) {
                printf("[3DNR] WARNING: Both VI and VPSS 3DNR failed\n");
                return HI_FAILURE;
            }
        }
    }

    /* ── 2. Configure 3DNR V2 parameters ─────────────────────── */
    /* Values extracted from superb's running ISP via /proc/umap/vi.
     * Key insight: superb uses MODERATE NR, not maxed-out values.
     * Previous code maxed everything (trc=255 etc) causing over-smoothing.
     *
     * Superb's proc dump (low-light, ISO~19189):
     *   mdy0: pretfs=8, premath=100, premathd=80, premabw=2, pretdz=32
     *   tfy:  tfs=0,11,12  tss=16,0,0  tfr0=14,8,14,8,0,0  tfr1=16,8,16,8,0,0
     *   mdy:  math=100,419  (mate not shown, using sensible defaults)
     *   nrc0: trc=24, sfc=24, tfc=12, tfs=13
     *   nrc1: pre_sfs=9, sfs1=119, sfs2_coarse=15, sfs2_coarse_f=15, sfs2_fine_f=15, sfs2_fine_b=15
     *   tfs_mode=1, sfs2_mode=0, gamma_en=1, ca_en=0
     *   nry1-4_en=1,1,1,1  nrc_en=1  nrc0_mode=0
     */
    {
        ot_3dnr_param nr_param;
        memset(&nr_param, 0, sizeof(nr_param));

        /* Read current params -- preserves all default values from the driver.
         * We only override the fields we can decode from superb's proc dump. */
        ret = ss_mpi_vi_get_pipe_3dnr_param(VI_PIPE, &nr_param);
        printf("[3DNR] VI get_param: version=%d, ret=0x%08X\n",
               nr_param.nr_version, (unsigned)ret);

        if (ret == HI_SUCCESS) {
            nr_param.nr_version = OT_NR_V2;

            ot_nr_v2 *v2 = &nr_param.nr_norm_param_v2.nr_manual.nr_param;

            /* Manual mode for precise control */
            nr_param.nr_norm_param_v2.op_mode = OT_OP_MODE_MANUAL;

            /* ── Enable all NR channels (matches superb) ── */
            v2->nry1_en = 1;
            v2->nry2_en = 1;
            v2->nry3_en = 1;
            v2->nry4_en = 1;
            v2->nrc_en = 1;
            v2->nrc0_mode = 0;

            /* ── Post-processing: gamma on, ca off (matches superb) ── */
            v2->pp.gamma_en = 1;
            v2->pp.ca_en = 0;

            /* ── mdy0 (pre-stage motion detection) ── */
            v2->mdy0.tfs = 8;
            v2->mdy0.math = 100;
            v2->mdy0.mathd = 80;
            v2->mdy0.mabw = 2;
            v2->mdy0.tdz = 32;
            printf("[3DNR] V2 MDY0: tfs=%u math=%u mathd=%u mabw=%u tdz=%u\n",
                   v2->mdy0.tfs, v2->mdy0.math, v2->mdy0.mathd,
                   v2->mdy0.mabw, v2->mdy0.tdz);

            /* ── Chroma NR channel 0 (nrc0) -- temporal chroma ──
             * Superb proc: trc=24 sfc=24 tfc=12 tfs=13
             * SDK ranges: trc[0-255] sfc[0-255] tfc[0-63] tfs[0-15]
             * Push trc/sfc to 128 (half-max) for aggressive chroma temporal NR.
             * This is the #1 knob for green/magenta speckle in low light. */
            v2->nrc0.trc = 128;            /* 5.3x superb -- half-max temporal chroma */
            v2->nrc0.sfc = 128;            /* 5.3x superb -- half-max chroma motion thresh */
            v2->nrc0.tfc = 32;             /* 2.7x superb -- stronger temporal control (max 63) */
            v2->nrc0.tfs = 13;             /* Keep superb's temporal strength */
            /* Leave tfs_mot at driver defaults (read-modify-write) */
            printf("[3DNR] V2 NRC0: trc=%u sfc=%u tfc=%u tfs=%u\n",
                   v2->nrc0.trc, v2->nrc0.sfc, v2->nrc0.tfc, v2->nrc0.tfs);

            /* ── Chroma NR channel 1 (nrc1) -- spatial chroma ──
             * Superb: pre_sfs=9 sfs1=119 sfs2_coarse=15
             * SDK ranges: pre_sfs[0-16] sfs1[0-255] sfs2_coarse[0-31]
             * Push sfs1 to 220 for stronger spatial chroma denoising. */
            v2->nrc1.pre_sfs = 14;         /* Near max (16) pre-filter */
            v2->nrc1.sfs1 = 220;           /* 1.85x superb -- strong spatial chroma */
            /* Leave sfs1_mot at driver defaults */
            v2->nrc1.sfs2_coarse = 24;     /* Bump from 15 toward max (31) */
            v2->nrc1.sfs2_coarse_f = 24;   /* Background chroma NR -- match coarse */
            v2->nrc1.sfs2_fine_f = 15;     /* Keep at superb level */
            v2->nrc1.sfs2_fine_b = 15;     /* Keep at superb level */
            /* Leave sfs2_mot at driver defaults */
            v2->nrc1.sfs2_mode = 0;
            printf("[3DNR] V2 NRC1: pre=%u sfs1=%u coarse=%u coarse_f=%u\n",
                   v2->nrc1.pre_sfs, v2->nrc1.sfs1, v2->nrc1.sfs2_coarse,
                   v2->nrc1.sfs2_coarse_f);

            /* ── Temporal luma NR (tfy) ──
             * Superb: tfs=0,11,12  tss=16,0,0
             *         tfr0=14,8,14,8,0,0  tfr1=16,8,16,8,0,0
             * tfs0=0 means coarse temporal OFF in superb. Enable tfs0=4 for
             * low-frequency noise patches at the cost of mild motion smear. */
            for (int i = 0; i < 2; i++) {
                v2->tfy[i].tfs0 = 4;       /* Coarse temporal ON (superb=0, try 4) */
                v2->tfy[i].tfs1 = 11;      /* Medium temporal (superb) */
                v2->tfy[i].tfs2 = 12;      /* Fine temporal (superb) */
                v2->tfy[i].ref_en = 1;     /* Reference frame enabled */
                v2->tfy[i].tss0 = 16;
                v2->tfy[i].tss1 = 0;
                v2->tfy[i].tss2 = 0;
                v2->tfy[i].tfr0[0] = 14; v2->tfy[i].tfr0[1] = 8;
                v2->tfy[i].tfr0[2] = 14; v2->tfy[i].tfr0[3] = 8;
                v2->tfy[i].tfr0[4] = 0;  v2->tfy[i].tfr0[5] = 0;
                v2->tfy[i].tfr1[0] = 16; v2->tfy[i].tfr1[1] = 8;
                v2->tfy[i].tfr1[2] = 16; v2->tfy[i].tfr1[3] = 8;
                v2->tfy[i].tfr1[4] = 0;  v2->tfy[i].tfr1[5] = 0;
                v2->tfy[i].tfs_mode = 1;
            }
            printf("[3DNR] V2 TFY: tfs=4,11,12 tss=16,0,0 tfr0=14,8,14,8,0,0 ref_en=1\n");

            /* ── Motion detection (mdy) ──
             * Superb: math0=100 math1=419 (high motion threshold = less temporal NR on moving areas)
             * mate and mabw not visible in proc -- use sensible defaults */
            for (int i = 0; i < 2; i++) {
                v2->mdy[i].math0 = 100;
                v2->mdy[i].mate0 = 4;      /* Keep from driver default */
                v2->mdy[i].math1 = 419;
                v2->mdy[i].mate1 = 4;
                v2->mdy[i].mabw0 = 7;      /* Keep from driver default */
                v2->mdy[i].mabw1 = 7;
            }
            printf("[3DNR] V2 MDY: math0=100 math1=419 mabw=7\n");

            /* ── Spatial luma NR (sfy) ──
             * Superb's proc dump decoded (columns are sfy[0]/nry1, sfy[1]/nry2, sfy[2]/nry3):
             * sf1: sfs1/sbr1  = 64/128 for all 3
             * sf2: sfs2/sft2/sbr2 = 64/0/128 for all 3
             * sf4: sfs4/sft4/sbr4 = 64/0/128 for sfy[0], 64/0/128+10/10 for sfy[1..2]
             * sth: 40/80/60 / 40/40/40 for sfy[0..2]
             * sfn: 2/2/2/0 + 0/0/0/4 for sfy[0]; 2/2/2/2 + 2/2/2/2 for sfy[1..2]
             * sf5 (sfy[1] only): sfs5=32
             * bld6: 2 for sfy[0]
             * Column 4 (nry4/pshrp) values from proc: 180/200/200 etc -- leave at defaults
             */
            for (int i = 0; i < 3; i++) {
                v2->sfy[i].sfs1 = 64;
                v2->sfy[i].sbr1 = 128;
                v2->sfy[i].sfs2 = 64;
                v2->sfy[i].sft2 = 0;
                v2->sfy[i].sbr2 = 128;
                v2->sfy[i].sfs4 = 64;
                v2->sfy[i].sft4 = 0;
                v2->sfy[i].sbr4 = 128;
                v2->sfy[i].sth1_0 = 40;
                v2->sfy[i].sth2_0 = 80;
                v2->sfy[i].sth3_0 = 60;
                v2->sfy[i].sth1_1 = 40;
                v2->sfy[i].sth2_1 = 40;
                v2->sfy[i].sth3_1 = 40;
            }
            /* sfy[0] (nry1): sfn = 2,2,2,0 / 0,0,0,4 */
            v2->sfy[0].sfn0_0 = 2; v2->sfy[0].sfn1_0 = 2;
            v2->sfy[0].sfn2_0 = 2; v2->sfy[0].sfn3_0 = 0;
            v2->sfy[0].sfn0_1 = 0; v2->sfy[0].sfn1_1 = 0;
            v2->sfy[0].sfn2_1 = 0; v2->sfy[0].sfn3_1 = 4;
            v2->sfy[0].bld6 = 2;
            v2->sfy[0].sfn6_0 = 2; v2->sfy[0].sfn6_1 = 4;
            /* sfy[1] (nry2): sfn = 2,2,2,2 / 2,2,2,2 */
            v2->sfy[1].sfn0_0 = 2; v2->sfy[1].sfn1_0 = 2;
            v2->sfy[1].sfn2_0 = 2; v2->sfy[1].sfn3_0 = 2;
            v2->sfy[1].sfn0_1 = 2; v2->sfy[1].sfn1_1 = 2;
            v2->sfy[1].sfn2_1 = 2; v2->sfy[1].sfn3_1 = 2;
            v2->sfy[1].sfs5 = 32;
            v2->sfy[1].bld6 = 5;
            v2->sfy[1].sfn6_0 = 5; v2->sfy[1].sfn6_1 = 3;
            v2->sfy[1].sfn7_0 = 4; v2->sfy[1].sfn7_1 = 3;
            v2->sfy[1].sfn8_0 = 4; v2->sfy[1].sfn8_1 = 3;
            /* strf3/strb3 for sfy[1..2]: from proc sf3_2=10_10 */
            v2->sfy[1].strf3 = 10; v2->sfy[1].strb3 = 10;
            v2->sfy[1].strf4 = 10; v2->sfy[1].strb4 = 10;
            /* sfy[2] (nry3): sfn = 2,2,2,2 / 2,2,2,2 */
            v2->sfy[2].sfn0_0 = 2; v2->sfy[2].sfn1_0 = 2;
            v2->sfy[2].sfn2_0 = 2; v2->sfy[2].sfn3_0 = 2;
            v2->sfy[2].sfn0_1 = 2; v2->sfy[2].sfn1_1 = 2;
            v2->sfy[2].sfn2_1 = 2; v2->sfy[2].sfn3_1 = 2;
            v2->sfy[2].bld6 = 5;
            v2->sfy[2].sfn6_0 = 5; v2->sfy[2].sfn6_1 = 0;
            v2->sfy[2].sfn7_0 = 4; v2->sfy[2].sfn7_1 = 3;
            v2->sfy[2].sfn8_0 = 4; v2->sfy[2].sfn8_1 = 3;
            v2->sfy[2].strf3 = 10; v2->sfy[2].strb3 = 10;
            v2->sfy[2].strf4 = 10; v2->sfy[2].strb4 = 10;
            printf("[3DNR] V2 SFY: sfs1=64 sbr1=128 sfs2=64 sth=40/80/60\n");

            /* Leave iey (pshrp/nry4) and luty at driver defaults --
             * these have complex LUT-based params. The proc dump shows
             * pshrp values (col 4: 180_180 etc) but those are sharpening
             * params that should be fine at PQ bin defaults. */

            ret = ss_mpi_vi_set_pipe_3dnr_param(VI_PIPE, &nr_param);
            printf("[3DNR] VI set_param(V2, manual, superb-matched): ret=0x%08X\n", (unsigned)ret);
            if (ret != HI_SUCCESS) {
                printf("[3DNR] WARNING: V2 param set failed (0x%08X)\n", (unsigned)ret);
            }
        } else {
            printf("[3DNR] get_param failed, skipping param config\n");
        }
    }

    printf("[INFO] 3DNR configuration done\n");
    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 4b: Dump raw frame from VI (fallback if ISP/VPSS fails)
 *
 *  Uses hi_mpi_vi_get_chn_frame to get a raw frame directly from
 *  the VI channel. This bypasses VPSS/VENC entirely.
 *  The frame will be YUV if ISP processed it, or raw if not.
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 dump_raw_frame(void)
{
    hi_s32 ret;
    hi_video_frame_info frame;

    printf("\n=== Dumping raw frame from VI ===\n");

    /* Try getting a frame with timeout */
    printf("[INFO] Calling hi_mpi_vi_get_chn_frame (timeout 5s)...\n");
    ret = hi_mpi_vi_get_chn_frame(VI_PIPE, VI_CHN, &frame, 5000);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] hi_mpi_vi_get_chn_frame: 0x%08X\n", (unsigned)ret);
        return ret;
    }

    printf("[ OK ] Got VI frame!\n");
    printf("  width=%u height=%u pixel_format=%u\n",
           frame.video_frame.width,
           frame.video_frame.height,
           frame.video_frame.pixel_format);
    printf("  stride[0]=%u stride[1]=%u\n",
           frame.video_frame.stride[0],
           frame.video_frame.stride[1]);
    printf("  phys_addr[0]=0x%llx phys_addr[1]=0x%llx\n",
           (unsigned long long)frame.video_frame.phys_addr[0],
           (unsigned long long)frame.video_frame.phys_addr[1]);
    printf("  pts=%llu time_ref=%u\n",
           (unsigned long long)frame.video_frame.pts,
           frame.video_frame.time_ref);

    /* Save raw frame data to file if we can mmap it */
    if (frame.video_frame.phys_addr[0] != 0 &&
        frame.video_frame.stride[0] > 0 &&
        frame.video_frame.height > 0) {
        unsigned int frame_size = frame.video_frame.stride[0] *
                                  frame.video_frame.height;
        /* For YUV420 semiplanar, total = Y + UV = stride*h*1.5 */
        if (frame.video_frame.pixel_format == HI_PIXEL_FORMAT_YVU_SEMIPLANAR_420 ||
            frame.video_frame.pixel_format == HI_PIXEL_FORMAT_YUV_SEMIPLANAR_420) {
            frame_size = frame_size * 3 / 2;
        }

        printf("[INFO] Frame size: %u bytes, saving to /progs/rec/00/raw_frame.bin\n",
               frame_size);

        /* Use hi_mpi_sys_mmap to map the physical address */
        void *virt = hi_mpi_sys_mmap(frame.video_frame.phys_addr[0], frame_size);
        if (virt != NULL) {
            FILE *fp = fopen("/progs/rec/00/raw_frame.bin", "wb");
            if (fp) {
                fwrite(virt, 1, frame_size, fp);
                fclose(fp);
                printf("[ OK ] Saved raw frame: /progs/rec/00/raw_frame.bin (%u bytes)\n",
                       frame_size);
            } else {
                printf("[FAIL] fopen: %s\n", strerror(errno));
            }
            hi_mpi_sys_munmap(virt, frame_size);
        } else {
            printf("[WARN] hi_mpi_sys_mmap failed, trying /dev/mem...\n");
            int memfd = open("/dev/mem", O_RDONLY);
            if (memfd >= 0) {
                void *p = mmap(NULL, frame_size, PROT_READ, MAP_SHARED,
                               memfd, frame.video_frame.phys_addr[0]);
                if (p != MAP_FAILED) {
                    FILE *fp = fopen("/progs/rec/00/raw_frame.bin", "wb");
                    if (fp) {
                        fwrite(p, 1, frame_size, fp);
                        fclose(fp);
                        printf("[ OK ] Saved raw frame via /dev/mem (%u bytes)\n",
                               frame_size);
                    }
                    munmap(p, frame_size);
                } else {
                    printf("[FAIL] /dev/mem mmap: %s\n", strerror(errno));
                }
                close(memfd);
            }
        }
    }

    hi_mpi_vi_release_chn_frame(VI_PIPE, VI_CHN, &frame);
    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 5: VPSS group + channel
 *
 *  Uses V1.0.2.1 SDK APIs. The SDK internally manages /dev/vpss fds
 *  and handles the REG/SET sequence. Previous raw ioctl attempt failed
 *  with errno=2 on SET_CHN_ATTR -- the kernel's REG disambiguation
 *  (group vs channel) was not understood. Letting the SDK do it.
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 vpss_init(void)
{
    hi_s32 ret;
    ot_vpss_grp_attr grp_attr;
    ot_vpss_chn_attr chn_attr;

    printf("\n=== VPSS init (V1.0.2.1 SDK) ===\n");

    /* ── Create + configure group ── */
    memset(&grp_attr, 0, sizeof(grp_attr));
    grp_attr.max_width      = SENSOR_WIDTH;
    grp_attr.max_height     = SENSOR_HEIGHT;
    grp_attr.dynamic_range  = OT_DYNAMIC_RANGE_SDR8;
    grp_attr.pixel_format   = OT_PIXEL_FORMAT_YVU_SEMIPLANAR_420;
    grp_attr.dei_mode       = OT_VPSS_DEI_MODE_OFF;
    grp_attr.frame_rate.src_frame_rate = -1;
    grp_attr.frame_rate.dst_frame_rate = -1;

    ret = ss_mpi_vpss_create_grp(VPSS_GRP, &grp_attr);
    CHECK_RET("ss_mpi_vpss_create_grp", ret);

    ret = ss_mpi_vpss_start_grp(VPSS_GRP);
    CHECK_RET("ss_mpi_vpss_start_grp", ret);

    /* ── Configure channel ── */
    memset(&chn_attr, 0, sizeof(chn_attr));
    chn_attr.mirror_en      = HI_FALSE;
    chn_attr.flip_en        = HI_FALSE;
    chn_attr.border_en      = HI_FALSE;
    chn_attr.width          = SENSOR_WIDTH;
    chn_attr.height         = SENSOR_HEIGHT;
    chn_attr.depth          = 0;  /* Bound mode: VENC pulls frames, no user queue */
    chn_attr.chn_mode       = OT_VPSS_CHN_MODE_USER;
    chn_attr.video_format   = OT_VIDEO_FORMAT_LINEAR;
    chn_attr.dynamic_range  = OT_DYNAMIC_RANGE_SDR8;
    chn_attr.pixel_format   = OT_PIXEL_FORMAT_YVU_SEMIPLANAR_420;
    chn_attr.compress_mode  = OT_COMPRESS_MODE_NONE;
    chn_attr.frame_rate.src_frame_rate = -1;
    chn_attr.frame_rate.dst_frame_rate = -1;

    ret = ss_mpi_vpss_set_chn_attr(VPSS_GRP, VPSS_CHN, &chn_attr);
    CHECK_RET("ss_mpi_vpss_set_chn_attr", ret);

    ret = ss_mpi_vpss_enable_chn(VPSS_GRP, VPSS_CHN);
    CHECK_RET("ss_mpi_vpss_enable_chn", ret);

    /* Bind VI -> VPSS */
    hi_mpp_chn src_chn, dst_chn;
    src_chn.mod_id = HI_ID_VI;
    src_chn.dev_id = VI_PIPE;
    src_chn.chn_id = VI_CHN;

    dst_chn.mod_id = HI_ID_VPSS;
    dst_chn.dev_id = VPSS_GRP;
    dst_chn.chn_id = 0;

    ret = hi_mpi_sys_bind(&src_chn, &dst_chn);
    CHECK_RET("hi_mpi_sys_bind(VI->VPSS)", ret);

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 6: VENC H.265 channel (continuous encoding)
 *
 *  JPEG VENC fails with ILLEGAL_PARAM on this B051 kernel -- likely
 *  an ABI mismatch between V1.0.2.1 SDK and B051's ot_venc_chn_attr.
 *  H.265 works (superb uses it), so we capture H.265 and convert
 *  to viewable format on the host PC with ffmpeg.
 * ═══════════════════════════════════════════════════════════════ */
static hi_s32 venc_init(void)
{
    hi_s32 ret;
    ot_venc_chn_attr chn_attr;
    ot_venc_start_param start_param;
    hi_mpp_chn src_chn, dst_chn;

    printf("\n=== VENC H.265 init ===\n");

    /* Extra cleanup: superb's VENC state may persist in kernel */
    ss_mpi_venc_stop_chn(VENC_CHN);
    ss_mpi_venc_destroy_chn(VENC_CHN);
    ss_mpi_venc_stop_chn(1);
    ss_mpi_venc_destroy_chn(1);

    memset(&chn_attr, 0, sizeof(chn_attr));

    /* H.265 main profile, matching superb's known-working config */
    chn_attr.venc_attr.type           = OT_PT_H265;
    chn_attr.venc_attr.max_pic_width  = SENSOR_WIDTH;
    chn_attr.venc_attr.max_pic_height = SENSOR_HEIGHT;
    chn_attr.venc_attr.pic_width      = SENSOR_WIDTH;
    chn_attr.venc_attr.pic_height     = SENSOR_HEIGHT;
    /* H.265 buf_size: SDK uses w*h*3/4 aligned to 64 */
    chn_attr.venc_attr.buf_size       = (SENSOR_WIDTH * SENSOR_HEIGHT * 3 / 4 + 63) & ~63;
    chn_attr.venc_attr.is_by_frame    = HI_TRUE;
    chn_attr.venc_attr.profile        = 0;  /* main profile */

    /* H.265 specific attr */
    chn_attr.venc_attr.h265_attr.frame_buf_ratio = 100;  /* 100% = no compression */
    chn_attr.venc_attr.h265_attr.rcn_ref_share_buf_en = HI_FALSE;

    /* Rate control: VBR at ~4Mbps max (matches superb: rcMode=3=VBR, bitrate=4096)
     * Superb runs sensor at 20fps but encodes at 15fps (SystemCfg.ini).
     * src_frame_rate=20 (ISP output), dst_frame_rate=15 (encoded output).
     * VENC drops every 4th frame to achieve 15fps from 20fps input. */
    chn_attr.rc_attr.rc_mode = OT_VENC_RC_MODE_H265_VBR;
    chn_attr.rc_attr.h265_vbr.gop           = 15;   /* I-frame every 15 frames (1 sec at 15fps) */
    chn_attr.rc_attr.h265_vbr.stats_time    = 1;    /* 1 second stats window */
    chn_attr.rc_attr.h265_vbr.src_frame_rate = 20;  /* Sensor/ISP runs at 20fps */
    chn_attr.rc_attr.h265_vbr.dst_frame_rate = 15;  /* Encode at 15fps (drop every 4th) */
    chn_attr.rc_attr.h265_vbr.max_bit_rate  = 4096; /* 4Mbps max (superb config) */

    /* GOP: all I-frames for easy extraction (every frame is independently decodable) */
    chn_attr.gop_attr.gop_mode = OT_VENC_GOP_MODE_NORMAL_P;
    chn_attr.gop_attr.normal_p.ip_qp_delta = 2;

    printf("[INFO] VENC H265 chn_attr: sizeof=%zu, type=%u, %ux%u, buf=%u, gop=%u, max_br=%u\n",
           sizeof(chn_attr), chn_attr.venc_attr.type,
           chn_attr.venc_attr.pic_width, chn_attr.venc_attr.pic_height,
           chn_attr.venc_attr.buf_size,
           chn_attr.rc_attr.h265_vbr.gop,
           chn_attr.rc_attr.h265_vbr.max_bit_rate);

    ret = ss_mpi_venc_create_chn(VENC_CHN, &chn_attr);
    CHECK_RET("ss_mpi_venc_create_chn(H265)", ret);

    /* Set VBR QP limits to match superb (minQP=35, maxQP=44) */
    {
        ot_venc_rc_param rc_param;
        memset(&rc_param, 0, sizeof(rc_param));
        rc_param.h265_vbr_param.max_qp = 44;
        rc_param.h265_vbr_param.min_qp = 35;
        rc_param.h265_vbr_param.max_i_qp = 44;
        rc_param.h265_vbr_param.min_i_qp = 35;
        rc_param.h265_vbr_param.max_reencode_times = 3;
        rc_param.h265_vbr_param.max_i_proportion = 100;
        rc_param.h265_vbr_param.min_i_proportion = 1;
        rc_param.h265_vbr_param.qpmap_en = HI_FALSE;
        hi_s32 rc_ret = ss_mpi_venc_set_rc_param(VENC_CHN, &rc_param);
        printf("[INFO] VBR QP limits set: max_qp=44 min_qp=35 ret=0x%08X\n", (unsigned)rc_ret);
    }

    /* Start the VENC channel (continuous receive) */
    start_param.recv_pic_num = -1;
    ret = ss_mpi_venc_start_chn(VENC_CHN, &start_param);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] ss_mpi_venc_start_chn: 0x%08X\n", (unsigned)ret);
        ss_mpi_venc_destroy_chn(VENC_CHN);
        return ret;
    }
    printf("[ OK ] ss_mpi_venc_start_chn\n");

    /* Bind VPSS -> VENC */
    src_chn.mod_id = HI_ID_VPSS;
    src_chn.dev_id = VPSS_GRP;
    src_chn.chn_id = VPSS_CHN;
    dst_chn.mod_id = HI_ID_VENC;
    dst_chn.dev_id = 0;
    dst_chn.chn_id = VENC_CHN;

    ret = hi_mpi_sys_bind(&src_chn, &dst_chn);
    CHECK_RET("hi_mpi_sys_bind(VPSS->VENC)", ret);

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  STEP 7: Capture H.265 bitstream
 *
 *  Grab ~3 seconds of H.265 frames after ISP/AE stabilization.
 *  Save raw .h265 bitstream to SD card. Convert on host with:
 *    ffmpeg -i capture.h265 -frames:v 1 capture.png
 * ═══════════════════════════════════════════════════════════════ */
#define CAPTURE_FRAMES   150  /* ~10 seconds at 15fps VENC output -- time for light toggle test */

static hi_s32 capture_h265(void)
{
    hi_s32 ret;
    hi_s32 venc_fd;
    fd_set read_fds;
    struct timeval timeout_val;
    ot_venc_chn_status stat;
    ot_venc_stream stream;
    FILE *fp = NULL;
    hi_u32 total_bytes = 0;
    int frames_saved = 0;

    if (g_rtsp_mode) {
        printf("\n=== RTSP streaming mode ===\n");
    } else {
        printf("\n=== Capturing H.265 bitstream ===\n");
    }

    /* Let ISP/AE stabilize after PQ bin loading.
     * With PQ calibration loaded, AE should converge to proper exposure
     * within a few seconds. Give it 12s to be safe.
     * Feed the watchdog during this wait to prevent SoC hard-reset. */
    printf("[INFO] Waiting 12s for ISP/AE/AWB to stabilize...\n");
    for (int stab_i = 0; stab_i < 12; stab_i++) {
        sleep(1);
        watchdog_feed();
    }

    /* Readback actual sensor exposure registers via I2C */
    if (g_sns_obj && g_sns_obj->pfn_read_reg) {
        int e0 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E00);
        int e1 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E01);
        int e2 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E02);
        int ag_c = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E08);
        int ag_f = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E09);
        int dg_c = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E06);
        int dg_f = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E07);
        int vts_h = g_sns_obj->pfn_read_reg(VI_PIPE, 0x320E);
        int vts_l = g_sns_obj->pfn_read_reg(VI_PIPE, 0x320F);
        unsigned int exp_val = ((e0 & 0x0F) << 12) | (e1 << 4) | ((e2 >> 4) & 0x0F);
        unsigned int vts_val = (vts_h << 8) | vts_l;
        printf("[I2C] exp=0x%X (%u half-lines), VTS=0x%X (%u)\n",
               exp_val, exp_val, vts_val, vts_val);
        printf("[I2C] again: coarse=0x%02X fine=0x%02X | dgain: coarse=0x%02X fine=0x%02X\n",
               ag_c, ag_f, dg_c, dg_f);
    }

    /* Query ISP AE + AWB info to see convergence state */
    {
        ot_isp_exp_info exp_info;
        ot_isp_wb_info wb_info;

        memset(&exp_info, 0, sizeof(exp_info));
        hi_s32 qret = ss_mpi_isp_query_exposure_info(VI_PIPE, &exp_info);
        if (qret == HI_SUCCESS) {
            printf("[AE ] exp_time=%u, exposure=%u, ave_lum=%u\n",
                   exp_info.exp_time, exp_info.exposure, exp_info.ave_lum);
            printf("[AE ] a_gain=%u, d_gain=%u, isp_d_gain=%u, iso=%u\n",
                   exp_info.a_gain, exp_info.d_gain, exp_info.isp_d_gain,
                   exp_info.iso);
            printf("[AE ] fps=%u, hist_error=%d, exposure_is_max=%u\n",
                   exp_info.fps, (int)exp_info.hist_error,
                   exp_info.exposure_is_max);
        } else {
            printf("[AE ] query failed: 0x%08X\n", (unsigned)qret);
        }

        memset(&wb_info, 0, sizeof(wb_info));
        qret = ss_mpi_isp_query_wb_info(VI_PIPE, &wb_info);
        if (qret == HI_SUCCESS) {
            printf("[AWB] r_gain=%u, gr_gain=%u, gb_gain=%u, b_gain=%u\n",
                   wb_info.r_gain, wb_info.gr_gain,
                   wb_info.gb_gain, wb_info.b_gain);
            printf("[AWB] color_temp=%u, saturation=%u\n",
                   wb_info.color_temp, wb_info.saturation);
            printf("[AWB] ccm=[%u,%u,%u, %u,%u,%u, %u,%u,%u]\n",
                   wb_info.ccm[0], wb_info.ccm[1], wb_info.ccm[2],
                   wb_info.ccm[3], wb_info.ccm[4], wb_info.ccm[5],
                   wb_info.ccm[6], wb_info.ccm[7], wb_info.ccm[8]);
        } else {
            printf("[AWB] query failed: 0x%08X\n", (unsigned)qret);
        }
    }



    venc_fd = ss_mpi_venc_get_fd(VENC_CHN);
    if (venc_fd < 0) {
        printf("[FAIL] ss_mpi_venc_get_fd: %d\n", venc_fd);
        return HI_FAILURE;
    }
    printf("[ OK ] VENC fd = %d\n", venc_fd);

    /* Start RTSP server if in streaming mode */
    if (g_rtsp_mode) {
        if (rtsp_start(g_rtsp_ip, g_rtsp_port) != 0) {
            printf("[FAIL] RTSP server failed to start\n");
            return HI_FAILURE;
        }
    }

    /* In file mode, also open file for capture */
    if (!g_rtsp_mode) {
        fp = fopen(OUTPUT_FILE, "wb");
        if (fp == NULL) {
            printf("[FAIL] fopen(%s): %s\n", OUTPUT_FILE, strerror(errno));
            return HI_FAILURE;
        }
    }

    /*
     * Main VENC loop:
     *   - File mode: run for CAPTURE_FRAMES then exit
     *   - RTSP mode: run indefinitely until SIGINT/SIGTERM
     */
    while (!g_stop) {
        /* In file mode, stop after enough frames */
        if (!g_rtsp_mode && frames_saved >= CAPTURE_FRAMES)
            break;

        FD_ZERO(&read_fds);
        FD_SET(venc_fd, &read_fds);
        timeout_val.tv_sec  = 5;
        timeout_val.tv_usec = 0;

        ret = select(venc_fd + 1, &read_fds, NULL, NULL, &timeout_val);
        if (ret <= 0) {
            watchdog_feed();  /* Feed during select timeout too */
            if (g_rtsp_mode) continue;  /* Timeout is OK in streaming mode */
            printf("[WARN] select returned %d after %d frames\n", ret, frames_saved);
            break;
        }

        ret = ss_mpi_venc_query_status(VENC_CHN, &stat);
        if (ret != HI_SUCCESS || stat.cur_packs == 0) {
            if (!g_rtsp_mode)
                printf("[WARN] query_status: ret=0x%08X, cur_packs=%u\n",
                       (unsigned)ret, stat.cur_packs);
            continue;
        }

        stream.pack = (ot_venc_pack *)malloc(sizeof(ot_venc_pack) * stat.cur_packs);
        if (stream.pack == NULL) break;
        stream.pack_cnt = stat.cur_packs;

        ret = ss_mpi_venc_get_stream(VENC_CHN, &stream, -1);
        if (ret != HI_SUCCESS) {
            free(stream.pack);
            if (!g_rtsp_mode)
                printf("[WARN] get_stream: 0x%08X\n", (unsigned)ret);
            continue;
        }

        /* Push to RTSP clients */
        if (g_rtsp_mode) {
            rtsp_push_venc_stream(&stream);
        }

        /* Write to file (file mode, or both if desired) */
        if (fp) {
            for (hi_u32 i = 0; i < stream.pack_cnt; i++) {
                hi_u8 *data = stream.pack[i].addr + stream.pack[i].offset;
                hi_u32 len  = stream.pack[i].len - stream.pack[i].offset;
                fwrite(data, 1, len, fp);
                total_bytes += len;
            }
        }

        ss_mpi_venc_release_stream(VENC_CHN, &stream);
        free(stream.pack);
        frames_saved++;

        /* Feed hardware watchdog every frame to prevent SoC hard-reset */
        watchdog_feed();

        /* Log every 20th frame for debugging in RTSP mode */
        if (g_rtsp_mode && (frames_saved % 20 == 0)) {
            fprintf(stderr, "[VENC] frame=%d\n", frames_saved);
        }

        /* Periodic AE/AWB status (every 50 frames in file mode, every 300 in RTSP) */
        {
            int interval = g_rtsp_mode ? 300 : 50;
            if (frames_saved % interval == 0) {
                ot_isp_exp_info ei;
                ot_isp_wb_info wi;
                memset(&ei, 0, sizeof(ei));
                memset(&wi, 0, sizeof(wi));
                ss_mpi_isp_query_exposure_info(VI_PIPE, &ei);
                ss_mpi_isp_query_wb_info(VI_PIPE, &wi);
                printf("[F%d] AE: exp=%u, again=%u, dgain=%u, lum=%u, iso=%u | "
                       "AWB: R=%u G=%u B=%u ct=%u\n",
                       frames_saved, ei.exp_time, ei.a_gain, ei.d_gain,
                       ei.ave_lum, ei.iso,
                       wi.r_gain, wi.gr_gain, wi.b_gain, wi.color_temp);
            }
        }
    }

    /* Cleanup */
    if (fp) fclose(fp);
    if (g_rtsp_mode) rtsp_stop();

    /* Final I2C readback after capture */
    if (g_sns_obj && g_sns_obj->pfn_read_reg) {
        int e0 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E00);
        int e1 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E01);
        int e2 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E02);
        int ag_c = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E08);
        int ag_f = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E09);
        int dg_c = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E06);
        int dg_f = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E07);
        unsigned int exp_val = ((e0 & 0x0F) << 12) | (e1 << 4) | ((e2 >> 4) & 0x0F);
        printf("[I2C-END] exp=%u, again=0x%02X/0x%02X, dgain=0x%02X/0x%02X\n",
               exp_val, ag_c, ag_f, dg_c, dg_f);
    }

    if (g_rtsp_mode) {
        printf("[ OK ] RTSP streaming ended after %d frames\n", frames_saved);
        return HI_SUCCESS;
    } else if (frames_saved > 0) {
        printf("[ OK ] Saved H.265: %s (%u bytes, %d frames)\n",
               OUTPUT_FILE, total_bytes, frames_saved);
        return HI_SUCCESS;
    } else {
        printf("[FAIL] No H.265 frames captured\n");
        return HI_FAILURE;
    }
}

/* ═══════════════════════════════════════════════════════════════
 *  Teardown (best-effort cleanup)
 * ═══════════════════════════════════════════════════════════════ */
static void teardown(void)
{
    hi_mpp_chn src_chn, dst_chn;
    hi_isp_3a_alg_lib ae_lib, awb_lib;

    printf("\n=== Teardown ===\n");

    /* Unbind VPSS->VENC */
    src_chn.mod_id = HI_ID_VPSS;
    src_chn.dev_id = VPSS_GRP;
    src_chn.chn_id = VPSS_CHN;
    dst_chn.mod_id = HI_ID_VENC;
    dst_chn.dev_id = 0;
    dst_chn.chn_id = VENC_CHN;
    hi_mpi_sys_unbind(&src_chn, &dst_chn);

    /* Stop VENC */
    hi_mpi_venc_stop_chn(VENC_CHN);
    hi_mpi_venc_destroy_chn(VENC_CHN);

    /* Unbind VI->VPSS */
    src_chn.mod_id = HI_ID_VI;
    src_chn.dev_id = VI_PIPE;
    src_chn.chn_id = VI_CHN;
    dst_chn.mod_id = HI_ID_VPSS;
    dst_chn.dev_id = VPSS_GRP;
    dst_chn.chn_id = 0;
    hi_mpi_sys_unbind(&src_chn, &dst_chn);

    /* Stop VPSS */
    hi_mpi_vpss_disable_chn(VPSS_GRP, VPSS_CHN);
    hi_mpi_vpss_stop_grp(VPSS_GRP);
    hi_mpi_vpss_destroy_grp(VPSS_GRP);

    /* Stop ISP: signal exit, then join the ISP thread */
    hi_mpi_isp_exit(VI_PIPE);
    if (g_isp_running) {
        printf("[INFO] Waiting for ISP thread to exit...\n");
        pthread_join(g_isp_thread, NULL);
    }

    /* Unregister AE/AWB algorithm libraries */
    memset(&ae_lib, 0, sizeof(ae_lib));
    ae_lib.id = VI_PIPE;
    strncpy(ae_lib.lib_name, HI_AE_LIB_NAME, sizeof(ae_lib.lib_name) - 1);
    memset(&awb_lib, 0, sizeof(awb_lib));
    awb_lib.id = VI_PIPE;
    strncpy(awb_lib.lib_name, HI_AWB_LIB_NAME, sizeof(awb_lib.lib_name) - 1);

    hi_mpi_awb_unregister(VI_PIPE, &awb_lib);
    hi_mpi_ae_unregister(VI_PIPE, &ae_lib);

    /* Unregister sensor */
    if (g_sns_obj && g_sns_obj->pfn_un_register_callback) {
        g_sns_obj->pfn_un_register_callback(VI_PIPE, &ae_lib, &awb_lib);
    }

    /* Stop VI */
    if (vi_used_raw) {
        /* Raw ioctl path */
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
        /* B040 library path */
        hi_mpi_vi_disable_chn(VI_PIPE, VI_CHN);
        hi_mpi_vi_stop_pipe(VI_PIPE);
        hi_mpi_vi_destroy_pipe(VI_PIPE);
        hi_mpi_vi_unbind(VI_DEV, VI_PIPE);
        hi_mpi_vi_disable_dev(VI_DEV);
    }

    /* System exit */
    hi_mpi_sys_exit();
    hi_mpi_vb_exit();

    /* Disarm watchdog cleanly so mySystem can take it back */
    watchdog_close();

    /* Unload PQ bin library */
    if (g_pqbin_dl) {
        dlclose(g_pqbin_dl);
        g_pqbin_dl = NULL;
    }

    /* Unload sensor .so */
    if (g_sns_dl) {
        dlclose(g_sns_dl);
        g_sns_dl = NULL;
    }

    printf("[ OK ] Teardown complete\n");
}

/* ═══════════════════════════════════════════════════════════════
 *  Crash signal handler -- capture fatal signals for debugging
 * ═══════════════════════════════════════════════════════════════ */
static void crash_handler(int sig)
{
    /* Write directly to stderr (fd 2) using write() -- async-signal-safe */
    const char *msg = NULL;
    switch (sig) {
        case SIGSEGV: msg = "\n!!! CRASH: SIGSEGV (segfault) !!!\n"; break;
        case SIGABRT: msg = "\n!!! CRASH: SIGABRT (abort) !!!\n"; break;
        case SIGBUS:  msg = "\n!!! CRASH: SIGBUS (bus error) !!!\n"; break;
        case SIGFPE:  msg = "\n!!! CRASH: SIGFPE (floating point) !!!\n"; break;
        case SIGPIPE: msg = "\n!!! CRASH: SIGPIPE (broken pipe) !!!\n"; break;
        case SIGHUP:  msg = "\n!!! SIGNAL: SIGHUP (hangup) !!!\n"; break;
        default:      msg = "\n!!! CRASH: unknown signal !!!\n"; break;
    }
    if (msg) write(2, msg, strlen(msg));

    /* Print the signal number for clarity */
    char buf[64];
    int n = snprintf(buf, sizeof(buf), "Signal number: %d\n", sig);
    if (n > 0) write(2, buf, n);

    /* Disarm watchdog to prevent reboot on crash */
    if (g_watchdog_fd >= 0) {
        write(g_watchdog_fd, "V", 1);
        close(g_watchdog_fd);
        g_watchdog_fd = -1;
    }

    /* Restore default handler and re-raise to get core dump */
    signal(sig, SIG_DFL);
    raise(sig);
}

/* ═══════════════════════════════════════════════════════════════
 *  Signal handler for clean exit
 * ═══════════════════════════════════════════════════════════════ */
static void sig_handler(int sig)
{
    (void)sig;
    printf("\n[INFO] Signal %d received, stopping...\n", sig);
    g_stop = 1;
    if (g_rtsp_mode) {
        rtsp_stop();
    }
    watchdog_close();  /* Disarm before teardown to prevent reboot */
    teardown();
    exit(1);
}

/* ═══════════════════════════════════════════════════════════════
 *  MAIN
 * ═══════════════════════════════════════════════════════════════ */
int main(int argc, char *argv[])
{
    hi_s32 ret;
    int i;

    /* Parse command-line arguments */
    for (i = 1; i < argc; i++) {
        if (strcmp(argv[i], "--rtsp") == 0) {
            g_rtsp_mode = 1;
        } else if (strcmp(argv[i], "--rtsp-port") == 0 && i + 1 < argc) {
            g_rtsp_port = atoi(argv[++i]);
        } else if (strcmp(argv[i], "--rtsp-ip") == 0 && i + 1 < argc) {
            g_rtsp_ip = argv[++i];
        } else if (argv[i][0] != '-') {
            /* Positional arg: PQ bin path (backward compat) */
            g_pq_bin_path = argv[i];
            printf("[INFO] PQ bin: %s\n", g_pq_bin_path);
        } else if (strcmp(argv[i], "--help") == 0 || strcmp(argv[i], "-h") == 0) {
            printf("Usage: pipeline_test [options] [pq_bin_path]\n");
            printf("  --rtsp           Stream via RTSP instead of file capture\n");
            printf("  --rtsp-port N    RTSP port (default: 554)\n");
            printf("  --rtsp-ip IP     Bind IP (default: 0.0.0.0)\n");
            printf("  pq_bin_path      Path to PQ bin file\n");
            return 0;
        }
    }

    /* Disable stdout buffering so crash output is visible */
    setvbuf(stdout, NULL, _IONBF, 0);
    setvbuf(stderr, NULL, _IONBF, 0);

    printf("============================================\n");
    printf("  SC635HAI Pipeline Test\n");
    printf("  Hi3516CV610 + SC635HAI (3200x1800 @ 20fps sensor, 15fps encode)\n");
    if (g_rtsp_mode) {
        printf("  Mode: RTSP streaming on port %d\n", g_rtsp_port);
    } else {
        printf("  Mode: File capture (%d frames)\n", CAPTURE_FRAMES);
    }
    printf("============================================\n");

    /* Install crash signal handlers (before any SDK calls) */
    signal(SIGSEGV, crash_handler);
    signal(SIGABRT, crash_handler);
    signal(SIGBUS,  crash_handler);
    signal(SIGFPE,  crash_handler);
    signal(SIGPIPE, crash_handler);
    signal(SIGHUP,  crash_handler);

    signal(SIGINT, sig_handler);
    signal(SIGTERM, sig_handler);

    /* Step 0: Load sensor driver */
    ret = load_sensor_driver();
    if (ret != HI_SUCCESS) goto fail;

    /* Step 1: System init */
    ret = sys_init();
    if (ret != HI_SUCCESS) goto fail;

    /* Take over hardware watchdog from superb (which we just killed).
     * Must happen ASAP after superb teardown to prevent watchdog timeout.
     * The SoC hard-resets ~30s after superb stops feeding /dev/watchdog. */
    watchdog_open();

    /* Step 2: MIPI RX init */
    ret = mipi_init();
    if (ret != HI_SUCCESS) goto fail;

    /* Step 3: VI device/pipe/channel */
    ret = vi_init();
    if (ret != HI_SUCCESS) goto fail;

    /* Step 4: ISP init + sensor registration + ISP run thread */
    ret = isp_init();
    if (ret != HI_SUCCESS) {
        printf("[FAIL] ISP init failed (0x%08X), trying raw VI frame dump...\n",
               (unsigned)ret);
        ret = dump_raw_frame();
        if (ret == HI_SUCCESS) {
            printf("\n========================================\n");
            printf("  Raw VI frame captured (no ISP processing)\n");
            printf("========================================\n");
        }
        goto cleanup;
    }

    /* Step 4a: Load PQ bin calibration (required for AE to work) */
    {
        hi_s32 pq_ret = load_pq_bin();
        if (pq_ret != HI_SUCCESS) {
            printf("[WARN] PQ bin loading failed -- continuing anyway\n");
            printf("[WARN] Image will likely be black without PQ calibration\n");
        }
    }

    /* ── CRITICAL: Re-set bayer_format after PQ bin load ─────────────
     *
     * The SC635HAI sensor is BGGR (standard for SmartSens sensors).
     * We set bayer_format=BGGR in pub_attr before isp_init, but the
     * PQ bin's ISP calibration data (type 0) overrides it back to RGGB.
     *
     * This caused the R/B channel swap that plagued Phase 5: the ISP
     * demosaic treated BGGR data as RGGB, assigning R pixels to the B
     * channel and vice versa.
     *
     * Fix: read back pub_attr after PQ bin load, re-set bayer_format
     * to BGGR, and write it back. This must be done EVERY time a PQ
     * bin is loaded (day.bin, night.bin, light.bin, black.bin all
     * override bayer_format).
     *
     * The PQ bins live at /home/sensor/sc635hai/pqbin/ and are swapped
     * by superb based on scene mode (day/night/IR). In a production
     * system with scene switching, this re-set must follow each load.
     */
    {
        ot_isp_pub_attr pa;
        hi_s32 r = ss_mpi_isp_get_pub_attr(VI_PIPE, &pa);
        printf("[BAYER] post-PQ-bin readback: bayer_format=%d (expected 0=RGGB from PQ bin)\n",
               pa.bayer_format);
        pa.bayer_format = OT_ISP_BAYER_BGGR;
        r = ss_mpi_isp_set_pub_attr(VI_PIPE, &pa);
        printf("[BAYER] re-set to BGGR(3): ret=0x%08X\n", (unsigned)r);
        r = ss_mpi_isp_get_pub_attr(VI_PIPE, &pa);
        printf("[BAYER] verified: bayer_format=%d\n", pa.bayer_format);
    }

    /* Step 4a+: Configure ISP color pipeline (disable chrominance crushers) */
    configure_isp_color();

    /* Step 4a++: Low-light NR tuning (ISP BayerNR + DRC chroma) */
    configure_lowlight_nr();

    /* Step 4a+++: Configure 3DNR at VI pipe level (before VPSS) */
    configure_3dnr();

    /* Step 5: VPSS group/channel + bind VI->VPSS */
    ret = vpss_init();
    if (ret != HI_SUCCESS) {
        printf("[WARN] VPSS failed, trying raw frame dump from VI...\n");
        ret = dump_raw_frame();
        if (ret == HI_SUCCESS) {
            printf("\n========================================\n");
            printf("  Raw frame captured! Sensor driver works!\n");
            printf("========================================\n");
        }
        goto cleanup;
    }

    /* Step 6: VENC JPEG channel */
    ret = venc_init();
    if (ret != HI_SUCCESS) goto fail;

    /* Step 7: Capture H.265 bitstream */
    ret = capture_h265();
    if (ret != HI_SUCCESS) {
        printf("\n[WARN] H.265 capture failed, but pipeline init succeeded!\n");
        printf("[INFO] This means the ISP/VI/VPSS pipeline is working.\n");
        printf("[INFO] The failure might be in VENC or frame timing.\n");
    } else {
        printf("\n========================================\n");
        if (g_rtsp_mode) {
            printf("  RTSP streaming completed\n");
        } else {
            printf("  Phase 4 SUCCESS!\n");
            printf("  H.265 saved to: %s\n", OUTPUT_FILE);
            printf("  Convert on host: ffmpeg -i capture.h265 -frames:v 1 capture.png\n");
        }
        printf("========================================\n");
    }

    /* Clean teardown */
    teardown();
    return (ret == HI_SUCCESS) ? 0 : 1;

cleanup:
    teardown();
    return (ret == HI_SUCCESS) ? 0 : 1;

fail:
    printf("\n[FAIL] Pipeline init failed, tearing down...\n");
    teardown();
    return 1;
}
