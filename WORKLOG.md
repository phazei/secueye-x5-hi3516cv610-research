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

---

## 2026-05-15 ~14:00 [Phase 0] -- Reset button research + doc cleanup

**Context:** CAMERA.md consolidation review revealed the reset button
wipe scope was incomplete, PTZ motor was incorrectly documented as
present, and ROADMAP.md had stale single-core/900MHz specs.

**Did:** Investigated reset button via GPIO enumeration, superb strings
analysis, and Ghidra decompilation cross-reference. Identified GPIO 13
as the reset input. Extracted full list of `rm` commands from superb
binary. Documented wipe scope, survival table, and SD card FAT32
corruption risk in CAMERA.md. Deleted 3 superseded shell scripts
(`start_pipeline_bg.sh`, `run_pipeline_bg.sh`, `probe_after_test.sh`).
Fixed PTZ motor claims (no motor hardware, PCB has unpopulated
connectors). Fixed ROADMAP.md CPU specs. Added PQ bin bind-mount
clarification to DRIVER.md. Updated README.md repo structure.

**Found:** Reset button handled entirely by superb (not mySystem or
scripts). GPIO 13 is the only input GPIO. Factory reset wipes
`syscfg/*`, `network/*`, `face/*`, `custom_voice/*`, and specific named
files but does NOT touch `debug.sh`, `hwconfig.cfg`, `lic.bin`, SD card,
or `/progs/`. SD card file truncation on mySystem kill is caused by
unclean FAT32 dismount, not the reset path. superb has `mkdosfs`/
`mkfs.ext4` commands and checks for "mount abnormal" on startup.

**Status:** Complete. Moved to ROADMAP Phase 0.6.

**Next:** Phase 1 (audio capture) or Phase 2 (NPU research).

---

## 2026-05-15 ~17:00 [Phase 0] -- Dropbear SSH deployment

**Context:** Needed secure shell access to camera. Stock firmware has
no SSH, telnet, or remote file transfer. We had tcpsvd on port 9999
(unauthenticated) and recv/send_file.py (unencrypted TCP). Dropbear
was referenced in the firmware building repo but never on the actual
camera.

**Did:** Found pre-compiled statically-linked dropbear v2025.89 (302 KB)
+ dropbearkey (129 KB) in `research/Hi3516CV610_Firmware_Building/overlay/`.
Deployed to camera via recv/send_file.py. Generated Ed25519/ECDSA/RSA
host keys, stored on configfs for persistence. Generated Ed25519 keypair
on PC (`~/.ssh/id_ed25519`), pushed pubkey to camera. Set root password
(`chpasswd`), stored hash on configfs. Rewrote `debug.sh` to set up SSH
at boot: mount SD card, set password, fix devpts, create /etc/shells,
install authorized_keys, start dropbear.

**Issues resolved during deployment:**
1. **Root password was empty** (not `sl.x.` as previously documented).
   Verified with `cryptpw` on camera and passlib locally. Fixed in
   CAMERA.md credentials table.
2. **devpts mounted with ptmxmode=000** -- blocked PTY allocation,
   caused dropbear to accept login then immediately disconnect
   ("closed by remote host"). Fix: remount with `ptmxmode=666`.
3. **No `/etc/shells`** on stock rootfs -- dropbear needs it. Fix:
   create at boot.
4. **`/root` on read-only squashfs** -- can't write authorized_keys.
   Fix: mount tmpfs on `/root` at boot.
5. **SSH key passphrase** -- PowerShell `-N '""'` quoting set literal
   `""` as passphrase instead of empty. Regenerated key.
6. **SD card not mounted at debug.sh time** -- superb normally mounts
   it, but debug.sh runs before superb. Dropbear binary on SD card
   was inaccessible. Fix: mount SD card in debug.sh.
7. **Mount point `/progs/rec/00` didn't exist** -- bashrc.sh creates
   `/progs/rec` as empty tmpfs; the `00` subdir is created by superb.
   Fix: `mkdir -p` before mount.

**Boot trace** (`set -x` in debug.sh, `/tmp/debug_boot.log`) was
critical for diagnosing issues #6 and #7. Removed after verification.

**Updated docs:**
- CAMERA.md: expanded boot sequence (6-line summary -> full 7-stage
  trace), added debug.sh boot hook section, added configfs layout
  table, fixed root password (empty not sl.x.), added SSH to security
  table and process tree.
- WORKLOG.md: this entry.

**Status:** Complete. SSH survives reboot with key-based auth (no
password prompt). Password auth also works as fallback.

**What's running after boot:**
- dropbear :22 (SSH, key + password auth)
- tcpsvd :9999 (legacy unauthenticated shell, kept as fallback)
- recv :8888 (fast file transfer)
- superb (stock camera daemon)
- mySystem (watchdog)

**Configfs footprint** (`/etc/conf.d/`, 1 MB jffs2):
- debug.sh (2.6 KB), shadow_root (131 B), dropbear/ (~1.7 KB keys +
  97 B authorized_keys). ~800 KB free.

**Next:** Consider cross-compiling dropbear with SCP support (it's MIT
licensed, we have the ARM toolchain). Then Phase 1 (audio) or Phase 2
(NPU).
