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
| Shell access | `tools/cam_cmd.py` (tcpsvd on port 9999) |
| Fast file transfer | `recv` on port 8888 + `tools/send_file.py` |
| Slow file fallback | `tools/deploy_file.py` (base64 over shell) |
| RTSP stream | `rtsp://192.168.1.153:554/live0` |
| Root password | `sl.x.` (rootfs `/etc/shadow`, for UART login) |
| Build environment | WSL Ubuntu + ARM cross toolchain |

## Talking to the camera

### Shell access (port 9999)

The camera runs a `tcpsvd` root shell on port 9999 (installed by us
via SD-card jailbreak, not factory). Use `cam_cmd.py` to send
commands:

```bash
# Run a command on the camera
python tools/cam_cmd.py "ls /progs/rec/00/ipc_drv/"

# Interactive-ish (each call is a new connection)
python tools/cam_cmd.py "cat /proc/umap/isp"
python tools/cam_cmd.py "ps | grep pipeline"
```

### Uploading files (port 8888 -- primary method)

For fast binary transfer, we use `recv` (a tiny TCP file receiver
compiled for ARM) running on the camera, paired with `send_file.py`
on the PC.

```bash
# 1. Start recv daemon on camera (one-time per boot, persists)
python tools/cam_cmd.py "pidof recv || (nohup /progs/rec/00/ipc_drv/recv 8888 /progs/rec/00/ipc_drv -d > /dev/null 2>&1 &)"

# 2. Send files from PC
python tools/send_file.py 192.168.1.153 8888 driver/build/libsns_sc635hai.so driver/build/pipeline_test

# 3. Set permissions
python tools/cam_cmd.py "chmod +x /progs/rec/00/ipc_drv/pipeline_test"
```

The `recv` source is at `tools/recv.c`. In daemon mode (`-d`), it
listens indefinitely and accepts multiple connections. Each connection
sends `<filename>\n<raw bytes>`, and recv writes the file to the
target directory.

### Uploading files (base64 fallback)

If recv isn't running yet (first deployment after a factory reset),
use the slow base64-over-shell method:

```bash
python tools/deploy_file.py driver/build/recv /progs/rec/00/ipc_drv/recv
python tools/cam_cmd.py "chmod +x /progs/rec/00/ipc_drv/recv"
# Now start recv daemon and switch to fast method above
```

### Downloading files from camera

```bash
python tools/pull_file.py /progs/rec/00/ipc_drv/capture.h265
python tools/pull_file.py /tmp/pipeline.log pipeline_output.log
```

Uses a reverse-connect approach: PC listens, camera connects back
and sends the file.

### Batch redeployment

After a reboot wipes the SD card overlay:

```powershell
# Redeploys all binaries + SDK libs to camera (uses send_file.py)
.\tools\redeploy_all.ps1
```

## Building

Requires WSL (the ARM cross-toolchain is Linux x86_64 ELF binaries):

```bash
# From WSL:
cd /mnt/e/Projects/ipc_XMeye_camera/driver
make all
```

Outputs in `driver/build/`:
- `libsns_sc635hai.so` -- sensor driver shared library
- `pipeline_test` -- full video pipeline binary (future daemon)
- `reg_dump` -- I2C register dump tool
- `sensor_test` -- standalone sensor test
- `awb_dump` -- ISP AWB calibration reader

Toolchain: `research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/`

## Running the RTSP stream

```bash
# Deploy files first (see above), then:

# Option A: Launch via cam_cmd (script handles mySystem/superb)
python tools/cam_cmd.py "setsid /progs/rec/00/ipc_drv/rtsp_run.sh </dev/null &>/dev/null &"

# Option B: Manual launch
python tools/cam_cmd.py "killall -STOP mySystem; killall -9 superb; sleep 1"
python tools/cam_cmd.py "cd /progs/rec/00/ipc_drv && LD_PRELOAD='libbnr.so libdrc.so libacs.so libcalcflicker.so libir_auto.so libldci.so libdehaze.so libextend_stats.so' LD_LIBRARY_PATH=/progs/rec/00/ipc_drv ./pipeline_test --rtsp"

# View in VLC or ffplay:
ffplay rtsp://192.168.1.153:554/live0
```

`pipeline_test` feeds `/dev/watchdog` internally. When it exits,
`rtsp_run.sh` resumes mySystem, which restarts superb.

## Repository structure

```
ipc_XMeye_camera/
  ROADMAP.md              Project plan (Phases 0-8+)
  README.md               This file

  driver/                 Sensor driver + pipeline code
    src/                  Production driver source
      sc635hai_cmos.c       ISP/AE/AWB callbacks
      sc635hai_sensor_ctl.c I2C init + register control
      sc635hai_cmos.h       Constants, gain tables, register map
      hi_compat.h           hi_* -> ss_mpi_* API compat macros
    rtsp/                 RTSP server for pipeline_test
    test/                 Test programs
      pipeline_test.c       Full pipeline (basis for future daemon)
      reg_dump.c            I2C register dump utility
      sensor_test.c         Standalone I2C sensor test
      awb_dump.c            ISP AWB calibration reader
    Makefile              Cross-compilation build
    README.md             Driver architecture + build details

  tools/                  Host-side utilities
    cam_cmd.py              Send shell commands via tcpsvd (port 9999)
    recv.c                  Camera-side TCP file receiver (port 8888)
    send_file.py            PC-side file sender (pairs with recv)
    deploy_file.py          Slow fallback: base64 over shell
    pull_file.py            Download files from camera (reverse connect)
    redeploy_all.ps1        Batch redeploy after reboot
    rtsp_run.sh             Production RTSP launch script (runs on camera)
    start_pipeline_bg.sh    Detached pipeline launcher (runs on camera)
    run_pipeline_bg.sh      Pipeline + diagnostic capture (runs on camera)
    diag_run.sh             Full diagnostic run w/ sensor probing (on camera)
    probe_after_test.sh     Post-test sensor state probe (on camera)
    fix_timezone.py         Change camera timezone from UTC+8
    ghidra/                 Ghidra RE scripts + decompilation output
      scripts/                Analysis scripts (Java + Python)
      output/                 Decompilation results, including
                              sensor_i2c_kernel.md (kernel sync forensics)

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
| RAM | 128 MB (split ~64 OS / ~64 MMZ for video) |
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
- **No dropbear/SSH yet.** The camera has no SSH server. Access is
  via the tcpsvd backdoor we installed. Dropbear deployment is
  planned for Phase 3.

## SD card jailbreak

The initial root shell was obtained via the `recycle_ali.sh` SD card
vector: place a script on SD, camera executes it at boot. Our script
starts `tcpsvd` listening on port 9999 for unauthenticated shell
access. This is NOT a factory backdoor -- we installed it.

The cracked root password (`sl.x.`) applies to rootfs `/etc/shadow`
(for UART console login). The port-9999 shell is unauthenticated.

## Note for AI coding assistants

**Do not commit or stage files** in this repo without being explicitly
asked.

## See also

- `ROADMAP.md` -- full project plan with Phases 0-8+
- `CAMERA.md` -- camera hardware, stock firmware, protocols, cloud
  architecture, control surfaces, security assessment
- `DRIVER.md` -- sensor driver reference (architecture, register map,
  I2C sync path, AE/AWB calibration, NR pipeline, build/deploy)
- `DRIVER_INTERNALS.md` -- kernel decompilation forensics, ioctl
  tables, binary comparison, investigation timeline
- `driver/README.md` -- older driver overview (mostly superseded by
  `DRIVER.md`)
- `research/archive/` -- historical research docs, research notes,
  investigative tools
