# Custom ARM Binaries for SECUEYE X5

Cross-compiled ARM binaries that run on the camera alongside or instead of
the stock `superb` firmware.

## Prerequisites

- **WSL (Windows Subsystem for Linux)** -- The cross-compiler is a Linux x86_64
  binary. It must run under WSL, not directly on Windows.
- The toolchain at `research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/`

## Quick Start

```bash
# From WSL:
cd /mnt/e/Projects/ipc_XMeye_camera/custom
make httppost        # Build the HTTP POST client
make alarm_watcher   # Build the alarm watcher daemon
make all             # Build everything
```

## Deployment

Copy the built binary to the camera's SD card:

```bash
# From Windows (PowerShell):
python tools/cam_cmd.py "ls /progs/rec/00/custom/"

# Or via the root shell, copy from SD card:
# 1. Put binary on SD card (e.g. via card reader or SCP-like transfer)
# 2. Or upload via base64 over the root shell (for small binaries)
```

The `deploy.sh` script automates uploading small binaries via base64 over
the root shell:

```bash
# From WSL:
./deploy.sh httppost
```

## Binaries

| Binary | Purpose | Size (est.) |
|--------|---------|-------------|
| `httppost` | Minimal HTTP POST client for webhooks | ~30 KB static |
| `alarm_watcher` | Monitors superb.log, fires webhooks on alarm | ~40 KB static |
| `npu_reader` | Reads NPU detection results via SVP ACL | TBD |

## Compiler Flags

Target: ARM Cortex-A7 with NEON/VFPv4, musl libc, soft-float ABI.

```
CFLAGS = -mcpu=cortex-a7 -mfloat-abi=softfp -mfpu=neon-vfpv4 -mthumb
LDFLAGS = -static
```

Static linking is required because the camera's musl libc version may differ
slightly from the toolchain's. Static binaries are self-contained and work
regardless of the target's shared libraries.
