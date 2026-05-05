# Firmware Analysis Summary

Human-readable summary of findings from `analysis_report.txt` (46,317 strings extracted from the `superb` binary and filesystem analysis of rootfs/appfs/resfs).

---

## 1. Architecture Overview

The camera runs a single monolithic binary (`superb`, 7.5MB ARM ELF) that handles everything: RTSP, ONVIF, DVRIP, cloud connectivity, recording, ISP control, AI detection, audio, PTZ, and OSD. Two small helpers exist: `mySystem` (watchdog) and `upgrade` (firmware updater).

The boot chain loads three read-only squashfs partitions (rootfs, appfs, resfs) and one writable jffs2 partition (configfs). All persistent settings live in configfs at `/etc/conf.d/`.

---

## 2. What the Camera Actually Is

**Not a traditional XMeye/DVRIP camera.** The firmware analysis confirms:

- **DVRIP is a minimal stub** -- only `NetStream.Record.Start`, `NetStream.Record.Stop`, and `getSystemInfo` are implemented. This explains why all config get/set commands return the generic 0x64 binary response instead of JSON.
- **Primary control is via Alibaba Cloud IoT (Link Vision)** -- the binary contains a full `linkvisual` C++ SDK with RTMP streaming, P2P NAT traversal, RPC sessions, cloud recording, AI event upload, OTA updates, and MQTT Thing Model property get/set.
- **Secondary cloud: Danale** -- another Chinese IoT cloud platform, used in parallel.
- **Tertiary cloud: TUTK** -- Kalay P2P platform for video streaming.
- **ONVIF is more complete** than DVRIP -- full Profile S + Profile T implementation with device, media, media2, events, imaging, PTZ, and deviceIO services. But it operates as an independent shim that doesn't bridge to actual firmware settings.

---

## 3. Cloud Connectivity (What Phones Home)

### Alibaba IoT (Primary)
- MQTT broker: `public.iot-as-mqtt.cn-shanghai.aliyuncs.com` (ports 1883/443)
- Auth: HMAC-SHA1 with device-specific tokens from `lic.bin`
- SDK: `sdk-c-2.3.0_FY_1.6.6-6` (custom Seculink fork)
- Uses TLS with embedded CA certificates (mbedtls)
- Thing Model: property get/set, event posting, service invocation

### Danale
- Credentials in `/etc/conf.d/syscfg/danale_private.cfg`
- Separate cloud platform, likely for Chinese market users

### TUTK (Kalay)
- P2P video streaming platform
- Config at `/etc/conf.d/syscfg/tutk.cfg`

### SIM/4G Management URLs
| URL | Purpose |
|-----|---------|
| `traffic.secueye.app/?iccidx=` | SIM data usage query |
| `traffic.seculink.com.cn/?iccid=` | SIM data (alternate) |
| `craiot.seculink.com.cn` | SIM card management UI |
| `wx.88iot.net/query?iccid=` | IoT SIM lookup |
| `sim.eiotclub.com/2/` | eIotClub SIM platform |
| `admin.yywso.com` | SIM card details |
| `wx.linksfield.net` | Partner portal |

### OTA Update
- Bootstrap: `https://<server>/auth/bootstrap`
- Registration: `https://<server>/auth/register/device`
- Firmware check: `/ota/device/inform/<pk>/<dn>`
- Download: `/ota/device/request`, `/ota/device/upgrade`
- Legacy: `http://<ip>:<port>/FirmwareUpgrade`, `http://<ip>:<port>/SystemRestore`

---

## 4. AI / Intelligence Capabilities

### Human/Vehicle Detection (IVP)
- Model file: `det_hv_hor.bin` (872KB) in resfs
- Loaded when `IVP=1` in `/home/variable` (currently enabled)
- Controls: `IVPEnable`, `IVPFunction`, `IVPSensitivity` (0-5), `IVPExSwitch`, `IVPIntelligentTracking`
- Detects humans and vehicles in the video stream
- Can trigger alarms, recording, PTZ tracking

### Face Recognition (Chinese firmware only)
- Voice prompts exist: `face_entry_start`, `face_entry_successful`, `face_entry_failed`, `face_deletion_successful`, `face_entry_multiple_faces`, `face_entry_timeout`, etc.
- Model: `det_hvf_normal.bin` referenced in strings but NOT present in resfs (this variant doesn't ship face detection model)
- Data path: `/etc/conf.d/syscfg/face/*`

### AI-ISP (Neural Noise Reduction)
- 5 AIBNR models in resfs for different resolutions:
  - `aibnr_model_2688x1520.bin` (standard)
  - `aibnr_model_2688x1520_high_iso.bin` (high ISO noise reduction)
  - `aibnr_model_2688x1520_pro.bin` (pro mode)
  - `aibnr_model_3200x1800.bin` (native sensor resolution)
  - `aibnr_model_3200x1800_high_iso.bin`
- Currently disabled: `AIISP=0` in variable file
- Toggle: set `AIISP=1` in variable file (requires reflashing appfs or runtime hack)

### NPU
- Kernel module: `ot_svp_npu.ko` (110KB) loaded
- Currently disabled at app level: `NPU=0` in variable file
- The IVP detection runs on NPU hardware regardless of the NPU variable

---

## 5. Controllable Settings (SystemCfg.ini)

The `superb` binary reads ~978 config keys from `/etc/conf.d/syscfg/SystemCfg.ini`. The file is semicolon-delimited with `[section]` headers. Here are the most useful categories:

### Time & Locale
| Key | Current | Notes |
|-----|---------|-------|
| `timezone` | 800 | Offset in 1/100 hours. 800 = UTC+8 (China). Use -500 for EST, -600 for CST, -700 for MST, -800 for PST |
| `posixTZ` | CST-8 | POSIX timezone string |
| `regionTZ` | Asia/Shanghai | Olson timezone |
| `ntpServer` | ntp.fudan.edu.cn | Change to `pool.ntp.org` or your preferred NTP |
| `nLanguage` | 1 | 0=English(?), 1=Chinese(?) -- need to test |
| `nTimeFormat` | 0 | Time format on OSD |

### Image Quality (ISP)
| Key | Current | Range (typical) | Notes |
|-----|---------|-----------------|-------|
| `brightness` | 161 | 0-255 | Image brightness |
| `contrast` | 169 | 0-255 | Image contrast |
| `saturation` | 164 | 0-255 | Color saturation |
| `sharpness` | 178 | 0-255 | Edge sharpness |
| `exposure` | 128 | 0-255 | Exposure level |
| `antifog` | 0 | 0-1 | Anti-fog enhancement |
| `bEnableWdr` | 1 | 0-1 | Wide dynamic range |
| `wdrStrength` | 256 | 0-? | WDR strength |
| `bDRCEnable` | 0 | 0-1 | Dynamic range compression |
| `whiteBalanceMode` | 0 | 0=auto | White balance mode |
| `FlickerFrequency` | 50 | 50/60 | Anti-flicker: 50Hz (EU/Asia) or 60Hz (US) |
| `maxShutterSpeed` | 1000000 | microseconds | Maximum shutter speed |

### Day/Night Vision
| Key | Current | Notes |
|-----|---------|-------|
| `daynightMode` | 0 | Day/night switching mode (0=auto) |
| `nightVisionMode` | 0 | 0=auto, 1=color, 2=B&W |
| `powermode` | 1 | Light control mode |
| `dayStart` | 06:00 | Day mode start time |
| `dayEnd` | 18:00 | Day mode end time |
| `sw_cut` | 0 | IR cut filter control |
| `cut_rate` | 30 | IR cut switch threshold |
| `cutDelay` | 2 | Seconds delay before switching |
| `IRLBrightness` | 5 | IR LED brightness (0-10?) |
| `WLBrightness` | 5 | White LED brightness (0-10?) |
| `AlarmLightSwitch` | 0 | Alarm strobe enable |

### Video Encoding (Main Stream - CH1)
| Key | Current | Notes |
|-----|---------|-------|
| `width` | 3840 | Horizontal resolution |
| `height` | 2160 | Vertical resolution (4K) |
| `framerate` | 15 | Frames per second |
| `bitrate` | 4096 | Kbps |
| `vencType` | 1 | 0=H.264, 1=H.265 |
| `rcMode` | 3 | Rate control: 0=CBR, 1=VBR, 2=AVBR, 3=QVBR(?) |
| `quality` | 4 | Quality preset (1-6?) |
| `nGOP` | 80 | GOP length (keyframe interval in frames) |
| `minQP` / `maxQP` | 35/44 | QP range for rate control |

### Video Encoding (Sub Stream - CH2)
| Key | Current | Notes |
|-----|---------|-------|
| `width` | 720 | |
| `height` | 576 | PAL SD resolution |
| `framerate` | 15 | |
| `bitrate` | 1024 | Kbps |
| `vencType` | 1 | H.265 |

### Motion Detection
| Key | Current | Notes |
|-----|---------|-------|
| `bMDEnable` | 0 | Motion detection master switch |
| `nMDSensitivity` | 30720 | Sensitivity (higher = more sensitive?) |
| `nMDFunction` | 0 | Detection function mode |
| `nMDDelay` | 10 | Seconds to hold alarm after last motion |
| `nMDpushSwitch` | 1 | Push notification enable |
| `nMDTracking` | 0 | Motion tracking (PTZ follow) |
| `nMDRegion` | all -1 | Detection region grid (32 zones, -1=all) |
| `detect_bEnRecord` | 1 | Record on detection |

### AI Detection (IVP)
| Key | Current | Notes |
|-----|---------|-------|
| `IVPEnable` | 1 | Human/vehicle detection master switch |
| `IVPFunction` | 0 | Detection function type |
| `IVPSensitivity` | 3 | Sensitivity (0-5?) |
| `IVPExSwitch` | 0 | Extended detection features |
| `IVPIntelligentTracking` | 0 | Auto-tracking follow target |

### Tripwire & Region Detection
| Key | Current | Notes |
|-----|---------|-------|
| `CrossLineEnable` | 0 | Tripwire detection |
| `CrossLineStartX/Y` | 300,0 | Line start point |
| `CrossLineEndX/Y` | 320,360 | Line end point |
| `CrossLineDirection` | 0 | 0=both, 1=left-to-right, 2=right-to-left |
| `RegionDetectEnable` | 0 | Region intrusion detection |
| `Region*TopX/Y` | corners | Detection zone quadrilateral |

### OSD (On-Screen Display)
| Key | Current | Notes |
|-----|---------|-------|
| `bShowOSD` | 1 | Show OSD overlay |
| `channelName` | H.265 IPC | Text shown on OSD |
| `timeFormat` | MM/DD/YYYY hh:mm:ss | Timestamp format |
| `nPosition` | 3 | OSD position (0-3 = corners) |
| `osdText0-7` | (empty) | Custom OSD text lines |
| `osdSize` | 32 | Font size |

### Recording
| Key | Current (CH1) | Notes |
|-----|---------------|-------|
| `bRecEnable` | 0 | Recording master switch (CH1 off, CH2-5 on) |
| `RecFileLength` | 600 | Seconds per recording file |
| `bRecAlarmEnable` | 1 | Record on alarm |
| `bRecTimingEnable` | 0 | Scheduled recording |
| `storageMode` | 2 (CH1), 1 (others) | Storage mode |
| `recordMode` | 2 | Record mode |

### Alarm / Notifications
| Key | Current | Notes |
|-----|---------|-------|
| `armingMainEnable` | 1 | Master arming switch |
| `armingEnable` | 1 | Arming active |
| `armingAlarmMode` | 1 | Alarm mode |
| `armingSensitivity` | 30720 | Arming sensitivity |
| `doublelight_bEnable` | 1 | Dual-light alarm |
| `doublelight_bOutVoice` | 1 | Voice alarm |
| `doublelight_bLightOn` | 1 | Light on alarm |

### PTZ
| Key | Current | Notes |
|-----|---------|-------|
| `moveSpeed` | 255 | Pan/tilt speed (max) |
| `scanSpeed` | 55 | Auto-scan speed |
| `bLRSW` / `bUDSW` | 1/0 | Left-right / up-down swap |
| `guardTime` | 0 | Auto-return to guard position (0=disabled) |

### Email (SMTP)
| Key | Current | Notes |
|-----|---------|-------|
| `acMailSender` | ipcmail@163.com | From address (Chinese default) |
| `acSMTPServer` | smtp.163.com | SMTP server |
| `acSMTPUser` | ipcmail | SMTP username |
| `acSMTPPasswd` | ipcam71a | SMTP password (plaintext!) |
| `acReceiver0-3` | (null) | Recipient addresses |

### Network
| Key | Current | Notes |
|-----|---------|-------|
| `bSntp` | 1 | NTP sync enabled |
| `bIPSelfAdapt` | 0 | Auto-IP (Zeroconf) |
| `bEnablePrivateProto` | 0 | Private protocol (Danale?) |
| `bEnable4G` | 0 | 4G modem support |
| `enableStreamWatchDog` | 1 | Stream watchdog restart |

---

## 6. Hardware Config (hwconfig.cfg)

This file on configfs overrides hardware capabilities. Changes here affect how `superb` initializes hardware.

| Key | Current | Notes |
|-----|---------|-------|
| `double_light` | 1 | Has both IR and white LEDs |
| `PTZ` | 1 | PTZ motor enabled |
| `rotate` | 0 | Image rotation |
| `auto_focus` | 0 | No auto-focus hardware |
| `lang` | en | Voice prompt language (en/cn/ru/vn/br/rn/ln) |
| `nightVision_mode` | 0 | Default night vision behavior |
| `nightVisionShowCtrl` | 5 | Which night vision modes are exposed in UI |
| `ir_light` | 0 | IR light type |
| `alarm_light` | 0 | Alarm light type |
| `floodlight` | 0 | Floodlight type |
| `record_quality` | 3 | Default recording quality |
| `tracking_type` | 0 | Tracking algorithm type |
| `MOTOR_DRV_FUNC` | 5 | Motor driver function |
| `MAXLRSTEP` | 2100 | Maximum left-right steps |
| `MAXUDSTEP` | 650 | Maximum up-down steps |
| `UDSPEED` / `LRSPEED` | 40.0/50.0 | Motor speeds |
| `LR_REDUCTION` / `UD_REDUCTION` | 64/64 | Motor gear reduction |
| `cutcheck` | 5_7 | IR-cut check parameters |
| `cutcheck_en` | 1 | IR-cut check enabled |

---

## 7. Hardware DNA (`/home/variable`)

This file in appfs defines the hardware platform. The `superb` binary reads it to decide which drivers and features to enable.

| Key | Value | Notes |
|-----|-------|-------|
| `chip` | hi3516cv610_20s | 128MB RAM variant |
| `SENSOR` | sc635hai | SmartSens 6.35MP |
| `WIFI` | atbm6x3x_atbm6x6x | AltoBeam WiFi 6 (USB) |
| `PART` | 16 | 16MB flash layout |
| `IVP` | 1 | AI human/vehicle detection: **enabled** |
| `AIISP` | 0 | AI noise reduction: **disabled** |
| `NPU` | 0 | NPU standalone: **disabled** |
| `USB` | 0 | USB host features: **disabled** |
| `NET4G` | 0 | 4G modem: **disabled** |
| `ipc_type` | 101 | Camera type identifier |
| `BOARD_TYPE` | AIO | All-in-one board |
| `language` | cn | Default language (overridden by hwconfig.cfg `lang=en`) |
| `gop_mode` | 1 | GOP mode |
| `EZOOM_SUPPORT` | 1 | Electronic zoom supported |
| `OnlineInterpolationType` | 81 | Resolution interpolation algorithm |

---

## 8. Embedded Crypto & Security

- **TLS library**: mbedtls (full implementation with AES-128/192/256, RSA, ECDHE, SHA-256/384/512, ChaCha20-Poly1305)
- **Ciphers supported**: ECDHE-RSA-AES128-GCM-SHA256, ECDHE-ECDSA-AES256-GCM-SHA384, PSK-AES128, and more
- **NTRU encryption**: Post-quantum crypto references present (likely part of Link Vision SDK)
- **HiSilicon hardware crypto**: `ot_cipher.ko` (162KB), `ot_km.ko` (key management), `ot_otp.ko` (one-time programmable)
- **Cloud auth**: HMAC-SHA1 token-based authentication for Alibaba IoT
- **Video encryption**: `EncryptSwitch` property exists -- streams can be AES encrypted
- **RSA private key**: embedded PEM format key present in binary (for TLS client auth)
- **BLE security**: NimBLE stack with bonding, encryption, and authentication support

---

## 9. WiFi AP Mode

The binary has full WiFi AP (Access Point) support:

- `hostapd` configuration template embedded
- `wlan1` interface at `10.10.0.1` subnet for AP mode
- `udhcpd.conf` for DHCP server in AP mode
- Used during initial provisioning (before WiFi is configured)
- References: `sp_ifconfig_wifi_start_ap`, `start_ap_mode.711` voice prompt

---

## 10. SD Card & Storage

- Partition auto-detection: `/dev/mmcblk0`, `/dev/mmcblk0p1`
- FAT32 support (vfat mount)
- Recording to SD: 5 virtual channels (CH1-CH5)
- Factory provisioning via SD card directories (see INVESTIGATION.md)
- Storage modes: continuous, alarm-triggered, scheduled
- File management: create/delete/query via cloud or DVRIP

---

## 11. Audio Pipeline

Full two-way audio:

- **Codecs**: G.711A (A-law), G.711U (mu-law), G.726, AAC, ADPCM
- **VQE (Voice Quality Enhancement)**: AEC (echo cancellation), AGC (gain control), ANR (noise reduction), HPF (high-pass filter), EQ (equalizer)
- **Audio in RTSP**: embedded in both live1 and live2 streams
- **Two-way talk**: `libvqe_talkv2.so` (301KB) for intercom functionality
- **Voice prompts**: .711 files in 7 languages (en, cn, ru, vn, br, rn, ln)

---

## 12. PWM / GPIO Control

The binary directly controls hardware via sysfs:

- **PWM chip 3**: `echo N > /sys/class/pwm/pwmchip3/pwmN/duty_cycle` -- controls IR LEDs, white LEDs, or motor
- **GPIO**: `hi_gpio.ko` module for general purpose I/O
- **Motor**: `motor_advance.ko` for PTZ stepper motor control
- **IR cut filter**: hardware-controlled day/night switch
- **Door alarm**: `dooralarm.ko` module exists for door sensor input
- **ADC**: `ot_adc.ko` for analog sensing (light level sensor for auto day/night)

---

## 13. ONVIF Implementation Detail

The ONVIF server is extensive (1,285 unique SOAP strings):

### Fully Implemented Services
- Device management (GetDeviceInformation, GetCapabilities, GetScopes, DNS, NTP, hostname, network interfaces, relay outputs, users, remote user, zero config, dot11)
- Media (profiles, video/audio source configs, encoder configs, OSD, metadata, streaming URI)
- Media2 (tr2 namespace -- newer ONVIF media service)
- Events (pull-point subscription, event properties, topic filtering)
- Imaging (settings, options, presets, move/focus, status)
- PTZ (nodes, configs, presets, preset tours, continuous/relative/absolute move, auxiliary commands)
- DeviceIO (video/audio outputs, relay outputs, serial ports, digital inputs)

### Embedded Default Config
An XML config block reveals default ONVIF settings:
- Video source: 2560x1920 (sensor native)
- Main encoder: 2560x1920 H.264 Main, 20fps, 4096kbps, GOP=80
- Sub encoder: 720x576 H.264 Main, 20fps, 1024kbps
- Audio: G.711, 8kHz, 16kbps
- `need_auth=0` (authentication disabled)
- `http_max_users=4`

---

## 14. What Can Be Changed Without Reflashing

Everything in `/etc/conf.d/` (configfs, jffs2) is writable and survives reboot:

1. **SystemCfg.ini** -- all the settings listed in section 5 above
2. **hwconfig.cfg** -- hardware capabilities
3. **Network config** -- `wpa_supplicant.conf`, `interface.cfg`, `mac.cfg`
4. **Cloud identities** -- `aliyun.conf`, `danale.conf`, `lic.bin`
5. **debug.sh** -- our backdoor
6. **ONVIF users** -- `onvif_user.json`
7. **Account data** -- `account.dat`
8. **Voice prompts** -- custom .711 files in `fixed/custom_voice/`

Changes to SystemCfg.ini require a reboot (or `superb` restart) to take effect, since `superb` reads the file at startup.

---

## 15. What Cannot Be Changed Without Reflashing

- `/home/variable` -- hardware platform definition (in appfs squashfs, read-only)
- `superb` binary behavior -- DVRIP binary responses, cloud endpoints
- Kernel modules -- sensor/WiFi/media drivers
- Voice prompt base set -- in resfs squashfs
- BusyBox configuration -- telnet/nc/wget remain stripped

---

## 16. Key Unknowns (Need Ghidra)

1. **How does `superb` parse SystemCfg.ini?** -- Does it re-read on SIGHUP, or only at startup?
2. **DVRIP binary response format** -- What struct is being serialized? Can it be patched to return JSON?
3. **How are ONVIF imaging changes bridged (or not) to ISP?** -- The ONVIF shim code path
4. **Cloud disable** -- Can cloud modules be disabled via config, or do they need to be patched out?
5. **nightVisionMode values** -- Exact mapping of mode numbers to behavior
6. **timezone format** -- Is 800 really hundredths of hours, or tenths, or minutes?
7. **AIISP enable** -- Can `AIISP=1` be set at runtime somehow, or does it require appfs modification?
