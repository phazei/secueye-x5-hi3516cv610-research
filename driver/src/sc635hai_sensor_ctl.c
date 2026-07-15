/*
 * SC635HAI Sensor Control -- I2C Communication and Register Init
 *
 * Handles low-level I2C read/write to the SC635HAI sensor and provides
 * the register initialization sequence for linear 3200x1800 30fps 10-bit mode.
 *
 * Init sequence uses the "snapshot driver" approach: all non-zero register
 * values from the live I2C dump are written in standard SmartSens order:
 *   reset -> standby -> PLL bypass -> core regs -> PLL enable -> stream on
 *
 * Reference: research/SC635HAI_SENSOR_ANALYSIS.md
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <linux/i2c-dev.h>

#include "ot_type.h"
#include "ot_common.h"
#include "sc635hai_cmos.h"

#ifndef I2C_SLAVE_FORCE
#define I2C_SLAVE_FORCE 0x0706
#endif

/* ── Per-pipe state ────────────────────────────────────────────── */
#define VI_MAX_PIPE_NUM  4

static int g_fd[VI_MAX_PIPE_NUM] = { -1, -1, -1, -1 };
static td_u8 g_i2c_dev[VI_MAX_PIPE_NUM] = { 0 };  /* default: /dev/i2c-0 */

/* ── I2C init/exit ─────────────────────────────────────────────── */

int sc635hai_i2c_init(ot_vi_pipe vi_pipe)
{
    char dev_file[32];
    int ret;

    if (vi_pipe < 0 || vi_pipe >= VI_MAX_PIPE_NUM)
        return TD_FAILURE;

    if (g_fd[vi_pipe] >= 0)
        return TD_SUCCESS;

    snprintf(dev_file, sizeof(dev_file), "/dev/i2c-%u", g_i2c_dev[vi_pipe]);
    g_fd[vi_pipe] = open(dev_file, O_RDWR, 0600);
    if (g_fd[vi_pipe] < 0) {
        printf("sc635hai: failed to open %s\n", dev_file);
        return TD_FAILURE;
    }

    ret = ioctl(g_fd[vi_pipe], I2C_SLAVE_FORCE, SC635HAI_I2C_ADDR);
    if (ret < 0) {
        printf("sc635hai: I2C_SLAVE_FORCE 0x%02x failed\n", SC635HAI_I2C_ADDR);
        close(g_fd[vi_pipe]);
        g_fd[vi_pipe] = -1;
        return TD_FAILURE;
    }

    return TD_SUCCESS;
}

int sc635hai_i2c_exit(ot_vi_pipe vi_pipe)
{
    if (vi_pipe < 0 || vi_pipe >= VI_MAX_PIPE_NUM)
        return TD_FAILURE;

    if (g_fd[vi_pipe] >= 0) {
        close(g_fd[vi_pipe]);
        g_fd[vi_pipe] = -1;
    }
    return TD_SUCCESS;
}

/* ── I2C read/write ────────────────────────────────────────────── */

int sc635hai_write_register(ot_vi_pipe vi_pipe, td_u32 addr, td_u32 data)
{
    td_u8 buf[3];
    int ret;

    if (vi_pipe < 0 || vi_pipe >= VI_MAX_PIPE_NUM || g_fd[vi_pipe] < 0)
        return TD_FAILURE;

    buf[0] = (addr >> 8) & 0xFF;   /* reg addr high */
    buf[1] = addr & 0xFF;          /* reg addr low */
    buf[2] = data & 0xFF;          /* data */

    ret = write(g_fd[vi_pipe], buf, 3);
    if (ret != 3) {
        printf("sc635hai: i2c write 0x%04x=0x%02x failed (ret=%d)\n",
               addr, data, ret);
        return TD_FAILURE;
    }

    return TD_SUCCESS;
}

int sc635hai_read_register(ot_vi_pipe vi_pipe, td_u32 addr)
{
    td_u8 buf[2];
    int ret;

    if (vi_pipe < 0 || vi_pipe >= VI_MAX_PIPE_NUM || g_fd[vi_pipe] < 0)
        return TD_FAILURE;

    /* Write register address */
    buf[0] = (addr >> 8) & 0xFF;
    buf[1] = addr & 0xFF;
    ret = write(g_fd[vi_pipe], buf, 2);
    if (ret < 0)
        return TD_FAILURE;

    /* Read data */
    buf[0] = 0;
    ret = read(g_fd[vi_pipe], buf, 1);
    if (ret < 0)
        return TD_FAILURE;

    return buf[0];
}

/* ── Standby / Restart ─────────────────────────────────────────── */

void sc635hai_standby(ot_vi_pipe vi_pipe)
{
    sc635hai_write_register(vi_pipe, SC635HAI_REG_STREAM, 0x00);
}

void sc635hai_restart(ot_vi_pipe vi_pipe)
{
    sc635hai_write_register(vi_pipe, SC635HAI_REG_STREAM, 0x00);
    usleep(20 * 1000);
    sc635hai_write_register(vi_pipe, SC635HAI_REG_STREAM, 0x01);
}

/* ── Bus info ──────────────────────────────────────────────────── */

void sc635hai_set_bus_info(ot_vi_pipe vi_pipe, td_u8 i2c_dev)
{
    if (vi_pipe >= 0 && vi_pipe < VI_MAX_PIPE_NUM)
        g_i2c_dev[vi_pipe] = i2c_dev;
}

/* ── Register init sequence: Linear 3200x1800 30fps 10bit ──────
 *
 * All values from live I2C dump of running camera (see sensor analysis).
 * Order follows the standard SmartSens pattern confirmed from SC500AI:
 *
 *   1. Software reset (0x0103=0x01)
 *   2. Standby (0x0100=0x00)
 *   3. PLL bypass (0x36E9=0x80)
 *   4. Core configuration registers (all banks)
 *   5. PLL re-enable (0x36E9=actual value)
 *   6. Default reg init (ISP-tracked AE registers)
 *   7. Stream on (0x0100=0x01)
 *
 * Note: Register 0x3107/0x3108 (chip ID) are read-only and not written.
 * AE registers (0x3E00-0x3E09) are not in the init sequence because
 * the ISP framework writes them via cmos_gains_update/cmos_inttime_update.
 */

/* {register, value} pairs */
static const td_u16 g_sc635hai_linear_init[][2] = {
    /* ── Step 0: Sync test mode + reset + standby + clear ──────
     * From Rockchip SC635HAI 2-lane init prefix. Required before
     * the main register block to put sensor in correct state. */
    { 0x3105, 0x32 },
    { 0x0103, 0x01 },
    { 0x0100, 0x00 },
    { 0x302C, 0x0C },
    { 0x302C, 0x00 },
    { 0x3105, 0x12 },

    /* ── Step 1: PLL bypass (both PLLs) ────────────────────────
     * Critical: SC635HAI has TWO PLLs that both must be bypassed
     * before writing core regs, then re-enabled afterward.
     * Missing 0x37F9 (PLL2) was a root cause of MIPI TX silence. */
    { 0x36E9, 0x80 },
    { 0x37F9, 0x80 },

    /* ── Step 2: Core registers (Rockchip 2-lane reference) ──────
     * Based on sc635hai_linear_10_3200x1800_30fps_2lane_regs[] from
     * the Rockchip V4L2 driver (research/sc635hai_rockchip_v4l2.c).
     * VTS adjusted to 2812 (~20fps) to match superb's actual config.
     * Includes MIPI CSI-2 protocol regs (0x48xx) that were absent
     * from the previous live I2C dump approach. */
#include "sc635hai_init_regs.inc"

    /* ── Step 3: PLL re-enable (both PLLs, final values) ───────── */
    { 0x36E9, 0x24 },
    { 0x37F9, 0x24 },

    /* ── Step 4: Stream on ─────────────────────────────────────── */
    { 0x0100, 0x01 },
};

#define SC635HAI_LINEAR_INIT_SIZE \
    (sizeof(g_sc635hai_linear_init) / sizeof(g_sc635hai_linear_init[0]))

/* ── Init function called by ISP framework ─────────────────────── */

void sc635hai_linear_6m30_10bit_init(ot_vi_pipe vi_pipe)
{
    td_u32 i;

    printf("sc635hai: writing %u init registers\n", (unsigned)SC635HAI_LINEAR_INIT_SIZE);

    for (i = 0; i < SC635HAI_LINEAR_INIT_SIZE; i++) {
        td_u16 reg = g_sc635hai_linear_init[i][0];

        sc635hai_write_register(vi_pipe, reg, g_sc635hai_linear_init[i][1]);

        /* Settle delays:
         *   0x0103 -- after software reset, sensor needs ~10ms to come up
         *   0x36E9 -- PLL1 enable: must let PLL lock before further writes
         *   0x37F9 -- PLL2 enable: same as above
         *   0x0100 -- stream on: previous reg in table is 0x37F9=0x24
         *             (PLL2 enable). Need PLL lock time (~100us per
         *             datasheet, but other SC sensors want ~10ms) before
         *             stream-on actually starts MIPI TX.
         *
         * Past failure mode: with no delay between PLL enable and
         * stream-on, MIPI PHY stays in LP11 even though 0x0100=0x01
         * reads back correctly.
         */
        if (reg == 0x0103 || reg == 0x36E9 || reg == 0x37F9) {
            usleep(10 * 1000);  /* 10ms PLL lock / reset settle */
        }
        if (reg == 0x0100) {
            usleep(50 * 1000);  /* 50ms post-stream-on so MIPI TX engages */
        }
    }

    printf("sc635hai: init complete\n");
}

/* ── Default reg init: apply ISP-tracked registers ─────────────
 *
 * Called after mode init to write any registers the ISP framework
 * has queued. This is the bridge between the ISP's register shadow
 * and the actual sensor hardware.
 */
void sc635hai_default_reg_init(ot_vi_pipe vi_pipe,
                                const td_u32 *addrs,
                                const td_u32 *data,
                                td_u32 count)
{
    td_u32 i;

    for (i = 0; i < count; i++) {
        if (addrs[i] != 0)
            sc635hai_write_register(vi_pipe, addrs[i], data[i]);
    }
}

/* ── Mirror/flip ───────────────────────────────────────────────── */

void sc635hai_mirror_flip(ot_vi_pipe vi_pipe, td_u32 mode)
{
    /* mode: 0=normal, 1=mirror, 2=flip, 3=mirror+flip
     * (ot_isp_sns_mirrorflip_type ordering).
     *
     * Register 0x3221 encoding (confirmed from live sensor, see DRIVER.md):
     *   0x00 normal (BGGR), 0x06 mirror (GRBG),
     *   0x60 flip (GBRG),   0x66 mirror+flip (RGGB)
     *
     * NOTE: mirror/flip changes the effective Bayer order; the ISP
     * pub_attr bayer_format must be updated to match if this is ever
     * called with mode != 0. */
    static const td_u8 reg_val[4] = { 0x00, 0x06, 0x60, 0x66 };
    sc635hai_write_register(vi_pipe, SC635HAI_REG_MIRROR_FLIP,
                            reg_val[mode & 0x03]);
}
