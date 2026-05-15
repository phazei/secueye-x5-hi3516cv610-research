# Phase 3 -- Pipeline Init Working, Frame Flow Pending

## Status

**Full pipeline (VI -> ISP -> VPSS -> VENC) initializes end-to-end via the
V1.0.2.1 SDK. The only remaining issue is that `vpss_get_chn_frame` times
out -- frames aren't flowing through the pipe yet.**

## What Works (confirmed on camera)

| Component | Method | Status |
|-----------|--------|--------|
| Sensor driver load (dlopen) | `libsns_sc635hai.so` | OK |
| VB pool / SYS init | V1.0.2.1 SDK | OK |
| VI-VPSS mode set | V1.0.2.1 SDK | OK |
| MIPI RX config | Raw ioctl on `/dev/ot_mipi_rx` | OK |
| VI init (dev/pipe/chn) | V1.0.2.1 SDK (native) | OK |
| Sensor I2C init (374 regs) | sc635hai driver | OK |
| ISP mem_init / set_pub_attr / init / run | V1.0.2.1 SDK | OK |
| AE/AWB register | V1.0.2.1 SDK | OK |
| VPSS create_grp / start_grp / set_chn_attr / enable_chn | V1.0.2.1 SDK | OK |
| VI -> VPSS bind | `sys_bind` | OK |
| VENC create JPEG chn + SNAP mode | V1.0.2.1 SDK | OK |
| `vpss_get_chn_frame` | V1.0.2.1 SDK | **TIMEOUT (0xA0078016)** |

## The VPSS Blocker -- Resolved

The previous "VPSS blocker" was caused by **misinformation in this document**.
Every theory was wrong:

| Previous theory | Reality |
|-----------------|---------|
| `0x00005007` is `VPSS_CREATE_CHN`, required before `SET_CHN_ATTR` | **WRONG**: it's `VPSS_RESET_GRP`. Calling it between REG and SET_CHN_ATTR wiped the group state, causing `errno=2` (ENOENT). |
| `libss_mpi.so` hooks/intercepts `ioctl()` | **WRONG**: `readelf -s` shows `ioctl` is a regular UND import. No hooking. |
| SDK `set_chn_attr` fails with `0xA0078007` due to struct layout mismatch | **WRONG**: that error was observed before ISP was properly initialized. With ISP working, the SDK call succeeds. |
| B051 kernel struct differs from V1.0.2.1 headers, need raw ioctls | **WRONG**: V1.0.2.1 SDK matches B051 kernel ABI (both V1.0.2.x). SDK works natively. |

**Fix**: replace raw VPSS ioctl code with `ss_mpi_vpss_create_grp` /
`start_grp` / `set_chn_attr` / `enable_chn`. Done.

### How we figured it out

Disassembled `libss_mpi.so`:
- `ot_mpi_vpss_reset_grp` (0x17c2c) uses cmd `0x00005007` -- **not** create_chn
- `ot_mpi_vpss_set_chn_attr` (0x182b0) uses cmd `0x40605008` directly after REG
- Internal helper at `0x1778c` lazily opens `/dev/vpss` and calls REG with
  packed arg `((grp & 0xFF) << 16) | (chn & 0xFF)`
- The SDK maintains an internal fd table indexed by `(6 * grp + chn)`

Trying to replicate this from outside the SDK is error-prone. Using the SDK
directly is the right approach.

## ISP Warnings (non-fatal)

```
isp_check_dng_color_param: ccm_tab1/2.color_temp should between [2000, 10000]
isp_init_sensor_update: ISP[0] dng_info not initialized in cmos.c!
awb_result_check: awb result white_balance_gain should not be all 0
```

These occur because `cmos_get_isp_default()` returns a zeroed struct (no PQ
calibration data embedded). ISP can load PQ bins from `/home/sensor/sc635hai/pqbin/`
which we haven't provided yet. Pipeline runs without them but image quality
will be suboptimal.

## The Remaining Issue: Frame Timeout

After full pipeline init, `ss_mpi_vpss_get_chn_frame(0, 0, &frame, 5000)`
returns `0xA0078016`:
- `0x007` = module VPSS (`HI_ID_VPSS`)
- `0x16` = errnum 22 = `OT_ERR_TIMEOUT`

Pipeline is initialized but no frames are flowing. Likely causes:
1. **VENC not started** -- in bound mode (VPSS->VENC via `sys_bind`), VENC
   pulls frames automatically. We should bind VPSS->VENC and call
   `ss_mpi_venc_start_chn` rather than manually grabbing from VPSS.
2. **Sensor settling** -- ISP may need more time after init before frames flow.
3. **VPSS chn depth=1** -- might need more buffers, or to bind VPSS->VENC so
   frames are consumed downstream.

### Next Steps

1. Bind VPSS -> VENC via `sys_bind`
2. Call `ss_mpi_venc_start_chn` (let SDK drive the flow)
3. Get JPEG stream via `ss_mpi_venc_get_stream`
4. Save to file

Reference: `sample_comm_venc.c:1720` (`sample_comm_venc_snap_start`) in the
V1.0.2.1 SDK shows the canonical JPEG snapshot pattern.

## Deploy and Run

```bash
# Build (from WSL)
cd /mnt/e/Projects/ipc_XMeye_camera/driver && make pipeline

# Start recv daemon on camera (if not running -- gets killed when superb dies)
python tools/cam_cmd.py "pidof recv || (nohup /progs/rec/00/recv 8888 /progs/rec/00 -d > /dev/null 2>&1 &)"

# Deploy
python tools/send_file.py 192.168.1.153 8888 driver/build/pipeline_test

# Run (SDK libs + ISP plugins already on camera)
python tools/cam_cmd.py "killall superb; sleep 2; cd /progs/rec/00 && \
    LD_PRELOAD='/progs/rec/00/libbnr.so /progs/rec/00/libdrc.so /progs/rec/00/libacs.so /progs/rec/00/libcalcflicker.so /progs/rec/00/libir_auto.so /progs/rec/00/libldci.so /progs/rec/00/libdehaze.so /progs/rec/00/libextend_stats.so' \
    LD_LIBRARY_PATH=/progs/rec/00 \
    ./pipeline_test"
```

ISP plugin libs need `LD_PRELOAD` since `libot_mpi_isp.so` doesn't list them
as `NEEDED`. The V1.0.2.1 SDK handles VI/ISP/VPSS natively -- no shim required.

## SDK Version Compatibility

```
V1.0.0.3 B030 (Jun 2024) -- kodo repo, ISP type=0x49     INCOMPATIBLE
V1.0.1.0 B040 (Sep 2024) -- HIVIEW libs, ISP type=0x49   INCOMPATIBLE
V1.0.2.0 B051 (Apr 2025) -- camera firmware, ISP type=0x70
V1.0.2.1 B020 (May 2025) -- our SDK, ISP type=0x70       COMPATIBLE
```

The ISP ioctl type changed from 0x49 to 0x70 between V1.0.1.x and V1.0.2.x
(complete interface redesign). The "B" number is relative to the SDK major
version, not globally sequential.

---

## Reference: B051 Kernel Ioctl Commands

The V1.0.2.1 SDK handles these internally. Documented for debugging only.

### VI module (`/dev/vi`)

| CMD | Value | Size | Description |
|-----|-------|------|-------------|
| VI_SET_DEV_ATTR | 0x40784900 | 120 | Set VI device attributes |
| VI_ENABLE_DEV | 0x00004902 | 0 | Enable VI device |
| VI_DISABLE_DEV | 0x00004903 | 0 | Disable VI device |
| VI_BIND | 0x4004490a | 4 | Bind dev to pipe |
| VI_UNBIND | 0x4004490b | 4 | Unbind dev from pipe |
| VI_WDR_FUSION | 0x401c490d | 28 | Set WDR fusion group |
| VI_SET_PIPE_ATTR | 0x40204910 | 32 | Set pipe attributes |
| VI_ISP_ALGO_CFG | 0x40104914 | 16 | ISP algorithm config |
| VI_START_PIPE | 0x0000491e | 0 | Start pipe |
| VI_STOP_PIPE | 0x0000491f | 0 | Stop pipe |
| VI_PIPE_CFG | 0x40084939 | 8 | Pipe config |
| VI_PIPE_FREQ | 0x4004494d | 4 | Pipe clock frequency |
| VI_SET_CHN_ATTR | 0x402c494e | 44 | Set channel attributes |
| VI_ENABLE_CHN | 0x00004952 | 0 | Enable channel |
| VI_DISABLE_CHN | 0x00004953 | 0 | Disable channel |
| VI_REG_DEV | 0x40044961 | 4 | Register fd for dev_id |

### ISP module (`/dev/isp_dev`)

| CMD | Value | Size | Description |
|-----|-------|------|-------------|
| ISP_REG | 0x40047000 | 4 | Register pipe_id |
| ISP_GET_STATUS | 0x80047022 | 4 | Get init status |
| ISP_SET_PUB_ATTR | 0x4034703a | 52 | Set public attributes |
| ISP_GET_PUB_ATTR | 0x8034703b | 52 | Get public attributes |

### VPSS module (`/dev/vpss`)

| CMD | Value | Size | Description |
|-----|-------|------|-------------|
| VPSS_REG | 0x40045000 | 4 | Register grp/chn -- arg = `((grp & 0xFF) << 16) \| (chn & 0xFF)` |
| VPSS_START_GRP | 0x00005005 | 0 | Start group |
| VPSS_STOP_GRP | 0x00005006 | 0 | Stop group |
| VPSS_RESET_GRP | 0x00005007 | 0 | Reset group (NOT "create chn"!) |
| VPSS_SET_CHN_ATTR | 0x40605008 | 96 | Set channel attributes |
| VPSS_GET_CHN_ATTR | 0x80605009 | 96 | Get channel attributes |
| VPSS_ENABLE_CHN | 0x0000500a | 0 | Enable channel |
| VPSS_DISABLE_CHN | 0x0000500b | 0 | Disable channel |
| VPSS_SET_GRP_ATTR | 0x4038500c | 56 | Set group attributes |
| VPSS_DESTROY_GRP | 0x0000500d | 0 | Destroy group |

### SYS module (`/dev/sys`)

| CMD | Value | Size | Description |
|-----|-------|------|-------------|
| SYS_INIT | 0x00005900 | 0 | System init |
| SYS_EXIT | 0x00005901 | 0 | System exit |
| SYS_BIND | 0x40185907 | 24 | Bind modules |
| SYS_GET_CHIP_ID | 0x8004590f | 4 | Get chip ID |
| SYS_SET_VI_VPSS_MODE | 0x40105910 | 16 | Set VI-VPSS mode |
| SYS_GET_VI_VPSS_MODE | 0x80105911 | 16 | Get VI-VPSS mode |

### MIPI RX (`/dev/ot_mipi_rx`)

All ioctls type 'm' (0x6d). See `ot_mipi_rx.h` in the SDK.

## VPSS Struct Layouts (verified)

`ot_vpss_chn_attr` (96 bytes), verified via `offsetof()` and disassembly:

```
mirror_en      bool(4)    @ 0
flip_en        bool(4)    @ 4
border_en      bool(4)    @ 8
width          u32(4)     @ 12
height         u32(4)     @ 16
depth          u32(4)     @ 20
chn_mode       enum(4)    @ 24
video_format   enum(4)    @ 28
dynamic_range  enum(4)    @ 32
pixel_format   enum(4)    @ 36
compress_mode  enum(4)    @ 40
frame_rate     {s32,s32}  @ 44  (src @ 44, dst @ 48)
border_attr    {5xu32}    @ 52  (top/bottom/left/right/color)
aspect_ratio   {enum,u32,rect} @ 72
```

`ot_vpss_grp_attr` (56 bytes), per SDK source.

Both layouts match the V1.0.2.1 SDK headers and the B051 kernel ABI.

---

## Project Files

| File | Purpose |
|------|---------|
| `driver/src/sc635hai_cmos.c` | Sensor driver ISP/AE/AWB callbacks |
| `driver/src/sc635hai_cmos.h` | SC635HAI constants, 7-range gain tables |
| `driver/src/sc635hai_sensor_ctl.c` | I2C register init, read/write |
| `driver/src/hi_compat.h` | `hi_*` -> `ss_mpi_*` compatibility macros |
| `driver/test/pipeline_test.c` | Full pipeline test (SYS/VB/MIPI/VI/ISP/VPSS/VENC) |
| `driver/Makefile` | Build system (V1.0.2.1 SDK paths) |
| `tools/ioctl_hook.c` | LD_PRELOAD ioctl logger (debugging) |
| `tools/cam_cmd.py` | Remote command execution via TCP shell |
| `tools/send_file.py` | File transfer to camera via recv daemon |
| `research/sc635hai_rockchip_v4l2.c` | Rockchip kernel driver (gain model reference) |

## Files on Camera (`/progs/rec/00/`)

SD card storage survives reboot. recv daemon must be restarted each boot:
```
/progs/rec/00/recv 8888 /progs/rec/00 -d &
```

Currently deployed:

| File | Size | Source |
|------|------|--------|
| recv | 17KB | Our build (TCP file receiver) |
| pipeline_test | 18KB | Our build (Phase 3 test) |
| ioctl_hook.so | 9.4KB | Our build (debug logger) |
| libsns_sc635hai.so | 14KB | Our build (sensor driver) |
| libss_mpi.so | 118KB | V1.0.2.1 SDK |
| libot_mpi_isp.so | 385KB | V1.0.2.1 SDK |
| libss_mpi_isp.so | 22KB | V1.0.2.1 SDK |
| libss_mpi_ae.so | 161KB | V1.0.2.1 SDK |
| libss_mpi_awb.so | 63KB | V1.0.2.1 SDK |
| libss_mpi_sysbind.so | 13KB | V1.0.2.1 SDK |
| libss_mpi_sysmem.so | 13KB | V1.0.2.1 SDK |
| libsecurec.so | 38KB | V1.0.2.1 SDK |
| libot_osal.so | 38KB | V1.0.2.1 SDK |
| lib{bnr,drc,acs,calcflicker,ir_auto,ldci,dehaze,extend_stats}.so | 5-30KB | V1.0.2.1 SDK ISP plugins |

## Building

```bash
cd /mnt/e/Projects/ipc_XMeye_camera/driver
make driver     # libsns_sc635hai.so
make pipeline   # pipeline_test
make hook       # ioctl_hook.so (debug)
make all
```

## Key Technical Details

- Camera IP: 192.168.1.153, root shell on port 9999 (tcpsvd backdoor)
- Shell: `python tools/cam_cmd.py "command"` (raw TCP, not SSH)
- File transfer: `python tools/send_file.py 192.168.1.153 8888 <files...>`
- Kernel: V1.0.2.0 B051. SDK libs: V1.0.2.1 B020. Compatible.
- rootfs is read-only squashfs -- can't install libs to `/lib/`
- Musl ignores `LD_LIBRARY_PATH` for required libs -- rpath is set in our binaries
- V1.0.2.1 SDK API prefix: `ss_mpi_*` (also exports `ot_mpi_*` aliases)
- Sensor driver uses `ss_mpi_*` directly; `hi_compat.h` lets `pipeline_test.c`
  keep older `hi_*` symbol names for now
- SC635HAI gain: 7 analog ranges (max 83.79x) + 4 digital stages (max 15.875x) = 1330x total
- Group hold: reg 0x3812 brackets per-frame register writes (0x00=start, 0x30=release)
- `/progs/bin/superb` is the stock daemon (7.8MB, V1.0.2.0 B051, statically linked)
