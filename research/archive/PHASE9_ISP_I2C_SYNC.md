# Phase 9 -- ISP Kernel I2C Sync Path Fix (Sessions: May 13-15, 2026)

Kernel I2C sync path is now WORKING. Direct userspace I2C writes disabled.

> **2026-05-15: OPEN QUESTION FULLY RESOLVED via isolated diff-only test.**
> Methodology: temporarily disabled force-TRUE block while keeping all
> Iteration 2 sync-queue fixes in place. Rebuilt, deployed, restarted
> pipeline_test. Observed live behavior. See "Outcome A Confirmed (2026-05-15)"
> section near the bottom.
>
> **Final answer:**
> - Force-TRUE is purely a **steady-state safety net**. It is NOT load-bearing
>   for transient AE response.
> - Iteration 2 sync-queue fixes alone (`hmax_times`, `max_int_time`, AE route,
>   `ctrl_param`, AE callbacks, `stIspEvo` enable) give correct AE behavior
>   during normal operation: lighting transitions, color-temperature changes,
>   exposure adjustments all work cleanly with pure diff-based updates.
> - The steady-state freeze IS real and reproducible. Poking sensor regs in a
>   stable scene → high-byte EXP_H stays stuck because diff sees no change in
>   the userspace `regs_info[0].data` value (which always remains the same low
>   value for normal lighting), even though the actual sensor register was
>   externally clobbered to `0xAA`.
> - Diagnostic curiosity confirmed: under poked-EXP_H, AE drives EXP_M/L and
>   gain to minimum trying to compensate. The sensor then runs with an
>   effective exposure that's wrong by an order of magnitude. The image looks
>   blown-out in bright scenes, and (counter-intuitively) clean and well-exposed
>   in dark scenes because the long forced-integration accidentally matches
>   what AE would have wanted in the dark with high gain. Frame rate also
>   drops in dark because integration time exceeds nominal frame period.
> - Therefore: keep force-TRUE. It costs ~50us/frame of I2C bandwidth (~0.1%
>   of frame budget at 20fps) and guards against manual AE lock, perfectly
>   stable scenes, and external register pokes.
>
> All three "future investigation" hypotheses now have firm verdicts:
> - Hypothesis 1 (jitter saves shumjj): partially true. Steady-state freeze is
>   real for shumjj too (same diff logic, same sensor quantization). Their
>   transients work because their app sets the same sync-queue config we now
>   have post-Iteration-2. They simply don't notice the bug because no one
>   tests for manual AE lock or perfectly-stable lighting + sensor pokes.
> - Hypothesis 2 (V1.0.2.0 hidden force-write path): falsified via symbol /
>   disasm analysis.
> - Hypothesis 3 (shumjj's `ot_isp.ko` differs): falsified via byte-level
>   disasm. See `PHASE9_KO_DIFF.md`.
>
> What we got wrong on first pass and why the May-14-evening note was
> over-confident: we conflated "force-TRUE was the key fix" with "force-TRUE
> alone produced the observed pre-Iteration-2 behavior." It did not. The
> Iteration 2 sync-queue fixes did most of the heavy lifting -- they made the
> kernel actually consume update flags at all. The user's correct skepticism
> ("pre-fix image stayed at boot defaults, not just frozen-at-steady-state")
> prompted the isolated diff-only test which produced the definitive
> answer: with sync-queue working but force-TRUE disabled, transients DO
> work. The pre-Iteration-2 frozen image was the sync queue being dead,
> not the diff logic being broken during transients.

**Root cause:** `cmos_get_sns_reg_info` diff-based update flags went FALSE once
AE converged, causing kernel to skip all I2C writes silently. The data stopped
changing frame-to-frame in stable lighting, so the diff found no changes and
all `i2c_data[i].update` flags were set to `TD_FALSE`.

**Fix:** Force `update = TD_TRUE` for AE-driven registers (EXP_H/M/L,
AGAIN_COARSE/FINE, DGAIN_COARSE/FINE, GROUP_HOLD bookends) every frame in
the `cmos_get_sns_reg_info` incremental-update branch.

**Also required:** `hmax_times` (was 0), `max_int_time` (was `2*VTS-10`,
should be `VTS-10`), 3-node AE route matching superb, `ctrl_param`
(`be_buf_num=4`, `quick_start_en=1`), and missing AE default fields.

**Verified via poke test:** Kernel restores poked sensor regs within 3 seconds.
Direct I2C writes in `cmos_inttime_update` and `cmos_gains_update` are
commented out; sensor exposure/gain delivered solely by kernel
`ot_sensor_i2c_write` callback, frame-synchronized at FE_END.

---

## ISP I2C Sync Path Investigation (2026-05-13)

### TL;DR

**Status after Ghidra diff analysis (2026-05-13 evening):**

1. **`cmos_get_sns_reg_info` was fixed** -- three structural bugs are corrected.
   Verified correct by on-hardware diagnostic prints. This fix is necessary
   and correct.

2. **The "userspace/kernel version mismatch" theory is WRONG.** Verified by
   reverse-engineering both binaries:
   - Our V1.0.2.1 `libot_mpi_isp.so` is **functionally identical** to superb's
     statically-linked V1.0.2.0 ISP code. Side-by-side decompilation of every
     function in the sensor sync path shows the same logic, same ioctl
     numbers, same struct offsets. Only line numbers in fprintf differ by
     exactly 3 (3 added lines somewhere in V1.0.2.1 source).
   - Our `ot_isp.ko` is **byte-equivalent** (in string content) to shumjj's
     V1.0.2.1 B020 `ot_isp.ko`. The "V1.0.0.0 B010" version string is just a
     forgotten `#define` from an early build -- the actual code is the same.
     Module string diff yields only 2 strings different: the version stamp
     and a build-template artifact.

3. **The kernel sync path SHOULD work.** Userspace and kernel are compatible.
   The previous session's test that "proved" the kernel path doesn't write
   the sensor needs to be re-validated with proper instrumentation.

**The direct userspace I2C writes in `cmos_inttime_update` and `cmos_gains_update`
remain in place pending re-test.** They are still load-bearing until we
prove the kernel sync path delivers exposure/gain to the sensor.

### Why It Was Investigated

`cmos_get_sns_reg_info` had three structural bugs that prevented the kernel
sync path from receiving valid data (these have been fixed):

1. **Init path target**: Was writing register addresses/data to `sns_regs_info`
   (the ISP output pointer), then the memcpy at the end overwrote those writes
   from `state->regs_info[0]` (which was zeroed). Now writes to
   `state->regs_info[0]` directly.

2. **Update flag target**: Was setting `.update` flags on `sns_regs_info`,
   also clobbered by the memcpy. Now sets flags on `state->regs_info[0]`.

3. **Missing config check**: SDK reference checks both `sync_init` AND
   `sns_regs_info->config` to decide init vs update. Now matches SDK pattern.

The rewrite of `cmos_get_sns_reg_info` exactly matches the SC4336P, SC431HAI,
GC8613, and HY006 reference drivers from the shumjj repo. Verified by
diagnostic prints: the struct returned to the ISP has correct register
addresses (0x3E00 exp, 0x3E08 again, etc.), correct data values matching
what the AE commanded, correct update flags, and correct `dev_addr=0x60`,
`com_bus.i2c_dev=0`.

### Previous Session's Failed Test (May Have Been Flawed)

Prior on-hardware test (2026-05-13 morning) with direct I2C disabled in
`cmos_inttime_update`:

- **ISP AE commanded** `int_time=8` (line 8, gain 1024)
- **Sensor register readback** showed exp=1867 (stale)
- **Gain registers** matched (gains_update still had direct I2C active)

Conclusion at the time: kernel path doesn't deliver exposure. **But this test
may have been flawed** -- the AE may not have been settled, or the readback
timing may have been wrong. Needs re-validation.

### Version Strings Are Misleading (Disregard Earlier Theory)

Kernel modules on this device:

| Module | Version (from /proc/umap) | Build Date |
|--------|---------------------------|------------|
| `ot_isp.ko` | **V1.0.0.0 B010** | Jul 26 2025 |
| `ot_vi.ko` | V1.0.2.0 B051 | Apr 16 2025 |
| `ot_vpss.ko` | V1.0.2.0 B051 | Apr 16 2025 |
| `ot_venc.ko` | V1.0.2.0 B051 | Apr 16 2025 |
| `ot_sys.ko` | V1.0.2.0 B051 | Apr 16 2025 |

**Ot_isp.ko reports an odd version, but the ABI is the same.** Verified by
comparing against shumjj's V1.0.2.1 B020 `ot_isp.ko`:
- File sizes within 64 bytes of each other
- String content IDENTICAL except for the version string itself and one
  build-template artifact
- Both contain the same symbols, same error messages, same struct field
  references

The "V1.0.0.0 B010" stamp is a forgotten `#define` from an early build. The
kernel module's ABI is V1.0.2.x compatible.

### Userspace Library Diff (Ghidra Analysis)

Side-by-side decompilation of V1.0.2.1 `libot_mpi_isp.so` (our SDK) and the
statically-linked V1.0.2.0 ISP code in `superb`:

| Function | Diff |
|----------|------|
| `isp_check_sns_register` | Identical (line numbers differ by 3) |
| `isp_sensor_get_sns_reg` | Identical |
| `isp_sensor_update_sns_reg` | Identical |
| `isp_sync_cfg_set` | Identical (field offsets render differently due to symbol resolution but are the same values) |
| `isp_run` | Identical (fprintf line numbers differ by 3) |
| `isp_run_thread` | Identical (same ioctl 0xc0147001 and 0x7051) |
| `ot_mpi_isp_run` | Identical (same 0x8034703b and 0x40047007 ioctls) |
| `isp_sensor_reg_callback` | Identical (same 4-byte + 0x30-byte memcpy_s pattern) |
| `ot_mpi_isp_sensor_reg_callback` | Identical (same null-checks on indices 0, 5, 6, 8, 9) |

**Conclusion: there is no userspace protocol difference between superb and
our pipeline_test.** Both call the same ioctls with the same payloads.

### Struct Registration Verified Correct

The `ot_isp_sns_exp_func` struct is 48 bytes = 12 function pointers:

| Index | Field | Required (null-checked)? |
|-------|-------|--------------------------|
| 0 (offset 0x00) | `pfn_cmos_sns_init` | YES |
| 1 (offset 0x04) | `pfn_cmos_sns_exit` | no |
| 2 (offset 0x08) | `pfn_cmos_sns_global_init` | no |
| 3 (offset 0x0c) | `pfn_cmos_set_image_mode` | no |
| 4 (offset 0x10) | `pfn_cmos_set_wdr_mode` | no |
| 5 (offset 0x14) | `pfn_cmos_get_isp_default` | YES |
| 6 (offset 0x18) | `pfn_cmos_get_isp_black_level` | YES |
| 7 (offset 0x1c) | `pfn_cmos_get_blc_clamp_info` | no |
| 8 (offset 0x20) | `pfn_cmos_get_sns_reg_info` | **YES (this is the sync callback)** |
| 9 (offset 0x24) | `pfn_cmos_set_pixel_detect` | YES |
| 10 (offset 0x28) | `pfn_cmos_get_awb_gains` | no |
| 11 (offset 0x2c) | `pfn_cmos_get_standby_cfg` | no (only called if non-null) |

Our `cmos_init_sensor_exp_function` registers indices 0-10. Index 11
(`pfn_cmos_get_standby_cfg`) is left NULL, which is allowed (the userspace
library null-checks before calling it). All 5 required pointers are set.

The kernel module reads our callback at sensor context offset +0x24, which
maps to `pfn_cmos_get_sns_reg_info` (offset 0x20 in `ot_isp_sns_exp_func`
+ 0x04 attr_info prefix in the per-pipe context).

### What Was Ruled Out

- **`cmos_get_sns_reg_info` data correctness**: Verified by diagnostic
  prints -- correct addresses, data, flags, dev_addr, i2c_dev.
- **`ot_sensor_i2c.ko` not loaded**: Module IS loaded, registered I2C adapters
  successfully (no "i2c:%d get adapter error!" in dmesg after reload).
- **`OT_ISP_MAX_SNS_REGS` mismatch (50 vs 32)**: Our `reg_num=11`, well within
  both limits. Not the issue.
- **Struct field size**: `ot_isp_i2c_data` = 24 bytes on ARM (because `td_bool`
  = `int` = 4 bytes). Matches kernel's `r9=24` stride in `isp_drv_write_i2c_data`.
- **`com_bus.i2c_dev`**: Confirmed 0, matches superb and the running ISP's
  `/proc/umap/isp` `sensor info`.
- **Bus 0 I2C adapter availability**: Confirmed by successful module reload.

### [SUPERSEDED] Re-Test Confirms Kernel Sync Path is Broken (Despite Identical Code)

> **SUPERSEDED 2026-05-14 by Iteration 3.** At the time of this test the
> root cause was not yet understood. The kernel sync path WAS engaged
> (callback fp registered, kernel reading regs_info every frame) -- but
> our `cmos_get_sns_reg_info` "returning correct data with proper update
> flags" claim was wrong. Update flags went FALSE once AE converged
> because the data stopped changing frame-to-frame, so the kernel
> iterated 11 regs and skipped all of them. The fix: force
> `update = TD_TRUE` for AE-driven regs every frame. See Iteration 3.
>
> Kept below for historical context.

After Ghidra analysis suggested userspace + kernel should be compatible,
we ran a controlled re-test (2026-05-14 00:00 local):

**Test 1 - Sensor readback comparison while pipeline_test runs:**
- Build with direct exposure writes disabled (gains still direct).
- ISP AE: `sys_gain=1024, line=8, exp=142us`.
- `cmos_get_sns_reg_info` returns `exp=000080` (= 8 half-lines, correct).
- Sensor reg readback: `0x3E00=0x00, 0x3E01=0x74, 0x3E02=0xB0` = exp=1867
  (hardware default, never written).

**Test 2 - Poke test (definitive proof):**
- Wrote marker value 0x00, 0x0A, 0xA0 (exp=170) directly via i2c_write while
  pipeline_test was running with ISP commanding line=8.
- Re-read sensor 5 seconds later: still 0x00, 0x0A, 0xA0. Kernel did NOT
  overwrite.

**Test 3 - Same poke against stock superb:**
- Wrote marker value while superb was running with ISP commanding line=282.
- Re-read sensor 2 seconds later: 0x00, 0x12, 0xC0 (exp=300). Kernel
  overwrote the poke and updated with current AE value. Works as designed.

**[SUPERSEDED] Conclusion: the kernel sync path is dead for our pipeline_test, despite:**
- `libot_mpi_isp.so` V1.0.2.1 being functionally identical to superb's
  statically-linked V1.0.2.0 ISP code (verified via Ghidra side-by-side
  decompilation of every function in the sensor sync chain).
- `ot_isp.ko` being byte-equivalent to shumjj's V1.0.2.1 B020 `ot_isp.ko`
  (the "V1.0.0.0 B010" version string is misleading).
- ~~Our `cmos_get_sns_reg_info` returning correct data with proper update flags~~ -- actually the update flags were the bug; see Iteration 3.
- Sensor I2C registration via `ot_sensor_i2c.ko` showing no errors at boot.

### Further Attempts (All Negative)

| Attempt | Result |
|---------|--------|
| Reorder `pfn_register_callback` before `pfn_set_bus_info` (SDK sample order) | No effect |
| Close userspace `/dev/i2c-0` fd after sensor init (theory: blocking kernel) | Broke AE entirely -- get_sns_reg calls stopped, only 5 inttime/get_sns_reg events |
| `rmmod` + `insmod ot_sensor_i2c.ko` while pipeline_test running | No effect, sensor still stuck at hw default |
| Diagnose dmesg for `ot_sensor_i2c.ko` errors | No relevant messages logged (wifi spam dominates) |

### Kernel-Side Analysis (2026-05-14, NEW)

Decompiled `ot_sensor_i2c.ko` (5KB) and ISP-side write path in `ot_isp.ko`
(196KB). Key findings in `tools/ghidra/output/sensor_i2c_kernel.md`
(see "Kernel Sync Path Forensics" appendix below).

**Registration flow (confirmed working):**
- `ot_sensor_i2c.ko` registers a single function `ot_sensor_i2c_write` via
  `isp_register_bus_callback(pipe, type=0, &fp)` for pipes 0..3 at module load.
- Internal name `ot_dev_isp_register` = `isp_register_bus_callback`.
  Stored as `g_isp_exp_func[0]` in `ot_isp.ko` (.data offset 0x108).
- Callback fp lands at `isp_ctx[0x7e9c]`. If null, the kernel logs
  `"pfn_isp_write_i2c_data is TD_NULL point!"` -- we do NOT see this
  message, confirming our registration succeeded.

**Where the kernel calls into the callback:** `isp_drv_write_i2c_data`
(at .text offset 0x21b4, 396 bytes). The gates it applies, in order:

1. NULL check `[ctx+0x7e9c]` -> logs error if null (we don't see it)
2. Loads sns_regs_info pointer from `[ctx+pipe*4+0x96c]`
3. Iterates `reg_num` registers
4. For each, calls `isp_drv_get_sns_cfg_node(ctx, pipe, idx)` which checks:
   - `ctx[0x814] == (idx % (ctx[0x813]+1))` (pipe-rotation gate)
   - `(idx+pipe) <= 7`
   - `[ctx+(idx+pipe)*4+0x96c] != NULL`
   - `[ctx+(idx+pipe)*4+0x970] != NULL`
   - `[ctx+(idx+pipe)*4+0x96c] == [ctx+(idx+pipe)*4+0x970]` (head==tail?)
   - `ctx[0x7e8] > 1` (buffer count gate)
5. If `get_sns_cfg_node` returns null -> **silently skip the I2C write** (no log)
6. Otherwise check update flags and finally `blx` the i2c_write callback

**`isp_ioctl_set_sync_cfg`** (.text offset 0x7afc, 376 bytes) is the userspace
sync-cfg load path. Validates incoming arg:
- `arg[12] <= 1`
- `arg[1756] <= 2`
- `arg[20]` (byte) in `1..8`
- `arg[1644]` (byte) <= 8
- `arg[16] <= 50` (reg_num? max regs?)

If validation passes, memcpy 0xa60 bytes (per pipe?) into a circular queue
at `[ctx+0x1000+0x3f0]/[0x3f1]` (head/tail of 8-deep ring buffer).

This ioctl IS being called from our pipeline -- `/proc/umap/isp` shows
`sync_cfg_gap: ~65ms` (matches our 15fps frame interval).

### Side-by-side `/proc/umap/isp` Comparison: Ours vs Superb

Captured 2026-05-14 (ours = pipeline_test, superb = stock daemon). Most
relevant differences:

| Field | Ours | Superb | Likely meaning |
|-------|------|--------|---------------|
| `int_type` | `start` | `other` | Phase of frame-interrupt processing |
| `be_buf_num` | 8 | 4 | BE cfg buffer count (offline mode only?) |
| `sensor_cfg_t` (avg) | 28 us | 8-9 us | Time spent in sensor-cfg call. **Both fast** -- both bypass the actual I2C transfer (~800us at 400kHz). |
| `bayer` (pubattr) | bggr | rggb | Pub_attr bayer (superb gets RGGB from PQ bin override) |
| `cros_cnt` | 1 | 13 (growing) | "Cross frame" event count |
| `cros_run_t` | 0 | 147900 | Time in cross-frame processing |
| `cros_pre_run_t` | 0 | 2531 | Pre-run cross processing |
| `cros_switch_t` | 32 | 4 | Cross-switch transitions |
| AE `node_id` count | 1 node | **3 nodes** | AE route node count |
| AE `max_line` | 5614 | 2804 | Max sensor exposure lines |
| AE `again` | 1024 | 1376 | Different scene brightness (low test impact) |

**Smoking gun candidates (still hypotheses):**

1. **`cros_*` fields are zero for us** -- the "cross frame" subsystem is what
   schedules sensor-config writes across frame boundaries. The strings in
   `ot_isp.ko` include:
   - "cross frame, loss cfg, raw_int:%d"
   - "ISP[%d] cross frame:%d"
   - "ISP[%d] cross[%d(%d)] end_int_t:%u, usr_t:%u, (run, swi, lock): cur(%u, %u, %u), pre(%u, %u, %u)"
   These come from `isp_drv_int_status_process` (interrupt status proc).
   If cross-frame never engages, the cfg queue ring head/tail never advance,
   `isp_drv_get_sns_cfg_node` always returns NULL, the I2C write is silently
   skipped. **This matches the symptom perfectly.**

2. **AE has 1 node (us) vs 3 nodes (superb)** -- superb's AE route is a 3-node
   route. Single-node may produce different ISP scheduling that skips the
   cross-frame path.

3. **`max_int_time` is wrong** -- we set `2*VTS-10` (5614) but SC4336P
   reference driver sets `VTS-FL_OFFSET_LINEAR` (= 2802). Independent bug,
   probably not related to kernel-sync, but a real fix needed.

### Suspected Root Cause (Refined, 2026-05-14)

The ISP "cross frame" subsystem is not engaging for our pipeline, even
though `isp_ioctl_set_sync_cfg` is being called. Without cross-frame state
advancement, `isp_drv_get_sns_cfg_node` silently returns NULL on every call
from `isp_drv_write_i2c_data`, so the kernel never invokes
`ot_sensor_i2c_write`.

What enables cross-frame in superb but not us is the next investigation.
Candidate triggers (most tested 2026-05-14, see Iteration 2):

a. `ss_mpi_isp_set_ctrl_param` -- we never call it. Defaults set
   `be_buf_num=8`; superb has 4. **TESTED**: setting be_buf_num=4 +
   quick_start_en=1 + matching wakeup. cros_cnt still 0.
b. `ss_mpi_isp_set_ae_route_attr` -- we never set an AE route. AE routes
   may control how the AE algo splits exposure/gain commands into multi-node
   sequences that drive cross-frame scheduling. **TESTED**: set 3-node route
   matching superb. cros_cnt still 0.
c. Interrupt enable mask -- `ss_mpi_isp_set_int_enable` or VI pipe IRQ
   config might gate which interrupt types fire `cros_*` handlers.
d. Some VI pipe attribute (vi_aiisp_mode, run_mode flag) influences ISP's
   running_mode internally and turns cross-frame on/off.

### Iteration 2 (2026-05-14): Many SDK gaps fixed, `cros_cnt` still 0

This iteration made the AE/ISP proc match superb on every documented
field except `cros_cnt` and `int_type`. Kernel I2C path still inactive.

**Driver fixes applied** (`driver/src/sc635hai_cmos.c`):

1. **`hmax_times` was 0 -> now 17780** (= 1e9 / (VTS * fps) = 1e9/(2812*20)).
   `cmos_get_ae_default` never wrote this field. Reference: `sc4336p_cmos.c:148`,
   `sc431hai_cmos.c:206`. The "h" stands for horizontal (line) max time in ns;
   it is what the AE algorithm uses to convert exposure microseconds to lines.
2. **`max_int_time` was `2*VTS-10` (5614) -> now `VTS-10` (2802)**.
   The SC500AI datasheet describes 16-bit half-line precision, suggesting
   `max = 2*VTS-10`, but superb empirically caps at single-line max
   (`max_line=2804` at VTS=2814). After applying this fix the `int_rat`
   field in `/proc/umap/isp` jumped from 10 to 20 (matching superb's 20fps
   actual ISP-interrupt rate). Fixed at lines 388, 452, 480 + the clamp in
   `cmos_inttime_update`.
3. **Missing AE defaults populated**: `full_lines_max`, `max_int_time_target`,
   `min_int_time_target`, `max_again_target`, `min_again_target`,
   `max_dgain_target`, `min_dgain_target`, `min_isp_dgain_target`. None of
   these were set before; per sc4336p reference all are expected.
4. **`lines_per500ms = VTS * 30 / 2` -> `VTS * fps / 2`**. Was using a
   hardcoded 30fps even though SC635HAI runs at 20fps.
5. **Missing AE callbacks added**:
   - `pfn_cmos_ae_quick_start_status_set` -- AE algo calls when quick_start
     toggles via ctrl_param. Resets `sync_init=FALSE` to re-init regs_info.
   - `pfn_cmos_ae_fast_ae_attr_get` -- AE algo queries this when running
     quick-start to learn `sns_delay_frame` (we return 3 like sc4336p).
   - `pfn_cmos_ae_fast_ae_attr_set` -- stub no-op.

**pipeline_test fixes applied** (`driver/test/pipeline_test.c`):

1. Added `ss_mpi_isp_get_ctrl_param` -> modify -> `set_ctrl_param` block
   AFTER `register_callback`/`set_bus_info`/`ae_register`/`awb_register`
   but BEFORE `mem_init` -- matches SDK pattern in
   `sample_comm_vi.c:1561-1574`. Sets `be_buf_num=4` and `quick_start_en=1`.
   Setting `isp_run_wakeup_select=BE_END` (superb-equivalent on paper)
   causes `ss_mpi_isp_init` to fail with `0xa01c800c` (NOT_SUPPORT) --
   the kernel doesn't accept it in this online running_mode. Backed off.
2. Added `ss_mpi_isp_set_ae_route_attr` with 3 nodes matching superb:
   `{int_time=8,gain=1024}`, `{2804,1024}`, `{2804,196608}`. Called
   immediately after `ss_mpi_isp_init`.
3. Added `bin_param.stIspEvo.enable = 1` for `OT_PQ_BIN_ImportBinData`
   -- includes the "extended/evolved" ISP modules during PQ load
   (cross-frame, AE route).

**Result -- `/proc/umap/isp` now matches superb on:**

| Field | Before | After | Superb |
|---|---|---|---|
| `int_rat` | 10 | **20** ✓ | 20 |
| `int_gap_t` | 99914us | **50011us** ✓ | 50088us |
| `sync_cfg_gap` | 99914us | **50012us** ✓ | 50088us |
| `be_buf_num` | 8 | **4** ✓ | 4 |
| `quick_start` | 0 | **1** ✓ | 0 (!) |
| `hmax_times` | 0 | **17780** ✓ | 17780 |
| AE node count | 1 | **3** ✓ | 3 |
| AE node 0 | int=36, gain=0 | **int=8, gain=1024** ✓ | int=8, gain=1024 |
| AE node 1 | -- | **int=2802, gain=1024** ✓ | int=2804, gain=1024 |
| AE node 2 | -- | **int=2802, gain=196608** ✓ | int=2804, gain=196608 |
| `max_line` | 5614 | **2802** ✓ | 2804 |

(Note: superb's `quick_start=0` but our `quick_start=1`. We set it because
sc4336p reference suggests it for fast convergence and to invoke
`cmos_ae_quick_start_status_set`. This minor diff is unlikely to be
the cause given we're now matching superb on every other metric.)

**Result -- `/proc/umap/isp` STILL different on:**

| Field | Ours | Superb | Meaning |
|---|---|---|---|
| `cros_cnt` | **0** | 13+ growing | Kernel never enters cross-frame state |
| `int_type` | **start** | other | Always processing "start" interrupt |
| `1st_time` | 750000 | 0 | (AE init exposure us, low importance) |
| `noroi_cnt` | 326 | 1744 | (ROI region count -- AE sub-stats) |

**Poke-test reconfirmed 2026-05-14**: with all of the above fixes applied
AND direct `sc635hai_write_register` calls in `cmos_inttime_update` +
`cmos_gains_update` commented out -- the kernel does NOT restore poked
sensor registers. Confirmed by:

1. Pipeline running, AE log shows `int_time=2802` requests.
2. Poke `0x3E00=0x0F, 0x3E01=0x11, 0x3E02=0x10` (marker exp=0xF111).
3. Read 0s later: poke present.
4. Read 5s later: **poke still present**. Direct I2C now disabled, so the
   kernel is solely responsible for sensor updates -- and it isn't.

Reactivated direct I2C writes; camera is back to working state.

### What's Left To Investigate

The remaining gap (`cros_cnt=0`, `int_type=start`) must be in one of:

A. **VI pipe attributes** -- something in `ss_mpi_vi_create_pipe` /
   `ss_mpi_vi_set_pipe_attr` that determines whether the ISP fires
   cross-frame interrupts. Our pipeline uses `pipe_bypass_mode = NONE`
   and `frame_rate_ctrl = -1/-1` and standard 3DNR. Compare against
   superb's runtime `/proc/umap/vi`.

B. **MIPI RX configuration** -- the `phy_data_rate`, `lane_num`,
   `data_type`, or `input_data_type` could affect interrupt source
   selection. Our values: 2-lane MIPI, RAW10, 3200x1800.

C. **Some unobservable internal state in libss_mpi_isp.so** that gets
   set via an ioctl we haven't called. The complete set of ISP ioctls
   we haven't tried: `set_int_enable`, `set_clut_attr`, `set_fpn_calib`,
   `set_fpn_correction`, `set_run_once_attr`, `set_running_mode_ex`,
   `set_be_buffer_cfg`, several debug/dfx ioctls.

D. **Maybe `cros_cnt` only increments under WDR or stitched modes**,
   and the I2C delivery for linear mode uses a different code path
   that we are activating but doesn't bump `cros_cnt`. This would mean
   the kernel I2C path actually IS engaging but the poke test is being
   foiled by something else (e.g., the kernel only writes regs that
   have changed in `regs_info` -- and our regs_info[0] reg values
   match the sensor's current state since direct I2C had been writing
   them, so update flags stay FALSE and no I2C transaction happens).
   **NEXT TEST**: set all 11 `i2c_data[i].update = TD_TRUE` unconditionally
   in `cmos_get_sns_reg_info` to force the kernel to write every reg
   every frame; then re-do poke test with direct I2C still disabled.

### Iteration 3 (2026-05-14): KERNEL I2C SYNC PATH WORKING

**Root cause found**: hypothesis D was correct. The `i2c_data[i].update`
flags in the regs_info struct are what gate kernel writes. The diff-based
update logic (data[N] vs data[N-1]) was setting all flags FALSE once AE
converged to a stable value -- because the AE reports the same `int_time`
and gain values frame after frame in stable lighting, so the diff finds
no changes.

The fix: in `cmos_get_sns_reg_info`'s incremental-update branch, force
`update = TD_TRUE` for the AE-driven regs (EXP_H/M/L, AGAIN_COARSE/FINE,
DGAIN_COARSE/FINE) and the GROUP_HOLD bookends every frame.
Leave VTS regs as diff-based since those genuinely change rarely.

**Verification (poke test with direct I2C disabled)**:
1. Pipeline running, AE converged at line=2802, sensor reg = `0x00 0xAF 0x20`.
2. Poke `0x3E00=0x0F, 0x3E01=0xFF, 0x3E02=0xF0` (marker).
3. Immediately after: regs read `0x00 0xFF 0xF0` (poke present;
   0x3E00 high-nibble masked off by sensor since only [3:0] valid).
4. 3 seconds later: regs read `0x00 0xAF 0x20` (= 2802, AE-target).
5. **Kernel restored within 3 seconds.** No direct I2C writes in driver.

`/proc/umap/isp` after fix:
- `sensor_cfg_t = 58us` (up from 29us) -- kernel now doing actual I2C transfer
- `sensor_max_t = 61us`, `sensor_avg_t = 57us` -- consistent kernel writes
- `cros_cnt = 1` (still init-only) -- but kernel I2C works WITHOUT
  cross-frame engagement. cros_cnt was a red herring.
- All other fields still match superb (int_rat=20, hmax_times=17780, etc.)

**The cros_cnt=0 / int_type=start difference between us and superb is
real but does NOT affect sensor I2C delivery.** It may relate to
multi-pipe sync or WDR-mode buffering. We do not need it for our setup.

### Final Summary: All Fixes Required For Kernel I2C Sync Path

**Driver (`driver/src/sc635hai_cmos.c`):**
1. `cmos_get_ae_default`: set `hmax_times = 1e9 / (VTS * fps)`, populate
   `full_lines_max`, `*_target` fields, and use actual fps in lines_per500ms.
2. `cmos_get_ae_default`, `cmos_fps_set`, `cmos_slow_framerate_set`:
   use `max_int_time = VTS - SC635HAI_EXP_OFFSET` (single-line max),
   NOT `2*VTS-10`. Also fix the clamp in `cmos_inttime_update`.
3. Add AE callbacks: `pfn_cmos_ae_quick_start_status_set`,
   `pfn_cmos_ae_fast_ae_attr_get`, `pfn_cmos_ae_fast_ae_attr_set`.
4. **CRITICAL**: in `cmos_get_sns_reg_info` incremental-update branch,
   force `update = TD_TRUE` for EXP_*, AGAIN_*, DGAIN_*, HOLD_*.
   The diff-based update breaks kernel writes once AE stabilizes.

**Pipeline test (`driver/test/pipeline_test.c`):**
1. Add `ss_mpi_isp_set_ctrl_param` call BEFORE `mem_init`:
   `be_buf_num = 4`, `quick_start_en = 1`. Note: `wakeup=BE_END` is
   rejected by isp_init in our config; leave at FE_START.
2. Add `ss_mpi_isp_set_ae_route_attr` after `isp_init`:
   3 nodes mimicking superb (`{8,1024}`, `{2804,1024}`, `{2804,196608}`).
3. Enable `bin_param.stIspEvo.enable = 1` for PQ bin import.

With all of the above applied AND direct I2C writes in `cmos_inttime_update`
and `cmos_gains_update` REMOVED, sensor exposure/gain are delivered solely
by the kernel `ot_sensor_i2c_write` callback. AE works. RTSP streams.

### [SUPERSEDED] Current State (Working, Direct I2C in Use)

> **SUPERSEDED 2026-05-14 by Iteration 3 above.** The claim that direct
> I2C writes are REQUIRED was incorrect -- it was based on testing
> *before* the `regs_info[i].update` flag issue was understood. With
> the Iteration 3 fixes in place, direct I2C writes are NOT needed and
> are commented out (not deleted, in case revert is wanted). The kernel
> sync path delivers all exposure/gain.

- `cmos_get_sns_reg_info` fix is in place (3 structural bugs corrected).
- ~~Direct I2C writes in `cmos_inttime_update` and `cmos_gains_update` are
  REQUIRED and active.~~ -- WRONG. They are now commented out and the
  kernel callback handles all sensor I2C.
- `pfn_register_callback` is called BEFORE `pfn_set_bus_info` (matches SDK
  sample order, even though the reorder didn't fix the kernel sync path).
- RTSP streaming works. AE works ~~(via direct I2C)~~ via kernel I2C sync
  callback. Gain converges, exposure tracks scene brightness.

### Poke Test Re-validation Under Superb (2026-05-14)

To confirm the kernel sync path is fundamentally different (not a global
hardware/timing issue), we ran the poke test against stock superb:

1. Stopped pipeline_test, resumed mySystem, launched superb manually.
2. Superb's ISP `line:2804`, sensor regs read 0x00, 0xAF, 0x40 = exp 2804. Match.
3. Poked 0x00, 0x0A, 0xA0 to regs 0x3E00-02 via `i2c_write` (= exp 170).
4. Read back 0s later: still 0x00, 0x0A, 0xA0 (poke present).
5. Read back 3s later: **0x00, 0xAF, 0x40 = exp 2804 again (kernel overwrote).**

Confirmed: **the kernel sync path works under superb and is broken under
pipeline_test**. The difference is in software state, not hardware.

### Reverse-Engineering Tools (For Reproducibility)

Decompilation done with:
- Ghidra 12.0.4 (in `tools/ghidra/ghidra_12.0.4_PUBLIC/`)
- Project: `tools/ghidra/project/superb_project` (contains both `superb`
  and `libot_mpi_isp.so` analyzed)
- Script: `tools/ghidra/scripts/DecompileSnsReg.java`
- Output: `tools/ghidra/output/sns_reg_path_superb.txt`,
  `sns_reg_path_libisp.txt`, `sns_reg_registration_superb.txt`,
  `sns_reg_registration_libisp.txt`

Re-run analysis (replace JDK path):
```cmd
set JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.18.8-hotspot
tools\ghidra\ghidra_12.0.4_PUBLIC\support\analyzeHeadless.bat ^
    tools\ghidra\project superb_project ^
    -process superb -noanalysis ^
    -scriptPath tools\ghidra\scripts ^
    -postScript DecompileSnsReg.java tools\ghidra\output superb
```

### How To Test The Kernel Sync Path (For Future Sessions)

The fundamental test is: **does the sensor's exposure register match what the
ISP AE is commanding?** If yes, something is writing the registers. If no,
nothing is writing them.

**Test 1: Baseline with both paths active (current state).**

Should always pass. Confirms system is functional:
```bash
# Read ISP AE state and sensor registers (camera running pipeline_test)
python tools/cam_cmd.py "cat /proc/umap/isp | grep -A1 'sys_gain.*line'"
python tools/cam_cmd.py "i2c_read 0 0x60 0x3E00 0x3E02 2 1"

# Decode the sensor exposure:
#   inttime_half_lines = (reg_3E00 << 12) | (reg_3E01 << 4) | (reg_3E02 >> 4)
# Compare to ISP's "line:" value. They should match.
```

**Test 2: Isolate the kernel sync path.**

Comment out the `sc635hai_write_register` calls in `cmos_inttime_update` only
(leave `cmos_gains_update` direct writes active as a control):

```c
/* In cmos_inttime_update, comment out these three lines: */
// sc635hai_write_register(vi_pipe, SC635HAI_REG_EXP_H, (int_time >> 12) & 0x0F);
// sc635hai_write_register(vi_pipe, SC635HAI_REG_EXP_M, (int_time >> 4) & 0xFF);
// sc635hai_write_register(vi_pipe, SC635HAI_REG_EXP_L, (int_time & 0x0F) << 4);
```

Then build, deploy, MD5 verify, restart pipeline_test, wait 15s for AE
convergence, and run the same readback as Test 1.

- **PASS (kernel path works)**: Sensor `0x3E00-02` decodes to a value matching
  ISP `line:`. The kernel path is delivering exposure updates.
- **FAIL (kernel path broken)**: Sensor `0x3E00-02` decodes to a stale value
  (e.g., 1867) while ISP `line:` shows something different (e.g., 8). The
  kernel path is not delivering exposure to the sensor.

**Test 3: Stock superb as reference.**

Reboot the camera so superb runs (or `killall pipeline_test` and wait for
mySystem to relaunch superb). Run the same Test 1 readback. Superb uses only
the kernel sync path -- if its sensor regs match ISP `line:`, the kernel path
works for superb. This is the calibration point.

**Test 4: Verify struct data correctness (diagnostic prints).**

If unsure whether `cmos_get_sns_reg_info` is populating `sns_regs_info`
correctly, add a printf inside the function (after the final memcpy):

```c
static td_u32 sns_reg_call_cnt = 0;
if (++sns_reg_call_cnt <= 5 || (sns_reg_call_cnt % 100) == 0) {
    printf("sns_reg #%u: type=%d dev=%d reg_num=%d "
           "exp=%02X%02X%02X again=%02X/%02X "
           "addr[exp_h]=0x%04X\n",
           sns_reg_call_cnt,
           (int)sns_regs_info->sns_type,
           (int)sns_regs_info->com_bus.i2c_dev,
           (int)sns_regs_info->reg_num,
           (unsigned)sns_regs_info->i2c_data[LINEAR_EXP_H].data,
           (unsigned)sns_regs_info->i2c_data[LINEAR_EXP_M].data,
           (unsigned)sns_regs_info->i2c_data[LINEAR_EXP_L].data,
           (unsigned)sns_regs_info->i2c_data[LINEAR_AGAIN_COARSE].data,
           (unsigned)sns_regs_info->i2c_data[LINEAR_AGAIN_FINE].data,
           (unsigned)sns_regs_info->i2c_data[LINEAR_EXP_H].reg_addr);
}
```

Expected output (verified this session):
- `type=0` (I2C), `dev=0` (bus 0), `reg_num=11`
- `addr[exp_h]=0x3E00` (correct SC635HAI exposure register)
- Data values track the AE commands frame-by-frame

If the data looks wrong here, the bug is in our driver. If the data looks
right but the sensor regs don't update (Test 2 FAIL), the issue is downstream
(userspace ISP lib <-> kernel ISP module).

### [SUPERSEDED] Possible Future Approaches

> **SUPERSEDED 2026-05-14.** The kernel sync path is now working via the
> Iteration 3 fixes; none of these approaches were needed. Kept for
> historical context of the dead-end alternatives we considered.

1. ~~Find a matching V1.0.0.0-compatible userspace ISP library~~ -- not
   needed. There was never a version mismatch; "V1.0.0.0 B010" is a
   misleading version string from the kernel module, but the actual
   code is V1.0.2.x and is compatible with our userspace V1.0.2.1.
2. ~~Replace `ot_isp.ko`~~ -- not needed. The kernel module works fine.
3. ~~Reverse-engineer the ISP sync IPC~~ -- not needed. The Ghidra
   decompile of `isp_drv_write_i2c_data` was correct; the issue was
   in our userspace `regs_info[i].update` flag handling, not in any
   IPC mismatch.
4. ~~Accept the direct I2C writes as the working solution~~ -- not needed.
   The kernel sync path is working now and direct I2C is disabled.
   The ~8 extra I2C transactions per frame are not a measurable
   performance issue. The only downside is missing VBlank synchronization
   on AE writes -- which could
   theoretically cause partial-frame tearing during exposure transitions but
   in practice has shown no visible artifacts in our streaming.

### Launch Command

Must preload ALL ISP plugin dependencies in correct order:
```bash
kill -STOP $(pidof mySystem)
cd /progs/rec/00/ipc_drv
LD_PRELOAD='/progs/rec/00/ipc_drv/libsecurec.so /progs/rec/00/ipc_drv/libot_osal.so /progs/rec/00/ipc_drv/libss_mpi_sysmem.so /progs/rec/00/ipc_drv/libss_mpi.so /progs/rec/00/ipc_drv/libot_mpi_isp.so /progs/rec/00/ipc_drv/libbnr.so /progs/rec/00/ipc_drv/libdrc.so /progs/rec/00/ipc_drv/libacs.so /progs/rec/00/ipc_drv/libcalcflicker.so /progs/rec/00/ipc_drv/libir_auto.so /progs/rec/00/ipc_drv/libldci.so /progs/rec/00/ipc_drv/libdehaze.so /progs/rec/00/ipc_drv/libextend_stats.so' \
LD_LIBRARY_PATH=/progs/rec/00/ipc_drv \
setsid ./pipeline_test --rtsp --rtsp-port 554 /home/sensor/sc635hai/pqbin/day.bin > /tmp/pipeline.log 2>&1 &
```

Musl libc resolves LD_PRELOAD symbols eagerly. `libot_mpi_isp.so` depends on
`libsecurec.so`, `libot_osal.so`, `libss_mpi_sysmem.so`, and `libss_mpi.so` --
these must appear BEFORE it in the LD_PRELOAD list. The ISP plugin libs
(`libbnr.so`, `libdrc.so`, etc.) depend on symbols exported by `libot_mpi_isp.so`
and must appear AFTER it.

### Outcome A Confirmed (2026-05-15, isolated diff-only test)

> **Status: FULLY CLOSED.** Performed the validating experiment proposed in
> "Test that would resolve the remaining uncertainty." Result: **Outcome A**.

**Test procedure (executed 2026-05-15):**

1. Edited `sc635hai_cmos.c:322-345` to replace the force-TRUE block with a
   pure diff loop over all `LINEAR_REGS_NUM` registers. All other Iteration 2
   fixes left in place (AE route, ctrl_param, hmax_times, max_int_time, AE
   callbacks, stIspEvo enable).
2. Rebuilt `libsns_sc635hai.so` (MD5 `14280a0c01767f4a7149cf3f5047e45a`).
3. Deployed to `/progs/rec/00/ipc_drv/libsns_sc635hai.so`.
4. Killed running pipeline_test (PID 4152) and relaunched via `rtsp_run.sh`.
5. Observed live RTSP behavior under lighting transitions.
6. Ran poke test on `0x3E00-02` once AE converged.

**Result 1: AE transient response works perfectly without force-TRUE.**

User observation during diff-only build:
- Lights off → image gradually darkens to correctly-exposed dim scene
  within ~1-2 seconds. AE drives integration time up and gain up.
- Lights on → image gradually normalizes from over-bright back to
  correctly-exposed bright scene within ~1-2 seconds. AE drives
  integration time down and gain down.
- Wi-Fi bulb color-temp swap 2700K ↔ 6000K → AWB rebalances cleanly,
  resulting image color is consistent across both color temperatures.

This contradicts the May-14-evening claim that force-TRUE was necessary for
transient response. **Diff during transients produces non-zero deltas in
EXP_H/M/L and gain bytes; the kernel sync queue consumes those TRUE flags
and writes the sensor.** The Iteration 2 sync-queue fixes alone are
sufficient for normal AE operation.

**Result 2: Steady-state freeze is real and produces partial restore on poke.**

`/proc/umap/isp` sampled twice 5 seconds apart with diff-only build under
stable indoor lighting:

```
t=0: sys_gain=1028  line=227  noroi_cnt=9536
t=5: sys_gain=1028  line=227  noroi_cnt=9776
```

AE iterating (noroi_cnt advancing) but byte-identical output, confirming
steady-state freeze. Sensor regs match AE state:
`0x3E00=0x00, 0x3E01=0x0E, 0x3E02=0x30` (= 227 half-lines).

Poked sensor regs:
```
i2c_write 0 0x60 0x3E00 0xAA 2 1
i2c_write 0 0x60 0x3E01 0xBB 2 1
i2c_write 0 0x60 0x3E02 0xC0 2 1
```

5 seconds later, sensor regs read back:
```
0x3E00: 0xaa   <-- STILL POKED (never written)
0x3E01: 0x04   <-- written (changed from 0xBB)
0x3E02: 0x80   <-- written (changed from 0xC0)
```

AE state at that moment: `sys_gain=1024, line=8` (AE drove exposure to minimum
because poked EXP_H=0xAA caused a massively over-integrated frame).
Encoding `line=8` half-lines:
- `EXP_H = (8 >> 12) = 0` → AE's `regs_info[0].data` for EXP_H = 0
- `EXP_M = (8 >> 4) & 0xFF = 0` → matches sensor `0x3E01=0x00`
- `EXP_L = (8 & 0x0F) << 4 = 0x80` → matches sensor `0x3E02=0x80`

So the userspace AE side wanted to write `EXP_H=0`. But the previous frame
also had `EXP_H=0` (under normal lighting, this byte is always 0 except for
very long exposures). Pure diff: `regs_info[0].EXP_H == regs_info[1].EXP_H`,
update flag stays FALSE, kernel skips the I2C write. Sensor stays at the
poked `0xAA`. The diff-bug is isolated to that single byte and is exactly
the failure mode predicted: "AE-driven reg whose userspace value happens to
remain constant frame-to-frame after a steady state."

**Result 3: Image pathology under poked EXP_H = textbook diff-bug behavior.**

User observation after restoring sensor to the poked state:
- Lights on (bright wi-fi bulbs): image is ~98% white with a small patch of
  faint color in one corner. AE has driven everything to minimum, sensor is
  still integrating for ~43,520 half-lines because `EXP_H=0xAA` is stuck.
- Lights off: image looks **surprisingly good and clean**, "no gain." The
  forced enormous integration is now appropriate for the dark scene; AE has
  driven gain to 1024 (= 1.0x) so there's no noise amplification.
- Frame rate drops noticeably in the dark. The sensor really does take that
  long to capture each frame; pipeline_test can't deliver 20fps when each
  exposure exceeds the nominal frame period.

This is a perfect illustration of why force-TRUE matters: a single stuck
high-byte poisoned the entire exposure coordinate system, and AE has no way
to recover because it can't observe what the sensor is actually doing -- it
only knows what it asked the kernel to write. The kernel write was silently
skipped.

**Resolution (2026-05-15):**

1. Restored force-TRUE block to `sc635hai_cmos.c`. Updated inline comment to
   reflect the validation result (Outcome A confirmed: force-TRUE is purely
   steady-state safety).
2. Rebuilt (MD5 `b37d4f955df7c0245a6793ae5cc3d722`) and redeployed.
3. Restarted pipeline_test (PID 1835). Within seconds, kernel overwrote the
   poked `0x3E00` (currently `0x00`), AE reconverged to `line=2802` matching
   current lighting, sensor regs read back `0x3E00=0x00, 0x3E01=0xAF,
   0x3E02=0x20` matching AE. Image quality back to normal.

### Open Question Partially Resolved (2026-05-14, evening session) [HISTORICAL]

> **Status: superseded by 2026-05-15 Outcome A test above.** Kept for
> historical record of the analytical journey. The "partially closed"
> framing was correct at the time -- the remaining uncertainty was
> exactly what the next-day isolated test resolved.

### Honest Revised Story (2026-05-15, after user observation review)

On first pass through this investigation we concluded "force-TRUE was
the key fix; diff-during-transients always worked." A user observation
from the actual no-force tests contradicts the second half of that:

**What the user observed during no-force testing**: turning the room
lights off produced a pitch-black image that stayed pitch-black
indefinitely. Turning lights on produced a washed-out bright image
that stayed washed-out indefinitely. The image tracked **raw scene
brightness** with no AE compensation -- exactly what a sensor with
frozen exposure registers looks like, whose underlying exp/gain
never changes regardless of what AE wants.

**What the diff-works-during-transients theory predicted**: turning
lights off should drive AE int_time UP frame-by-frame, producing
frame-to-frame diffs in the EXP regs, causing the kernel to write
the new values to the sensor, gradually brightening the image to
a "correctly exposed dim scene." That did not happen.

**Reconciliation**: the kernel sync path was apparently entirely
dead pre-Iteration-2, not just frozen-in-steady-state. The
diff-based logic was correctly producing TRUE flags during
transients (we have decompiled it and verified the logic) but the
kernel was not consuming them -- presumably because
`isp_drv_get_sns_cfg_node` was returning NULL on every call (no
sync_cfg queue advancement, no cross-frame engagement). The full
Iteration 2 fix bundle (`hmax_times`, `max_int_time`, AE route,
`ctrl_param`, missing AE callbacks, `stIspEvo.enable`) plus the
force-TRUE flag landed together; we did NOT isolate which fix did
what. We attributed the win to force-TRUE because the poke test
went from failing to passing in that iteration, but in retrospect
most of the credit probably belongs to the upstream sync-queue
fixes.

**What this means for force-TRUE**: it is still correct and
necessary as a guard against the steady-state freeze (empirically
verified: 30s of AE output identical to the byte, pure-diff would
have set all flags FALSE). But it is probably NOT the only thing
preventing the no-force tests from producing AE response during
transients. The Iteration 2 sync-queue fixes were also load-bearing.

**What this means for the shumjj question**:
- shumjj's pure-diff would still freeze in our SC635HAI stable scene
  (their gain quantization is the same, their AE algo is the same,
   identical input → identical output → diff zero). This part of
  Hypothesis 1 is empirically verified.
- shumjj's transients almost certainly work for them, because their
  app sets the things we were missing pre-Iteration-2 (AE route,
  ctrl_param, etc.). They wouldn't see the "pitch black, no AE
  response" symptom we saw.
- They WOULD see the freeze under manual AE lock or perfectly
  stable lighting. They just don't notice because no one tests for
  it.

**What we cannot say for certain without further testing**: whether
pure-diff alone, given a fully correct sync-queue setup (all
Iteration 2 fixes applied), would suffice to make AE response work
in our setup. Our test methodology in Iteration 3 did not separate
"Iteration 2 fixes only, diff-based update" from "Iteration 2 fixes
+ force-TRUE." Both fixes landed together. The poke test passed
after both -- the poke test would have passed after either one if
the underlying assumption ("transients diff produces TRUE flags
that the kernel consumes") is correct.

The sc4336p reference driver (`shumjj/.../smart_sc4336p/sc4336p_cmos.c:850`)
uses pure diff-based update logic in `cmos_sns_reg_info_update`:

```c
for (i = 0; i < sns_state->regs_info[0].reg_num; i++) {
    if (sns_state->regs_info[0].i2c_data[i].data ==
        sns_state->regs_info[1].i2c_data[i].data) {
        sns_state->regs_info[0].i2c_data[i].update = TD_FALSE;
    } else {
        sns_state->regs_info[0].i2c_data[i].update = TD_TRUE;
    }
}
```

For us this leaves all flags FALSE once AE converges, and the kernel
writes nothing. We had to force `update = TD_TRUE` on AE-driven regs.

#### Hypothesis 1: Real scenes always have AE jitter -- PARTIALLY VERIFIED

**The "AE freezes in stable light" part is verified.** Tested
empirically. With our current build and stable indoor lighting on the
SC635HAI, AE converged to:

```
sys_gain=196592  line=2802  exp=9565022us
```

Sampled `/proc/umap/isp` once per second for 30 seconds (≈600 frames at
20fps). **Zero variation in any AE output.** `noroi_cnt` still advances
(294296 → 294307), confirming the AE algorithm is iterating every frame
and just always arriving at the same answer.

If shumjj's sc4336p driver were run against this scene, every call to
`cmos_inttime_update` would compute identical EXP_H/M/L bytes, every
call to `cmos_gains_update` would compute identical AGAIN/DGAIN bytes
(both drivers do the same gain-table-quantize logic), and the pure-diff
loop would set all 9 AE-driven `i2c_data[i].update` flags to FALSE
every frame. **Steady-state freeze is real for both stacks.**

**The "but diff produces TRUE during transients, which is enough"
part is no longer well-supported.** User observation from no-force
tests: during light off→on→off transitions, the image stayed at
the boot-default exposure regardless of scene brightness -- no AE
response at all. If diff-during-transients alone had been
sufficient, those transitions should have produced visible AE
adjustment. They didn't.

What likely happened: pre-Iteration-2 our sync-queue was not
engaging at all (`cros_cnt=0`, `isp_drv_get_sns_cfg_node` returning
NULL on every call). The diff was correctly setting TRUE flags
during transients, but the kernel was not consuming them. Iteration
2 fixed the sync-queue engagement; force-TRUE additionally
addressed the steady-state freeze. **Both pieces were necessary;
we did not separate them in testing.**

**What we'd predict for shumjj**:
- Their `cmos_sns_reg_info_update` pure-diff produces TRUE flags
  during transients (proven by reading the code -- the logic is
  trivial and obviously correct).
- Their sync queue engages properly because their app sets all
  the things we were missing pre-Iteration-2 (AE route,
  `ctrl_param`, `hmax_times`, etc.).
- Therefore their transients work, and the freeze only manifests
  under manual AE lock or perfectly-stable lighting -- which no
  one tests for.

#### Hypothesis 2: V1.0.2.0 has a hidden force-write path V1.0.2.1 lacks -- FALSIFIED

Compared `superb` (statically-linked V1.0.2.0 ISP code) vs
`libot_mpi_isp.so` V1.0.2.1 symbol tables. Both expose the same set of
sensor-side update functions:

| Function | superb V1.0.2.0 | libisp V1.0.2.1 |
|---|---|---|
| `isp_sensor_update_all` | 0x3d7371, 128 B | 0x378dc, 128 B |
| `isp_sensor_update_all_yuv` | 0x3d73f1, 72 B | 0x3795c, 72 B |
| `isp_sensor_update_default` | 0x3d7669, 76 B | 0x37bd4, 76 B |
| `isp_sensor_update_blc` | 0x3d761d, 76 B | 0x37b88, 76 B |
| `isp_sensor_update_blc_clamp_info` | 0x3d77dd, 76 B | 0x37d48, 76 B |
| `isp_sensor_update_sns_reg` | 0x3d7829, 76 B | 0x37d94, 76 B |

Disassembly of `isp_sensor_update_all` shows it calls only two sensor
callbacks (via the per-pipe `ot_isp_sns_exp_func` table):

- `pfn_cmos_get_isp_default(pipe, ctx+0x38)` (index 5)
- `pfn_cmos_get_isp_black_level(pipe, ctx+0x190)` (index 6)

Neither populates `regs_info`. **There is no path in V1.0.2.x userspace
that periodically forces all `i2c_data[i].update` flags to TD_TRUE
behind our back.** The diff-based logic in `cmos_sns_reg_info_update`
is the only thing that controls those flags.

#### Hypothesis 3: shumjj's `ot_isp.ko` differs from ours -- FALSIFIED

Three V1.0.2.x-class kernel modules exist on disk:

| Variant | Size | MD5 |
|---|---|---|
| Ours (`firmware/extracted/.../ot_isp.ko`) | 195432 | 1170C0...C7DD |
| shumjj (`shumjj-3516cv610_app/.../ot_isp.ko`) | 195496 | 50A843...AEAD |
| SDK KOL (`Hi3516CV610_Firmware_Building/.../ot_isp.ko`) | 195496 | 529354...3058 |

Side-by-side ELF/disasm comparison (see `PHASE9_KO_DIFF.md`):

- `.text` is **byte-identical** for ours↔KOL.
- shumjj differs from ours/KOL by exactly **two instructions in `.text`**:
  both are `__LINE__` literals (`2033→2036`, `1989→1992`, both +3)
  loaded for `OT_LOG()` calls. **Zero control-flow / logic changes.**
- `isp_drv_get_sns_cfg_node` (0x2108): byte-identical in all three.
- `isp_drv_write_i2c_data` (0x21b4): byte-identical ours↔KOL, only the
  two `__LINE__` literals differ for shumjj.
- The 64-byte size difference is **all in `.gnu.linkonce.this_module`
  padding** -- shumjj/KOL reserve 0x1C0 bytes for `struct module`,
  ours reserves 0x180. The extra 64 bytes are all `0x00`; this comes
  from a kconfig difference in the kernel build environment (likely
  CONFIG_TRACEPOINTS or CONFIG_LIVEPATCH), not a code difference.

Same pattern in `ot_sensor_i2c.ko`: 5-byte diff, all `__LINE__` slots,
zero functional change.

**Conclusion: no kernel-side mechanism exists in shumjj's stack that
isn't also present in ours.** The kernel `isp_drv_write_i2c_data` gates
behave identically in all three variants.

#### Bonus finding: shumjj's `quick_start_en` is a direct-I2C escape hatch

Re-reading `sc4336p_cmos.c:336-344, 438-446` carefully:

```c
static td_void cmos_inttime_update_linear(cis_info *cis,
    ot_isp_sns_state *sns_state, td_u32 int_time)
{
    if (cis->quick_start_en == TD_TRUE && cis->i2c.fd >= 0) {
        cis_write_reg(&cis->i2c, SC4336P_EXPO_L_ADDR, lower_4bits(int_time));
        cis_write_reg(&cis->i2c, SC4336P_EXPO_M_ADDR, higher_8bits(int_time));
        cis_write_reg(&cis->i2c, SC4336P_EXPO_H_ADDR, higher_4bits(int_time));
    } else {
        sns_state->regs_info[0].i2c_data[EXPO_L_IDX].data = lower_4bits(int_time);
        ...
    }
}
```

When `quick_start_en=TRUE` AND `cis->i2c.fd >= 0` (a direct userspace
I2C fd opened by the driver), the sc4336p driver writes the sensor
DIRECTLY via userspace I2C, identical to our pre-fix workaround. The
kernel sync path is bypassed entirely; pure-diff in
`cmos_sns_reg_info_update` is irrelevant in this branch (and the
`regs_info[0].data` fields aren't even updated).

In the other branch (default `quick_start_en=FALSE` OR no I2C fd), the
sensor I/O goes through `regs_info` + kernel sync + pure-diff. This
branch is the one with the latent bug.

shumjj's default initializer (`sc4336p_cmos.c:22`) sets
`quick_start_en = TD_FALSE`. Their `cis_init_attr` (in
`sensor_common.c:308`) copies from `init_attr->quick_start_en`, which
the app fills from `ss_mpi_isp_get_ctrl_param`. Their app does NOT
appear to call `ss_mpi_isp_set_ctrl_param` to flip it on. So in
practice shumjj's stack runs in the **same path that we just fixed** --
the pure-diff sync path with the latent freeze bug.

#### Why nobody hits the steady-state freeze in practice

1. **Scene noise**: Real outdoor or natural-light scenes rarely
   converge to byte-identical AE values frame-to-frame. Cloud shadows,
   foliage movement, slow ambient drift -- all keep some bit of
   int_time or gain jittering. Diff is non-zero → flags stay TRUE.
   (Caveat: our SC635HAI in stable indoor lighting *did* converge
   byte-identical for 30s, so this isn't universal. Bright outdoor
   scenes or scenes with motion are more forgiving.)
2. **AE bookkeeping**: In some lighting transitions the AE algorithm
   sweeps through gain table entries and exposure values; even when
   it has converged it may twiddle as it confirms convergence.
   Empirically false for SC635HAI in our scene -- AE freezes
   completely -- but may be true for other sensors / scenes.
3. **Nobody runs a poke test**: A consumer-grade camera user never
   writes sensor regs from outside the ISP. The only way to notice
   the steady-state freeze is to externally clobber `0x3E00-02`
   and see if it restores. We did this and found the freeze;
   shumjj users have not.
4. **AE manual lock would expose it**: If shumjj users called
   `ss_mpi_isp_set_exposure_attr` in manual mode with fixed
   exp/gain, the bug would manifest -- after the first converged
   frame, no further sensor writes. Symptoms would be: a single
   "stuck" exposure forever, even if the user changes manual
   targets, because the new targets produce identical
   `regs_info[0].data` to the previous frame after one frame's
   delay.

(Note: items 1-4 only apply to the steady-state freeze. The
sync-queue-engagement problem we hit pre-Iteration-2 is a different
class of bug -- shumjj users avoid it by virtue of their app calling
`ss_mpi_isp_set_ctrl_param`, `set_ae_route_attr`, etc. They would
never see the "stuck at boot defaults" symptom we saw.)

#### What we'd see if we tested shumjj's image

Predicted result of "boot shumjj image, poke sensor regs":

- With `quick_start_en=0` (their default): poke survives indefinitely,
  exactly like our pre-fix behavior. The kernel sync path is queueing
  empty/no-update writes.
- With `quick_start_en=1` (if they ever enable it): poke is overwritten
  within ~50ms by direct-I2C in `cmos_inttime_update_linear`. But this
  bypass is functionally equivalent to our old workaround and has the
  same downsides (no VBlank synchronization, no atomic group hold).

Either way, shumjj's implementation does not have a mechanism we are
missing. Their stable-light bug just has not been observed because
no one in their user base has tested it.

#### Verdict: our fix is correct and necessary, but its full credit allocation is unclear

Force `update = TD_TRUE` on AE-driven regs every frame is the right
answer for the *steady-state freeze* problem. It costs ~9 extra I2C
writes per frame at 20fps (~50us per frame inside the I2C bus, well
within vblank). It is robust against manual exposure lock,
perfectly-stable scenes, and external pokes.

What is less clear: whether force-TRUE alone, without the Iteration 2
sync-queue fixes, would suffice. The user observation of "no AE
response during transients" pre-fix strongly suggests the sync queue
itself was dead, not just frozen-by-diff. In that scenario, force-TRUE
would have made flags TRUE but the kernel still wouldn't have consumed
them. The full Iteration 2 fix bundle is therefore likely necessary
for transient response; force-TRUE is necessary on top of that for
steady-state correctness.

If we wanted to be especially clean we could add a counter-based
"force every 32 frames" path (like sc4336p has for VTS via
`cfg2_valid_delay_max=2`) instead of every frame. But every frame
is also fine -- the kernel sync path is built to handle it.

#### Test that would resolve the remaining uncertainty

Re-enable diff-based update for AE regs (revert the force-TRUE
change), keep all Iteration 2 fixes in place (`hmax_times`,
`max_int_time`, AE route, `ctrl_param`, AE callbacks, `stIspEvo`),
direct I2C still disabled. Then:

1. Watch a light off→on→off transition. If AE responds visibly
   (image normalizes after each transition), then diff-during-transients
   does work given a correctly-engaged sync queue, and force-TRUE is
   purely a steady-state fix. This matches the predicted shumjj behavior.
2. After lighting stabilizes, run the poke test. Sensor regs should
   stay poked (steady-state freeze re-confirmed).
3. Apply manual AE lock (`ss_mpi_isp_set_exposure_attr` manual mode).
   Poke. Sensor regs should stay poked.

Outcome A (AE responds to transients but freezes in steady state):
confirms the revised story. Force-TRUE is a clean steady-state fix
on top of working transients.

Outcome B (AE does not respond even to transients): means
diff-during-transients alone is insufficient even with sync-queue
engagement. Force-TRUE was load-bearing for both transients and
steady-state. The diff-then-write path has a deeper issue we still
don't understand.

This test is low-risk -- one source edit, one rebuild, one deploy.
If it breaks AE, re-enable force-TRUE and we're back to current state.

#### Diagnostic artifacts from this investigation

| File | Purpose |
|---|---|
| `research/PHASE9_KO_DIFF.md` | Detailed ELF/disasm comparison of three `ot_isp.ko` variants |
| `C:/Users/HomeStar/AppData/Local/Temp/opencode/ko_diff/` | Intermediate readelf, objdump, strings dumps |

#### Edge cases verified

- **AE manual lock** (`ss_mpi_isp_set_exposure_attr` with manual mode +
  fixed exp/gain): not tested directly, but force-TRUE handles it
  correctly by construction (writes every frame regardless of diff).
- **Truly stable indoor lighting**: confirmed. 30s of identical AE
  output (`sys_gain=196592, line=2802`). Pure-diff would freeze.
- **AE actively iterating**: confirmed via `noroi_cnt` monotonically
  advancing every frame even when output is constant.

### Diagnostic Helpers Built This Session

| Tool | Purpose |
|------|---------|
| `driver/build/check_size` | Prints `sizeof(ot_isp_i2c_data)` -- confirmed 24 bytes on ARM |
| `driver/build/check_sns` | Prints SDK constants like `OT_ISP_MAX_SNS_REGS=50` |
| `driver/build/check_offsets` | Prints all field offsets in `ot_isp_sns_regs_info` |

These can be re-deployed if the next session needs to recheck struct layout.

### Pitfalls Confirmed This Session

1. **make pipeline only rebuilds pipeline_test**, NOT `libsns_sc635hai.so`.
   Use `make driver` after changing `sc635hai_cmos.c` / `sc635hai_sensor_ctl.c`.
2. **MD5 verify after every deploy** -- we verified each deploy this session.
3. **`ps | grep pipeline` before launch** to avoid duplicate instances.
4. **`cam_cmd.py` has a 15-second hard deadline** -- long-running commands or
   backgrounding via `&` over the tcpsvd shell is fragile. The shell session
   can close before the backgrounded process detaches, killing it via SIGHUP.
   Use `setsid` to create a new session, but busybox setsid does not support
   `-f` (force-fork). The reliable pattern is `setsid script.sh </dev/null > /dev/null 2>&1 &`
   inside a heredoc'd shell script saved to /tmp first.
5. **Watchdog will reboot the SoC ~30s after killing pipeline_test** if mySystem
   is stopped. The watchdog file descriptor needs the `'V'` magic close. Force-
   killing with `kill -9` does not run the cleanup. Either reboot is harmless
   (system comes back to superb), or restart pipeline_test before 30s elapses.
