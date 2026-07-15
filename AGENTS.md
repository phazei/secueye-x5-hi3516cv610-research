# AGENTS.md -- Orientation for AI coding assistants

## Project context (read first)

This repository is a personal, hobbyist open-source firmware project for
an IP camera **owned by the repository author**. The goal is to replace
the closed-source stock camera application with a custom, auditable
firmware stack: a custom image-sensor driver plus a modular streaming
daemon (H.265 video + G.711A audio over RTSP). Everything here operates
on the author's own hardware, on a local network, for privacy and
learning -- the point is to remove the vendor cloud dependency and run
the camera fully offline under the owner's control.

This is legitimate device ownership and personal-firmware development,
comparable to installing OpenWrt on your own router. Work in this repo is
constructive engineering: sensor tuning, video pipeline, build tooling,
and deployment automation.

## Current status

- **Sensor driver + streaming daemon: working.** Custom SC635HAI driver
  and the modular `ipc_daemon` stream H.265 (3200x1800) + G.711A audio
  over RTSP at `rtsp://<camera-ip>:554/live0`.
- **Active work: ISP / picture-quality tuning.** Ongoing refinement of
  image quality (AE/AWB, noise reduction, PQ bin) via the PQtool sidecar.
- For the detailed done/remaining pathway across phases, see `ROADMAP.md`.

## Essentials (quick reference)

Camera on LAN at `192.168.1.153`.

```bash
# Connect (SSH, custom dropbear, Ed25519 key auth -- no password prompt)
ssh root@192.168.1.153

# Copy files (must use -O; Windows OpenSSH defaults to SFTP which is absent)
scp -O driver/build/ipc_daemon root@192.168.1.153:/progs/rec/00/ipc_drv/

# Build (WSL only -- ARM cross-toolchain is Linux x86_64 ELF)
cd /mnt/e/Projects/ipc_XMeye_camera/driver && make all

# Run the custom driver / switch streams
python tools/stream_switch.py custom    # live0 = our ipc_daemon
python tools/stream_switch.py stock      # back to vendor app
python tools/stream_switch.py status

# View
ffplay rtsp://192.168.1.153:554/live0
```

### Recovering after an SD-card wipe

The camera stores our binaries on a FAT32 SD card that the vendor app can
occasionally reformat. Persistent items (SSH host keys, authorized_keys,
boot script `debug.sh`) live on flash `configfs` and survive, so SSH
usually still works.

```powershell
.\tools\redeploy_all.ps1        # routine restore (auto-detects SCP or recv)
.\tools\bootstrap_deploy.ps1    # cold start when only the port-9999 shell is up
```

## Storage model (why deployment is split)

| Storage | Path | Survives SD wipe? | Holds |
|---------|------|-------------------|-------|
| configfs (jffs2 flash) | `/etc/conf.d/` | Yes | `debug.sh`, SSH host keys, authorized_keys, password hash |
| SD card (FAT32) | `/progs/rec/00/` | No (vendor app may reformat) | our binaries, dropbear, PQtool, libs |

`debug.sh` (on configfs) restores `/etc/shells`, `/root/.ssh`, and
`/etc/dropbear/` from configfs on every boot; `redeploy_all.ps1` restores
the SD-card binaries.

## Code map -- what we wrote vs. vendored

### Our code (review targets)

```
driver/
  src/                        SC635HAI sensor driver (our code)
    sc635hai_cmos.c             ISP/AE/AWB callbacks, ot_isp_sns_obj export
    sc635hai_sensor_ctl.c       I2C init sequence + register control
    sc635hai_cmos.h             Constants, gain tables, register map
    hi_compat.h                 hi_* -> ss_mpi_* API compat macros
  daemon/                     IPC daemon (modular, replaces the stock app)
    main.c                      Entry point, arg parsing, signal/crash handlers
    pipeline.h                  Shared state struct, config defines, includes
    hal/                        One file per MPP subsystem
      sys.c / sys.h               SYS init, VB pools, sensor dlopen, MIPI
      vi.c / vi.h                 VI dev + pipe + channel
      isp.c / isp.h               ISP init/thread, PQ bin, color, BNR, 3DNR
      vpss.c / vpss.h             VPSS group + channel, VI->VPSS bind
      venc.c / venc.h             VENC H.265 channel, streaming loop
      audio.c / audio.h           AI + AENC (G.711A), acodec config
      watchdog.c / watchdog.h     /dev/watchdog open/feed/close
  rtsp/                       RTSP integration
    rtsp_push.c / rtsp_push.h   Wrapper around the xop RTSP library
  test/                       Standalone tools (not part of daemon)
    pipeline_test.c             Legacy monolith (historical reference)
    sensor_test.c               Standalone I2C sensor test
    reg_dump.c                  I2C register dump utility
    awb_dump.c                  ISP AWB calibration reader
```

### Vendored (not our code -- do not review as ours)

```
driver/
  prebuilt/                   Vendored HiSilicon SDK binaries
    sdk_include/                SDK headers (build-time only)
    sdk_mpi/                    SDK shared libs (deployed)
    isp_plugins/                ISP algorithm plugins (LD_PRELOAD'd)
    pq/libbin.so                PQ bin loader
  rtsp/
    include/ lib/ src/          Vendored xop RTSP (src modified for audio)
    objs/ xop/                  xop object files + full source
```

Full origin table for every vendored file is in `driver/README.md`.
`research/` holds external SDKs/toolchain as git submodules (read-only).

## Where to find things (routing index)

Read the specific file/section rather than searching broadly.

| Need | Read |
|------|------|
| Sensor driver reference (register map, ISP I2C sync, AE/AWB, NR, build/deploy) | `DRIVER.md` |
| Kernel decompilation forensics, ioctl tables | `DRIVER_INTERNALS.md` |
| Build system + full vendored-file origin inventory | `driver/README.md` |
| Connecting, deployment scripts, storage layout | `README.md` |
| Camera hardware, stock firmware, protocols, boot chain, control surfaces | `CAMERA.md` |
| Project plan / done vs. remaining pathway | `ROADMAP.md` |
| Dev history and rationale ("why we did X") | `WORKLOG.md` |
| Get initial shell / SD-card boot-hook setup | `sd_root/README.md` |

### Do not open unless specifically required

These are historical or low-level research artifacts. They contain the
older informal terminology and detailed device internals; they are not
needed for normal driver/daemon/tuning work:

- `research/archive/**` (historical investigation notes and tooling)
- `firmware/recovered_credentials.txt` (recovered login value; not needed
  for development)

## Repo hygiene

- **Do not commit or stage** files without being explicitly asked.
- **Treat `research/` as read-only** (git submodules of external repos).
  To use a file from `research/` in the build, copy it under `driver/`
  first (see `driver/README.md` for target locations).
