# SECUEYE X5 Custom Firmware Roadmap

End goal: replace the stock `superb` daemon with our own focused camera
firmware. No Chinese cloud calls, no closed-source 8 MB monolith, no
mandatory mobile app. Camera streams, records, detects motion, and pushes
alarms to a configurable webhook. Web UI for live view, recordings, and
settings. SC635HAI sensor driver already complete (Phase 9 in archived
docs).

## Hardware + stack baseline (frozen)

- SoC: HiSilicon Hi3516CV610, single-core Cortex-A7 @ ~900 MHz, 128 MB RAM
  (typically split ~64 MB OS / ~64 MB MMZ).
- Sensor: SmartSens SC635HAI (6.35 MP, 3200×1800 native, BGGR, 20 fps).
  Our driver is complete and working.
- Kernel: Linux 5.10 ARMv7 musleabi, modules already loaded by `mySystem`
  via `loadXXX.sh` scripts.
- MPP SDK: V1.0.2.1 (camera kernel modules + our `libot_mpi_*.so` are
  byte-identical to KOL SDK V1.0.2.1 build). We stay on V1.0.2.x
  indefinitely.
- Toolchain: `arm-v01c02-linux-musleabi-gcc` (musl) at
  `research/hi3516cv610_toolchain/`.
- Build flow: WSL Ubuntu → `make` in `driver/` → `tools/deploy_file.py`
  (will be replaced by dropbear/scp in Phase 0).

## Memory + CPU envelope

- Stock superb resident: ~30 MB. Our daemon target: 1-10 MB.
- HIVIEW per-module processes: ~1-3 MB each.
- shumjj single binary: ~15-25 MB.
- CPU is the real constraint, not RAM. Single Cortex-A7 @ 900 MHz. NPU
  inference offloads from CPU; H.265 encoding is dedicated HW. Daemon
  glue is the only CPU consumer that matters.

---

## Documentation discipline

Lesson learned from the early sessions: scattered phase docs
(`PHASE3_CONTINUE.md`, `PHASE5_CONTINUE.md`, `PHASE9_ISP_I2C_SYNC.md`,
`PHASE9_KO_DIFF.md`) created drift, superseded-but-not-deleted content,
and confusion about which doc was current. Going forward:

### One worklog, append-only, structured

Phase 0 creates `WORKLOG.md` at the repo root. It is the running
record of what has been done, what is in progress, and what was
learned per phase. With LLM-assisted development, multiple phases
can complete in a single day, so all timestamps include hour and
minute (`YYYY-MM-DD HH:MM`) to preserve useful resolution.
Strict rules at the top of the file:

1. **Never overwrite. Always append.** New entries go at the bottom
   with date+time headers.
2. **Never delete prior entries.** If a prior conclusion is wrong,
   add a new entry that says "2026-MM-DD HH:MM: revised; the entry from
   YYYY-MM-DD HH:MM was incorrect because X. Current understanding is Y.
   See also CAMERA.md/DRIVER.md for the canonical answer."
3. **Cross-reference, don't duplicate.** WORKLOG.md is the journal.
   CAMERA.md / DRIVER.md / ROADMAP.md are the canonical state.
   Findings get summarized in the canonical doc; the journal entry
   says "details in WORKLOG.md §<date+time> for the full session."
4. **Each entry has a phase tag.** `[Phase 1]`, `[Phase 3]`, etc., so
   readers can filter to the work they care about.
5. **Each entry ends with one of: status update, next action, or
   handoff note.** No dangling "we tried X" without saying what comes
   next.
6. **Big findings get a CAMERA.md / DRIVER.md update in the same
   commit as the WORKLOG entry.** Don't let knowledge live only in
   the journal.

Entry template:

```
## 2026-MM-DD HH:MM [Phase N] -- Short subject

**Context:** what we were trying to do.

**Did:** what was executed; commands, file changes, observations.

**Found:** key findings. Be specific. Cite file paths and line
numbers where relevant.

**Status:** done / in-progress / blocked / superseded.

**Next:** what comes after, or "see ROADMAP §X.Y."
```

### Phase completion ritual

When a phase is declared complete:

1. Append a final WORKLOG entry summarizing the phase outcome.
2. Update ROADMAP.md to mark the phase complete (add a
   `**STATUS: complete YYYY-MM-DD HH:MM**` line at the phase header).
3. Make sure all canonical docs (CAMERA.md / DRIVER.md) reflect the
   final state, not the journey.
4. Single commit: `docs: complete Phase N -- <subject>`.

### Worklog size budget

WORKLOG.md will grow but should stay manageable. If it crosses ~2000
lines or ~50 entries, split by phase: archive completed phases to
`research/archive/worklog-phaseN.md` and leave only active phases in
the live WORKLOG.md.

### What NOT to do

- Do not create `PHASE_N_CONTINUE.md`, `PHASE_N_NOTES.md`,
  `INVESTIGATION_X.md`, or any other ad-hoc per-session doc. If a
  finding doesn't fit in the worklog entry, it belongs in the
  canonical doc (CAMERA.md or DRIVER.md) for that subject area.
- Do not duplicate canonical content into the worklog. Cross-reference.
- Do not let stale content survive in canonical docs. Update or
  delete; don't leave [SUPERSEDED] markers as the long-term
  solution -- those are fine for a session in progress, but should
  be cleaned up at phase completion.

---

## Phase 0 -- Consolidation + cleanup (1 session)

Purely organizational. No camera functional changes.

### 0.1 Validate dropbear ssh

- Confirm `dropbearmulti` binary is present in `appfs/overlay/.../usr/bin/`
  per firmware_building research.
- Enable persistently if not running: probably needs `dropbearmulti dropbear`
  invocation at boot, plus host keys generated in `/etc/dropbear/`.
- Verify `root` / `sl.x.` login works via ssh from workstation.
- Verify `scp` upload/download.
- Document setup procedure.
- **Outcome A:** dropbear works → plan to retire tcpsvd backdoor + recv.c
  daemon in Phase 3 when we replace superb. Use scp/rsync as primary
  transfer for development.
- **Outcome B:** dropbear unavailable or broken → document why; keep
  tcpsvd + deploy_file.py for now; add dropbear enablement as a
  research item for Phase 3.

### 0.2 Repo audit + cleanup

Classify each file as **keep**, **delete**, or **archive** (move to
`research/archive/`). Cross-reference recent git log before deleting.
Present plan for approval before executing.

Suggested classifications (verify each):

**tools/ -- keep:**

- `cam_cmd.py` (essential, until dropbear replaces it)
- `deploy_file.py` (fallback transfer)
- `pull_file.py` (file download)
- `rtsp_run.sh` (production launcher, will evolve in Phase 3)
- `ghidra/` (reverse-engineering scripts + output, may be useful in
  Phase 1 audio + Phase 2 NPU research)

**tools/ -- delete:**

- `crack_des.py`, `crack_password.cmd` (password crack complete)
- `monitor_alerts.py`, `monitor_reboot.py`, `monitor_uptime.py`,
  `monitor_uptime.cmd`, `uptime_log.txt`, `uptime_monitor_*.txt`,
  `test_results_*.json` (uptime monitoring artifacts, served purpose)
- `setup_sd_logging2.py` (superseded)
- `redump_partitions.py` (firmware dump complete)
- `isp_control.py` (bspmm-poke approach abandoned, ISP overrides)
- `test_settings.py` (investigative, done)
- `parse_syscfg.py` (investigative, done)
- `stream_switch.py` (probably superseded)
- `start_pipeline_bg.sh` OR `run_pipeline_bg.sh` (pick one, kill other)
- `recv.c`, `send_file.py` (retire when dropbear works)

**tools/ -- review (may keep, may move to research/):**

- `dvrip/`, `onvif/`, `ble/`, `uart/` -- investigative tooling. If
  contents have unique value, move to `research/archive/`. Else delete.
- `fix_timezone.py` (will be replaced by web UI; keep until then as
  reference)
- `diag_run.sh`, `probe_after_test.sh` (one-off diagnostics; keep if
  small)
- `vi_shim.c`, `ioctl_hook.c` (research instruments; archive)

**driver/ -- keep:**

- `src/sc635hai_cmos.c`, `src/sc635hai_sensor_ctl.c`,
  `src/sc635hai_cmos.h` (production driver)
- `test/pipeline_test.c` (basis for Phase 3 daemon)
- `rtsp/` (pipeline_test RTSP server)
- `Makefile`, `README.md`

**driver/ -- delete:**

- `test/sensor_test.c` (early bringup test, complete)
- `test/awb_dump.c` (AWB data extracted, captured in
  `SC635HAI_SENSOR_ANALYSIS.md`)
- `test/hook.c`, `test/vi_shim.c`, `test/ioctl_hook.c`, `test/recv.c`
  (research instruments)
- `build/` intermediate artifacts (verify gitignored)

**custom/ -- delete entire directory** (confirmed scratch).

**apk_decompiled/ -- delete jadx output, keep APK_ANALYSIS.md**
(superb is going away, stock mobile app irrelevant; BLE + cloud protocol
findings are in APK_ANALYSIS.md).

**firmware/ -- keep:**

- `extracted/` (saved superb binary, partition contents -- needed for
  Phase 1 audio reference and Phase 2 NPU lib extraction)
- `ANALYSIS_SUMMARY.md` (or merge into CAMERA.md)
- Raw `.bin` partition dumps -- keep, modest size, occasional reference

**research/ -- keep:**

- `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/` (active SDK reference)
- `hi3516cv610_toolchain/` (build toolchain)
- `HIVIEW/` (upstream reference for cherry-picking in Phases 3-7)
- `shumjj-3516cv610_app/` (upstream reference)
- `Hi3516CV610_Firmware_Building/` (BSP reference)
- `hi3516cv610_PictureQuality/` (PQ tuning reference)

**research/ -- archive (move to `research/archive/`):**

- `PHASE3_CONTINUE.md`
- `PHASE5_CONTINUE.md`
- `PHASE9_ISP_I2C_SYNC.md`
- `PHASE9_KO_DIFF.md`
- `RESEARCH.md`
- `SC635HAI_SENSOR_ANALYSIS.md` (after merging current content into
  `DRIVER.md`)
- Top-level `INVESTIGATION.md`, `CAMERA_CONTROLS.md` (after merging
  into `CAMERA.md`)
- `tools/ghidra/output/sensor_i2c_kernel.md` (after merging into
  `DRIVER.md`)
- `apk_decompiled/APK_ANALYSIS.md` (after merging relevant pieces
  into `CAMERA.md`)

### 0.3 Execute approved cleanup

Series of small git commits, each with clear message:

- `chore: delete completed-research tooling`
- `chore: remove custom/ scratch directory`
- `chore: archive apk_decompiled raw jadx output`
- `chore: archive driver/test/ research instruments`
- `chore: archive phase docs to research/archive/`
- etc.

### 0.4 Write new top-level docs

- **`README.md`** -- project overview, current state, quick start,
  build/deploy workflow. Pointers to other docs.
- **`CAMERA.md`** -- hardware + firmware reference: SoC, sensor,
  partitions (`bootargs`, `bootloader`, `kernel`, `rootfs`, `appfs`,
  `data`), mySystem behavior, BLE provisioning (existing path),
  watchdog (hardware peripheral, ping requirement), kernel module
  load order, access paths (tcpsvd backdoor history + dropbear future),
  cracked passwords (`sl.x.` for rootfs; appfs uncracked and
  irrelevant), the SD-card jailbreak vector (`recycle_ali.sh`),
  network ports, OEM cloud architecture (for context on what we're
  removing).
- **`DRIVER.md`** -- sensor driver work: SC635HAI register map, AWB
  calibration, ISP/sensor sync path, kernel I2C sync mechanics
  (force-TRUE fix, Iteration 2 sync-queue fixes), build/deploy
  procedure, test methodology (poke test, /proc/umap/isp sampling).
  Consolidates PHASE3 + PHASE5 + PHASE9 + PHASE9_KO_DIFF + SC635HAI
  sensor analysis + sensor_i2c_kernel.md.
- **`ROADMAP.md`** -- this document. Phase 0 marked complete, Phase 1
  next.
- **`WORKLOG.md`** -- create with the rules section at the top (copy
  from ROADMAP "Documentation discipline" section). Seed with a
  closing entry for Phase 0:

  ```
  ## 2026-MM-DD HH:MM [Phase 0] -- Cleanup complete

  **Context:** Pre-existing scattered phase docs and exploratory
  tooling needed consolidation before starting daemon work.

  **Did:** Validated dropbear (or not). Audited tools/driver/custom/
  apk_decompiled/firmware. Removed N files, archived M to
  research/archive/. Wrote README.md, CAMERA.md, DRIVER.md,
  ROADMAP.md, WORKLOG.md.

  **Found:** [any surprises during consolidation; e.g. "appfs
  password gates nothing important per investigation" or "dropbear
  required X workaround"]

  **Status:** complete.

  **Next:** Phase 1 -- audio capture. See ROADMAP §1.
  ```

### 0.5 Final commit

`docs: consolidate research into top-level README/CAMERA/DRIVER/ROADMAP/WORKLOG`

### Phase 0 deliverables

- New top-level docs in place: README.md, CAMERA.md, DRIVER.md,
  ROADMAP.md, WORKLOG.md.
- `research/archive/` containing historical phase docs.
- Slim `tools/`, `driver/`, `custom/` gone, `apk_decompiled/` trimmed.
- dropbear scp path validated (or documented as future task).
- Git history reflects the cleanup as a clean series of commits.
- WORKLOG.md seeded with documentation rules + Phase 0 closing entry.

---

## Phase 1 -- Audio capture (estimated 1-2 weeks)

Prerequisite for replacing superb -- we need audio working before we
cut over.

### 1.1 Hardware + driver investigation

- Identify audio MPP modules loaded on camera: `ot_ai` (audio input),
  `ot_aenc` (encoder), `ot_aio` (in/out base), `ot_acodec` (codec).
- Inspect `lsmod` and `/proc/umap/audio*` for module state.
- Locate audio device nodes (`/dev/ai*`, `/dev/ao*`).
- Determine on-board codec chip (likely a small I2C codec; cross-ref
  schematic from teardown notes).
- Find sample rate, bit depth, channel count, format.

### 1.2 SDK reference audit

- Walk through `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/sample/audio/`.
- Identify the AI (audio-in) init sequence: device → channel → enable →
  read frames.
- Identify the encoder init: aenc channel → bind to AI → read encoded
  stream.
- Note any codec-specific init that needs to happen before MPP touches
  the device.

### 1.3 Integrate audio capture into pipeline_test

- Add AI device init + AENC channel init alongside the existing video
  pipeline.
- Encode to G.711 (a-law or u-law) -- simplest, no licensing concerns,
  RTSP-compatible. AAC as a stretch goal.
- Feed encoded frames into the RTSP track alongside H.265 video.
- Verify on a RTSP client (VLC, ffplay): both video + audio play,
  reasonable lip sync.

### 1.4 Audio quality validation

- Test in quiet room, normal speech, loud environment.
- Verify gain is reasonable (not clipping, not silence).
- Test sample rate and frame timing for sync drift over 5+ minutes.

### 1.5 Reset button behavior research (small side task)

Sneaks in here because it's low-effort camera observation work that
fits the audio investigation context.

- Locate which script or daemon handles the GPIO reset button. Likely
  in `mySystem` or one of the `/etc/init.d/` startup scripts. Check
  for `reset`, `factory`, `default`, `gpio` keywords in scripts.
- Determine the long-press handler:
  - What files / directories does it remove or rewrite?
  - Does it touch `wpa_supplicant.conf`?
  - Does it touch `SystemCfg.ini` or other config files in `/etc/`?
  - Does it wipe SD card recordings?
  - Does it reformat any partition?
  - **Crucially: does it touch our daemon binary in `/progs/`?**
- Confirmed empirical fact: the tcpsvd port-9999 backdoor survives a
  long-press reset. So the reset is NOT a full re-flash; it's a
  scoped config wipe.
- Document the exact wipe scope in `CAMERA.md` so we know what's safe
  vs. what gets clobbered.
- Determine: do we want our daemon's config files to live in a path
  the reset DOES wipe (so users get a clean default on reset) or one
  it doesn't (so user settings survive)? Or split: per-user settings
  wipeable, per-camera-state non-wipeable.
- Document recommended config-file paths in ROADMAP Phase 3 (daemon
  config) and Phase 5 (web UI settings) once reset scope is known.

### Deliverables

- pipeline_test streams video + audio over RTSP.
- `CAMERA.md` updated with audio module documentation.
- `CAMERA.md` updated with reset-button wipe scope.
- ROADMAP Phase 1 marked complete.

### Open risks

- Codec may need an out-of-tree init step (some Hi3516 platforms
  require a tinyalsa-style configuration before MPP can use it).
- Sample rate negotiation with the encoder may have constraints we
  hit only at runtime.
- Reset button handler may be embedded in `mySystem` rather than a
  separate script -- harder to inspect; may require Ghidra work.

---

## Phase 2 -- NPU + AI research (can run parallel with Phase 1)

Background work -- code reading + binary extraction, no camera
interaction needed. Output is a recommendation, not running code.

### 2.1 Extract aidetect from camera firmware

- Find `libss_mpi_aidetect.so` + the model file `det_hv_hor.bin` on
  the camera.
- Pull both to workstation for analysis.
- Identify the API surface from headers in
  `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/include/hisilicon/`.
- Reference shumjj `device/aidetect/dev_aidetect.cpp` for working
  invocation pattern.
- Document: init sequence, frame format expected, output format
  (`ot_aidetect_result_array`), supported classes (face, human,
  vehicle, pet, garbage, bag, wallet, phone, head_shoulder, bicycle,
  motorcycle), track-ID semantics.

### 2.2 Extract / locate SVP-ACL (YOLOv8 path)

- Find `libsvp_acl.so` + the kernel module `ot_svp_npu.ko` on camera.
- Confirm both loaded; check `lsmod` and `/proc/umap/`.
- Reference shumjj `device/dev_svp_yolov8.cpp` (971 lines) for YOLOv8
  model load + inference pattern.
- Identify whether camera has a yolov8.om already, or if we need to
  bring one.

### 2.3 Comparison: aidetect vs YOLOv8

- aidetect pros: tracking IDs built-in, smaller model, vendor-tuned,
  human/vehicle/pet classes likely tuned for surveillance.
- aidetect cons: closed library, fixed class set, can't retrain.
- YOLOv8 pros: open architecture, retrainable, larger class set
  (COCO 80), industry standard.
- YOLOv8 cons: bigger model (4 MB OM), more inference time, need to
  implement our own tracker (IoU matching or DeepSORT).
- Test both against real scenes from the camera (people, cars,
  pets, common objects). Compare:
  - Detection accuracy.
  - Inference latency on Cortex-A7 + NPU.
  - Memory footprint.
  - False positive rate.

### 2.4 Recommendation for Phase 7

- Pick one as primary for v1 alarm logic.
- Document why.
- If aidetect picked, note YOLOv8 as a future option for custom
  training.

### Deliverables

- `AI_RESEARCH.md` (or section in `CAMERA.md`) with findings.
- Sample inference code for the chosen library, runnable on the
  camera (could be a standalone test program for now, not yet
  integrated into pipeline_test).

---

## Phase 3 -- Daemon foundation (1 week)

Cut over from superb to our own binary.

### 3.1 mySystem investigation

- Locate mySystem config file (likely `/etc/mySystem.conf` or
  similar; check `/etc/init.d/` and process tree).
- Document how mySystem decides what to launch.
- Identify the modification point: edit config to launch our daemon
  instead of superb.
- Investigate `mySystem`'s own watchdog feeding -- does it ping the
  HW watchdog, or rely on its child? If child, our daemon needs to
  ping; if mySystem itself does it, we just need to not block
  mySystem.

### 3.2 Convert pipeline_test to daemon

- Add JSON config file support (use existing cJSON or similar).
- Add proper signal handling: SIGTERM = graceful shutdown, SIGHUP =
  config reload, SIGINT for development.
- Add structured logging to a rotating log file (or syslog).
- Detach from controlling terminal correctly.
- PID file for mySystem to track.
- Crash dump to log on SIGSEGV/SIGBUS (we already have a basic
  handler; upgrade it).

### 3.3 Retire tcpsvd + recv.c

If Phase 0 confirmed dropbear works:

- Remove `debug.sh` from `recycle_ali.sh` startup chain.
- Enable dropbear at boot via the proper init mechanism.
- Remove tcpsvd from process list.
- Use scp/rsync via dropbear for development from now on.
- Update CAMERA.md to reflect the new access model.

### 3.4 Replace superb in mySystem

- Modify mySystem config to launch our daemon instead of superb.
- Boot test: power-cycle camera, verify daemon comes up.
- Crash recovery test: kill -9 the daemon, verify mySystem respawns.
- Watchdog test: insert artificial deadlock, verify hardware
  watchdog resets the SoC.

### 3.5 Strip superb from filesystem

Once daemon is stable and proven:

- Remove `superb` binary from `/progs/`.
- Remove `superb.log` rotation.
- Remove any Alibaba IoT cert files, scripts, helpers from rootfs.
- Document what was removed.

### Deliverables

- Camera boots into our daemon by default.
- Survives crashes via mySystem restart.
- Survives deadlocks via hardware watchdog.
- No more superb, no more Chinese cloud.
- dropbear-only access.

### Open risks

- mySystem may have hardcoded paths or expectations about superb.
- Some hw-init step we depend on may be in superb's startup and need
  porting to our daemon.
- Recovery from a bad config (daemon won't start) requires SD-card
  jailbreak as fallback -- ensure that path is documented and tested.

---

## Phase 4 -- Recording + HLS (1 week)

Continuous SD-card recording with browser-playable timeline.

### 4.1 fMP4 muxer integration

- Cherry-pick `fw/libmov/` from HIVIEW (permissive license, ireader
  fork on GitHub).
- Integrate into our daemon: encoded H.265 NALs in, fMP4 fragments
  out.
- Add audio track (G.711 from Phase 1).
- Use fragmented MP4 (fMP4) -- power-loss tolerant, browser playable.

### 4.2 Segment manager

- Configurable segment duration (default 5 minutes).
- Filename pattern: `/sdcard/recordings/YYYY-MM-DD/HH-MM-SS.mp4`.
- Auto-create date directories.
- Index file: `/sdcard/recordings/index.json` listing all segments
  with start time, duration, size. Updated on segment close. Used by
  web UI for browse without full directory scan.

### 4.3 Disk space management

- Configurable reserve (default 100 MB).
- On segment close, check free space. If below threshold, delete
  oldest segment(s) until above threshold + safety margin.
- Update index.json accordingly.
- Log retention actions.

### 4.4 HLS manifest generation

- Generate live `.m3u8` manifest pointing at the last N segments
  (e.g. last 5 minutes worth).
- Either:
  - **Option A:** transmux fMP4 to HLS-compatible MPEG-TS on the
    fly (more CPU, more standard HLS).
  - **Option B:** fMP4 in HLS (HLS supports CMAF since 2017, all
    modern browsers handle it).
- Prefer Option B for simplicity -- same files serve archive and
  live.
- Update manifest every segment close.
- Also generate per-day manifests for archive playback.

### 4.5 SD card mount + format

- At boot, scan for SD card.
- If unformatted, format ext4 (or whatever, just not FAT32 for
  >4 GB file support).
- Mount at known path.
- Handle hot-swap (SD card removed/inserted while running).

### Deliverables

- Continuous recording with rotation.
- Power-loss tolerant (last 1-2 seconds lost at most).
- Browser-playable archive via HLS manifests.
- Index for fast browse.

---

## Phase 5 -- Web UI v1 (1-2 weeks)

Single-page-app, no auth on LAN. Serves live preview, recording
browse, basic settings.

### 5.1 HTTP server

- Embed Mongoose (`fw/mongoose` from HIVIEW or upstream).
- Listen on port 80.
- Serve static files from `/sdcard/www/` (so updates don't require
  daemon rebuild).
- REST API for control + status (JSON in/out).

### 5.2 REST endpoints

- `GET /api/status` -- daemon status, uptime, current AE/AWB, free
  disk, etc.
- `GET /api/recordings?date=YYYY-MM-DD` -- list segments.
- `GET /api/live.m3u8` -- proxy/redirect to HLS manifest.
- `GET /api/recordings/playlist.m3u8?date=YYYY-MM-DD` -- archive
  playback.
- `GET /api/settings` -- current settings JSON.
- `PUT /api/settings` -- update settings (validates, writes config
  file, applies live where possible).
- `POST /api/reboot` -- reboot camera.
- `GET /api/snapshot.jpg` -- one-shot JPEG snapshot.

### 5.3 SPA frontend

- One HTML file, vanilla JS (no framework -- keep it small + simple).
- Single page with sections / tabs:
  - **Live:** HLS video element + a few status fields.
  - **Recordings:** date picker, timeline scrubber on a per-day HLS
    playlist, snapshot grid maybe.
  - **Settings:** form for the v1 settings (see 5.4).
  - **System:** uptime, free space, CPU/mem if cheap, reboot button.

### 5.4 Settings v1

- Camera flip (horizontal mirror)
- Camera rotate 180 (vertical flip)
- Time overlay format + position (default: `YYYY-MM-DD HH:MM:SS`,
  top-left)
- Name overlay text (user-configurable, default: hostname, bottom-right)
- Timezone selector (write `/etc/localtime`, set `TZ`)
- Webhook URL (where alarms POST)
- Retention size (or "use all available with 100MB reserve")
- Segment duration (1, 2, 5, 10, 15, 30 minutes)
- Live HLS chunk duration (2-6 seconds)
- Reboot camera button

### 5.5 OSD via ot_rgn

- Build font bitmap once at startup.
- Configure two regions (time top-left, name bottom-right).
- Attach to encoder.
- Update time region every second (or every minute, or every frame --
  evaluate cost).
- Update name region only on settings change.

### Deliverables

- Browser-accessible camera with live view, recording browse, basic
  settings.
- All v1 settings functional and persisted.
- OSD overlays burning into the H.265 stream and recordings.

---

## Phase 6 -- AP-mode Wi-Fi provisioning (3-5 days)

Out-of-box setup without phone app.

### 6.1 Captive portal infrastructure

- Cross-compile hostapd + dnsmasq for the toolchain (or find busybox
  alternatives; busybox has both as applets in some configs).
- At boot, check for valid `/etc/wpa_supplicant.conf` with a
  configured network.
- If not present or empty: switch wifi to AP mode, start hostapd
  (`SSID = Camera-Setup-<MAC tail>`), start dnsmasq (returns
  192.168.4.1 for every DNS query, hands out 192.168.4.x to clients).
- Mongoose web server already running on port 80 from Phase 5;
  it now serves the setup page on the AP interface.

### 6.2 Captive portal UX

- Add detection endpoints to web server: `/generate_204` (Android),
  `/hotspot-detect.html` (iOS), `/connecttest.txt` (Windows). Each
  returns the setup HTML so the OS auto-opens the browser.
- Setup HTML: list available networks (scan via `iw` or
  `wpa_cli scan_results`), let user pick + enter password.

### 6.3 Mode switch

- On form submit: write `/etc/wpa_supplicant.conf`, kill hostapd +
  dnsmasq, switch interface to client mode, restart wpa_supplicant,
  obtain DHCP lease.
- If association succeeds: persist config, web UI now shows "switched
  to home Wi-Fi, here's the new IP, reconnect to that network and
  visit <new IP>".
- If association fails: revert to AP mode, show error, user can
  retry.

### 6.4 Reset to AP mode

- Web UI button: "forget Wi-Fi" → clears wpa_supplicant.conf,
  reboots into AP mode on next boot.
- Physical button alternative if camera has one (check teardown).

### 6.5 Preserve BLE provisioning fallback

- Don't delete mySystem's existing BLE provisioning path.
- BLE provisioning writes wpa_supplicant.conf via the same format we
  use, so both paths converge on the same outcome.
- Document both paths in CAMERA.md.

### Deliverables

- Out-of-box: power on camera, see SSID `Camera-Setup-XXXX` on phone,
  connect, auto-redirect to setup page, configure home Wi-Fi, done.
- BLE path still works for users who prefer it.
- Web UI has "forget Wi-Fi" option for re-provisioning.

---

## Phase 7 -- Motion + AI alarms (1-2 weeks)

The actual surveillance feature set.

### 7.1 Motion detection

- Use IVE (Intelligent Video Engine) `ot_ive.ko` for frame-difference
  motion detection. Lightweight, runs on VPSS frames at full rate.
- Reference: shumjj has `libhi_ivs_md.so` integrated but unused;
  HIVIEW has `mod/svp/3516c/ive/sample_ive_md.c`. Copy from either.
- Output: per-frame "motion present" boolean + bounding box of
  motion region.
- Configurable: sensitivity, minimum motion area, ignore regions.

### 7.2 AI object detection integration

- Wire up the library chosen in Phase 2 (aidetect or YOLOv8).
- Pull 640x640 frames from VPSS chn1 (the AI-sized output).
- Run inference per frame (or every N frames if CPU/NPU bound).
- Output: array of `(class, confidence, x, y, w, h, track_id)`.
- Overlay detection boxes onto a debug region of the stream (toggle
  in web UI).

### 7.3 Rule engine

Configurable rules, evaluated per frame against detections + motion:

- **Polygon zone (intrusion):** point-in-polygon test on detection
  center. Fire if box center enters zone.
- **Line crossing:** segment-segment intersection test against
  detection track. Direction-aware (cross from side A to side B = one
  event, B to A = another). Fire on crossing event.
- **Object-class filter:** rules apply only to specified classes
  (e.g. "person" only, ignore "cat").
- **Time-of-day filter:** rules active only during configured hours.
- **Debounce:** suppress repeat alarms within N seconds.
- **Track confidence:** require track to persist N frames before
  firing (reduces flicker false positives).

### 7.4 Alarm dispatch

- On rule fire:
  - Capture JPEG snapshot via VENC JPEG channel.
  - Optionally annotate with detection box drawn on it.
  - POST JSON to configured webhook URL:
    ```json
    {
      "timestamp": "2026-05-15T12:34:56Z",
      "camera": "front_door",
      "rule": "person_in_yard",
      "object": "person",
      "confidence": 0.87,
      "zone": "yard",
      "snapshot_url": "http://<cam_ip>/snapshots/abc123.jpg",
      "video_url": "http://<cam_ip>/recordings/2026-05-15/12-30-00.mp4"
    }
    ```
- libcurl-style HTTP client (or hand-rolled, ~200 lines).
- Webhook URL configurable in web UI.
- Test against ntfy, Discord, Telegram, Home Assistant webhook.

### 7.5 Motion-triggered recording mode

- Setting: continuous / motion-only / disabled.
- In motion-only mode:
  - Always maintain rolling 30-second pre-roll buffer (write to a
    temporary fMP4).
  - On motion: flush pre-roll into a recording file, start writing
    live to it.
  - Continue recording until N seconds (default 30) after last
    motion event.
- Snapshots: always captured on alarm regardless of recording mode.

### 7.6 Web UI alarm config

- Section in Settings tab for:
  - Webhook URL + test button (sends a test alarm POST).
  - Recording mode toggle.
  - Rule list with add/edit/delete.
- Each rule editor:
  - Polygon zone drawing on a live frame snapshot (HTML canvas).
  - Line drawing with direction indicator.
  - Class filter dropdown (populated from detection library's class
    list).
  - Time-of-day picker.
  - Debounce slider.
- Alarm history view (last N alarms with snapshot thumbnails).

### Deliverables

- Real surveillance camera.
- Motion detection working at frame rate.
- AI object detection running on NPU.
- Configurable zones + lines + filters.
- Webhook firing to your phone via ntfy/Discord/HA.
- Motion-triggered recording option.

### Open risks

- Detection latency may force every-N-frames inference (3 fps
  instead of 20 fps) -- usually fine for surveillance.
- IVE motion detection may produce false positives in low light or
  with foliage movement -- requires sensitivity tuning.
- Rule engine complexity creep -- keep v1 minimal, defer fancier
  rules.

---

## Phase 8+ -- Deferred (no order, no commitment)

In rough priority order based on Anthony's feedback:

1. **WebRTC** -- sub-second browser streaming. After basics working.
2. **Manual exposure / gain / WB controls in web UI** -- nice bonus,
   auto is already good.
3. **Intercom (two-way audio)** -- speaker output via `ot_ao` +
   `ot_adec`, web UI mic input via WebRTC or similar.
4. **MQTT publish for Home Assistant** -- easy once we own the stack;
   add when HA setup is ready.
5. **ONVIF Profile S** -- for NVR/HA auto-discovery. Heavy (~10K LOC
   from gSOAP) but valuable.
6. **HTTPS / authentication for internet access** -- LAN trust model
   for v1; revisit if exposing externally.
7. **Watchdog research** -- can we extend timeout, swap with software
   watchdog, etc. Currently we just ping it.
8. **Firmware OTA flow** -- replace our daemon binary in-place
   without SD card.
9. **Custom AI model training** -- retrain YOLOv8 on home-specific
   objects, convert to .om format.
10. **Audio improvements** -- AAC codec, noise suppression, gain
    control.
11. **Multi-camera support** -- probably never needed.
12. **appfs root password crack** -- low value, ~9.5 days RTX 5090
    for 8-char DES. We have rootfs root, so this is academic.

---

## Document trail

This roadmap is the source of truth for project direction. After
Phase 0 it lives alongside:

- `README.md` -- entry point.
- `CAMERA.md` -- hardware + firmware reference (canonical).
- `DRIVER.md` -- sensor driver work (canonical).
- `ROADMAP.md` -- this file (plan).
- `WORKLOG.md` -- append-only journal (per "Documentation discipline"
  section above). Rules at the top, entries appended below.
- `research/archive/` -- historical phase docs for reference.
- `research/<upstream>/` -- HIVIEW, shumjj, SDK, toolchain, etc.

Each phase commit should include:
1. A WORKLOG.md entry (append, never overwrite).
2. CAMERA.md / DRIVER.md updates for any new canonical findings.
3. A ROADMAP.md update if the plan needs adjusting based on what was
   learned.

Substantive technical findings live in the canonical docs; the
journal cross-references them.
