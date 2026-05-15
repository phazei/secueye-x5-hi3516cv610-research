/*
 * SC635HAI Sensor Driver -- ISP/AE/AWB Callbacks
 *
 * Implements the ot_isp_sns_obj interface for the Hi3516CV610 ISP framework.
 * Provides auto-exposure, auto-white-balance, and ISP integration callbacks.
 *
 * Gain model: SC500AI-compatible (same SmartSens register family).
 * Platform API: ot_/td_ types, ss_mpi_* registration functions (V1.0.2.1 SDK).
 *
 * References:
 *   - research/SC635HAI_SENSOR_ANALYSIS.md (complete sensor analysis)
 *   - research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/include/hisilicon/ (SDK headers)
 *   - research/shumjj-3516cv610_app/device/sensor/ (CV610 sensor driver examples)
 *
 * Scaffolding origins (repos since removed, all data extracted into this file):
 *   - SC500AI (Sophgo SensorSupportList) -- gain tables, AE logic
 *   - IMX307 (YJSNPI-Hi, Hi3516EV200 SDK) -- HiSilicon driver architecture
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "ot_type.h"
#include "ot_common.h"
#include "ot_common_isp.h"
#include "ot_common_sns.h"
#include "ot_common_ae.h"
#include "ot_common_awb.h"
#include "ot_sns_ctrl.h"

/* ss_mpi_* registration APIs (V1.0.2.1 SDK, also exports ot_mpi_* aliases) */
#include "ss_mpi_isp.h"
#include "ss_mpi_ae.h"
#include "ss_mpi_awb.h"

#include "sc635hai_cmos.h"

/* ── Forward declarations (sensor_ctl.c) ───────────────────────── */
extern int  sc635hai_i2c_init(ot_vi_pipe vi_pipe);
extern int  sc635hai_i2c_exit(ot_vi_pipe vi_pipe);
extern int  sc635hai_write_register(ot_vi_pipe vi_pipe, td_u32 addr, td_u32 data);
extern int  sc635hai_read_register(ot_vi_pipe vi_pipe, td_u32 addr);
extern void sc635hai_standby(ot_vi_pipe vi_pipe);
extern void sc635hai_restart(ot_vi_pipe vi_pipe);
extern void sc635hai_set_bus_info(ot_vi_pipe vi_pipe, td_u8 i2c_dev);
extern void sc635hai_linear_6m30_10bit_init(ot_vi_pipe vi_pipe);
extern void sc635hai_mirror_flip(ot_vi_pipe vi_pipe, td_u32 mode);

/* ── Per-pipe sensor state ─────────────────────────────────────── */
#define SC635HAI_MAX_PIPE  4

static ot_isp_sns_state *g_sns_state[SC635HAI_MAX_PIPE] = { TD_NULL };
static td_bool g_dpc_enabled[SC635HAI_MAX_PIPE] = { TD_FALSE };
static ot_isp_sns_commbus g_bus_info[SC635HAI_MAX_PIPE] = { { .i2c_dev = 0 } };

#define SC635HAI_CHECK_PIPE(pipe) \
    do { \
        if ((pipe) < 0 || (pipe) >= SC635HAI_MAX_PIPE) { \
            printf("sc635hai: invalid pipe %d\n", (pipe)); \
            return OT_ERR_ISP_ILLEGAL_PARAM; \
        } \
    } while (0)

#define SC635HAI_CHECK_PIPE_VOID(pipe) \
    do { \
        if ((pipe) < 0 || (pipe) >= SC635HAI_MAX_PIPE) return; \
    } while (0)

#define SC635HAI_GET_STATE(pipe) (g_sns_state[pipe])

/* ── State init/exit ───────────────────────────────────────────── */

static td_s32 sensor_ctx_init(ot_vi_pipe vi_pipe)
{
    if (g_sns_state[vi_pipe] != TD_NULL)
        return TD_SUCCESS;

    g_sns_state[vi_pipe] = (ot_isp_sns_state *)malloc(sizeof(ot_isp_sns_state));
    if (g_sns_state[vi_pipe] == TD_NULL) {
        printf("sc635hai: malloc sns_state failed\n");
        return TD_FAILURE;
    }
    memset(g_sns_state[vi_pipe], 0, sizeof(ot_isp_sns_state));

    return TD_SUCCESS;
}

static td_void sensor_ctx_exit(ot_vi_pipe vi_pipe)
{
    if (g_sns_state[vi_pipe] != TD_NULL) {
        free(g_sns_state[vi_pipe]);
        g_sns_state[vi_pipe] = TD_NULL;
    }
}

/* ════════════════════════════════════════════════════════════════
 *  ISP SENSOR CALLBACKS (ot_isp_sns_exp_func)
 * ════════════════════════════════════════════════════════════════ */

/* Called once at sensor init -- writes the full register sequence */
static td_void cmos_sns_init(ot_vi_pipe vi_pipe)
{
    ot_isp_sns_state *state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return;

    sc635hai_i2c_init(vi_pipe);
    sc635hai_linear_6m30_10bit_init(vi_pipe);

    state->init = TD_TRUE;
}

/* Cleanup */
static td_void cmos_sns_exit(ot_vi_pipe vi_pipe)
{
    ot_isp_sns_state *state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return;

    sc635hai_i2c_exit(vi_pipe);
    state->init = TD_FALSE;
}

/* One-time global init (set defaults) */
static td_void cmos_sns_global_init(ot_vi_pipe vi_pipe)
{
    ot_isp_sns_state *state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return;

    state->init      = TD_FALSE;
    state->sync_init = TD_FALSE;
    state->img_mode  = 0;  /* linear 6M30 */
    state->hdr       = TD_FALSE;
    state->wdr_mode  = OT_WDR_MODE_NONE;
    state->fl_std    = SC635HAI_VTS_DEF;
    state->fl[0]     = SC635HAI_VTS_DEF;
    state->fl[1]     = SC635HAI_VTS_DEF;
}

/* Set image mode (resolution/fps) -- we only support one mode */
static td_s32 cmos_set_image_mode(ot_vi_pipe vi_pipe,
    const ot_isp_cmos_sns_image_mode *sns_image_mode)
{
    SC635HAI_CHECK_PIPE(vi_pipe);

    if (sns_image_mode == TD_NULL)
        return OT_ERR_ISP_NULL_PTR;

    /* Only support 3200x1800 @ 30fps */
    if (sns_image_mode->width != SC635HAI_WIDTH ||
        sns_image_mode->height != SC635HAI_HEIGHT) {
        printf("sc635hai: unsupported resolution %ux%u (need %ux%u)\n",
               sns_image_mode->width, sns_image_mode->height,
               SC635HAI_WIDTH, SC635HAI_HEIGHT);
        return OT_ERR_ISP_NOT_SUPPORT;
    }

    return TD_SUCCESS;
}

/* Set WDR mode -- only linear supported for now */
static td_s32 cmos_set_wdr_mode(ot_vi_pipe vi_pipe, td_u8 mode)
{
    ot_isp_sns_state *state;
    SC635HAI_CHECK_PIPE(vi_pipe);
    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return OT_ERR_ISP_NULL_PTR;

    if (mode != OT_WDR_MODE_NONE) {
        printf("sc635hai: WDR mode %u not supported (linear only)\n", mode);
        return OT_ERR_ISP_NOT_SUPPORT;
    }

    state->wdr_mode = OT_WDR_MODE_NONE;
    return TD_SUCCESS;
}

/* Return ISP default parameters */
static td_s32 cmos_get_isp_default(ot_vi_pipe vi_pipe,
    ot_isp_cmos_default *def)
{
    SC635HAI_CHECK_PIPE(vi_pipe);
    if (def == TD_NULL) return OT_ERR_ISP_NULL_PTR;

    memset(def, 0, sizeof(ot_isp_cmos_default));

    /* The ISP loads PQ bins from /home/sensor/sc635hai/pqbin/ automatically.
     * We don't embed calibration data in the driver. */

    return TD_SUCCESS;
}

/* Return per-channel black level */
static td_s32 cmos_get_isp_black_level(ot_vi_pipe vi_pipe,
    ot_isp_cmos_black_level *black_level)
{
    SC635HAI_CHECK_PIPE(vi_pipe);
    if (black_level == TD_NULL) return OT_ERR_ISP_NULL_PTR;

    memset(black_level, 0, sizeof(ot_isp_cmos_black_level));

    /* 10-bit SmartSens typical black level = 64.
     * BGGR Bayer pattern: all 4 channels same. */
    black_level->auto_attr.black_level[0][0] = SC635HAI_BLACK_LEVEL; /* B */
    black_level->auto_attr.black_level[0][1] = SC635HAI_BLACK_LEVEL; /* Gb */
    black_level->auto_attr.black_level[0][2] = SC635HAI_BLACK_LEVEL; /* Gr */
    black_level->auto_attr.black_level[0][3] = SC635HAI_BLACK_LEVEL; /* R */

    return TD_SUCCESS;
}

/* BLC clamp info */
static td_s32 cmos_get_blc_clamp_info(ot_vi_pipe vi_pipe, td_bool *clamp_en)
{
    (void)vi_pipe;
    if (clamp_en != TD_NULL)
        *clamp_en = TD_TRUE;
    return TD_SUCCESS;
}

/* Return I2C register map for ISP synchronization.
 *
 * The ISP framework calls this every frame during VBlank. The returned
 * ot_isp_sns_regs_info tells ot_isp.ko which I2C registers to write
 * (via the kernel-internal ot_sensor_i2c.ko path -- no /dev/ device node).
 *
 * CRITICAL DATA FLOW (must match SDK reference drivers exactly):
 *
 *   Init path:  populate state->regs_info[0]  (NOT sns_regs_info directly)
 *   Update path: set .update flags on state->regs_info[0]  (NOT sns_regs_info)
 *   End:         memcpy(sns_regs_info <- regs_info[0])  // return to ISP
 *                memcpy(regs_info[1]  <- regs_info[0])  // save for next diff
 *
 * Previous bugs (both now fixed):
 *   Bug 1: Init path wrote to sns_regs_info, which got clobbered by memcpy
 *   Bug 2: Update flags were set on sns_regs_info, also clobbered by memcpy
 *
 * Reference: SC4336P (shumjj repo) cmos_isp_get_sns_regs_info()
 */
static td_s32 cmos_get_sns_reg_info(ot_vi_pipe vi_pipe,
    ot_isp_sns_regs_info *sns_regs_info)
{
    ot_isp_sns_state *state;
    td_s32 i;

    SC635HAI_CHECK_PIPE(vi_pipe);
    if (sns_regs_info == TD_NULL) return OT_ERR_ISP_NULL_PTR;

    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return OT_ERR_ISP_NULL_PTR;

    if ((state->sync_init == TD_FALSE) || (sns_regs_info->config == TD_FALSE)) {
        /* ── Full init: populate state->regs_info[0] ──────────────── */
        state->regs_info[0].sns_type = OT_ISP_SNS_TYPE_I2C;
        state->regs_info[0].reg_num = LINEAR_REGS_NUM;
        state->regs_info[0].cfg2_valid_delay_max = 2;
        state->regs_info[0].com_bus.i2c_dev = g_bus_info[vi_pipe].i2c_dev;

        for (i = 0; i < LINEAR_REGS_NUM; i++) {
            state->regs_info[0].i2c_data[i].update = TD_TRUE;
            state->regs_info[0].i2c_data[i].dev_addr = SC635HAI_I2C_ADDR_WRITE;
            state->regs_info[0].i2c_data[i].addr_byte_num = SC635HAI_ADDR_BYTE;
            state->regs_info[0].i2c_data[i].data_byte_num = SC635HAI_DATA_BYTE;
        }

        /* Set register addresses */
        /* Group hold: start buffering before any gain/exposure writes */
        state->regs_info[0].i2c_data[LINEAR_HOLD_START].reg_addr   = SC635HAI_REG_GROUP_HOLD;
        state->regs_info[0].i2c_data[LINEAR_HOLD_START].data       = SC635HAI_GROUP_HOLD_START;

        state->regs_info[0].i2c_data[LINEAR_EXP_H].reg_addr       = SC635HAI_REG_EXP_H;
        state->regs_info[0].i2c_data[LINEAR_EXP_M].reg_addr       = SC635HAI_REG_EXP_M;
        state->regs_info[0].i2c_data[LINEAR_EXP_L].reg_addr       = SC635HAI_REG_EXP_L;
        state->regs_info[0].i2c_data[LINEAR_AGAIN_COARSE].reg_addr = SC635HAI_REG_AGAIN_COARSE;
        state->regs_info[0].i2c_data[LINEAR_AGAIN_FINE].reg_addr   = SC635HAI_REG_AGAIN_FINE;
        state->regs_info[0].i2c_data[LINEAR_DGAIN_COARSE].reg_addr = SC635HAI_REG_DGAIN_COARSE;
        state->regs_info[0].i2c_data[LINEAR_DGAIN_FINE].reg_addr   = SC635HAI_REG_DGAIN_FINE;
        state->regs_info[0].i2c_data[LINEAR_VTS_H].reg_addr        = SC635HAI_REG_VTS_H;
        state->regs_info[0].i2c_data[LINEAR_VTS_L].reg_addr        = SC635HAI_REG_VTS_L;

        /* Group hold: release to apply all changes atomically */
        state->regs_info[0].i2c_data[LINEAR_HOLD_END].reg_addr     = SC635HAI_REG_GROUP_HOLD;
        state->regs_info[0].i2c_data[LINEAR_HOLD_END].data         = SC635HAI_GROUP_HOLD_END;

        /* Exposure registers: delay 0 (apply this frame's VBlank) */
        state->regs_info[0].i2c_data[LINEAR_HOLD_START].delay_frame_num = 0;
        state->regs_info[0].i2c_data[LINEAR_EXP_H].delay_frame_num      = 0;
        state->regs_info[0].i2c_data[LINEAR_EXP_M].delay_frame_num      = 0;
        state->regs_info[0].i2c_data[LINEAR_EXP_L].delay_frame_num      = 0;
        state->regs_info[0].i2c_data[LINEAR_AGAIN_COARSE].delay_frame_num = 0;
        state->regs_info[0].i2c_data[LINEAR_AGAIN_FINE].delay_frame_num   = 0;
        state->regs_info[0].i2c_data[LINEAR_DGAIN_COARSE].delay_frame_num = 0;
        state->regs_info[0].i2c_data[LINEAR_DGAIN_FINE].delay_frame_num   = 0;
        state->regs_info[0].i2c_data[LINEAR_HOLD_END].delay_frame_num     = 0;

        /* VTS registers: delay 0 (SC4336P uses delay 1, but SmartSens group
         * hold ensures atomic update within the same VBlank) */
        state->regs_info[0].i2c_data[LINEAR_VTS_H].delay_frame_num = 0;
        state->regs_info[0].i2c_data[LINEAR_VTS_L].delay_frame_num = 0;

        /* Initialize VTS data to default so ISP doesn't write 0.
         * Without this, the ISP's first sync write clobbers VTS
         * from the sensor init table (0x0AFC) to 0x0000. */
        state->regs_info[0].i2c_data[LINEAR_VTS_H].data = (SC635HAI_VTS_DEF >> 8) & 0xFF;
        state->regs_info[0].i2c_data[LINEAR_VTS_L].data = SC635HAI_VTS_DEF & 0xFF;

        /* Initialize exposure to a sane mid-range value */
        {
            td_u32 init_exp = SC635HAI_VTS_DEF;  /* ~1/20s at 20fps */
            state->regs_info[0].i2c_data[LINEAR_EXP_H].data = (init_exp >> 12) & 0x0F;
            state->regs_info[0].i2c_data[LINEAR_EXP_M].data = (init_exp >> 4) & 0xFF;
            state->regs_info[0].i2c_data[LINEAR_EXP_L].data = (init_exp & 0x0F) << 4;
        }

        /* Initialize gain to 1x */
        state->regs_info[0].i2c_data[LINEAR_AGAIN_COARSE].data = 0x00;
        state->regs_info[0].i2c_data[LINEAR_AGAIN_FINE].data   = 0x20; /* 1.0x */
        state->regs_info[0].i2c_data[LINEAR_DGAIN_COARSE].data = 0x00;
        state->regs_info[0].i2c_data[LINEAR_DGAIN_FINE].data   = 0x80; /* 1.0x */

        state->sync_init = TD_TRUE;
    } else {
        /* ── Force AE-driven regs to update every frame ────────────
         * 2026-05-14: Diff-based update (sc4336p style) leaves update
         * flags FALSE once AE converges (same exp/gain values frame
         * after frame). The kernel ISP then skips the I2C write for
         * those regs. POKE TEST CONFIRMS: with diff-based update,
         * poking 0x3E00-02 manually is never restored by the kernel,
         * but with all regs forced TD_TRUE the kernel restores within
         * one frame.
         *
         * 2026-05-15: Validated empirically. Outcome A confirmed --
         * diff-based update DOES handle transients correctly given
         * the Iteration 2 sync-queue fixes (AE responds to lighting
         * changes and color-temperature changes), but the steady-
         * state freeze is real and reproducible (poked 0x3E00 stays
         * stuck until pipeline_test restart). Force-TRUE re-enabled
         * as a steady-state safety net. See PHASE9_ISP_I2C_SYNC.md.
         *
         * Force TRUE for exposure + gain regs. Leave VTS / group hold
         * as diff-based since those rarely change and we'd waste I2C
         * bandwidth writing them every frame. */
        state->regs_info[0].i2c_data[LINEAR_EXP_H].update      = TD_TRUE;
        state->regs_info[0].i2c_data[LINEAR_EXP_M].update      = TD_TRUE;
        state->regs_info[0].i2c_data[LINEAR_EXP_L].update      = TD_TRUE;
        state->regs_info[0].i2c_data[LINEAR_AGAIN_COARSE].update = TD_TRUE;
        state->regs_info[0].i2c_data[LINEAR_AGAIN_FINE].update   = TD_TRUE;
        state->regs_info[0].i2c_data[LINEAR_DGAIN_COARSE].update = TD_TRUE;
        state->regs_info[0].i2c_data[LINEAR_DGAIN_FINE].update   = TD_TRUE;
        /* Group hold bookends always TRUE when other regs update */
        state->regs_info[0].i2c_data[LINEAR_HOLD_START].update = TD_TRUE;
        state->regs_info[0].i2c_data[LINEAR_HOLD_END].update   = TD_TRUE;
        /* VTS: diff-based (rarely changes during AE) */
        for (i = 0; i < LINEAR_REGS_NUM; i++) {
            if (i == LINEAR_VTS_H || i == LINEAR_VTS_L) {
                if (state->regs_info[0].i2c_data[i].data !=
                    state->regs_info[1].i2c_data[i].data) {
                    state->regs_info[0].i2c_data[i].update = TD_TRUE;
                } else {
                    state->regs_info[0].i2c_data[i].update = TD_FALSE;
                }
            }
        }
    }

    /* Return regs_info[0] to ISP framework, then snapshot for next diff.
     * This is the standard SDK pattern (see SC4336P, GC8613). */
    sns_regs_info->config = TD_FALSE;
    memcpy(sns_regs_info, &state->regs_info[0], sizeof(ot_isp_sns_regs_info));
    memcpy(&state->regs_info[1], &state->regs_info[0], sizeof(ot_isp_sns_regs_info));
    state->fl[1] = state->fl[0];

    return TD_SUCCESS;
}

/* Dead pixel detection mode (test pattern) */
static td_void cmos_set_pixel_detect(ot_vi_pipe vi_pipe, td_bool enable)
{
    (void)vi_pipe;
    (void)enable;
    /* Not implemented -- would set very long exposure + max gain
     * to illuminate dead pixels. */
}

/* Get AWB gains from sensor (optional) */
static td_s32 cmos_get_awb_gains(ot_vi_pipe vi_pipe, td_u32 *sns_awb_gain)
{
    (void)vi_pipe;
    (void)sns_awb_gain;
    return TD_SUCCESS;
}

/* ════════════════════════════════════════════════════════════════
 *  AE SENSOR CALLBACKS (ot_isp_ae_sensor_exp_func)
 * ════════════════════════════════════════════════════════════════ */

/* Return AE defaults: gain ranges, exposure limits, accuracy */
static td_s32 cmos_get_ae_default(ot_vi_pipe vi_pipe,
    ot_isp_ae_sensor_default *ae_sns_dft)
{
    ot_isp_sns_state *state;

    SC635HAI_CHECK_PIPE(vi_pipe);
    if (ae_sns_dft == TD_NULL) return OT_ERR_ISP_NULL_PTR;

    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return OT_ERR_ISP_NULL_PTR;

    memset(ae_sns_dft, 0, sizeof(ot_isp_ae_sensor_default));

    /* Frame lines */
    ae_sns_dft->full_lines_std = SC635HAI_VTS_DEF;
    ae_sns_dft->full_lines     = SC635HAI_VTS_DEF;
    ae_sns_dft->full_lines_max = SC635HAI_VTS_MAX;  /* slow-shutter cap */
    /* fl_std is tracked in ot_isp_sns_state, not ae_sensor_default */

    /* Per-line time in ns: 1e9 / (VTS * fps).
     * Reference: sc4336p_cmos.c:148, sc431hai_cmos.c:206, gc8613, gc4023.
     * Without this set, the AE algorithm cannot schedule
     * cross-frame exposure transitions (cros_cnt stays 0). */
    ae_sns_dft->hmax_times =
        (td_u32)(1000000000U / ((td_u32)SC635HAI_VTS_DEF * (td_u32)SC635HAI_FPS_DEF));

    /* Exposure max in register units = VTS - 10 (matches superb's
     * /proc/umap/isp max_line=2804 at VTS=2814). The SC500AI doc
     * suggests max=2*VTS-10 (half-line precision), but superb
     * deliberately caps at single-line max. Matching superb. */
    ae_sns_dft->max_int_time = SC635HAI_VTS_DEF - SC635HAI_EXP_OFFSET;
    ae_sns_dft->min_int_time = SC635HAI_EXP_MIN;
    ae_sns_dft->max_int_time_target = 65535;  /* per sc4336p */
    ae_sns_dft->min_int_time_target = SC635HAI_EXP_MIN;

    ae_sns_dft->int_time_accu.accu_type = OT_ISP_AE_ACCURACY_LINEAR;
    ae_sns_dft->int_time_accu.accuracy  = 1;  /* 1 half-line step */
    ae_sns_dft->int_time_accu.offset    = 0;

    /* Analog gain: table lookup */
    ae_sns_dft->max_again = g_again_table[SC635HAI_AGAIN_TBL_SIZE - 1]; /* 85804 = ~83.8x */
    ae_sns_dft->min_again = 1024;  /* 1.0x */
    ae_sns_dft->max_again_target = ae_sns_dft->max_again;
    ae_sns_dft->min_again_target = ae_sns_dft->min_again;
    ae_sns_dft->again_accu.accu_type = OT_ISP_AE_ACCURACY_TABLE;
    ae_sns_dft->again_accu.accuracy  = 1;

    /* Digital gain: linear steps within power-of-2 ranges */
    ae_sns_dft->max_dgain = SC635HAI_DGAIN_MAX;  /* 32640 = ~31.875x */
    ae_sns_dft->min_dgain = SC635HAI_DGAIN_MIN;  /* 1024 = 1.0x */
    ae_sns_dft->max_dgain_target = ae_sns_dft->max_dgain;
    ae_sns_dft->min_dgain_target = ae_sns_dft->min_dgain;
    ae_sns_dft->dgain_accu.accu_type = OT_ISP_AE_ACCURACY_TABLE;
    ae_sns_dft->dgain_accu.accuracy  = 1;

    /* ISP digital gain */
    ae_sns_dft->isp_dgain_shift = 8;
    ae_sns_dft->max_isp_dgain_target = 2 << 8;  /* 512 = 2x ISP dgain */
    ae_sns_dft->min_isp_dgain_target = 1 << 8;  /* 256 = 1x */

    /* Flicker */
    ae_sns_dft->flicker_freq = 50 * 256;  /* 50Hz (PAL regions) */

    /* Initial exposure (reasonable starting point for indoor lighting) */
    ae_sns_dft->init_exposure = 76151;
    ae_sns_dft->ae_compensation = 40;

    ae_sns_dft->lines_per500ms = (td_u32)(SC635HAI_VTS_DEF * SC635HAI_FPS_DEF / 2);

    return TD_SUCCESS;
}

/* Change frame rate by adjusting VTS */
static td_void cmos_fps_set(ot_vi_pipe vi_pipe, td_float f32_fps,
    ot_isp_ae_sensor_default *ae_sns_dft)
{
    ot_isp_sns_state *state;
    td_u32 vts;

    SC635HAI_CHECK_PIPE_VOID(vi_pipe);
    if (ae_sns_dft == TD_NULL) return;

    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return;

    if (f32_fps <= 0.0f) return;

    /* Scale VTS proportionally: VTS_new = VTS_def * FPS_def / FPS_new */
    vts = (td_u32)(SC635HAI_VTS_DEF * SC635HAI_FPS_DEF / f32_fps);
    if (vts > SC635HAI_VTS_MAX) vts = SC635HAI_VTS_MAX;
    if (vts < SC635HAI_VTS_DEF) vts = SC635HAI_VTS_DEF;

    state->fl_std = vts;
    state->fl[0]  = vts;

    /* Update VTS registers in ISP sync buffer */
    state->regs_info[0].i2c_data[LINEAR_VTS_H].data = (vts >> 8) & 0xFF;
    state->regs_info[0].i2c_data[LINEAR_VTS_L].data = vts & 0xFF;

    /* Update AE limits */
    ae_sns_dft->full_lines_std = vts;
    ae_sns_dft->max_int_time   = vts - SC635HAI_EXP_OFFSET;
    ae_sns_dft->lines_per500ms = vts * f32_fps / 2;
    ae_sns_dft->fps            = f32_fps;
}

/* Slow framerate for long exposure (extend VTS beyond default) */
static td_void cmos_slow_framerate_set(ot_vi_pipe vi_pipe,
    td_u32 full_lines, ot_isp_ae_sensor_default *ae_sns_dft)
{
    ot_isp_sns_state *state;
    td_u32 vts;

    SC635HAI_CHECK_PIPE_VOID(vi_pipe);
    if (ae_sns_dft == TD_NULL) return;

    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return;

    vts = full_lines;
    if (vts > SC635HAI_VTS_MAX) vts = SC635HAI_VTS_MAX;
    if (vts < SC635HAI_VTS_DEF) vts = SC635HAI_VTS_DEF;

    state->fl[0] = vts;

    state->regs_info[0].i2c_data[LINEAR_VTS_H].data = (vts >> 8) & 0xFF;
    state->regs_info[0].i2c_data[LINEAR_VTS_L].data = vts & 0xFF;

    ae_sns_dft->full_lines = vts;
    ae_sns_dft->max_int_time = vts - SC635HAI_EXP_OFFSET;
}

/* Write exposure time to sensor registers.
 *
 * Exposure is in half-line units, encoded across 3 registers:
 *   0x3E00[3:0] = bits [15:12]
 *   0x3E01[7:0] = bits [11:4]
 *   0x3E02[7:4] = bits [3:0] << 4
 */
static td_u32 g_inttime_dbg_cnt = 0;

static td_void cmos_inttime_update(ot_vi_pipe vi_pipe, td_u32 int_time)
{
    ot_isp_sns_state *state;
    td_u32 max_exp;

    SC635HAI_CHECK_PIPE_VOID(vi_pipe);
    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return;

    /* Debug: print first 10 and every 100th AE request to track convergence */
    if (g_inttime_dbg_cnt < 10 || (g_inttime_dbg_cnt % 100) == 0) {
        printf("sc635hai: inttime_update #%u: int_time=%u, fl=%u\n",
               g_inttime_dbg_cnt, int_time, state->fl[0]);
    }
    g_inttime_dbg_cnt++;

    /* Clamp to valid range (single-line max, matches superb) */
    max_exp = state->fl[0] - SC635HAI_EXP_OFFSET;
    if (int_time > max_exp) int_time = max_exp;
    if (int_time < SC635HAI_EXP_MIN) int_time = SC635HAI_EXP_MIN;

    /* Populate regs_info[0] for the ISP kernel sync path (ot_sensor_i2c.ko).
     * Note: this path is NOT functional in our setup -- see direct write below. */
    state->regs_info[0].i2c_data[LINEAR_EXP_H].data =
        (int_time >> 12) & 0x0F;
    state->regs_info[0].i2c_data[LINEAR_EXP_M].data =
        (int_time >> 4) & 0xFF;
    state->regs_info[0].i2c_data[LINEAR_EXP_L].data =
        (int_time & 0x0F) << 4;

    /* Direct I2C write -- DISABLED for force-update test 2026-05-14. */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_EXP_H, (int_time >> 12) & 0x0F); */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_EXP_M, (int_time >> 4) & 0xFF); */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_EXP_L, (int_time & 0x0F) << 4); */
}

/* Write analog + digital gain to sensor registers.
 *
 * again: index into g_again_table (0-288)
 * dgain: linear digital gain (1024 = 1.0x)
 */
static td_u32 g_gains_dbg_cnt = 0;

static td_void cmos_gains_update(ot_vi_pipe vi_pipe,
    td_u32 again, td_u32 dgain)
{
    ot_isp_sns_state *state;
    const struct sc635hai_again_info *info;
    td_u32 total_gain;
    td_u32 dgain_base, dgain_reg, dgain_fine;
    int i;

    SC635HAI_CHECK_PIPE_VOID(vi_pipe);
    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return;

    /* Debug: print first 10 and every 20th gain request */
    if (g_gains_dbg_cnt < 10 || (g_gains_dbg_cnt % 20) == 0) {
        printf("sc635hai: gains_update #%u: again_idx=%u, dgain=%u\n",
               g_gains_dbg_cnt, again, dgain);
    }
    g_gains_dbg_cnt++;

    /* ── Analog gain ────────────────────────────────────────── */

    /* Find which coarse range contains this index */
    info = &g_again_info[0];
    for (i = SC635HAI_AGAIN_RANGES - 1; i >= 0; i--) {
        if (again >= g_again_info[i].idx_base) {
            info = &g_again_info[i];
            break;
        }
    }

    /* Write coarse gain register (0x3E08) */
    state->regs_info[0].i2c_data[LINEAR_AGAIN_COARSE].data =
        info->reg_coarse;

    /* Write fine gain register (0x3E09) = base + (index - range_start) */
    state->regs_info[0].i2c_data[LINEAR_AGAIN_FINE].data =
        info->reg_fine_base + (again - info->idx_base) * info->reg_fine_step;

    /* ── Digital gain ───────────────────────────────────────── */

    /* Find power-of-2 coarse base */
    if (dgain < 2048)       { dgain_base = 1024;  dgain_reg = 0x00; }
    else if (dgain < 4096)  { dgain_base = 2048;  dgain_reg = 0x01; }
    else if (dgain < 8192)  { dgain_base = 4096;  dgain_reg = 0x03; }
    else if (dgain < 16384) { dgain_base = 8192;  dgain_reg = 0x07; }
    else                    { dgain_base = 16384; dgain_reg = 0x0F; }

    /* Fine = linear_gain * 128 / coarse_base (range 128..255) */
    dgain_fine = dgain * 128 / dgain_base;
    if (dgain_fine > 255) dgain_fine = 255;
    if (dgain_fine < 128) dgain_fine = 128;

    /* Write coarse dgain (0x3E06) and fine dgain (0x3E07) */
    state->regs_info[0].i2c_data[LINEAR_DGAIN_COARSE].data = dgain_reg;
    state->regs_info[0].i2c_data[LINEAR_DGAIN_FINE].data   = dgain_fine;

    /* Direct gain writes -- DISABLED for force-update test 2026-05-14. */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_GROUP_HOLD, SC635HAI_GROUP_HOLD_START); */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_AGAIN_COARSE, info->reg_coarse); */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_AGAIN_FINE,
        info->reg_fine_base + (again - info->idx_base) * info->reg_fine_step); */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_DGAIN_COARSE, dgain_reg); */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_DGAIN_FINE, dgain_fine); */
    /* sc635hai_write_register(vi_pipe, SC635HAI_REG_GROUP_HOLD, SC635HAI_GROUP_HOLD_END); */

    /* ── DPC noise fix (hysteresis at high gain) ────────────── */
    total_gain = g_again_table[again] * dgain / 1024;

    if (total_gain >= SC635HAI_DPC_GAIN_HIGH && !g_dpc_enabled[vi_pipe]) {
        sc635hai_write_register(vi_pipe, SC635HAI_REG_DPC, 0x07);
        g_dpc_enabled[vi_pipe] = TD_TRUE;
    } else if (total_gain <= SC635HAI_DPC_GAIN_LOW && g_dpc_enabled[vi_pipe]) {
        sc635hai_write_register(vi_pipe, SC635HAI_REG_DPC, 0x00);
        g_dpc_enabled[vi_pipe] = TD_FALSE;
    }
}

/* Analog gain: linear value -> table index lookup */
static td_void cmos_again_calc_table(ot_vi_pipe vi_pipe,
    td_u32 *again_lin, td_u32 *again_db)
{
    int i;
    (void)vi_pipe;

    if (again_lin == TD_NULL || again_db == TD_NULL) return;

    /* Clamp to max */
    if (*again_lin >= g_again_table[SC635HAI_AGAIN_TBL_SIZE - 1]) {
        *again_lin = g_again_table[SC635HAI_AGAIN_TBL_SIZE - 1];
        *again_db = SC635HAI_AGAIN_TBL_SIZE - 1;
        return;
    }

    /* Linear search for largest entry not exceeding requested gain */
    for (i = 1; i < SC635HAI_AGAIN_TBL_SIZE; i++) {
        if (*again_lin < g_again_table[i]) {
            *again_lin = g_again_table[i - 1];
            *again_db = i - 1;
            return;
        }
    }
}

/* Digital gain: quantize to nearest 1/128 step within power-of-2 range */
static td_void cmos_dgain_calc_table(ot_vi_pipe vi_pipe,
    td_u32 *dgain_lin, td_u32 *dgain_db)
{
    td_u32 dgain, dgain_base;
    (void)vi_pipe;

    if (dgain_lin == TD_NULL || dgain_db == TD_NULL) return;

    dgain = *dgain_lin;

    /* Clamp */
    if (dgain > SC635HAI_DGAIN_MAX) dgain = SC635HAI_DGAIN_MAX;
    if (dgain < SC635HAI_DGAIN_MIN) dgain = SC635HAI_DGAIN_MIN;

    /* Find coarse base */
    if (dgain < 2048)       dgain_base = 1024;
    else if (dgain < 4096)  dgain_base = 2048;
    else if (dgain < 8192)  dgain_base = 4096;
    else if (dgain < 16384) dgain_base = 8192;
    else                    dgain_base = 16384;

    /* Quantize to 1/128 step */
    dgain = dgain_base * (dgain * 128 / dgain_base) / 128;

    *dgain_lin = dgain;
    *dgain_db  = dgain;  /* linear value, not an index */
}

/* Max integration time (for WDR ratio calculations) */
static td_void cmos_get_inttime_max(ot_vi_pipe vi_pipe,
    td_u16 man_ratio_enable, td_u32 *ratio,
    ot_isp_ae_int_time_range *int_time, td_u32 *lf_max_int_time)
{
    ot_isp_sns_state *state;

    (void)man_ratio_enable;
    (void)ratio;

    SC635HAI_CHECK_PIPE_VOID(vi_pipe);
    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL || int_time == TD_NULL || lf_max_int_time == TD_NULL)
        return;

    /* Linear mode: max = VTS - 10 (matches cmos_inttime_update clamp) */
    int_time->int_time_max[0] = state->fl[0] - SC635HAI_EXP_OFFSET;
    int_time->int_time_min[0] = SC635HAI_EXP_MIN;
    *lf_max_int_time = int_time->int_time_max[0];
}

/* Frame-sync WDR AE attributes (stub for linear mode) */
static td_void cmos_ae_fswdr_attr_set(ot_vi_pipe vi_pipe,
    ot_isp_ae_fswdr_attr *ae_fswdr_attr)
{
    (void)vi_pipe;
    (void)ae_fswdr_attr;
}

/* ════════════════════════════════════════════════════════════════
 *  AWB SENSOR CALLBACK
 *
 *  SC635HAI-specific AWB calibration data extracted from the manufacturer's
 *  "superb" firmware running ISP via read-only query APIs (awb_dump tool).
 *
 *  Contains:
 *    - Planckian locus curve fitting (wb_para p1=-31, p2=287, a=187899, c=-137074)
 *    - CCM matrices at 4 color temperatures (2640K, 3850K, 4950K, 6350K)
 *    - Saturation rolloff table per ISO (140 at low ISO, 90 at max)
 *    - Static WB gain offset at ref temp 4950K (R=477, B=535)
 *    - Initial WB gains from superb's converged daylight state (R=523, B=538)
 *
 *  Extraction method: awb_dump tool queries superb's running ISP on pipe 0
 *  using ss_mpi_isp_get_wb_attr, ss_mpi_isp_get_ccm_attr, etc. Requires
 *  LD_PRELOAD of ISP algorithm plugins. See driver/test/awb_dump.c.
 *
 *  Key platform notes:
 *    - SC635HAI QE differs significantly from SC4336P (R=1.86x vs 1.60x at 4950K)
 *    - Planckian curve shape is unique (p1=-31 vs SC4336P p1=+36)
 *    - AWB must use ADVANCE algorithm (set in pipeline_test.c, not here)
 *    - PQ bin does NOT load AWB calibration -- it only comes from this driver
 * ════════════════════════════════════════════════════════════════ */

/* CCM matrices at 4 color temperatures (extracted from superb's running ISP).
 * Format: 8.8 signed -- bit15 = sign, [14:8] = integer, [7:0] = fraction.
 * The AWB algorithm interpolates between these based on detected CT. */
static ot_isp_awb_ccm g_awb_ccm = {
    /* ccm_tab_num: 4 active matrices, 3 identity fallbacks */
    4,
    {
        {
            6350,  /* D65 daylight (~6350K) */
            { 0x01D3, 0x80C3, 0x8010,   /* R' = +1.824*R - 0.762*G - 0.062*B */
              0x8057, 0x01F1, 0x809A,   /* G' = -0.340*R + 1.941*G - 0.602*B */
              0x8002, 0x80AF, 0x01B1 }, /* B' = -0.008*R - 0.684*G + 1.691*B */
        },
        {
            4950,  /* CWF / D50 (~4950K) */
            { 0x01D8, 0x80C5, 0x8013,
              0x8067, 0x0206, 0x809F,
              0x000A, 0x80ED, 0x01E3 },
        },
        {
            3850,  /* TL84 / warm fluorescent (~3850K) */
            { 0x01E3, 0x80EF, 0x000C,
              0x8066, 0x016C, 0x8006,
              0x000D, 0x8130, 0x0223 },
        },
        {
            2640,  /* Incandescent / warm tungsten (~2640K) */
            { 0x01D6, 0x80E7, 0x0011,
              0x8073, 0x0179, 0x8006,
              0x0014, 0x8214, 0x0300 },
        },
        /* Unused slots: identity matrices */
        { 2100, { 0x0100, 0, 0, 0, 0x0100, 0, 0, 0, 0x0100 } },
        { 1600, { 0x0100, 0, 0, 0, 0x0100, 0, 0, 0, 0x0100 } },
        { 1400, { 0x0100, 0, 0, 0, 0x0100, 0, 0, 0, 0x0100 } },
    },
};

/* Saturation rolloff by ISO (extracted from superb).
 * 16 entries for ISO steps: 1x,2x,4x,8x,16x,32x,64x,128x,...,32768x
 * Starts at 140 (slightly above neutral 128), rolls off at high ISO. */
static ot_isp_awb_agc_table g_awb_agc_table = {
    1,  /* valid */
    { 140, 132, 128, 128, 124, 120, 110, 105, 100, 100, 100, 94, 90, 90, 90, 90 }
};

static td_s32 cmos_get_awb_default(ot_vi_pipe vi_pipe,
    ot_isp_awb_sensor_default *awb_sns_dft)
{
    (void)vi_pipe;
    if (awb_sns_dft == TD_NULL) return OT_ERR_ISP_NULL_PTR;
    memset(awb_sns_dft, 0, sizeof(ot_isp_awb_sensor_default));

    /* Reference color temperature and static WB gain offset.
     * gain_offset[] is the WB gain needed at wb_ref_temp (~4950K D50).
     * Extracted from superb's running ISP (static_wb[] from wb_attr). */
    awb_sns_dft->wb_ref_temp      = 4950;
    awb_sns_dft->gain_offset[0]   = 477;   /* R ~1.86x at 4950K */
    awb_sns_dft->gain_offset[1]   = 256;   /* Gr 1.0x */
    awb_sns_dft->gain_offset[2]   = 256;   /* Gb 1.0x */
    awb_sns_dft->gain_offset[3]   = 535;   /* B ~2.09x at 4950K */

    /* Planckian locus curve fit parameters.
     * Used by AWB algorithm to convert R/B gain ratios to color temperature.
     * The curve equation: R/G = f(1/CT) with polynomial coefficients.
     * Extracted from superb's running ISP (curve_para[] from wb_attr). */
    awb_sns_dft->wb_para[0]       = -31;      /* p1 */
    awb_sns_dft->wb_para[1]       = 287;      /* p2 */
    awb_sns_dft->wb_para[2]       = 0;        /* q1 */
    awb_sns_dft->wb_para[3]       = 187899;   /* a  */
    awb_sns_dft->wb_para[4]       = 128;      /* b  */
    awb_sns_dft->wb_para[5]       = -137074;  /* c  */

    /* Golden sample correction (0 = not calibrated per-unit) */
    awb_sns_dft->golden_rgain     = 0;
    awb_sns_dft->golden_bgain     = 0;

    /* CCM and AGC saturation table */
    memcpy(&awb_sns_dft->ccm, &g_awb_ccm, sizeof(ot_isp_awb_ccm));
    memcpy(&awb_sns_dft->agc_tbl, &g_awb_agc_table, sizeof(ot_isp_awb_agc_table));

    /* Initial WB gains (from superb's converged state at ~5500K) */
    awb_sns_dft->init_rgain       = 523;   /* ~2.04x */
    awb_sns_dft->init_ggain       = 256;   /* 1.0x */
    awb_sns_dft->init_bgain       = 538;   /* ~2.10x */
    awb_sns_dft->awb_run_interval = 2;     /* run AWB every 2nd frame (matches superb) */

    return TD_SUCCESS;
}

/* ════════════════════════════════════════════════════════════════
 *  CALLBACK INITIALIZATION (fills function pointer structs)
 * ════════════════════════════════════════════════════════════════ */

static td_s32 cmos_init_sensor_exp_function(ot_isp_sns_exp_func *exp_func)
{
    if (exp_func == TD_NULL) return TD_FAILURE;
    memset(exp_func, 0, sizeof(ot_isp_sns_exp_func));

    exp_func->pfn_cmos_sns_init          = cmos_sns_init;
    exp_func->pfn_cmos_sns_exit          = cmos_sns_exit;
    exp_func->pfn_cmos_sns_global_init   = cmos_sns_global_init;
    exp_func->pfn_cmos_set_image_mode    = cmos_set_image_mode;
    exp_func->pfn_cmos_set_wdr_mode      = cmos_set_wdr_mode;
    exp_func->pfn_cmos_get_isp_default   = cmos_get_isp_default;
    exp_func->pfn_cmos_get_isp_black_level = cmos_get_isp_black_level;
    exp_func->pfn_cmos_get_blc_clamp_info = cmos_get_blc_clamp_info;
    exp_func->pfn_cmos_get_sns_reg_info  = cmos_get_sns_reg_info;
    exp_func->pfn_cmos_set_pixel_detect  = cmos_set_pixel_detect;
    exp_func->pfn_cmos_get_awb_gains     = cmos_get_awb_gains;

    return TD_SUCCESS;
}

/* Quick-start status callback: AE algorithm calls this when quick_start_en
 * is set via ss_mpi_isp_set_ctrl_param. Reference: sc4336p_cmos.c:483.
 * Resets sync_init so the next get_sns_reg_info re-populates regs_info[0]
 * with full register set + update flags. */
static td_void cmos_ae_quick_start_status_set(ot_vi_pipe vi_pipe, td_bool quick_start_en)
{
    ot_isp_sns_state *state;

    SC635HAI_CHECK_PIPE_VOID(vi_pipe);
    state = SC635HAI_GET_STATE(vi_pipe);
    if (state == TD_NULL) return;
    (void)quick_start_en;
    state->sync_init = TD_FALSE;
}

/* Fast-AE attribute query: tells the AE algorithm how many frames of
 * sensor pipeline delay to compensate for. Reference: sc4336p_cmos.c:498.
 * Returning enable=0 means fast AE is disabled (we don't try to do the
 * extra-fast convergence path). */
static td_void cmos_ae_fast_ae_attr_get(ot_vi_pipe vi_pipe,
    ot_isp_ae_fast_ae_attr *fast_ae_attr)
{
    SC635HAI_CHECK_PIPE_VOID(vi_pipe);
    if (fast_ae_attr == TD_NULL) return;
    fast_ae_attr->enable = TD_FALSE;
    fast_ae_attr->sns_delay_frame = 1 + 2;  /* delay 1 + 2 frame */
}

/* Fast-AE attribute set: stub (no-op, fast AE disabled). */
static td_void cmos_ae_fast_ae_attr_set(ot_vi_pipe vi_pipe,
    const ot_isp_ae_fast_ae_attr *fast_ae_attr)
{
    SC635HAI_CHECK_PIPE_VOID(vi_pipe);
    if (fast_ae_attr == TD_NULL) return;
    (void)fast_ae_attr;  /* no-op */
}

static td_s32 cmos_init_ae_exp_function(ot_isp_ae_sensor_exp_func *ae_func)
{
    if (ae_func == TD_NULL) return TD_FAILURE;
    memset(ae_func, 0, sizeof(ot_isp_ae_sensor_exp_func));

    ae_func->pfn_cmos_get_ae_default              = cmos_get_ae_default;
    ae_func->pfn_cmos_fps_set                     = cmos_fps_set;
    ae_func->pfn_cmos_slow_framerate_set          = cmos_slow_framerate_set;
    ae_func->pfn_cmos_inttime_update              = cmos_inttime_update;
    ae_func->pfn_cmos_gains_update                = cmos_gains_update;
    ae_func->pfn_cmos_again_calc_table            = cmos_again_calc_table;
    ae_func->pfn_cmos_dgain_calc_table            = cmos_dgain_calc_table;
    ae_func->pfn_cmos_get_inttime_max             = cmos_get_inttime_max;
    ae_func->pfn_cmos_ae_fswdr_attr_set           = cmos_ae_fswdr_attr_set;
    ae_func->pfn_cmos_ae_quick_start_status_set   = cmos_ae_quick_start_status_set;
    ae_func->pfn_cmos_ae_fast_ae_attr_get         = cmos_ae_fast_ae_attr_get;
    ae_func->pfn_cmos_ae_fast_ae_attr_set         = cmos_ae_fast_ae_attr_set;
    /* pfn_cmos_exp_param_convert = NULL (not needed) */
    /* pfn_cmos_get_thermo_default = NULL (no thermal sensor) */

    return TD_SUCCESS;
}

static td_s32 cmos_init_awb_exp_function(ot_isp_awb_sensor_exp_func *awb_func)
{
    if (awb_func == TD_NULL) return TD_FAILURE;
    memset(awb_func, 0, sizeof(ot_isp_awb_sensor_exp_func));

    awb_func->pfn_cmos_get_awb_default = cmos_get_awb_default;

    return TD_SUCCESS;
}

/* ════════════════════════════════════════════════════════════════
 *  SENSOR REGISTRATION (the bridge to the ISP framework)
 * ════════════════════════════════════════════════════════════════ */

static td_s32 sensor_register_callback(ot_vi_pipe vi_pipe,
    ot_isp_3a_alg_lib *ae_lib, ot_isp_3a_alg_lib *awb_lib)
{
    td_s32 ret;
    ot_isp_sns_register       isp_register;
    ot_isp_ae_sensor_register ae_register;
    ot_isp_awb_sensor_register awb_register;
    ot_isp_sns_attr_info      sns_attr_info;

    SC635HAI_CHECK_PIPE(vi_pipe);
    if (ae_lib == TD_NULL || awb_lib == TD_NULL)
        return OT_ERR_ISP_NULL_PTR;

    ret = sensor_ctx_init(vi_pipe);
    if (ret != TD_SUCCESS) return TD_FAILURE;

    sns_attr_info.sns_id = SC635HAI_ID;

    /* Register ISP sensor callbacks */
    cmos_init_sensor_exp_function(&isp_register.sns_exp);
    ret = ss_mpi_isp_sensor_reg_callback(vi_pipe, &sns_attr_info, &isp_register);
    if (ret != TD_SUCCESS) {
        printf("sc635hai: ISP sensor reg failed (%d)\n", ret);
        sensor_ctx_exit(vi_pipe);
        return ret;
    }

    /* Register AE sensor callbacks */
    cmos_init_ae_exp_function(&ae_register.sns_exp);
    ret = ss_mpi_ae_sensor_reg_callback(vi_pipe, ae_lib, &sns_attr_info,
                                         &ae_register);
    if (ret != TD_SUCCESS) {
        printf("sc635hai: AE sensor reg failed (%d)\n", ret);
        ss_mpi_isp_sensor_unreg_callback(vi_pipe, SC635HAI_ID);
        sensor_ctx_exit(vi_pipe);
        return ret;
    }

    /* Register AWB sensor callbacks */
    cmos_init_awb_exp_function(&awb_register.sns_exp);
    ret = ss_mpi_awb_sensor_reg_callback(vi_pipe, awb_lib, &sns_attr_info,
                                          &awb_register);
    if (ret != TD_SUCCESS) {
        printf("sc635hai: AWB sensor reg failed (%d)\n", ret);
        ss_mpi_ae_sensor_unreg_callback(vi_pipe, ae_lib, SC635HAI_ID);
        ss_mpi_isp_sensor_unreg_callback(vi_pipe, SC635HAI_ID);
        sensor_ctx_exit(vi_pipe);
        return ret;
    }

    printf("sc635hai: registered on pipe %d\n", vi_pipe);
    return TD_SUCCESS;
}

static td_s32 sensor_unregister_callback(ot_vi_pipe vi_pipe,
    ot_isp_3a_alg_lib *ae_lib, ot_isp_3a_alg_lib *awb_lib)
{
    SC635HAI_CHECK_PIPE(vi_pipe);
    if (ae_lib == TD_NULL || awb_lib == TD_NULL)
        return OT_ERR_ISP_NULL_PTR;

    ss_mpi_isp_sensor_unreg_callback(vi_pipe, SC635HAI_ID);
    ss_mpi_ae_sensor_unreg_callback(vi_pipe, ae_lib, SC635HAI_ID);
    ss_mpi_awb_sensor_unreg_callback(vi_pipe, awb_lib, SC635HAI_ID);

    sensor_ctx_exit(vi_pipe);

    printf("sc635hai: unregistered from pipe %d\n", vi_pipe);
    return TD_SUCCESS;
}

/* ════════════════════════════════════════════════════════════════
 *  ot_isp_sns_obj -- TOP-LEVEL SENSOR OBJECT (ENTRY POINT)
 *
 *  This struct is loaded by dlopen("libsns_sc635hai.so") +
 *  dlsym("g_sns_sc635hai_obj"). It provides all 12 function
 *  pointers the ISP framework needs to control the sensor
 *  (V1.0.2.1 SDK added pfn_set_fast_ae as the 12th).
 * ════════════════════════════════════════════════════════════════ */

/* Bus info wrapper (called by ISP framework before registration) */
static td_s32 sensor_set_bus_info(ot_vi_pipe vi_pipe,
    ot_isp_sns_commbus sns_bus_info)
{
    SC635HAI_CHECK_PIPE(vi_pipe);
    g_bus_info[vi_pipe] = sns_bus_info;
    sc635hai_set_bus_info(vi_pipe, (td_u8)sns_bus_info.i2c_dev);
    return TD_SUCCESS;
}

/* Bus extended info (not used) */
static td_s32 sensor_set_bus_ex_info(ot_vi_pipe vi_pipe,
    ot_isp_sns_bus_ex *serdes_info)
{
    (void)vi_pipe;
    (void)serdes_info;
    return TD_SUCCESS;
}

/* Standby wrapper */
static td_void sensor_standby(ot_vi_pipe vi_pipe)
{
    sc635hai_standby(vi_pipe);
}

/* Restart wrapper */
static td_void sensor_restart(ot_vi_pipe vi_pipe)
{
    sc635hai_restart(vi_pipe);
}

/* Mirror/flip wrapper */
static td_void sensor_mirror_flip(ot_vi_pipe vi_pipe,
    ot_isp_sns_mirrorflip_type sns_mirror_flip)
{
    sc635hai_mirror_flip(vi_pipe, (td_u32)sns_mirror_flip);
}

/* BLC clamp mode (not used) */
static td_void sensor_set_blc_clamp(ot_vi_pipe vi_pipe,
    ot_isp_sns_blc_clamp sns_blc_clamp)
{
    (void)vi_pipe;
    (void)sns_blc_clamp;
}

/* Direct register write (for debug/manual control) */
static td_s32 sensor_write_reg(ot_vi_pipe vi_pipe, td_u32 addr, td_u32 data)
{
    return sc635hai_write_register(vi_pipe, addr, data);
}

/* Direct register read */
static td_s32 sensor_read_reg(ot_vi_pipe vi_pipe, td_u32 addr)
{
    return sc635hai_read_register(vi_pipe, addr);
}

/* Set initial attributes (exposure, gain for quick start) */
static td_s32 sensor_set_init(ot_vi_pipe vi_pipe,
    ot_isp_init_attr *init_attr)
{
    (void)vi_pipe;
    (void)init_attr;
    return TD_SUCCESS;
}

/* ── THE EXPORTED SENSOR OBJECT ────────────────────────────────── */

ot_isp_sns_obj g_sns_sc635hai_obj = {
    .pfn_register_callback   = sensor_register_callback,
    .pfn_un_register_callback = sensor_unregister_callback,
    .pfn_set_bus_info        = sensor_set_bus_info,
    .pfn_set_bus_ex_info     = sensor_set_bus_ex_info,
    .pfn_standby             = sensor_standby,
    .pfn_restart             = sensor_restart,
    .pfn_mirror_flip         = sensor_mirror_flip,
    .pfn_set_blc_clamp       = sensor_set_blc_clamp,
    .pfn_write_reg           = sensor_write_reg,
    .pfn_read_reg            = sensor_read_reg,
    .pfn_set_init            = sensor_set_init,
    .pfn_set_fast_ae         = TD_NULL,  /* V1.0.2.1 SDK: new field */
};
