/*
 * ipc_daemon -- SECUEYE X5 camera daemon
 *
 * Replaces the stock "superb" monolithic binary with an open-source
 * daemon for the Hi3516CV610 + SC635HAI platform.
 *
 * Pipeline: Sensor -> MIPI -> VI -> ISP -> VPSS -> VENC(H.265) -> RTSP
 *           Mic -> AI -> AENC(G.711A) -> RTSP (audio)
 *
 * Usage:
 *   ipc_daemon --rtsp                  # Stream via RTSP on port 554
 *   ipc_daemon --rtsp --rtsp-port 8554 # Custom port
 *   ipc_daemon                         # File capture mode (debug)
 *   ipc_daemon --help
 *
 * Build: see driver/Makefile "ipc_daemon" target
 */

#include "pipeline.h"
#include "hal/sys.h"
#include "hal/vi.h"
#include "hal/isp.h"
#include "hal/vpss.h"
#include "hal/venc.h"
#include "hal/audio.h"
#include "hal/watchdog.h"

/* ── Global state definitions ─────────────────────────────────── */

hi_isp_sns_obj *g_sns_obj = NULL;
void           *g_sns_dl  = NULL;
volatile int    g_isp_running = 0;
pthread_t       g_isp_thread;

int             g_rtsp_mode = 0;
const char     *g_rtsp_ip   = "0.0.0.0";
int             g_rtsp_port = 554;
volatile int    g_stop      = 0;

int             g_audio_enabled = 0;
int             g_mic_gain = 45;

int             g_watchdog_fd = -1;

const char     *g_pq_bin_path = PQ_BIN_PATH_DEFAULT;
void           *g_pqbin_dl = NULL;

int             vi_fd_dev  = -1;
int             vi_fd_pipe = -1;
int             vi_fd_chn  = -1;
int             vi_used_raw = 0;

/* ── Teardown ─────────────────────────────────────────────────── */

static void teardown(void)
{
    hi_mpp_chn src_chn, dst_chn;
    hi_isp_3a_alg_lib ae_lib, awb_lib;

    printf("\n=== Teardown ===\n");

    /* Audio first */
    audio_deinit();

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

    /* Stop ISP */
    hi_mpi_isp_exit(VI_PIPE);
    if (g_isp_running) {
        printf("[INFO] Waiting for ISP thread to exit...\n");
        pthread_join(g_isp_thread, NULL);
    }

    /* Unregister AE/AWB */
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
    vi_deinit();

    /* System exit */
    hi_mpi_sys_exit();
    hi_mpi_vb_exit();

    /* Disarm watchdog */
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

/* ── Crash signal handler ─────────────────────────────────────── */

static void crash_handler(int sig)
{
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

    char buf[64];
    int n = snprintf(buf, sizeof(buf), "Signal number: %d\n", sig);
    if (n > 0) write(2, buf, n);

    /* Disarm watchdog to prevent reboot on crash */
    if (g_watchdog_fd >= 0) {
        write(g_watchdog_fd, "V", 1);
        close(g_watchdog_fd);
        g_watchdog_fd = -1;
    }

    /* Restore default handler and re-raise for core dump */
    signal(sig, SIG_DFL);
    raise(sig);
}

/* ── Clean exit signal handler ────────────────────────────────── */

static void sig_handler(int sig)
{
    (void)sig;
    printf("\n[INFO] Signal %d received, stopping...\n", sig);
    g_stop = 1;
    if (g_rtsp_mode) {
        rtsp_stop();
    }
    watchdog_close();
    teardown();
    exit(1);
}

/* ── Main ─────────────────────────────────────────────────────── */

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
        } else if (strcmp(argv[i], "--mic-gain") == 0 && i + 1 < argc) {
            g_mic_gain = atoi(argv[++i]);
        } else if (argv[i][0] != '-') {
            g_pq_bin_path = argv[i];
            printf("[INFO] PQ bin: %s\n", g_pq_bin_path);
        } else if (strcmp(argv[i], "--help") == 0 || strcmp(argv[i], "-h") == 0) {
            printf("Usage: ipc_daemon [options] [pq_bin_path]\n");
            printf("  --rtsp           Stream via RTSP instead of file capture\n");
            printf("  --rtsp-port N    RTSP port (default: 554)\n");
            printf("  --rtsp-ip IP     Bind IP (default: 0.0.0.0)\n");
            printf("  --mic-gain N     Microphone gain in dB (default: 45, range: -78 to 80)\n");
            printf("  pq_bin_path      Path to PQ bin file\n");
            return 0;
        }
    }

    /* Disable stdout buffering for crash visibility */
    setvbuf(stdout, NULL, _IONBF, 0);
    setvbuf(stderr, NULL, _IONBF, 0);

    printf("============================================\n");
    printf("  IPC Daemon (ipc_daemon)\n");
    printf("  Hi3516CV610 + SC635HAI (3200x1800 @ 20fps sensor, 15fps encode)\n");
    if (g_rtsp_mode) {
        printf("  Mode: RTSP streaming on port %d\n", g_rtsp_port);
    } else {
        printf("  Mode: File capture (%d frames)\n", CAPTURE_FRAMES);
    }
    printf("============================================\n");

    /* Install crash signal handlers */
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

    /* Take over hardware watchdog ASAP */
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

    /* Step 4a: Load PQ bin calibration */
    {
        hi_s32 pq_ret = load_pq_bin();
        if (pq_ret != HI_SUCCESS) {
            printf("[WARN] PQ bin loading failed -- continuing anyway\n");
            printf("[WARN] Image will likely be black without PQ calibration\n");
        }
    }

    /* Fix bayer_format after PQ bin load */
    isp_fix_bayer_format();

    /* Step 4a+: Configure ISP color pipeline */
    configure_isp_color();

    /* Step 4a++: Low-light NR tuning */
    configure_lowlight_nr();

    /* Step 4a+++: 3DNR */
    configure_3dnr();

    /* Step 5: VPSS */
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

    /* Step 6: VENC */
    ret = venc_init();
    if (ret != HI_SUCCESS) goto fail;

    /* Step 6a: Audio pipeline (RTSP mode only) */
    if (g_rtsp_mode) {
        hi_s32 audio_ret = audio_init();
        if (audio_ret != HI_SUCCESS) {
            printf("[WARN] Audio init failed (0x%08X) -- streaming video only\n",
                   (unsigned)audio_ret);
        }
    }

    /* Step 7: Capture / stream */
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
            printf("  H.265 capture complete\n");
            printf("  Saved to: %s\n", OUTPUT_FILE);
            printf("  Convert: ffmpeg -i capture.h265 -frames:v 1 capture.png\n");
        }
        printf("========================================\n");
    }

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
