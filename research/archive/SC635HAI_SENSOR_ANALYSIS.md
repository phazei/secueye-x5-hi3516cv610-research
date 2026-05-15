# SmartSens SC635HAI Sensor Driver Analysis

Complete analysis of the SC635HAI image sensor as used in the SECUEYE X5 camera.
This document captures everything needed to write a standalone sensor driver
for the Hi3516CV610 platform without depending on the stock `superb` firmware.

## Sensor Specifications

| Parameter | Value | Source |
|-----------|-------|--------|
| Manufacturer | SmartSens Technology (Shanghai) | Official flyer |
| Part Number | SC635HAI | Firmware strings |
| Resolution | 6MP (3208 x 1808 native) | Flyer + I2C register readback |
| Output Resolution | 3200 x 1800 (cropped) | I2C registers 0x3208-0x320B |
| Pixel Size | 2.0 um x 2.0 um | Flyer |
| Optical Format | 1/2.45" | Flyer |
| Technology | SmartClarity-3 (BSI) | Flyer |
| Shutter | Rolling | Flyer |
| Output Interface | 10-bit 1/2/4 Lane MIPI, 10-bit 1/2/4 Lane LVDS | Flyer |
| Output Format | RAW RGB (Bayer) | Flyer |
| Bit Depth | 10-bit | Flyer + function names (`_10bit_init`) |
| Bayer Pattern | BGGR (assumed, standard SmartSens) | Convention |
| Max Frame Rate | 30/60 fps (at DVDD 1.2V) | Flyer |
| Configured Frame Rate | 30fps | Function names (`_6m30_`) |
| Camera MIPI Config | 2 Lane, 1080 Mbps, 27MHz input | superb log string |
| HDR Modes | 2-exposure Staggered HDR + InSensor HDR | Flyer |
| Configured HDR | VC WDR 2-to-1 | Function names (`vc_wdr_2t1`) |
| Sensitivity | 4133 mV/lux·s | Flyer |
| Dynamic Range | Normal: 83 dB, HDR: >100 dB | Flyer |
| SNR | 39 dB | Flyer |
| CRA | 12 degrees | Flyer |
| Operating Temp | -30C to +85C | Flyer |
| Best IQ Temp | -20C to +60C | Flyer |
| Power Supply | Analog 2.8V, Digital 1.2V, I/O 1.8V | Flyer |
| Package | 41-pin CSP, 7.044 x 4.704 mm | Flyer |
| Chip ID | 0xCE7C | I2C registers 0x3107-0x3108 |
| I2C Address | 0x30 (7-bit) / 0x60 (8-bit) | I2C bus scan |
| I2C Bus | Bus 0 (`/dev/i2c-0`) | I2C scan |
| Register Width | 16-bit addresses, 8-bit data | Standard SmartSens |

### Datasheet

Only a **2-page product flyer** is publicly available (no register map):
```
https://smartsens.oss-cn-beijing.aliyuncs.com/web/img/1758190067924265729.pdf
```

The full datasheet with register map is under NDA from SmartSens. However,
the SmartSens register architecture is consistent across their product line
(SC200AI, SC500AI, SC431HAI, etc.), so register addresses and bit fields can
be inferred from other sensors in the family. The live I2C register dump below
provides the actual configured values, which is more useful than a register
map for building a "snapshot" driver.

### Key Notes from Flyer

- **60fps capable** at DVDD 1.2V -- our camera runs at 30fps (likely sensor at
  30fps, or 60fps with 2x WDR merge)
- **Best IQ at -20C to +60C** -- the camera body runs warm; if internal temp
  exceeds 60C, image quality degrades (may explain grainy dark images)
- **InSensor HDR** -- a single-frame HDR mode separate from the 2-exposure
  staggered HDR. Our camera uses 2-exposure staggered (`vc_wdr_2t1`)
- **SmartAOV (Always-On Video)** -- low-power always-on mode. The app had an
  "AOV mode" option -- this is the sensor feature it controls
- **MIPI configuration** -- supports 1/2/4 lanes. Our camera uses 2 lanes
  at 1080 Mbps with 27 MHz MCLK input (from superb boot log:
  `SC635HAI_raw_MIPI_27Minput_2Lane_10bit_1080Mbps_3200x1800_30fps`)

## I2C Communication

Confirmed via live I2C scan on running camera:

```
Bus 0: Device at 0x30 (SC635HAI primary sensor)
Bus 1: Empty
Bus 2: Empty (partially scanned)
```

Tool usage:
```bash
# Read single register (8-bit write addr = 0x60, 16-bit reg addr, 8-bit data):
i2c_read 0 0x60 <reg_start> <reg_end> 2 1

# HiSilicon i2c_read args: bus dev_addr reg_start reg_end addr_width data_width
```

## Register Map (Live Capture)

The following registers were read from the running camera while `superb` was
actively streaming. This represents the complete **linear 3200x1800 30fps 10-bit**
mode configuration.

### Key Register Definitions

#### Chip ID (Read-Only)
| Register | Value | Description |
|----------|-------|-------------|
| 0x3107 | 0xCE | Chip ID high byte |
| 0x3108 | 0x7C | Chip ID low byte |

**SC635HAI Chip ID = 0xCE7C**

#### Frame Geometry
| Register | Value | Description |
|----------|-------|-------------|
| 0x3200-0x3201 | 0x0000 | Row start = 0 |
| 0x3202-0x3203 | 0x0000 | Column start = 0 |
| 0x3204-0x3205 | 0x0C87 | Row end = 3207 (3208 rows total) |
| 0x3206-0x3207 | 0x070F | Column end = 1807 (1808 cols total) |
| 0x3208-0x3209 | 0x0C80 | Output width = 3200 |
| 0x320A-0x320B | 0x0708 | Output height = 1800 |
| 0x320C-0x320D | 0x0780 | HTS (H total size) = 1920 |
| 0x320E-0x320F | 0x0AFC | VTS (V total size) = 2812 |

**Frame rate calculation:** Pixel clock / (HTS * VTS) = fps
VTS = 2812 lines, HTS = 1920 pixels. If pixel clock = ~162 MHz: 162M / (1920 * 2812) = ~30 fps.

#### Mirror / Flip
| Register | Value | Description |
|----------|-------|-------------|
| 0x3221 | 0x00 | Mirror/flip control (0=normal, bit0=mirror, bit1=flip) |

Standard SmartSens mirror/flip register. To flip:
- 0x00 = Normal
- 0x01 = Mirror (horizontal)
- 0x02 = Flip (vertical)
- 0x03 = Mirror + Flip (180 degrees)

#### Exposure and Gain (AE Registers)
| Register | Value | Description |
|----------|-------|-------------|
| 0x3E01 | 0xAF | Exposure time [15:8] |
| 0x3E02 | 0x40 | Exposure time [7:4] (upper nibble) |
| 0x3E03 | 0x0B | AE control (mode bits) |
| 0x3E06 | 0x01 | Digital gain [?] |
| 0x3E07 | 0x92 | Digital gain [?] |
| 0x3E08 | 0x8F | Analog gain coarse + fine high |
| 0x3E09 | 0x3F | Analog gain fine low |
| 0x3E0A | 0x8F | Short exposure analog gain (WDR) |
| 0x3E0B | 0x3F | Short exposure analog gain fine (WDR) |

### Full Register Dump (Non-Zero Values)

All registers with non-zero values from the live capture, organized by bank:

#### Bank 0x30xx (System, PLL, Clock)
```
0x3004=0x60  0x3007=0x62  0x300a=0x20  0x3011=0x64
0x3012=0x66  0x3018=0x3b  0x3019=0x0c  0x301a=0xf0
0x301c=0xf0  0x301e=0xf0  0x301f=0x13  0x3020=0x02
0x3021=0x67  0x3022=0x01  0x3023=0x04  0x3025=0x31
0x3026=0x0d  0x3029=0x0d  0x302b=0x60  0x302d=0x20
0x3031=0x0a  0x3032=0x30  0x3033=0x20  0x3034=0x06
0x3037=0x60  0x303f=0x01  0x3040=0x01  0x3052=0x01
0x3058=0x10  0x3059=0x32  0x3085=0x30  0x3086=0x99
0x309b=0xf0  0x309d=0xf0  0x309e=0x30  0x30a1=0x01
0x30b0=0x01  0x30b1=0x01  0x30b8=0x44
```

#### Bank 0x31xx (Sensor Core, Timing)
```
0x3101=0x12  0x3103=0x04  0x3104=0x01  0x3105=0x12
0x3106=0x01  0x3107=0xce  0x3108=0x7c  0x3109=0x01
```

#### Bank 0x32xx (Frame, Window, Crop)
```
0x3204=0x0c  0x3205=0x87  0x3206=0x07  0x3207=0x0f
0x3208=0x0c  0x3209=0x80  0x320a=0x07  0x320b=0x08
0x320c=0x07  0x320d=0x80  0x320e=0x0a  0x320f=0xfc
0x3211=0x03  0x3213=0x03  0x3214=0x11  0x3215=0x11
0x3219=0x02  0x321a=0x11  0x321f=0x0b  0x3223=0xc0
0x3224=0xc2  0x3225=0x20  0x3227=0x03  0x3228=0x01
0x3233=0x04  0x3236=0x04  0x3238=0x04  0x323b=0x02
0x3243=0x03  0x3248=0x04  0x3249=0x0f  0x3251=0x98
0x3253=0x0c  0x3256=0x08  0x3258=0x14  0x3259=0x02
0x325b=0x64  0x325d=0x02  0x325f=0x18  0x3271=0x10
0x3273=0x13  0x3274=0x09  0x3277=0x02  0x3279=0x17
0x327c=0x04  0x327e=0xff  0x327f=0x3f  0x3295=0x04
0x3296=0x04  0x3297=0x01  0x329b=0x08  0x329c=0x04
0x329f=0x01  0x32b2=0x03  0x32c0=0x0f  0x32c5=0x14
0x32c6=0x38  0x32c8=0x40  0x32c9=0x20  0x32cd=0x84
0x32d1=0x14  0x32d2=0x02  0x32d3=0x03  0x32d5=0xf0
0x32d9=0x19  0x32da=0x19  0x32db=0x10  0x32df=0x10
0x32e2=0x09  0x32e7=0x10  0x32ed=0x01  0x32ee=0x01
0x32f7=0x10
```

#### Bank 0x33xx (Analog, Column)
```
0x3301=0x12  0x3302=0x10  0x3303=0x10  0x3304=0x50
0x3306=0x70  0x3307=0x08  0x3308=0x18  0x3309=0xb0
0x330a=0x01  0x330b=0x20  0x330c=0x10  0x330d=0x20
0x330e=0x30  0x330f=0x08  0x3310=0x08  0x3312=0x80
0x3313=0x88  0x3314=0x14  0x3316=0x10  0x3317=0x04
0x3318=0x02  0x3319=0x04  0x331a=0x04  0x331b=0x01
0x331c=0x04  0x331e=0x39  0x331f=0x99  0x3320=0x06
0x3323=0x02  0x3324=0x01  0x3325=0x01  0x3326=0x0e
0x3328=0x08  0x3329=0x10  0x332a=0x04  0x332b=0x05
0x332d=0x01  0x332f=0x06  0x3332=0x34  0x3333=0x10
0x3334=0x40  0x3338=0x10  0x3339=0x05  0x333a=0x02
0x333b=0x01  0x333d=0x01  0x333e=0x06  0x333f=0x02
0x3340=0x04  0x3341=0x03  0x3346=0x0f  0x3348=0x90
0x3349=0x04  0x334a=0x02  0x334b=0x0c  0x334c=0x10
0x334d=0x08  0x334f=0x01  0x3351=0x08  0x3352=0x04
0x3353=0x04  0x3354=0x04  0x3356=0x12  0x3358=0x34
0x3359=0x08  0x335d=0x40  0x335e=0x06  0x335f=0x0a
0x3361=0x05  0x3362=0x72  0x3363=0x01  0x3364=0x5e
0x3366=0x0e  0x3367=0x04  0x3368=0x02  0x3369=0x30
0x336b=0x80  0x336c=0xce  0x336d=0x03  0x336e=0xe0
0x336f=0x48  0x3370=0x69  0x3372=0x0c  0x3376=0x31
0x337a=0x06  0x337b=0x0a  0x337c=0x02  0x337d=0x0e
0x337e=0x80  0x337f=0x33  0x3381=0x01  0x3385=0x03
0x338e=0xff  0x338f=0xa0  0x3393=0x18  0x3394=0x2c
0x3395=0x3c  0x3396=0x0f  0x3399=0x12  0x339a=0x16
0x339b=0x1e  0x339c=0x3e  0x339e=0x34  0x339f=0x06
0x33a2=0x04  0x33a3=0x08  0x33a7=0x05  0x33ac=0x0c
0x33ad=0x2c  0x33ae=0x30  0x33af=0x90  0x33b0=0x0f
0x33b2=0x24  0x33b3=0x10  0x33b4=0xff  0x33b5=0x10
0x33b6=0x06  0x33b7=0x05  0x33b9=0x08  0x33ba=0x04
0x33bb=0x03  0x33bc=0x01  0x33f0=0x30  0x33f1=0x30
0x33f2=0x3e  0x33f5=0x70  0x33f9=0x70  0x33fb=0x70
0x33ff=0x0a
```

#### Bank 0x36xx (MIPI, PLL, Gain Table)
```
0x3616=0xbc  0x3630=0x46  0x3631=0xf0  0x3632=0x6d
0x3633=0x4d  0x363a=0x80  0x363b=0x57  0x363c=0xd8
0x363d=0x40  0x3641=0x40  0x3650=0x41  0x3651=0x9d
0x3654=0x30  0x3656=0x53  0x3670=0x41  0x3671=0x31
0x3672=0x31  0x3673=0x04  0x3674=0x08  0x3675=0x04
0x3676=0x18  0x367e=0x69  0x367f=0x6d  0x3680=0x8d
0x3681=0x04  0x3682=0x08  0x3683=0x04  0x3684=0x78
0x3685=0x80  0x3686=0x80  0x3687=0x83  0x3688=0x82
0x3689=0x85  0x368a=0x8b  0x368b=0x97  0x368c=0xbf
0x368e=0x08  0x3690=0x18  0x3691=0x04  0x3693=0x04
0x3694=0x08  0x3695=0x04  0x3696=0x18  0x3697=0x04
0x3698=0x38  0x3699=0x04  0x369a=0x78  0x36c0=0x31
0x36c2=0x8d  0x36ca=0xbf  0x36d0=0x0d  0x36e9=0x24
0x36ea=0x14  0x36eb=0x45  0x36ec=0x4b  0x36ed=0x18
```

#### Bank 0x3Exx (Exposure, Gain, AE)
```
0x3e01=0xaf  0x3e02=0x40  0x3e03=0x0b  0x3e05=0x40
0x3e06=0x01  0x3e07=0x92  0x3e08=0x8f  0x3e09=0x3f
0x3e0a=0x8f  0x3e0b=0x3f  0x3e0e=0x02  0x3e11=0x80
0x3e13=0x20  0x3e14=0xb1  0x3e16=0x01  0x3e17=0x54
0x3e18=0x01  0x3e19=0x54  0x3e1a=0x80  0x3e1b=0x29
0x3e1c=0x0f  0x3e1d=0x0f  0x3e1e=0xf4  0x3e1f=0x01
0x3e24=0x20  0x3e26=0x20  0x3e27=0xc0  0x3e28=0xc0
0x3e29=0x10  0x3e2b=0x20  0x3e2c=0x03  0x3e2d=0x24
0x3e2e=0x01  0x3e36=0x03  0x3e37=0x40  0x3e3a=0x03
0x3e3b=0x40  0x3e3c=0x0b  0x3e3d=0xe0  0x3e3f=0x20
0x3e43=0x10  0x3e47=0x10  0x3e67=0x80  0x3e69=0x80
0x3e6b=0x80  0x3e81=0x80  0x3e83=0x20  0x3e89=0x80
0x3e8b=0x20  0x3e8c=0x3f  0x3e8d=0x3f  0x3e8e=0x0f
0x3e8f=0x3f  0x3e91=0x20  0x3e93=0x20  0x3e99=0x20
0x3e9b=0x20  0x3e9c=0x3f  0x3e9d=0xff  0x3e9e=0xff
0x3ea2=0x10  0x3ea8=0x10  0x3eab=0x20  0x3eaf=0x20
0x3eb7=0x80  0x3eb9=0x80  0x3ebb=0x80  0x3edb=0x80
0x3edd=0x80  0x3edf=0x80  0x3ee7=0x80  0x3ee9=0x80
0x3eeb=0x80  0x3ef8=0x02  0x3ef9=0x10
```

## Sensor Object Structure (from SDK Headers)

The HiSilicon ISP SDK defines the sensor object as `ot_isp_sns_obj`:

```c
typedef struct {
    /* +0x00 */ td_s32 (*pfn_register_callback)(ot_vi_pipe, ot_isp_3a_alg_lib*, ot_isp_3a_alg_lib*);
    /* +0x04 */ td_s32 (*pfn_un_register_callback)(ot_vi_pipe, ot_isp_3a_alg_lib*, ot_isp_3a_alg_lib*);
    /* +0x08 */ td_s32 (*pfn_set_bus_info)(ot_vi_pipe, ot_isp_sns_commbus);
    /* +0x0C */ td_s32 (*pfn_set_bus_ex_info)(ot_vi_pipe, ot_isp_sns_bus_ex*);
    /* +0x10 */ td_void (*pfn_standby)(ot_vi_pipe);
    /* +0x14 */ td_void (*pfn_restart)(ot_vi_pipe);
    /* +0x18 */ td_void (*pfn_mirror_flip)(ot_vi_pipe, ot_isp_sns_mirrorflip_type);
    /* +0x1C */ td_void (*pfn_set_blc_clamp)(ot_vi_pipe, ot_isp_sns_blc_clamp);
    /* +0x20 */ td_s32 (*pfn_write_reg)(ot_vi_pipe, td_u32 addr, td_u32 data);
    /* +0x24 */ td_s32 (*pfn_read_reg)(ot_vi_pipe, td_u32 addr);
    /* +0x28 */ td_s32 (*pfn_set_init)(ot_vi_pipe, ot_isp_init_attr*);
} ot_isp_sns_obj;
```

**Confirmed by Ghidra**: The mirror/flip call at offset +0x18 matches exactly.

## Functions in superb Binary (Ghidra)

### Primary Sensor Functions
| Address | Size | Name | Purpose |
|---------|------|------|---------|
| 0x00432e9c | 18 | `sc635hai_get_obj` | Returns &g_sns_sc635hai_obj |
| 0x00432eb8 | 168 | (unnamed) | Object setup / init |
| 0x00432f80 | 102 | `sc635hai_linear_6m30_10bit_init` | Linear mode register table |
| 0x00433010 | 102 | `sc635hai_vc_wdr_2t1_6m30_10bit_init` | WDR mode register table |
| 0x004330a4 | 30 | `sc635hai_get_standby_cfg` | Standby configuration |

### Slave Sensor Functions
| Address | Size | Name | Purpose |
|---------|------|------|---------|
| 0x004395c8 | 258 | `sc635hai_slave_set_slave_registers` | I2C config for slave mode |
| 0x00439708 | 196 | (unnamed) | Slave object setup |
| 0x004397f8 | 18 | `sc635hai_slave_get_obj` | Returns &g_sns_sc635hai_slave_obj |
| 0x00439814 | 44 | `sc635hai_slave_set_i2c_addr` | Change I2C address |
| 0x0043984c | 24 | `sc635hai_slave_set_single_mipi_mode` | Single MIPI lane mode |
| 0x00439870 | 10 | `sc635hai_slave_get_single_mipi_mode` | Read MIPI mode |
| 0x00439880 | 210 | (unnamed) | Slave init helper |
| 0x0043997c | 240 | `sc635hai_slave_linear_6m30_10bit_init` | Slave linear init |
| 0x00439abc | 136 | `sc635hai_slave_vc_wdr_2t1_6m30_10bit_init` | Slave WDR init |

### Global Objects (RAM)
| Address | Name |
|---------|------|
| 0x00782a40 | `g_sns_sc635hai_obj` |
| 0x00772600 | `g_sns_sc635hai_slave_obj` |

### Sensor Type ID
- Primary: **0x55**
- Slave: **0x56**

## ISP Calibration Data (PQ Bins)

Located at `/home/sensor/sc635hai/pqbin/` (resfs partition, bind-mounted):

| File | Size | Scene |
|------|------|-------|
| day.bin | 144,774 bytes | Daylight ISP calibration |
| night.bin | 144,774 bytes | Night (IR) ISP calibration |
| light.bin | 144,774 bytes | White-light (color night) calibration |
| black.bin | 144,774 bytes | Very dark scene calibration |

These are loaded by the ISP framework automatically and do NOT need to be
embedded in the sensor driver.

## Comparison with SC500AI

| Aspect | SC500AI | SC635HAI | Compatible? |
|--------|---------|----------|-------------|
| I2C address | 0x30 (7-bit) | 0x30 (7-bit) | Yes |
| Register arch | SmartSens standard | SmartSens standard | Yes |
| SDK object | `ot_isp_sns_obj` (11 ptrs) | Same structure | Yes |
| Mirror/flip reg | 0x3221 | 0x3221 | Yes |
| Exposure regs | 0x3E00-0x3E02 | 0x3E00-0x3E02 | Yes |
| Gain regs | 0x3E06-0x3E09 | 0x3E06-0x3E09 | Yes |
| Resolution | 2880x1620 | 3200x1800 | Different |
| Init registers | ~108 writes | Unknown count | Different values |
| PLL config | Different | Different | Different |
| VTS/HTS | Different | 1920/2812 | Different |
| Chip ID | Different | 0xCE7C | Different |

**The register architecture is the same. The specific register VALUES differ.**
An SC500AI driver can serve as a structural template, but the init sequences,
timing, and gain parameters must be replaced with SC635HAI-specific values.

---

## SmartSens Gain Model (confirmed from SC500AI source)

The gain model is **fully documented** from the SC500AI source code (Sophgo
SensorSupportList repo, since removed). All extracted data is below and in
`driver/src/sc635hai_cmos.h`. Consistent across SC200AI/SC500AI/SC635HAI family.

### Analog Gain Registers (0x3E08 / 0x3E09)

**Register 0x3E08** = coarse gain selector:

| Value | Coarse Multiplier | Notes |
|-------|-------------------|-------|
| 0x03 | 1x | Fine gain range 0x40-0x7F |
| 0x23 | 2x | Fine gain range 0x40-0x7F |
| 0x27 | 4x | Fine gain range 0x40-0x7F |
| 0x2F | 8x | Fine gain range 0x40-0x7F |
| 0x3F | 16x | Fine gain range 0x40-0x7F |

**Register 0x3E09** = fine gain (0x40 to 0x7F = 64 steps per coarse range).

Total analog gain = coarse * (fine / 64), giving 5 ranges * 64 steps = 289
entries in the lookup table, from 1.0x to ~24x.

**Our live dump**: 0x3E08=0x8F, 0x3E09=0x24. The 0x8F value doesn't match the
SC500AI table directly (bit 7 is set), suggesting the SC635HAI may have a
different coarse encoding or an additional mode bit. This needs investigation
when building the driver -- possibly bit[7] is a WDR/DCG mode flag.

### Again Lookup Table (SC500AI reference, 289 entries, base 1024 = 1.0x)

The SC500AI driver uses a 289-entry table organized as 5 coarse gain ranges
via `AgainInfo[5]`:

| Range | Index | 0x3E08 | 0x3E09 range | Gain range (linear, 1024=1x) |
|-------|-------|--------|-------------|-------------------------------|
| 1x | 0-32 | 0x03 | 0x40-0x60 | 1024-1536 |
| 2x | 33-96 | 0x23 | 0x40-0x7F | ~1536-3080 |
| 4x | 97-160 | 0x27 | 0x40-0x7F | ~3080-6161 |
| 8x | 161-224 | 0x2F | 0x40-0x7F | ~6161-12321 |
| 16x | 225-288 | 0x3F | 0x40-0x7F | ~12321-24644 |

The `cmos_again_calc_table()` function does a simple linear search: given a
desired gain value, find the largest table entry not exceeding it, return the
index. The index is then decomposed back into coarse + fine register values
in `cmos_gains_update()`:

```c
// Find which AgainInfo range the index falls into
for (i = tbl_num - 1; i >= 0; i--) {
    info = &AgainInfo[i];
    if (u32Again >= info->idxBase) break;
}
// 0x3E08 = info->regGain (coarse selector)
// 0x3E09 = info->regGainFineBase + (u32Again - info->idxBase) * step
//        = 0x40 + (index_within_range)
```

Full table source: SC500AI sc500ai_cmos.c (from Sophgo SensorSupportList, removed)

### Digital Gain Registers (0x3E06 / 0x3E07)

**Register 0x3E06** = coarse digital gain:

| Value | Multiplier | Bit pattern |
|-------|-----------|-------------|
| 0x00 | 1x | 0b0000 |
| 0x01 | 2x | 0b0001 |
| 0x03 | 4x | 0b0011 |
| 0x07 | 8x | 0b0111 |
| 0x0F | 16x | 0b1111 |

Each bit enables a 2x doubling; lower bits must be set (thermometer code).

**Register 0x3E07** = fine digital gain (0x80 to 0xFF):
- Formula: `fine_gain = reg_value / 128` (0x80=1.0x, 0xFF=1.992x)
- Total dgain = coarse * (fine / 128)
- Max digital gain: 16 * (255/128) = 31.875x (linear value 32640, base 1024)

Calculation in `cmos_gains_update()`:
```c
// Find power-of-2 coarse base
if (u32Dgain < 2048)       { dgainBase = 1024;  dgainReg = 0x0; }  // 1x
else if (u32Dgain < 4096)  { dgainBase = 2048;  dgainReg = 0x1; }  // 2x
else if (u32Dgain < 8192)  { dgainBase = 4096;  dgainReg = 0x3; }  // 4x
else if (u32Dgain < 16384) { dgainBase = 8192;  dgainReg = 0x7; }  // 8x
else                        { dgainBase = 16384; dgainReg = 0xf; }  // 16x
// Fine = linear_gain * 128 / coarse_base  (range 128..255)
u32Dgain = u32Dgain * 128 / dgainBase;
// 0x3E06 = dgainReg, 0x3E07 = u32Dgain
```

**Our live dump**: 0x3E06=0x00, 0x3E07=0x80 = dgain 1.0x * (128/128) = 1.0x.
Confirms camera running at minimum digital gain (expected in normal lighting).

### Exposure Registers (0x3E00 / 0x3E01 / 0x3E02)

> **Status**: VERIFIED CORRECT (2026-05-14, kernel I2C sync path working).

Three registers encode a 16-bit exposure value. The register holds an
integer count in **half-line units**, but the AE algorithm and ISP
scheduling treat AE `int_time` as **whole lines**, with `max_int_time = VTS - 10`.

```
0x3E00 = bits [15:12] of inttime  (high nibble only, bits 3:0)
0x3E01 = bits [11:4]  of inttime  (full byte)
0x3E02 = bits [3:0]   of inttime, shifted left by 4  (high nibble only)
```

Encoding:
```c
reg_3E00 = (inttime >> 12) & 0x0F;
reg_3E01 = (inttime >> 4)  & 0xFF;
reg_3E02 = (inttime & 0x0F) << 4;
```

Constraints (verified against stock superb behavior):
- Minimum exposure: 2 lines (SC635HAI_EXP_MIN)
- Maximum exposure: `VTS - 10` (= 2802 at VTS=2812)
- At VTS=2812, 20fps: max = 2802

**Live readback during AE-converged operation**: exposure register
decodes to whatever `/proc/umap/isp` reports as `line:`. They MUST
match if the kernel I2C sync path is working.

#### Superseded hypotheses (kept for context, do not use)

- ~~"Max exposure = 2*VTS - 10 = 5614 half-lines"~~ -- inferred from
  SC500AI datasheet, but stock superb empirically caps at `VTS - 10`.
  Setting `max_int_time = 2*VTS - 10` halved our ISP interrupt rate
  (`int_rat` 10 vs superb's 20). Tracking: the AE algorithm computes
  exposure in line units, and `2*VTS - 10` made it schedule double
  the exposure steps it should, presumably overrunning frame time.

### Activating The Kernel I2C Sync Path (Required For Frame-Synchronized Sensor Writes)

> **Status**: VERIFIED 2026-05-14. With all of the following in place,
> `cmos_inttime_update` and `cmos_gains_update` need NOT issue any direct
> I2C writes -- the kernel's registered `ot_sensor_i2c_write` callback
> handles all sensor exposure/gain delivery, frame-synchronized at FE_END.

**Required fixes (all six)**:

1. `cmos_get_ae_default`: populate `hmax_times = 1e9 / (VTS * fps)`.
   The AE algorithm uses this to convert exposure microseconds to
   sensor lines. Missing => AE cannot schedule frame-accurate exposure.

2. `cmos_get_ae_default`, `cmos_fps_set`, `cmos_slow_framerate_set`:
   use `max_int_time = VTS - SC635HAI_EXP_OFFSET` (single-line max).
   Also fix the clamp in `cmos_inttime_update`.

3. Add ALL AE callbacks in `cmos_init_ae_exp_function`:
   - `pfn_cmos_ae_quick_start_status_set` -- resets `sync_init=FALSE`
     when AE algo signals quick-start toggle.
   - `pfn_cmos_ae_fast_ae_attr_get` -- returns `sns_delay_frame = 3`.
   - `pfn_cmos_ae_fast_ae_attr_set` -- stub OK.

4. In pipeline_test (or equivalent application), call
   `ss_mpi_isp_set_ctrl_param` AFTER `register_callback`/`set_bus_info`/
   `ae_register`/`awb_register` but BEFORE `ss_mpi_isp_mem_init`:
   - `be_buf_num = 4`
   - `quick_start_en = 1`
   - (Do NOT set `isp_run_wakeup_select = BE_END` -- isp_init rejects
     it with `0xa01c800c` in our online running_mode.)

5. After `ss_mpi_isp_init`, call `ss_mpi_isp_set_ae_route_attr` with
   3 nodes: `{int_time=8, sys_gain=1024}`, `{2804, 1024}`,
   `{2804, 196608}`. Iris fields set to 1.

6. **CRITICAL** -- in `cmos_get_sns_reg_info`'s incremental-update
   branch (the path taken on every frame after the first), force
   `i2c_data[i].update = TD_TRUE` for:
   - EXP_H, EXP_M, EXP_L (3E00/01/02)
   - AGAIN_COARSE, AGAIN_FINE (3E08/09)
   - DGAIN_COARSE, DGAIN_FINE (3E06/07)
   - HOLD_START, HOLD_END (group-hold bookends at 3812)

   Why: the kernel iterates `regs_info[].reg_num` registers and only
   sends I2C transactions for ones with `update == TD_TRUE`. The
   sc4336p reference uses diff-based updates (`update = (data[N] != data[N-1])`),
   but once AE converges the values stop changing and ALL flags go
   FALSE, so the kernel writes nothing. Forcing TRUE on AE-driven
   regs makes the kernel write them every frame -- which is what AE
   actually wants since sensor regs can drift (e.g., from external
   pokes, brownouts, or initial register load mismatch).

   VTS regs can remain diff-based since AE rarely changes them and
   we don't want unnecessary I2C overhead.

#### How to verify the kernel I2C sync path is working

1. Comment out all `sc635hai_write_register` calls in
   `cmos_inttime_update` AND `cmos_gains_update`.
2. Rebuild driver, deploy, restart pipeline_test, wait ~10s for AE.
3. Read sensor regs: `i2c_read 0 0x60 0x3E00 0x3E02 2 1`. Decode the
   16-bit value: it MUST match `/proc/umap/isp` `line:` field.
4. Poke a marker: `i2c_write 0 0x60 0x3E01 0xFF 2 1`. Wait 3s. Read
   back. Sensor regs MUST have been restored to the AE target.

If steps 3 or 4 fail, the kernel I2C path isn't engaged. Check each
of the 6 requirements above.

#### Open Question (worth investigating in a new session)

The diff-based update logic (sc4336p style) doesn't work for us. The
sc4336p reference (in shumjj/.../smart_sc4336p/sc4336p_cmos.c:850)
uses pure diff-based updates and is presumably working in shumjj's
image. Either:

- shumjj's image also has this problem and just doesn't notice
  because real scenes always have minor exposure jitter that keeps
  diffs non-zero
- shumjj's `ot_isp.ko` differs from ours in a subtle way that
  re-writes all regs regardless of update flag (our byte-equivalence
  analysis was against camera firmware's stock, not shumjj's)
- There's a different code path in V1.0.2.0 vs V1.0.2.1 userspace
  ISP libs that influences this

For now we force-update on AE-driven regs and the path works. The
edge case to worry about: **manual exposure mode** where AE explicitly
locks at one value -- in our solution this is fine (kernel writes
every frame); in a pure-diff solution this would silently break.

### DPC Noise Fix at High Gain

SC500AI writes register 0x5799 based on gain level (hysteresis):
- Total gain >= ~30x: write 0x5799 = 0x07 (enable DPC correction)
- Total gain <= ~20x: write 0x5799 = 0x00 (disable DPC correction)

Our SC635HAI dump shows 0x5799=0x00, confirming DPC is disabled at the
current low gain setting. This behavior should be replicated in our driver.

### WDR Mode Exposure (deferred to phase 2)

For WDR (2-exposure staggered / `vc_wdr_2t1`):
- Long exposure: 0x3E00/0x3E01/0x3E02 (same as linear)
- Short exposure: 0x3E22/0x3E04/0x3E05
- Short frame gain: 0x3E0A/0x3E0B
- Short min=5, step=4, max=2*reg_sexp_max-14
- Long min=5, step=4, max=2*(vts-max_sexp)-18

---

## HiSilicon Sensor Driver Architecture

Our driver was scaffolded from two reference repos (both since removed):
- **SC500AI** (Sophgo SensorSupportList) -- gain tables, exposure encoding, AE logic
- **IMX307** (YJSNPI-Hi, Hi3516EV200 SDK) -- HiSilicon 2-file driver architecture

All extracted knowledge is implemented in `driver/src/sc635hai_cmos.c` and
`driver/src/sc635hai_sensor_ctl.c`. The shumjj repo (`research/shumjj-3516cv610_app/
device/sensor/`) now provides equivalent CV610 sensor driver source for reference.

### Driver File Structure

Every HiSilicon sensor driver has exactly **2 source files + Makefile**:

1. **`sc635hai_cmos.c`** -- ISP integration layer:
   - Per-pipe state array + context macros
   - `cmos_get_ae_default()` -- AE defaults (gain ranges, exposure limits)
   - `cmos_fps_set()` -- VTS calculation for given FPS
   - `cmos_inttime_update()` -- write exposure registers (0x3E00/01/02)
   - `cmos_gains_update()` -- write gain registers (0x3E06-0x3E09)
   - `cmos_again_calc_table()` / `cmos_dgain_calc_table()` -- gain lookups
   - `cmos_get_isp_default()` -- ISP tuning parameters
   - `cmos_get_isp_black_level()` -- per-channel black level
   - `cmos_set_image_mode()` -- resolution/mode selection
   - `cmos_set_wdr_mode()` -- WDR mode switching
   - `cmos_get_sns_regs_info()` -- register map for ISP sync
   - `sensor_register_callback()` / `sensor_unregister_callback()`
   - Exported `ot_isp_sns_obj` struct (the single entry point)

2. **`sc635hai_sensor_ctl.c`** -- Low-level I2C control:
   - I2C address (0x60 write / 0x30 7-bit), bus 0, 16-bit reg, 8-bit data
   - `i2c_init()` / `i2c_exit()`
   - `write_register()` / `read_register()` via `/dev/i2c-0`
   - `standby()` / `restart()`
   - `sc635hai_linear_6m30_10bit_init()` -- register write sequence
   - `default_reg_init()` -- apply ISP-tracked registers after mode init

### Registration Flow

```
sensor_register_callback(ViPipe, pstAeLib, pstAwbLib):
  1. sensor_ctx_init(ViPipe)           -- allocate ot_isp_sns_state
  2. cmos_init_sensor_exp_function()   -- fill ot_isp_sns_exp_func (9 callbacks)
     -> ss_mpi_isp_sensor_reg_callback(ViPipe, &stSnsAttrInfo, &stIspRegister)
  3. cmos_init_ae_exp_function()       -- fill ot_isp_ae_sensor_exp_func (9 callbacks)
     -> ss_mpi_ae_sensor_reg_callback(ViPipe, pstAeLib, &stSnsAttrInfo, &stAeRegister)
  4. cmos_init_awb_exp_function()      -- fill ot_isp_awb_sensor_exp_func
     -> ss_mpi_awb_sensor_reg_callback(ViPipe, pstAwbLib, &stSnsAttrInfo, &stAwbRegister)
```

### ISP Callback Struct (`ot_isp_sns_exp_func`)

```c
pfn_cmos_sensor_init         // call sensor's mode init (register sequence)
pfn_cmos_sensor_exit         // i2c cleanup
pfn_cmos_sensor_global_init  // one-time global state init
pfn_cmos_set_image_mode      // set resolution/fps mode
pfn_cmos_set_wdr_mode        // set linear/WDR mode
pfn_cmos_get_isp_default     // return ISP default parameters
pfn_cmos_get_isp_black_level // return black level per channel
pfn_cmos_set_pixel_detect    // dead pixel detection mode
pfn_cmos_get_sns_reg_info    // return I2C register map for ISP sync
```

### AE Callback Struct (`ot_isp_ae_sensor_exp_func`)

```c
pfn_cmos_get_ae_default      // AE defaults: gain ranges, inttime limits, accuracy
pfn_cmos_fps_set             // change FPS (updates VTS register 0x320E/0x320F)
pfn_cmos_slow_framerate_set  // slow framerate for long exposure
pfn_cmos_inttime_update      // write exposure to sensor (0x3E00/01/02)
pfn_cmos_gains_update        // write again+dgain to sensor (0x3E06-0x3E09)
pfn_cmos_again_calc_table    // linear gain -> table index lookup
pfn_cmos_dgain_calc_table    // linear dgain -> register value lookup
pfn_cmos_get_inttime_max     // max exposure per WDR mode
pfn_cmos_ae_fswdr_attr_set   // frame-sync WDR AE attributes
```

### Naming Convention: Old (EV200) vs New (CV610)

| Old (Hi3516EV200 / YJSNPI-Hi) | New (Hi3516CV610 / HIVIEW) |
|---|---|
| `HI_S32`, `HI_U32`, `HI_VOID` | `td_s32`, `td_u32`, `td_void` |
| `HI_NULL`, `HI_SUCCESS` | `TD_NULL`, `TD_SUCCESS` |
| `ISP_SNS_OBJ_S` | `ot_isp_sns_obj` |
| `ISP_SNS_STATE_S` | `ot_isp_sns_state` |
| `ISP_SENSOR_EXP_FUNC_S` | `ot_isp_sns_exp_func` |
| `AE_SENSOR_EXP_FUNC_S` | `ot_isp_ae_sensor_exp_func` |
| `HI_MPI_ISP_SensorRegCallBack` | `ss_mpi_isp_sensor_reg_callback` |
| `VI_PIPE` | `ot_vi_pipe` |
| `combo_dev_attr_t` | `combo_dev_attr_t` (unchanged) |

### MIPI Configuration (confirmed for SC635HAI)

```c
static combo_dev_attr_t mipi_2lane_sc635hai_attr = {
    .devno = 0,
    .input_mode = INPUT_MODE_MIPI,
    .data_rate = MIPI_DATA_RATE_X1,       // 1080 Mbps
    .img_rect = {0, 0, 3200, 1800},

    .mipi_attr = {
        DATA_TYPE_RAW_10BIT,
        OT_MIPI_WDR_MODE_NONE,            // Linear; OT_MIPI_WDR_MODE_VC for WDR
        {0, 1, -1, -1, -1, -1, -1, -1}    // 2 lanes on lane 0 and lane 1
    }
};
```

Source: Chinese forum post (ebaina.com) + confirmed by superb boot log string
`SC635HAI_raw_MIPI_27Minput_2Lane_10bit_1080Mbps_3200x1800_30fps`.

---

## Path to a Working Driver

### What We Have

| Item | Status | Source |
|------|--------|--------|
| Complete running register state | Done | Live I2C dump from camera |
| Frame geometry/timing (HTS=1920, VTS=2812) | Done | I2C registers 0x320C-0x320F |
| I2C address/bus/chip ID | Done | Bus 0, addr 0x30, ID 0xCE7C |
| `ot_isp_sns_obj` struct (11 ptrs) | Done | SDK headers (HIVIEW) |
| MIPI combo config struct | Done | Forum post + superb boot log |
| Mirror/flip register | Done | 0x3221, confirmed |
| Standby register | Done | 0x3000 bit 0 (standard SmartSens) |
| PQ calibration bins (4 files) | Done | On device filesystem (resfs) |
| Ghidra function addresses/sizes | Done | 14 functions identified |
| Gain model (analog) | Done | SC500AI source: 5 coarse ranges, 64 fine steps, 289-entry table |
| Gain model (digital) | Done | SC500AI source: coarse (0x3E06) + fine (0x3E07), max 31.875x |
| Exposure model | Done | SC500AI source: 3 regs (0x3E00/01/02), 16-bit, half-line precision |
| AE callback structure | Done | YJSNPI-Hi: `AE_SENSOR_EXP_FUNC_S` with 9 callbacks |
| ISP callback structure | Done | YJSNPI-Hi: `ISP_SENSOR_EXP_FUNC_S` with 9 callbacks |
| Registration flow | Done | YJSNPI-Hi: ISP + AE + AWB triple registration |
| Driver file scaffold | Done | YJSNPI-Hi: exact 2-file structure for HiSilicon |
| DPC noise threshold | Done | SC500AI: 0x5799 toggled at gain 20x/30x boundary |

### What We Still Need

1. **SC635HAI-specific gain table** -- The SC500AI's 289-entry table is for
   SC500AI. The SC635HAI may have different gain ranges or step sizes.
   Options:
   - Use the SC500AI table as-is (likely works -- same sensor family)
   - Build a table from the live register dump values
   - Extract from superb via Ghidra (search .rodata for monotonic 32-bit array)

2. **Register init sequence ordering** -- The live dump gives the end state
   but not the write order. SmartSens sensors are sensitive to init order
   (PLL must be set before timing, etc.). Options:
   - Extract from `sc635hai_linear_6m30_10bit_init` in Ghidra (102 bytes at 0x00432f80)
   - Use SC500AI ordering as template, substitute SC635HAI register values
   - Use the standard SmartSens pattern:
     `reset -> PLL bypass -> core regs -> PLL enable -> stream start`

3. **SC635HAI-specific AE limits** -- Max/min exposure, gain clamping values.
   Can be derived from VTS=2812 and the gain model, but confirming against
   superb's behavior would be ideal.

4. **Black level values** -- Per-channel sensor black level. Typically 64 for
   10-bit SmartSens sensors. Can be confirmed from PQ bins or by reading
   sensor registers.

5. **0x3E08 bit[7] meaning** -- Our dump shows 0x3E08=0x8F, but SC500AI's
   coarse gain codes don't use bit[7]. This may be:
   - A DCG (dual conversion gain) flag
   - A WDR mode indicator
   - SC635HAI-specific
   Needs investigation. If DCG: 0x8F = DCG enabled + 16x coarse (0x0F with
   DCG bit set). The SigmaStar SC501AI driver confirms bit[5] is DCG on that
   sensor, but the SC635HAI may use bit[7] for the same purpose.

### Approach: "Snapshot Driver"

The simplest viable approach uses the **live register dump as the init sequence**:

1. At sensor init, write ALL non-zero registers from the dump, following the
   standard SmartSens ordering pattern:
   - 0x0103=0x01 (software reset)
   - 0x0100=0x00 (standby)
   - 0x36E9/0x36F9 (PLL bypass)
   - Core registers (0x30xx-0x3Fxx from dump)
   - 0x36E9/0x36F9 (PLL re-enable with final values)
   - 0x0100=0x01 (stream start)

2. For AE callbacks, use the SC500AI gain model (same register encoding):
   - Exposure: write 0x3E00/0x3E01/0x3E02 (16-bit inttime, half-line)
   - Analog gain: write 0x3E08/0x3E09 (5 coarse ranges * 64 fine steps)
   - Digital gain: write 0x3E06/0x3E07 (coarse + fine, max 31.875x)

3. For mirror/flip: write 0x3221
4. For standby: write 0x3000 = 0x01
5. VTS control: write 0x320E-0x320F for frame rate
6. DPC: toggle 0x5799 at gain 20x/30x boundary (per SC500AI pattern)

### Next Steps

1. **Build the driver scaffold** -- `sc635hai_cmos.c` + `sc635hai_sensor_ctl.c`
   using YJSNPI-Hi IMX307 as structural template, with SC500AI gain/exposure
   logic adapted for CV610's `ot_` API naming

2. **Populate init sequence** from the live register dump, organized by bank:
   - 0x30xx: system/ID/standby
   - 0x32xx: frame timing (HTS, VTS, output window)
   - 0x33xx-0x36xx: analog front-end, PLL
   - 0x39xx-0x3Fxx: digital processing, AE registers

3. **Build as `libsns_sc635hai.so`** using HIVIEW SDK headers + cross-compiler

4. **Targeted Ghidra decompilation** of `sc635hai_linear_6m30_10bit_init`
   (0x00432f80, 102 bytes) to confirm register write ordering

5. **Test on camera** using the non-destructive procedure below

### Non-Destructive Testing Procedure

The driver can be tested **without reflashing** anything. The kernel modules
(ot_vi.ko, ot_isp.ko, ot_mipi_rx.ko, etc.) are the real hardware interface.
`superb` is just a userspace consumer. Our test app is another consumer using
the same kernel APIs. If anything goes wrong, reboot restores normal operation.

**Prerequisites:**
- `libsns_sc635hai.so` and a test app compiled and on the SD card
- Root shell access (port 9999 via tcpsvd backdoor)
- Camera at 192.168.1.153

**Step-by-step:**

```bash
# 1. Connect to camera root shell
# (from PC, using tools/cam_cmd.py or raw TCP)

# 2. Stop the watchdog first (otherwise it restarts superb)
killall mySystem

# 3. Stop superb (releases sensor, ISP, VENC, etc.)
killall superb
# Wait a few seconds for cleanup

# 4. Verify kernel modules are still loaded
cat /proc/umap/sys    # should show system info
cat /proc/umap/vi     # should show VI device (unconfigured now)
lsmod | head          # 49 ot_*.ko modules should still be loaded

# 5. The sensor hardware is now free. superb released its ISP/VI handles.
#    Deploy test files if not already on SD card:
ls /progs/rec/00/     # confirm SD card accessible

# 6. Set library path and run the test app
export LD_LIBRARY_PATH=/progs/rec/00:$LD_LIBRARY_PATH
cd /progs/rec/00
./sensor_test          # our test binary

# 7. The test app should:
#    a. dlopen libsns_sc635hai.so (or link statically)
#    b. Configure MIPI via combo_dev_attr_t
#    c. Register sensor callbacks with ISP framework
#    d. Initialize the sensor (write register init sequence via I2C)
#    e. Start ISP processing
#    f. Capture a frame or start VENC to confirm video output
#    g. Print success/failure status

# 8. When done testing, kill the test app (Ctrl+C or killall)

# 9. Restart normal operation
/progs/startup.sh &
# Or just reboot:
reboot
```

**What can go wrong (and recovery):**

| Problem | Recovery |
|---------|----------|
| Test app crashes / segfaults | Reboot. superb starts normally. |
| Sensor doesn't init (no I2C ack) | Reboot. superb re-inits the sensor. |
| ISP pipeline error (bad config) | Kill test app, reboot. |
| Kernel module error | Reboot reloads all modules from appfs. |
| Test app hangs | `killall sensor_test` from another shell, then reboot. |

**Nothing is modified on flash.** The test app runs from SD card, the .so loads
from SD card. All kernel modules were loaded from the read-only appfs squashfs
at boot. Rebooting always returns to stock behavior.

**Incremental testing strategy:**

1. **Phase 1: I2C only** -- Test app just opens `/dev/i2c-0`, reads chip ID
   (0x3107/0x3108 should return 0xCE/0x7C). Confirms I2C works without superb.

2. **Phase 2: Register init** -- Write the full init sequence from our dump.
   Read back key registers to confirm they took. Don't touch ISP yet.

3. **Phase 3: MIPI + ISP** -- Configure MIPI PHY, start ISP with our sensor
   callbacks, try to get a raw frame via VI channel. This is the real test.

4. **Phase 4: Full pipeline** -- VPSS + VENC, try to get an H.265 stream.
   Connect via RTSP to confirm video output.

---

## Driver Build Status

### Built Artifacts (driver/ directory)

The driver has been built and tested. Source is in `driver/src/`:

| File | Size | Purpose |
|------|------|---------|
| `build/libsns_sc635hai.so` | 13.7 KB | ARM shared library, exports `g_sns_sc635hai_obj` |
| `build/sensor_test` | 21.6 KB | Static ARM test binary |
| `src/sc635hai_cmos.h` | Constants, 289-entry gain table, register addresses |
| `src/sc635hai_cmos.c` | ISP/AE/AWB callbacks, `ot_isp_sns_obj` (11 function pointers) |
| `src/sc635hai_sensor_ctl.c` | I2C r/w, 250+ register init sequence, standby/restart |

The `.so` links against `libhi_mpi_isp.so`, `libhi_mpi_ae.so`, `libhi_mpi_awb.so`
(already present on camera as part of the kernel module / SDK layer).

### Build Requirements

- WSL (toolchain is Linux x86_64)
- `make` installed in WSL (`apt-get install make`)
- Build command: `cd /mnt/e/Projects/ipc_XMeye_camera/driver && make all`

### Toolchain Quirks

The toolchain repo (`research/hi3516cv610_toolchain/`) has issues with Git LFS:

1. **Binary names**: Files named `arm-v01c02-linux-musleabi-gcc` in `bin/` are
   one-line text scripts containing `arm-linux-musleabi-gcc`. The actual ELF
   binaries are named `arm-linux-musleabi-*`. The Makefile uses the real names.

2. **LTO plugin broken**: `liblto_plugin.so` is a text pointer (Git LFS not
   fetched), causing linker errors. Fixed with `-fno-lto -fno-use-linker-plugin`
   in the Makefile. No impact on output quality.

### SDK Field Names (CV610 vs SC500AI/EV200 reference code)

When adapting SC500AI (Sophgo CVI_ API) or IMX307 (HiSilicon HI_ API) code
to the CV610 SDK (ot_/td_ API), these field renames are needed:

| SC500AI / EV200 | CV610 SDK | Struct |
|-----------------|-----------|--------|
| `u32Data` | `data` | `ot_isp_i2c_data` |
| `reg_addr_byte_num` | `addr_byte_num` | `ot_isp_i2c_data` |
| `f32_fps` / `f32Fps` | `fps` | `ot_isp_ae_sensor_default` |
| `fl_std` | `full_lines_std` | `ot_isp_ae_sensor_default` |
| `int_time_max` | `int_time_max[0]` | `ot_isp_ae_int_time_range` (array) |
| `int_time_min` | `int_time_min[0]` | `ot_isp_ae_int_time_range` (array) |

The `hi_` prefix types are 100% identical aliases for `ot_`/`td_` types (defined
in `hi_type.h`, `hi_common.h`, etc. via simple typedefs). API functions use
`hi_mpi_*` prefix. Struct definitions use `ot_*` prefix. Both work interchangeably.

---

## Test Results

### Phase 1: I2C Chip ID Read (PASSED)

Run with superb streaming. Read-only, zero risk.

```
=== Phase 1: Chip ID Read ===
[OK] I2C bus 0 opened, slave addr 0x30
  Chip ID: 0xCE7C [OK] SC635HAI confirmed
  VTS: 0x0AFC (expect 0x0AFC = 2812)
  HTS: 0x0780 (expect 0x0780 = 1920)
  Mirror/Flip: 0x00
  Stream: 0x01 (0x01=streaming, 0x00=standby)
  Again: 0x3E08=0x00, 0x3E09=0x20
  Dgain: 0x3E06=0x00, 0x3E07=0x80
[OK] Phase 1 complete
```

**Key observations:**
- Chip ID confirmed: 0xCE7C
- All frame timing registers match our init table exactly
- Sensor is streaming (0x0100=0x01)
- AE registers (0x3E08/09) show 0x00/0x20 — different from original dump
  (0x8F/0x3F). This is expected: the ISP AE loop adjusts gain every frame.
  Current values indicate low gain (bright conditions).
- Dgain 0x00/0x80 = 1.0x digital gain (minimum, normal)

### Phase 2: Register State Verification (PASSED)

Run with superb streaming. Read-only verification of all frame geometry
and key analog/PLL registers against our init table values.

```
=== Phase 2: Register State Verification ===
  0x3208 = 0x0C [OK]    (width high = 3200)
  0x3209 = 0x80 [OK]
  0x320A = 0x07 [OK]    (height high = 1800)
  0x320B = 0x08 [OK]
  0x320C = 0x07 [OK]    (HTS = 1920)
  0x320D = 0x80 [OK]
  0x320E = 0x0A [OK]    (VTS = 2812)
  0x320F = 0xFC [OK]

  Analog/PLL registers:
  0x3031 = 0x0A    0x3034 = 0x06    0x36E9 = 0x24
  0x36EA = 0x14    0x36EB = 0x45    0x36EC = 0x4B
  0x36ED = 0x18    0x3301 = 0x12    0x3306 = 0x70
  0x3309 = 0xB0    0x330B = 0x20    0x3616 = 0xBC
  0x3630 = 0x46    0x3633 = 0x4D

  Stream status: 0x01
[OK] Phase 2 complete -- all frame geometry registers match
```

**All registers match the values in our init sequence.** This confirms the
live I2C register dump used to build the driver is accurate.

### Phase 3: MIPI + ISP Pipeline (IN PROGRESS)

Phase 3 requires a substantial test application that sets up the full
Hi3516CV610 video pipeline. See "Phase 3 Specification" section below.

---

## Phase 3 Specification

Phase 3 is the real test: stop superb, initialize the sensor with our driver,
and bring up the full ISP video pipeline to capture a frame.

### What Phase 3 Must Do

A new test application (`driver/test/pipeline_test.c`) that:

1. Loads `libsns_sc635hai.so` via `dlopen`/`dlsym("g_sns_sc635hai_obj")`
2. Configures the system (VB pools, sys init)
3. Configures MIPI PHY via `/dev/ot_mipi_rx` ioctl
4. Sets up VI device, pipe, and channel
5. Registers sensor with ISP (ISP + AE + AWB callbacks)
6. Starts the ISP processing loop in a thread
7. Sets up VPSS for format conversion
8. Captures a frame (either raw from VI or JPEG via VENC)
9. Writes frame to SD card as proof
10. Tears down cleanly

### API Call Sequence (from HIVIEW reference code)

The complete pipeline requires ~26 SDK API calls in strict order.
Reference source: `research/HIVIEW/mod/mpp/3516c/src/`

```
── System Init ──────────────────────────────────────────────
1.  hi_mpi_sys_exit()                    // clean slate
2.  hi_mpi_vb_exit()
3.  hi_mpi_vb_set_cfg(vb_conf)           // video buffer pools
4.  hi_mpi_vb_init()
5.  hi_mpi_sys_init()
6.  hi_mpi_sys_set_vi_vpss_mode()        // online/offline mode

── MIPI PHY ─────────────────────────────────────────────────
7.  open("/dev/ot_mipi_rx")
8.  ioctl(HI_MIPI_SET_HS_MODE)           // lane divide mode
9.  ioctl(HI_MIPI_ENABLE_SENSOR_CLOCK)
10. ioctl(HI_MIPI_RESET_SENSOR)
11. ioctl(HI_MIPI_UNRESET_SENSOR)
12. ioctl(HI_MIPI_ENABLE_MIPI_CLOCK)
13. ioctl(HI_MIPI_RESET_MIPI)
14. ioctl(HI_MIPI_SET_DEV_ATTR, &combo_dev_attr)  // our SC635HAI config
15. ioctl(HI_MIPI_UNRESET_MIPI)

── VI Device + Pipe ─────────────────────────────────────────
16. hi_mpi_vi_set_dev_attr()
17. hi_mpi_vi_enable_dev()
18. hi_mpi_vi_bind(dev, pipe)
19. hi_mpi_vi_create_pipe() + hi_mpi_vi_start_pipe()
20. hi_mpi_vi_set_chn_attr() + hi_mpi_vi_enable_chn()

── ISP ──────────────────────────────────────────────────────
21. sns_obj->pfn_register_callback(pipe, ae_lib, awb_lib)
22. sns_obj->pfn_set_bus_info(pipe, i2c_bus)
23. hi_mpi_isp_mem_init(pipe)
24. hi_mpi_isp_set_pub_attr(pipe, &pub_attr)
25. hi_mpi_isp_init(pipe)
26. Thread: hi_mpi_isp_run(pipe)         // blocks, runs AE loop

── VPSS + Capture ───────────────────────────────────────────
27. hi_mpi_vpss_create_grp() + hi_mpi_vpss_start_grp()
28. hi_mpi_vpss_set_chn_attr() + hi_mpi_vpss_enable_chn()
29. hi_mpi_sys_bind(VI -> VPSS)
30. hi_mpi_vpss_get_chn_frame()          // get a YUV frame
    -- or --
    hi_mpi_venc_start() + hi_mpi_venc_send_frame()  // JPEG encode
```

### SC635HAI-Specific Configuration

**MIPI config** (combo_dev_attr_t):
```c
{
    .devno = 0,
    .input_mode = INPUT_MODE_MIPI,
    .data_rate = MIPI_DATA_RATE_X1,       // 1080 Mbps
    .img_rect = {0, 0, 3200, 1800},
    .mipi_attr = {
        DATA_TYPE_RAW_10BIT,
        HI_MIPI_WDR_MODE_NONE,
        {0, 1, -1, -1}                    // 2 lanes
    }
}
```

**ISP pub_attr**:
```c
{
    .wnd_rect = {0, 0, 3200, 1800},
    .sns_size = {3200, 1800},
    .frame_rate = 30,
    .bayer_format = HI_ISP_BAYER_BGGR,    // assumed, standard SmartSens
    .wdr_mode = HI_WDR_MODE_NONE,
}
```

**VI dev_attr**:
```c
{
    .intf_mode = HI_VI_INTF_MODE_MIPI,
    .work_mode = HI_VI_WORK_MODE_MULTIPLEX_1,
    .scan_mode = HI_VI_SCAN_PROGRESSIVE,
    .data_type = HI_VI_DATA_TYPE_RAW,
    .in_size = {3200, 1800},
    .data_rate = HI_DATA_RATE_X1,
}
```

### Libraries Required

The test app must link against (all available in `research/HIVIEW/mod/mpp/3516c/lib/hisisdk/`):
- `libhi_mpi_isp.so` -- ISP control
- `libhi_mpi_ae.so` -- AE algorithm
- `libhi_mpi_awb.so` -- AWB algorithm
- `libhi_mpi_vi.so` -- Video Input (if separate)
- `libhi_mpi_vpss.so` -- Video Processing
- `libhi_mpi_venc.so` -- Video Encoding (for JPEG capture)
- `libhi_mpi_sys.so` -- System
- `libhi_mpi_vb.so` -- Video Buffer
- `libsns_sc635hai.so` -- Our sensor driver (loaded via dlopen)

At runtime these .so files must be either:
- On the camera already (check `/usr/lib/` or loaded by kernel modules)
- Deployed to SD card alongside the test app

### Reference Source Files (for writing Phase 3)

| File | What to use from it |
|------|-------------------|
| `research/HIVIEW/mod/mpp/3516c/src/common/sample_comm_sys.c` | VB config, sys init |
| `research/HIVIEW/mod/mpp/3516c/src/common/sample_comm_vi.c` | MIPI setup, VI device/pipe/channel, ISP startup |
| `research/HIVIEW/mod/mpp/3516c/src/common/sample_comm_isp.c` | Sensor registration, ISP run thread |
| `research/HIVIEW/mod/mpp/3516c/src/common/sample_comm_vpss.c` | VPSS group/channel setup |
| `research/HIVIEW/mod/mpp/3516c/src/common/sample_comm_venc.c` | VENC channel setup, JPEG capture |
| `research/HIVIEW/mod/mpp/3516c/src/sample_venc.c` | `sample_venc_normal()` — minimal complete pipeline |
| `research/HIVIEW/mod/mpp/3516c/src/mpp.c` | Production pipeline, sensor dlopen, JPEG snapshot |

### Success Criteria

Phase 3 is successful if:
1. No kernel panics or hard hangs (reboot recovers)
2. ISP initializes without errors
3. AE loop runs (exposure/gain registers update)
4. A frame is captured and saved to SD card
5. The saved frame shows a recognizable image (not black, not garbage)

### Risk Mitigation

- Run from SD card, no flash writes
- If ISP init fails, the test app prints error and exits
- If pipeline hangs, `killall pipeline_test` + `reboot` recovers
- All kernel modules remain loaded from boot (our app doesn't load/unload them)
- The sensor PQ bins at `/home/sensor/sc635hai/pqbin/` must still be accessible
  (they're on resfs, bind-mounted at boot -- verify path after stopping superb)

---

## Phase 3 Results: B040/B051 SDK Mismatch and Resolution

Phase 3 testing revealed a major obstacle: the camera's kernel modules are
**MPP V1.0.2.0 B051** (April 2025) while the only SDK available at the time
was **V1.0.1.0 B040** (September 2024). Between these versions, HiSilicon
changed ioctl command numbers, struct content, and ISP protocols. This section
documents every difference found and how each was resolved.

### SDK Version Timeline

| Version | Build | Date | Source | ISP ioctl type |
|---------|-------|------|--------|----------------|
| V1.0.0.3 B030 | kodo-hi3516cv610 | ~2024 | GitHub | 0x49 (old) |
| V1.0.1.0 B040 | HIVIEW/3516c | Sep 2024 | GitHub | 0x49 (old) |
| V1.0.2.0 B051 | Camera kernel | Apr 2025 | On device | 0x70 (new) |
| V1.0.2.1 B020 | Hi3516CV610_SDK | ~2025 | Found online | 0x70 (new) |

B020 and B051 are near-identical in their ioctl interfaces despite the
different build numbers. The V1.0.2.1 B020 SDK (with `ss_mpi_*` APIs) is
the correct match for the camera's B051 kernel modules.

### Pipeline Stage Results

| Stage | Method | Status | Notes |
|-------|--------|--------|-------|
| Sensor driver load | dlopen `libsns_sc635hai.so` | OK | |
| VB pool init | SDK library | OK | Same between B040/B051 |
| SYS init | SDK library | OK | Same between B040/B051 |
| VI-VPSS mode | SDK library | OK | Same between B040/B051 |
| MIPI RX | Raw ioctls on `/dev/ot_mipi_rx` | OK | Same between B040/B051 |
| VI device | B040 lib + vi_shim.so | OK | Needed offset 56 patch |
| VI pipe | B040 lib + vi_shim.so | OK | |
| VI channel | B040 lib + vi_shim.so | OK | Needed SET_CHN_ATTR nr+7 |
| ISP registration | Raw B051 ioctl | OK | Type 0x70 |
| ISP mmap | Raw mmap on `/dev/isp_dev` | OK | Fixed hardware addresses |
| ISP SET_PUB_ATTR | Raw B051 ioctl | OK | 52-byte struct |
| ISP mem_init | B040 library | BLOCKED | Incompatible mmap protocol |
| ISP init/run | B040 library | BLOCKED | Depends on mem_init |
| VPSS create/start | SDK library | OK | Same between B040/B051 |
| VPSS set_chn_attr | SDK library | BLOCKED | Missing new ioctl (see below) |
| VPSS set_chn_attr | Raw ioctl with nr=7 first | OK | B051 requires new step |
| VENC | Not reached | -- | Blocked by ISP |

### Discovery: B051 VPSS Requires New Ioctl Before SET_CHN_ATTR

The B051 kernel module for VPSS added a **new required ioctl** (`_IO('P', 7)`,
command `0x00005007`) that must be called on the **group fd** after START_GRP
and before any channel operations. Without this call, SET_CHN_ATTR returns
`0xA0078007` (NOT_CONFIG).

This was discovered through systematic probing of all VPSS ioctl nr values
(0-20) in both `_IO` and `_IOW(4)` forms. Only nr=7 as `_IO` succeeded on
the group fd. After calling it, SET_CHN_ATTR and ENABLE_CHN both succeed.

**B040 VPSS flow** (old):
```
1. VPSS_REG (grp fd)
2. VPSS_SET_GRP_ATTR (grp fd)
3. VPSS_START_GRP (grp fd)
4. VPSS_REG (chn fd)
5. VPSS_SET_CHN_ATTR (chn fd)    <-- works in B040
6. VPSS_ENABLE_CHN (chn fd)
```

**B051 VPSS flow** (new):
```
1. VPSS_REG (grp fd)             -- 0x40045000, 4 bytes
2. VPSS_SET_GRP_ATTR (grp fd)    -- 0x4038500c, 56 bytes
3. VPSS_START_GRP (grp fd)       -- 0x00005005
4. VPSS_CREATE_CHN (grp fd)      -- 0x00005007  <-- NEW, REQUIRED
5. VPSS_REG (chn fd)             -- 0x40045000, 4 bytes
6. VPSS_SET_CHN_ATTR (chn fd)    -- 0x40605008, 96 bytes
7. VPSS_ENABLE_CHN (chn fd)      -- 0x0000500a
```

This discovery was confirmed with `vpss_test.c` -- a minimal standalone test
that does only VB/SYS init + VPSS operations, proving the issue is independent
of VI or ISP state.

### B051 VPSS Channel Attributes (from running superb)

Read via `_IOR('P', 9, 96)` = `0x80605009` while superb was running:

```
offset  field          B051 value   B040 struct field
  0     mirror_en      0            mirror_en
  4     flip_en        0            flip_en
  8     (reserved)     0            (gap)
 12     width          3200         width
 16     height         1800         height
 20     depth          0            depth
 24     chn_mode       1 (AUTO)     chn_mode
 28     video_format   0 (LINEAR)   video_format
 32     dynamic_range  0 (SDR8)     dynamic_range
 36     pixel_format   38 (YUV420)  pixel_format
 40     compress_mode  0 (NONE)     compress_mode
 44     src_frame_rate -1           frame_rate.src
 48     dst_frame_rate -1           frame_rate.dst
 52-95  (reserved)     0            (padding)
```

The struct layout is identical between B040 and B051 for VPSS chn_attr (96 bytes).

### B040->B051 VI Ioctl Command Number Changes

The VI module changed several ioctl `nr` values between B040 and B051.
All have the same type byte (0x49) and struct sizes.

| Operation | B040 nr | B051 nr | Delta | Ioctl cmd (B040 -> B051) |
|-----------|---------|---------|-------|--------------------------|
| VI_REG | 90 (0x5a) | 97 (0x61) | +7 | 0x4004495a -> 0x40044961 |
| SET_CHN_ATTR | 71 (0x47) | 78 (0x4e) | +7 | 0x402c4947 -> 0x402c494e |
| GET_CHN_ATTR | 72 (0x48) | 79 (0x4f) | +7 | 0x802c4948 -> 0x802c494f |
| ENABLE_CHN | 75 (0x4b) | 82 (0x52) | +7 | 0x0000494b -> 0x00004952 |
| DISABLE_CHN | 76 (0x4c) | 83 (0x53) | +7 | 0x0000494c -> 0x00004953 |

All other VI commands (SET_DEV_ATTR, ENABLE_DEV, BIND, WDR_FUSION,
SET_PIPE_ATTR, START_PIPE, etc.) use the same nr values in both versions.

Additionally, the VI SET_DEV_ATTR struct (120 bytes) requires a new field at
**offset 56 set to 1** in B051 (zero in B040). This field is related to
MIPI input mode. Without it, SET_DEV_ATTR returns ILLEGAL_PARAM.

The VI WDR_FUSION struct (28 bytes) requires the **sensor height at offset 8**
in B051. The B040 library sends zeros there.

### B040->B051 ISP Ioctl Changes (Complete Redesign)

The ISP module was completely redesigned between B040 and B051. The ioctl
type byte changed from 0x49/0x45 to 0x70, and command numbers have no
simple arithmetic mapping.

| Operation | B040 cmd | B051 cmd |
|-----------|----------|----------|
| ISP_REG | 0x40044900 (type 0x49) | 0x40047000 (type 0x70) |
| ISP_MEM_INIT | 0x40044971 | No B051 equivalent found |
| ISP_GET_STATUS | 0x80044920 | 0x80047022 |
| ISP_SET_PUB | ~0x40444938 (68 bytes) | 0x4034703a (52 bytes) |
| ISP_GET_PUB | ~0x80444939 (68 bytes) | 0x8034703b (52 bytes) |

The ISP pub_attr struct shrank from 68 bytes (B040) to 52 bytes (B051).
Superb's B051 ISP pub_attr values (from ioctl trace):

```
offset  value   likely field
  0     4       bayer_format or intf_mode
  4     30      frame_rate
  8     1       wdr_mode+1 or enable flag
 16     200     unknown (SNR related?)
 20     7       unknown
 24-51  0       (zeros)
```

The B040 ISP library's `isp_mem_init` cannot work with B051 because the
mmap-based virtual register protocol changed entirely. The B040 library
calls `ISP_MEM_INIT` (0x40044971) to allocate vreg memory, but this ioctl
has no equivalent in B051. In B051, ISP vreg memory appears to be
allocated automatically during ISP_REG.

### ISP Hardware Register Addresses (from /proc/iomem)

The ISP uses fixed SoC hardware register blocks (not dynamically allocated):

```
17400000-174fffff : vi_cap0   (ISP capture / BE config)
17800000-1783ffff : vi_proc0  (ISP processing registers)
17900000-1790ffff : vpss0
```

Superb maps 3 regions from `/dev/isp_dev` (confirmed via /proc/pid/maps):

| Virtual region | Physical addr | Size | SoC block |
|----------------|---------------|------|-----------|
| ISP BE config | 0x17420000 | 128KB | vi_cap0 + 0x20000 |
| ISP proc ctrl | 0x17800000 | 4KB | vi_proc0 base |
| ISP proc regs | 0x17820000 | 64KB | vi_proc0 + 0x20000 |

These mmaps succeed from userspace via `mmap(fd_isp, phys_addr, size)`.
However, all registers read as zero until the ISP run loop is active,
confirming that the ISP hardware block needs proper initialization
(clock enable, reset release, register configuration) before it processes
frames.

### Resolution: V1.0.2.1 SDK

The V1.0.2.1 SDK (build B020) was found with complete `.so`/`.a` libraries
and headers. Despite the lower build number (B020 vs B051), the ioctl
interfaces are near-identical:

- ISP uses type 0x70 ioctls (matches B051 kernel)
- API names use `ss_mpi_*` prefix (replacing `hi_mpi_*` from B040)
- Type names use `ot_*`/`td_*` natively (B040 had `hi_*` aliases)
- Headers include `ot_common_*.h` instead of `hi_common_*.h`
- The `ot_isp_sns_obj` struct gained a `pfn_set_fast_ae` field

With this SDK:
- **vi_shim.so is no longer needed** -- the B020 libraries use B051-compatible
  ioctl command numbers natively
- **ISP init should work** -- the B020 `libot_mpi_isp.so` speaks the type 0x70
  ISP protocol, including the vreg mmap flow
- **VPSS nr=7 should be handled** -- the B020 VPSS library likely includes
  the CREATE_CHN call internally (needs verification)

The sensor driver (`sc635hai_cmos.c`) and pipeline test (`pipeline_test.c`)
have been migrated to use V1.0.2.1 headers and `ss_mpi_*` APIs. A
compatibility header (`hi_compat.h`) maps old `hi_*` names to `ot_*`/`ss_mpi_*`
so the pipeline_test body code doesn't need wholesale renaming.

### Superb Binary Ioctl Trace (B051 Reference)

Complete init sequence captured via `ioctl_hook.so` LD_PRELOAD on superb.
This is the ground truth for what the B051 kernel expects:

```
=== SYS/VB ===
SYS_BIND (0x40185908): setup module connections
VB_SET_CFG (0x4308420b): 2 pools, 11.5MB + 8.6MB
VB_INIT (0x00004208)
SYS_INIT (0x00005900)
SYS_SET_VI_VPSS_MODE (0x40105910): all zeros (offline-offline)

=== MIPI RX ===
MIPI SET_HS_MODE, ENABLE_SENSOR_CLOCK, RESET/UNRESET_SENSOR
MIPI ENABLE_MIPI_CLOCK, RESET_MIPI
MIPI SET_DEV_ATTR: 3200x1800, RAW10, 2-lane, data_rate=0x10000
MIPI UNRESET_MIPI

=== VI ===
VI REG (0x40044961): dev_id=0
VI SET_DEV_ATTR (0x40784900, 120 bytes):
  [0]=4(MIPI) [8]=0xFFF00000 [20-32]=-1 [56]=1(B051_NEW)
  [108]=3200 [112]=1800
VI ENABLE_DEV (0x00004902)
VI BIND (0x4004490a): pipe=0
VI WDR_FUSION (0x401c490d, 28 bytes): all zeros
VI SET_PIPE_ATTR (0x40204910, 32 bytes):
  [8]=3200 [12]=1800 [16]=0x18(BAYER10) [24-28]=-1,-1
VI START_PIPE (0x0000491e)
VI SET_CHN_ATTR (0x402c494e, 44 bytes):
  [0]=3200 [4]=1800 [8]=0x26(YUV420) [36-40]=-1,-1
VI ENABLE_CHN (0x00004952)

=== ISP ===
ISP REG (0x40047000): pipe=0
ISP SET_PUB (0x4034703a, 52 bytes):
  [0]=4 [4]=30 [8]=1 [16]=200 [20]=7
ISP GET_STATUS (0x80047022): returns 1

=== VPSS ===
VPSS REG (0x40045000): grp=0
VPSS SET_GRP_ATTR (0x4038500c, 56 bytes):
  [16]=3200 [20]=1800 [36]=0x26(YUV420) [48-52]=-1,-1
VPSS START_GRP (0x00005005)
VPSS REG (0x40045000): chn=0 (new fd)
VPSS SET_CHN_ATTR (0x40605008, 96 bytes):
  [12]=3200 [16]=1800 [20]=1(depth) [36]=0x26 [44-48]=-1,-1
VPSS ENABLE_CHN (0x0000500a)

=== SYS BIND ===
SYS_BIND (0x40185907, 24 bytes): VI(pipe0,chn0) -> VPSS(grp0)
```

### Tools Built for Phase 3

| Tool | File | Purpose |
|------|------|---------|
| pipeline_test | `driver/test/pipeline_test.c` | Full pipeline test binary |
| vpss_test | `driver/test/vpss_test.c` | Minimal VPSS ioctl probe |
| vpss_read | `driver/test/vpss_read.c` | Read VPSS state from running superb |
| vi_shim.so | `tools/vi_shim.c` | B040->B051 VI ioctl translator (LD_PRELOAD) |
| ioctl_hook.so | `tools/ioctl_hook.c` | Ioctl/mmap logger (LD_PRELOAD) |
| check_offsets | `driver/test/check_offsets.c` | Prints struct field offsets |
| hi_compat.h | `driver/src/hi_compat.h` | B040 hi_ to V1.0.2.1 ot_/ss_mpi_ name mapping |

### Phase 3 Next Steps

1. **Build and test with V1.0.2.1 SDK** -- the libraries should handle ISP init
   natively, eliminating the ISP blocker entirely
2. **Deploy V1.0.2.1 .so files to camera** -- replace the B040 libraries on SD card
3. **Verify VPSS nr=7** is handled by the new SDK's `ss_mpi_vpss_set_chn_attr`
4. **Run full pipeline** -- if ISP and VPSS work, capture a JPEG frame
5. **vi_shim.so may be retired** -- verify the new SDK's VI ioctls match B051

---

## Open Source Status

**No open-source SC635HAI driver exists anywhere** (searched GitHub, OpenIPC,
HIVIEW, Sophgo SensorSupportList, YJSNPI-Hi, general web). The sensor was
announced September 2024 and is still very new. SmartSens keeps register maps
under NDA.

However, with the SC500AI source code (gain model, exposure logic), the
YJSNPI-Hi driver scaffold (HiSilicon callback structure), and our live
register dump (complete sensor state), we have **built a working driver**
without the official documentation. The driver compiles, exports the correct
symbol, and Phase 1+2 testing confirms our register values match the running
sensor.

Remaining risks:
- Register init ordering (mitigated by standard SmartSens pattern + Phase 2 confirms values match)
- 0x3E08 bit[7] meaning (Phase 1 showed 0x00 in bright light -- may only appear in low light / high gain)
- SC635HAI-specific gain table differences from SC500AI (likely minimal)
- ~~Bayer pattern assumed BGGR (standard SmartSens, but unconfirmed for SC635HAI)~~
  **CONFIRMED BGGR** -- setting RGGB produces R/B color swap. PQ bin overrides
  bayer_format to RGGB; must re-set to BGGR after every PQ bin load.

---

## Bayer Pattern: BGGR (Confirmed)

SC635HAI uses BGGR Bayer pattern, consistent with all SmartSens sensors on this
platform (SC4336P, SC500AI). Evidence:

1. SC4336P reference driver uses `OT_ISP_BAYER_BGGR`
2. SC500AI PQ configs use `bayer_format = 3` (BGGR)
3. shumjj third-party app README: forcing SC4336P from BGGR to RGGB
   "will cause color anomalies" (R/B swap) -- the exact symptom we had
4. Our ISP output with RGGB shows swapped R/B channels; BGGR is correct

**Critical**: The PQ bin `day.bin` ISP calibration (type 0) overrides
`pub_attr.bayer_format` from BGGR(3) to RGGB(0). Must re-set after every
PQ bin load. All 4 scene bins (day/night/light/black) have this override.

Mirror/flip (register 0x3221) changes the physical Bayer pattern:
- `0x00` = normal BGGR
- `0x06` = mirror = GRBG
- `0x60` = vflip = GBRG
- `0x66` = both = RGGB

Superb runs with `0x3221 = 0x00` (normal BGGR).

---

## AWB Calibration (Extracted from Superb)

SC635HAI AWB calibration was extracted from superb's running ISP via read-only
query APIs (tool: `driver/test/awb_dump.c`). Key values in `sc635hai_cmos.c`:

| Parameter | Value | Notes |
|-----------|-------|-------|
| Ref color temp | 4950K | D50 reference |
| Gain offset R | 477 (~1.86x) | Higher than SC4336P (409, ~1.60x) |
| Gain offset B | 535 (~2.09x) | Higher than SC4336P (452, ~1.77x) |
| Planckian p1 | -31 | Negative (SC4336P is +36) |
| Planckian p2 | 287 | |
| Planckian a,b,c | 187899, 128, -137074 | |
| CCM temps | 6350, 4950, 3850, 2640K | 4 matrices |
| Init WB R/G/B | 523/256/538 | Daylight converged |
| AWB algorithm | ADVANCE (not LOWCOST) | Set in pipeline_test.c |
| AWB run interval | 2 | Every 2nd frame |

SC635HAI has significantly different quantum efficiency from SC4336P -- the
Planckian curve shape and gain ratios are quite different. Using SC4336P
calibration produces CT~4566K for a scene that superb reports as ~5524K.
With SC635HAI-specific calibration, we get CT~5617K (near-match).

---

## Platform Noise Reduction Architecture (Hi3516CV610)

The Hi3516CV610 has a multi-stage NR pipeline:

### Stage 1: ISP Bayer NR (`ot_isp_nr_attr`)
- Operates in Bayer domain (before demosaic)
- API: `ss_mpi_isp_set_nr_attr` / `ss_mpi_isp_get_nr_attr`
- `snr_cfg`: Spatial NR with `fine_strength` [0,128] and `coring_wgt` [0,3200]
  per-ISO auto table (16 entries for OT_ISP_AUTO_ISO_NUM)
- `md_cfg`: Motion detection NR (Hi3516CV610-only, replaces tnr_cfg on Hi3519DV500)
  with `md_static_fine_strength`, `tfs` (temporal filter), `sfr_r/g/b` per-ISO
- PQ bin `day.bin` sets this to auto mode with fine_str=80, coring_wgt=50

### Stage 2: DRC BCNR (`ot_isp_drc_bcnr_attr`)
- Hi3516CV610-specific Bayer chroma NR embedded in the DRC module
- `enable` [0,1], `strength` [0,8], `detail_restore_lut` [16 entries]
- PQ bin sets enable=0, strength=3 by default (disabled!)
- Also: `dark_gain_limit_chroma` [0, 0x85] in DRC attr limits chroma
  amplification in shadows (superb sets this to 0 = no chroma boost)

### Stage 3: 3DNR V2 at VI Pipe (`ot_3dnr_attr` / `ot_3dnr_param`)
- **Must use VI pipe APIs** (not VPSS) on this platform with VI_ONLINE_VPSS_OFFLINE
- VPSS 3DNR calls return `0xA007800C` (NOT_PERM)
- Platform uses **NR V2** structures (not V1). V1 returns `0xA0108007`.
- APIs: `ss_mpi_vi_set_pipe_3dnr_attr` / `ss_mpi_vi_set_pipe_3dnr_param`
- Contains temporal + spatial luma NR (tfy, sfy, mdy) and chroma NR (nrc0, nrc1)
- Chroma NR (nrc0): `trc` [0,255] temporal, `tfc` [0,63], `tfs` [0,15]
- Chroma NR (nrc1): `sfs1` [0,255] spatial, `sfs2_coarse` [0,31]
- Must read-modify-write: read current params, modify only known fields, write back.
  Zeroing the whole struct causes ILLEGAL_PARAM (pshrp/sfy have complex defaults).
- 3DNR confirmed active by ~3x bitstream size reduction at same ISO.

### Stage 4: VENC (H.265 encoder NR)
- VBR mode with QP 35-44 matching superb's SystemCfg.ini
- No additional NR tuning at VENC level -- relies on upstream ISP/3DNR

### No standalone CNR API
This SDK has **no** `ss_mpi_isp_set_cnr_attr`. Chroma NR is done via:
1. DRC BCNR (Bayer domain, stage 2)
2. 3DNR nrc0/nrc1 (post-demosaic, stage 3)

### Superb's framerate: 20fps sensor, 15fps encode
**CORRECTION**: SystemCfg.ini's 15fps is the **VENC output rate**, not the
sensor/ISP rate. Live evidence from `/proc/umap/vi`:
- `src_rate=20, dst_rate=20` in VI pipe
- `frame_rate=20` in VI chn status
- `/proc/umap/isp` AE: `fps: 20.00, real_fps: 2000`
- VTS register live: `0x320E/0F = 0x0AFC` = VTS=2812 (20fps native)

The sensor runs at 20fps (VTS=2812). VENC takes 20fps input and encodes at
15fps by dropping every 4th frame. The extra frames improve 3DNR temporal
quality (more samples for motion detection and frame averaging).
Max exposure per frame at 20fps: ~50ms (VTS=2812).

---

## Superb's SystemCfg.ini Video Configuration

From `/etc/conf.d/syscfg/SystemCfg.ini`:

| Channel | Resolution | FPS | RC Mode | Bitrate | QP Range |
|---------|-----------|-----|---------|---------|----------|
| CH1 (main) | 3840x2160 | 15 | VBR (3) | 4096 kbps | 35-44 |
| CH2 (sub) | 720x576 | 15 | VBR (3) | 1024 kbps | 24-38 |

Note: Config says 3840x2160 but sensor is 3200x1800. Superb may upscale
or this may be a template value. The 15fps here is the **encoded output rate**;
the sensor/ISP runs at 20fps (confirmed via `/proc/umap/vi` and live VTS read).

---

## WDR/HDR Mode: Linear (No Sensor HDR)

**Superb uses pure LINEAR mode.** Evidence:

- `/proc/umap/vi` WDR fusion: `wdr_mode: none`
- `/proc/umap/isp` pub_attr: `wdr_mode: linear`, `bayer: rggb`
- MIPI WDR mode: `OT_MIPI_WDR_MODE_NONE`
- VI WDR fusion struct: all zeros in ioctl trace
- Sensor init: calls `sc635hai_linear_6m30_10bit_init` (not `vc_wdr_2t1`)

Superb's "WDR" feature (`bEnableWdr=1, wdrStrength=256` in SystemCfg.ini) is
**ISP DRC (Dynamic Range Compression)** -- purely digital tone-mapping. Ghidra
decompilation confirms: `secu_sensor_digital_wdr_set` calls
`hi_mpi_isp_set_drc_attr()` with enable + strength params.

The SC635HAI hardware supports 2-exposure staggered HDR (`vc_wdr_2t1`) and
InSensor HDR, but neither is used on this camera.

---

## Superb's Live ISP State (from /proc/umap/isp, low-light ISO ~19189)

Captured while superb is running, nightlight-only scene:

### AE
- `fps: 20.00`, `real_fps: 2000`, `vmax: 2812`
- `again: 85800`, `dgain: 2336`, `isp_dg: 1028`, `iso: 19189`
- `slow_mod: 1` (enabled but not currently active)
- `max_line: 2804`, `max_agt: 85801`, `max_dgt: 16128`, `max_idgt: 4096`

### AWB
- `sat: 103`, `speed: 256`
- `gain0=0x11f, gain1=0x100, gain2=0x100, gain3=0x2b1` (R~1.12x, B~2.69x)
- `cotemp: 2680` K

### BayerNR
- `enable: 1`, `md_en: 1`, `nr_lsc_enable: 0`
- `fine_strength: 80`, `coring_wgt: 50`, `sfm0_de_prot: 16`, `tss: 39`
- `sfm0_coarse_str 1-4: 108`
- `md_mode: 2`, `tfs: 255`, `md_sta_ratio: 26`, `md_mot_ratio: 13`
- `md_sta_fine_str: 55`, `md_anti_fli_str: 32`
- `sfr_r: 26`, `sfr_g: 32`, `sfr_b: 26`
- `bnr_proc_iso: 19114`

### DRC
- `en: 1`, `manu_en: 1`, `strength: 256`

### Dehaze
- `enable: 1`, `manu_en: 1`, `manu_strength: 32`

### Sharpen
- `enable: 1`, `texture_freq: 100`, `edge_freq: 100`
- `over_shoot: 7`, `under_shoot: 33`, `detail_ctrl: 124`

### Demosaic
- `enable: 1`, `nondir_str: 64`, `nondir_mf_str: 15`, `hf_str: 8`

### Black Level
- `mode: manual`, `isp_blc: 1012` (all channels)

### 3DNR V2 (from /proc/umap/vi)
- `enable: Y`, `nr_type: NORM`, `compress_mode: frame`, `nr_motion_mode: NORM`
- `version: VER_2`, `opt_mode: MANUAL`, `ref: 1`
- `nry1-4_en: 1,1,1,1`, `nrc0_mode: 0`, `nrc_en: 1`, `gamma_en: 1`, `ca_en: 0`
- `tfs_mode: 1`, `sfs2_mode: 0`
- **mdy0**: `pretfs=8, premath=100, premathd=80, premabw=2, pretdz=32`
- **nrc0**: `trc=24, sfc=24, tfc=12, tfs=13`
- **nrc1**: `presfs=9, ncsfs1=119, sfs2c=15, sfs2c_f=15, sfs2f_b=15, sfs2f_f=15`
- **tfy**: `tfs=0,11,12; tss=16,0,0; tfr0=14,8,14,8,0,0; tfr1=16,8,16,8,0,0`
- **mdy**: `math=100,419`
- **sfy**: `sfs1=64, sbr1=128, sfs2=64, sft2=0, sbr2=128, sth=40/80/60`

---

## RTSP Streaming

### Library

Uses SDK's xop RTSP library (`libxoprtsp.a`) -- pre-built 32-bit ARM static
library from `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/lib/3rdparty/`. C++ internally,
exposed via 4-function C API (`rtsp_server_api.h`). Statically linked (no
additional .so deployment needed).

### Stream URL

`rtsp://<camera_ip>:554/live0` -- H.265/HEVC, 3200x1800 @ 15fps VBR

### Integration

`driver/rtsp/rtsp_push.c` wraps the xop API. In the VENC get_stream loop, each
`ot_venc_pack` is pushed as a NALU. The xop library handles RTP packetization
(FU fragmentation for large NALUs), SDP generation (VPS/SPS/PPS base64), RTSP
handshake (OPTIONS/DESCRIBE/SETUP/PLAY/TEARDOWN), and multi-client management.

### Hardware Watchdog

**Critical discovery:** `superb` feeds `/dev/watchdog` (fd 4) with a 30-second
timeout. When superb is killed, the hardware watchdog fires and hard-resets the
SoC after exactly 30 seconds. This was the root cause of the "~27s RTSP crash"
that plagued Phase 8 development.

**Fix:** `pipeline_test` now takes over watchdog duties:
- Opens `/dev/watchdog` with `O_RDWR` immediately after `sys_init()`
- Extends timeout to 120s via `WDIOC_SETTIMEOUT`
- Feeds via `WDIOC_KEEPALIVE` every VENC frame and during AE stabilization
- Disarms with magic close `write(fd, "V", 1)` on clean exit
- Crash handler also disarms to prevent reboot on segfault

The watchdog is NOT `nowayout` -- magic close works. But closing without 'V'
(e.g., `echo > /dev/watchdog` in a script) triggers immediate reboot.

### Launch Requirements

Must SIGSTOP mySystem to prevent superb respawn. Pipeline_test feeds the
watchdog internally. Shell connection dies when mySystem stops (tcpsvd is a
mySystem child), so launch must be fire-and-forget via `setsid`.

### Alternative RTSP implementations in research/

Found but not used (xop is simplest for our needs):

1. **shumjj-3516cv610_app/rtsp/** -- Full C++ RTSP server with `ceanic::rtsp`
   namespace, H.265 FU packetization, UDP+TCP transport, observer pattern.
   Uses both `ss_mpi_*` and `ot_mpi_*` prefixes.

2. **HIVIEW/mod/rtsps/** -- C-based, multi-process GSF framework with st-rtsp
   threads. Uses its own IPC messaging. Too complex for our single-stream need.

---

## ISP Kernel I2C Sync Path (ot_isp.ko -> ot_sensor_i2c.ko)

> **Status (2026-05-14): RESOLVED.** The kernel I2C sync path is working.
> Root cause was diff-based `update` flags in `cmos_get_sns_reg_info` going
> FALSE once AE converged, causing the kernel to silently skip all I2C
> writes. Fix: force `update = TD_TRUE` for AE-driven registers every frame.
> Direct I2C writes in `cmos_inttime_update` and `cmos_gains_update` are
> now disabled. See `research/PHASE9_ISP_I2C_SYNC.md` for the full
> investigation, Ghidra analysis, and all fixes applied.

### Architecture

The Hi3516CV610 ISP framework uses **two parallel I2C write paths**:

1. **Userspace direct I2C** (`/dev/i2c-0`): Used for sensor init sequence,
   standby/restart, mirror/flip, and debug writes. The sensor driver opens
   `/dev/i2c-0` in `pfn_cmos_sns_init` and uses raw `write()` calls.

2. **Kernel VBlank-synchronized I2C** (`ot_sensor_i2c.ko`): Used for per-frame
   exposure, gain, and VTS register updates. The ISP kernel thread calls
   `pfn_cmos_get_sns_reg_info` each frame to get the register descriptors,
   then writes them via `ot_sensor_i2c.ko` at the precise VBlank interrupt
   timing. This ensures exposure/gain changes take effect between frames
   without tearing.

Both paths coexist -- all reference drivers (SC4336P, GC8613, SC431HAI, etc.)
use both. The kernel path handles the critical frame-synchronized AE writes;
the userspace path handles one-time or infrequent writes.

### Kernel Module

`ot_sensor_i2c.ko` is loaded at boot from `appfs/home/ipc_drv/extdrv/`:
```
insmod extdrv/ot_sensor_i2c.ko
```
It loads after `ot_isp.ko` and before `ot_mipi_rx.ko`. No explicit API call
is needed to enable it -- the ISP framework uses it automatically when the
sensor driver provides valid `ot_isp_sns_regs_info` data.

### Critical Data Flow in cmos_get_sns_reg_info

The `pfn_cmos_get_sns_reg_info` callback has a strict data flow pattern that
must be followed exactly. All SDK reference drivers follow this pattern:

```
ISP calls pfn_cmos_get_sns_reg_info(vi_pipe, &sns_regs_info)
    |
    |-- if (sync_init == FALSE || sns_regs_info->config == FALSE):
    |       populate state->regs_info[0]  (full init, all update=TRUE)
    |       sync_init = TRUE
    |
    |-- else:
    |       diff state->regs_info[0] vs regs_info[1]  (delta update)
    |       set update flags on state->regs_info[0]
    |
    |-- memcpy(sns_regs_info <- state->regs_info[0])  // return to ISP
    |-- memcpy(state->regs_info[1] <- state->regs_info[0])  // save snapshot
    |-- fl[1] = fl[0]
```

**Critical**: The init and update paths must write to `state->regs_info[0]`,
NOT to `sns_regs_info` directly. The memcpy at the end copies `regs_info[0]`
to `sns_regs_info`, so any writes directly to `sns_regs_info` get clobbered.

### Bugs Found and Fixed (Session 2025-05-13)

Our original `cmos_get_sns_reg_info` had two structural bugs:

**Bug 1 - Init path target**: The init path wrote register addresses, data,
and metadata directly to `sns_regs_info` (the ISP-provided output pointer).
The memcpy at the end then overwrote `sns_regs_info` from `state->regs_info[0]`
(which was still zeroed), clobbering all the init data. Result: ISP received
zeroed register descriptors with no valid addresses or data.

**Bug 2 - Update flag target**: The update path set `.update` flags on
`sns_regs_info`, which was also clobbered by the same memcpy. Result: ISP
always saw stale update flags from the previous frame's snapshot.

**Bug 3 - Missing config check**: The SDK reference checks both `sync_init`
AND `sns_regs_info->config` to determine init vs update path. Our code only
checked `sync_init`, meaning the ISP couldn't request a full re-init.

**Fix**: Rewrote `cmos_get_sns_reg_info` to match the SDK pattern exactly:
- Init path writes to `state->regs_info[0]`
- Update path sets flags on `state->regs_info[0]`
- Checks `(sync_init == FALSE) || (config == FALSE)` for init trigger
- memcpy copies `regs_info[0]` to output, then snapshots to `regs_info[1]`

### dev_addr Format

The `dev_addr` field in `ot_isp_i2c_data` uses the **8-bit write address**
(7-bit address left-shifted by 1). All SmartSens sensors on this platform:
- SC635HAI: `dev_addr = 0x60` (7-bit = 0x30)
- SC4336P: `dev_addr = 0x60` (7-bit = 0x30)
- SC431HAI: `dev_addr = 0x60` (7-bit = 0x30)

The kernel `ot_sensor_i2c.ko` internally right-shifts to get the 7-bit
address for Linux I2C operations.

### Direct I2C Writes (REQUIRED -- DO NOT REMOVE)

Our `cmos_inttime_update` and `cmos_gains_update` contain **both** the standard
`regs_info[0]` data writes (for the kernel sync path) AND direct userspace I2C
writes via `sc635hai_write_register()`. The direct writes are **load-bearing** --
they are the actual mechanism delivering exposure/gain to the sensor. The
kernel sync path's writes do NOT reach the sensor in our setup (see below).

Reference sensor drivers (SC4336P, GC8613) do NOT have direct I2C writes in
their `cmos_inttime_update` / `cmos_gains_update` -- those drivers run with a
correctly-matched ISP kernel module + userspace library, so the kernel sync
path works. Our setup has a version mismatch (see PHASE3_CONTINUE.md "ISP I2C
Sync Path Investigation").

### What Was Fixed in cmos_get_sns_reg_info (2026-05-13)

Three structural bugs were fixed so the function now matches the SDK reference
pattern (SC4336P, SC431HAI, GC8613, HY006):

1. **Init path target**: Was writing register addresses/data to `sns_regs_info`
   (ISP output pointer). The memcpy at the end then overwrote those writes
   from `state->regs_info[0]` (zeroed). Now writes to `state->regs_info[0]`.

2. **Update flag target**: Was setting `.update` flags on `sns_regs_info`,
   also clobbered by the memcpy. Now sets flags on `state->regs_info[0]`.

3. **Missing config check**: SDK reference checks both `sync_init` AND
   `sns_regs_info->config` to decide init vs update. Now matches SDK.

**Verification of the fix (diagnostic prints temporarily added during testing):**
With the fix, `sns_regs_info` returned to the ISP framework contains correct
data: `type=I2C(0)`, `dev=0`, `reg_num=11`, `cfg2_valid_delay_max=2`,
`addr[exp_h]=0x3E00`, `addr[again_c]=0x3E08`, data values that track the AE
commands frame-by-frame, and update flags that correctly differentiate
which registers changed between frames.

### Earlier Test Suggested Kernel Path Doesn't Work (Needs Re-Validation)

Previous test (2026-05-13 morning) disabled direct I2C in `cmos_inttime_update`
only (leaving gains direct as control):

- ISP AE commanded `int_time=8` (sensor should be at minimum exposure)
- Sensor register readback: exp=1867 half-lines (stale value)
- Gain registers correct (because direct gain writes still active)

This was interpreted as proof that the kernel sync path doesn't deliver
exposure updates. **However**, subsequent Ghidra analysis shows the userspace
library and kernel module are compatible with superb's working flow, so the
test result is suspect -- the AE may not have been converged when the readback
was taken, or there was some other timing issue.

A controlled re-test with proper instrumentation is in progress.

### Testing Procedure for Kernel Sync Path Verification

```bash
# 1. With both paths active (current state), confirm system works:
python tools/cam_cmd.py "i2c_read 0 0x60 0x3E00 0x3E09 2 1"
python tools/cam_cmd.py "cat /proc/umap/isp | grep -A1 'sys_gain'"
# The ISP's "line:" value should match the encoded exposure from sensor regs:
# inttime = (0x3E00 << 12) | (0x3E01 << 4) | (0x3E02 >> 4)

# 2. To isolate the kernel path: comment out the sc635hai_write_register
# calls in cmos_inttime_update (keep gains direct writes as control).
# Rebuild: cd driver && make driver
# Deploy: python tools/send_file.py 192.168.1.153 8888 driver/build/libsns_sc635hai.so
# MD5 verify: python tools/cam_cmd.py "md5sum /progs/rec/00/ipc_drv/libsns_sc635hai.so"

# 3. Restart pipeline_test, wait 12-15s for AE convergence, then:
python tools/cam_cmd.py "i2c_read 0 0x60 0x3E00 0x3E02 2 1"
python tools/cam_cmd.py "cat /proc/umap/isp | grep -A1 'sys_gain'"

# 4. SUCCESS criterion: sensor exp register matches ISP "line:" value.
#    FAILURE criterion: sensor exp register stale while ISP line varies.

# 5. As a baseline reference, run the same checks with stock superb.
```
