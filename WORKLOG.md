# Worklog

Append-only journal for the SECUEYE X5 custom firmware project.
See `ROADMAP.md` "Documentation discipline" section for the full
rules. Summary:

1. **Never overwrite. Always append.** New entries at the bottom.
2. **Never delete prior entries.** If wrong, add a correction entry.
3. **Cross-reference, don't duplicate.** Canonical state lives in
   CAMERA.md / DRIVER.md / ROADMAP.md. This is the journal.
4. **Each entry has a phase tag.** `[Phase N]` for filtering.
5. **Each entry ends with a status + next action.**
6. **Big findings get a canonical doc update in the same commit.**

---

## 2026-05-14 ~18:00 [Phase 0] -- Repo cleanup and README

**Context:** Pre-existing scattered phase docs and exploratory
tooling needed consolidation before starting daemon work.

**Did:** Validated dropbear (not available on camera -- Outcome B).
Audited and classified all files in tools/, driver/, custom/,
apk_decompiled/, firmware/. Deleted completed research tooling,
archived investigative tools and phase docs to research/archive/.
Wrote README.md with project overview, 3 transfer methods, build
workflow, repo structure map, hardware table, constraints.

**Found:** Dropbear binary not present anywhere on camera filesystem.
recv.c + send_file.py confirmed as primary transfer method (fast TCP
on port 8888), not expendable. deploy_file.py is slow base64 fallback.

**Status:** Partially complete. README.md and ROADMAP.md done.
CAMERA.md and DRIVER.md deferred to fresh sessions (source docs too
large for remaining context budget).

**Next:** DRIVER.md consolidation, then CAMERA.md.

---

## 2026-05-14 ~22:30 [Phase 0] -- DRIVER.md consolidation

**Context:** 7 source documents with overlapping and partially
superseded content needed consolidation into canonical driver
documentation.

**Did:** Read all 7 sources via parallel agents (PHASE9_ISP_I2C_SYNC,
PHASE9_KO_DIFF, SC635HAI_SENSOR_ANALYSIS, PHASE3_CONTINUE,
PHASE5_CONTINUE, driver/README.md, sensor_i2c_kernel.md). Deep-read
critical sections to resolve contradictions. Produced structured
digests, identified 6 contradictions, resolved all against source
code.

Created two files:
- `DRIVER.md` (~630 lines): canonical sensor driver reference.
- `DRIVER_INTERNALS.md` (~370 lines): kernel decompilation forensics.

Fixed 3 code issues found during consolidation:
- `cmos_get_inttime_max` used `2*VTS-10` (should be `VTS-10`; dormant
  bug, only affects WDR ratio calculations in linear mode).
- Comment said "11 function pointers" (V1.0.2.1 SDK has 12;
  `pfn_set_fast_ae` was added).
- `EXP_OFFSET` comment said `max = 2*VTS - offset` (should be
  `VTS - offset`).

Updated README.md "See also" to reference new docs.

**Found:** 6 contradictions across source docs, all resolved:
1. FPS 30 vs 20: **20fps** is correct (VTS=2812, confirmed by user
   and register reads). Init function name `_6m30_` is inherited
   from Ghidra and misleading.
2. ot_isp_sns_obj 11 vs 12 pointers: **12** (V1.0.2.1 added
   `pfn_set_fast_ae`). This is a different struct from
   ot_isp_sns_exp_func (also 12 pointers).
3. Analog gain 5 vs 7 ranges: **7** (verified in sc635hai_cmos.h).
4. Watchdog KEEPALIVE vs SETTIMEOUT: **SETTIMEOUT only** (verified
   in pipeline_test.c; KEEPALIVE returns EPERM on ot_wdt).
5. Black level 64 vs 1024 vs 1030: no contradiction -- 64 (10-bit)
   = 1024 (14-bit ISP format); superb uses 1030.
6. max_int_time 2*VTS-10 vs VTS-10: **VTS-10** is correct (fixed
   the one remaining callsite in `cmos_get_inttime_max`).

Completeness verification: 55/56 items from source docs covered.
Only omission: SC500AI register comparison table (historical
investigation context, not current-truth reference).

**Status:** Complete. DRIVER.md and DRIVER_INTERNALS.md are the
canonical driver documentation.

**Next:** CAMERA.md consolidation (INVESTIGATION.md +
CAMERA_CONTROLS.md + APK_ANALYSIS.md). See ROADMAP.md Phase 0.4.

---

## 2026-05-14 ~23:00 [Phase 0] -- CAMERA.md consolidation

**Context:** 3 source documents (INVESTIGATION.md 1624 lines,
CAMERA_CONTROLS.md 928 lines, APK_ANALYSIS.md 139 lines) needed
consolidation into canonical camera/firmware reference.

**Did:** Read all 3 sources in full. Identified overlaps (APK doc
entirely subsumed by the other two; cloud protocol covered in all
three; SystemCfg test results duplicated). Found 5 contradictions,
verified 2 against live camera.

Created `CAMERA.md` (~757 lines): hardware specs, firmware/boot chain,
network protocols, cloud architecture (Alibaba IoT MQTT, Thing Model,
app analysis), BLE provisioning, camera controls (ISP, night vision,
detection, recording, PTZ, audio, OSD), SystemCfg.ini tested results,
security assessment, SD card jailbreak, known issues, 3 appendices
(ISP functions, HI_XUID commands, APK directory map).

Fixed 3 errors in README.md:
- WiFi chip: "RTL8188FU" -> "AltoBeam ATBM6x6x" (confirmed via
  `lsusb`, USB product string "AltoBeam_WIFI", driver `atbm_wlan`).
- CPU cores: "single Cortex-A7 @ ~900 MHz" -> "dual Cortex-A7 @
  ~950 MHz" (confirmed via `/proc/cpuinfo`: two processors, CPU
  part 0xc07).
- Key constraints: "Single core" -> "Dual core, still constrained".

Updated README.md "See also" with CAMERA.md reference.

**Found:** 5 contradictions across source docs:
1. RTSP codec H.264 vs H.265: **H.265** (vencType=1, ConfigExport
   returns "H.265 IPC"). INVESTIGATION.md was written before config
   analysis; corrected in CAMERA.md.
2. WiFi chip RTL8188FU vs AltoBeam: **AltoBeam ATBM6x6x** (USB ID
   007a:6162, verified on live camera). README.md was wrong; fixed.
3. CPU single vs dual core: **Dual-core** (2 processors in
   /proc/cpuinfo, kernel says "SMP 2 CPUs"). README.md was wrong;
   fixed.
4. RTSP resolution 3840x2160 vs 3200x1800: Both valid -- superb
   uses 4K, our pipeline_test uses 3200x1800. Documented both.
5. FPS 15 vs 20: Both valid -- sensor runs 20fps, superb VENC
   outputs 15fps. Documented both.

**Status:** Complete. CAMERA.md is the canonical camera/firmware
reference. All Phase 0 doc consolidation is now done.

**Next:** Phase 1 (audio capture) or Phase 2 (NPU research). See
ROADMAP.md.
