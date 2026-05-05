# SECUEYE X5 Camera Controls Reference

Camera: SECUEYE X5 Smart Window Camera (Hi3516CV610 + SC635HAI)
Firmware: MZ0201V160_EN_20251126
Main binary: `superb` (7.8MB, statically linked, replaces Xiongmai "Sofia")

> **See also:** [INVESTIGATION.md](INVESTIGATION.md) for full device investigation,
> [firmware/ANALYSIS_SUMMARY.md](firmware/ANALYSIS_SUMMARY.md) for firmware analysis.
>
> **Active tools for camera control:**
> - `tools/cam_cmd.py` -- Run any shell command on the camera
> - `tools/parse_syscfg.py` -- Read and display SystemCfg.ini
> - `tools/fix_timezone.py` -- Fix OSD timezone from China to local
> - `tools/test_settings.py` -- Interactive SystemCfg.ini key tester (edit, reboot, verify)
> - `tools/monitor_alerts.py` -- Real-time alarm event monitor (superb.log based)
> - `tools/isp_control.py` -- Direct ISP register control (no visible effect, see notes)

## Architecture

All camera functions are handled by the `superb` binary. It exposes multiple
protocol interfaces, but **real control flows only through the Alibaba IoT
cloud protocol**. ONVIF and DVRIP are shallow facades that store values
internally but do not bridge to the ISP/recording/detection subsystems.

The ISP (Image Signal Processor) is controlled via HiSilicon MPP API calls
(`hi_mpi_isp_set_csc_attr`, `ot_mpi_isp_set_saturation_attr`, etc.) through
ioctls to `/dev/isp_dev`. Only `superb` makes these calls, triggered by
cloud commands received via Alibaba IoT MQTT.

```
Cloud App (Danale/Secueye)
    |
    v
Alibaba IoT MQTT (TLS:443) --> superb --> hi_mpi_isp_set_*() --> /dev/isp_dev
    |                            |
    v                            v
thing.service.property.set   SystemCfg.ini (some settings persisted here)
```

---

## Control Methods -- What Works, What Doesn't

### Summary Table

| Method | Can Read Settings | Can Change Settings | Can Stream | Notes |
|--------|:-:|:-:|:-:|-------|
| **RTSP** | - | - | YES | Only reliable streaming method |
| **HTTP Snapshot** | - | - | YES | JPEG snapshots work |
| **ONVIF** | YES | Writes accepted, **NOT applied** | Unreliable | Facade only |
| **DVRIP (port 34567)** | Binary responses | Record Start/Stop only | Broken | 3-command stub |
| **Alibaba IoT MQTT** | All properties | All properties | P2P/RTMP | **Only real control path** |
| **SystemCfg.ini + reboot** | - | Some settings work | - | See tested results below |
| **Root shell (port 9999)** | Full filesystem | Config files, processes | - | Requires backdoor |

### Tested Results (Verified 2026-05-04)

**SystemCfg.ini settings that WORK (edit + full reboot):**

| Key | Description | Verified Effect |
|-----|-------------|-----------------|
| `bShowOSD` | OSD visibility (1=show, 0=hide) | OSD overlay disappeared/appeared |
| `doublelight_bOutVoice` | Voice alarm (1=on, 0=off) | Voice announcement silenced on alarm |
| `timezone` / `posixTZ` / `regionTZ` | Timezone | OSD timestamp corrected |
| `ntpServer` | NTP server | Time sync works from new server |
| `hours_fmt` | 12/24h format (requires full reboot) | OSD time format changed |

**SystemCfg.ini settings that DO NOT WORK:**

| Key | Description | Result |
|-----|-------------|--------|
| `channelName` | OSD text | Value persists, OSD unchanged |
| `nightVisionMode` | Night mode (0=auto, 1=color, 2=B&W) | No effect (also broken in app) |
| `IVPEnable` | AI detection on/off | No effect (green box still appears) |
| `RegionDetectEnable` | Region intrusion on/off | No effect |
| `brightness` / `contrast` / `saturation` / `sharpness` | ISP image quality | No effect |

**ONVIF settings that DO NOT WORK:**

| Method | Result |
|--------|--------|
| SetImagingSettings brightness=10 | Stored internally, **not applied** to ISP |
| SetImagingSettings IrCutFilter=OFF | Stored internally, **not applied** |
| SetImagingSettings EFlip | Stored internally, **not applied** (never calls sensor flip) |
| ONVIF PullPoint events (MotionAlarm) | Subscription works, **events never fire** |

**Other verified behaviors:**

| Method | Result |
|--------|--------|
| Killing superb (SIGHUP) | mySystem restarts it. Config re-read on restart. |
| ISP CSC registers via `bspmm` | Registers read/write, **no visible effect** on stream |

**Pattern**: Simple output toggle flags (`bShowOSD`, `doublelight_bOutVoice`)
work via SystemCfg.ini. Core processing pipeline settings (ISP, AI detection,
OSD content, night vision) are controlled exclusively through Alibaba IoT MQTT
cloud commands and ignore config file values at startup.

**Conclusion**: The only reliable local control path for most settings is the
Alibaba IoT MQTT protocol. ONVIF and DVRIP are facade implementations. The
camera is architecturally cloud-dependent by design.

---

## 1. Streaming

### RTSP (Working)

| Stream | URL | Resolution | Codec | FPS | Bitrate |
|--------|-----|-----------|-------|-----|---------|
| Main | `rtsp://192.168.1.153/live1` | 3840x2160 | H.265 | 15 | 4096 kbps |
| Sub | `rtsp://192.168.1.153/live2` | 720x576 | H.265 | 15 | 1024 kbps |

- No authentication required
- Audio included (G.711)
- Works with VLC, ffplay, any standard RTSP client

### HTTP Snapshots (Working)

| Endpoint | Result |
|----------|--------|
| `http://192.168.1.153:80/snapshot/MainStream` | ~34KB JPEG |
| `http://192.168.1.153:80/snapshot/SubStream` | ~34KB JPEG |

- No authentication required

### ONVIF Media (Partially Working)

- Profile tokens: `MainStream`, `SubStream`
- GetStreamUri: returns correct RTSP URLs
- GetSnapshotUri: returns correct HTTP URLs
- GetVideoEncoderConfigurations: works
- GetVideoSources: works, source token is `V_SRC_1`
- **Auth is inverted**: adding any WS-Security header causes faults; no auth = works
- Media2 (Profile T): faults despite advertising Profile T in scopes
- GetServices: faults

### DVRIP (Broken)

- Port 34567, binary protocol
- Login returns binary-only response (no JSON)
- All DVR GUI applications fail to connect properly
- Only implemented commands: `NetStream.Record.Start` (msg 1100), `NetStream.Record.Stop`, `getSystemInfo`

### Encoding Config (SystemCfg.ini -- Untested)

These may work after reboot since they configure the encoder, not the ISP:

```ini
# CH1 - Main stream
width=3840; height=2160; framerate=15; bitrate=4096
vencType=1        # 0=H.264, 1=H.265
rcMode=3          # Rate control mode
nGOP=80           # GOP size
quality=4         # Encoding quality
bAudioEn=1        # Audio in stream

# CH2 - Sub stream  
width=720; height=576; framerate=15; bitrate=1024
vencType=1; quality=5
```

Supported resolutions (from config):
3840x2160, 3200x1800, 2960x1664, 2592x1944, 2560x1920, 2880x1620,
2688x1520, 2560x1440, 2304x1296, 1920x1080, 1280x960, 1280x720,
960x540, 768x432, 720x576, 720x480, 704x576, 640x480, 640x384,
640x360, 512x288, 352x288, 368x208

---

## 2. Image Quality (Brightness, Contrast, Saturation)

### Current State

The ISP pipeline auto-tunes image quality using:
- Auto Exposure (AE) with gain/shutter control
- Auto White Balance (AWB)
- WDR (Wide Dynamic Range) -- enabled, strength=256
- DRC (Dynamic Range Compression) -- enabled, strength=256
- Dehaze -- enabled, strength=32
- 3DNR (3D Noise Reduction) -- enabled
- Sharpening -- enabled with per-ISO curves

These are set by `superb` at startup using PQ calibration files:
- `/tmp/resfs/sensor/sc635hai/pqbin/day.bin` (141KB)
- `/tmp/resfs/sensor/sc635hai/pqbin/night.bin` (141KB)
- `/tmp/resfs/sensor/sc635hai/pqbin/light.bin` (141KB)
- `/tmp/resfs/sensor/sc635hai/pqbin/black.bin` (141KB)

### How to Actually Change Image Settings

**Only confirmed working method**: Alibaba IoT cloud commands via MQTT.

The `superb` binary contains these ISP control functions:
```
secu_sensor_brightness          # Calls hi_mpi_isp_set_csc_attr
secu_sensor_contrast            # Calls hi_mpi_isp_set_csc_attr
secu_sensor_saturation          # Calls hi_mpi_isp_set_csc_attr  
secu_sensor_saturate_set        # Saturation setter
secu_sensor_sharpness           # Calls hi_mpi_isp_set_sharpen_attr
secu_sensor_chroma              # Chroma/hue control
secu_sensor_set_security_image_effect  # Master image effect function
```

These are called when the cloud sends a `thing.service.property.set` command.
The CSC (Color Space Conversion) module range is 0-100 for saturation
(`Csc Isp Saturate Range [0-100] !!!` error message in binary).

### Potential Local Control Approaches

1. **Local MQTT broker** -- Run mosquitto with TLS on your PC, add
   `/etc/hosts` entry on camera pointing `public.iot-as-mqtt.cn-shanghai.aliyuncs.com`
   to your PC IP. Send `thing.service.property.set` JSON commands.
   Challenge: TLS certificate verification may need to be bypassed.

2. **Cross-compile ISP tool** -- Write a small ARM binary that opens
   `/dev/isp_dev` and calls `hi_mpi_isp_set_csc_attr` directly.
   Needs HiSilicon MPP SDK headers (from OpenIPC or leaked SDKs).

3. **Ghidra reverse engineering** -- Decompile `superb` to find exact
   ioctl command numbers for ISP CSC operations, then craft raw ioctls
   using `dd` to `/dev/isp_dev`.

4. **Temporarily allow cloud access** -- Unblock the camera's internet
   briefly, use the Danale/Secueye app to change settings, then re-block.
   Settings should persist across reboots once set via cloud.

---

## 3. Night Vision / IR Control

### Hardware

- Dual-light system: IR LEDs + white flood LEDs
- IR-cut filter (mechanical, electrically actuated)
- ADC-based ambient light sensor for auto-switching
- PWM control for LED brightness

### Config Keys (SystemCfg.ini)

```ini
daynightMode=0          # 0=auto
nightVisionMode=0       # 0=auto, 1=color, 2=B&W
last_nightVisionMode=2  # Last known mode (2=B&W)
powermode=1             # Light control mode
dayStart=06:00          # Day mode start
dayEnd=18:00            # Day mode end
sw_cut=0                # IR-cut filter state
cut_rate=30             # Light threshold for switching
cutDelay=2              # Seconds delay before switch
IRLBrightness=5         # IR LED brightness (0-10)
WLBrightness=5          # White LED brightness (0-10)
AlarmLightSwitch=0      # Alarm strobe
doublelight_bEnable=1   # Dual-light mode
doublelight_bLightOn=1  # Light enabled
```

### Cloud Properties

| Property | Description |
|----------|-------------|
| `NightVisionMode` | Master night vision mode |
| `IRLightBrightness` | IR LED brightness |
| `WhiteLightBrightness` | White LED brightness |
| `FloodlightSwitch` | White light on/off |
| `FloodlightSchedule` | White light schedule |
| `AlarmLightSwitch` | Alarm strobe enable |
| `StatusLightSwitch` | Status LED on/off |

### Internal Functions

```
secu_sensor_set_nightmode       # Sets night mode
secu_sensor_set_daynight_mode   # Day/night switching
secu_sensor_set_ircut           # IR-cut filter control
secu_sensor_b_night             # Check if night
secu_sensor_b_night_auto        # Auto night detection
secu_sensor_b_night_use_ae      # AE-based night detection
secu_sensor_Adc_CheckNight_v2   # ADC light sensor check
secu_sensor_light               # LED control
HI_XUID_SET_NIGHTVISION_MODE   # Internal IPC command
```

### PWM LED Control (Direct Hardware)

```sh
# IR LEDs - pwmchip3
echo <duty_cycle> > /sys/class/pwm/pwmchip3/pwm0/duty_cycle
echo 1 > /sys/class/pwm/pwmchip3/pwm0/enable

# White LEDs - similar path
```

---

## 4. Motion Detection / AI Detection

### Motion Detection (Basic)

Disabled by default. Config keys:

```ini
bMDEnable=0             # Master switch (OFF)
nMDSensitivity=30720    # Sensitivity
nMDFunction=0           # Function mode
nMDDelay=10             # Alarm hold time (seconds)
nMDpushSwitch=1         # Push notification
nMDTracking=0           # PTZ tracking
nMDRegion=-1,-1,...     # Detection grid (32 zones, -1=all)
detect_bEnRecord=1      # Record on detect
nMDOutClient=0          # Notify client
nMDOutEMail=0           # Email notification
nMDOutVMS=0             # VMS notification
nMDOutVoice=0           # Voice alert
```

### AI Human/Vehicle Detection (IVP)

Enabled by default. Uses neural network model.

```ini
IVPEnable=1             # ENABLED
IVPFunction=0           # Detection type
IVPSensitivity=3        # Sensitivity (0-5)
IVPExSwitch=0           # Extended features
IVPIntelligentTracking=0  # PTZ auto-tracking
```

Model: `/tmp/resfs/ivp/det_hv_hor.bin` (872KB) -- human + vehicle detection

### Tripwire Detection

```ini
CrossLineEnable=0       # OFF
CrossLineStartX=300; CrossLineStartY=0
CrossLineEndX=320; CrossLineEndY=360
CrossLineDirection=0    # 0=both, 1=L-R, 2=R-L
```

### Region Intrusion Detection

```ini
RegionDetectEnable=0    # OFF
RegionLeftTopX=0; RegionLeftTopY=0
RegionRightTopX=640; RegionRightTopY=0
RegionLeftBottomX=0; RegionLeftBottomY=360
RegionRightBottomX=640; RegionRightBottomY=360
```

### Arming Schedule

```ini
armingMainEnable=1      # Master arming switch
armingEnable=1          # Arming enabled
armingAlarmMode=1       # Alarm mode
armingValid0=1          # Schedule 0 valid
armingSwitch0=1         # Schedule 0 enabled
armingWeekMask0=127     # All days (bitmask, 7 bits)
armingStime0=0          # Start: 00:00:00
armingEtime0=86399      # End: 23:59:59
```

### ONVIF Events — NON-FUNCTIONAL

Available event topics (advertised but **never fire**):
- `tns1:VideoSource/MotionAlarm` -- tested, no events delivered on IVP trigger
- `tns1:VideoSource/ImageTooBlurry`
- `tns1:VideoSource/ImageTooDark`
- `tns1:VideoSource/ImageTooBright`
- `tns1:VideoSource/GlobalSceneChange`
- `tns1:VideoSource/SignalLoss`
- `tns1:Device/ProfileChanged`

PullPoint subscription succeeds (`CreatePullPointSubscription` returns a
reference, `PullMessages` returns valid XML), but **no alarm events are
ever delivered** when detection triggers. The ONVIF event layer is not
wired to the IVP detection pipeline. Tested 2026-05-05.

Cannot configure detection parameters via ONVIF.

### Local Alarm Detection via superb.log — WORKS

`tools/monitor_alerts.py` monitors `/tmp/superb.log` (superb's stdout, redirected
by the `debug.sh` backdoor) for alarm patterns in real-time.

**Confirmed alarm indicators:**

| Log Pattern | Event Type | Latency |
|-------------|------------|---------|
| `start maudio_speaker` | Voice alarm prompt fired | ~0ms (instant) |
| `Create snap` | Alarm snapshot captured | ~0ms |
| `goto preset NNN` | PTZ preset triggered (100=alarm, 103=tracking) | ~0ms |
| `mivp_set_param` burst | IVP reconfiguration post-alarm | ~30s |

**Recording:** M-prefix files (`M{HHMMSS}.H265`) are created **instantly** at
alarm time and grow as recording continues. N-prefix = normal, M-prefix = alarm.

**NPU inference:** The `det_hv_hor.bin` model runs at ~17fps on the SVP NPU.
Output shape: 3x(Nx6) = 5040 candidate detections per frame. Results are not
accessible from the shell -- they flow via `/dev/svp_npu` ioctl to superb's
userspace memory only. Status readable at `/proc/umap/svp_npu`.

**Limitation:** The camera has no `wget`, `curl`, `nc`, or any outbound HTTP
capability from the shell. To send webhook notifications from the camera, a
cross-compiled static ARM binary for HTTP POST is needed on the SD card.
`superb` itself has full TLS/HTTP internally but doesn't expose it to shell.

**Note on `doublelight_bOutVoice`:** Setting this to 0 via SystemCfg.ini
silences the voice alarm. If silenced, the `start maudio_speaker` log line
may still appear (untested) or may not -- this could affect log-based detection.
The `Create snap` and `goto preset` patterns are independent of voice setting.

### Cloud Properties

| Property | Description |
|----------|-------------|
| `MotionDetectSensitivity` | Motion detection sensitivity |
| `AlarmSwitch` | Master alarm enable |
| `FaceDetectSensitivity` | Human detection sensitivity |
| `IntelligentTracking` | PTZ auto-tracking |
| `CrossLineDetect` | Tripwire config |
| `RegionDetect` | Region intrusion config |
| `IvpAbility` | Intelligence feature bitmask |
| `AlarmFrequencyLevel` | Alarm frequency |
| `AlarmNotifyPlan` | Notification schedule |

---

## 5. Recording

### Current State

Recording to SD card is **already happening** (with SD inserted).

Files found on SD card:
```
/progs/rec/00/20260502/N192127.H265   (8.5 MB)  # N = Normal
/progs/rec/00/20260502/N193129.H265   (3.1 MB)
/progs/rec/00/20260502/N193501.H265   (3.4 MB)
/progs/rec/00/20260502/M193758.H265  (24.4 MB)  # M = Motion/alarm
/progs/rec/00/20260502/N194749.H265   (8.8 MB)
```

File naming: `{Type}{HHMMSS}.H265` where Type is N(ormal), M(otion), etc.
Files are raw H.265 streams, not MP4 containers.
Directory structure: `/progs/rec/00/YYYYMMDD/`

### Config Keys (SystemCfg.ini)

```ini
# CH1 (main stream) - recording DISABLED
[CH1]
bRecEnable=0            # OFF
RecFileLength=600       # 10 minutes per file
bRecAlarmEnable=1       # Record on alarm
bRecTimingEnable=0      # Scheduled recording OFF
storageMode=2           # Storage mode
recordMode=2            # Recording mode

# CH2 (sub stream) - recording ENABLED  
[CH2]
bRecEnable=1            # ON
RecFileLength=600       
bRecAlarmEnable=1
bRecTimingEnable=0
storageMode=1
```

### Recording Schedule (per channel)

```ini
RecTimingCount=7        # 7 schedule entries (one per day)
RecTimingWeek[0]=1      # Monday
RecTimingStart[0]=0     # 00:00:00
RecTimingStop[0]=86399  # 23:59:59
# ... through RecTimingWeek[6]=7 (Sunday)
```

### Cloud Properties

| Property | Description |
|----------|-------------|
| `StorageRecordMode` | Recording mode |
| `StorageRecordQuality` | Recording quality |
| `StorageStatus` | SD card status/capacity |
| `TimeRecordEnable` | Scheduled recording |
| `TimeRecordPlan` | Recording schedule |

### Cloud Commands

| Command | Description |
|---------|-------------|
| `FormatStorageMedium` | Format SD card |
| `QueryTFCard` | Query SD card status |
| `QueryRecordDateList` | List dates with recordings |
| `QueryRecordTimeList` | List recording times for a date |
| `QueryFileList` | List recording files |
| `DeleteFile` | Delete a recording |
| `StartVod` / `StopVod` | Video on demand playback |
| `StartVodByTime` | VOD by time range |

### Playback

ONVIF Recording/Replay/Search services all return `Action Not Implemented`.
Recordings can only be browsed by:
1. Direct SD card file access via root shell
2. Cloud app (Secueye/Danale)
3. Removing SD card and reading on PC (files are raw H.265)

---

## 6. PTZ (Pan-Tilt-Zoom)

### Hardware

- Motorized pan/tilt using stepper motor via `motor_advance.ko`
- Device: `/dev/motor`
- Max steps: LR=2100, UD=650
- Speed: LR=50.0, UD=40.0
- Electronic zoom (EZOOM) supported

### Config Keys (SystemCfg.ini)

```ini
moveSpeed=255           # Movement speed (max)
scanSpeed=55            # Auto-scan speed
bLRSW=1                 # Left-right swap
bUDSW=0                 # Up-down swap
bIZSW=0; bFZSW=1; bZSW=1  # Zoom swaps
guardTime=0             # Auto-return delay (0=disabled)
guardType=0; guardValue=0
nPTZAddr=1              # PTZ address
```

### ONVIF PTZ (Likely Working)

Service at `/onvif/ptz_service`:
- ContinuousMove, RelativeMove, AbsoluteMove
- GetPresets, SetPreset, RemovePreset
- GetPresetTours, CreatePresetTour
- SendAuxiliaryCommand

Configuration token: `PTZCFG_3900227332`

### Cloud Commands

| Command | Description |
|---------|-------------|
| `StartPTZAction` | Begin movement |
| `StopPTZAction` | Stop movement |
| `ZoomActionControl` | Zoom control |
| `PresetLocateControl` | Go to preset |
| `PresetAddControl` | Save preset |
| `PresetDeleteControl` | Delete preset |
| `QueryPresetMap` | List presets |

---

## 7. Audio

### Two-Way Audio

- Microphone: built-in, always active
- Speaker: built-in
- Audio in RTSP: G.711 in both streams (`bAudioEn=1`)
- Two-way talk: via cloud app only (uses P2P/RTMP)
- Chat mode: `chatMode=2`

### Audio Processing Libraries

| Library | Function | Size |
|---------|----------|------|
| `libvqe_aec.so` | Echo cancellation | 77KB |
| `libvqe_agc.so` | Gain control | 61KB |
| `libvqe_anr.so` | Noise reduction | 57KB |
| `libvqe_hpf.so` | High-pass filter | 10KB |
| `libvqe_eq.so` | Equalizer | 46KB |
| `libvqe_talkv2.so` | Two-way talk | 301KB |
| `libvqe_record.so` | Recording VQE | 201KB |

### Voice Prompts

Custom voice files can be placed on SD card:
- `seculinkVoice/language/*.711` -- language-specific prompts
- `seculinkVoice/common/*.711` -- common sounds

Stored in configfs: `/etc/conf.d/fixed/custom_voice/`

Available languages: English, Chinese, Russian, Vietnamese, Brazilian, and 2 others.

---

## 8. OSD (On-Screen Display)

### Config Keys (SystemCfg.ini)

```ini
bShowOSD=1              # OSD enabled
nPosition=3             # OSD position
nTimePosition=0         # Time position
bLargeOSD=1             # Large font
nOSDbInvColEn=1         # Inverse color
osdSize=32              # Font size
channelName=H.265 IPC   # Channel name text
timeFormat=MM/DD/YYYY hh:mm:ss
hours_fmt=1             # 0=24-hour, 1=12-hour (requires reboot, not just superb restart)
osdText0= through osdText7=  # Custom text lines
multiPosition=4         # Multiple OSD positions
alignment=0             # Text alignment
```

### Cloud Properties

| Property | Description |
|----------|-------------|
| `CustomIPCOSDName` | Custom OSD text |
| `DateFormat` | Date display format |
| `HoursFormat` | 12/24 hour format (maps to `hours_fmt` in SystemCfg.ini) |
| `TimeFormat` | Date format string (maps to `timeFormat` in SystemCfg.ini) |

---

## 9. Network / System

### Open Ports

| Port | Protocol | Service | Auth |
|------|----------|---------|------|
| 80 | TCP | ONVIF SOAP | None (auth headers break it) |
| 554 | TCP | RTSP | None |
| 9999 | TCP | Root shell (backdoor) | None |
| 34567 | TCP | DVRIP (stub) | Binary |
| 3702 | UDP | WS-Discovery | None |
| 30012 | UDP | Internal (superb) | Unknown |
| 30014 | UDP | Internal (superb) | Unknown |
| 8899 | UDP | mySystem heartbeat (localhost only) | Internal |

### WiFi Config

```ini
# Can be overridden via SD card: wifi/wpa_supplicant.conf
# Current DNS: nameserver 192.168.1.1 (from DHCP)
# Camera uses hardcoded Chinese DNS for NTP: 114.114.114.114, 223.5.5.5, 223.6.6.6, 8.8.8.8
```

### System Settings (SystemCfg.ini)

```ini
# Factory defaults (Chinese):
#   timezone=800, posixTZ=CST-8, regionTZ=Asia/Shanghai,
#   ntpServer=ntp.fudan.edu.cn (unreachable outside China)
# Fixed by tools/fix_timezone.py:
timezone=-800           # UTC-8 (PST). Offset in 1/100 hours; negative=west of UTC
posixTZ=PST8PDT,M3.2.0,M11.1.0  # POSIX TZ with DST rules (this is what the OSD uses)
regionTZ=America/Los_Angeles     # Olson timezone name
ntpServer=pool.ntp.org           # Global NTP (was ntp.fudan.edu.cn)
sntpInterval=24         # NTP sync every 24 hours
nLanguage=1             # Language (1=English?)
DevName=H.265 IPC       # Device name
enableStreamWatchDog=1  # Stream watchdog (restarts on failure)
rebootSwitch=0          # Scheduled reboot OFF
rebootDay=0; rebootHour=1
powerFreq=0             # 0=50Hz, 1=60Hz (anti-flicker)
```

### Email (SMTP) Config

```ini
acMailSender=ipcmail@163.com    # Factory default (Chinese email)
acSMTPServer=smtp.163.com
acSMTPUser=ipcmail
acSMTPPasswd=ipcam71a           # Plaintext password
acSMTPPort=25
acSMTPCrypto=none
acReceiver0=(null)              # No recipients configured
```

---

## 10. Alibaba IoT Cloud Protocol (Primary Control)

### Credentials

```
ProductKey:    a1y8M6TXvzw
DeviceName:    zZV8td5Gt8IQzlUVdMXE
DeviceSecret:  3ce9ed72245abc91b74b96e651d8ad3c
MQTT Broker:   public.iot-as-mqtt.cn-shanghai.aliyuncs.com
Port:          443 (TLS with mbedTLS)
SDK:           Alibaba IoT Link Vision (ali-smartliving-device-sdk-c)
```

### MQTT Topics

```
Subscribe: /sys/{PK}/{DN}/thing/service/property/set
Publish:   /sys/{PK}/{DN}/thing/event/property/post
Config:    /sys/{PK}/{DN}/thing/config/get
Reset:     /sys/{PK}/{DN}/thing/reset
```

### Thing Model Properties (Complete List from Binary)

**Image Control:**
- `NightVisionMode` -- Day/night/auto mode
- `IRLightBrightness` -- IR LED brightness
- `WhiteLightBrightness` -- White LED brightness
- `ImageFlipState` -- Image flip/mirror
- `StreamVideoQuality` -- Main stream quality
- `SubStreamVideoQuality` -- Sub stream quality

**Detection & Alarms:**
- `MotionDetectSensitivity` -- Motion detection sensitivity
- `AlarmSwitch` -- Master alarm enable
- `AlarmFrequencyLevel` -- Alarm frequency
- `AlarmNotifyPlan` -- Notification schedule
- `FaceDetectSensitivity` -- Human detection sensitivity
- `CrossLineDetect` -- Tripwire config
- `RegionDetect` -- Region intrusion config
- `IvpAbility` -- AI feature bitmask
- `AlarmLightSwitch` -- Alarm strobe
- `StrongReminderSwitch` -- Strong reminder

**Recording:**
- `StorageRecordMode` -- Recording mode
- `StorageRecordQuality` -- Recording quality
- `StorageStatus` -- SD card status
- `TimeRecordEnable` -- Scheduled recording

**Lights:**
- `FloodlightSwitch` -- White flood light
- `FloodlightSchedule` -- Flood light schedule
- `FloodlightScheduleEnable` -- Schedule enable
- `StatusLightSwitch` -- Status LED
- `DoubleLight` -- Dual-light mode
- `LaserLight` -- Laser illuminator

**PTZ:**
- `IntelligentTracking` -- Auto-tracking
- `PreviewSwitch` -- Preview mode

**System:**
- `RebootSchedule` -- Auto-reboot schedule
- `VoicePrompt` -- Voice prompt enable
- `CustomCmd` -- Custom command

### Internal IPC Commands (HI_XUID)

These are the internal message bus commands within `superb`:

```
HI_XUID_SET_NIGHTVISION_MODE   # Night vision control
HI_XUID_SET_FLIP_MIRROR        # Image flip
HI_XUID_SET_LED                # LED control
HI_XUID_SET_ALARM_ATTR         # Alarm configuration
HI_XUID_SET_IDR_FRAME          # Force IDR frame
HI_XUID_AI_DETECT_ATTR         # AI detection config
HI_XUID_AI_DETECT_MODE         # AI detection mode
HI_XUID_STREAM_ATTR            # Stream attributes
HI_XUID_SWITCH_RESOLUTION      # Resolution change
HI_XUID_SYNC_TIME              # Time sync
HI_XUID_KEEP_ALIVE             # Watchdog keepalive
HI_XUID_GET_SNAPSHOT_IMAGE     # Snapshot capture
HI_XUID_GET_VERSION            # Version query
HI_XUID_DRAW_OSD               # OSD drawing
HI_XUID_SLAVE_REBOOT           # Reboot command
HI_XUID_NPU_RESULT             # NPU inference result
HI_XUID_CTRL_ID                # Control identifier
HI_XUID_TRANSFER_DATA          # Data transfer
```

---

## 11. SD Card Jailbreak (recycle_ali.sh)

The factory provisioning script `updateID.sh` runs on **every boot** and
sources an arbitrary shell script from the SD card:

```bash
# From updateID.sh (runs as root, before superb starts):
if [ -f /var/udisk/seculinkIdRecycle/recycle_ali.sh ]; then
    chmod 777 /var/udisk/seculinkIdRecycle/recycle_ali.sh
    source /var/udisk/seculinkIdRecycle/recycle_ali.sh
fi
```

### To jailbreak without UART:

1. Format SD card as FAT32
2. Create directory: `seculinkIdRecycle/`
3. Create file: `seculinkIdRecycle/recycle_ali.sh`
4. Insert SD card, power cycle camera
5. Script executes as root

### Other SD Card Provisioning Paths

| SD Card Path | What It Does |
|---|---|
| `seculinkIdRecycle/recycle_ali.sh` | **Arbitrary code execution** |
| `seculinkAliyunUid/aliyunUid/*.conf` | Alibaba IoT credentials |
| `seculinkDanaleUid/danaleUid/*.conf` | Danale cloud credentials |
| `seculinkMac/seculinkMAC.txt` | MAC address override |
| `seculinkHardware/hwconfig.cfg` | Hardware config override |
| `seculinkVoice/language/*.711` | Voice prompt replacement |
| `seculinkVoice/common/*.711` | Common voice replacement |
| `wifi/wpa_supplicant.conf` | WiFi credentials override |
| `UserMallCfg.txt` | App store URL |
| `*.json` (root) | Hardware info override |

---

## 12. Firmware Update Mechanisms

### Built-in Updater (`/progs/bin/upgrade`, 43KB)

```
Usage: ./upgrade -u app.bin
```

- Knows all MTD partitions, does MD5 verification
- Supports A/B rootfs switching
- Can modify U-Boot env variables
- Monitors for `ota_upgrade.bin` in `/progs/rec/update` and `/progs/rec/00`

### Cloud OTA

- Server: `updatewt.afdvr.com`
- Endpoint: `/ota/device/{PK}/{DN}/{version}`
- Blocked by firewall (intentional)

### No U-Boot SD Card Boot

U-Boot boot command is hardcoded to SPI NOR flash:
```
sf probe 0; sf read 0x41000000 0x60000 0x200000; bootm 0x41000000
```
No SD card fallback exists in the bootloader.

---

## Appendix: ISP Functions in `superb`

### Sensor Control (secu_sensor_*)

```
secu_sensor_brightness              secu_sensor_contrast
secu_sensor_saturation              secu_sensor_saturate_set
secu_sensor_sharpness               secu_sensor_chroma
secu_sensor_set_security_image_effect
secu_sensor_set_nightmode           secu_sensor_set_daynight_mode
secu_sensor_set_ircut               secu_sensor_set_sence
secu_sensor_set_fps                 secu_sensor_set_isp_fps
secu_sensor_set_exp_mode            secu_sensor_set_exp_params
secu_sensor_set_definition          secu_sensor_set_dis
secu_sensor_set_local_exposure
secu_sensor_ae_set                  secu_sensor_ae_get
secu_sensor_awb                     secu_sensor_white_balance_get
secu_sensor_digital_wdr_set         secu_sensor_digital_wdr_get
secu_sensor_drc_set                 secu_sensor_drc_get
secu_sensor_wdr                     secu_sensor_wdr_switch
secu_sensor_wdr_mode_get
secu_sensor_mirror_flip             secu_sensor_mirror_flip_set
secu_sensor_rotate_set              secu_sensor_rotate_get
secu_sensor_light                   secu_sensor_nocolour
secu_sensor_antifog                 secu_sensor_auto_lowframe_enable
secu_sensor_low_frame               secu_sensor_flicker_enable
secu_sensor_b_night                 secu_sensor_b_night_auto
secu_sensor_b_night_use_ae          secu_sensor_Adc_CheckNight_v2
secu_sensor_check_live              secu_sensor_parse_bin
secu_sensor_get_ircut_staus         secu_sensor_get_isp_fps
secu_sensor_get_again               secu_sensor_get_focus_value
secu_sensor_get_low_frame           secu_sensor_get_b_low_frame
secu_sensor_get_dis                 secu_sensor_get_local_exposure
secu_sensor_daynight_ae_set
secu_sensor_exp_mode_strategy_set   secu_sensor_exp_mode_strategy_get
```

### HiSilicon ISP API (hi_mpi_isp_set_*)

```
hi_mpi_isp_set_csc_attr             # CSC: brightness/contrast/saturation/hue
hi_mpi_isp_set_saturation_attr      # Saturation curves
hi_mpi_isp_set_sharpen_attr         # Sharpening
hi_mpi_isp_set_gamma_attr           # Gamma correction
hi_mpi_isp_set_exposure_attr        # Exposure control
hi_mpi_isp_set_wb_attr              # White balance
hi_mpi_isp_set_ccm_attr             # Color correction matrix
hi_mpi_isp_set_drc_attr             # Dynamic range compression
hi_mpi_isp_set_dehaze_attr          # Dehaze
hi_mpi_isp_set_nr_attr              # Noise reduction
hi_mpi_isp_set_ldci_attr            # Local dynamic contrast
hi_mpi_isp_set_black_level_attr     # Black level
hi_mpi_isp_set_color_tone_attr      # Color tone
hi_mpi_isp_set_color_sector_attr    # Color sector
hi_mpi_isp_set_demosaic_attr        # Demosaic
hi_mpi_isp_set_anti_false_color_attr
hi_mpi_isp_set_ca_attr              # Chromatic aberration
hi_mpi_isp_set_cac_attr             # Chromatic aberration correction
hi_mpi_isp_set_mesh_shading_attr    # Lens shading correction
hi_mpi_isp_set_fswdr_attr           # Frame-stitch WDR
hi_mpi_isp_set_smart_exposure_attr  # Smart exposure
hi_mpi_isp_set_bayershp_attr        # Bayer sharpening
```

---

## Appendix: Process Architecture

```
PID 1    init (BusyBox)
  |-- rcS -> bashrc.sh -> startup.sh -> debug.sh
  |-- PID ~1046  mySystem (process watchdog, UDP 8899 heartbeat)
  |-- PID ~1052  tcpsvd 0.0.0.0 9999 /bin/sh -il (backdoor)
  |-- PID ~1055  superb (main binary, all protocols)
  |     |-- TCP 80  (ONVIF)
  |     |-- TCP 554 (RTSP)
  |     |-- TCP 34567 (DVRIP)
  |     |-- UDP 3702 (WS-Discovery)
  |     |-- UDP 30012, 30014 (internal)
  |     |-- UDP 34569 (broadcast)
  |     |-- 7x /dev/isp_dev FDs
  |     `-- /dev/motor (PTZ)
  `-- kernel threads: [phy0-ble_recv], [kworker], etc.

Watchdog: Hardware, 30 second timeout, fed by superb on fd 4
          If superb crashes/hangs, SoC hard-resets.
mySystem: UDP heartbeat monitor, restarts superb on failure.
```
