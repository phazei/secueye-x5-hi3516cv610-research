# Research Materials for SECUEYE X5 / Hi3516CV610

This directory contains third-party SDK materials, toolchains, and reference
projects for the HiSilicon Hi3516CV610 SoC used in the SECUEYE X5 camera.

> **Important:** Nothing in this directory is original project code. These are
> external resources gathered during investigation. The HIVIEW folder is a full
> Git checkout of the OpenHisilicon/HIVIEW project. The toolchain folder is a
> separate Git checkout of a Hi3516CV610 cross-compiler repository.

---

## Contents

### `Hi3516CV610 超高清智慧视觉 SoC 产品简介.pdf`

HiSilicon's official product brief (datasheet) for the Hi3516CV610 SoC.
Version 02, dated 2024-03-31. Confirms:

| Spec | Value |
|------|-------|
| CPU | Dual-core ARM Cortex-A7 @ 950MHz, NEON + FPU |
| NPU | 1 TOPS, Transformer acceleration |
| Video | 4K@20 / 6M@30, H.265 + H.264 + SVAC3.0 |
| RAM | Built-in 1Gb DDR3/3L @ 2133Mbps |
| Flash | SPI NOR (32MB max), SPI NAND (512MB), eMMC4.5 |
| Network | Integrated 10/100 Ethernet PHY, 2x SDIO2.0 (WiFi) |
| USB | USB2.0 Host/Device |
| Security | TrustZone, AES/RSA/ECC/SHA, SM2/3/4 |
| SDK | Linux 5.10, OpenHarmony 4.1 |
| Package | QFN 9x9mm, 0.35mm pitch |
| Variants | 00B/10B/20B/00S/20S/00G/20G |

Our camera has the Hi3516CV610-20S variant.

---

### `HIVIEW/` -- OpenHisilicon HIVIEW Project

**Source:** https://github.com/openhisilicon/HIVIEW
**Company:** Shenzhen Hiview Science and Technology Co., Ltd.
**License:** Open source (full Git checkout)

HIVIEW is a complete, open-source alternative camera firmware platform for
HiSilicon SoCs. It targets the same Hi3516CV610 chip as our SECUEYE X5 camera
but uses a modular, open architecture instead of XMeye's proprietary monolithic
`superb` binary with Alibaba IoT cloud lock-in.

#### Why This Matters

1. **Complete MPP SDK for our exact chip** (`mod/mpp/3516c/`)
2. **NPU/SVP sample code** with working YOLO inference (`mod/svp/3516c/`)
3. **Cross-compiler configuration** identifying the exact toolchain
4. **Pre-compiled libraries** for HTTP, SSL, JSON, streaming protocols
5. **Web server and UI** source code targeting our SoC
6. **Reference architecture** for building custom camera software

#### Architecture

HIVIEW uses a modular design where each component runs as a separate process,
communicating via nanomsg IPC (pub/sub over Unix domain sockets):

| Module | Purpose |
|--------|---------|
| BSP | System init, network, WiFi, watchdog, SADP discovery, firmware upgrade |
| CODEC | ISP, VENC, VDEC, VO (video output) |
| SVP | Smart Vision Platform (YOLO object detection, LPR) |
| REC | Video recording (fragmented MP4) |
| RTSPS | RTSP server/client/push |
| RTMPS | RTMP push |
| WEBS | HTTP/HTTPS/WebSocket/WebRTC server (Mongoose-based) |
| ONVIF | NVT/NVC (gSOAP-based) |
| SIPS | SIP/GB28181 |
| SRTS | SRT/UDP/RTP/SRTP |
| UVC | USB Video Class gadget |
| APP | NVR/GUI |

#### Key Directories

```
HIVIEW/
  build/3516c/            Cross-compiler config for Hi3516CV610
  mod/mpp/3516c/          Complete MPP SDK (headers, libs, kernel modules)
    inc/hisisdk/          200+ SDK headers (ISP, NPU, VPSS, VI, VENC, audio...)
    lib/hisisdk/          60+ shared libraries (.so)
    ko/                   42 kernel modules (.ko) + load scripts
    src/                  MPP init code, sample common code, ISP scene auto
  mod/svp/3516c/          NPU/SVP code with working YOLO inference
    svp_npu/              Full inference pipeline (init, detect, post-process)
    model/                Pre-trained YOLOv5 + YOLOv8 .om models
    common/               SVP structures, NPU model management headers
  mod/webs/               Web server (Mongoose) + full web UI
  mod/onvif/              ONVIF implementation (gSOAP)
  mod/bsp/                BSP (network, WiFi, upgrade, watchdog)
  fw/                     Framework libraries:
    libhttp/              HTTP library
    cjson/                cJSON
    nm/                   nanomsg IPC
    librtsp/, librtp/     RTSP/RTP
    librtmp/              RTMP
    gsoap/                gSOAP (ONVIF)
    openssl/              OpenSSL headers
    sqlite/               SQLite
    opencv/               OpenCV headers (3.4, 4.5)
    freetype/             Font rendering
    ...
  lib/3516c/              Pre-compiled shared libraries:
    libhttp.so            HTTP
    libssl.so, libcrypto.so  OpenSSL
    libcjson.so           JSON
    librtsp.so, librtp.so RTSP/RTP
    librtmp.so            RTMP
    libnm.so              nanomsg
    libmov.so, libmpeg.so Media containers
    libsip.so             SIP/GB28181
    ...
  bin/                    Pre-compiled executables (only for 3403 and 3531d,
                          NOT for 3516c -- must be built from source)
  res/                    Photos of hardware boards and UI screenshots
  tools/                  minizip/miniunz (x86 Linux, not ARM)
```

#### MPP SDK Headers (Most Relevant)

| Header | Purpose |
|--------|---------|
| `svp_acl.h` | SVP ACL (AI Compute Library) -- NPU model loading and inference |
| `svp_acl_mdl.h` | NPU model management |
| `svp_acl_rt.h` | NPU runtime (memory, streams, events) |
| `hi_mpi_isp.h` | ISP control (exposure, white balance, image quality) |
| `hi_mpi_vpss.h` | Video Processing Subsystem |
| `hi_mpi_vi.h` | Video Input |
| `hi_mpi_venc.h` | Video Encoder |
| `hi_mpi_ao.h` | Audio Output (speaker volume, etc.) |
| `hi_mpi_ai.h` | Audio Input (microphone) |
| `hi_mpi_ive.h` | Intelligent Video Engine (motion detection) |
| `hi_mpi_rgn.h` | Region/OSD overlay |
| `hi_mpi_sys.h` | System control |
| `ot_common_svp_npu.h` | NPU common types and constants |

#### NPU Sample Code (mod/svp/3516c/)

Working code showing how to:
1. Load a YOLO `.om` model onto the NPU
2. Capture frames from the VPSS video pipeline
3. Run inference via `sample_svp_npu_detect()`
4. Post-process results (NMS, confidence filtering)
5. Draw bounding box overlays via VGS
6. Publish detection results via nanomsg

This is directly applicable to our goal of reading detection results from
the camera's NPU without relying on `superb`'s internal callbacks.

#### Sensor Drivers

The SDK includes sensor drivers for: gc4023, os04d10, sc431hai, sc4336p,
sc450ai, sc500ai. Our camera uses the SC635HAI sensor, which is NOT in this
list (it's in the XMeye/Xiongmai firmware only). However, the SC635HAI is
similar to the SC450AI/SC500AI family and the sensor-independent parts of
the SDK (ISP pipeline, NPU, video encoder, audio) work identically.

#### Differences from Our Camera's XMeye Firmware

| Aspect | HIVIEW (Open) | XMeye/SECUEYE (Proprietary) |
|--------|--------------|----------------------------|
| Architecture | Modular processes via nanomsg | Monolithic `superb` binary |
| Cloud | None (local only) | Alibaba IoT MQTT (cloud-locked) |
| Protocols | RTSP, RTMP, ONVIF, SIP, SRT, WebRTC | RTSP, ONVIF (facade), DVRIP (stub) |
| Detection | YOLO models, local processing | Custom `det_hv_hor.bin`, cloud push |
| Web UI | Mongoose + w2ui (full featured) | None (app only) |
| Configuration | JSON files, web API | SystemCfg.ini + cloud MQTT |
| Source | Open on GitHub | Closed, reverse-engineered via Ghidra |

---

### `toolchain/` -- Hi3516CV610 Cross-Compiler

**Source:** Separate Git repository (single commit: "新建KODO-HI3516CV610编译工具链仓库")
**Path:** `toolchain/gcc-20240318-arm-v01c02-linux-musleabi/`

Complete cross-compilation toolchain for Hi3516CV610.

| Component | Details |
|-----------|---------|
| GCC | 10.3.0 |
| musl libc | 1.2.3 |
| Linux headers | 5.10 |
| Target | `arm-linux-musleabi` (ARM 32-bit, musl C library) |
| Build date | 2024-03-18 |
| SDK version | V12CS61.005.010 |
| Host | Linux x86_64 (requires WSL/VM on Windows) |

#### Multilib Configurations

| Variant | Flags | Use Case |
|---------|-------|----------|
| Default | (generic ARM) | Basic compilation |
| `a7_soft` | `-mcpu=cortex-a7 -mfloat-abi=soft` | Software float |
| `a7_softfp_neon-vfpv4` | `-mcpu=cortex-a7 -mfloat-abi=softfp -mfpu=neon-vfpv4` | **Recommended for our camera** |
| `a7_hard_neon-vfpv4` | `-mcpu=cortex-a7 -mfloat-abi=hard -mfpu=neon-vfpv4` | Hard float (if firmware uses it) |

Our camera's firmware uses `softfp` (confirmed by HIVIEW's `build/3516c` config:
`-mfloat-abi=softfp`). The recommended flags for cross-compilation are:

```
-mcpu=cortex-a7 -mfloat-abi=softfp -mfpu=neon-vfpv4 -mthumb
```

#### Included Tools

- `arm-v01c02-linux-musleabi-gcc` -- C compiler
- `arm-v01c02-linux-musleabi-g++` -- C++ compiler
- `arm-v01c02-linux-musleabi-as` -- Assembler
- `arm-v01c02-linux-musleabi-ld` -- Linker
- `arm-v01c02-linux-musleabi-strip` -- Strip symbols
- `arm-v01c02-linux-musleabi-objdump` -- Disassembler
- `arm-v01c02-linux-musleabi-readelf` -- ELF reader
- `arm-v01c02-linux-musleabi-gdb` -- Debugger
- Full set of binutils (ar, nm, objcopy, ranlib, size, strings, etc.)

#### Included Libraries

Sysroot at `arm-v01c02-linux-musleabi-gcc/target/usr/`:

| Library | Static | Shared | Notes |
|---------|--------|--------|-------|
| musl libc | 1.8 MB | 345 KB | Full C library |
| libstdc++ | 3.0 MB | 636 KB | C++ standard library |
| libgcc_s | - | 38 KB | GCC runtime |
| libsecurec | 87 KB | 46 KB | HiSilicon secure C (memcpy_s etc.) |
| libmathlib | 366 KB | 42 KB | Extended math |
| libtirpc | 229 KB | 103 KB | RPC |
| libiconv | 146 KB | - | Character encoding |
| libcrypt | 43 KB | - | Crypt |
| libpthread | stub | - | Integrated into musl libc |

#### Installation

The toolchain binaries are Linux x86_64 ELF executables. On Windows, use
WSL (Windows Subsystem for Linux) to run them:

```bash
# In WSL, the toolchain is accessible at the Windows path:
export TC=/mnt/e/Projects/ipc_XMeye_camera/research/toolchain/gcc-20240318-arm-v01c02-linux-musleabi/arm-v01c02-linux-musleabi-gcc
export PATH=$TC/bin:$PATH

# Compile a static ARM binary:
arm-v01c02-linux-musleabi-gcc -mcpu=cortex-a7 -mfloat-abi=softfp -mfpu=neon-vfpv4 \
  -static -o hello hello.c

# Deploy to camera via SD card or root shell
```

---

## What Can We Build With This?

### Immediately Feasible

1. **Static `wget`/HTTP POST binary** -- Cross-compile a minimal HTTP client
   (or just raw socket code in C) to enable webhook calls from the camera.
   Deploy via SD card. ~20-50KB static binary.

2. **Alarm watcher daemon** -- C program that reads `/tmp/superb.log` via
   `inotify` or `poll`, detects alarm patterns, writes structured event files,
   and optionally fires HTTP webhooks.

3. **Busybox with more applets** -- Cross-compile busybox with `httpd`, `wget`,
   `nc` enabled. Replace or supplement the firmware's stripped-down busybox.

### Medium Effort

4. **NPU detection reader** -- Using the SVP ACL headers (`svp_acl.h`) and
   sample code, build a program that reads detection results from the NPU
   alongside `superb`. This gives direct access to bounding boxes, confidence
   scores, and object classes without log scraping.

5. **Lightweight web server** -- Cross-compile Mongoose (already in HIVIEW) to
   run on the camera. Serve a settings/status page from the SD card. Could
   also proxy the RTSP stream and display alarm history.

6. **ISP control tool** -- Using `hi_mpi_isp.h`, build a tool that calls
   `hi_mpi_isp_set_csc_attr()` directly instead of writing registers via
   `bspmm`. This might work where register writes didn't (if ISP auto-tuning
   only overwrites the register but respects the API-level setting).

### Major Effort

7. **Replace `superb` entirely** -- Use HIVIEW as a reference to build a
   complete custom firmware. Would require writing an SC635HAI sensor driver
   (or adapting one from the XMeye firmware). Gains: full local control, no
   cloud dependency, standard protocols, web UI.

8. **Local MQTT bridge** -- Build a program that uses the HiSilicon SDK to
   intercept `superb`'s MQTT traffic or inject commands into its internal
   message bus (HI_XUID commands).

---

## Camera Partition Layout and Boot Chain

### Flash Partitions (16MB SPI NOR)

| Partition | MTD | Size | Filesystem | Writable | Contents |
|-----------|-----|------|------------|----------|----------|
| boot | mtd0 | 320 KB | raw | No (U-Boot) | U-Boot bootloader |
| bootargs | mtd1 | 64 KB | raw | U-Boot env | Boot parameters |
| kernel | mtd2 | 2 MB | FIT image | No | Linux 5.10.221 (armv7l) |
| rootfs | mtd3 | 1.25 MB | squashfs | No | Minimal root (busybox, init) |
| appfs | mtd4 | 5 MB | squashfs | No | `superb`, kernel modules, libs, scripts |
| configfs | mtd5 | 1 MB | jffs2 | **Yes** | SystemCfg.ini, debug.sh, hwconfig |
| resfs | mtd6 | 6.375 MB | squashfs | No | Sensor PQ bins, WiFi/BLE, IVP model, voice |

**SD card**: 29.1 GB FAT32 at `/progs/rec/00` (1.5 GB used for recordings).

### Storage Summary for Custom Software

| Location | Available | Writable | Persists Reboot | Use Case |
|----------|-----------|----------|-----------------|----------|
| `/etc/conf.d/` (configfs) | ~808 KB free | Yes | Yes | Config, small scripts |
| `/tmp/` (tmpfs) | ~17.5 MB free | Yes | No | Runtime binaries, logs |
| SD card `/progs/rec/00/` | ~27.6 GB free | Yes | Yes | **Primary deployment target** |

Custom ARM binaries should be deployed to the SD card. The `debug.sh` backdoor
on configfs already demonstrates the pattern: configfs script launches programs
from tmpfs or SD card.

### Partition Contents Detail

**16MB SPI NOR is standard** for this class of IP camera. Most WiFi IP cameras
use 8MB or 16MB SPI NOR -- it's cheap, simple, and boots instantly. OpenIPC
targets 8MB as their minimum. Higher-end devices (NVRs, dashcams) use NAND or
eMMC, but standalone WiFi cameras almost always use SPI NOR. The SoC supports
up to 32MB SPI NOR, so there's an upgrade path if needed.

Everything runs from RAM (64MB DDR). Flash is just compressed storage -- squashfs
partitions are decompressed on-the-fly at mount time, and binaries execute from
the 40MB Linux RAM pool.

#### boot (mtd0, 320KB) -- U-Boot Bootloader
The "BIOS" of the camera. Runs first at power-on, initializes DDR, loads the
kernel from flash. Has a serial console (115200 baud) where you can interrupt
boot with Ctrl+C to get a U-Boot shell. From there you can read/write any
flash address with `sf` commands. **Never overwrite this partition** unless
you have a UART connection -- it's the only way to recover from a brick.

#### bootargs (mtd1, 64KB) -- U-Boot Environment
U-Boot settings stored as key=value pairs. Contains the boot command (which
kernel to load), memory allocation (`mem=40m`), and the partition layout
string (`mtdparts=sfc:320K(boot),64K(bootargs),...`). To repartition the
flash, you'd change the `mtdparts` string here.

#### kernel (mtd2, 2MB) -- Linux Kernel
Linux 5.10.221 as a FIT image (kernel + device tree blob). Compiled with
arm-v01c02-linux-musleabi-gcc 10.3.0. This is the OS itself.

#### rootfs (mtd3, 1.25MB) -- Minimal Root Filesystem
Squashfs, decompresses to 2.5MB. Contains just enough to boot Linux and mount
the other partitions:
- BusyBox (721KB) -- 370 Unix commands in one binary
- musl libc (330KB) -- lightweight C library
- libstdc++ (738KB) -- C++ standard library
- udev (415KB) -- device manager
- Init scripts that mount appfs/configfs/resfs
- 387 of 434 files are zero-byte BusyBox symlinks

This is a bootstrap partition -- it boots, mounts appfs, then hands off to
`/home/bashrc.sh` which does the real work.

#### appfs (mtd4, 5MB) -- Application Partition
Squashfs, decompresses to **12.8MB** (2.6:1 compression). The main event:

| Component | Uncompressed | % | What it is |
|-----------|-------------|---|------------|
| `superb` binary | 7.5 MB | 58% | Monolithic camera app (RTSP, ONVIF, DVRIP, cloud, ISP, NPU, audio, recording, detection, OSD -- everything) |
| 49 kernel modules (.ko) | 3.7 MB | 29% | HiSilicon MPP hardware drivers (video pipeline, encoders, ISP, NPU, audio, crypto, watchdog) |
| 13 audio libs (.so) | 1.0 MB | 8% | Voice quality enhancement: echo cancellation, noise reduction, AGC, equalizer, two-way talk |
| btools (diagnostic) | 30 KB | <1% | I2C/SPI/memory read-write tools (9 hardlinked names, 1 binary) |
| Shell scripts | 28 KB | <1% | bashrc.sh (master init), startup.sh, networkcfg.sh, loadhi3516cv610 (module loader) |
| Data (fonts, devInfo) | 265 KB | 2% | Chinese OSD font (256KB), ASCII font (3.5KB), firmware version string |

`superb` dominates. It's a statically-linked 7.5MB ARM ELF containing the
entire camera application including Alibaba IoT, Danale, TUTK P2P, and the
SC635HAI sensor driver.

#### configfs (mtd5, 1MB) -- Writable Config
The **only writable flash partition**. jffs2 filesystem (~808KB free). Contains:
- `SystemCfg.ini` -- all camera settings (978 keys)
- WiFi credentials, cloud credentials, Danale/TUTK/Aliyun configs
- `debug.sh` -- our backdoor (starts tcpsvd on port 9999)
- `hwconfig.cfg` -- hardware configuration overrides

#### resfs (mtd6, 6.375MB) -- Resources
Squashfs, decompresses to 8.9MB (1.4:1 ratio -- much of this is
incompressible binary data):

| Component | Uncompressed | % | What it is |
|-----------|-------------|---|------------|
| Voice prompts (7 languages) | 3.5 MB | 40% | G.711 audio: en, cn, br, ru, vn, ln, rn. Boot tones, alarm sounds, WiFi setup prompts, face enrollment prompts |
| WiFi subsystem | 2.7 MB | 31% | ATBM6062 USB WiFi driver (741KB), wpa_supplicant (722KB), mac80211 (396KB), cfg80211 (256KB), tools |
| AI-ISP models | 1.2 MB | 14% | 5 neural noise reduction models for 2688x1520 and 3200x1800 resolutions |
| IVP detection model | 0.9 MB | 10% | `det_hv_hor.bin` -- person/vehicle detection for NPU (runs at ~17fps) |
| Sensor PQ calibration | 0.6 MB | 6% | 4 ISP tuning profiles: day.bin, night.bin, light.bin, black.bin (color matrices, gamma, AE/AWB tables, noise profiles) |

### Custom Firmware Space Budget

Can we fit a replacement firmware in 16MB? Yes, comfortably.

The usable flash for rootfs + app + resources is **11.625 MB** (everything
except boot, bootargs, kernel, and configfs). Here's the budget:

#### What we'd keep (must have)

| Component | Uncompressed | Notes |
|-----------|-------------|-------|
| Kernel modules (.ko) | 3.7 MB | 49 hardware drivers -- non-negotiable |
| Audio libs (.so) | 1.0 MB | VQE stack for two-way audio |
| WiFi driver + wpa_supplicant | 2.7 MB | Must have WiFi |
| Sensor PQ calibration bins | 0.6 MB | ISP image quality tuning |
| Detection model (det_hv_hor.bin) | 0.9 MB | Person/vehicle detection |
| AI-ISP models (3200x1800 only) | 0.5 MB | Keep 2 of 5 (native resolution only) |
| English voice prompts | 0.7 MB | 1 language instead of 7 |
| rootfs (BusyBox, libc, etc.) | 2.5 MB | Minimal root filesystem |
| Boot scripts (new, minimal) | ~30 KB | Rewritten for our needs |
| **Subtotal** | **~12.6 MB** | |

#### What we'd drop

| Component | Freed | Notes |
|-----------|-------|-------|
| `superb` binary | 7.5 MB | The whole point -- replaced by our code |
| Voice prompts (6 of 7 languages) | 2.8 MB | Keep English only |
| 3 redundant AI-ISP models | 0.8 MB | Only need 3200x1800 variants |
| Chinese OSD font | 0.25 MB | Unless Chinese OSD needed |
| btools, devInfo | 0.03 MB | Diagnostic tools, optional |
| **Subtotal freed** | **~11.4 MB** | |

#### What we'd add

| Component | Estimated | Notes |
|-----------|-----------|-------|
| Our main app (RTSP + ONVIF + web + sensor init) | 1-2 MB | Far simpler than superb. HIVIEW's web server source compiles to ~300-500KB. |
| SC635HAI sensor driver (libsns_sc635hai.so) | ~130 KB | Based on HIVIEW's SC500AI driver size |
| Minimal web UI | 0.2-0.5 MB | Camera controls + live view. HIVIEW's full UI is 4MB but includes jQuery, jsoneditor, chart.js -- overkill. |
| **Subtotal added** | **~1.3-2.6 MB** | |

#### The math

| | Uncompressed | Compressed (~2:1 avg) |
|---|---|---|
| Keep | 12.6 MB | ~6.3 MB |
| Add | 1.3-2.6 MB | ~0.7-1.3 MB |
| **Total** | **~14-15 MB** | **~7-7.6 MB** |
| Available flash | | **11.6 MB** (rootfs + appfs + resfs) |
| **Headroom** | | **~4-4.6 MB free** |

We'd have **4+ MB of flash headroom**, which is plenty. Could even fit a
second language pack, larger web UI, or additional NPU models.

#### HIVIEW reference sizes (for comparison)

| HIVIEW Component | Size | Notes |
|------------------|------|-------|
| Kernel modules (.ko, 45 files) | 3.6 MB | Nearly identical to camera's 3.7MB set |
| Shared libraries (.so, 60 files) | 4.8 MB | Full SDK libs including 6 sensor drivers, audio codecs, ISP |
| Web server source | 744 KB | mongoose.c + camera UI code (compiles to ~300-500KB) |
| Web UI assets (www/) | 4.0 MB | Heavy JS: jsoneditor (904KB), w2ui (412KB), jQuery. Our minimal UI would be much smaller. |
| YOLO NPU models | 12.3 MB | yolov5.om (8.5MB) + yolov8.om (4MB). **Would NOT fit.** Stock det_hv_hor.bin (893KB) is much smaller. |
| Sensor drivers (each) | ~130 KB | SC500AI, SC431HAI, SC450AI all ~130KB |

The YOLO models are the only HIVIEW component that wouldn't fit in 16MB flash.
The stock detection model is a fraction of the size and already works.

#### Reflashing via U-Boot

Read-only squashfs partitions can only be modified by reflashing via U-Boot:

```
# Interrupt U-Boot at power-on (Ctrl+C over UART at 115200 baud)
# Build custom squashfs on PC:
mksquashfs custom_appfs/ custom_appfs.bin -comp xz -b 131072

# In U-Boot console:
loady 0x42000000              # send .bin via ymodem over serial
sf probe 0
sf erase 0x3A0000 0x500000    # erase appfs (5MB)
sf write 0x42000000 0x3A0000 <actual_size>
boot
```

Transfer speed is limited: 115200 baud serial only (no WiFi/USB/TFTP in
U-Boot). 5MB takes ~7-8 min via ymodem. Full 16MB flash takes ~25 min.

Repartitioning is possible by changing the `mtdparts=` string in bootargs
(mtd1), but requires coordinated reflash of all affected partitions.

We have a **complete backup** of all 7 partitions (MD5-verified) in
`firmware/`. The only way to truly brick the device is corrupting U-Boot
(mtd0 at offset 0x000000-0x050000) -- which we'd never touch.

### Boot Chain

```
U-Boot -> kernel (mtd2) -> rootfs (mtd3) -> /etc/init.d/rcS
  -> mounts appfs (mtd4) as squashfs to /tmp/appfs, /progs, /home
  -> /home/bashrc.sh
    -> mounts configfs (mtd5) as jffs2 to /etc/conf.d/
    -> sources /home/variable (SENSOR=sc635hai, chip=hi3516cv610_20s)
    -> sources /etc/conf.d/fixed/hwconfig.cfg
    -> mounts resfs (mtd6) overlays (wifi, ble, ivp, sensor, voice)
    -> loads kernel modules via /home/ipc_drv/loadhi3516cv610
    -> configures WiFi, network
    -> starts mySystem (watchdog)
    -> /progs/startup.sh
      -> if debug.sh exists: runs debug.sh (our backdoor)
      -> else: runs superb directly
```

### Kernel Version

```
Linux 5.10.221 (arm-v01c02-linux-musleabi-gcc (musl-1.2.3 linux-5.10
CS71.2.10.5.B002 2025-03-05) 10.3.0) #1 SMP Mon Jun 9 09:02:27 UTC 2025
```

This confirms the toolchain in `research/toolchain/` is the correct family
(same GCC 10.3.0, same musl 1.2.3, same linux-5.10 headers). The firmware
SDK version is CS71.2.10.5.B002 vs the toolchain's V12CS61.005.010 -- slightly
different SDK releases but same toolchain generation.

---

## Kernel Module Comparison: HIVIEW SDK vs Camera

### Module Match Summary

| Category | HIVIEW SDK | Camera Firmware | Match |
|----------|-----------|-----------------|-------|
| Core (osal, mmz, base, sys, vb, vca) | 6 | 6 | 100% |
| Video pipeline (vi, isp, vpss, vgs, vpp, rgn, chnl) | 7 | 7 | 100% |
| Encoding (venc, rc, h264e, h265e, svac3e, jpege) | 6 | 6 | 100% |
| AI (svp_npu, ive) | 2 | 2 | 100% |
| Audio (aio, ai, ao, aenc, adec, acodec) | 6 | 6 | 100% |
| System (pm, cipher, km, mipi_rx) | 4 | 4 | 100% |
| Other (wdt, devstat, otp, uvc, user, etc.) | 7 | 9 | Partial |
| Extdrv (sensor_i2c, pwm, piris, etc.) | 6 | 7 | Partial |

### Modules on Camera but NOT in HIVIEW SDK

| Module | Purpose | Impact if Missing |
|--------|---------|-------------------|
| `ot_aiisp.ko` | AI-ISP neural noise reduction | Currently disabled (AIISP=0). Low impact. |
| `ot_user_proc.ko` | User-space proc interface | Debug only. No impact. |
| `ot_spi_dma_transfer.ko` | SPI DMA transfers | Not loaded at boot. No impact. |
| `motor_advance.ko` | PTZ stepper motor | No motor hardware on this device. No impact. |
| `hi_gpio.ko` | GPIO control | May affect IR LED / IR-cut control. Medium impact. |

### Modules in HIVIEW SDK but NOT on Camera

| Module | Purpose | Notes |
|--------|---------|-------|
| `ot_es8388.ko` | ES8388 audio codec | Camera uses internal codec. Not needed. |

**Conclusion**: 39 of 39 core modules match exactly. The camera has 5 extra modules,
none of which are critical for basic operation. The HIVIEW SDK modules would boot
the hardware pipeline successfully.

---

## The SC635HAI Sensor Driver Problem

This is the **single biggest blocker** for replacing `superb` or running custom firmware.

### Current State

The SmartSens SC635HAI sensor driver is **statically compiled into the `superb`
binary**. There is no separate `libsns_sc635hai.so` file. Ghidra analysis found
11 SC635HAI-specific functions embedded in superb:

- `sc635hai_get_obj` (0x00432e9c)
- `sc635hai_linear_6m30_10bit_init` (0x00432f80)
- `sc635hai_vc_wdr_2t1_6m30_10bit_init`
- `sc635hai_get_standby_cfg`
- Plus 7 `sc635hai_slave_*` variants

### What a Sensor Driver Does

A HiSilicon sensor driver (typically `libsns_XXX.so`) provides:
1. **Register initialization sequences** -- I2C writes to configure the sensor
   chip for specific modes (resolution, frame rate, HDR, etc.)
2. **AE (Auto Exposure) callbacks** -- Read/write exposure, gain registers
3. **AWB (Auto White Balance) data** -- Sensor-specific color calibration
4. **ISP calibration parameters** -- PQ (Picture Quality) bins for day/night/etc.

The PQ bins ARE available separately at `/home/sensor/sc635hai/pqbin/` (on the
resfs partition), but the register init sequences and AE callbacks are inside
`superb`.

### Possible Paths to Get a Working Driver

1. **Extract from `superb` via Ghidra** -- The I2C register writes during
   `sc635hai_linear_6m30_10bit_init` can be decompiled to reconstruct the
   init sequence. The AE callback functions can be identified. This is the
   most realistic path but requires significant RE effort.

2. **Use a similar sensor driver as a template** -- SC635HAI is in the same
   SmartSens family as SC500AI (both are SmartSens "HAI" series, similar
   register maps). The `libsns_sc500ai.so` from HIVIEW could be adapted.

3. **Find SC635HAI driver source online** -- SmartSens sometimes provides
   reference drivers to camera manufacturers. The SC635HAI is relatively
   new (2024+) so leaked sources are unlikely but worth searching.

4. **Use the camera's own kernel modules + a custom userspace** -- Keep the
   XMeye kernel modules loaded (they're already running) and write a userspace
   application that uses the HiSilicon SDK headers to control the video
   pipeline. The sensor is already initialized by the existing boot scripts.
   **This is the pragmatic approach**: don't replace the kernel, just replace
   the userspace application (`superb`).

### The Pragmatic Path: Run Alongside `superb`

Rather than replacing `superb`, we can run custom programs alongside it. The
kernel modules are already loaded and the sensor is already initialized. A
custom binary using the SDK headers can:

- Open `/dev/svp_npu` and run its own NPU inference stream
- Read `/proc/umap/*` for hardware status
- Use `/dev/venc` or `/dev/vpss` for frame capture
- Control GPIO for LEDs
- Run an HTTP server
- Fire webhooks

The risk is resource contention with `superb` (shared hardware access), but
the NPU supports multiple concurrent streams (3 free streams shown in
`/proc/umap/svp_npu`).

---

## OpenIPC and Alternative Firmware Status

### OpenIPC

**NOT SUPPORTED.** Hi3516CV610 is too new (2024+). The highest supported
Hi3516C variant is CV500. Flashing CV500 firmware on CV610 will brick the
device. OpenIPC would need to add CV610 support from scratch using the kernel
modules and SDK from this research.

### HIVIEW (OpenHisilicon)

**BEST AVAILABLE ALTERNATIVE.** Complete open-source camera platform for
Hi3516CV610. Has everything except the SC635HAI sensor driver. Could serve
as the base for a custom firmware if the sensor driver problem is solved.

### Other Projects

No other alternative firmware projects (Thingino, yi-hack, Dafang, WyzeCam,
OpenWrt) support the Hi3516CV610.

---

## Compatibility Notes

### What Matches Our Camera

- **CPU architecture**: ARM Cortex-A7 -- identical
- **SoC**: Hi3516CV610 -- identical (same kernel modules, same register map)
- **Kernel modules**: 39 of 39 core modules match by name between HIVIEW and camera
- **Kernel version**: Both use Linux 5.10 with the same GCC 10.3.0 / musl 1.2.3 toolchain
- **NPU model format**: `.om` (Offline Model) -- same format as our camera's
  `det_hv_hor.bin` (likely also `.om` with different extension)
- **SDK API**: The `hi_mpi_*` and `ot_mpi_*` APIs match the function names
  found in our Ghidra analysis of `superb`
- **Toolchain**: `arm-v01c02-linux-musleabi-gcc` with musl libc -- confirmed
  by `/proc/version` on the camera showing the same compiler family

### What Doesn't Match

- **Sensor driver**: SC635HAI not available in any open-source project. Statically
  linked into `superb`. Must be reverse-engineered or obtained from SmartSens.
- **Application layer**: HIVIEW uses modular processes; camera uses monolithic `superb`
- **Cloud protocol**: HIVIEW has no Alibaba IoT; camera's cloud is inside `superb`
- **SDK version**: Camera firmware uses CS71.2.10.5.B002, toolchain is V12CS61.005.010,
  HIVIEW SDK is V1.0.2.0 B051. All are for the same chip but different release dates.
  Kernel module ABI may differ slightly between releases.

### Do We Still Need superb.log?

**For now, yes.** Until we build a custom binary that can either:
1. Access the NPU detection results directly (via SVP ACL API using HIVIEW headers)
2. Run its own inference stream alongside `superb`
3. Or replace `superb` entirely with HIVIEW-based firmware

The log-scraping approach (`monitor_alerts.py`) remains the only working method
for local alarm detection. The HIVIEW SDK and toolchain provide the path to
eliminate this dependency, but building and testing the replacement takes time.
