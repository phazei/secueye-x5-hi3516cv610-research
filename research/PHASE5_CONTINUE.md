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
- ISP I2C sync mechanism FIXED -- see PHASE9_ISP_I2C_SYNC.md
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

### 1. Low-Light Noise Investigation (HIGH) -- IN PROGRESS
Our pipeline has more chroma noise/blotching than superb's main stream in
low light. Ruled out so far:
- **PQ bin**: night.bin (grayscale, slow shutter), light.bin (slow shutter,
  similar noise). day.bin is the only working bin.
- **VENC mode**: VBR (QP 35-44, matching superb config) vs CBR -- no difference
- **Bayer NR settings**: auto mode, fine_str=80, coring_wgt=50 -- matches superb
- **AI ISP**: ot_aiisp kernel module loaded but not active; no dayaiisp.bin
  for SC635HAI exists anywhere
- **Bitrate**: superb uses 4096kbps same as us

Still to investigate:
- Superb's full /proc/umap/venc config (actual encoding params, not just cfg file)
- VPSS NR (separate from ISP Bayer NR -- VPSS can do spatial/temporal NR)
- Superb's framerate (config says 15fps, we run 20fps -- slower fps = more
  light per frame = less gain = less noise)
- Motion detection NR (md_en=1 in both, but md_cfg params may differ)
- Whether superb does NR in auto mode with different high-ISO table entries
  (we only checked [0], need to check [8..15] for high ISO)
- DRC `dark_gain_limit_chroma` -- limits chroma amplification in shadows

### 2. DRC Tuning (MEDIUM)
Current DRC uses PQ bin values in manual mode (op_type=1). Superb's stream
is brighter but washes out highlights (forest curtain). May want to adjust
DRC strength or switch to auto mode for better shadow/highlight balance.

### 3. ISP I2C Sync Fix (RESOLVED)
Kernel I2C sync path is working. See PHASE9_ISP_I2C_SYNC.md for details.
Direct I2C writes removed; sensor registers delivered by kernel callback.

### 4. Scene Mode Switching (MEDIUM)
Implement day/night/light/black PQ bin swapping. Each swap needs the
bayer_format BGGR re-set afterward. night.bin and light.bin both use slow
shutter mode (fl=5624, ~10fps) which our VENC can't handle -- needs frame
rate adjustment.

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
| `driver/test/pipeline_test.c` | PQ bin path configurable via argv[1]; post-load bayer_format BGGR reset; configure_isp_color() with ISP module logging + NR detail logging; auto AWB (ADVANCE) + auto CCM; VBR encoding (4Mbps max, QP 35-44 matching superb); 12s stabilization wait |
| `driver/test/awb_dump.c` | **New**: Read-only ISP query tool that dumps all AWB calibration data from superb's running ISP (pub_attr, wb_attr, wb_info, ccm_attr, awb_attr_ex, saturation, color_tone, stats_cfg). Requires LD_PRELOAD of ISP algorithm plugins. |
| `driver/Makefile` | Added `awb_dump` build target |
| `tools/pull_file.py` | New: pull files from camera (tries /dev/tcp, falls back to base64) |
| `tools/deploy_base64.py` | **New**: deploy files to camera via base64 over shell (when recv daemon is down) |
| `tools/diag_run.sh` | Added PQ bin path as optional $1 argument (defaults to day.bin) |
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

### 12. Superb's SystemCfg.ini VENC Configuration
From `/etc/conf.d/syscfg/SystemCfg.ini`:
- CH1 (main): 3840x2160 @15fps, VBR rcMode=3, bitrate=4096, QP 35-44
- CH2 (sub): 720x576 @15fps, VBR rcMode=3, bitrate=1024, QP 24-38
- Note: config says 3840x2160 but sensor is 3200x1800. Config may be
  a template or superb may upscale.
- **Framerate is 15fps** (not 20fps like our pipeline) -- this means superb
  gets 33% more light per frame, needing less gain, producing less noise.
  This could be a significant contributor to the noise difference.

### 13. PQ Bin Scene Modes Are Not Interchangeable
night.bin and light.bin both switch sensor to slow-shutter mode (fl=5624
vs day.bin fl=2812), effectively halving the frame rate to ~10fps. This
breaks our VENC pipeline timing. night.bin also sets saturation=0
(grayscale). These bins are designed for specific modes and can't be used
as drop-in replacements for day.bin.

---

## Deploy and Run

```bash
# Build
cd /mnt/e/Projects/ipc_XMeye_camera/driver && make all

# Deploy (requires recv daemon running on camera port 8888)
python tools/send_file.py 192.168.1.153 8888 \
    driver/build/pipeline_test \
    driver/build/libsns_sc635hai.so \
    driver/build/libbin.so \
    tools/diag_run.sh

# Deploy fallback (when recv is down -- uses base64 over shell, slower)
python tools/deploy_base64.py driver/build/pipeline_test /progs/rec/00/ipc_drv/pipeline_test

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

### Low-light tests (ISO ~1550-2000, warm indoor)
- `capture_images/capture_lowlight2.jpg` -- day.bin, CBR 4Mbps, ISO=1552, CT=2506K.
  Correct colors but more chroma noise/blotching than superb's main stream.
- `capture_images/capture_nightbin.h265` -- night.bin: grayscale (sat=0), slow shutter.
  Video plays OK but no color. Different artifacts, sharp/contrasty.
- `capture_images/capture_lightbin.h265` -- light.bin: slow shutter, slightly warmer,
  no noise improvement over day.bin. H265 playable but ffmpeg frame extract fails.
- `capture_images/capture_vbr.jpg` -- day.bin, VBR 4Mbps max, QP 35-44.
  No visible improvement over CBR.
- `capture_images/capture_nrboost.jpg` -- day.bin, VBR, NR boost attempted but
  NR was AUTO mode (same as superb: fine_str=80, coring_wgt=50). No change.

### Low-light NR tuning session (May 12-13, 2026 -- nightlight-only scene)
- `capture_images/capture_nr_v1.jpg` -- First NR iteration. 15fps, BayerNR boost,
  DRC BCNR enabled. Lights were accidentally on, so not a fair comparison.
- `capture_images/capture_3dnr_v2.jpg` -- 3DNR V2 enabled at VI pipe level.
  Nightlight-only. First working 3DNR capture. Chroma noise reduced.
- `capture_images/capture_3dnr_max.jpg` -- Max 3DNR + max BayerNR + BCNR=8 +
  reduced high-ISO saturation. Nightlight-only. Noticeable improvement but
  lighting may have been slightly different (more grainy than sidebyside).
- `capture_images/capture_maxnr.jpg` -- Same config as 3dnr_max, second run.
- `capture_images/capture_sidebyside.jpg` -- **BEST low-light: same conditions
  as superb's live stream for direct comparison.** ISO=19198, CT=2717K.
  Side-by-side shows similar quality to superb with different noise character:
  our noise is fine per-pixel grain (fast), superb's is coarser chroma blobs
  (slower). Superb still has smoother flat areas but our text/edge detail is
  comparable or slightly better in motion.
- `capture_images/capture_sidebyside.h265` -- Full 10s H265 video for the above.

**Best daylight**: `capture_awb_cal.jpg` -- BGGR + PQ bin + auto CCM/AWB (ADVANCE) + superb's calibration
**Best low-light**: `capture_sidebyside.jpg` -- All NR enabled, 15fps, nightlight-only, ISO=19198

---

# Phase 6 -- Low-Light NR Tuning (Session: May 12-13, 2026)

## Summary

**3DNR V2 successfully enabled at VI pipe level.** Combined with ISP BayerNR
boost, DRC BCNR, framerate reduction to 15fps, and high-ISO saturation
reduction. Low-light image quality now approaches superb's with different
noise characteristics.

### Key breakthrough: 3DNR V2 at VI pipe level
- VPSS 3DNR (`ss_mpi_vpss_set_grp_3dnr_attr`) fails with `0xA007800C` on
  this platform. 3DNR must be configured via **VI pipe APIs**:
  `ss_mpi_vi_set_pipe_3dnr_attr` / `ss_mpi_vi_set_pipe_3dnr_param`
- The platform uses **NR V2** (not V1). Attempting to set V1 params returns
  `0xA0108007` (ILLEGAL_PARAM). Must use `ot_nr_v2` structures.
- 3DNR attr set must happen AFTER ISP init but BEFORE VPSS bind.
- 3DNR reduces H265 bitstream size by ~3x (14MB -> 4.5MB for 150 frames)
  confirming temporal averaging is active.

### Side-by-side comparison with superb (nightlight-only, ISO ~19198)
- **Color/brightness**: Near-identical warm yellow cast, similar exposure
- **Noise character differs**:
  - Ours: Fine, fast, per-pixel grain. Uniform across image.
  - Superb: Coarser, slower chroma blobs. Less uniform, "patchier."
- **Detail**: Comparable. Our text clarity may be slightly better in motion
  (less temporal smearing). Superb's still frames look smoother.
- **Verdict**: Close to matching. The noise difference is more about character
  than absolute level. Superb favors smooth stills at the cost of temporal
  artifacts; we have honest per-pixel noise.

## Changes Made

### pipeline_test.c

1. **Framerate reduced to 15fps** (was 20fps, matching superb's SystemCfg.ini)
   - `SENSOR_FPS = 15.0f`
   - VENC GOP=15, src/dst_frame_rate=15
   - 33% more exposure time per frame = less gain needed at same scene brightness

2. **ISP BayerNR: aggressive auto table boost**
   - `fine_strength` set to 128 (max) for ALL 16 ISO entries
   - `coring_wgt` set to 20 for all entries
   - `sfm0_detail_prot` set to 24 for all entries
   - `md_en = 1` (motion detection enabled)
   - `md_cfg` high-ISO boost: `md_static_fine_strength=200`, `md_static_ratio=48`,
     `md_anti_flicker_strength=48`, `tfs=200` for entries [8..15]

3. **DRC BCNR enabled** (Hi3516CV610-specific Bayer chroma NR in DRC module)
   - `bcnr_attr.enable = 1`, `bcnr_attr.strength = 8` (max, range [0, 8])
   - `dark_gain_limit_chroma` was already 0 (good -- no chroma amplification)

4. **3DNR V2 at VI pipe level** -- the biggest change
   - `ot_3dnr_attr`: enable=1, type=VIDEO_NORM, compress=NONE, motion=NORM
   - Chroma NR (nrc0): trc=255, sfc=200, tfc=63, tfs=15 (all max)
   - Chroma NR (nrc1): pre_sfs=16, sfs1=255, sfs2_coarse=31 (all max)
   - Temporal luma NR (tfy): tfs0=15, tfs1=14, tfs2=12, tfr0=31, ref_en=1
   - Motion detection (mdy): math0=32, math1=64, mabw=7
   - Spatial/edge params (sfy, iey) left at driver defaults (read-modify-write)

5. **High-ISO saturation reduction**
   - Auto saturation table entries [8..11] capped at 70 (was 100-110)
   - Entries [12..15] capped at 50 (was 90-100)
   - Reduces visible chroma noise at extreme ISO

6. **VB buffer count**: VB_YUV_CNT increased to 3 (was 2) for 3DNR headroom

### Files modified
- `driver/test/pipeline_test.c` -- All above changes
- `driver/src/sc635hai_cmos.c` -- Updated AWB section header comments only
- `tools/diag_run.sh` -- Sleep increased to 35s for 15fps timing

## Technical Discoveries

### 14. 3DNR Must Be at VI Pipe Level (Not VPSS)
On Hi3516CV610 with VI_ONLINE_VPSS_OFFLINE mode, VPSS 3DNR is not available.
All `ss_mpi_vpss_*_grp_3dnr_*` calls return `0xA007800C` (NOT_PERM). Instead,
use VI pipe APIs: `ss_mpi_vi_set_pipe_3dnr_attr` / `ss_mpi_vi_set_pipe_3dnr_param`.
The `/proc/umap/vi` output shows `vi pipe 3dnr attr` section confirming VI-level 3DNR.

### 15. Platform Uses NR V2 (Not V1)
`ss_mpi_vi_get_pipe_3dnr_param` returns `nr_version=2` (OT_NR_V2). Setting V1
params returns `0xA0108007`. V2 structures differ significantly from V1:
- `ot_nr_v2_pshrp` replaces `ot_nr_v1_iey` (complex sharpening, not simple ies0-3)
- `ot_nr_v2_sfy` has 3 levels (not 5), different field layout
- `ot_nr_v2_tfy` has tfs0/tfs1/tfs2 4-bit bitfields, tss0/1/2 (no tdz fields)
- `ot_nr_v2_mdy` has no `adv_math` field
- `ot_nr_v2_nrc0` has tfs_mot[17] motion mapping array
- `ot_nr_v2_nrc1` has sfs1_mot[17], sfs2_mot[16], sfs2_sat[20]

### 16. 3DNR Read-Modify-Write Is Essential
The 3DNR param structure is large and complex. Zeroing it and setting only known
fields causes ILLEGAL_PARAM errors or visual artifacts. Must read current params
first with `get_pipe_3dnr_param`, then modify only the fields you understand,
then write back. This preserves driver defaults for iey/pshrp/sfy/luty/pp etc.

### 17. 3DNR Temporal Averaging Confirmed via Bitstream Size
Without 3DNR: 14MB for 150 frames at ISO 19198.
With 3DNR V2 (max settings): 4.5MB for same frame count and ISO.
The ~3x bitstream reduction proves temporal NR is actively denoising (the encoder
has less per-frame variation to encode).

### 18. DRC BCNR Is Hi3516CV610-Specific
The `ot_isp_drc_bcnr_attr` inside `ot_isp_drc_attr` is marked "Only support for
Hi3516CV610". It provides Bayer-domain chroma NR within the DRC module. Range
[0, 8] for strength. PQ bin sets it to enable=0, strength=3 by default.

### 19. Superb's dark_gain_limit_chroma Is Already 0
We expected to find superb boosting chroma in dark areas (causing chroma noise),
but `dark_gain_limit_chroma=0` means DRC is not amplifying chroma in shadows
at all. The remaining chroma noise is purely from sensor read noise at high gain.

### 20. Noise Character Difference: Per-Pixel vs Block
Our pipeline: fine per-pixel grain, fast temporal variation (every pixel flickers).
Superb's pipeline: coarser chroma blobs, slower variation, "patchier" noise.
This suggests superb uses stronger spatial smoothing that groups noise into larger
structures, possibly through different 3DNR spatial filter settings or a different
NR approach entirely (AI ISP / neural network NR).

## Next Steps

### 1. Query Superb's 3DNR Configuration (HIGH)
Build a 3dnr_dump tool (similar to awb_dump) that queries superb's running
VI pipe 3DNR params via `ss_mpi_vi_get_pipe_3dnr_attr` / `get_pipe_3dnr_param`.
This would reveal the exact NR V2 settings superb uses.

### 2. Spatial NR Tuning (MEDIUM)
Current approach maxes temporal NR but leaves spatial params at defaults. The
`sfy` (spatial filter) and `iey` (pre-sharpening) params could be tuned to
better match superb's spatial smoothing character.

### 3. ISP I2C Sync Fix (RESOLVED)
See PHASE9_ISP_I2C_SYNC.md. Kernel sync path working, direct I2C removed.

### 4. DRC Tuning (LOW)
Superb's stream is slightly brighter in shadows. DRC manual mode params from
PQ bin may need adjustment for better shadow/highlight balance.

### 5. AI ISP Investigation (LOW)
Superb's coarser noise pattern could indicate AI-based NR. The ot_aiisp kernel
module is loaded but no SC635HAI AI model exists. Would need a neural network
model file (dayaiisp.bin) to enable this.

---

## Phase 7 Session Notes: Framerate + NR Parameter Matching

### Key Corrections

1. **Framerate: Superb runs sensor at 20fps, NOT 15fps**
   - `/proc/umap/vi` shows `src_rate=20, dst_rate=20`, `frame_rate=20`
   - `/proc/umap/isp` AE shows `fps: 20.00`, `real_fps: 2000`
   - VTS register live: `0x320E=0x0A, 0x320F=0xFC` = VTS=2812 (20fps native)
   - **SystemCfg.ini 15fps is the VENC OUTPUT rate, not sensor rate**
   - VENC picks 15 of 20 frames/second (drop every 4th frame)
   - More temporal samples at 20fps = better 3DNR performance

2. **WDR/HDR: Superb uses LINEAR mode, no sensor HDR**
   - `/proc/umap/vi` WDR fusion: `wdr_mode: none`
   - `/proc/umap/isp` pub_attr: `wdr_mode: linear`
   - Superb's "WDR" (`bEnableWdr=1`) is ISP DRC tone-mapping, NOT sensor WDR
   - Ghidra: `secu_sensor_digital_wdr_set` calls `hi_mpi_isp_set_drc_attr()`
   - SC635HAI hardware supports 2-exp staggered HDR but superb doesn't use it

3. **Superb's ISP bayer format is RGGB(0) in proc**
   - PQ bin sets it to RGGB; superb doesn't re-set to BGGR
   - Our pipeline re-sets to BGGR(3) which produces correct colors
   - This means superb may actually run with a color swap that's compensated
     elsewhere (in CCM or sensor mirror/flip register)

### Changes Made (pipeline_test.c)

1. **SENSOR_FPS = 20.0f** (was 15.0f)
   - VENC: `src_frame_rate=20, dst_frame_rate=15`
   - Sensor/ISP runs at native 20fps, VENC outputs 15fps

2. **3DNR params: superb-matched with boosted chroma NR**
   Based on superb's `/proc/umap/vi` 3DNR V2 dump:
   - mdy0: tfs=8, math=100, mathd=80, mabw=2, tdz=32 (exact superb)
   - tfy: tfs=0,11,12; tss=16,0,0; tfr0=14,8,14,8,0,0 (exact superb)
   - mdy: math0=100, math1=419 (exact superb)
   - sfy: sfs1=64, sbr1=128, sfs2=64, sth=40/80/60 (exact superb)
   - **nrc0**: trc=64, sfc=64, tfc=24, tfs=13 (2.7x superb's 24/24/12)
   - **nrc1**: pre_sfs=12, sfs1=180 (1.5x superb's 9/119)
   
   Chroma NR boosted because our pipeline's BayerNR temporal integration
   appears weaker than superb's (same PQ bin but different init order/timing).

3. **BayerNR: PQ bin defaults preserved** (was maxed at fine_str=128)
   - PQ bin gives fine_str=80 for ISO [0..11], 128 for [12..15]
   - md_auto tfs=255 across all ISOs (matches superb's proc exactly)
   - md_en=1 ensured

4. **Saturation: PQ bin defaults preserved** (was crushed to 50-70)
   - Superb shows sat=103 at ISO 19189
   - PQ bin auto table: 90-100 at high ISO -- close enough

5. **DRC BCNR: only enabled if off** (was force-set to strength=8)
   - PQ bin sets bcnr: enable=0, strength=3
   - We just flip enable to 1, keep PQ bin's strength=3

### Capture Results (nightlight-only, ISO 19198)

| Version | H265 size (150f) | NRC0 trc/sfc | NRC1 sfs1 | Quality |
|---------|-----------------|-------------|-----------|---------|
| Phase 6 maxed NR | 4.5 MB | 255/200 | 255 | Smooth but soft, detail loss |
| Superb-exact NR | 9.3 MB | 24/24 | 119 | Too noisy (missing sfy) |
| Superb + sfy spatial | 9.3 MB | 24/24 | 119 | Still noisy |
| **Phase 7 v3** | **5.6 MB** | **64/64** | **180** | **Good balance** |

### Superb's Complete NR Chain (from proc)

BayerNR (ISP):
- fine_strength=80, coring_wgt=50, sfm0_de_prot=16
- md_en=1, md_mode=2, tfs=255, md_sta_fine_str=55
- md_sta_ratio=26, md_anti_fli_str=32

DRC:
- enable=1, manual, strength=256 ("digital WDR")
- BCNR: we enable, superb's state unknown from proc

3DNR V2 (VI pipe):
- See table above for exact values
- tfs_mode=1, sfs2_mode=0, gamma_en=1, ca_en=0

### Visual Comparison (end of Phase 7)

Compared superb live feed, our v3 capture, and old sidebyside (Phase 6) in
the same viewer. Lighting was darker than Phase 6 session. Result:

- **Superb live**: Noisy (worse than Phase 6 sidebyside due to darker scene)
- **Our v3**: Similar noise level to superb live, slightly more chroma speckle
- **Old sidebyside**: Cleanest, but was captured in better lighting + maxed NR

**Conclusion**: Quality gap between us and superb is small in matched conditions.
The main remaining difference is chroma noise texture -- superb's is more uniform/
smoother, ours has more green/magenta dot speckling. This is a chroma NR gap.

Proper comparison requires streaming (live video, not still extraction from H.265
P-frames which amplify inter-frame noise). Defer further NR tuning until streaming
is working.

### Things to Try (Future NR Tuning)

Once streaming is working for real-time A/B comparison:

1. **Bump chroma NR higher**: Push nrc0 trc/sfc from 64 toward 128 (half-max),
   nrc1 sfs1 from 180 toward 220. This is the most impactful knob for the
   remaining green/magenta speckle.

2. **BCNR strength**: Currently using PQ bin's strength=3 (we just flip enable).
   Try strength=5-8 to see if DRC-embedded Bayer chroma NR helps.

3. **BayerNR md_static_fine_strength**: PQ bin gives [32..64] across ISOs.
   Superb's proc shows 55 at runtime. Could try boosting [8..15] entries
   to 80-100 for more aggressive static-area denoising at high ISO.

4. **3DNR auto mode instead of manual**: Our manual params are a snapshot of
   superb at one ISO. Superb may use auto-mode 3DNR that scales with ISO.
   Try `op_mode = OT_OP_MODE_AUTO` with appropriate ISO tables.

5. **luty (sfy_lut) tables**: The 3DNR V2 has per-brightness/variance spatial
   filter LUTs (luty[0], luty[1]) that we leave at driver defaults. Superb's
   proc shows specific values (sf_var, sf_dir, sf_bri etc). These control
   how spatial NR varies across the image and could explain superb's more
   uniform noise character.

6. **Temporal NR tfs0**: Superb uses tfs0=0 (coarse temporal OFF). Try tfs0=4-8
   to add coarse temporal averaging -- may help with large low-frequency noise
   patches at the cost of some motion smearing.

7. **DRC tuning**: Superb's proc shows DRC strength=256 (manual mode). Our PQ bin
   sets the same. Could experiment with slightly lower strength for less shadow
   noise amplification, or adjust dark_gain_limit_chroma.

8. **Saturation curve**: PQ bin gives [90..140] across ISOs. At ISO 19198 superb
   shows sat=103. Could try slightly lower high-ISO saturation (85-95) to reduce
   chroma noise visibility without the aggressive 50-70 crush we tried before.

---

## Phase 8: RTSP Streaming

### Implementation

Added live RTSP streaming to `pipeline_test` using the SDK's pre-built xop RTSP
library (`libxoprtsp.a`). This is a complete RTSP/RTP server with H.265 support,
exposed via a 4-function C API:

```c
rtsp_server_start(ip, port);
rtsp_session_create(index, is_h265);
rtsp_session_push_frame(index, data, len, is_key);  // in VENC loop
rtsp_server_stop();
```

We wrapped this in `driver/rtsp/rtsp_push.{h,c}` which bridges our VENC output
directly to the xop library. Each VENC pack (NALU) is pushed individually, with
SEI NALUs skipped and IDR/I-slice keyframes flagged.

### Architecture

```
Sensor -> ISP -> VPSS -> VENC -> [get_stream loop]
                                        |
                            +-----------+-----------+
                            |                       |
                      rtsp_push_venc_stream()   fwrite() (file mode)
                            |
                     xop RTSP library
                     (RTP packetization,
                      SDP, client mgmt)
                            |
                     UDP to VLC/ffplay
```

### Files

| File | Purpose |
|------|---------|
| `driver/rtsp/rtsp_push.h` | Public API (3 functions) |
| `driver/rtsp/rtsp_push.c` | VENC-to-RTSP bridge (~100 lines) |
| `driver/test/pipeline_test.c` | Added `--rtsp` flag, streaming loop |
| `driver/Makefile` | libxoprtsp.a + static libstdc++ linking |

### Usage

```bash
# On camera (via diag_run.sh or manual):
pipeline_test --rtsp                          # port 554, bind all
pipeline_test --rtsp --rtsp-port 8554         # alt port
pipeline_test --rtsp --rtsp-ip 192.168.1.153  # specific bind

# On host:
vlc rtsp://192.168.1.153:554/live0
ffplay rtsp://192.168.1.153:554/live0
```

### Build details

- `libxoprtsp.a` statically linked (SDK pre-built, ARM EABI5)
- `libstdc++.a` statically linked (no runtime .so dependency)
- Binary: 421KB (was ~380KB; +40KB for xop + C++ runtime)
- No new dynamic dependencies -- deploys same as before

### Coexistence with superb

Superb uses XMeye protocol on its own ports. Our RTSP on port 554 won't conflict.
However, we can't run both pipelines simultaneously (only one process can own
the sensor/ISP at a time). The `diag_run.sh` workflow handles this:

1. SIGSTOP mySystem (freezes superb)
2. Kill superb processes
3. Run pipeline_test --rtsp
4. Ctrl+C or kill to stop
5. SIGCONT mySystem (restores superb)

Superb's RTSP-like endpoints are `/live1` and `/live2`. Ours is `/live0`.

### Testing (Phase 8a) -- RESOLVED

**Root cause found: hardware watchdog reboot (not a crash).**

The ~27s "crash" was actually the Hi3516CV610's hardware watchdog resetting
the entire SoC. `superb` feeds `/dev/watchdog` on fd 4 with a 30-second
timeout. When we killed superb, nobody fed the watchdog, and the SoC
hard-rebooted after exactly 30 seconds.

**Evidence:**
- File capture mode (no RTSP) runs perfectly: 150 frames, EXIT_CODE=0,
  clean teardown. Pipeline is not crashing.
- No signal handler fired (not SEGV/ABRT/PIPE/HUP). Process was killed by
  hardware reset, not a software signal.
- `WDIOC_GETTIMEOUT` confirmed: **timeout = 30 seconds** (matches the
  ~27-30s observation exactly).
- SDP was never broken -- previous DESCRIBE responses were from superb's
  RTSP server (which came back after reboot), not from our xop server.
  Our xop SDP correctly includes `m=video 0 RTP/AVP 96`.

**Fix applied to `pipeline_test.c`:**
1. Open `/dev/watchdog` with O_RDWR after sys_init (takes over from superb)
2. `WDIOC_SETTIMEOUT` to extend timeout to 120 seconds (accepted by driver)
3. Feed via `WDIOC_SETTIMEOUT(120)` every VENC frame (~15fps) -- this is the
   ONLY mechanism that works on the HiSilicon `ot_wdt` driver. Both
   `WDIOC_KEEPALIVE` and `write()` return EPERM on this driver. Confirmed
   by `wdt_test.c`: survived 45s with 30s timeout using SETTIMEOUT, while
   KEEPALIVE and write both failed every call.
4. `write(fd, "V", 1)` on clean exit to disarm without triggering reboot
5. Crash signal handler also disarms watchdog before re-raise

**Result: RTSP streaming stable indefinitely.**
- 21,000+ frames (23+ minutes) confirmed stable, stream stays up
- Previous version using WDIOC_KEEPALIVE died at exactly 120s (the extended
  timeout) because KEEPALIVE was silently failing with EPERM
- Live video visible in MPV player via `rtsp://192.168.1.153:554/live0`
- SDP correct: `m=video 0 RTP/AVP 96`, `a=rtpmap:96 H265/90000`
- xop RTSP server V5.5, session `live0`, H265 RTP payload type 96

**Launch pattern (must use SIGSTOP mySystem):**
```sh
MYS=$(pidof mySystem)
kill -STOP $MYS       # Prevent superb respawn
killall -9 superb     # Free ISP resources
# pipeline_test --rtsp feeds watchdog internally
./pipeline_test --rtsp /home/sensor/sc635hai/pqbin/day.bin
# On exit, pipeline_test disarms watchdog with magic close 'V'
kill -CONT $MYS       # Resume mySystem
```

### Remaining Work

1. ~~**ISP I2C sync fix**~~ -- RESOLVED. See PHASE9_ISP_I2C_SYNC.md.
   Kernel sync path now delivers frame-synchronized register writes.
2. **Audio support** -- camera has mic, needs AI/AENC init + second RTSP track
3. **Daylight test** -- verify NR changes don't over-smooth bright scenes
4. **Scene mode switching** -- day/night/light PQ bin swapping

### Completed / Resolved

- **NR tuning** -- all deployed and verified, user confirmed "looks good"
- **DRC tuning** -- TESTED: boosted strength 160→384, asymmetry 10→4, etc.
  No visible difference in dark scene (AE maxed at ISO 19198, DRC can't create
  light). Reverted to PQ bin defaults. Only BCNR boost (3→6) kept.
- **Deploy workflow** -- `recv` binary on camera (port 8888 one-shot) + send_file.py
  is fast and reliable. base64 chunked deploy_file.py works but slow.
  `capture_run.sh` deployed for file capture mode (no RTSP).
- **PQ bin DRC defaults discovered**: strength=160 (not 256), asymmetry=10,
  second_pole=200, stretch=60, compress=200, detail_adj=8, spatial_flt=1

### Key Files for New Sessions

- `tools/recv.c` -- already compiled on camera at `/progs/rec/00/ipc_drv/recv`
- `tools/send_file.py` -- send files to recv (one-shot or daemon mode)
- `tools/pull_file.py` -- pull files from camera (TCP callback + base64 fallback)
- `tools/capture_run.sh` -- on camera, runs capture mode (setsid, SIGSTOP, 150 frames)
- `tools/rtsp_run.sh` -- on camera, runs RTSP streaming mode
- `tools/deploy_file.py` -- base64 deploy (slow fallback, use recv instead)
- `driver/test/pipeline_test.c` -- main binary
- `driver/Makefile` -- `make pipeline` to cross-compile
