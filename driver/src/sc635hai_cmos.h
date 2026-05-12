/*
 * SC635HAI Sensor Driver -- Constants and Gain Tables
 *
 * SmartSens SC635HAI 6MP (3200x1800) image sensor for Hi3516CV610.
 * Register values captured from live I2C dump of running camera.
 * Gain model adapted from SC500AI (same SmartSens register family).
 *
 * Reference: research/SC635HAI_SENSOR_ANALYSIS.md
 */

#ifndef __SC635HAI_CMOS_H__
#define __SC635HAI_CMOS_H__

#ifdef __cplusplus
extern "C" {
#endif

/* ── Sensor identification ─────────────────────────────────────── */
#define SC635HAI_ID              0x55    /* sensor type from Ghidra */
#define SC635HAI_CHIP_ID_H       0xCE   /* reg 0x3107 */
#define SC635HAI_CHIP_ID_L       0x7C   /* reg 0x3108 */
#define SC635HAI_I2C_ADDR        0x30   /* 7-bit I2C address */
#define SC635HAI_I2C_ADDR_WRITE  0x60   /* 8-bit write address */
#define SC635HAI_ADDR_BYTE       2      /* 16-bit register addresses */
#define SC635HAI_DATA_BYTE       1      /* 8-bit register data */

/* ── Resolution and timing ─────────────────────────────────────── */
#define SC635HAI_WIDTH           3200
#define SC635HAI_HEIGHT          1800
#define SC635HAI_HTS_DEF         1920   /* reg 0x320C/0x320D */
#define SC635HAI_VTS_DEF         2812   /* reg 0x320E/0x320F (superb's actual value) */
#define SC635HAI_VTS_MAX         0x1FFFF /* max VTS for slow shutter (17-bit, reg 0x326D[0]:0x320E:0x320F) */
#define SC635HAI_FPS_DEF         20.0f  /* actual fps at VTS=2812, 2-lane 1080Mbps */
#define SC635HAI_FPS_MIN         0.5f   /* slow shutter limit */

/* ── Exposure constants ────────────────────────────────────────── */
#define SC635HAI_EXP_MIN         2      /* half-lines (from Rockchip SC635HAI driver) */
#define SC635HAI_EXP_OFFSET      10     /* max = 2*VTS - offset */

/* ── Register addresses ────────────────────────────────────────── */
/* Stream control */
#define SC635HAI_REG_RESET       0x0103
#define SC635HAI_REG_STREAM      0x0100
#define SC635HAI_REG_STANDBY     0x3000

/* Chip ID */
#define SC635HAI_REG_CHIP_ID_H   0x3107
#define SC635HAI_REG_CHIP_ID_L   0x3108

/* Frame timing */
#define SC635HAI_REG_VTS_H       0x320E
#define SC635HAI_REG_VTS_L       0x320F
#define SC635HAI_REG_HTS_H       0x320C
#define SC635HAI_REG_HTS_L       0x320D

/* Mirror/flip */
#define SC635HAI_REG_MIRROR_FLIP 0x3221

/* Exposure (half-line precision, 16-bit split across 3 regs) */
#define SC635HAI_REG_EXP_H       0x3E00  /* bits [15:12] in [3:0] */
#define SC635HAI_REG_EXP_M       0x3E01  /* bits [11:4] */
#define SC635HAI_REG_EXP_L       0x3E02  /* bits [3:0] in [7:4] */

/* Analog gain */
#define SC635HAI_REG_AGAIN_COARSE 0x3E08  /* coarse gain selector */
#define SC635HAI_REG_AGAIN_FINE   0x3E09  /* fine gain 0x20-0x3F */

/* Digital gain */
#define SC635HAI_REG_DGAIN_COARSE 0x3E06  /* coarse: thermometer code */
#define SC635HAI_REG_DGAIN_FINE   0x3E07  /* fine: 0x80-0xFF */

/* Group hold (atomic register update) */
#define SC635HAI_REG_GROUP_HOLD   0x3812
#define SC635HAI_GROUP_HOLD_START 0x00   /* hold: buffer writes */
#define SC635HAI_GROUP_HOLD_END   0x30   /* release: apply all at once */

/* DPC noise fix */
#define SC635HAI_REG_DPC          0x5799

/* PLL bypass (for init sequence) */
#define SC635HAI_REG_PLL_BYPASS   0x36E9

/* WDR short exposure (phase 2, not used in linear mode) */
#define SC635HAI_REG_SEXP_H       0x3E22
#define SC635HAI_REG_SEXP_M       0x3E04
#define SC635HAI_REG_SEXP_L       0x3E05
#define SC635HAI_REG_SAGAIN_COARSE 0x3E0A
#define SC635HAI_REG_SAGAIN_FINE   0x3E0B

/* ── I2C register indices for ISP sync ─────────────────────────── */
/* These map to ot_isp_sns_regs_info.i2c_data[] entries */
enum sc635hai_linear_regs_e {
    LINEAR_HOLD_START = 0,  /* 0x3812 = 0x00 (group hold begin) */
    LINEAR_EXP_H,           /* 0x3E00 */
    LINEAR_EXP_M,           /* 0x3E01 */
    LINEAR_EXP_L,           /* 0x3E02 */
    LINEAR_AGAIN_COARSE,    /* 0x3E08 */
    LINEAR_AGAIN_FINE,      /* 0x3E09 */
    LINEAR_DGAIN_COARSE,    /* 0x3E06 */
    LINEAR_DGAIN_FINE,      /* 0x3E07 */
    LINEAR_VTS_H,           /* 0x320E */
    LINEAR_VTS_L,           /* 0x320F */
    LINEAR_HOLD_END,        /* 0x3812 = 0x30 (group hold release) */
    LINEAR_REGS_NUM
};

/* ── Analog gain model (from Rockchip SC635HAI driver) ──────────
 *
 * SC635HAI has 7 analog gain ranges with fine steps 0x20..0x3F
 * in register 0x3E09 (32 steps per range), giving gain = fine/32
 * times the coarse multiplier.
 *
 * The coarse register (0x3E08) values and multipliers are:
 *   0x00 =   1.00x    (fine 0x20..0x3F -> 1.00x .. 1.97x)
 *   0x01 =   2.00x    (fine 0x20..0x3F -> 2.00x .. 3.94x)  -- only to 2.66x
 *   0x80 =   2.66x    (fine 0x20..0x3F -> 2.66x .. 5.24x)
 *   0x81 =   5.32x    (fine 0x20..0x3F -> 5.32x .. 10.48x)
 *   0x83 =  10.64x    (fine 0x20..0x3F -> 10.64x .. 20.96x)
 *   0x87 =  21.28x    (fine 0x20..0x3F -> 21.28x .. 41.93x)
 *   0x8f =  42.56x    (fine 0x20..0x3F -> 42.56x .. 83.79x)
 *
 * Total analog gain range: 1.0x to 83.79x (vs SC500AI's 1.0x to 24x).
 * This explains the excellent low-light performance.
 *
 * Table entries: 32 fine steps per range x 7 ranges = 224 entries.
 * Base unit: 1024 = 1.0x gain.
 *
 * Reference: research/sc635hai_rockchip_v4l2.c sc635hai_set_gain_reg()
 */
#define SC635HAI_AGAIN_TBL_SIZE  224

struct sc635hai_again_info {
    unsigned int   gain_max;     /* max gain value (linear, 1024=1x) */
    unsigned short idx_base;     /* first index in the lookup table */
    unsigned char  reg_coarse;   /* 0x3E08 value for this range */
    unsigned char  reg_fine_base; /* 0x3E09 start (0x20) */
    unsigned char  reg_fine_step; /* fine step (always 1) */
};

/* 7 analog gain ranges (SC635HAI-specific) */
#define SC635HAI_AGAIN_RANGES  7
static const struct sc635hai_again_info g_again_info[SC635HAI_AGAIN_RANGES] = {
    { .gain_max = 2016,  .idx_base = 0,   .reg_coarse = 0x00,
      .reg_fine_base = 0x20, .reg_fine_step = 1 },   /* 1.00x - 1.97x */
    { .gain_max = 2720,  .idx_base = 32,  .reg_coarse = 0x01,
      .reg_fine_base = 0x20, .reg_fine_step = 1 },   /* 2.00x - 2.66x */
    { .gain_max = 5369,  .idx_base = 64,  .reg_coarse = 0x80,
      .reg_fine_base = 0x20, .reg_fine_step = 1 },   /* 2.66x - 5.24x */
    { .gain_max = 10738, .idx_base = 96,  .reg_coarse = 0x81,
      .reg_fine_base = 0x20, .reg_fine_step = 1 },   /* 5.32x - 10.48x */
    { .gain_max = 21475, .idx_base = 128, .reg_coarse = 0x83,
      .reg_fine_base = 0x20, .reg_fine_step = 1 },   /* 10.64x - 20.96x */
    { .gain_max = 42950, .idx_base = 160, .reg_coarse = 0x87,
      .reg_fine_base = 0x20, .reg_fine_step = 1 },   /* 21.28x - 41.93x */
    { .gain_max = 85801, .idx_base = 192, .reg_coarse = 0x8f,
      .reg_fine_base = 0x20, .reg_fine_step = 1 },   /* 42.56x - 83.79x */
};

/* Full 224-entry analog gain lookup table (computed from SC635HAI gain model)
 *
 * Each entry = coarse_multiplier * (fine_reg / 32) * 1024
 * where fine_reg goes from 0x20 (32) to 0x3F (63).
 */
static const unsigned int g_again_table[SC635HAI_AGAIN_TBL_SIZE] = {
    /* Range 0: 1.00x coarse (0x3E08=0x00), fine 0x20..0x3F, indices 0-31 */
    /* gain = 1.0 * fine/32 * 1024 = fine * 32 */
    1024, 1056, 1088, 1120, 1152, 1184, 1216, 1248,
    1280, 1312, 1344, 1376, 1408, 1440, 1472, 1504,
    1536, 1568, 1600, 1632, 1664, 1696, 1728, 1760,
    1792, 1824, 1856, 1888, 1920, 1952, 1984, 2016,
    /* Range 1: 2.00x coarse (0x3E08=0x01), fine 0x20..0x3F, indices 32-63 */
    /* gain = 2.0 * fine/32 * 1024 = fine * 64 */
    2048, 2112, 2176, 2240, 2304, 2368, 2432, 2496,
    2560, 2624, 2688, 2752, 2816, 2880, 2944, 3008,
    3072, 3136, 3200, 3264, 3328, 3392, 3456, 3520,
    3584, 3648, 3712, 3776, 3840, 3904, 3968, 4032,
    /* Range 2: 2.66x coarse (0x3E08=0x80), fine 0x20..0x3F, indices 64-95 */
    /* gain = 2.66 * fine/32 * 1024 ~= fine * 85.12, rounded */
    2723, 2808, 2893, 2978, 3063, 3148, 3233, 3318,
    3404, 3489, 3574, 3659, 3744, 3829, 3914, 3999,
    4085, 4170, 4255, 4340, 4425, 4510, 4595, 4680,
    4766, 4851, 4936, 5021, 5106, 5191, 5276, 5362,
    /* Range 3: 5.32x coarse (0x3E08=0x81), fine 0x20..0x3F, indices 96-127 */
    /* gain = 5.32 * fine/32 * 1024 ~= fine * 170.24 */
    5448, 5618, 5788, 5958, 6128, 6298, 6468, 6638,
    6808, 6978, 7148, 7318, 7488, 7658, 7828, 7998,
    8168, 8338, 8508, 8678, 8848, 9018, 9188, 9358,
    9528, 9698, 9868, 10038, 10208, 10378, 10548, 10718,
    /* Range 4: 10.64x coarse (0x3E08=0x83), fine 0x20..0x3F, indices 128-159 */
    /* gain = 10.64 * fine/32 * 1024 ~= fine * 340.48 */
    10894, 11234, 11574, 11914, 12254, 12594, 12934, 13274,
    13614, 13954, 14294, 14634, 14974, 15314, 15654, 15994,
    16334, 16674, 17014, 17354, 17694, 18034, 18374, 18714,
    19054, 19394, 19734, 20074, 20414, 20754, 21094, 21434,
    /* Range 5: 21.28x coarse (0x3E08=0x87), fine 0x20..0x3F, indices 160-191 */
    /* gain = 21.28 * fine/32 * 1024 ~= fine * 680.96 */
    21790, 22472, 23154, 23836, 24518, 25200, 25882, 26564,
    27246, 27928, 28610, 29292, 29974, 30656, 31338, 32020,
    32702, 33384, 34066, 34748, 35430, 36112, 36794, 37476,
    38158, 38840, 39522, 40204, 40886, 41568, 42250, 42932,
    /* Range 6: 42.56x coarse (0x3E08=0x8f), fine 0x20..0x3F, indices 192-223 */
    /* gain = 42.56 * fine/32 * 1024 ~= fine * 1361.92 */
    43582, 44944, 46306, 47668, 49030, 50392, 51754, 53116,
    54478, 55840, 57202, 58564, 59926, 61288, 62650, 64012,
    65374, 66736, 68098, 69460, 70822, 72184, 73546, 74908,
    76270, 77632, 78994, 80356, 81718, 83080, 84442, 85804,
};

/* ── Digital gain limits ───────────────────────────────────────── */
/* Digital gain: coarse (0x3E06) x fine (0x3E07).
 * Coarse: thermometer code 0x00=1x, 0x01=2x, 0x03=4x, 0x07=8x, 0x0F=16x
 * Fine: 0x80..0xFF = 128..255 = 1.0x..1.992x
 * Max: 16 * 1.992 * 1024 = 32640 => ~15.875x digital gain
 * Total max gain: 83.79x analog * 15.875x digital = 1330x (!!)
 */
#define SC635HAI_DGAIN_MIN    1024   /* 1.0x (1024 base) */
#define SC635HAI_DGAIN_MAX    32640  /* 16x * 255/128 * 1024 = ~15.875x */

/* ── Black level ───────────────────────────────────────────────── */
/* Typical 10-bit SmartSens black level. Confirm during testing. */
#define SC635HAI_BLACK_LEVEL  1024  /* 14-bit format: 10-bit BL of 64 << 4 = 1024; superb uses 1030 */

/* ── DPC thresholds (total gain, linear units, 1024=1x) ──────── */
#define SC635HAI_DPC_GAIN_HIGH  30720  /* ~30x: enable DPC */
#define SC635HAI_DPC_GAIN_LOW   20480  /* ~20x: disable DPC */

#ifdef __cplusplus
}
#endif

#endif /* __SC635HAI_CMOS_H__ */
