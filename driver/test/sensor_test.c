/*
 * SC635HAI Sensor Test -- Incremental hardware validation
 *
 * Usage: ./sensor_test [phase]
 *   phase 1: I2C chip ID read only (safest)
 *   phase 2: Full register init sequence (no ISP)
 *   phase 3: MIPI + ISP pipeline (future)
 *   phase 4: Full video pipeline (future)
 *
 * This is a standalone binary that talks directly to /dev/i2c-0.
 * It does NOT link against the sensor .so or SDK libraries.
 * Run it after stopping superb (see testing procedure in sensor analysis).
 *
 * Deploy to SD card: /progs/rec/00/sensor_test
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <linux/i2c-dev.h>

#include "sc635hai_cmos.h"

#ifndef I2C_SLAVE_FORCE
#define I2C_SLAVE_FORCE 0x0706
#endif

/* ── I2C helpers ───────────────────────────────────────────────── */

static int g_i2c_fd = -1;

static int i2c_open(void)
{
    g_i2c_fd = open("/dev/i2c-0", O_RDWR);
    if (g_i2c_fd < 0) {
        perror("open /dev/i2c-0");
        return -1;
    }
    if (ioctl(g_i2c_fd, I2C_SLAVE_FORCE, SC635HAI_I2C_ADDR) < 0) {
        perror("ioctl I2C_SLAVE_FORCE");
        close(g_i2c_fd);
        g_i2c_fd = -1;
        return -1;
    }
    printf("[OK] I2C bus 0 opened, slave addr 0x%02X\n", SC635HAI_I2C_ADDR);
    return 0;
}

static void i2c_close(void)
{
    if (g_i2c_fd >= 0) {
        close(g_i2c_fd);
        g_i2c_fd = -1;
    }
}

static int i2c_read_reg(unsigned short reg)
{
    unsigned char buf[2];
    buf[0] = (reg >> 8) & 0xFF;
    buf[1] = reg & 0xFF;

    if (write(g_i2c_fd, buf, 2) != 2)
        return -1;

    if (read(g_i2c_fd, buf, 1) != 1)
        return -1;

    return buf[0];
}

static int i2c_write_reg(unsigned short reg, unsigned char val)
{
    unsigned char buf[3];
    buf[0] = (reg >> 8) & 0xFF;
    buf[1] = reg & 0xFF;
    buf[2] = val;

    if (write(g_i2c_fd, buf, 3) != 3)
        return -1;

    return 0;
}

/* ── Phase 1: Chip ID read ─────────────────────────────────────── */

static int phase1_chip_id(void)
{
    int id_h, id_l;

    printf("\n=== Phase 1: Chip ID Read ===\n");

    if (i2c_open() < 0)
        return -1;

    id_h = i2c_read_reg(SC635HAI_REG_CHIP_ID_H);
    id_l = i2c_read_reg(SC635HAI_REG_CHIP_ID_L);

    if (id_h < 0 || id_l < 0) {
        printf("[FAIL] Cannot read chip ID registers\n");
        i2c_close();
        return -1;
    }

    printf("  Chip ID: 0x%02X%02X", id_h, id_l);
    if (id_h == SC635HAI_CHIP_ID_H && id_l == SC635HAI_CHIP_ID_L) {
        printf(" [OK] SC635HAI confirmed\n");
    } else {
        printf(" [WARN] Expected 0x%02X%02X\n",
               SC635HAI_CHIP_ID_H, SC635HAI_CHIP_ID_L);
    }

    /* Read some key registers to confirm sensor is responsive */
    printf("  VTS: 0x%02X%02X (expect 0x0AFC = %d)\n",
           i2c_read_reg(SC635HAI_REG_VTS_H),
           i2c_read_reg(SC635HAI_REG_VTS_L),
           SC635HAI_VTS_DEF);
    printf("  HTS: 0x%02X%02X (expect 0x0780 = %d)\n",
           i2c_read_reg(SC635HAI_REG_HTS_H),
           i2c_read_reg(SC635HAI_REG_HTS_L),
           SC635HAI_HTS_DEF);
    printf("  Mirror/Flip: 0x%02X\n",
           i2c_read_reg(SC635HAI_REG_MIRROR_FLIP));
    printf("  Stream: 0x%02X (0x01=streaming, 0x00=standby)\n",
           i2c_read_reg(SC635HAI_REG_STREAM));
    printf("  Again: 0x3E08=0x%02X, 0x3E09=0x%02X\n",
           i2c_read_reg(SC635HAI_REG_AGAIN_COARSE),
           i2c_read_reg(SC635HAI_REG_AGAIN_FINE));
    printf("  Dgain: 0x3E06=0x%02X, 0x3E07=0x%02X\n",
           i2c_read_reg(SC635HAI_REG_DGAIN_COARSE),
           i2c_read_reg(SC635HAI_REG_DGAIN_FINE));

    i2c_close();
    printf("[OK] Phase 1 complete\n");
    return 0;
}

/* ── Phase 2: Register init ────────────────────────────────────── */

/* Reference init table (subset -- just key registers to verify) */
static const unsigned short phase2_verify_regs[][2] = {
    /* { register, expected_value } */
    { 0x3208, 0x0C },  /* Width high: 0x0C80 = 3200 */
    { 0x3209, 0x80 },
    { 0x320A, 0x07 },  /* Height high: 0x0708 = 1800 */
    { 0x320B, 0x08 },
    { 0x320C, 0x07 },  /* HTS: 0x0780 = 1920 */
    { 0x320D, 0x80 },
    { 0x320E, 0x0A },  /* VTS: 0x0AFC = 2812 */
    { 0x320F, 0xFC },
};

static int phase2_register_init(void)
{
    int val;
    unsigned int i;
    int errors = 0;

    printf("\n=== Phase 2: Register State Verification ===\n");
    printf("Read-only: verifies current sensor register state.\n");
    printf("Run while superb is streaming (no writes performed).\n\n");

    if (i2c_open() < 0)
        return -1;

    /* Confirm chip ID first */
    val = i2c_read_reg(SC635HAI_REG_CHIP_ID_H);
    if (val != SC635HAI_CHIP_ID_H) {
        printf("[FAIL] Chip ID mismatch, aborting\n");
        i2c_close();
        return -1;
    }

    /* Verify key registers against expected running state */
    printf("Verifying registers:\n");
    for (i = 0; i < sizeof(phase2_verify_regs) / sizeof(phase2_verify_regs[0]); i++) {
        val = i2c_read_reg(phase2_verify_regs[i][0]);
        if (val == (int)phase2_verify_regs[i][1]) {
            printf("  0x%04X = 0x%02X [OK]\n",
                   phase2_verify_regs[i][0], val);
        } else {
            printf("  0x%04X = 0x%02X [FAIL] expected 0x%02X\n",
                   phase2_verify_regs[i][0], val,
                   phase2_verify_regs[i][1]);
            errors++;
        }
    }

    /* Also read some analog config registers from our init table */
    printf("\nAnalog/PLL registers (from init table):\n");
    static const unsigned short extra_regs[] = {
        0x3031, 0x3034, 0x36E9, 0x36EA, 0x36EB, 0x36EC, 0x36ED,
        0x3301, 0x3306, 0x3309, 0x330B, 0x3616, 0x3630, 0x3633,
    };
    for (i = 0; i < sizeof(extra_regs) / sizeof(extra_regs[0]); i++) {
        val = i2c_read_reg(extra_regs[i]);
        printf("  0x%04X = 0x%02X\n", extra_regs[i], val);
    }

    printf("\nStream status: 0x%02X\n",
           i2c_read_reg(SC635HAI_REG_STREAM));

    i2c_close();

    if (errors > 0) {
        printf("[WARN] %d register mismatches in frame geometry\n", errors);
    } else {
        printf("[OK] Phase 2 complete -- all frame geometry registers match\n");
    }
    return errors > 0 ? -1 : 0;
}

/* ── Main ──────────────────────────────────────────────────────── */

int main(int argc, char *argv[])
{
    int phase = 1;

    printf("SC635HAI Sensor Test v0.1\n");
    printf("Camera: Hi3516CV610 + SC635HAI (3200x1800 30fps 10bit)\n\n");

    if (argc > 1)
        phase = atoi(argv[1]);

    switch (phase) {
    case 1:
        return phase1_chip_id() < 0 ? 1 : 0;
    case 2:
        return phase2_register_init() < 0 ? 1 : 0;
    case 3:
        printf("Phase 3 (MIPI + ISP) not yet implemented.\n");
        printf("This requires linking against SDK libraries.\n");
        return 1;
    case 4:
        printf("Phase 4 (Full pipeline) not yet implemented.\n");
        return 1;
    default:
        printf("Usage: %s [phase]\n", argv[0]);
        printf("  1 = I2C chip ID read (safest, default)\n");
        printf("  2 = Register init + verify\n");
        printf("  3 = MIPI + ISP pipeline (TODO)\n");
        printf("  4 = Full video pipeline (TODO)\n");
        return 1;
    }
}
