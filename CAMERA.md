# SECUEYE X5 -- Camera & Firmware Reference

Canonical reference for the SECUEYE X5 hardware, stock firmware, network
protocols, cloud architecture, and control surfaces. For the custom sensor
driver see `DRIVER.md`; for kernel decompilation forensics see
`DRIVER_INTERNALS.md`.

> **Sources:** Consolidated from `research/archive/INVESTIGATION.md` (1624
> lines), `research/archive/CAMERA_CONTROLS.md` (928 lines), and
> `research/archive/APK_ANALYSIS.md` (139 lines). Contradictions resolved
> against live camera verification where possible.

---

## Table of Contents

- [Device Overview](#device-overview)
- [Hardware](#hardware)
- [Firmware & Boot Chain](#firmware--boot-chain)
- [Network Services & Protocols](#network-services--protocols)
- [Cloud Architecture](#cloud-architecture)
- [BLE Provisioning Protocol](#ble-provisioning-protocol)
- [Camera Controls](#camera-controls)
- [SystemCfg.ini](#systemcfgini)
- [Security](#security)
- [SD Card Jailbreak & Firmware Update](#sd-card-jailbreak--firmware-update)
- [Known Issues](#known-issues)
- [Appendix: ISP Functions in superb](#appendix-isp-functions-in-superb)
- [Appendix: HI_XUID Internal Commands](#appendix-hi_xuid-internal-commands)
- [Appendix: Secueye APK Directory Map](#appendix-secueye-apk-directory-map)

---

## Device Overview

| Field | Value |
|-------|-------|
| Product | SECUEYE Smart Window Camera X5 |
| Manufacturer | Seculink Technology (HK) Limited |
| Platform | Rebranded Xiongmai (XMeye) |
| Resolution | 3840x2160 (4K) via superb; 3200x1800 via our pipeline |
| Video codec | H.265 (hardware), H.264 also supported |
| Audio codec | G.711 (two-way) |
| WiFi | Dual-band 2.4/5 GHz, WiFi 6 (802.11ax) |
| Power | USB-C (power only, no data) |
| Storage | MicroSD card slot |
| Mount | Magnetic |
| App | "Secueye" (Play Store, developer: haoshiyou) |
| Firmware | MZ0201V160_EN_20251126 |
| ONVIF | V9.0, Profile S + T |
| Price | ~$20 (Amazon) |

---

## Hardware

### SoC

| Field | Value |
|-------|-------|
| Chip | HiSilicon Hi3516CV610-20S |
| CPU | Dual-core ARM Cortex-A7 @ ~950 MHz |
| RAM | 128 MB DDR3 (16-bit, 2133 MHz rated) |
| Split | 40 MB OS (`mem=40m` in cmdline) / 88 MB MMZ (video pipeline) |
| Generation | 2024+ (newest in Hi3516C line) |
| Capabilities | 4K/8MP H.265+H.264 encode, SVP NPU, hardware crypto |

Confirmed via `/proc/cpuinfo`: two processors, CPU part `0xc07` (Cortex-A7).

**Memory split:** The kernel cmdline `mem=40m` tells Linux to use only 40 MB of
the 128 MB physical RAM. The remaining 88 MB is reserved as MMZ (Media Memory
Zone) for the HiSilicon video pipeline -- ISP buffers, VENC, VDA, NPU inference.
MemTotal reports ~36 MB (kernel reserves the rest). Superb consumes ~15 MB at
runtime, leaving ~20 MB free. Our replacement daemon must fit in this budget.

**Full kernel cmdline** (from `/proc/cmdline`):
```
mem=40m earlycon=pl011,0x11040000 console=ttyAMA0,115200 clk_ignore_unused
initcall_debug rw root=/dev/mtdblock3 rootfstype=squashfs
mtdparts=sfc:320K(boot),64K(bootargs),2M(kernel),1280K(rootfs),
5M(appfs),1M(configfs),6528K(resfs)
```

### Flash

| Field | Value |
|-------|-------|
| Chip | GigaDevice GD25Q128XX |
| Type | SPI NOR |
| Size | 16 MB (128 Mbit) |
| Partitions | 7 (see [Flash Layout](#flash-partition-layout)) |

### Image Sensor

| Field | Value |
|-------|-------|
| Sensor | SmartSens SC635HAI |
| Resolution | 6.35 MP native (3200x1800); superb upscales to 3840x2160 |
| Interface | MIPI CSI-2 (`ot_mipi_rx.ko`) |
| Frame rate | 20 fps (VTS=2812); superb encodes at 15 fps |

See `DRIVER.md` for register map, ISP sync path, and AE/AWB calibration.

### WiFi

| Field | Value |
|-------|-------|
| Chip | AltoBeam ATBM6x6x |
| USB ID | `007a:6162` |
| Product string | `AltoBeam_WIFI` |
| Driver | `atbm_wlan` (kernel module in resfs) |
| Standard | WiFi 6 (802.11ax), dual-band 2.4/5 GHz |
| Interface | Internal USB (not user-accessible) |

Confirmed via `/sys/class/net/wlan0/device/uevent` and `lsusb`.

> **Note:** Some earlier docs and README.md (now corrected) incorrectly
> listed "RTL8188FU". The kernel logs, USB product string, and driver all
> confirm AltoBeam.

### Audio

- Built-in microphone (always active) + speaker
- G.711 codec, embedded in both RTSP streams
- Two-way talk via cloud app only (P2P/RTMP path)
- VQE pipeline: AEC (77 KB), AGC (61 KB), ANR (57 KB), HPF (10 KB),
  EQ (46 KB), TalkV2 (301 KB), Record (201 KB)
- Custom voice prompts: `.711` files on SD card in `seculinkVoice/`

### IR / Night Vision Hardware

- Dual-light: IR LEDs + white flood LEDs
- IR-cut filter: mechanical, electrically actuated
- ADC ambient light sensor for auto day/night switching
- PWM brightness control (`/sys/class/pwm/pwmchip3/pwm0/`)

### PTZ Motor

**No motor hardware on this camera.** The PCB has unpopulated pin
connectors for a pan/tilt motor assembly, and the firmware includes
`motor_advance.ko` + `/dev/motor` + SystemCfg.ini PTZ config keys, but
this camera case has no physical motor. The app's PTZ controls do nothing.
Electronic zoom (EZOOM) via the ISP is the only "zoom" available.

### Other

| Component | Detail |
|-----------|--------|
| USB-C | Power only; no USB data enumeration |
| UART | 2-pin JST header, 3.3V, 115200 8N1 (PL011 / ttyAMA0) |
| USB host | xHCI USB 3.0 (WiFi adapter connected internally) |
| Ethernet PHY | Generic PHY on MDIO (not physically exposed) |
| NPU | SVP NPU (`ot_svp_npu.ko`); ~17 inf/s on `det_hv_hor.bin` |
| Crypto | Hardware crypto engine (`ot_cipher.ko`) |
| SD card | MicroSD, auto-records H.265 to `/progs/rec/00/` |
| Reset button | GPIO 13 (input), polled by superb, 1.5s hold = factory reset |
| Status LED | Controllable via `StatusLightSwitch` cloud property |

### Network Identity

| Field | Value |
|-------|-------|
| BLE MAC | `38:77:07:75:97:3A` |
| WiFi MAC | `38:77:07:75:97:39` (BLE minus 1) |
| BLE name | `ipc_xmy-zZV8` |
| IP (DHCP) | `192.168.1.153` |
| OUI | `38:77:07` -- Xiongmai / Hangzhou Xiongmai Technology |
| Factory MAC pool | `A6:88:E0` prefix (Seculink) |

---

## Firmware & Boot Chain

### Key Binaries

| Binary | Size | Function |
|--------|------|----------|
| `superb` | 7.8 MB | Monolithic camera daemon (replaces Xiongmai "Sofia"). Handles RTSP, ONVIF, DVRIP, cloud MQTT, recording, ISP, AI, P2P. Statically linked. |
| `mySystem` | 9.7 KB | Process supervisor + watchdog. UDP 8899 heartbeat (localhost). Restarts superb on failure. |
| `upgrade` | 42.9 KB | Firmware update handler. Monitors for `ota_upgrade.bin`. |

### Boot Sequence

```
Stage 1: U-Boot
  U-Boot 2022.07 loads FIT image from SPI NOR flash offset 0x60000
  Hardcoded: sf probe 0; sf read ...; bootm
  No SD card boot fallback.

Stage 2: Kernel
  Linux 5.10.221 boots
  Mounts squashfs rootfs from /dev/mtdblock3 (read-only)
  /etc/fstab auto-mounts: proc, sysfs, tmpfs on /dev, /tmp, /var

Stage 3: BusyBox init reads /etc/inittab
  Action: ::sysinit:/etc/init.d/rcS  (runs to completion first)
  Action: ttyS000::respawn:/bin/login (UART console)
  Action: ::restart, ::ctrlaltdel, ::shutdown handlers

Stage 4: /etc/init.d/rcS
  /bin/mount -a
  Glob loop: /etc/init.d/S[0-9][0-9]* (alphabetical order)
    S00devs     : mknod console, ttyAMA0, ttyAMA1, ttyS000, null
    S01udev     : mkdir /dev/pts, mount devpts (ptmxmode=000!)
    S80network  : configure eth0 from kernel cmdline ip= param
    S90hibernate: check for hibernate/snap mode in cmdline
  Mount appfs (squashfs): find "appfs" MTD, mount to /tmp/appfs
  Bind-mount /tmp/appfs/progs -> /progs, /tmp/appfs/home -> /home
  Execute /home/bashrc.sh

Stage 5: /home/bashrc.sh (appfs, 293 lines -- the big one)
  mount_as_tmpfs /etc/          (tmpfs overlay, makes /etc writable in RAM)
  mount_as_tmpfs /progs/rec     (tmpfs overlay for recording dir)
  Set LD_LIBRARY_PATH, ulimit -s unlimited, ulimit -c unlimited
  Mount configfs (jffs2) -> /etc/conf.d/  (THE writable persistent partition)
  source /home/variable          (chip, sensor, wifi, partition layout vars)
  source /etc/conf.d/fixed/hwconfig.cfg
  Mount resfs (squashfs) -> /tmp/resfs, bind-mount wifi/ble/ivp/sensor/voice
  insmod motor_advance.ko (if present)
  /progs/updateID.sh             (SD card jailbreak point! sources recycle_ali.sh)
  loadhi3516cv610 -i -sensor0 sc635hai ...  (HiSilicon kernel modules)
  insmod ot_adc.ko
  Load WiFi kernel modules (atbm6x6x)
  /progs/networkcfg.sh           (eth0 up, DHCP or static, wpa_supplicant)
  Launch mySystem &
  Launch startup.sh &

Stage 6: /progs/startup.sh (runs in background)
  ulimit -s unlimited
  IF /etc/conf.d/debug.sh exists -> execute it (our hook)
  ELSE -> launch superb &, sleep 15, PQTools.sh

Stage 7: superb (once running)
  Creates /progs/rec/00/ on the tmpfs
  Mounts SD card: mount -t vfat /dev/mmcblk0p1 /progs/rec/00
  Starts all services (RTSP, ONVIF, DVRIP, cloud MQTT, recording, AI)
```

**Key boot facts:**
- `/etc/init.d/` is on read-only squashfs; cannot add scripts without reflashing.
- `/etc` becomes writable (tmpfs) after bashrc.sh step, but contents lost on reboot.
- `/root` is on read-only squashfs; needs tmpfs overlay for writable home dir.
- `S01udev` mounts devpts with `ptmxmode=000` -- blocks PTY allocation. Must remount
  with `ptmxmode=666` for SSH to work.
- No `/etc/shells` on stock rootfs -- dropbear needs it created.
- **SD card is mounted by superb**, not by init. The `/progs/rec/00/` directory
  doesn't exist until superb creates it. Any boot script that needs SD card access
  must mount it explicitly (and `mkdir -p` the mount point first).
- `debug.sh` is the primary hook for custom boot logic. It runs instead of the
  normal superb launch path, so it must launch superb itself.

### Flash Partition Layout

| # | Name | Offset | Size | FS | Contents |
|---|------|--------|------|----|----------|
| 0 | boot | 0x000000 | 320 KB | raw | U-Boot |
| 1 | bootargs | 0x050000 | 64 KB | raw | U-Boot env |
| 2 | kernel | 0x060000 | 2 MB | FIT | Linux kernel + DTB |
| 3 | rootfs | 0x260000 | 1.25 MB | squashfs | BusyBox (~370 applets), /etc |
| 4 | appfs | 0x3A0000 | 5 MB | squashfs | superb, drivers, libs |
| 5 | configfs | 0x8A0000 | 1 MB | jffs2 | **Writable** config |
| 6 | resfs | 0x9A0000 | 6.375 MB | squashfs | WiFi/BLE/sensor/voice/IVP resources |

All partitions dumped and verified: `firmware/mtd[0-6]_*.bin` + `full_flash.bin`.

### Kernel

| Field | Value |
|-------|-------|
| Version | 5.10.221 (Jun 9 2025) |
| Builder | `xiely@ubuntu24` |
| Toolchain | arm-v01c02-linux-musleabi-gcc 10.3.0 (musl-1.2.3) |
| C library | musl libc (not glibc) |
| Root FS | squashfs on /dev/mtdblock3 (read-only) |

### Kernel Modules (HiSilicon "OpenT")

| Module | Purpose |
|--------|---------|
| `ot_sys`, `ot_base`, `ot_osal` | System infrastructure |
| `ot_vi` | Video input (sensor interface) |
| `ot_isp` | Image signal processor |
| `ot_vpss` | Video processing subsystem |
| `ot_venc`, `ot_h264e`, `ot_h265e` | Video encoder + codecs |
| `ot_jpege` | JPEG encoder |
| `ot_rgn` | Region/OSD overlay (zero CPU) |
| `ot_ai`, `ot_ao`, `ot_aenc`, `ot_adec` | Audio pipeline |
| `ot_mipi_rx` | MIPI CSI-2 receiver |
| `ot_ive` | Intelligent video engine |
| `ot_svp_npu` | Neural processing unit |
| `ot_vca` | Video content analysis |
| `ot_mmz` | Media memory zone |
| `ot_cipher` | Hardware crypto |

### Process Tree (Normal Boot)

```
PID 1  init (BusyBox)
  |- mySystem        (watchdog, UDP 8899 localhost)
  |- dropbear :22    (SSH daemon, key + password auth)
  |- tcpsvd 9999     (our backdoor, legacy fallback)
  |- recv :8888      (fast file transfer daemon)
  |- superb          (TCP 80/554/34567, UDP 3702/30012/30014/34569)
  |   |- 7x /dev/isp_dev FDs
  |   `- /dev/motor (PTZ)
  |- wpa_supplicant  (WiFi)
  `- /bin/login      (UART console)
```

### BusyBox

**Available:** vi, sed, grep, find, awk, dd, base64, xxd, md5sum, sha256sum,
ip, ifconfig, route, mount, insmod, tcpsvd, udpsvd, inetd, passwd, crontab,
udhcpc, udhcpd, and ~350 more.

**Stripped:** telnet, telnetd, nc/netcat, tftp, ftpd, httpd, wget, curl, scp.
All remote access and file transfer tools intentionally removed. We added
dropbear (SSH) and recv (fast file transfer) via SD card; see
[debug.sh Boot Hook](#debugsh-boot-hook).

### Shared Libraries (appfs)

| Library | Purpose |
|---------|---------|
| `libsecurec.so` | HiSilicon bounds-checked string ops |
| `libiw.so.29` | Wireless tools |
| `libupvqe.so` | Uplink voice quality enhancement |
| `libvoice_engine.so` | Voice engine (two-way audio) |
| `libvqe_*.so` | VQE modules (AEC, AGC, ANR, HPF, EQ, TalkV2, Record, Resampler) |

### PQ Calibration

superb loads 4 PQ bin files at startup (141 KB each):

- `/tmp/resfs/sensor/sc635hai/pqbin/day.bin`
- `/tmp/resfs/sensor/sc635hai/pqbin/night.bin`
- `/tmp/resfs/sensor/sc635hai/pqbin/light.bin`
- `/tmp/resfs/sensor/sc635hai/pqbin/black.bin`

---

## Network Services & Protocols

### Open Ports

| Port | Protocol | Service | Auth |
|------|----------|---------|------|
| 80 | TCP | ONVIF SOAP (superb) | None |
| 554 | TCP | RTSP (superb) | None |
| 9999 | TCP | Root shell (tcpsvd, our backdoor) | None |
| 34567 | TCP | DVRIP binary dialect (superb) | None (all logins accepted) |
| 3702 | UDP | WS-Discovery | None |
| 30012 | UDP | Internal (superb) | Unknown |
| 30014 | UDP | Internal (superb) | Unknown |
| 8899 | UDP | mySystem heartbeat | Localhost only |

Notable closed ports: 23 (telnet), 9527 (XMeye debug), 9530 (macGuarder).

### RTSP

| Stream | URL | Resolution | Codec | FPS | Bitrate |
|--------|-----|-----------|-------|-----|---------|
| Main | `rtsp://<ip>/live1` | 3840x2160 | H.265 | 15 | 4096 kbps |
| Sub | `rtsp://<ip>/live2` | 720x576 | H.265 | 15 | 1024 kbps |

No authentication. G.711 audio embedded in both streams. Works with VLC,
ffplay, any RTSP client. Alternative URL formats: `/user=admin_password=_channel=1_stream=0.sdp`, `/1`.

> **Note:** Earlier INVESTIGATION.md incorrectly said H.264. The encoder
> config has `vencType=1` (H.265) and ConfigExport returns "H.265 IPC".

### HTTP Snapshots

| URL | Result |
|-----|--------|
| `http://<ip>:80/snapshot/MainStream` | ~34 KB JPEG |
| `http://<ip>:80/snapshot/SubStream` | ~34 KB JPEG |

No auth. Both return similar low-resolution images despite the "MainStream"
name -- not full 4K.

### ONVIF

Port 80, SOAP XML. Manufacturer=SECULINK, Model=SECUEYE Camera.

**Services:** Device, Media, Events, Imaging, PTZ, DeviceIO.

**What works:** Discovery, GetDeviceInformation, GetStreamUri, GetSnapshotUri,
GetVideoEncoderConfigurations, GetVideoSources. Profile tokens: `MainStream`,
`SubStream`.

**What does NOT work:** All settings writes (SetSystemDateAndTime,
SetImagingSettings for brightness/contrast/IrCutFilter/EFlip) are accepted
and echoed back but **never applied** to the actual ISP. ONVIF PullPoint
events are advertised (MotionAlarm, ImageTooBlurry, etc.) but **never fire**.
Media2 (Profile T) faults despite being advertised. GetServices faults.

**Auth is inverted:** Adding any WS-Security header causes faults; no auth
works fine.

**Verdict:** Shallow shim useful for discovery only. Not a control path.

### DVRIP (Port 34567)

Xiongmai's proprietary protocol. 20-byte binary header (0xFF, version 0x01,
session, sequence, msgID, length).

**This camera returns binary-only responses** instead of the standard JSON.
Breaks all existing clients (General VMS, python-dvr, etc.). Login always
succeeds regardless of credentials.

**Commands that return data:**

| MsgID | Command | Response |
|-------|---------|----------|
| 1452 | OPMachine | 48 bytes: time fields as uint32 LE |
| 1048 | ConfigExport | 2064 bytes: "H.265 IPC" model string |
| 1440 | PTZ query | 11796 bytes: preset table (all zeroed) |

Only 3 commands implemented: Record Start/Stop (msg 1100), getSystemInfo.
Everything else returns a generic 16-byte ack (`0x64` = "not implemented").

**Verdict:** Non-functional for practical use. Cannot configure camera.

---

## Cloud Architecture

### Overview

The camera is architecturally cloud-dependent. **Real control flows
exclusively through Alibaba Cloud IoT MQTT.** ONVIF and DVRIP are facades.

```
Secueye App (or any Alibaba IoT client)
    | HTTPS REST + MQTT-over-TLS
    v
Alibaba Cloud IoT Platform (Aliyun Link Vision)
    | MQTT
    v
superb (cloud agent module)
    | Internal calls: hi_mpi_isp_set_*(), motor, venc, etc.
    v
ISP / recording / detection / PTZ subsystems
```

### Alibaba IoT Credentials

| Field | Value |
|-------|-------|
| Product Key | `a1y8M6TXvzw` |
| Device Name | `zZV8td5Gt8IQzlUVdMXE` |
| Device Secret | `3ce9ed72245abc91b74b96e651d8ad3c` |
| MQTT broker | `public.iot-as-mqtt.cn-shanghai.aliyuncs.com` |
| Port | 443 (TLS via mbedTLS) |
| SDK | `sdk-c-2.3.0_FY_1.6.6-6` (ali-smartliving-device-sdk-c) |
| Auth | TLS + custom-ilop, hmacsha1 token |

Credentials stored in plaintext: `lic.bin` (device identity), `aiot_kv.bin`
(MQTT URL + ~70 seed_key entries). UART boot log leaks full ClientID with
auth token.

Credentials are per-device (factory-provisioned via SD card). Protocol and
property names are universal across this firmware version. BLE command
`PK&DN?` returns product key and device name without UART.

### MQTT Topics

```
Subscribe: /sys/{PK}/{DN}/thing/service/property/set
Publish:   /sys/{PK}/{DN}/thing/event/property/post
Config:    /sys/{PK}/{DN}/thing/config/get
Reset:     /sys/{PK}/{DN}/thing/reset
```

### Thing Model Properties

**Image:**
`NightVisionMode`, `IRLightBrightness`, `WhiteLightBrightness`,
`ImageFlipState`, `StreamVideoQuality`, `SubStreamVideoQuality`

**Detection & alarms:**
`MotionDetectSensitivity`, `AlarmSwitch`, `AlarmFrequencyLevel`,
`AlarmNotifyPlan`, `FaceDetectSensitivity`, `CrossLineDetect`,
`RegionDetect`, `IvpAbility`, `AlarmLightSwitch`, `StrongReminderSwitch`

**Recording:**
`StorageRecordMode`, `StorageRecordQuality`, `StorageStatus`,
`TimeRecordEnable`, `TimeRecordPlan`

**Lights:**
`FloodlightSwitch`, `FloodlightSchedule`, `FloodlightScheduleEnable`,
`StatusLightSwitch`, `DoubleLight`, `LaserLight`

**PTZ:**
`IntelligentTracking`, `PreviewSwitch`

**System:**
`RebootSchedule`, `VoicePrompt`, `CustomCmd`

50+ additional property names in `linkkit_set_property_handler` (see
`tools/ghidra/output/resolved_strings.txt`).

### Thing Model Commands

`StartPTZAction`, `StopPTZAction`, `ZoomActionControl`,
`PresetLocateControl`, `PresetAddControl`, `PresetDeleteControl`,
`QueryPresetMap`, `Restart`, `DeviceDefault`, `FormatStorageMedium`,
`QueryTFCard`, `SetWifi`, `QueryAPList`, `QueryRecordDateList`,
`QueryRecordTimeList`, `QueryFileList`, `DeleteFile`, `StartVod`,
`StopVod`, `StartVodByTime`

### Secueye App Architecture

APK: `com.seculink.app_2.3.7.apk`, decompiled with jadx v1.5.5.
30,383 classes, 188,168 methods.

**The app contains zero DVRIP code.** Never connects to port 34567. All
communication via Alibaba Cloud IoT. Video uses native `liblinkvision.so`
via P2P (STUN/TURN), RTMP (cloud relay), or HLS (cloud recordings) -- not
RTSP.

Key classes:
- `sdk/IPCDevice.java` -- wraps Aliyun PanelDevice, `CLOUD_CHANNEL_ONLY`
  (no local LAN control from app)
- `sdk/SDKInitHelper.java` -- App Key: `26001873`, Secret:
  `f4a90ebee699166af95b092dffadcfc3`
- `sdk/LinkVisionAPI.java` -- REST calls to `/vision/customer/*`
- `config/Constants.java` -- Thing Model property names
- `config/TMPConstants.java` -- cloud command names

Cloud API endpoints: `/awss/time/window/user/bind`, `/thing/info/get`,
`/vision/customer/storage/picture/capture`,
`/vision/customer/storage/device/record/query`,
`/vision/customer/eventrecord/plan/*`,
`https://traffic.secueye.app/api/app/*`

### Local MQTT Broker (Potential, Blocked)

Protocol is fully mapped. A local mosquitto broker impersonating
`public.iot-as-mqtt.cn-shanghai.aliyuncs.com` could send
`thing.service.property.set` JSON commands.

**Blocker:** TLS certificate verification. superb uses mbedTLS with server
cert validation. The Alibaba IoT CA cert is compiled into the binary. A
self-signed broker fails the TLS handshake. Possible bypasses:
- Patch superb for `securemode=3` (plaintext TCP port 1883)
- Replace embedded CA cert in binary
- Check if `HAL_SSL_Establish` loads CAs from a configfs file path

---

## BLE Provisioning Protocol

Fully reverse-engineered from APK decompilation.

### Service Map

| UUID | Type | Purpose |
|------|------|---------|
| `0x181C` (User Data) | Service | Provisioning service |
| `0x2A8A` | Characteristic (read/write) | Command input |
| `0x2A90` | Characteristic (notify) | Status responses |
| `0x180A` (Device Info) | Service | Device identification |

Device Info: Manufacturer=`nimble4.2`, Firmware=`cronus_1.0`, SW=`V1.0.0`.
BLE name prefix `ipc_xmy` = "IPC XMeye".

### WiFi Provisioning Sequence

```
1. Scan for BLE devices with name containing "ipc_xmy"
2. Connect, subscribe to 0x2A90 notifications, wait ~1.5s
3. Write 0x2A8A: STATUS?       -> reply: STATUS=wifi_wait
4. Write 0x2A8A: SSID:<name>
5. Wait ~500ms
6. Write 0x2A8A: PWD:<password>
7. Poll with STATUS?            -> wifi_find -> wifi_connecting ->
                                   wifi_success (done!) or wifi_failed
```

All commands are plain UTF-8 text on characteristic `0x2A8A`. No binary
framing, no encryption, no JSON. Colon is the delimiter.

### Other BLE Commands

| Command | Response | Purpose |
|---------|----------|---------|
| `PK&DN?` | `pk=<key>&dn=<name>` | Get Alibaba IoT identity |
| `UNBIND?` | (acknowledged) | Unbind from cloud account |

---

## Camera Controls

### Control Architecture

All camera functions are handled by `superb`. The ISP is controlled via
HiSilicon MPP API calls (`hi_mpi_isp_set_*`) through ioctls to
`/dev/isp_dev`. Only superb makes these calls, triggered by cloud MQTT
`thing.service.property.set` commands.

**Only confirmed working local control method for core settings:** Alibaba
IoT cloud MQTT. ONVIF and DVRIP are facades. See [SystemCfg.ini](#systemcfgini)
for the limited set of config-file-controllable settings.

### Streaming

Covered in [RTSP](#rtsp) and [HTTP Snapshots](#http-snapshots) above.

Encoding config in SystemCfg.ini (untested -- may work since it configures
the encoder, not the ISP):

```ini
# CH1 main: 3840x2160, 15fps, 4096kbps, H.265 (vencType=1), GOP=80
# CH2 sub:  720x576, 15fps, 1024kbps, H.265
```

23 supported resolutions from 3840x2160 down to 352x288.

### Image Quality

ISP auto-tunes at startup using PQ bin files + these modules:
- AE (auto-exposure) with gain/shutter
- AWB (auto white balance)
- WDR strength=256, DRC strength=256, Dehaze strength=32
- 3DNR, Sharpening (per-ISO curves)

superb ISP control functions call `hi_mpi_isp_set_csc_attr` (brightness,
contrast, saturation 0-100, hue), `hi_mpi_isp_set_sharpen_attr`, etc.
These are triggered only by cloud commands.

Writing ISP CSC registers via `bspmm` from the shell has no visible effect
-- superb continuously overwrites them (holds 7 FDs to `/dev/isp_dev`).

### Night Vision

| Config Key | Default | Range / Notes |
|------------|---------|---------------|
| `daynightMode` | 0 | 0=auto |
| `nightVisionMode` | 0 | 0=auto, 1=color, 2=B&W (cloud-only) |
| `IRLBrightness` | 5 | 0-10, IR LED brightness |
| `WLBrightness` | 5 | 0-10, white LED brightness |
| `cut_rate` | 30 | Light threshold for IR-cut switch |
| `cutDelay` | 2 | Seconds delay before switch |
| `dayStart`/`dayEnd` | 06:00/18:00 | Day mode window |

Internal functions: `secu_sensor_set_nightmode`, `secu_sensor_set_ircut`,
`secu_sensor_Adc_CheckNight_v2`, `secu_sensor_light`.

PWM direct control: `echo <duty> > /sys/class/pwm/pwmchip3/pwm0/duty_cycle`

### Motion Detection

Disabled by default (`bMDEnable=0`). Config: sensitivity=30720, delay=10s,
grid=32 zones (-1=all), push enabled, record on detect enabled.

### AI Human/Vehicle Detection (IVP)

Enabled by default (`IVPEnable=1`, sensitivity=3 range 0-5).

Model: `/tmp/resfs/ivp/det_hv_hor.bin` (872 KB). Input: 1x3x384x640.
Output: 3x(Nx6) = 5040 candidates (x, y, w, h, conf, class). Runs at
~17 fps on SVP NPU. Results flow via `/dev/svp_npu` ioctl to superb only
-- not accessible from the shell. Status: `/proc/umap/svp_npu`.

### Tripwire & Region Intrusion

Both disabled by default. Config keys: `CrossLineEnable`, `CrossLineDirection`
(0=both, 1=L-R, 2=R-L), `RegionDetectEnable` with corner coordinates.
Controllable only via cloud.

### Local Alarm Detection

ONVIF events are **non-functional** -- subscriptions succeed but events
never fire (tested 2026-05-05).

**superb.log monitoring works** (requires `debug.sh` redirecting stdout):

| Log Pattern | Event | Latency |
|-------------|-------|---------|
| `start maudio_speaker` | Voice alarm fired | ~0 ms |
| `Create snap` | Alarm snapshot captured | ~0 ms |
| `goto preset NNN` | PTZ preset (100=alarm, 103=tracking) | ~0 ms |
| `mivp_set_param` burst | IVP reconfiguration | ~30s post-alarm |

Recording: M-prefix files (`M{HHMMSS}.H265`) created instantly at alarm
time. N-prefix = normal. Path: `/progs/rec/00/YYYYMMDD/`.

**Limitation:** No `wget`/`curl`/`nc` on camera. A cross-compiled static
ARM HTTP binary is needed for on-camera webhooks.

### Recording

Active with SD card inserted. Files: raw H.265 streams at
`/progs/rec/00/YYYYMMDD/{N|M}{HHMMSS}.H265`. Config: CH2 (sub) recording
enabled (`bRecEnable=1`), CH1 (main) disabled. File length: 600s (10 min).
Both channels have alarm recording enabled.

ONVIF Recording/Replay/Search: all return "Action Not Implemented".
Playback only via root shell, cloud app, or physical SD card removal.

### PTZ

**No motor hardware** -- see [Hardware > PTZ Motor](#ptz-motor). ONVIF
PTZ service exists at `/onvif/ptz_service` and cloud commands
(`StartPTZAction`, etc.) are accepted, but all are no-ops on this
camera. Digital zoom (EZOOM) is the only functional zoom path.

### Image Orientation

- **Flip/mirror:** Cloud property `ImageFlipState` (binary toggle). Chain:
  `sp_image_set_flip` -> `msensor_setparam` -> `secu_sensor_mirror_flip_set`
  -> SC635HAI register write. Not controllable locally without cloud.
- **Rotation (90-degree):** Empty stubs (`secu_sensor_rotate_set` returns 0).
  Not functional on this firmware.

### OSD

| Key | Default | Works via config? |
|-----|---------|:-:|
| `bShowOSD` | 1 | YES |
| `hours_fmt` | 1 (12h) | YES (requires full reboot) |
| `channelName` | H.265 IPC | NO (cloud-only) |
| `osdSize` | 32 | Untested |
| `osdText0`-`osdText7` | empty | Untested |

Cloud properties: `CustomIPCOSDName`, `DateFormat`, `HoursFormat`.

### Audio Control

- `bAudioEn=1` in both channels (audio in RTSP streams)
- `chatMode=2` (two-way talk mode)
- `doublelight_bOutVoice` = voice alarm on/off (works via config)

---

## SystemCfg.ini

Master config file at `/etc/conf.d/syscfg/SystemCfg.ini` (~12 KB).
Tested 2026-05-04 via `tools/test_settings.py`.

### Settings That WORK (edit + full reboot)

| Key | Description |
|-----|-------------|
| `bShowOSD` | OSD visibility (1=show, 0=hide) |
| `doublelight_bOutVoice` | Voice alarm (1=on, 0=off) |
| `timezone` / `posixTZ` / `regionTZ` | Timezone (OSD uses posixTZ) |
| `ntpServer` | NTP server address |
| `hours_fmt` | 12/24h format (requires full reboot, not just superb restart) |

### Settings That DO NOT WORK

| Key | Description | Result |
|-----|-------------|--------|
| `channelName` | OSD text | Persists in file, OSD unchanged |
| `nightVisionMode` | Night mode | No effect (also broken in app) |
| `IVPEnable` | AI detection | No effect (green box still appears) |
| `RegionDetectEnable` | Region intrusion | No effect |
| `brightness`/`contrast`/`saturation`/`sharpness` | ISP quality | No effect |

**Pattern:** Simple output toggle flags work. Core pipeline settings (ISP,
AI, OSD content, night vision) are cloud-only and ignore config file values.

### Other Config Files on configfs

| File | Contents |
|------|----------|
| `syscfg/account.dat` | XOR-obfuscated account data |
| `syscfg/network/wpa_supplicant.conf` | WiFi creds (plaintext) |
| `syscfg/network/wifipasswd.cfg` | WiFi password (plaintext) |
| `syscfg/sensor.sh` | Sensor model (SC635HAI) |
| `fixed/hwconfig.cfg` | Hardware config (PTZ, lights, motor) |
| `fixed/base.cfg` | OTA URLs (`updatewt.afdvr.com`) |
| `devInfo` | `version=MZ0201V160_CN_20251126` |
| `lic.bin` | Alibaba IoT device identity |
| `aiot_kv.bin` | MQTT URL + seed keys |

### System Settings

| Key | Default | Notes |
|-----|---------|-------|
| `powerFreq` | 0 | 0=50Hz, 1=60Hz anti-flicker |
| `enableStreamWatchDog` | 1 | Restarts stream on failure |
| `rebootSwitch` | 0 | Scheduled reboot off |
| `sntpInterval` | 24 | NTP sync hours |
| `nLanguage` | 1 | 1=English |

### Email Config (Factory Defaults)

Chinese SMTP: `smtp.163.com`, user `ipcmail`, password `ipcam71a`
(plaintext). No recipients configured. Non-functional out of box.

---

## Security

### Authentication: None

| Service | Auth Status |
|---------|-------------|
| SSH (22) | **Ed25519 key + password** (dropbear, our addition) |
| RTSP (554) | None |
| ONVIF (80) | None (adding WS-Security breaks it) |
| DVRIP (34567) | None (all credentials accepted) |
| HTTP snapshots | None |
| BLE provisioning | None (no pairing/bonding) |
| Root shell (9999) | None (our backdoor, legacy fallback) |

**Anyone on the same LAN can view, snapshot, and send commands.** SSH is the
only authenticated service. All stock services remain unauthenticated.

### Credentials

| Source | Username | Password | Notes |
|--------|----------|----------|-------|
| rootfs `/etc/shadow` | root | *(empty)* | SHA-512 hash; verified with `cryptpw` on camera |
| appfs `/home/passwd` | root | (uncracked) | DES salt=`GI`, exhausted 1-6 chars |
| ONVIF | admin | admin | Level 0 (admin) |
| ONVIF | user | 123456 | Level 2 (viewer) |
| Danale | admin | ZKAcmKhE | Secondary cloud platform |

WiFi PSK stored plaintext in `wpa_supplicant.conf` and `wifipasswd.cfg`.
Cloud credentials in plaintext across `lic.bin` and `aiot_kv.bin`. UART
boot log leaks full MQTT ClientID with auth token.

### Cloud Connectivity (Default)

Camera phones home via MQTT-TLS + P2P STUN/TURN. Creates outbound tunnel
allowing remote access without port forwarding. Camera initiates all
connections outward.

### Mitigation

One firewall rule blocks all cloud access:
```
iptables -I FORWARD -s 192.168.1.153 -o $(nvram get wan_iface) -j DROP
```
Also disable UPnP on router. Assign static DHCP lease for
`38:77:07:75:97:39`.

### Xiongmai Backdoor Ports

All disabled in this firmware: 9527 (debug), 9530 (macGuarder), 23
(telnet). Known passwords (`I0TO5Wv9`, `xmhdipc`, etc.) irrelevant since
DVRIP accepts everything anyway.

---

## SD Card Jailbreak & Firmware Update

### SD Card Jailbreak (recycle_ali.sh)

`/progs/updateID.sh` runs every boot as root (before superb starts) and
**sources** an arbitrary script from SD card:

```bash
if [ -f /var/udisk/seculinkIdRecycle/recycle_ali.sh ]; then
    chmod 777 /var/udisk/seculinkIdRecycle/recycle_ali.sh
    source /var/udisk/seculinkIdRecycle/recycle_ali.sh
fi
```

**To jailbreak without UART:** Format SD as FAT32, create
`seculinkIdRecycle/recycle_ali.sh` with desired commands, insert and power
cycle. Script executes as root.

### SD Card Provisioning Paths

| SD Card Path | Purpose |
|--------------|---------|
| `seculinkIdRecycle/recycle_ali.sh` | **Arbitrary code execution** |
| `seculinkAliyunUid/aliyunUid/*.conf` | Alibaba IoT credentials |
| `seculinkDanaleUid/danaleUid/*.conf` | Danale cloud credentials |
| `seculinkMac/seculinkMAC.txt` | MAC address override |
| `seculinkHardware/hwconfig.cfg` | Hardware config override |
| `seculinkVoice/language/*.711` | Voice prompt files |
| `wifi/wpa_supplicant.conf` | WiFi credentials override |
| `UserMallCfg.txt` | App store URL override |
| `*.json` (root) | Hardware info override |

### Factory Reset

**Trigger:** GPIO 13 (the only input GPIO), polled by superb via
`/sys/class/gpio/gpio%u/value`. Counter `reset_detect_times` increments
while held; fires at ~1.5 seconds. Voice prompt `reset.711` plays.

**Three entry points in superb:**
- `SYSFuncs_factory_default` -- button-triggered, with reboot
- `SYSFuncs_factory_default_without_reboot` -- programmatic
- `SYSFuncs_factory_default_by_remote` -- cloud/DVRIP `DeviceDefault`

**What gets wiped** (shell commands embedded in superb binary):

```
rm -rf /etc/conf.d/syscfg/*
rm -rf /etc/conf.d/syscfg/network/*
rm -rf /etc/conf.d/syscfg/face/*
rm -rf /etc/conf.d/fixed/custom_voice/*
rm /etc/conf.d/aliyun.conf
rm -f /etc/conf.d/seted_id
rm -f /etc/conf.d/hwinfo.json
rm /etc/conf.d/UserMallCfg.json
rm /etc/conf.d/syscfg/app_reboot.cfg
rm /etc/conf.d/syscfg/dooralarm.dat
rm /etc/conf.d/syscfg/patrol.dat
rm /etc/conf.d/syscfg/startTime.dat
rm /etc/conf.d/syscfg/restart.cfg
rm -rf /etc/conf.d/syscfg/watchpos.cfg
```

Then restores default network:
`cp /etc/conf.d/syscfg/default/interface.cfg /etc/conf.d/syscfg/network/`

Also calls `awss_report_reset(0)` to unbind from Alibaba cloud, and
`mdoubleLight_del_all_custom_voice()`.

**What survives factory reset:**

| Path | Contents | Survives? |
|------|----------|:-:|
| `/etc/conf.d/debug.sh` | Our backdoor | YES |
| `/etc/conf.d/fixed/hwconfig.cfg` | Hardware config | YES |
| `/etc/conf.d/fixed/base.cfg` | OTA URLs | YES |
| `/etc/conf.d/lic.bin` | Device identity | YES (but cloud unbind sent) |
| `/etc/conf.d/aiot_kv.bin` | MQTT keys | YES |
| SD card (`/progs/rec/00/`) | Recordings, our binaries | YES |
| `/progs/` (appfs) | superb, libs | YES (read-only squashfs) |

**SD card note:** Factory reset does NOT wipe the SD card. However,
killing `mySystem` outright (instead of SIGSTOP) causes superb to die
mid-write to the journalless FAT32 filesystem. On next boot, the
unclean VFAT state can cause file truncation or trigger a reformat
(superb checks for "mount abnormal" and has `mkdosfs`/`mkfs.ext4`
commands). Always use `kill -STOP` on mySystem, never `kill -9`.

### debug.sh Boot Hook

`/etc/conf.d/debug.sh` is our primary boot hook. When it exists, `startup.sh`
runs it instead of launching superb directly. Our debug.sh handles:

1. Mount SD card (`mkdir -p` + `mount -t vfat`, since superb normally does this)
2. Set root password (copy stored hash into tmpfs `/etc/shadow`)
3. Fix devpts (`ptmxmode=666` for SSH PTY allocation)
4. Create `/etc/shells` (stock rootfs has none)
5. Mount tmpfs on `/root`, install SSH authorized_keys from configfs
6. Copy dropbear host keys from configfs to `/etc/dropbear/`
7. Start dropbear SSH daemon (port 22)
8. Start tcpsvd backdoor (port 9999, legacy fallback)
9. Start recv file transfer daemon (port 8888)
10. Start superb with logging to `/tmp/superb.log`
11. PQTools calibration after 15s delay
12. Background log sync to SD card every 30s

### Configfs Layout (`/etc/conf.d/`, jffs2, 1 MB, ~800 KB free)

| Path | Purpose | Ours? |
|------|---------|:-----:|
| `debug.sh` | Boot hook script | YES |
| `shadow_root` | Persistent root password hash | YES |
| `dropbear/dropbear_*_host_key` | SSH host keys (RSA, ECDSA, Ed25519) | YES |
| `dropbear/authorized_keys` | SSH public keys for key-based auth | YES |
| `aiot_kv.bin` | Alibaba IoT MQTT keys | no |
| `customer.json` | Device identity | no |
| `devInfo` | Device info | no |
| `lic.bin` | License/identity | no |
| `fixed/hwconfig.cfg` | Hardware config | no |
| `syscfg/` | App settings (wiped on factory reset) | no |

**For our daemon (Phase 3):** We define our own reset behavior. Read
GPIO 13, decide what to wipe. Recommended: user config in a wipeable
path, daemon binary + device identity in a non-wipeable path. Skip
the cloud unbind entirely.

### Firmware Update

**Built-in updater** (`/progs/bin/upgrade`): `./upgrade -u app.bin`. MD5
verification, A/B rootfs switching. Monitors for `ota_upgrade.bin` in
`/progs/rec/update` and `/progs/rec/00`.

**Cloud OTA:** `updatewt.afdvr.com`, endpoint
`/ota/device/{PK}/{DN}/{version}`. Blocked by our firewall.

**U-Boot:** Hardcoded to SPI NOR (`sf probe 0; sf read ...; bootm`). No SD
card boot fallback. Firmware restore requires UART + ymodem transfer.

### OpenIPC Status

**Hi3516CV610 is NOT supported by OpenIPC.** Too new (2024+). Highest
supported variant is CV500. Flashing CV500 firmware on CV610 will brick.

---

## Known Issues

### OSD Timezone -- FIXED

Fixed by `tools/fix_timezone.py`. Root cause: app set numeric `timezone`
offset but left `posixTZ` as `CST-8` (UTC+8). OSD renderer uses posixTZ.
Fix updates all three (`timezone`, `posixTZ`, `regionTZ`) plus `ntpServer`
(was Chinese `ntp.fudan.edu.cn`, changed to `pool.ntp.org`).

Cloud may re-push timezone via `DeviceTime` MQTT property. Block cloud if
it reverts.

### App Issues (Secueye v2.3.7, tested 2026-05-04)

**Working:** Live video, screen flip, volume, human detection, cross-line
detection, area detection, intercom, digital zoom, photo/video capture,
auto night vision.

**Broken:** Night mode dropdown (no effect), strong light suppression
(no effect), PTZ (no motor hardware on this camera), focus
(fixed focus), playback (error 1100, causes brief unreachability).

**Other:** Timezone reverts to China. Year bug (app picker max 2025).
Camera lags severely when lens covered (possibly NPU overload in low
light or IR LED power draw affecting WiFi). Camera runs warm; SD card
edge hot. No contrast/saturation controls exposed in app.

### ONVIF Settings Don't Apply

Writes accepted, echoed back, never bridged to ISP. See [ONVIF](#onvif).

### DVRIP Binary Dialect

Binary-only responses break all clients. See [DVRIP](#dvrip-port-34567).

### HTTP Snapshots Low Resolution

Both MainStream and SubStream URLs return similar low-res JPEGs (~25 KB),
not the 4K that "MainStream" implies.

---

## Appendix: ISP Functions in superb

### secu_sensor_* (47 functions)

```
brightness              contrast                saturation
saturate_set            sharpness               chroma
set_security_image_effect
set_nightmode           set_daynight_mode       set_ircut
set_sence               set_fps                 set_isp_fps
set_exp_mode            set_exp_params          set_definition
set_dis                 set_local_exposure
ae_set                  ae_get
awb                     white_balance_get
digital_wdr_set         digital_wdr_get
drc_set                 drc_get
wdr                     wdr_switch              wdr_mode_get
mirror_flip             mirror_flip_set
rotate_set              rotate_get
light                   nocolour                antifog
auto_lowframe_enable    low_frame               flicker_enable
b_night                 b_night_auto            b_night_use_ae
Adc_CheckNight_v2       check_live              parse_bin
get_ircut_staus         get_isp_fps             get_again
get_focus_value         get_low_frame           get_b_low_frame
get_dis                 get_local_exposure
daynight_ae_set
exp_mode_strategy_set   exp_mode_strategy_get
```

### hi_mpi_isp_set_* (23 functions)

```
csc_attr                saturation_attr         sharpen_attr
gamma_attr              exposure_attr           wb_attr
ccm_attr                drc_attr                dehaze_attr
nr_attr                 ldci_attr               black_level_attr
color_tone_attr         color_sector_attr       demosaic_attr
anti_false_color_attr   ca_attr                 cac_attr
mesh_shading_attr       fswdr_attr              smart_exposure_attr
bayershp_attr
```

---

## Appendix: HI_XUID Internal Commands

Internal message bus within superb (18 commands):

```
SET_NIGHTVISION_MODE    SET_FLIP_MIRROR         SET_LED
SET_ALARM_ATTR          SET_IDR_FRAME
AI_DETECT_ATTR          AI_DETECT_MODE
STREAM_ATTR             SWITCH_RESOLUTION
SYNC_TIME               KEEP_ALIVE
GET_SNAPSHOT_IMAGE      GET_VERSION
DRAW_OSD                SLAVE_REBOOT
NPU_RESULT              CTRL_ID                 TRANSFER_DATA
```

---

## Appendix: Secueye APK Directory Map

APK: `com.seculink.app_2.3.7.apk`, decompiled with jadx v1.5.5.
~11,800 files (72%) removed (third-party libs). Kept: all Secueye code,
Alibaba IoT/BLE/mesh SDKs.

### App Code

| Directory | Files | Key Contents |
|-----------|-------|-------------|
| `activity/` | 138 | IPCameraActivity, LoginActivity, CameraSettingActivity, AddDeviceActivity (WiFi provisioning) |
| `adapter/` | 51 | DeviceAdapter, WiFiAdapter, ScanBleAdapter |
| `bean/` | 103 | Device, CloudVideo, AlarmPlanBean, WifiBean |
| `bluetooth/` | 9 | AddBleDeviceBusiness, DeviceAddHandler |
| `config/` | 4 | Constants (properties), TMPConstants (commands) |
| `datasource/` | 140 | IoT mesh, AuthManager, Feiyan auth |
| `sdk/` | 10 | IPCManager, IPCDevice (`CLOUD_CHANNEL_ONLY`), LinkVisionAPI |
| `tools/` | 68 | BleClient, G711Code, AudioPlayManager |
| `view/` | 72 | ZoomableTextureView, JoystickTouchViewListener |

### Alibaba SDKs

| Directory | Files | Contents |
|-----------|-------|----------|
| `aisble/` + `a/` | 308 | BLE provisioning (GATT, advertise, encrypt) |
| `meshprovisioner/` + `b/` | 202 | BLE Mesh networking |
| `lv*/` (15 dirs) | 41 | LinkVision SDK (P2P, RTMP, HLS streaming) |
| `com/alibaba/` | 1171 | AILabs IoT, ACCS, CloudAPI |
| `com/aliyun/` | 1533 | LinkVision media, LinkSDK (CoAP/ALCS unused), push |

`com/aliyun/linksdk/` contains CoAP/ALCS local LAN control code, but
`IPCDevice.java` sets `CLOUD_CHANNEL_ONLY` -- the app never uses it.

---

## Local-Only Operation

### Works Without Cloud

| Feature | Method |
|---------|--------|
| WiFi provisioning | BLE (`ble_provision.py`) |
| Live 4K video | VLC -> `rtsp://<ip>/live1` |
| Live sub-stream | VLC -> `rtsp://<ip>/live2` |
| Audio | Embedded in RTSP |
| JPEG snapshots | `http://<ip>:80/snapshot/MainStream` |
| Discovery | ONVIF or ARP scan |
| SD recording | Automatic with SD inserted |

### Requires Cloud

All camera configuration beyond streaming: image quality, night vision mode,
detection sensitivity, recording schedule, PTZ presets, alarm settings, OSD
text, flip/mirror. The camera is architecturally cloud-dependent by design.

### Recommended Clients

**Mobile:** tinyCam Pro (Android, $4, best), ONVIFER (Android, free),
IP Cam Viewer (free tier), LiveCams Pro (iOS).

**Desktop:** VLC, ONVIF Device Manager, Blue Iris (NVR, paid), iSpy/Agent
DVR (open-source NVR).
