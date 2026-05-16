# Driver & Pipeline Build System

Build system for the SECUEYE X5 camera firmware: SC635HAI sensor driver,
video pipeline binary, RTSP server, and supporting tools.

## Directory structure

```
driver/
  src/                        Our sensor driver source
    sc635hai_cmos.c             ISP/AE/AWB callbacks
    sc635hai_sensor_ctl.c       I2C init + register control
    sc635hai_cmos.h             Constants, gain tables, register map
    hi_compat.h                 hi_* -> ss_mpi_* API compat macros

  test/                       Test programs / main binary
    pipeline_test.c             Full video pipeline + RTSP (future daemon)
    sensor_test.c               Standalone I2C sensor test
    reg_dump.c                  I2C register dump utility
    awb_dump.c                  ISP AWB calibration reader

  rtsp/                       RTSP server integration
    rtsp_push.c                 Our wrapper around the xop RTSP library
    rtsp_push.h                 Wrapper header
    include/
      rtsp_server_api.h         Vendored xop RTSP API header
    lib/
      libxoprtsp.a              Vendored xop RTSP static archive (ARM, C++)
    src/                        HiSilicon API wrapper (modified for audio)
      rtsp_manager.cpp
      rtsp_manager.h
      rtsp_server_api.cpp
      rtsp_server_api.h
    objs/                       Pre-compiled ARM .o files (for re-archiving)
    xop/                        Full xop RTSP/RTP source (for full rebuilds)
      net/                        Network layer (TCP, sockets, epoll)
      xop/                        RTSP/RTP protocol + media sources
      3rdpart/                    MD5 (digest auth)

  prebuilt/                   Vendored HiSilicon SDK binaries
    sdk_include/                SDK headers (140 files, build-time only)
    sdk_mpi/                    SDK shared libs (9 .so, deployed to camera)
    isp_plugins/                ISP algorithm plugins (8 .so, LD_PRELOAD'd)
    pq/
      libbin.so                 PQ bin file loader (deployed to camera)

  build/                      Build output (gitignored except final binaries)
  Makefile                    Cross-compilation build system
```

## Origins of vendored files

All files under `prebuilt/` and `rtsp/{include,lib,src}/` are vendored
copies from external sources. They are checked into this repo so the build
has no dependency on git submodules that could disappear.

| Directory | Source | Notes |
|-----------|--------|-------|
| `prebuilt/sdk_include/` | `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/include/hisilicon/` | 140 HiSilicon MPP API headers. Build-time only. |
| `prebuilt/sdk_mpi/` | `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/lib/hisilicon/` | 9 shared libs linked by pipeline_test. Deployed to camera. |
| `prebuilt/isp_plugins/` | `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/lib/hisilicon/` | 8 ISP algorithm plugins. Loaded via LD_PRELOAD at runtime. |
| `prebuilt/pq/libbin.so` | `hi3516cv610_PictureQuality/.../libbin/release/` | PQ bin file loader. Deployed to camera. |
| `rtsp/lib/libxoprtsp.a` | `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/lib/3rdparty/` | xop RTSP server, static archive. Modified to add G.711A audio support (Phase 1 prep). Linked into pipeline_test at build time. |
| `rtsp/include/` | `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/include/3rdparty/` | RTSP API header. Modified to add audio function declarations. |
| `rtsp/src/` | `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/src/rtspserver/hisi_sample/` | HiSilicon API wrapper. Modified to add audio session/push support. |
| `rtsp/objs/` | `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/src/rtspserver/objs/` | Pre-compiled ARM .o files for the xop core. Can re-archive with modified wrapper .o files without recompiling the full stack. |
| `rtsp/xop/` | `Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/src/rtspserver/src/` | Full xop RTSP/RTP C++ source (70 files). For complete rebuilds from source if needed. |

The only external dependency not vendored is the **ARM cross-compilation
toolchain** at `research/hi3516cv610_toolchain/`. It is too large to vendor
(~1 GB). If the submodule is lost, the same toolchain can be obtained from
HiSilicon SDK distributions for Hi3516CV610.

## Building

Requires WSL (the ARM cross-toolchain is Linux x86_64 ELF binaries):

```bash
cd /mnt/e/Projects/ipc_XMeye_camera/driver
make all
```

Build outputs in `build/`:

| File | Description |
|------|-------------|
| `pipeline_test` | Full video pipeline binary with RTSP server (~430 KB) |
| `libsns_sc635hai.so` | SC635HAI sensor driver shared library (~14 KB) |
| `recv` | TCP file receiver for deployment (~17 KB, static) |
| `reg_dump` | I2C register dump tool (~22 KB, static) |
| `sensor_test` | Standalone sensor I2C test (~22 KB, static) |
| `libbin.so` | PQ bin loader (copied from prebuilt) |

## Deploying to camera

Use the redeploy script from the project root:

```powershell
.\tools\redeploy_all.ps1
```

This sends all build artifacts, prebuilt SDK libs, ISP plugins, and
on-camera scripts to `/progs/rec/00/ipc_drv/` on the camera. See the
script header for prerequisites and options (SCP vs recv transport).

For cold-start (no recv or SSH on camera):

```powershell
.\tools\bootstrap_deploy.ps1
```

## Sensor driver architecture

The driver follows the standard HiSilicon 2-file pattern:

**`sc635hai_sensor_ctl.c`** -- hardware layer:
- I2C communication via the SDK's sensor I2C abstraction
- 374-register init sequence for 3200x1800 @ 20fps linear mode (BGGR)
- Group hold around per-frame updates (reg 0x3812)

**`sc635hai_cmos.c`** -- ISP integration:
- `ot_isp_sns_obj g_sns_sc635hai_obj` (12 function pointers for V1.0.2.1)
- ISP callbacks: init, black level, register sync, image mode
- AE callbacks: exposure update, gain update, gain table lookup, FPS set
- AWB callback: defaults
- Triple registration: ISP + AE + AWB via `ss_mpi_*` APIs

**`sc635hai_cmos.h`** -- shared constants:
- 7-range analog gain model (max 83.79x analog, 1330x with digital)
- Register addresses for exposure, gain, timing, group hold
- Sensor ID, resolution, timing constants

## See also

- `DRIVER.md` -- canonical sensor driver reference
- `DRIVER_INTERNALS.md` -- kernel decompilation forensics
- `ROADMAP.md` -- project plan
- `README.md` (root) -- project overview, quick start
