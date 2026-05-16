/*
 * pipeline.h -- Shared state, configuration, and common includes for ipc_daemon
 *
 * All HAL modules and main.c include this header. It provides:
 *   - SDK includes (ot_type, ss_mpi_*, etc.)
 *   - Pipeline configuration constants
 *   - Global state (shared across modules)
 *   - Common macros (CHECK_RET, etc.)
 */

#ifndef PIPELINE_H
#define PIPELINE_H

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

/* Audio SDK APIs */
#include "ss_mpi_audio.h"
#include "ot_common_aio.h"
#include "ot_common_aenc.h"
#include "ot_acodec.h"

/* MIPI RX ioctl definitions */
#include "ot_mipi_rx.h"

/* B040 hi_ to V1.0.2.1 ot_/ss_mpi_ compatibility */
#include "hi_compat.h"

/* RTSP streaming */
#include "rtsp_push.h"

/* ── Pipeline Configuration ───────────────────────────────────── */

#define SENSOR_WIDTH      3200
#define SENSOR_HEIGHT     1800
#define SENSOR_FPS        20.0f  /* Sensor native 20fps (VTS=2812) */

#define VI_DEV            0
#define VI_PIPE           0
#define VI_CHN            0
#define VPSS_GRP          0
#define VPSS_CHN          0      /* Base channel: sensor native resolution */
#define VPSS_EXT_CHN      3      /* Ext channel: upscaled to encode resolution */
#define VENC_CHN          0

/* Encode resolution: 4K upscaled from sensor native via VPSS ext channel 3.
 * Matches superb's output. Key: single common pool (3840x2160 YUV x3) instead
 * of separate RAW+YUV pools. No RAW pool needed in VI_ONLINE mode. */
#define ENCODE_WIDTH      3840
#define ENCODE_HEIGHT     2160

#define MIPI_DEV          "/dev/ot_mipi_rx"
#define I2C_BUS           0

#define OUTPUT_FILE       "/progs/rec/00/ipc_drv/capture.h265"
#define SENSOR_LIB        "/progs/rec/00/ipc_drv/libsns_sc635hai.so"
#define SENSOR_OBJ_NAME   "g_sns_sc635hai_obj"

/* Audio configuration (G.711A, 8kHz, mono) */
#define AI_DEV            0
#define AI_CHN            0
#define AENC_CHN          0
#define AUDIO_SAMPLE_RATE OT_AUDIO_SAMPLE_RATE_8000
#define AUDIO_PTNUMPERFRM 320    /* 40ms per frame at 8kHz */
#define ACODEC_FILE       "/dev/acodec"
#define AENC_G711A_HDR_SIZE 4    /* HiSilicon private header prepended to G.711 frames */

/* Buffer pool sizes.
 * RAW10: 10 bits/pixel -> ceil(width*10/8) per row, stride-aligned to 16 bytes.
 * Actual: ceil(3200*10/8 / 16)*16 = 4000 bytes/row * 1800 = 7,200,000.
 * Use width*2 as safe upper bound (11.5 MB); the kernel may need headroom. */
#define VB_BLK_SIZE_RAW   (SENSOR_WIDTH * SENSOR_HEIGHT * 2)
#define VB_BLK_SIZE_YUV   (SENSOR_WIDTH * SENSOR_HEIGHT * 3/2)
#define VB_BLK_SIZE_ENC   (ENCODE_WIDTH * ENCODE_HEIGHT * 3/2)  /* Upscaled YUV for VENC */
#define VB_RAW_CNT        2
#define VB_YUV_CNT        2      /* Reduced: ext channel is the VENC path now */
#define VB_ENC_CNT        2      /* Ext channel output buffers for upscaled frames */

/* PQ bin paths */
#define PQ_BIN_PATH_DEFAULT  "/home/sensor/sc635hai/pqbin/day.bin"
#define LIBBIN_PATH          "/progs/rec/00/ipc_drv/libbin.so"

/* Capture settings */
#define CAPTURE_FRAMES    150    /* ~10 seconds at 15fps */

/* ── Global State ─────────────────────────────────────────────── */

/* Sensor driver (loaded via dlopen) */
extern hi_isp_sns_obj *g_sns_obj;
extern void           *g_sns_dl;

/* ISP thread */
extern volatile int    g_isp_running;
extern pthread_t       g_isp_thread;

/* Operating mode */
extern int             g_rtsp_mode;
extern const char     *g_rtsp_ip;
extern int             g_rtsp_port;
extern volatile int    g_stop;

/* Audio */
extern int             g_audio_enabled;
extern int             g_mic_gain;

/* Watchdog */
extern int             g_watchdog_fd;

/* PQ bin */
extern const char     *g_pq_bin_path;
extern void           *g_pqbin_dl;

/* VI raw ioctl state (shared between vi.c and main.c teardown) */
extern int             vi_fd_dev;
extern int             vi_fd_pipe;
extern int             vi_fd_chn;
extern int             vi_used_raw;

/* ── Common Macros ────────────────────────────────────────────── */

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

#endif /* PIPELINE_H */
