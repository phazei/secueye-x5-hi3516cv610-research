# PQTools Tuning Setup (SC635HAI)

Trimmed PQTools configuration for live ISP tuning on our SC635HAI camera.
This directory is the source-of-truth -- `sd_card/PQtool/` is built from it.

## What this is

HiSilicon's PQTools lets you adjust ISP parameters (exposure, white balance,
noise reduction, sharpening, gamma, DRC, etc.) in real-time from a Windows PC
application while viewing the live RTSP stream.

**`ittb_control`** runs as a sidecar process alongside the running camera
pipeline (superb or pipeline_test). It does NOT start its own pipeline or
produce any video -- it only reads/writes ISP parameters on the existing
ISP pipe via `ss_mpi_isp_set_*/get_*` MPI calls.

## Architecture

```
Camera board                              Windows PC
┌──────────────┐                         ┌───────────────┐
│ superb or    │─── RTSP stream ────────>│ VLC / player  │
│ pipeline_test│                         │               │
│              │                         │               │
│ ittb_control │<── port 4321 TCP ──────>│ PQTools.exe   │
│  (sidecar)   │   ISP param r/w        │ (tuning GUI)  │
└──────────────┘                         └───────────────┘
```

## On-device usage

```sh
# 1. Camera pipeline must already be running (superb or pipeline_test)

# 2. Start ittb_control
cd /progs/rec/00/PQtool
./start_pqcontrol.sh

# 3. On PC: open PQTools.exe, connect to <camera_ip>:4321
#    View live feed via RTSP at rtsp://<camera_ip>:554/stream0

# 4. Stop when done
killall ittb_control
```

## Directory layout

```
pq_tune/
├── config.cfg                    # Main config (sensor, ports, module IDs)
├── ittb_control                  # Control daemon binary (from SDK PQ package)
├── start_pqcontrol.sh            # Launch script (control-only mode)
├── README.md                     # This file
├── configs/
│   ├── sc635hai/
│   │   ├── config_entry.ini      # Mode selector (6M20 linear)
│   │   └── sc635hai_6M20.ini     # Full pipeline descriptor for our sensor
│   └── common/
│       ├── config_mt.ini         # Media transport (RTSP/HTTP) settings
│       └── config_stream.ini     # Stream server port
└── libs/
    ├── libsns_sc635hai.so        # Our custom sensor driver
    ├── libot_mpi_isp.so          # ISP MPI (large, ~376KB)
    ├── libss_mpi_ae.so           # Auto-exposure algorithm
    ├── libss_mpi_awb.so          # Auto-white-balance algorithm
    ├── libss_mpi_isp.so          # ISP MPI wrapper
    ├── libss_mpi.so              # Core MPI
    └── ... (27 libs total)       # All runtime deps for ittb_control
```

## Caveats

- **superb overwrites some params**: superb continuously re-applies CSC
  (brightness/contrast/saturation) from cloud commands. Changes to those
  params via PQTools may be overwritten within one frame.
  Works better with pipeline_test which leaves ISP params alone after init.

- **Sensor driver must stay in sync**: `libs/libsns_sc635hai.so` here must
  match the version used by the running pipeline. After rebuilding the
  driver (`driver/build/libsns_sc635hai.so`), copy it here:
  ```
  cp driver/build/libsns_sc635hai.so tools/pq_tune/libs/
  ```

- **ittb_control does NOT initialize the sensor**. It only reads/writes ISP
  module parameters on an already-running pipe. If no pipeline is running,
  it will start but PQTools.exe won't be able to do anything useful.

## What was removed from the stock SDK PQ package

The original `Hi3516CV610_PQ_V1.0.2.1` package was ~10MB with 91 files.
This trimmed version drops:

| Removed | Reason |
|---------|--------|
| `ittb_stream` binary | Not needed -- we use our own RTSP stream |
| `PQTools.sh` / `StartControl.sh` | Replaced by `start_pqcontrol.sh` |
| `configs/sc4336p/` | Wrong sensor |
| `configs/sc450ai/` | Wrong sensor |
| `configs/sc500ai/` | Wrong sensor |
| `configs/hy006_3814_0011/` | Wrong sensor |
| `configs/template_sns/` | Not needed |
| `configs/*_aibnr/` (all) | AIBNR not used |
| `configs/common/aibnr_model_*.bin` | AIBNR model files |
| `configs/common/yolov8.om` | AI detection model (unused) |
| `configs/webserver.conf` | For ittb_stream's web viewer |
| `libs/libsns_sc4336p.so` | Wrong sensor driver |
| `libs/libsns_sc450ai.so` | Wrong sensor driver |
| `libs/libsns_sc500ai.so` | Wrong sensor driver |
| `libs/libsns_hy006_3814_0011.so` | Wrong sensor driver |
| `libs/libsns_template_sns.so` | Template stub |

## Deploying to SD card

Copy this entire directory to the SD card as `PQtool/`:
```sh
# From project root on the host
cp -r tools/pq_tune/ <sd_mount>/PQtool/
```

Or to update just the in-tree sd_card mirror:
```sh
rm -rf sd_card/PQtool/
cp -r tools/pq_tune/ sd_card/PQtool/
```
