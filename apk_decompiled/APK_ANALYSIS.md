# Secueye APK Decompilation Reference

APK: `com.seculink.app_2.3.7.apk` (Secueye v2.3.7)
Decompiler: jadx v1.5.5
Original stats: 30,383 classes, 188,168 methods, 16,381 decompiled source files

## Cleanup

Third-party libraries (~11,800 files, 72% of total) were removed to keep only
project-relevant code. The original APK is preserved at the repo root if
re-decompilation is ever needed.

**Removed:** AndroidX, Kotlin stdlib, OkHttp/Okio, Netty (`io/`), Google
(ExoPlayer, GMS, Firebase, Material, Gson, ZXing), Facebook SDK, Glide,
Tencent (Bugly/WeChat), Xiaomi/Huawei/Vivo/OPPO push SDKs, various UI libs
(PickerView, CalendarView, PhotoView, EventBus, etc.), Mozilla Rhino,
SpongyCastle crypto, obfuscated library internals (`c/`, `d/`), and 24
sub-packages under `com/`.

**Kept:** All Secueye app code, Alibaba IoT/BLE/mesh SDKs, Aliyun
LinkVision/LinkSDK, and obfuscated camera-specific code.

---

## Directory Map

### Secueye App Code

| Directory | Files | Contents |
|-----------|-------|----------|
| `activity/` | 138 | All Activity classes -- `IPCameraActivity` (live view), `LoginActivity`, `CameraSettingActivity`, `BleRouterActivity`, `CloudStorageActivity`, `RecordVideoActivity`, `AddDeviceActivity` (WiFi provisioning sequence at lines 211-240), etc. |
| `adapter/` | 51 | RecyclerView/List adapters -- `DeviceAdapter`, `WiFiAdapter`, `CloudDownloadAdapter`, `ScanBleAdapter`, etc. |
| `bean/` | 103 | Data model classes -- `Device`, `CloudVideo`, `AlarmPlanBean`, `BleFoundDevice`, `PushBean`, `WifiBean`, IoT request/response beans |
| `bluetooth/` | 9 | BLE device add/bind business logic -- `AddBleDeviceBusiness`, `DeviceAddHandler`, `DeviceBindBusiness` |
| `config/` | 4 | `APIConstants` (API endpoints), `AppConfig`, `Constants` (Thing Model property names), `TMPConstants` (cloud command names) |
| `datasource/` | 140 | IoT mesh network data source -- `MeshConfig`, `AuthManager`, Feiyan auth, IoT device provisioning |
| `dialog/` | 21 | UI dialogs -- `LoadingDialog`, `UpgradeDialog`, `ShareDialog`, `LenCamCtrlDialog`, `TimeSettingDialog` |
| `enums/` | 4 | `ActionTypeEnum`, `NetworkStateEnum`, `PicRequestTypeEnums`, `SpeedEnum` |
| `event/` | 2 | Event bus definitions -- `EventType`, `MyEvent` |
| `fragment/` | 16 | UI fragments -- `HomeTabFragment`, `ControllerFragment`, `LensControllerFragment`, `MessageFragment`, `YunFragment` |
| `kt/` | 14 | Kotlin-sourced classes -- `AlexaUtil`, `APNActivity`, `AreaDetectActivity`, `EXOPlayActivity`, `SensorView` |
| `receiver/` | 5 | Broadcast receivers/services -- `PushReceiver`, `NotificationReceiver`, `AccessService` |
| `sdk/` | 10 | **Core SDK integration** -- `IPCManager` (singleton entry), `IPCDevice` (wraps Aliyun PanelDevice, `CLOUD_CHANNEL_ONLY`), `LinkVisionAPI` (REST calls to `/vision/customer/*`), `ChannelManager` (MQTT push), `SDKInitHelper` (App Key: `26001873`) |
| `tools/` | 68 | Utilities -- `BleClient.java` (BLE UUIDs + provisioning), `G711Code`/`G711A` (audio codecs), `AudioPlayManager`, `NetworkUtil`, `MessageApi`, `WXApiManager` |
| `view/` | 72 | Custom views -- `ZoomableTextureView`, `MyGlSurfaceView`, `DevicePanelView`, `SeekTimeBar`, `TimeRulerView`, `JoystickTouchViewListener` |

### BLE & Mesh Provisioning

| Directory | Files | Contents |
|-----------|-------|----------|
| `aisble/` | 65 | Alibaba IoT BLE manager -- `BleManager`, `ConnectRequest`, `ReadRequest`, `WriteRequest`, callbacks |
| `aisscanner/` | 6 | BLE scanner compat layer -- `BluetoothLeScannerCompat`, `ScanCallback`, `ScanFilter` |
| `meshprovisioner/` | 138 | BLE Mesh provisioner -- `BaseMeshNode`, configuration, control, transport, models, states |
| `a/` | 243 | Obfuscated Alibaba IoT BLE provisioning internals -- `GattTransmissionLayer`, `AdvertiseManager`, `MobileChannelImpl`, `SendProvisionDataMsg`, `EncryptUtil` |
| `b/` | 64 | Obfuscated BLE Mesh networking -- `BlockAcknowledgementMessage`, `SubnetsBiz`, `FastProvisionManager` |

### LinkVision Camera SDK (Obfuscated)

| Directory | Files | Contents |
|-----------|-------|----------|
| `lvbreak/` through `lvvoid/` (15 dirs) | 41 | Alibaba LinkVision SDK -- video stream URLs (`LIVE_QUERY`, `CLOUD_VOD_BY_FILENAME`), `MediaCodec` audio decoder with `G711Code`, `AudioParams`, camera streaming config. The `lv` prefix = "LinkVisual". |

### Alibaba Cloud SDKs

| Directory | Files | Contents |
|-----------|-------|----------|
| `com/alibaba/` | 1,171 | AILabs IoT SDK (BLE advertise, mesh provision, GATT library), Android ACCS (long-lived connections), CloudAPI, FastJSON parser, third-push integration |
| `com/aliyun/` | 1,533 | `iotx/linkvisual/media/` (LinkVision media SDK -- camera streaming), `linksdk/` (CoAP/ALCS local control), `iot/aep/` (application enablement), `iot/push/` (IoT push channel) |
| `com/taobao/` | 144 | ACCS session management (long-lived connection to Alibaba), Agoo push infrastructure |
| `anet/` | 204 | Alibaba network channel library -- ACCS session, strategy, heartbeat |
| `anetwork/` | 92 | Alibaba HTTP layer -- cache, download, interceptor |

### App Package

| Directory | Files | Contents |
|-----------|-------|----------|
| `com/seculink/` | 177 | Main Seculink package -- `BuildConfig`, `R`, `DataBinderMapperImpl`, databinding generated code, WeChat API entry |
| `com/http/` | 7 | App HTTP networking helpers |
| `com/linkkit/` | 16 | Link Kit tools/utils for IoT communication |

---

## Key Files for Camera Control Investigation

### Cloud Protocol (Thing Model)

- **`config/Constants.java`** -- All Thing Model property names (`MotionDetectSensitivity`, `AlarmSwitch`, `NightVisionMode`, `ImageFlipState`, `StorageRecordMode`, `IRLightBrightness`, etc.)
- **`config/TMPConstants.java`** -- All cloud command names (`StartPTZAction`, `Restart`, `FormatStorageMedium`, `QueryRecordDateList`, `SetWifi`, etc.)
- **`sdk/IPCManager.java`** -- Singleton entry point, delegates all camera operations to Aliyun cloud APIs
- **`sdk/IPCDevice.java`** -- Wraps Aliyun `PanelDevice`, uses `CLOUD_CHANNEL_ONLY` strategy (no local LAN control)
- **`sdk/LinkVisionAPI.java`** -- HTTPS REST calls to `/vision/customer/*` endpoints
- **`sdk/ChannelManager.java`** -- MQTT push channel for real-time events and property changes
- **`sdk/SDKInitHelper.java`** -- App Key: `26001873`, Secret: `f4a90ebee699166af95b092dffadcfc3`

### BLE Provisioning

- **`tools/BleClient.java`** -- BLE UUIDs (service `0x181C`, char `0x2A8A`/`0x2A90`), `sendOrder2()` write method, notification handler
- **`activity/AddDeviceActivity.java:211-240`** -- WiFi provisioning sequence (`SSID:`, `PWD:`, `STATUS?`)
- **`activity/BleScantActivity.java:137-164`** -- Same provisioning sequence, alternate UI path

### Video Streaming

- **`lv*/`** -- LinkVision SDK with video stream query URLs, MediaCodec audio decoding
- **`com/aliyun/iotx/linkvisual/media/`** -- Native media pipeline (P2P, RTMP, HLS)
- **`tools/G711Code.java`**, **`tools/G711A.java`** -- G.711 audio codec implementation

### API Endpoints

- **`config/APIConstants.java`** -- REST API base URLs
- **`sdk/LinkVisionAPI.java`** -- Full list of cloud API calls:
  - `/awss/time/window/user/bind` -- device binding
  - `/thing/info/get` -- device status
  - `/vision/customer/storage/picture/capture` -- cloud snapshot
  - `/vision/customer/storage/device/record/query` -- cloud recording
  - `/vision/customer/eventrecord/plan/*` -- recording plan CRUD
  - `https://traffic.secueye.app/api/app/*` -- Secueye backend

---

## Architecture Note

The Secueye app contains **zero DVRIP code**. It never connects to port 34567.
All camera communication flows through Alibaba Cloud IoT (Aliyun Link Vision):

```
Secueye App
    | HTTPS REST API + MQTT-over-TLS
    v
Alibaba Cloud IoT Platform
    | MQTT
    v
Camera's superb binary (cloud agent module)
    | Internal function calls
    v
ISP / recording / detection / PTZ subsystems
```

Video streaming uses a native library (`liblinkvision.so`) via P2P (STUN/TURN),
RTMP (cloud relay), or HLS (cloud recordings) -- not RTSP or DVRIP.
