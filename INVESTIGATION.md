# SECUEYE X5 Smart Window Camera — Complete Investigation

## Table of Contents

- [Device Overview](#device-overview)
- [Hardware Teardown](#hardware-teardown)
- [Network Identity](#network-identity)
- [BLE Provisioning Protocol](#ble-provisioning-protocol)
- [Network Services](#network-services)
- [ONVIF Interface](#onvif-interface)
- [DVRIP Protocol (Port 34567)](#dvrip-protocol-port-34567)
- [Secueye APK Analysis](#secueye-app-analysis)
- [Security Assessment](#security-assessment)
- [Firmware & Alternative Options](#firmware--alternative-options)
- [UART & Root Shell Findings](#uart--root-shell-findings)
- [Local-Only Operation Guide](#local-only-operation-guide)
- [Known Issues](#known-issues)
- [Scripts Reference](#scripts-reference)
- [Future Work](#future-work)

---

## Device Overview

| Field | Value |
|-------|-------|
| **Product** | SECUEYE Smart Window Camera X5 |
| **Manufacturer** | Seculink Technology (HK) Limited |
| **Platform** | Rebranded Xiongmai (XMeye) |
| **Resolution** | 4K (3840x2160) |
| **Video Codec** | H.264 (H.265 hardware capable) |
| **Audio Codec** | G.711 |
| **WiFi** | Dual-band 2.4G/5G, WiFi 6 |
| **Power** | USB-C (power only, no data) |
| **Storage** | MicroSD card slot (not included) |
| **Mount** | Magnetic |
| **App** | "Secueye" on Play Store (developer: haoshiyou / 郝世友) |
| **Price** | $20 (Amazon Vine) |
| **Firmware Version** | MZ0201V160_EN_20251126 |
| **ONVIF Server** | V9.0, Profile S + Profile T |

---

## Hardware Teardown

### SoC (System on Chip)

| Field | Value |
|-------|-------|
| **Chip** | HiSilicon Hi3516CV610 |
| **Full marking** | Hi3516CV610-20S |
| **Architecture** | Dual-core ARM Cortex-A7 @ 950MHz (confirmed via U-Boot/kernel) |
| **RAM** | 128MB DDR3 (16-bit, 2133MHz rated) |
| **Generation** | 2024+ (newest in Hi3516C line) |
| **Capabilities** | 4K/8MP encoding, H.265+H.264, NPU for AI inference (ot_svp_npu.ko) |

### Flash Memory

| Field | Value |
|-------|-------|
| **Chip** | GD25Q128XX (GigaDevice) |
| **Type** | SPI NOR flash |
| **Size** | 128Mbit (16MB) — confirmed via U-Boot and kernel |
| **Contents** | 7 partitions: U-Boot + env + kernel + rootfs + appfs + config + resources |

### Image Sensor

| Field | Value |
|-------|-------|
| **Sensor** | SmartSens SC635HAI |
| **Resolution** | 6.35MP (3K native, scaled to 4K output) |
| **Interface** | MIPI CSI-2 (via ot_mipi_rx.ko) |
| **Confirmed by** | `/home/variable`, `/etc/conf.d/syscfg/sensor.sh` |

### WiFi Module

| Field | Value |
|-------|-------|
| **Chip** | AltoBeam ATBM6x3x / ATBM6x6x |
| **Interface** | USB (internal, idVendor=007a, idProduct=6162) |
| **Standard** | WiFi 6 (802.11ax), dual-band 2.4G/5G |
| **Driver** | In resfs (`/tmp/resfs/wifi/`) |

### UART Serial Console

- **Connector:** 2-pin JST header on the PCB
- **Signals:** TX, RX (GND available on board ground pads)
- **Voltage:** 3.3V (standard for HiSilicon — do NOT use 5V)
- **Baud rate:** Likely 115200 (standard for HiSilicon/XMeye)
- **Access:** Provides U-Boot console and Linux root shell
- **Adapter needed:** Any 3.3V USB-to-UART (CP2102, FTDI FT232, CH340)

### USB-C Port

Power only. No USB data device enumerates. Windows PnP/USB event logs confirm
zero device events when plugging in. The connector supplies 5V power to the
camera's internal regulator.

---

## Network Identity

| Field | Value |
|-------|-------|
| **BLE MAC** | `38:77:07:75:97:3A` |
| **WiFi MAC** | `38:77:07:75:97:39` |
| **BLE Name** | `ipc_xmy-zZV8` |
| **IP (DHCP)** | `192.168.1.153` |
| **OUI** | `38:77:07` — Xiongmai / Hangzhou Xiongmai Technology |

Note: WiFi MAC is BLE MAC minus 1 in the last octet. Same OUI prefix.

---

## BLE Provisioning Protocol

**Fully reverse-engineered from Secueye APK v2.3.7 decompilation.**

The camera uses BLE (Bluetooth Low Energy) for initial WiFi provisioning. The
protocol is plain UTF-8 text commands — no binary framing, no encryption, no
JSON.

### BLE Service Map

| UUID | Type | Purpose |
|------|------|---------|
| `0x181C` (User Data) | Service | Provisioning service |
| `0x2A8A` | Characteristic (read/write) | Command input |
| `0x2A90` | Characteristic (notify) | Status responses |
| `0x180A` (Device Info) | Service | Device identification |

### Device Information (from 0x180A)

| Characteristic | Value |
|----------------|-------|
| Manufacturer | `nimble4.2` (NimBLE BLE stack, common on ESP32) |
| Firmware | `cronus_1.0` (Xiongmai firmware codename) |
| Software Version | `V1.0.0` |

### BLE Advertisement

| Field | Value |
|-------|-------|
| Name | `ipc_xmy-zZV8` |
| RSSI | -65 (at ~1m) |
| Service UUIDs | `bbb0`, `bbb1`, `ccc0`, `ddd0` (advertisement-only metadata) |
| Manufacturer Data | `{43947: b'\xa0'}` |

The `ipc_xmy` prefix means "IPC XMeye" — the camera identifies itself as a
Xiongmai device over BLE.

### WiFi Provisioning Sequence

```
1. BLE scan for devices with name containing "ipc_xmy"
2. Connect to camera BLE
3. Subscribe to notifications on 0x2A90
4. Wait ~1.5s for connection to stabilize
5. Send: STATUS?          → Camera replies: STATUS=wifi_wait
6. Send: SSID:<wifi_name>
7. Wait ~500ms
8. Send: PWD:<wifi_password>
9. Poll with: STATUS?     → Camera replies with progress:
     STATUS=wifi_find        — SSID found
     STATUS=wifi_connecting  — Connecting
     STATUS=wifi_success     — Connected (done!)
     STATUS=wifi_failed      — Failed
```

All commands are plain UTF-8 text written to characteristic `0x2A8A`. The colon
is a literal delimiter. No spaces, no quotes.

### Other BLE Commands

| Command | Response | Purpose |
|---------|----------|---------|
| `STATUS?` | `STATUS=wifi_wait\|wifi_find\|wifi_connecting\|wifi_success\|wifi_failed` | Poll WiFi state |
| `PK&DN?` | `pk=<key>&dn=<name>` | Get Alibaba IoT product key & device name |
| `UNBIND?` | (acknowledged) | Unbind device from cloud account |

### Why Previous Fuzzing Failed

Earlier attempts sent JSON objects, semicolon-delimited strings, and binary
payloads. The camera silently accepts and ignores anything that doesn't match
the exact `SSID:`, `PWD:`, or `STATUS?` format. Without a `STATUS?` poll, the
camera never sends notifications on `0x2A90`.

### Source Code Reference

- `tools/BleClient.java` — BLE client implementation (UUIDs, sendOrder2, notification handler)
- `activity/AddDeviceActivity.java:211-240` — WiFi provisioning sequence
- `activity/BleScantActivity.java:137-164` — Same sequence, alternate UI path

---

## Network Services

### Open Ports

| Port | Protocol | Service | Auth |
|------|----------|---------|------|
| **80** | TCP | ONVIF SOAP server (V9.0) | None |
| **554** | TCP | RTSP streaming | None |
| **9999** | TCP | Root shell (tcpsvd backdoor) | None |
| **34567** | TCP | DVRIP/Sofia (binary dialect) | None (all logins accepted) |

### Closed Ports (Notable)

| Port | Expected Service | Status |
|------|-----------------|--------|
| 23 | Telnet | Closed (disabled in firmware) |
| 9527 | XMeye debug console | Closed (disabled in firmware) |
| 9530 | XMeye telnet enabler (macGuarder) | Closed (disabled in firmware) |
| 34599 | XMeye secondary data | Closed |
| 8899 | XMeye HTTP alt | Closed |

### RTSP Streams

| Stream | Resolution | Encoding | URL |
|--------|-----------|----------|-----|
| Main | 3840x2160 (4K) | H.264 | `rtsp://192.168.1.153/live1` |
| Sub | 720x576 | H.264 | `rtsp://192.168.1.153/live2` |

No authentication required. Audio (G.711) is embedded in both streams.

Alternative RTSP URL formats (from iSpy database for XMeye "H.265" model):
```
rtsp://<ip>/user=admin_password=_channel=1_stream=0.sdp
rtsp://<ip>/1
```

### HTTP Snapshots

| URL | Description |
|-----|-------------|
| `http://192.168.1.153:80/snapshot/MainStream` | JPEG snapshot (low-res despite name) |
| `http://192.168.1.153:80/snapshot/SubStream` | JPEG snapshot (low-res) |

No authentication required. Refresh to get a new frame.

---

## ONVIF Interface

Port 80 serves a full ONVIF SOAP server. No web UI, no login page — purely
ONVIF XML over HTTP POST.

### Device Information (ONVIF)

| Field | Value |
|-------|-------|
| Manufacturer | SECULINK |
| Model | SECUEYE Camera |
| Firmware Version | MZ0201V160_EN_20251126 |
| Hardware ID | 1.0 |
| Serial Number | (empty) |

### ONVIF Services Available

| Service | Endpoint |
|---------|----------|
| Device | `http://<ip>:80/onvif/device_service` |
| Media | `http://<ip>:80/onvif/media_service` |
| Events | `http://<ip>:80/onvif/event_service` |
| Imaging | `http://<ip>:80/onvif/image_service` |
| PTZ | `http://<ip>:80/onvif/ptz_service` |
| DeviceIO | `http://<ip>:80/onvif/deviceIO_service` |

### ONVIF Scopes

```
onvif://www.onvif.org/Profile/Streaming
onvif://www.onvif.org/Profile/T
onvif://www.onvif.org/location/country/china
onvif://www.onvif.org/type/video_encoder
onvif://www.onvif.org/name/IP-Camera
onvif://www.onvif.org/hardware/IPCamera
```

### ONVIF Media Profiles

| Profile | Resolution | Encoding |
|---------|-----------|----------|
| MainStream / VideoMain | 3840x2160 | H.264 |
| SubStream / VideoSub | 720x576 | H.264 |
| Audio (A_ENC_*) | — | G.711 |

### ONVIF Limitations

The ONVIF server is a **shallow shim** that does not bridge to the camera's
actual firmware settings:

- **SetSystemDateAndTime** — Accepted and echoed back, but does NOT change the
  OSD timestamp or the camera's internal clock
- **Imaging settings** (brightness, contrast, saturation, sharpness) — Accepted
  and echoed back, but do NOT change the actual video output
- **Live video in ONVIF clients** (ODM, etc.) — Often shows "no signal" even
  though the RTSP URL is correct. The ONVIF streaming setup may not properly
  negotiate the media session

ONVIF is useful for **discovery** and **reading device metadata**, but not for
actual camera configuration on this firmware.

---

## DVRIP Protocol (Port 34567)

### Protocol Overview

DVRIP (also called "Sofia protocol" or "NetSDK") is Xiongmai's proprietary
camera control protocol. It normally uses JSON payloads over a binary-framed
TCP connection.

### Binary Header Format (20 bytes)

```
Offset  Size  Field
0       1     head_flag     (always 0xFF)
1       1     version       (0x01)
2       2     reserved      (0x00 0x00)
4       4     session_id    (little-endian uint32)
8       4     sequence      (little-endian uint32)
12      1     total_packets
13      1     current_packet
14      2     message_id    (little-endian uint16)
16      4     data_length   (little-endian uint32)
```

### This Camera's Binary Dialect

This camera accepts standard JSON commands inbound but returns **binary-only
responses** instead of JSON. This is unusual and breaks all existing DVRIP
clients (General VMS, python-dvr, etc.).

### Login Response (24 bytes binary)

```
Field 0: ret            = 1  (success)
Field 1: alive_interval = 10 (seconds)
Field 2: status         = 100 (0x64)
Field 3: type           = 2
Field 4: session_id     = (incrementing)
Field 5: reserved       = 0
```

Login always succeeds regardless of username/password. Tested with:
- `admin` / (blank) — success
- `admin` / `admin` — success
- `admin` / `I0TO5Wv9` (universal XMeye backdoor) — success
- `default` / (blank) — success
- `user` / (blank) — success
- Any arbitrary credentials — success

**There is no authentication on this camera.**

### Generic Config Response (16 bytes binary)

Most config get/set commands return this:

```
Bytes: [session_id(4)] [0x64000000(4)] [0x00000000(4)] [0x00000000(4)]
```

The `0x64` (100 decimal) likely means "command received but no data / not
implemented for this config path."

### Commands That Return Real Data

| MsgID | Command | Response |
|-------|---------|----------|
| 1452 | OPMachine (time query) | 48 bytes: 16-byte header + 32 bytes with year, month, day, hour, minute, second, weekday, 0 as uint32 little-endian fields |
| 1048 | ConfigExport | 2064 bytes: contains ASCII string "H.265 IPC" — hardware model identifier |
| 1440 | PTZ query | 11796 bytes: preset position table (all zeroed — no presets) |

### OPMachine Time Response Format

```
Offset 0-15:  Standard 16-byte response header
Offset 16-19: Year    (uint32 LE) — e.g., 0x07EA = 2026
Offset 20-23: Month   (uint32 LE)
Offset 24-27: Day     (uint32 LE)
Offset 28-31: Hour    (uint32 LE)
Offset 32-35: Minute  (uint32 LE)
Offset 36-39: Second  (uint32 LE)
Offset 40-43: Weekday (uint32 LE)
Offset 44-47: Reserved (0)
```

The camera's internal UTC clock is accurate. The OSD timestamp displays in
the wrong timezone (UTC+8 / China time).

### Why VMS Shows "Connected" But "Connections: 0"

General VMS (2021 beta) sends JSON login, receives binary response, attempts
`json.loads()` on the binary data, fails silently. The TCP session establishes
but the application-layer handshake never completes. The VMS reports the TCP
connection but no functional DVRIP session.

### Known DVRIP Message IDs

```
1000  LOGIN_REQ          1001  LOGIN_RSP
1006  KEEPALIVE_REQ      1007  KEEPALIVE_RSP
1020  SYSINFO_REQ        1021  SYSINFO_RSP
1040  CONFIG_GET          1041  CONFIG_GET_RSP
1042  CONFIG_GET2         1043  CONFIG_GET2_RSP
1044  CONFIG_SET          1046  CONFIG_DEFAULT
1048  CONFIG_EXPORT       1049  CONFIG_EXPORT_RSP
1100  MONITOR_REQ         1440  PTZ_REQ
1450  TIMESETTING_REQ    1452  OPMACHINE_REQ
1460  SYSOPERATION_REQ   1500  GUARD_REQ
```

---

## Secueye App Analysis

### Decompilation

- **Tool:** jadx v1.5.5
- **APK:** `com.seculink.app_2.3.7.apk`
- **Classes:** 30,383 | Methods: 188,168 | Instructions: 5,347,825
- **Decompilation errors:** 52 (normal for obfuscated APK)

### Architecture: Cloud-Only

**The Secueye app contains ZERO DVRIP code.** It never connects to port 34567.

All camera communication goes through Alibaba Cloud IoT (Aliyun Link Vision):

```
Secueye App
    ↓ HTTPS REST API + MQTT-over-TLS
Alibaba Cloud IoT Platform
    ↓ MQTT
Camera's Cloud Agent Module
    ↓ Internal function calls
Sofia firmware (DVRIP, RTSP, ONVIF)
```

### Key SDK Components

| Class | Purpose |
|-------|---------|
| `sdk/IPCManager.java` | Singleton entry point, delegates to cloud APIs |
| `sdk/IPCDevice.java` | Wraps Aliyun PanelDevice, uses `CLOUD_CHANNEL_ONLY` strategy |
| `sdk/LinkVisionAPI.java` | HTTPS REST calls to `/vision/customer/*` endpoints |
| `sdk/ChannelManager.java` | MQTT push channel for real-time events |
| `sdk/SDKInitHelper.java` | App Key: `26001873`, Secret: `f4a90ebee699166af95b092dffadcfc3` |

### Video Streaming (Native Library)

The app uses a native library (`liblinkvision.so`) for video, not DVRIP:
- **P2P** — Primary method, STUN/TURN NAT traversal
- **RTMP** — Fallback cloud relay
- **HLS** — Cloud recordings playback

### Camera Properties (from `config/Constants.java`)

These are the Thing Model properties the app can get/set via the cloud:

| Property | Purpose |
|----------|---------|
| `MotionDetectSensitivity` | Motion detection sensitivity |
| `AlarmSwitch` | Alarm enable/disable |
| `AlarmMode` | Smart/all-day/timing |
| `NightVisionMode` | Day/night/auto |
| `ImageFlipState` | Image flip toggle |
| `StorageRecordMode` | SD card recording mode |
| `StorageStatus` | SD card status |
| `TimeRecordEnable` | Scheduled recording toggle |
| `TimeRecordPlan` | Recording schedule |
| `StreamVideoQuality` | Main stream quality |
| `SubStreamVideoQuality` | Sub stream quality |
| `StorageRecordQuality` | Recording quality |
| `IRLightBrightness` | IR LED brightness |
| `WhiteLightBrightness` | White LED brightness |
| `FloodlightSwitch` | White light on/off |
| `FloodlightSchedule` | White light schedule |
| `AlarmLightSwitch` | Alarm strobe |
| `IntelligentTracking` | PTZ auto-tracking |
| `CrossLineDetect` | Tripwire detection |
| `RegionDetect` | Region intrusion detection |
| `FaceDetectSensitivity` | Human/face detection sensitivity |
| `EncryptSwitch` | Video encryption |
| `RebootSchedule` | Auto-reboot schedule |
| `IvpAbility` | Intelligence feature bitmask |

### Camera Commands (from `config/TMPConstants.java`)

| Command | Purpose |
|---------|---------|
| `StartPTZAction` / `StopPTZAction` | PTZ control |
| `ZoomActionControl` | Optical zoom |
| `PresetLocateControl` / `PresetAddControl` | PTZ presets |
| `Restart` | Reboot camera |
| `DeviceDefault` | Factory reset |
| `FormatStorageMedium` | Format SD card |
| `QueryTFCard` | SD card status |
| `SetWifi` | Change WiFi config |
| `QueryAPList` | Scan for WiFi networks |
| `QueryRecordDateList` / `QueryRecordTimeList` | Recording queries |
| `QueryFileList` / `DeleteFile` | File management |

### Cloud API Endpoints

| Path | Purpose |
|------|---------|
| `/awss/time/window/user/bind` | Bind device to user account |
| `/thing/info/get` | Get device online status |
| `/vision/customer/storage/picture/capture` | Cloud snapshot |
| `/vision/customer/storage/device/record/query` | Cloud recording query |
| `/vision/customer/eventrecord/plan/*` | Recording plan CRUD |
| `https://traffic.secueye.app/api/app/*` | Secueye backend |

### Account Verification

Email verification codes come from `cloud_intelligence@service.aliyun.com`
(Alibaba Cloud / Aliyun).

---

## Security Assessment

### Authentication: NONE

| Service | Authentication |
|---------|---------------|
| RTSP (554) | None — any client can stream |
| ONVIF (80) | None — full SOAP access |
| DVRIP (34567) | None — all credentials accepted |
| HTTP Snapshots (80) | None — direct URL access |
| BLE | None — any BLE client can send commands |

**Anyone on the same LAN can view, snapshot, and send commands to this camera.**

### Cloud Connectivity (Default)

Out of the box, the camera phones home to Alibaba Cloud via:
- MQTT-over-TLS for command/control
- P2P with STUN/TURN for video streaming
- HTTPS for API calls

This creates an **outbound tunnel** that allows the Secueye app to access the
camera from anywhere in the world. The camera does NOT need inbound port
forwarding — it initiates the connection outward.

### Backdoor Ports

All known Xiongmai backdoor services are **disabled** in this firmware:
- Port 9527 (debug console) — closed
- Port 9530 (telnet enabler / macGuarder) — closed
- Port 23 (telnet) — closed
- DVRIP telnet-enable commands — accepted but non-functional

Known backdoor passwords (`I0TO5Wv9`, `xmhdipc`, `xc3511`, etc.) are
irrelevant since DVRIP accepts all credentials anyway.

### Mitigation

**One firewall rule blocks all cloud access while preserving LAN functionality:**

DD-WRT: Administration → Commands → Save Firewall:
```
iptables -I FORWARD -s 192.168.1.153 -o $(nvram get wan_iface) -j DROP
```

Also verify: DD-WRT → NAT/QoS → UPnP → **Disabled**

Also recommended: assign a static DHCP lease for `38:77:07:75:97:39` →
`192.168.1.153` so the IP doesn't change.

---

## Firmware & Alternative Options

### Current Firmware

| Field | Value |
|-------|-------|
| Version | MZ0201V160_EN_20251126 |
| SoC | Hi3516CV610 |
| Main process | Sofia (Xiongmai standard) |
| ConfigExport ID | "H.265 IPC" |
| DVRIP behavior | Binary-only responses (non-standard) |
| Backdoor ports | All disabled |

### OpenIPC Status

**Hi3516CV610 is NOT supported by OpenIPC.** It is not listed in their
supported hardware at all. The chip is too new (2024+). The highest supported
Hi3516C variant is CV500.

Flashing CV500 firmware on CV610 hardware **will brick the camera** — the boot
sequence, hardware initialization, and driver stack are incompatible between
chip generations.

### Hi3516C Generation Comparison

| Chip | Era | Max Res | CPU | H.265 | OpenIPC |
|------|-----|---------|-----|-------|---------|
| CV100 | ~2013 | 1080p | ARM926 | No | DONE |
| CV200 | ~2015 | 1080p | ARM926 | No | DONE |
| CV300 | ~2017 | 1080p | ARM926 | Yes | DONE |
| CV500 | ~2019 | 4MP | Cortex-A7 | Yes | DONE |
| **CV610** | **~2024** | **4K/8MP** | **Cortex-A7/A53** | **Yes** | **NOT SUPPORTED** |

### Path to Shell Access — COMPLETED

Root shell access has been obtained via two methods:

#### Method 1: U-Boot Interrupt (UART)

1. Connect 3.3V USB-to-UART adapter (TX→RX, RX→TX, shared GND)
2. Open terminal: 115200 baud, 8N1
3. Power-cycle camera while spamming Ctrl+C / Enter on serial
4. U-Boot has a 1-second `Hit any key to stop autoboot` window
5. At the `#` prompt, bypass login with:
   ```
   setenv bootargs mem=40m earlycon=pl011,0x11040000 console=ttyAMA0,115200 clk_ignore_unused initcall_debug rw root=/dev/mtdblock3 rootfstype=squashfs mtdparts=sfc:320K(boot),64K(bootargs),2M(kernel),1280K(rootfs),5M(appfs),1M(configfs),6528K(resfs) init=/bin/sh
   boot
   ```
6. Kernel boots directly into a root shell (no login required)

#### Method 2: Persistent WiFi Backdoor (No UART Needed)

A `debug.sh` script was written to the writable configfs (jffs2) partition.
On every normal boot, the camera's `startup.sh` checks for
`/etc/conf.d/debug.sh` and executes it. Our script starts a root shell
listener using `tcpsvd` on port 9999, then launches the normal camera
firmware (`superb`).

```
# Connect from any machine on the LAN:
nc 192.168.1.153 9999
# or use PuTTY: Raw connection to port 9999
```

The backdoor file lives at `/etc/conf.d/debug.sh` (configfs jffs2 mount).
To remove: `rm /etc/conf.d/debug.sh` and reboot.

**Note:** busybox on this firmware has `telnetd`, `nc`, `httpd`, and `wget`
stripped out. The backdoor uses `tcpsvd` (TCP service daemon) instead, which
is compiled into busybox.

---

## UART & Root Shell Findings

### U-Boot Environment

| Field | Value |
|-------|-------|
| **U-Boot version** | 2022.07 (Jun 06 2025) |
| **Build** | hi3516cv610-debug |
| **Boot delay** | 1 second |
| **Boot command** | `sf probe 0; sf read 0x41000000 0x60000 0x200000; bootm 0x41000000` |
| **Console** | ttyAMA0 @ 115200 (PL011 UART) |
| **Kernel format** | FIT image (Flattened Image Tree) |

### Linux Kernel

| Field | Value |
|-------|-------|
| **Version** | 5.10.221 |
| **Build date** | Mon Jun 9 09:02:27 UTC 2025 |
| **Builder** | `xiely@ubuntu24` |
| **Toolchain** | arm-v01c02-linux-musleabi-gcc 10.3.0 (musl-1.2.3) |
| **Architecture** | ARMv7, SMP (2 CPUs), Cortex-A7 @ 24MHz timer |
| **C library** | musl libc (not glibc) |
| **Root filesystem** | squashfs on /dev/mtdblock3 (read-only) |

### Hardware Details (Confirmed via Kernel/U-Boot)

| Component | Value |
|-----------|-------|
| **SoC** | Hi3516CV610 (dual-core ARM Cortex-A7, 950MHz) |
| **RAM** | 128MB DDR3 (16-bit, 2133MHz rated) |
| **SPI Flash** | GD25Q128XX (GigaDevice), 16MB |
| **Image Sensor** | SmartSens SC635HAI (6.35MP, confirmed in `variable` and `sensor.sh`) |
| **WiFi Chip** | AltoBeam ATBM6x3x/6x6x (USB, WiFi 6) — `idVendor=007a, idProduct=6162` |
| **USB** | xHCI USB 3.0 host controller (WiFi adapter connected internally via USB) |
| **Ethernet PHY** | Generic PHY on MDIO (not physically exposed) |

### Flash Partition Layout (16MB SPI NOR)

| # | Name | Offset | Size | Filesystem | Contents |
|---|------|--------|------|------------|----------|
| 0 | boot | 0x000000 | 320KB | raw | U-Boot bootloader |
| 1 | bootargs | 0x050000 | 64KB | raw | U-Boot environment variables |
| 2 | kernel | 0x060000 | 2MB | FIT image | Linux kernel + DTB |
| 3 | rootfs | 0x260000 | 1.25MB | squashfs | Root filesystem (BusyBox, /etc) |
| 4 | appfs | 0x3A0000 | 5MB | squashfs | Application files (superb, drivers, libs) |
| 5 | configfs | 0x8A0000 | 1MB | jffs2 | **Writable** config storage |
| 6 | resfs | 0x9A0000 | 6.375MB | squashfs | Resources (WiFi/BLE/sensor/voice/IVP) |

### Firmware Dump

All 7 partitions have been dumped over WiFi and verified with MD5 checksums.
Files are in the `firmware/` directory:

```
firmware/
  mtd0_boot.bin       327,680 bytes  MD5: 29db41a77f2cc88d5be2ef5aef1e5441
  mtd1_bootargs.bin    65,536 bytes  MD5: 10e3bc84a77a944c53f900694c3eda12
  mtd2_kernel.bin   2,097,152 bytes  MD5: c9e2a956af28368e8797caaccbc4f14f
  mtd3_rootfs.bin   1,310,720 bytes  MD5: b351f36d2c762f553a44fc2fcbbb1218
  mtd4_appfs.bin    5,242,880 bytes  MD5: b31a1ef13b3a9229e56f6363aa34b230
  mtd5_configfs.bin 1,048,576 bytes  MD5: 1bf274961f7ff3921497c0bfb326c036
  mtd6_resfs.bin    6,684,672 bytes  MD5: 913d8b75cf21cd7e8a7b922e90ed8549
  full_flash.bin   16,777,216 bytes  (concatenation of all partitions)
  checksums.md5                      (verification file)
```

### Boot Sequence

```
1. U-Boot loads kernel FIT image from SPI flash offset 0x60000
2. Kernel boots, mounts squashfs rootfs on /dev/mtdblock3
3. BusyBox init runs /etc/init.d/rcS:
   a. S00devs — creates /dev/console, /dev/ttyAMA0, etc.
   b. S01udev — starts mdev
   c. S80network — configures network from kernel cmdline (if present)
   d. S90hibernate — checks for hibernate resume
   e. rcS mounts appfs (/dev/mtdblock4) → /tmp/appfs
   f. rcS bind-mounts /tmp/appfs/progs → /progs and /tmp/appfs/home → /home
   g. rcS runs /home/bashrc.sh
4. bashrc.sh:
   a. Overlays /etc/ and /progs/rec with tmpfs (writable layer)
   b. Mounts configfs (jffs2, /dev/mtdblock5) → /etc/conf.d/
   c. Sources /home/variable (hardware config) and /etc/conf.d/fixed/hwconfig.cfg
   d. Mounts resfs (/dev/mtdblock6) → /tmp/resfs
   e. Bind-mounts WiFi, BLE, IVP, sensor, voice resources from resfs
   f. Loads kernel modules (HiSilicon media pipeline: ot_*.ko)
   g. Runs /progs/startup.sh
5. startup.sh:
   a. IF /etc/conf.d/debug.sh exists → executes it (our backdoor path)
   b. ELSE → launches /tmp/appfs/progs/bin/superb (main camera binary, 7.8MB)
   c. After 15s, runs PQTools.sh for image quality calibration
6. inittab spawns /bin/login on ttyS000 (UART console)
```

### Main Camera Binary

| Field | Value |
|-------|-------|
| **Binary** | `/tmp/appfs/progs/bin/superb` |
| **Size** | 7,846,580 bytes (7.5MB) |
| **Function** | Replaces "Sofia" in standard Xiongmai — handles RTSP, ONVIF, DVRIP, cloud, recording, ISP |
| **Helper** | `mySystem` (9,720 bytes) — watchdog/monitor process |
| **Upgrade tool** | `upgrade` (42,904 bytes) — firmware update handler |

### Root Password Hashes

| Source | Hash | Password |
|--------|------|----------|
| `/etc/passwd` (rootfs) | `04h6XLo9zAfEM` (DES crypt, salt=`04`) | **`sl.x.`** (CRACKED via hashcat) |
| `/home/passwd` (appfs) | `GIgEh3ZZNHRh2` (DES crypt, salt=`GI`) | Not yet cracked (exhausted 1-6 chars) |
| `/etc/shadow` | `$6$kZOiX1vJ1cPLQ9X9$tOVj31T7yXhl1B1jCmnzznBv3YW1bSK9y16dAWHin0/evOSMs7AURmhvjrbYeW1Cd5TyIQjI8CLYHrZwi8IH7/` (SHA-512) | Same as rootfs passwd (same user) |

The rootfs password `sl.x.` was cracked by hashcat (DES mode 1500) on an
RTX 5090 in ~20 seconds using brute-force of all printable ASCII up to 8
characters. The password is non-standard and does not appear in any known
Xiongmai/IoT password list.

The appfs hash uses a different password (different salt AND different hash
output). Brute-force exhausted the 1-6 character keyspace (~735 billion
candidates) in ~1.5 minutes. The 7-character keyspace (~69.8 trillion)
requires ~2.4 hours, and 8 characters (~6.6 quadrillion) ~9.5 days at
8 GH/s.

### Credentials Found in Config Files

| File | Username | Password | Purpose |
|------|----------|----------|---------|
| `configfs/syscfg/onvif_user.json` | `admin` | `admin` | ONVIF access (level 0 = admin) |
| `configfs/syscfg/onvif_user.json` | `user` | `123456` | ONVIF access (level 2 = viewer) |
| `configfs/syscfg/danale_private.cfg` | `admin` | `ZKAcmKhE` | Danale cloud platform credentials |

### Alibaba IoT Cloud Credentials

The camera's Alibaba IoT (Aliyun Link Vision) credentials are stored in
plaintext across multiple files on the configfs partition.

**Device Identity** (from `/etc/conf.d/lic.bin`):

| Field | Value |
|-------|-------|
| Product Key | `a1y8M6TXvzw` |
| Device Name | `zZV8td5Gt8IQzlUVdMXE` |
| Device Secret | `3ce9ed72245abc91b74b96e651d8ad3c` |
| License Token | Base64 blob (~160 bytes, stored in `lic.bin`) |

**MQTT Connection** (from `/etc/conf.d/aiot_kv.bin` and UART boot log):

| Field | Value |
|-------|-------|
| Broker (plaintext) | `public.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883` |
| Broker (TLS) | `public.iot-as-mqtt.cn-shanghai.aliyuncs.com:443` |
| Auth method | TLS + custom-ilop, hmacsha1 token |
| Client SDK | `sdk-c-2.3.0_FY_1.6.6-6` |
| ClientID format | `<ProductKey>.<DeviceName>\|securemode=2,tokenType=0,token=<hex>,signmethod=hmacsha1,...\|` |

**Key-Value Store** (`/etc/conf.d/aiot_kv.bin`):
- Header: `KD` magic, `kv_verion1.0`
- Contains MQTT URL and ~70 `seed_key` entries (4-byte cryptographic seeds
  used for key derivation in the Alibaba IoT SDK)

**UART Boot Leak**: During normal boot, the cloud agent prints the full
ClientID string (including auth token), CA certificate snippet, and
connection status to the UART console. This is the `superb` binary's
Alibaba IoT module initializing.

These credentials could theoretically be used to impersonate the camera on
the Alibaba IoT platform, or to build a local MQTT broker that the camera
connects to (by redirecting DNS for `aliyuncs.com`).

### Key Config Files on configfs (Writable Partition)

| File | Contents |
|------|----------|
| `syscfg/SystemCfg.ini` | Master config (~12KB) — encoding, OSD, alarm, PTZ, ISP settings |
| `syscfg/account.dat` | Binary account data (XOR-obfuscated, JVS format) |
| `syscfg/network/wpa_supplicant.conf` | WiFi credentials (SSID + PSK in plaintext) |
| `syscfg/network/wifipasswd.cfg` | WiFi password (plaintext) |
| `syscfg/network/mac.cfg` | MAC address override command |
| `syscfg/sensor.sh` | Image sensor model (SC635HAI) |
| `fixed/hwconfig.cfg` | Hardware config (PTZ, lights, motor, features) |
| `fixed/base.cfg` | OTA update URLs (`updatewt.afdvr.com`, etc.) |
| `fixed/version` | `firstversion=V2.4.` |
| `customer.json` | Empty customer branding fields |
| `devInfo` | `version=MZ0201V160_CN_20251126` |

### Kernel Modules (HiSilicon Media Pipeline)

The `ot_*.ko` modules use HiSilicon's newer "OpenT" naming convention:

| Module | Purpose |
|--------|---------|
| `ot_sys.ko`, `ot_base.ko`, `ot_osal.ko` | System/base infrastructure |
| `ot_vi.ko` | Video input (sensor interface) |
| `ot_isp.ko` | Image signal processor |
| `ot_vpss.ko` | Video processing subsystem |
| `ot_venc.ko` | Video encoder (main) |
| `ot_h264e.ko`, `ot_h265e.ko` | H.264/H.265 codec modules |
| `ot_jpege.ko` | JPEG encoder |
| `ot_rgn.ko` | Region/OSD overlay |
| `ot_ai.ko`, `ot_ao.ko`, `ot_aenc.ko`, `ot_adec.ko` | Audio pipeline |
| `ot_mipi_rx.ko` | MIPI CSI-2 receiver (sensor interface) |
| `ot_ive.ko` | Intelligent video engine |
| `ot_svp_npu.ko` | Neural processing unit (AI inference) |
| `ot_vca.ko` | Video content analysis |
| `ot_mmz.ko` | Media memory zone |
| `ot_cipher.ko` | Hardware crypto engine |

### Running Processes (Normal Boot)

```
PID  COMMAND
  1  init
1037  /tmp/appfs/progs/bin/mySystem          (watchdog/monitor)
1041  tcpsvd 0.0.0.0 9999 /bin/sh -il        (our backdoor)
1042  {MainThrd} /tmp/appfs/progs/bin/superb  (main camera binary)
1120  /home/wifi/wpa_supplicant ...           (WiFi client)
1330  /bin/login                              (UART console)
```

Plus kernel threads for jffs2 GC, VICAP (video capture IRQ), SDHCI,
cfg80211, and the AltoBeam WiFi driver (`phy0-atbm_*`).

### Open TCP Ports (Confirmed via netstat)

| Port | PID | Process | Purpose |
|------|-----|---------|---------|
| 80 | superb | ONVIF SOAP server | Device discovery, media profiles |
| 554 | superb | RTSP server | Live video streaming |
| 9999 | tcpsvd | Root shell backdoor | Remote administration |
| 34567 | superb | DVRIP protocol | Camera control (binary responses) |

No UDP listeners. Cloud MQTT connections are outbound-only.

### SD Card Provisioning (updateID.sh)

The factory provisioning script `/progs/updateID.sh` reads from specific SD
card directory structures to program device identities. This reveals how
Seculink provisions cameras on the assembly line:

| SD Card Path | Purpose |
|--------------|---------|
| `seculinkAliyunUid/aliyunUid/*.conf` or `*.txt` | Alibaba IoT device credentials → `lic.bin` |
| `seculinkDanaleUid/danaleUid/*.conf` | Danale cloud platform credentials |
| `seculinkMac/seculinkMAC.txt` | MAC address assignment (from pool) |
| `seculinkIdRecycle/recycle_ali.sh` | **ARBITRARY CODE EXECUTION** (see below) |
| `seculinkVoice/language/*.711` | Custom voice prompt files |
| `seculinkVoice/common/*.711` | Common voice prompt files |
| `seculinkHardware/hwconfig.cfg` | Hardware configuration override |
| `wifi/wpa_supplicant.conf` | WiFi credentials override |
| `UserMallCfg.txt` | Custom app store URL (`url=...`) |
| `*.json` (root of SD) | Hardware info override → `hwinfo.json` |

This means you can re-provision the camera's cloud identity, change WiFi,
or modify hardware config by placing files on an SD card and rebooting.
The MAC prefix `A6:88:E0` is the factory MAC pool for Seculink devices.

#### SD Card Root Shell (recycle_ali.sh) -- UART-Free Jailbreak

**Critical finding**: `updateID.sh` runs on every boot (called from `bashrc.sh:125`)
and **sources** an arbitrary shell script from the SD card:

```bash
# From updateID.sh lines 10-13:
if [ -f /var/udisk/seculinkIdRecycle/recycle_ali.sh ]; then
    chmod 777 /var/udisk/seculinkIdRecycle/recycle_ali.sh
    source /var/udisk/seculinkIdRecycle/recycle_ali.sh
fi
```

The `source` command executes the script **in the current shell context as root**,
before `superb` starts. This is a full code execution vector from SD card with
no UART required.

**To jailbreak any camera of this model:**
1. Format SD card as FAT32
2. Create directory: `seculinkIdRecycle/`
3. Create file: `seculinkIdRecycle/recycle_ali.sh` with desired commands
4. Insert SD card, power cycle the camera
5. Script executes as root during boot

This can be used to install the `debug.sh` backdoor (tcpsvd shell on port 9999),
modify any config in `/etc/conf.d/`, dump firmware, or flash modified partitions
via the built-in `upgrade` binary at `/progs/bin/upgrade`.

Additionally, the `upgrade` binary monitors for `ota_upgrade.bin` in
`/progs/rec/update` and `/progs/rec/00` (the SD mount point), providing
a second firmware update vector once root access is established.

### Shared Libraries

| Library | Purpose |
|---------|---------|
| `libiw.so.29` | Wireless tools (iwconfig/iwlist) |
| `libsecurec.so` | HiSilicon secure C library (bounds-checked string ops) |
| `libupvqe.so` | Uplink VQE (voice quality enhancement) |
| `libvoice_engine.so` | Voice engine (two-way audio) |
| `libvqe_aec.so` | Acoustic echo cancellation |
| `libvqe_agc.so` | Automatic gain control |
| `libvqe_anr.so` | Audio noise reduction |
| `libvqe_eq.so` | Audio equalizer |
| `libvqe_hpf.so` | High-pass filter |
| `libvqe_record.so` | Recording VQE |
| `libvqe_res.so` | Audio resampler |
| `libvqe_talkv2.so` | Two-way talk v2 |

### Factory Reset Button Behavior

The physical reset button triggers a factory reset when **held for 1.5
seconds**. The `superb` binary polls a GPIO pin and counts detection cycles
(`reset_detect_times`). A voice prompt (`reset.711`) plays during the reset.

**What factory reset does** (from embedded shell commands in `superb`):

| Action | Command |
|--------|---------|
| Wipe all system config | `rm -rf /etc/conf.d/syscfg/*` |
| Wipe network config | `rm -rf /etc/conf.d/syscfg/network/*` |
| Wipe face data | `rm -rf /etc/conf.d/syscfg/face/*` |
| Wipe custom voice files | `rm -rf /etc/conf.d/fixed/custom_voice/*` |
| Remove Alibaba cloud config | `rm /etc/conf.d/aliyun.conf` |
| Remove provisioning flags | `rm -f /etc/conf.d/seted_id` |
| Remove hardware info | `rm -f /etc/conf.d/hwinfo.json` |
| Notify cloud to unbind | `awss_report_reset_to_cloud` / `thing.reset` |
| Restore default network config | Copy from `syscfg/default/interface.cfg` |

**Impact on backdoor:** The factory reset targets specific subdirectories
(`syscfg/*`, `fixed/custom_voice/*`) and named files. Our `debug.sh` sits
at the root of configfs (`/etc/conf.d/debug.sh`), which is NOT explicitly
targeted by any of the cleanup commands. **The backdoor should survive a
factory reset**, but this is not 100% certain -- the `superb` binary may
have additional programmatic cleanup not visible from string analysis alone.

**Two factory reset functions exist:**
- `SYSFuncs_factory_default` -- full reset with reboot
- `SYSFuncs_factory_default_without_reboot` -- reset without reboot
- `SYSFuncs_factory_default_by_remote` -- triggered via cloud/DVRIP
  (`DeviceDefault` command)

If the backdoor is ever lost, it can be re-installed in ~2 minutes via
U-Boot interrupt over UART.

### Firmware Restore Procedure

The firmware dump (`firmware/`) is a raw SPI NOR flash image, NOT a
packaged OTA update. It cannot be used through the camera's normal update
mechanisms (SD card `upgrade` binary or cloud OTA), which expect a signed
`.bin` package with headers and checksums.

**To restore from our dump, use U-Boot over UART:**

```
# 1. Interrupt U-Boot at boot (hold Ctrl+C during power-on)
# 2. Transfer partition file via serial ymodem:
loady 0x42000000
# (send the .bin file from your terminal)
# 3. Erase and write to flash:
sf probe 0
sf erase <offset> <size>
sf write 0x42000000 <offset> <size>
# 4. Boot normally:
boot
```

Partition offsets and sizes for the `sf` commands:

| Partition | Offset | Size | File |
|-----------|--------|------|------|
| boot | 0x000000 | 0x050000 | mtd0_boot.bin |
| bootargs | 0x050000 | 0x010000 | mtd1_bootargs.bin |
| kernel | 0x060000 | 0x200000 | mtd2_kernel.bin |
| rootfs | 0x260000 | 0x140000 | mtd3_rootfs.bin |
| appfs | 0x3A0000 | 0x500000 | mtd4_appfs.bin |
| configfs | 0x8A0000 | 0x100000 | mtd5_configfs.bin |
| resfs | 0x9A0000 | 0x660000 | mtd6_resfs.bin |

**Note:** U-Boot has no WiFi/USB-storage support. File transfer is limited
to serial protocols (`loady` = ymodem, `loadx` = xmodem, `loadb` = kermit).
At 115200 baud, transferring 16MB takes ~25 minutes. For individual
partitions (e.g., restoring configfs at 1MB), it takes ~1.5 minutes.

### BusyBox Configuration

Busybox is fully loaded (~370 applets) but with notable omissions:

**Available:** vi, sed, grep, find, awk, dd, base64, xxd, md5sum, sha256sum,
ip, ifconfig, route, mount, insmod, modprobe, tcpsvd, udpsvd, inetd, passwd,
crontab, udhcpc, udhcpd, and many more.

**Stripped:** telnet, telnetd, nc/netcat, tftp, tftpd, ftpd, httpd, wget.
All remote access and file transfer tools have been intentionally removed.

---

## Local-Only Operation Guide

### What Works Without Any Cloud/App

| Feature | Method | Status |
|---------|--------|--------|
| WiFi provisioning | BLE script (`ble_provision.py`) | Working |
| Live 4K video | VLC → `rtsp://<ip>/live1` | Working |
| Live sub-stream | VLC → `rtsp://<ip>/live2` | Working |
| Audio | Embedded in RTSP streams | Working |
| JPEG snapshots | `http://<ip>:80/snapshot/MainStream` | Working |
| Device discovery | ONVIF or ARP scan | Working |
| Camera identification | ONVIF GetDeviceInformation | Working |

### What Requires SD Card

- On-device motion detection recording
- File playback
- Event storage

### What Requires Cloud (Secueye App)

- Changing motion detection sensitivity
- Changing image settings (brightness, contrast, etc.)
- Night vision mode control
- Recording schedule configuration
- PTZ presets
- Alarm/notification settings
- Cloud storage and playback
- Any camera configuration beyond stream viewing

### What Requires UART Shell Access (or WiFi Backdoor)

- ~~Fixing OSD timezone~~ (requires modifying SystemCfg.ini or superb behavior)
- Enabling DVRIP JSON responses (requires patching superb binary)
- ~~Full firmware dump/analysis~~ — **DONE**, all 7 partitions dumped and verified
- ~~Enabling telnet for remote administration~~ — **DONE**, tcpsvd backdoor on port 9999

### Recommended Mobile Apps (RTSP/ONVIF, No Account)

| App | Platform | Price | Notes |
|-----|----------|-------|-------|
| tinyCam Pro | Android | $4 | Best option. ONVIF discovery, RTSP, alerts |
| ONVIFER | Android | Free | Pure ONVIF client |
| IP Cam Viewer | Android/iOS | Free tier | RTSP viewer |
| LiveCams Pro | iOS | Paid | ONVIF + RTSP |
| VLC | Any | Free | Manual RTSP URL entry |

### Recommended Desktop Software

| Software | Purpose |
|----------|---------|
| VLC | RTSP stream viewing and recording |
| ONVIF Device Manager (ODM) | ONVIF device discovery and inspection |
| Blue Iris | Full NVR with motion detection (paid, Windows) |
| iSpy / Agent DVR | Open-source NVR |

---

## Known Issues

### OSD Timestamp Wrong Timezone

The on-screen display shows UTC+8 (China Standard Time) instead of local time.
The camera's internal UTC clock is correct (verified via DVRIP OPMachine
query). The OSD renderer reads from a timezone config that cannot be changed
via ONVIF or DVRIP on this firmware. **Requires UART shell access to fix.**

### ONVIF Settings Don't Apply

ONVIF SetSystemDateAndTime, imaging settings (brightness/contrast/etc.), and
other configuration commands are accepted and echoed back correctly but do not
bridge to the actual firmware settings. The ONVIF server is a thin shim with
its own state that operates independently from the camera's real configuration.

### ONVIF Live Video "No Signal" in ODM

ONVIF Device Manager and similar clients may show "no signal" when attempting
live video, even though the RTSP URL is reported correctly. The ONVIF media
session negotiation may not complete properly. Workaround: use the RTSP URL
directly in VLC.

### VMS "Connected" But Non-Functional

Xiongmai General VMS (2021 beta) connects TCP to port 34567 but cannot parse
the binary login response. Shows "connected" with "connections: 0" and
"unknown record status." This is a protocol incompatibility with the camera's
newer binary DVRIP dialect.

### HTTP Snapshots Low Resolution

Both `/snapshot/MainStream` and `/snapshot/SubStream` return similar low-
resolution JPEG images (~25KB), not the full 4K resolution that MainStream
implies.

---

## Scripts Reference

Requires Python 3.12+ and `pip install bleak pyserial passlib`.

### Root Directory

| Script | Purpose |
|--------|---------|
| `dump_firmware.py` | **Dump all flash partitions over WiFi.** Auto-detects camera, reads all 7 MTD partitions + full 16MB SPI NOR image via base64-encoded `dd`, verifies MD5. |
| `analyze_firmware.py` | **Firmware analysis report.** Extracts file trees from all 3 squashfs partitions, runs strings analysis on `superb` binary (URLs, paths, credentials, hardcoded IPs). |

### tools/ -- Camera Interaction

| Script | Purpose |
|--------|---------|
| `cam_cmd.py` | **General-purpose remote command tool.** Runs any shell command on the camera via the port 9999 root shell and prints output. |
| `isp_control.py` | **ISP register control.** Reads/writes Hi3516CV610 ISP CSC registers (brightness, contrast, saturation, hue) via `bspmm`. Subcommands: `read`, `set brightness 60`, `reset`. Full register map documented in script. |
| `fix_timezone.py` | **Fix OSD timezone.** Changes timezone from UTC+8 (China default) to local timezone in `SystemCfg.ini`, updates NTP server, restarts `superb`. Supports `--apply`, `--restore`, `--ntp-only`, and timezone presets (EST, CST, MST, PST, etc.). |
| `parse_syscfg.py` | **Config parser.** Reads `SystemCfg.ini` from the camera and displays it in a readable, organized format. |
| `setup_sd_logging2.py` | **SD card logging setup.** Modifies `debug.sh` to redirect `superb` stdout/stderr to a log file on the SD card. Logs initially to `/tmp` (tmpfs), then moves to SD once mounted. |
| `monitor_reboot.py` | **Reboot monitor.** Long-running script that detects camera reboots and immediately reads SD card logs from the previous boot. Designed to run in a separate terminal. |
| `monitor_uptime.py` | **Uptime monitor.** Continuously polls `/proc/uptime` to detect reboots, logs timestamps. |
| `monitor_uptime.cmd` | Windows batch wrapper for `monitor_uptime.py`. |
| `crack_des.py` | **CPU DES cracker.** Brute-force DES hash cracker in Python (slow -- use hashcat instead for GPU acceleration). |
| `crack_password.cmd` | **Hashcat GPU cracker.** Runs hashcat against the root password DES/SHA-512 hashes from `/etc/passwd` and `/etc/shadow`. |
| `redump_partitions.py` | **Partition re-dump.** Re-dumps individual flash partitions using `xxd` hex encoding for improved reliability over base64. |

### tools/ble/ -- Bluetooth Provisioning

| Script | Purpose |
|--------|---------|
| `ble_provision.py` | **Main provisioning tool.** WiFi setup via BLE. Scans for `ipc_xmy-*` devices, sends `SSID:` and `PWD:` commands, polls `STATUS?` for result. Generic, CLI-driven. |
| `ble_scan.py` | Scan for XMeye cameras via BLE advertisement |
| `ble_enumerate.py` | Connect and enumerate all GATT services/characteristics |
| `ble_probe.py` | Read BLE characteristics and wait for notifications |
| `ble_fuzz.py` | Write test payloads to BLE characteristics (pre-protocol-discovery) |
| `ble_fuzz2.py` | Extended BLE fuzzing with JSON/binary probes |

### tools/onvif/ -- ONVIF Protocol

| Script | Purpose |
|--------|---------|
| `onvif_probe.py` | Query ONVIF device info, capabilities, stream URIs, snapshots |
| `onvif_set_time.py` | Set camera time/timezone via ONVIF (accepted but not applied -- see Known Issues) |

### tools/dvrip/ -- DVRIP Protocol

| Script | Purpose |
|--------|---------|
| `xm_test_login.py` | Basic DVRIP login test (port 34567) |
| `xm_dvrip_explore.py` | DVRIP protocol exploration -- scans all message IDs |
| `xm_dvrip_explore2.py` | Extended DVRIP scan with config name enumeration |
| `xm_dvrip_explore3.py` | Parse binary responses, time query/set, ConfigExport |
| `xm_backdoor_test.py` | Test known XMeye backdoors (telnet enable, HTTP paths, default passwords) |

### tools/uart/ -- Serial Console

| Script | Purpose |
|--------|---------|
| `uart_cmd.py` | Send a single command to camera via UART serial |
| `uart_check.py` | Run multiple diagnostic commands via UART |
| `uart_monitor_boot.py` | Capture full boot log via UART during power-cycle |
| `uart_write_backdoor.py` | Write `debug.sh` backdoor to configfs (heredoc method) |
| `uart_write_backdoor2.py` | Write `debug.sh` backdoor to configfs (echo method) |
| `logs/uart_boot_capture.txt` | Raw UART boot capture |
| `logs/uart_normal_boot.txt` | Normal boot sequence log |
| `logs/uart_rootshell_boot.txt` | Boot with root shell log |

### tools/ghidra/ -- Reverse Engineering

| Item | Purpose |
|------|---------|
| `analyze_superb.cmd` | Run Ghidra headless analysis on the `superb` binary |
| `download_and_setup.cmd` | Download and install Ghidra |
| `scripts/*.java` | Ghidra Java scripts: `DecompileTargeted`, `ExtractFunctions`, `ExtractISP`, `ExtractCloudHandlers`, `ExtractStringXrefs`, `FindIoWrite`, `ResolveStrings` |
| `scripts/*.py` | Ghidra Python scripts: `extract_functions`, `extract_isp_ioctls`, `extract_cloud_handlers`, `extract_strings_xrefs` |
| `output/` | Analysis output: function lists (all/cloud/ISP/sensor/ioctl/XUID), IO write analysis, ioctl constants, property dispatch tables, resolved strings, string xref analysis, targeted decompilation results |

### Key Tool Usage

**Run a command on the camera:**
```bash
python tools/cam_cmd.py "cat /proc/uptime"
python tools/cam_cmd.py "ls /etc/conf.d/"
```

**Control ISP image settings:**
```bash
python tools/isp_control.py read          # Read current CSC register values
python tools/isp_control.py set brightness 60
python tools/isp_control.py set saturation 50
python tools/isp_control.py reset         # Reset to defaults
```

**Fix timezone:**
```bash
python tools/fix_timezone.py --apply EST   # Eastern Standard Time
python tools/fix_timezone.py --apply PST   # Pacific Standard Time
python tools/fix_timezone.py --ntp-only    # Just change NTP to pool.ntp.org
python tools/fix_timezone.py --restore     # Restore original China settings
```

**WiFi provisioning via BLE:**
```bash
python tools/ble/ble_provision.py --scan
python tools/ble/ble_provision.py --ssid "YourWiFi" --password "YourPassword"
python tools/ble/ble_provision.py --ssid "YourWiFi" --password "YourPassword" --mac AA:BB:CC:DD:EE:FF
python tools/ble/ble_provision.py --status --mac 38:77:07:75:97:3A
```

**Dump firmware:**
```bash
python dump_firmware.py                        # Auto-detect camera
python dump_firmware.py --ip 192.168.1.153     # Specify IP
python dump_firmware.py --output my_backup     # Custom output dir
```

### Other Files

| File/Directory | Purpose |
|----------------|---------|
| `com.seculink.app_2.3.7.apk` | Secueye Android app (original APK for decompilation reference) |
| `apk_decompiled/` | jadx decompilation output (Java source from APK) |
| `firmware/` | Dumped flash partitions + extracted filesystems (see `firmware/ANALYSIS_SUMMARY.md`) |
| `CAMERA_CONTROLS.md` | Detailed reference for all camera control methods, settings, and protocols |
| `firmware/ANALYSIS_SUMMARY.md` | Human-readable firmware analysis findings |

---

## Future Work

### Completed

- ~~UART serial console~~ — **DONE.** Root shell via U-Boot interrupt + init=/bin/sh.
- ~~Firmware dump~~ — **DONE.** All 7 MTD partitions dumped over WiFi, MD5 verified.
- ~~Enable remote shell~~ — **DONE.** Persistent tcpsvd backdoor on port 9999.
- ~~Identify sensor model~~ — **DONE.** SmartSens SC635HAI (6.35MP).
- ~~Crack root password~~ — **DONE.** Rootfs password is `sl.x.` (cracked via
  hashcat DES mode 1500 on RTX 5090, ~20 seconds). Normal UART login now
  possible with `root` / `sl.x.` without U-Boot interrupt.
- ~~Reverse engineer `superb` binary~~ — **PARTIALLY DONE.** Ghidra analysis
  completed with custom scripts. Extracted function lists, ISP ioctl constants,
  cloud handler dispatch tables, and string cross-references. See
  `tools/ghidra/output/` for results.
- ~~Fix OSD timezone~~ — **DONE.** `tools/fix_timezone.py` modifies SystemCfg.ini
  timezone/NTP settings and restarts superb.
- ~~ISP register control~~ — **DONE.** `tools/isp_control.py` reads/writes ISP CSC
  registers (brightness, contrast, saturation, hue) directly via `bspmm`.
- ~~SD card testing~~ — **DONE.** Camera auto-records to SD card. Recording files
  are raw H.265 streams at `/progs/rec/00/YYYYMMDD/`.
- ~~binwalk analysis~~ — **DONE.** `analyze_firmware.py` extracts and analyzes all
  squashfs partitions. Full results in `firmware/ANALYSIS_SUMMARY.md`.

### High Value — Active Investigation

1. **Manually control `superb` image settings** — The ISP CSC register approach
   (`isp_control.py`) works for direct hardware control, but the ideal path is
   to find how to send `thing.service.property.set` commands to `superb`
   locally (without Alibaba cloud). This would control brightness, contrast,
   saturation, night vision mode, and all other cloud-only settings.

2. **Local MQTT broker** — Build a local MQTT broker that impersonates
   `public.iot-as-mqtt.cn-shanghai.aliyuncs.com`. Redirect camera DNS to it.
   Send `thing.service.property.set` JSON commands. Challenge: TLS certificate
   verification in `superb` (uses mbedtls) may need to be bypassed.

3. **Crack appfs root password** — Different hash from rootfs (`GIgEh3ZZNHRh2`,
   DES crypt, salt=`GI`). Exhausted 1-6 character keyspace. 7 characters
   requires ~2.4 hours, 8 characters ~9.5 days at 8 GH/s on RTX 5090.

4. **Patch superb for local DVRIP control** — The camera returns binary-only
   DVRIP responses. Patching superb to return JSON would make it compatible
   with standard VMS clients and python-dvr.

### Medium Value

5. **OpenIPC contribution** — Donate firmware dumps to the OpenIPC project
   for Hi3516CV610 support development. The kernel modules and ISP pipeline
   details in the dump would be valuable. OpenIPC does not yet support CV610.

6. **Cross-compile ISP tool** — Write a small ARM binary using HiSilicon MPP
   SDK headers (from OpenIPC or leaked SDKs) that calls `hi_mpi_isp_set_csc_attr`
   directly, bypassing the need for register-level access.

### Low Value

7. **Native library extraction** — Extract `liblinkvision.so` from the APK's
   `lib/` directory. Reverse-engineer the P2P protocol.

8. **Android BLE HCI snoop** — Protocol is already fully reverse-engineered
   from APK decompilation. Only useful for validation.

---

## References

### Open Source Projects

| Project | URL | Relevance |
|---------|-----|-----------|
| OpenIPC | https://openipc.org | Alternative firmware (CV610 not yet supported) |
| python-dvr | PyPI `python-dvr` | DVRIP client library (crashes on binary responses) |
| bleak | PyPI `bleak` | Python BLE library (used in all BLE scripts) |
| jadx | https://github.com/skylot/jadx | APK decompiler |
| pwn-hisilicon-dvr | https://github.com/tothi/pwn-hisilicon-dvr | XMeye firmware RE (older chips) |
| hisilicon-dvr-telnet | https://github.com/Snawoot/hisilicon-dvr-telnet | Telnet backdoor PoC |

### Xiongmai / XMeye Resources

| Resource | URL |
|----------|-----|
| cd-ipc.com | http://www.cd-ipc.com/xaizaien/ (download page) |
| iSpy camera DB | https://www.ispyconnect.com/camera/xmeye |
| Habr firmware analysis | https://habr.com/en/articles/486856/ |

### Default Credentials (Xiongmai ecosystem)

| Username | Password | Context |
|----------|----------|---------|
| admin | (blank) | Standard default |
| admin | admin | Common alternative |
| admin | I0TO5Wv9 | Universal backdoor (older firmware) |
| root | xmhdipc | Telnet/SSH (older firmware) |
| root | xc3511 | Telnet/SSH (older firmware) |
| root | klv123 | Telnet/SSH (older firmware) |

Note: This camera accepts ANY credentials on DVRIP. Authentication is
effectively disabled.
