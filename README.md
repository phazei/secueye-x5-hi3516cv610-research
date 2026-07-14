# SECUEYE X5 Custom Firmware

Replacing the stock `superb` daemon on a SECUEYE X5 IP camera
(Hi3516CV610 + SC635HAI sensor) with our own open-source firmware.
No Chinese cloud, no closed-source monolith, no mandatory mobile app.

**Current state:** Custom SC635HAI sensor driver complete and streaming
RTSP (H.265 3200x1800 @ 20fps). Next: audio capture, NPU research,
then daemon conversion. See `ROADMAP.md` for the full plan.

## Quick reference

| Item | Value |
|------|-------|
| Camera IP | `192.168.1.153` |
| SSH | `ssh root@192.168.1.153` (key auth, no prompt) |
| SCP file transfer | `scp -O file root@192.168.1.153:/path` (use `-O` flag!) |
| Shell (legacy) | `tools/cam_cmd.py` (tcpsvd on port 9999, no auth) |
| Fast file transfer (legacy) | `recv` on port 8888 + `tools/send_file.py` |
| Slow file fallback | `tools/deploy_file.py` (base64 over shell) |
| RTSP stream (custom) | `rtsp://192.168.1.153:554/live0` |
| RTSP stream (stock) | `rtsp://192.168.1.153:554/live1` |
| Stream switcher | `python tools/stream_switch.py` |
| Build environment | WSL Ubuntu + ARM cross toolchain |

## Talking to the camera

### SSH (port 22 -- primary)

Dropbear v2026.91 (custom-compiled multi-binary) with Ed25519 key-based
auth. No password prompt:

```bash
ssh root@192.168.1.153
ssh root@192.168.1.153 "ps | grep superb"
```

### SCP file transfer (primary)

SCP is built into our custom dropbear. **Windows gotcha:** OpenSSH 8.6+
defaults to SFTP protocol, which fails because we have no sftp-server.
Always use `-O` to force legacy SCP mode:

```bash
# Upload to camera
scp -O driver/build/ipc_daemon root@192.168.1.153:/progs/rec/00/ipc_drv/
scp -O tools/rtsp_run.sh root@192.168.1.153:/progs/rec/00/ipc_drv/

# Download from camera
scp -O root@192.168.1.153:/tmp/superb.log .
scp -O root@192.168.1.153:/progs/rec/00/ipc_drv/capture.h265 .
```

Without `-O` you'll get: `sh: /progs/rec/00/ipc_drv/sftp-server: not found`

### Shell access (port 9999 -- legacy fallback)

The camera runs a `tcpsvd` root shell on port 9999 (unauthenticated,
installed by us via SD-card jailbreak). Use `cam_cmd.py` to send
commands. Kept as fallback in case SSH breaks:

```bash
python tools/cam_cmd.py "ls /progs/rec/00/ipc_drv/"
python tools/cam_cmd.py "cat /proc/umap/isp"
```

### Legacy file transfer (recv/send_file.py on port 8888)

The `recv` daemon (port 8888) + `send_file.py` predates SCP and is
still available as fallback. Useful if SSH is broken or for bulk
transfers where you don't want to type paths.
The `recv` daemon is auto-started by `debug.sh` on boot
(if the binary exists on the SD card).


```bash
# Send files from PC
python tools/send_file.py 192.168.1.153 8888 driver/build/libsns_sc635hai.so driver/build/ipc_daemon
```

Other legacy tools:
`deploy_file.py` (base64 over shell, very slow -- used only for bootstrapping recv itself),
`pull_file.py` (reverse-connect download from camera).

### Deployment scripts

Three scripts handle deployment, from cold start to routine updates:

| Script | When to use |
|--------|-------------|
| `tools/bootstrap_deploy.ps1` | **Cold start.** No recv, no SSH -- just tcpsvd. Deploys recv via base64, starts it, deploys debug.sh, then runs full redeploy. |
| `tools/redeploy_all.ps1` | **Routine deploy.** Requires recv or SSH already running. Sends all 26 files + creates dropbear hard copies. |
| `tools/stream_switch.py` | **Switch streams.** Toggle between custom pipeline (live0) and stock superb (live1). |

```powershell
# Cold start (after SD card wipe or fresh camera):
.\tools\bootstrap_deploy.ps1

# Routine redeploy (recv or SSH already running):
.\tools\redeploy_all.ps1

# Redeploy with explicit transfer method:
.\tools\redeploy_all.ps1 -recv    # Force recv daemon
.\tools\redeploy_all.ps1 -scp     # Force SCP (requires SSH)

# Switch between custom and stock streams:
python tools/stream_switch.py          # Interactive menu
python tools/stream_switch.py custom   # Custom pipeline (live0)
python tools/stream_switch.py stock    # Stock superb (live1)
python tools/stream_switch.py status   # Show current state
```

### SD card protection (.format marker)

superb reformats the entire SD card if a `.format` marker file is
missing when it starts. Two places maintain this marker:

- `debug.sh` writes `.format` at boot (before superb starts)
- `rtsp_run.sh` writes `.format` after killing superb (before
  mySystem can respawn it)

If the SD card is ever wiped, use `bootstrap_deploy.ps1` to recover.

## Building

Requires WSL (the ARM cross-toolchain is Linux x86_64 ELF binaries):

```bash
# From WSL:
cd /mnt/e/Projects/ipc_XMeye_camera/driver
make all
```

Outputs in `driver/build/`:
- `ipc_daemon` -- camera daemon (H.265 + G.711A RTSP, replaces superb)
- `libsns_sc635hai.so` -- sensor driver shared library
- `recv` -- camera-side TCP file receiver
- `sensor_test` -- standalone sensor test
- `awb_dump` -- ISP AWB calibration reader
- `libbin.so` -- PQ extension library (copied from SDK)

Toolchain: `research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/`

## Running the RTSP stream

```bash
# Deploy files first (see above), then:

# Option A: Launch via cam_cmd (script handles mySystem/superb)
python tools/cam_cmd.py "setsid /progs/rec/00/ipc_drv/rtsp_run.sh </dev/null &>/dev/null &"

# Option B: Use stream_switch.py (handles safe superb shutdown)
python tools/stream_switch.py custom

# View in VLC or ffplay:
ffplay rtsp://192.168.1.153:554/live0
```

`ipc_daemon` feeds `/dev/watchdog` internally. When it exits,
`rtsp_run.sh` resumes mySystem, which restarts superb.

## Repository structure

```
ipc_XMeye_camera/
  ROADMAP.md              Project plan (Phases 0-8+)
  README.md               This file

  driver/                 Sensor driver + daemon code
    src/                  Sensor driver source
      sc635hai_cmos.c       ISP/AE/AWB callbacks
      sc635hai_sensor_ctl.c I2C init + register control
      sc635hai_cmos.h       Constants, gain tables, register map
      hi_compat.h           hi_* -> ss_mpi_* API compat macros
    daemon/               IPC daemon (modular, replaces superb)
      main.c                Entry point, signals, teardown
      pipeline.h            Shared state + config
      hal/                  One file per MPP subsystem (sys, vi, isp, vpss, venc, audio, watchdog)
    rtsp/                 RTSP server library
    test/                 Standalone test tools
      reg_dump.c            I2C register dump utility
      sensor_test.c         Standalone I2C sensor test
      awb_dump.c            ISP AWB calibration reader
    Makefile              Cross-compilation build
    README.md             Driver architecture + build details

  tools/                  Host + camera-side utilities
    bootstrap_deploy.ps1    Cold-start deploy (base64 recv + full redeploy)
    redeploy_all.ps1        Batch redeploy (recv or SCP, auto-detects)
    stream_switch.py        Switch between custom/stock RTSP streams
    cam_cmd.py              Send shell commands via tcpsvd (port 9999)
    recv.c                  Camera-side TCP file receiver (port 8888)
    send_file.py            PC-side file sender (pairs with recv)
    deploy_file.py          Slow fallback: base64 over shell
    pull_file.py            Download files from camera (reverse connect)
    debug.sh                Camera boot script (deployed to /etc/conf.d/)
    rtsp_run.sh             RTSP launch script (deployed to SD card)
    diag_run.sh             Diagnostic run script (deployed to SD card)
    dropbearmulti           Pre-built dropbear SSH binary (ARM, static)
    build_dropbear.sh       Cross-compile dropbear v2026.91 (WSL)
    dropbear_localoptions.h Compile-time options for dropbear build
    fix_timezone.py         Change camera timezone from UTC+8
    ghidra/                 Ghidra RE scripts + decompilation output
      scripts/                Analysis scripts (Java + Python)
      output/                 Decompilation results, including
                              sensor_i2c_kernel.md (kernel sync forensics)

  sd_root/                Ready-to-use SD card jailbreak files
    seculinkIdRecycle/
      recycle_ali.sh        Starts root shell on port 9999 at boot
    README.md               Step-by-step jailbreak + persistence guide

  firmware/               Dumped flash partitions + extracted filesystems
    mtd[0-6]_*.bin          Raw partition images (boot, kernel, rootfs, etc)
    full_flash.bin          Complete 16MB SPI NOR flash dump
    extracted/              Mounted filesystem trees
      rootfs/                 Root filesystem (squashfs)
      appfs/                  Application filesystem (superb lives here)
      resfs/                  Resources (PQ bins, audio, web assets)

  research/               Reference code + SDKs (mostly git submodules)
    Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/
                            Authoritative SDK: headers, libs, sample code
    hi3516cv610_toolchain/  ARM cross-compilation toolchain (musl)
    HIVIEW/                 HIVIEW camera framework (cherry-pick source
                            for libmov, mongoose, NPU sample code)
    shumjj-3516cv610_app/   Reference streaming app (cherry-pick source
                            for YOLOv8, aidetect wrappers)
    Hi3516CV610_Firmware_Building/
                            BSP/firmware packaging reference
    hi3516cv610_PictureQuality/
                            PQ tuning tools + SDK extensions
    archive/                Historical research docs + tools
```

## Hardware summary

| Component | Detail |
|-----------|--------|
| SoC | Hi3516CV610, dual Cortex-A7 @ ~950 MHz |
| RAM | 128 MB (split 40 MB OS / 88 MB MMZ for video) |
| Flash | 16 MB SPI NOR |
| Sensor | SmartSens SC635HAI, 6.35 MP, 3200x1800, 20 fps |
| Encoding | H.265 hardware encoder |
| NPU | Built-in (for AI detection) |
| Wi-Fi | AltoBeam ATBM6x6x (USB, WiFi 6, dual-band 2.4/5 GHz) |
| Storage | Micro SD card slot |
| Watchdog | Hardware, ~30s timeout, must be fed |

## Key constraints

- **Dual core, still constrained.** CPU is the bottleneck, not RAM.
  Two Cortex-A7 cores at ~950 MHz. NPU + HW encoder offload the heavy
  work; daemon glue is the only CPU consumer.
- **MPP SDK V1.0.2.1.** Our kernel modules and userspace libs are
  byte-identical to the KOL SDK V1.0.2.1 build. We stay on this
  version. HIVIEW uses V1.0.1.0 (ABI mismatch -- do not adopt
  wholesale).
- **musl libc.** The toolchain uses musl, not glibc. Some POSIX
  features behave differently.
- **SSH + SCP via dropbear.** Custom-compiled dropbear v2026.91
  (static multi-binary, 226 KB) deployed to SD card. Ed25519 key auth
  + password fallback. Starts at boot via `debug.sh`. SCP enabled;
  use `scp -O` from Windows (OpenSSH defaults to SFTP which we don't
  have). Build script: `tools/build_dropbear.sh`.

## SD card jailbreak

Root access is obtained via the `recycle_ali.sh` SD card vector: the
stock firmware (`/progs/updateID.sh`) sources an arbitrary script from
the SD card on every boot. Our script starts `tcpsvd` listening on
port 9999 for an unauthenticated root shell.

**To get started:** See `sd_root/README.md` for step-by-step
instructions. Copy the `sd_root/seculinkIdRecycle/` folder to a FAT32
SD card, insert it, power cycle, and connect with `nc <camera-ip> 9999`.

The stock rootfs `/etc/shadow` has an empty root password (previously
believed to be `sl.x.` -- incorrect). Our `debug.sh` sets a real
password at boot from a hash stored on configfs. The port-9999 shell
is unauthenticated (legacy fallback).

## What lives where on the camera

| Location | Mount | Persists? | Contents |
|----------|-------|-----------|----------|
| configfs (`/etc/conf.d/`) | jffs2 on flash | Reboots + factory reset | `debug.sh`, SSH keys, password hash, device config |
| SD card (`/progs/rec/00/`) | FAT32 on mmcblk0p1 | Reboots + factory reset | Our binaries (dropbearmulti+copies, recv, ipc_daemon, driver), recordings |
| rootfs (`/`) | squashfs on flash | Read-only | BusyBox, base OS (1.25 MB) |
| appfs (`/progs/`) | squashfs on flash | Read-only | superb, SDK libs (5 MB) |
| `/etc/` | tmpfs (RAM) | **Lost on reboot** | Runtime config (shadow, shells, dropbear keys -- restored by debug.sh) |
| `/root/` | tmpfs (RAM) | **Lost on reboot** | SSH authorized_keys -- restored by debug.sh |

Repo backups: `tools/debug.sh` (boot script), `tools/camera_authorized_keys`
(SSH public key), `tools/build_dropbear.sh` + `tools/dropbear_localoptions.h`
(rebuild dropbear). Host keys and password hash are on camera configfs only --
regenerate if lost.

## Note for AI coding assistants

**Do not commit or stage files** in this repo without being explicitly
asked.

**Treat `research/` as read-only.** The subdirectories under `research/`
are git submodules pointing to external repos we do not control. Never
modify, add, or delete files inside them. If you need a file from
`research/` for building or deployment, copy it into the appropriate
location under `driver/` first:

- SDK headers → `driver/prebuilt/sdk_include/`
- SDK shared libs → `driver/prebuilt/sdk_mpi/` or `driver/prebuilt/isp_plugins/`
- RTSP library → `driver/rtsp/lib/`, `driver/rtsp/include/`, `driver/rtsp/src/`
- PQ libraries → `driver/prebuilt/pq/`

See `driver/README.md` for the full vendored file inventory and origins.

## See also

- `sd_root/README.md` -- **getting root access** (start here if you
  just want a shell on the camera)
- `ROADMAP.md` -- full project plan with Phases 0-8+
- `CAMERA.md` -- camera hardware, stock firmware, protocols, cloud
  architecture, control surfaces, security assessment
- `DRIVER.md` -- sensor driver reference (architecture, register map,
  I2C sync path, AE/AWB calibration, NR pipeline, build/deploy)
- `DRIVER_INTERNALS.md` -- kernel decompilation forensics, ioctl
  tables, binary comparison, investigation timeline
- `driver/README.md` -- build system, directory structure, vendored file
  origins
- `research/archive/` -- historical research docs, research notes,
  investigative tools
