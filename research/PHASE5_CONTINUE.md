# Phase 5 -- Color Pipeline Fix & AWB Calibration (Session: May 11-12, 2026)

## Summary

**Color pipeline fully working with manufacturer-calibrated AWB.** Three issues found and fixed:

1. **Stub AWB sensor driver** -- zero calibration data (CCM matrices, Planckian
   locus params, gain offsets). Initially populated from SC4336P reference,
   then replaced with SC635HAI-specific data extracted from superb's running ISP.
2. **PQ bin overrides bayer_format** -- the SC635HAI sensor is BGGR (standard
   for SmartSens sensors), but the PQ bin `day.bin` ISP calibration (type 0)
   resets `bayer_format` from BGGR(3) to RGGB(0). The ISP then treats BGGR
   Bayer data as RGGB, swapping R and B channels. Fixed by re-setting
   `bayer_format = BGGR` after every PQ bin load.
3. **SC4336P AWB approximation replaced with SC635HAI-specific calibration** --
   built `awb_dump` tool that queries superb's running ISP (read-only) and
   extracts all AWB params. Updated cmos driver with manufacturer's Planckian
   curve, CCM matrices, saturation table, WB gains, and ADVANCE AWB algorithm.

### Final result
- `capture_awb_cal.jpg` -- correct colors matching superb's stream, with
  manufacturer's AWB calibration. CT=5617K (superb reports 5524K for same
  scene). R=520/B=519 gains vs superb's R=523/B=538. Saturation=136 matches
  exactly. Side-by-side comparison shows near-identical color accuracy, with
  slightly better highlight preservation (forest curtain not washed out).
- Previous: `capture_bggr_final.jpg` -- correct colors with SC4336P-approximated
  AWB. CT=4566K (less accurate than the 5617K with proper calibration).
- Method: BGGR bayer_format + auto AWB (ADVANCE) + auto CCM + superb's calibration

### The bayer_format fix (2 lines of code)
After `load_pq_bin()`, read back `pub_attr`, set `bayer_format = BGGR`,
write it back. Must be done after EVERY PQ bin load (day/night/light/black
all override bayer_format). See `pipeline_test.c` Step 4a.

### Remaining work
- ISP I2C sync mechanism still broken (direct I2C writes work)
- Scene mode switching (day/night/light/black PQ bin swap) not implemented
- Low-light / different lighting comparison testing (next session)
- DRC tuning (our highlights are better preserved but shadows slightly darker
  than superb -- likely DRC aggressiveness difference)

---

## Root Cause Analysis

### Primary Issue: Missing AWB Calibration Data

The `cmos_get_awb_default()` function in `sc635hai_cmos.c` was a stub that
only provided initial WB gains (R=499, G=256, B=294) and a reference temp
(6500K). ALL of the following were missing (all zeros from memset):

| Missing Data | Purpose |
|---|---|
| `gain_offset[4]` | Static WB gain reference at calibration temp |
| `wb_para[6]` (p1,p2,q1,a,b,c) | Planckian locus curve fitting coefficients |
| `ccm` (CCM matrices at 4-7 color temps) | Color correction per color temperature |
| `agc_tbl` (16-entry saturation vs ISO) | Saturation rolloff at high ISO |
| `golden_rgain/bgain` | Per-unit golden sample correction |
| `init_ccm[9]` | Initial CCM matrix for boot |

Without these, the AWB algorithm had no framework to:
- Map R/B gain ratios to color temperature
- Select appropriate CCM matrices
- Apply proper WB corrections

### Secondary Issues Investigated & Ruled Out

| Suspect | Result |
|---|---|
| DRC crushing chrominance | No effect on color (Test 1 vs 8) |
| Dehaze/sharpen/LDCI/NR | No effect on color (Test 1 vs 8) |
| Gamma curve | Not a color issue (operates on luminance) |
| CSC saturation=0 | Was already 50 (not the bug) |
| CSC ext_csc_en / ct_mode_en | Minimal effect (PQ bin values OK) |
| Bayer pattern RGGB vs BGGR | No visible difference (Tests 1 vs 2) |
| NV21 vs NV12 (UV swap) | NV12 rejected by VI (only NV21 supported) |
| Manual vs auto WB | Manual superb gains wrong for our pipeline |

### Why Manual WB Gains From Superb Were Wrong

Superb's WB gains (R=0x155=341, B=0x398=920) were captured from a **running
superb ISP** with its complete color processing pipeline. When applied to our
ISP with identity CCM, the high B gain overdosed blue because:

1. Identity CCM doesn't compensate for Bayer's natural 2x green dominance
2. The gains are coupled to superb's specific CCM -- they only produce
   correct color when paired with superb's full color pipeline
3. The gains were for superb's specific lighting condition at capture time

With auto AWB, the algorithm converges to gains that match the actual scene
and the currently active CCM, producing balanced color.

---

## Fix Applied

### sc635hai_cmos.c Changes

#### Phase A: SC4336P Approximation (initial fix)

Added to `cmos_get_awb_default()` using SC4336P reference driver values:

1. **Planckian locus parameters**: `p1=36, p2=220, q1=0, a=218409, b=128, c=-167686`
2. **Static WB gain offset** at 5000K: `R=409, Gr=256, Gb=256, B=452`
3. **CCM matrices** at 6420K, 4949K, 3630K, 2525K + 3 identity fallbacks
4. **AGC saturation**: `{128,128,128,128,124,120,116,112,108,104,100,94,90,90,90,90}`
5. **Initial WB**: R=499, G=256, B=294, interval=1

Result: working color, CT reported ~4566K (reasonable but not precise).

#### Phase B: SC635HAI-Specific Calibration (from superb extraction)

Built `awb_dump` tool that queries superb's running ISP via read-only APIs
(`ss_mpi_isp_get_wb_attr`, `ss_mpi_isp_get_ccm_attr`, `ss_mpi_isp_get_awb_attr_ex`,
`ss_mpi_isp_query_wb_info`, `ss_mpi_isp_get_saturation_attr`, etc.).

Replaced all SC4336P values with manufacturer's calibration:

| Parameter | SC4336P (old) | Superb/SC635HAI (new) |
|---|---|---|
| Planckian p1,p2,q1 | 36, 220, 0 | -31, 287, 0 |
| Planckian a,b,c | 218409, 128, -167686 | 187899, 128, -137074 |
| Ref temp | 5000K | 4950K |
| Gain offset R/B | 409/452 | 477/535 |
| CCM temps | 6420/4949/3630/2525K | 6350/4950/3850/2640K |
| CCM matrices | SC4336P values | Superb's actual matrices |
| Saturation[0..2] | 128,128,128 | 140,132,128 |
| Saturation[6..8] | 116,112,108 | 110,105,100 |
| Init WB R/G/B | 499/256/294 | 523/256/538 |
| AWB run interval | 1 | 2 |
| AWB alg_type | (default LOWCOST) | ADVANCE (set in pipeline_test) |

Result: CT=5617K (superb=5524K), gains R=520/B=519 (superb=523/538),
saturation=136 (matches superb exactly). Side-by-side comparison shows
near-identical color accuracy.

#### Superb's Full AWB Configuration (reference dump)

```
AWB alg_type:       ADVANCE (1)
ref_color_temp:     4950
static_wb[]:        { 477, 256, 256, 535 }
curve_para[]:       { -31, 287, 0, 187899, 128, -137074 }
speed:              256
zone_sel:           32
high_color_temp:    10000
low_color_temp:     2500
shift_limit_en:     1, shift_limit=64
gain_norm_en:       1
ct_limit:           en=1, auto, high_rg=384, high_bg=128, low_rg=128, low_bg=464
cb_cr_track:        en=1 (16-entry tables per ISO)
luma_hist:          en=1, auto, thresh={0,4,16,128,235,255}, wt={32,128,384,512,256,32}
multi_light_src:    en=1, type=SAT, scaler=192
multi_ct_bin[8]:    { 2300, 2800, 3500, 4800, 5500, 6300, 7000, 8500 }
fine_tun:           en=1, strength=128
auto_sat[16]:       { 140,132,128,128,124,120,110,105,100,100,100,94,90,90,90,90 }
color_tone:         R=0x100, G=0x100, B=0x100 (neutral)
bayer_format:       RGGB(0) -- confirms PQ bin override (sensor is BGGR)
```

### pipeline_test.c Changes

1. **Re-enabled PQ bin loading** (was `#if 0` from previous session)
2. **Added `configure_isp_color()` function** that:
   - Logs ALL ISP module states for diagnostics
   - Sets AWB to auto mode with ADVANCE algorithm (matches superb)
   - Sets CCM to auto mode (was manual identity/superb)
   - Configures AWB speed=256, zone_sel=32, shift_limit=64, gain_norm
   - Keeps PQ bin settings for DRC/dehaze/sharpen/LDCI/NR/gamma
   - Keeps PQ bin CSC settings
   - Keeps PQ bin saturation settings
3. **Bayer set to BGGR with post-PQ-bin reset** (was RGGB matching superb's
   PQ bin default, but that was wrong for the sensor)
4. **Increased stabilization wait to 12s** (from 8s)

---

## Test Results Summary

| Test | Config | Result |
|------|--------|--------|
| 1 | PQ+disable all+identity CCM+superb WB | BLUE (B_gain=920 overdoses blue) |
| 2 | Same as 1 but RGGB Bayer | BLUE (identical, Bayer not the issue) |
| 3 | Unity WB (all 256) | GREEN (Bayer's natural 2x green) |
| 4 | Superb CCM + superb WB | DEEPER BLUE (CCM amplifies B channel) |
| 5 | Auto AWB + superb CCM | **REAL COLOR** with teal tint (breakthrough!) |
| 6 | Auto AWB + auto CCM | Similar to 5, good color, mild teal |
| 7 | Same + AWB cal data in cmos | Similar (cal data loaded, same AWB convergence) |
| 8 | All PQ modules re-enabled | Same color (modules don't affect color) |
| 9 | NV12 instead of NV21 | FAILED (VI rejects NV12) |
| 10 | Manual warm WB (R=325,B=600) | BLUE (high B + CCM overdose) |
| 11 | Auto AWB+CCM, satu=60 | Slightly more vivid, same teal |
| 12 | Auto AWB+CCM, PQ bin CSC | Same as 11 (ext_csc_en doesn't matter) |

**Best result**: Tests 8/12 -- auto AWB + auto CCM + all PQ modules on +
AWB calibration data in sensor driver.

---

## Current Pipeline State

### Configuration
```
Bayer:        BGGR (SmartSens native, re-set after PQ bin load)
Black level:  1024 (14-bit)
PQ bin:       loaded (ISP/AE/NR types 0/1/2), bayer_format re-set after
AWB:          AUTO ADVANCE (with SC635HAI calibration from superb)
CCM:          AUTO (4 matrices from superb: 6350/4950/3850/2640K)
AE:           AUTO (PQ bin calibration)
DRC:          enabled (PQ bin, manual mode)
Dehaze:       enabled (PQ bin)
Sharpen:      enabled (PQ bin auto)
LDCI:         enabled (PQ bin auto)
NR:           enabled (PQ bin auto)
Gamma:        user curve from PQ bin
CSC:          BT709, satu=50, ext_csc=1, ct_mode=1
Saturation:   auto (superb's AGC table: 140,132,128,...,90)
Color tone:   neutral (R=G=B=0x100)
```

### Convergence (indoor, daylight through window + warm light)
```
AE:  exp=29410us, again=1024, dgain=1024, isp_dgain=1024, iso=100
     ave_lum=55, hist_error=2, fps=20
AWB: R=520, G=256, B=519, ct=5617K, saturation=136
CCM: [497,32995,32782, 32871,521,32930, 2,33001,487]
```
Superb reference (same scene): R=523, G=256, B=538, ct=5524K, sat=136

### Previous convergence (SC4336P calibration, dim indoor, warm light)
```
AE:  exp=99822us, again=6298, dgain=1024, isp_dgain=1032, iso=619
     ave_lum=54, hist_error=2, fps=20
AWB: R=633, G=256, B=389, ct=12820K
```
Note: CT=12820K was wildly inaccurate (SC4336P Planckian params).
After switching to SC635HAI params, same-type scene reports ~5600K.

---

## Next Steps (Priority Order)

### 1. Low-Light / Different Lighting Comparison (HIGH)
Test pipeline under different lighting conditions and compare with superb.
Verify AWB tracks correctly under warm artificial light (should converge
to lower CT like 2500-3500K), dim conditions (gain ramp-up), and mixed
lighting. Side-by-side captures with superb's stream for comparison.

### 2. DRC Tuning (MEDIUM)
Current DRC uses PQ bin values in manual mode (op_type=1). Superb's stream
is brighter but washes out highlights (forest curtain). May want to adjust
DRC strength or switch to auto mode for better shadow/highlight balance.

### 3. ISP I2C Sync Fix (MEDIUM)
Direct I2C writes work but aren't frame-synchronized. Need to get the
ISP register sync mechanism working for production-quality AE/gain control.

### 4. Scene Mode Switching (MEDIUM)
Implement day/night/light/black PQ bin swapping. Each swap needs the
bayer_format BGGR re-set afterward. Consider extracting AWB dump under
each scene mode to see if superb uses different AWB params per scene.

### 5. Mirror/Flip UI Support (LOW)
When mirror/flip is toggled, must update both sensor reg 0x3221 AND
ISP bayer_format to match the new Bayer pattern.

### 6. Fix PQ Bin at Source (LOW)
Ideally regenerate PQ bins with correct BGGR setting using the PQ
tuning tool, eliminating the need for the post-load bayer_format reset.

---

## Files Modified This Session

| File | Changes |
|------|---------|
| `driver/src/sc635hai_cmos.c` | AWB calibration: CCM matrices, Planckian locus params, gain offsets, AGC saturation table. Initially from SC4336P, then updated with SC635HAI-specific data extracted from superb via `awb_dump`. |
| `driver/test/pipeline_test.c` | PQ bin loading + post-load bayer_format BGGR reset; configure_isp_color() with ISP module logging; auto AWB (ADVANCE) + auto CCM; AWB speed/zone/shift params from superb; 12s stabilization wait |
| `driver/test/awb_dump.c` | **New**: Read-only ISP query tool that dumps all AWB calibration data from superb's running ISP (pub_attr, wb_attr, wb_info, ccm_attr, awb_attr_ex, saturation, color_tone, stats_cfg). Requires LD_PRELOAD of ISP algorithm plugins. |
| `driver/Makefile` | Added `awb_dump` build target |
| `tools/pull_file.py` | New: pull files from camera (tries /dev/tcp, falls back to base64) |
| `research/PHASE5_CONTINUE.md` | This file |

---

## Key Technical Discoveries

### 1. SC635HAI Is BGGR (Not RGGB)
All SmartSens sensors on this platform (SC4336P, SC500AI, SC635HAI) use
BGGR Bayer pattern natively. This was confirmed by:
- SC4336P reference driver uses `OT_ISP_BAYER_BGGR`
- SC500AI PQ configs use `bayer_format = 3` (BGGR)
- shumjj third-party app README: forcing SC4336P from BGGR to RGGB
  "will cause color anomalies" (R/B swap) -- the exact symptom we had
- Our own RESEARCH.md and SC635HAI_SENSOR_ANALYSIS.md both noted BGGR

### 2. PQ Bin Overrides bayer_format to RGGB
**ROOT CAUSE of the R/B swap.** The PQ bin `day.bin` ISP calibration
(type 0) resets `pub_attr.bayer_format` from BGGR(3) to RGGB(0). This
was confirmed by reading back pub_attr before and after PQ bin load:
- Before PQ bin: `bayer_format = 3` (BGGR, as we set it)
- After PQ bin:  `bayer_format = 0` (RGGB, overridden by PQ bin)

The PQ bin was generated by the camera manufacturer's PQ tuning tool
with RGGB as the default template setting. All 4 scene bins (day.bin,
night.bin, light.bin, black.bin) will have this same override.

**Fix**: re-set `bayer_format = BGGR` after every PQ bin load.

### 3. bayer_format DOES Work (Not Ignored)
Earlier testing concluded bayer_format was "ignored by hardware" because
all 4 values produced identical output. This was wrong -- the PQ bin was
overriding all our test values back to RGGB before the ISP processed any
frames. Once we re-set bayer_format AFTER PQ bin load, it works correctly.

### 4. PQ Bin Loads ISP/AE/NR But NOT AWB Calibration
The PQ bin `day.bin` loads 3 data types (0=ISP, 1=AE, 2=NR) but does NOT
load AWB calibration curves or CCM matrices. The AWB data must come from
the sensor driver's `cmos_get_awb_default` callback.

### 5. Manual CCM Set Late Has No Visible Effect
Setting CCM via `ss_mpi_isp_set_ccm_attr` after VPSS/VENC are bound and
running returns success and readback confirms the value, but the output
does not change. CCM must be set before VPSS/VENC bind (in
`configure_isp_color()`). This was a red herring during debugging --
the "late set doesn't work" behavior is likely due to the ISP processing
pipeline already having buffered frames with the old CCM.

### 6. ISP Module Bypass Has No Effect on Color
Disabling DRC, dehaze, sharpen, LDCI, and NR had zero effect on the
monochrome/blue issue. These modules operate on contrast/detail/noise,
not on fundamental color balance.

### 7. Sensor Mirror/Flip (0x3221) Changes Bayer Pattern
SmartSens sensors physically change Bayer pattern with mirror/flip:
- `0x00` = normal BGGR
- `0x06` = mirror → GRBG
- `0x60` = vflip → GBRG
- `0x66` = both → RGGB
Superb runs with `0x3221 = 0x00` (normal). The ISP's bayer_format must
match the sensor's current mirror/flip state. A UI flip toggle would need
to update both the sensor register AND the ISP bayer_format.

### 8. PQ Bin Scene Modes
Superb swaps PQ bins based on scene: `day.bin`, `night.bin`, `light.bin`,
`black.bin`, plus AI ISP variants (`dayaiisp.bin`, `blackaiisp.bin`, etc).
Path pattern: `/home/sensor/<sensor>/pqbin/<scene>.bin`
Also checks `/etc/conf.d/pq_bin/` as override path.
All bins are 144,774 bytes with 3 data types (ISP, AE, NR).

### 9. ISP Read-Only Query APIs Work While Superb Is Running
All `ss_mpi_isp_get_*` and `ss_mpi_isp_query_*` APIs work correctly on
pipe 0 while superb owns the ISP. These are read-only and don't disturb
superb's pipeline. The `awb_dump` tool uses this to extract calibration.
Requires LD_PRELOAD of ISP algorithm plugins (libbnr, libdrc, libacs,
libcalcflicker, libir_auto, libldci, libdehaze, libextend_stats) because
`libot_mpi_isp.so` references symbols from them.

### 10. Superb Uses ADVANCE AWB Algorithm (Not LOWCOST)
Superb runs `alg_type = OT_ISP_AWB_ALG_ADVANCE` (1), not the default
LOWCOST (0). ADVANCE classifies statistics and re-filters white blocks
for better accuracy. It also enables multi-light-source detection (with
8 CT bins from 2300K-8500K), fine-tuning for skin color, CbCr tracking
by ISO, and luminance-weighted zone statistics.

### 11. SC635HAI WB Gains Are Higher Than SC4336P
SC635HAI needs ~1.86x R and ~2.09x B gain at 4950K (vs SC4336P's ~1.60x R
and ~1.77x B at 5000K). This indicates SC635HAI has different quantum
efficiency per color channel, likely a different color filter array or
microlens design. The Planckian curve shape is also quite different
(p1=-31 vs +36), meaning the R/B gain ratio vs. color temperature
relationship follows a different curve.

---

## Deploy and Run

```bash
# Build
cd /mnt/e/Projects/ipc_XMeye_camera/driver && make all

# Deploy
python tools/send_file.py 192.168.1.153 8888 \
    driver/build/pipeline_test \
    driver/build/libsns_sc635hai.so \
    driver/build/libbin.so \
    tools/diag_run.sh

# Run pipeline test
python tools/cam_cmd.py "/progs/rec/00/ipc_drv/diag_run.sh"

# Wait ~65s, then pull log and capture
python tools/cam_cmd.py "cat /progs/rec/00/ipc_drv/pipeline.log | tail -100"
python tools/pull_file.py /progs/rec/00/ipc_drv/capture.h265 capture_images/capture.h265
ffmpeg -y -i capture_images/capture.h265 -vf "select=gte(n\,150)" \
    -frames:v 1 -update 1 -q:v 2 capture_images/capture.jpg
```

### AWB Dump (query superb's running ISP -- read-only)

```bash
# Deploy
python tools/send_file.py 192.168.1.153 8888 driver/build/awb_dump

# Run while superb is streaming (no need to stop anything)
python tools/cam_cmd.py "cd /progs/rec/00/ipc_drv && \
    LD_LIBRARY_PATH=/progs/rec/00/ipc_drv \
    LD_PRELOAD='libbnr.so libdrc.so libacs.so libcalcflicker.so \
    libir_auto.so libldci.so libdehaze.so libextend_stats.so' \
    ./awb_dump 2>&1"
```

## Image Files

Older captures in workspace root, newer in `capture_images/`:

### Early tests (R/B swapped, before fix)
- `capture_test1.jpg` -- PQ+disable all+identity CCM+superb WB = BLUE
- `capture_test5_auto_awb.jpg` -- Auto AWB+superb CCM = first real color (teal tint)
- `capture_bggr_fix.jpg` -- BGGR Bayer + auto CCM = vivid but R/B SWAPPED

### R/B swap workaround tests
- `capture_rb_swap_ccm.jpg` -- Manual R/B swap identity CCM = correct but desaturated
- `capture_images/capture_early_rb_swap.jpg` -- Identity swap set early = correct but muted
- `capture_images/capture_auto_rb_swap2.jpg` -- Auto CCM swap set late = NO EFFECT (still swapped)

### BGGR fix (correct bayer_format)
- `capture_images/capture_bggr_no_pqbin.jpg` -- BGGR without PQ bin = very dark (AE broken)
- `capture_images/capture_bggr_pqbin_reset.jpg` -- BGGR + PQ bin + post-load reset = CORRECT
- `capture_images/capture_bggr_final.jpg` -- correct colors, full auto CCM (SC4336P cal), vibrant

### AWB calibration (SC635HAI-specific, from superb extraction)
- `capture_images/capture_awb_cal.jpg` -- **BEST: manufacturer's AWB calibration,
  CT=5617K, near-identical to superb's stream.** Side-by-side shows matching
  color accuracy with slightly better highlight preservation (forest curtain
  not washed out). Superb's stream is slightly brighter (DRC difference).

**Best image**: `capture_awb_cal.jpg` -- BGGR + PQ bin + auto CCM/AWB (ADVANCE) + superb's calibration
