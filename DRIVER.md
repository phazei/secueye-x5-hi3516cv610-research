# SC635HAI Sensor Driver

Canonical reference for the SC635HAI sensor driver on Hi3516CV610.
First open-source SC635HAI driver for any HiSilicon platform.

Built from four sources:

1. **V1.0.2.1 SDK headers** -- `ot_isp_sns_obj` / `ot_isp_sns_exp_func`
   callback structures
2. **Rockchip V4L2 SC635HAI driver** -- 7-range analog gain model,
   exposure encoding
3. **Ghidra decompilation of `superb`** -- 374-register init sequence,
   AWB calibration, AE route, I2C addressing
4. **HIVIEW SC500AI/SC450AI drivers** -- two-file architecture template

---

## Sensor overview

| Parameter | Value | Source |
|-----------|-------|--------|
| Chip ID | `0xCE7C` (reg `0x3107`=0xCE, `0x3108`=0x7C) | I2C |
| Resolution | 3208x1808 native, 3200x1800 cropped output | datasheet |
| Pixel | 2.0um BSI (SmartClarity-3), 1/2.45" format | flyer |
| MIPI | 2 lanes, 1080 Mbps, 27 MHz MCLK, RAW10 | firmware |
| Bayer pattern | **BGGR** (OT_ISP_BAYER_BGGR = 3) | confirmed |
| I2C | Bus 0, 7-bit addr `0x30`, 8-bit write addr `0x60` | I2C |
| Frame rate | 20 fps (VTS=2812); superb encodes at 15 fps via VENC | confirmed |
| WDR mode | Linear only; superb's "WDR" is ISP DRC tone-mapping | Ghidra |
| Black level | 1024 (14-bit) = 64 (10-bit sensor); superb uses 1030 | extracted |
| Dynamic range | 83 dB normal, 100 dB HDR (unused) | flyer |
| Max analog gain | 83.79x (7 ranges) | Rockchip |
| Max total gain | ~1330x (83.79x analog x 15.875x digital) | calculated |

### Mirror/flip (register 0x3221)

| Value | Orientation | Effective Bayer |
|-------|-------------|-----------------|
| `0x00` | Normal | BGGR |
| `0x06` | Mirror | GRBG |
| `0x60` | Vertical flip | GBRG |
| `0x66` | Both | RGGB |

Superb runs `0x3221 = 0x00` (normal BGGR).

### Frame geometry registers

| Register | Value | Meaning |
|----------|-------|---------|
| `0x3200-01` | `0x0000` | Row start = 0 |
| `0x3202-03` | `0x0000` | Column start = 0 |
| `0x3204-05` | `0x0C87` | Row end = 3207 |
| `0x3206-07` | `0x070F` | Column end = 1807 |
| `0x3208-09` | `0x0C80` | Output width = 3200 |
| `0x320A-0B` | `0x0708` | Output height = 1800 |
| `0x320C-0D` | `0x0780` | HTS = 1920 |
| `0x320E-0F` | `0x0AFC` | VTS = 2812 |

---

## Driver architecture

### File structure

```
driver/
  src/
    sc635hai_cmos.c          ISP/AE/AWB callbacks, ot_isp_sns_obj export
    sc635hai_sensor_ctl.c    I2C init sequence, register read/write
    sc635hai_cmos.h          Constants, gain tables, register addresses
    hi_compat.h              hi_* -> ss_mpi_* API compatibility macros
  rtsp/
    rtsp_push.h, rtsp_push.c RTSP server (wraps SDK libxoprtsp.a)
  test/
    pipeline_test.c          Full pipeline binary (future daemon)
    reg_dump.c               I2C register dump diagnostic
    sensor_test.c            Standalone I2C sensor test
    awb_dump.c               ISP AWB calibration reader
  Makefile                   Cross-compilation build
```

### Two structs, two roles

**`ot_isp_sns_obj`** (12 function pointers, `ot_sns_ctrl.h`) -- the
top-level sensor object exported as `g_sns_sc635hai_obj` via `dlsym`.
Called by the ISP framework for lifecycle management:

| # | Field | Purpose |
|---|-------|---------|
| 0 | `pfn_register_callback` | Register ISP+AE+AWB callbacks |
| 1 | `pfn_un_register_callback` | Unregister callbacks |
| 2 | `pfn_set_bus_info` | Set I2C bus number |
| 3 | `pfn_set_bus_ex_info` | Set SerDes/extended bus info |
| 4 | `pfn_standby` | Put sensor in standby |
| 5 | `pfn_restart` | Restart sensor from standby |
| 6 | `pfn_mirror_flip` | Set mirror/flip mode |
| 7 | `pfn_set_blc_clamp` | BLC clamp enable/disable |
| 8 | `pfn_write_reg` | Direct I2C register write |
| 9 | `pfn_read_reg` | Direct I2C register read |
| 10 | `pfn_set_init` | Set initial exposure/gain attrs |
| 11 | `pfn_set_fast_ae` | Fast AE attribute (V1.0.2.1, set to NULL) |

**`ot_isp_sns_exp_func`** (12 function pointers, `ot_common_sns.h`) --
the ISP callback struct registered via `ss_mpi_isp_sensor_reg_callback`.
Called per-frame by the ISP thread:

| # | Field | Purpose |
|---|-------|---------|
| 0 | `pfn_cmos_sns_init` | Sensor hardware init (I2C sequence) |
| 1 | `pfn_cmos_sns_exit` | Sensor cleanup |
| 2 | `pfn_cmos_sns_global_init` | Global init (unused) |
| 3 | `pfn_cmos_set_image_mode` | Set resolution/fps mode |
| 4 | `pfn_cmos_set_wdr_mode` | Set WDR mode |
| 5 | `pfn_cmos_get_isp_default` | Return ISP defaults + PQ bin path |
| 6 | `pfn_cmos_get_isp_black_level` | Return black level |
| 7 | `pfn_cmos_get_blc_clamp_info` | BLC clamp info |
| 8 | `pfn_cmos_get_sns_reg_info` | **Per-frame register sync** (critical) |
| 9 | `pfn_cmos_set_pixel_detect` | Dead pixel test mode |
| 10 | `pfn_cmos_get_awb_gains` | Get sensor-side AWB gains |
| 11 | `pfn_cmos_get_standby_cfg` | Standby register config |

### Registration flow

```
sensor_register_callback()
  -> sensor_ctx_init()          -- allocate per-pipe state
  -> cmos_init_sensor_exp_function()
     -> ss_mpi_isp_sensor_reg_callback()   -- register 12 ISP callbacks
  -> cmos_init_ae_exp_function()
     -> ss_mpi_ae_sensor_reg_callback()    -- register AE callbacks
  -> cmos_init_awb_exp_function()
     -> ss_mpi_awb_sensor_reg_callback()   -- register AWB callback
```

---

## Register map

### ISP sync register order

11 registers written per frame via the kernel I2C sync path.
Defined in `enum sc635hai_linear_regs_e` (`sc635hai_cmos.h`):

| Index | Register | Value | Purpose |
|-------|----------|-------|---------|
| 0 | `0x3812` | `0x00` | Group hold start |
| 1 | `0x3E00` | exp[15:12] | Exposure high |
| 2 | `0x3E01` | exp[11:4] | Exposure mid |
| 3 | `0x3E02` | exp[3:0]<<4 | Exposure low |
| 4 | `0x3E08` | coarse sel | Analog gain coarse |
| 5 | `0x3E09` | fine 0x20-0x3F | Analog gain fine |
| 6 | `0x3E06` | therm code | Digital gain coarse |
| 7 | `0x3E07` | fine 0x80-0xFF | Digital gain fine |
| 8 | `0x320E` | VTS high | Frame length high |
| 9 | `0x320F` | VTS low | Frame length low |
| 10 | `0x3812` | `0x30` | Group hold release |

### Exposure encoding

Registers `0x3E00`/`0x3E01`/`0x3E02` encode exposure in half-line units:

```c
reg_3E00 = (inttime >> 12) & 0x0F;
reg_3E01 = (inttime >> 4)  & 0xFF;
reg_3E02 = (inttime & 0x0F) << 4;

// Decode:
inttime = (reg_3E00 << 12) | (reg_3E01 << 4) | (reg_3E02 >> 4);
```

- Minimum: 2 half-lines (`SC635HAI_EXP_MIN`)
- Maximum: VTS - 10 = 2802 at VTS=2812 (`SC635HAI_EXP_OFFSET = 10`)
- Half-line time: 1e9 / (VTS x FPS) = 17,780 ns at 20 fps
- AE treats `int_time` as whole lines; register holds half-line units

### Analog gain model

7 coarse ranges with 32 fine steps each (0x20..0x3F), giving 224 entries.
Base unit: 1024 = 1.0x gain.

| Range | `0x3E08` | Multiplier | Gain span (1024=1x) |
|-------|----------|------------|---------------------|
| 0 | `0x00` | 1.00x | 1024 -- 2016 |
| 1 | `0x01` | 2.00x | 2048 -- 2720 (capped) |
| 2 | `0x80` | 2.66x | 2723 -- 5362 |
| 3 | `0x81` | 5.32x | 5448 -- 10718 |
| 4 | `0x83` | 10.64x | 10894 -- 21434 |
| 5 | `0x87` | 21.28x | 21790 -- 42932 |
| 6 | `0x8f` | 42.56x | 43582 -- 85804 |

Full 224-entry lookup table in `g_again_table[]` (`sc635hai_cmos.h`).
Source: Rockchip V4L2 SC635HAI driver.

### Digital gain model

Two registers: coarse (`0x3E06`, thermometer code) and fine
(`0x3E07`, 0x80..0xFF = 1.0x..1.992x).

| `0x3E06` | Multiplier |
|----------|------------|
| `0x00` | 1x |
| `0x01` | 2x |
| `0x03` | 4x |
| `0x07` | 8x |
| `0x0F` | 16x |

Max digital gain: 16 x (255/128) x 1024 = 32640 (~15.875x).

### DPC noise threshold (register 0x5799)

- Total gain >= ~30x (30720): write `0x5799 = 0x07`
- Total gain <= ~20x (20480): write `0x5799 = 0x00`
- Hysteresis prevents toggling at boundary.

### Other key registers

| Register | Purpose | Notes |
|----------|---------|-------|
| `0x0100` | Stream control | 0x01=streaming, 0x00=standby |
| `0x0103` | Software reset | |
| `0x3000` | Standby (SmartSens) | Bit 0 |
| `0x3221` | Mirror/flip | See table above |
| `0x3812` | Group hold | 0x00=hold, 0x30=release |
| `0x36E9` | PLL bypass | Used during init sequence |
| `0x326D` | VTS bit 16 | For slow shutter (VTS_MAX = 0x1FFFF) |

---

## ISP I2C sync path

This section documents the kernel-mediated sensor register update
mechanism. This was the hardest problem in the driver -- three
iterations of debugging to get working. See `DRIVER_INTERNALS.md`
for the full kernel decompilation details.

### Architecture

```
Per-frame flow:
  ISP thread (userspace)
    -> cmos_inttime_update()     writes exp to state->regs_info[0]
    -> cmos_gains_update()       writes gain to state->regs_info[0]
    -> cmos_get_sns_reg_info()   sets update flags, returns regs_info
    -> ISP framework ioctl       queues sync_cfg to kernel ring buffer
  ISP interrupt (kernel)
    -> isp_drv_reg_config_sensor()
    -> isp_drv_write_i2c_data()  reads ring buffer, checks update flags
    -> ot_sensor_i2c_write()     dispatches I2C via bsp_i2c_master_send_mul_reg
```

### The three structural bugs in `cmos_get_sns_reg_info`

All three were init-path bugs where writes went to the wrong target:

1. **Register address/data target:** Was writing to `sns_regs_info`
   (the ISP output pointer), but `memcpy(sns_regs_info,
   &state->regs_info[0], ...)` at the end overwrites from
   `regs_info[0]` (which was zeroed). **Fix:** write to
   `state->regs_info[0]` directly.

2. **Update flag target:** Was setting `.update` flags on
   `sns_regs_info`, also clobbered by the memcpy. **Fix:** set flags
   on `state->regs_info[0]`.

3. **Missing config check:** SDK reference drivers check both
   `sync_init == FALSE` AND `sns_regs_info->config == FALSE` to
   decide init vs update. We only checked `sync_init`. **Fix:** match
   SDK pattern: `if (!state->sync_init || !sns_regs_info->config)`.

These match the SC4336P, SC431HAI, GC8613, and HY006 reference
drivers from the shumjj repo.

### The force-TRUE fix

**Root cause:** The kernel's `isp_drv_write_i2c_data` checks each
register's `.update` flag before writing. In diff-based mode
(sc4336p style), `update` is set TRUE only when
`regs_info[0].data != regs_info[1].data`. Once AE converges to
stable values, all AE-driven registers have identical data in both
buffers, so all flags go FALSE and the kernel skips all I2C writes.

**Symptom:** Poke test fails -- manually writing to exposure
registers via I2C is never restored by the kernel. Stream looks
fine because AE has already converged to correct values.

**Fix** (`sc635hai_cmos.c:322-364`): Force `update = TD_TRUE` every
frame for exposure, gain, and group hold registers. Leave VTS as
diff-based (rarely changes). Cost: ~50us/frame of I2C bandwidth
(~0.1% of frame budget at 20 fps).

**2026-05-15 validation:** Isolated diff-only test (Outcome A)
confirmed force-TRUE is steady-state safety only, not load-bearing
for transient AE response. The Iteration 2 sync-queue fixes (below)
handle transients correctly. Force-TRUE guards against: manual AE
lock, perfectly stable scenes, external register pokes.

### Iteration 2 sync-queue fixes

These fixes enabled the kernel sync path to work for transient AE
changes (lighting/color-temperature shifts). All are in
`pipeline_test.c`:

1. **`ss_mpi_isp_set_ctrl_param`** (before `mem_init`):
   `be_buf_num = 4`, `quick_start_en = 1`. Note: `wakeup = BE_END`
   is rejected by `isp_init` in online mode (error `0xa01c800c`
   NOT_SUPPORT).

2. **`ss_mpi_isp_set_ae_route_attr`** (after `isp_init`): 3 nodes
   matching superb:
   - Node 0: int_time=8, sys_gain=1024
   - Node 1: int_time=2802, sys_gain=1024
   - Node 2: int_time=2802, sys_gain=196608

   (2802 = VTS 2812 - EXP_OFFSET 10 = our max_int_time. Superb uses
   2804 because it runs VTS=2814; 2804 would exceed our AE limit and
   get clamped anyway.)

3. **`bin_param.stIspEvo.enable = 1`** for PQ bin import (includes
   cross-frame and AE route modules).

### AE callback requirements

Three AE callbacks that must be registered for the sync path to
function:

- `pfn_cmos_ae_quick_start_status_set` -- resets `sync_init = FALSE`
  to re-trigger `regs_info` initialization
- `pfn_cmos_ae_fast_ae_attr_get` -- returns `sns_delay_frame = 3`
- `pfn_cmos_ae_fast_ae_attr_set` -- stub (no-op)

### `cmos_get_ae_default` requirements

| Field | Value | Notes |
|-------|-------|-------|
| `hmax_times` | 17780 ns | `1e9 / (VTS * FPS)` at VTS=2812, 20fps |
| `max_int_time` | 2802 | `VTS - SC635HAI_EXP_OFFSET` |
| `min_int_time` | 2 | `SC635HAI_EXP_MIN` |
| `max_again` | 85804 | Last table entry (~83.79x) |
| `min_again` | 1024 | 1.0x |
| `max_dgain` | 32640 | ~15.875x |
| `min_dgain` | 1024 | 1.0x |
| `lines_per500ms` | 28120 | `VTS * FPS / 2` (must use 20, not 30) |
| `init_exposure` | 76151 | Reasonable indoor starting point |
| `flicker_freq` | 12800 | 50 Hz x 256 (PAL) |

### Poke test procedure

Canonical verification that the kernel I2C sync path is working:

1. Pipeline running, AE converged (stable exposure).
2. Read `0x3E00-02` via `i2c_read` -- should match ISP `line:` value.
3. Poke marker: `i2c_write 0 0x60 0x3E00 0xAA 2 1` (etc.)
4. Read back immediately -- poke value present.
5. Read back 3 seconds later -- kernel should have restored AE values.

If the poke value persists, the sync path is broken.

---

## AE/AWB calibration

### AWB calibration data

Extracted from superb's running ISP via the `awb_dump` tool. These
are manufacturer-tuned values specific to the SC635HAI's color filter
array:

**Static WB gains at reference temperature (4950K D50):**

| Channel | Gain | Multiplier |
|---------|------|------------|
| R | 477 | ~1.86x |
| Gr | 256 | 1.0x |
| Gb | 256 | 1.0x |
| B | 535 | ~2.09x |

**Planckian locus curve fit:**

| Parameter | Value |
|-----------|-------|
| p1 | -31 |
| p2 | 287 |
| q1 | 0 |
| a | 187899 |
| b | 128 |
| c | -137074 |

**Color Correction Matrices (4 temperatures):**

| Temp | Description | Matrix (row-major, hex) |
|------|-------------|------------------------|
| 6350K | D65 daylight | `01D3 80C3 8010 / 8057 01F1 809A / 8002 80AF 01B1` |
| 4950K | CWF / D50 | `01D8 80C5 8013 / 8067 0206 809F / 000A 80ED 01E3` |
| 3850K | TL84 warm | `01E3 80EF 000C / 8066 016C 8006 / 000D 8130 0223` |
| 2640K | Incandescent | `01D6 80E7 0011 / 8073 0179 8006 / 0014 8214 0300` |

**Saturation rolloff by ISO** (16 entries, 1x..32768x):
`{140, 132, 128, 128, 124, 120, 110, 105, 100, 100, 100, 94, 90, 90, 90, 90}`

**Initial WB gains:** R=523, G=256, B=538. AWB run interval: 2.

### PQ bin behavior

Path: `/home/sensor/sc635hai/pqbin/{day,night,light,black}.bin`
(each 144,774 bytes, on resfs partition; `/home/sensor/` is a bind-mount
of `/tmp/resfs/sensor/` -- same files, same inodes).

- Loads 3 data types: 0=ISP, 1=AE, 2=NR. Does **NOT** load AWB/CCM.
- **All PQ bins override `pub_attr.bayer_format` from BGGR to RGGB.**
  Must re-set `bayer_format = OT_ISP_BAYER_BGGR` after every PQ bin
  load. This was a multi-session debugging mystery -- the "bayer_format
  is ignored by hardware" belief was wrong; PQ bin was silently
  resetting it.
- PQ bin DRC defaults: strength=160, asymmetry=10, second_pole=200,
  stretch=60, compress=200.

---

## Noise reduction pipeline

Hi3516CV610 has a 4-stage NR architecture. There is no standalone
chroma NR API -- chroma NR is handled by DRC BCNR and 3DNR nrc0/nrc1.

### Stage 1: ISP Bayer NR

`ss_mpi_isp_set_nr_attr()` with `ot_isp_nr_attr`. Spatial Bayer-domain
denoising. Superb values: fine_strength=80, coring_wgt=50.

### Stage 2: DRC BCNR (Hi3516CV610-specific)

`ot_isp_drc_bcnr_attr` inside `ot_isp_drc_attr`. Bayer-domain chroma
NR within the DRC module. Range [0,8] for strength. PQ bin default:
enable=0, strength=3. Superb: `dark_gain_limit_chroma = 0` (no chroma
amplification in shadows).

### Stage 3: 3DNR V2 at VI pipe level

**Critical:** VPSS 3DNR APIs (`ss_mpi_vpss_*_grp_3dnr_*`) return
`0xA007800C` (NOT_PERM) on Hi3516CV610 in VI_ONLINE_VPSS_OFFLINE mode.
Must use **VI pipe APIs**:
- `ss_mpi_vi_set_pipe_3dnr_attr` / `ss_mpi_vi_get_pipe_3dnr_attr`
- `ss_mpi_vi_set_pipe_3dnr_param` / `ss_mpi_vi_get_pipe_3dnr_param`

**NR V2 only.** Setting V1 parameters returns `0xA0108007`
(ILLEGAL_PARAM).

**Must read-modify-write.** Zeroing the param struct and setting only
known fields causes errors/artifacts.

**Timing:** After ISP init, before VPSS bind.

V2 structural differences from V1: `ot_nr_v2_pshrp` replaces
`ot_nr_v1_iey`; `ot_nr_v2_sfy` has 3 levels (not 5);
`ot_nr_v2_tfy` has `tfs0/tfs1/tfs2` 4-bit bitfields;
`ot_nr_v2_nrc0` has `tfs_mot[17]`; `ot_nr_v2_nrc1` has
`sfs1_mot[17]`, `sfs2_mot[16]`, `sfs2_sat[20]`.

**Superb's 3DNR V2 parameters** (from `/proc/umap/vi`):

```
mdy0:  tfs=8, math=100, mathd=80, mabw=2, tdz=32
tfy:   tfs=0,11,12  tss=16,0,0  tfr0=14,8,14,8,0,0
nrc0:  trc=24, sfc=24, tfc=12, tfs=13
nrc1:  pre_sfs=9, sfs1=119
tfs_mode=1, sfs2_mode=0, gamma_en=1, ca_en=0
```

**Current tuning** (boosted above superb for better low-light):

```
nrc0:  trc=128, sfc=128, tfc=32, tfs=13
nrc1:  pre_sfs=14, sfs1=220, sfs2_coarse=24, sfs2_coarse_f=24
tfy:   tfs=4,11,12  tss=16,0,0  (tfs0=4 enables coarse temporal)
```

### Stage 4: No standalone CNR

No `ss_mpi_isp_set_cnr_attr()` on this SDK version. Chroma NR is
handled entirely by DRC BCNR (stage 2) and 3DNR nrc0/nrc1 (stage 3).

---

## Hardware watchdog

| Property | Value |
|----------|-------|
| Device | `/dev/watchdog` (ot_wdt driver) |
| Default timeout | 30 seconds |
| Feed mechanism | **`WDIOC_SETTIMEOUT` only** |
| KEEPALIVE | Returns EPERM (does NOT work) |
| write() | Returns EPERM (does NOT work) |
| Disarm | `write(fd, "V", 1)` (magic close character) |
| nowayout | No -- magic close works |

The ot_wdt driver is unusual: the only way to reset the countdown is
to call `WDIOC_SETTIMEOUT`. `pipeline_test` "feeds" by re-setting the
timeout to 120 seconds every frame. On exit or crash, it writes `'V'`
to cleanly disarm so mySystem can take the watchdog back.

**Critical timing:** Must open `/dev/watchdog` immediately after
killing superb. The SoC hard-resets ~30 seconds after superb stops
feeding. The signal handler also disarms the watchdog before re-raise.

---

## Build and deploy

### Building

Requires WSL (toolchain is Linux x86_64 ELF):

```bash
cd /mnt/e/Projects/ipc_XMeye_camera/driver
make all        # builds everything
make driver     # just libsns_sc635hai.so
make pipeline   # just pipeline_test (does NOT rebuild driver lib!)
```

Outputs in `driver/build/`:
- `libsns_sc635hai.so` (~14KB) -- sensor driver shared library
- `pipeline_test` (~421KB with RTSP) -- full pipeline binary
- `reg_dump` -- I2C register dump tool
- `sensor_test` -- standalone sensor test
- `awb_dump` -- ISP AWB calibration reader

Toolchain: `research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/`

### Deploying

Primary method (fast TCP):

```bash
# Start recv daemon on camera (one-time per boot)
python tools/cam_cmd.py "pidof recv || (nohup /progs/rec/00/ipc_drv/recv 8888 /progs/rec/00/ipc_drv -d > /dev/null 2>&1 &)"

# Send files
python tools/send_file.py 192.168.1.153 8888 driver/build/libsns_sc635hai.so driver/build/pipeline_test

# Set permissions
python tools/cam_cmd.py "chmod +x /progs/rec/00/ipc_drv/pipeline_test"
```

Fallback (base64 over shell): `python tools/deploy_file.py <local> <remote>`

### LD_PRELOAD ordering

Musl libc resolves `LD_PRELOAD` eagerly. ISP library dependencies
must appear **before** `libot_mpi_isp.so`; algorithm plugins **after**:

```
LD_PRELOAD='
  libsecurec.so       # 1. Security C library
  libot_osal.so       # 2. OS abstraction
  libss_mpi_sysmem.so # 3. System memory
  libss_mpi.so        # 4. MPI base
  libot_mpi_isp.so    # 5. ISP (needs 1-4)
  libbnr.so           # 6+ Algorithm plugins
  libdrc.so
  libacs.so
  libcalcflicker.so
  libir_auto.so
  libldci.so
  libdehaze.so
  libextend_stats.so
'
```

### Build pitfalls

1. `make pipeline` only rebuilds `pipeline_test`, NOT
   `libsns_sc635hai.so`. After changing `cmos.c`/`cmos.h`/
   `sensor_ctl.c`, run `make driver` (or `make all`).
2. MD5-verify deployed files after every transfer.
3. Check `ps | grep pipeline` before launching to avoid duplicates.
4. `cam_cmd.py` has a ~15-second hard deadline. For long-running
   commands, use: `setsid script.sh </dev/null >/dev/null 2>&1 &`
5. Watchdog reboots SoC ~30s after killing `pipeline_test` if
   `mySystem` is stopped. Always kill `pipeline_test` before stopping
   `mySystem`, or use `rtsp_run.sh` which handles the dance.

---

## SDK compatibility

| Component | Version | Notes |
|-----------|---------|-------|
| Userspace libs | V1.0.2.1 (B020) | `ss_mpi_*` APIs |
| Kernel modules | V1.0.2.0 (B051) | ISP ioctl type `0x70` |
| `ot_isp.ko` version string | "V1.0.0.0 B010" | **Misleading** -- forgotten `#define`, code is V1.0.2.x |
| Toolchain | GCC 10.3.0, musl 1.2.3 | SDK release CS71.2.10.5.B002 |

**V1.0.2.1 `libot_mpi_isp.so` is functionally identical to superb's
V1.0.2.0 ISP code.** All 9 functions in the sensor sync path are
identical (only `fprintf` line numbers differ by 3). Verified via
Ghidra side-by-side decompilation.

**HIVIEW uses V1.0.1.0** (ISP ioctl type `0x49`) -- ABI incompatible.
Do not adopt HIVIEW libraries wholesale. Cherry-pick source code only.

**shumjj's `ot_isp.ko` has wrong `struct module` size** (0x1C0 vs
our kernel's 0x180) and a broken `depends=` field. Cannot be loaded
on our camera.

---

## RTSP streaming

Uses SDK pre-built `libxoprtsp.a` (xop RTSP library V5.5), wrapped
in `driver/rtsp/rtsp_push.{h,c}`.

| Property | Value |
|----------|-------|
| URL | `rtsp://<ip>:554/live0` |
| Codec | H.265 (HEVC), RTP payload type 96 |
| Resolution | 3840x2160 @ 15 fps (upscaled from 3200x1800 via VPSS ext chn 3, matching superb) |
| Binary size impact | ~40 KB (xop + C++ runtime, statically linked) |

Note: the sensor is 3200x1800 native; the 4K output is a VPSS upscale.
Superb does the same on this hardware class (see DRIVER_INTERNALS.md,
"Superb resolution selection"). Historical `pipeline_test` encoded
3200x1800 native.

4-function C API: `rtsp_server_start`, `rtsp_session_create`,
`rtsp_session_push_frame`, `rtsp_server_stop`.

Launch via `rtsp_run.sh` (handles mySystem SIGSTOP, superb kill,
watchdog, LD_PRELOAD) or manually:

```bash
killall -STOP mySystem; killall -9 superb; sleep 1
cd /progs/rec/00/ipc_drv && \
  LD_PRELOAD='...' LD_LIBRARY_PATH=/progs/rec/00/ipc_drv \
  ./pipeline_test --rtsp
```

---

## Pipeline state (current production config)

```
Bayer:       BGGR (re-set after PQ bin load)
Black level: 1024 (14-bit)
PQ bin:      loaded (types 0/1/2: ISP, AE, NR)
AWB:         AUTO ADVANCE (SC635HAI calibration)
CCM:         AUTO (4 matrices at 6350/4950/3850/2640K)
AE:          AUTO (3-node route: {8,1024}, {2802,1024}, {2802,196608})
DRC:         enabled (PQ bin defaults, manual mode)
Saturation:  auto (superb's AGC rolloff table)
CSC:         BT709, satu=50, ext_csc=1, ct_mode=1
Sensor FPS:  20 (VENC outputs 15)
Encode:      3840x2160 H.265 VBR 4096kbps, QP 35-44 (superb-matched)
3DNR:        V2 at VI pipe level (boosted above superb)
ISP ctrl:    be_buf_num=4, quick_start_en=1
```

---

## Provenance and references

- `driver/src/` -- production driver source
- `driver/test/pipeline_test.c` -- full pipeline (future daemon)
- `research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/` -- authoritative SDK
- `research/shumjj-3516cv610_app/device/sensor/` -- reference drivers
  (SC4336P, SC431HAI, GC8613, HY006)
- `DRIVER_INTERNALS.md` -- kernel decompilation forensics
- `research/archive/PHASE9_ISP_I2C_SYNC.md` -- I2C sync investigation
  journal (the journey)
- `research/archive/SC635HAI_SENSOR_ANALYSIS.md` -- original sensor
  analysis (partially superseded by this doc)
