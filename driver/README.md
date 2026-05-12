# SC635HAI Sensor Driver for Hi3516CV610

Open-source sensor driver for the SmartSens SC635HAI 6MP image sensor,
targeting the Hi3516CV610 SoC (SECUEYE X5 camera).

**No open-source SC635HAI sensor driver existed for HiSilicon platforms
before this one.** Built from:

- **V1.0.2.1 SDK headers** (`research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/`)
  for the `ot_isp_sns_obj` callback structure
- **Rockchip V4L2 SC635HAI driver** (`research/sc635hai_rockchip_v4l2.c`) for
  the 7-range analog gain model (max 83.79x) and 4-stage digital gain
- **Ghidra decompilation of `superb`** for the 374-register linear 6M @ 30fps
  init sequence and I2C addressing
- **HIVIEW SC500AI / SC450AI sensor drivers** as a template for the two-file
  ISP/AE/AWB callback architecture

## Files

```
driver/
  src/
    sc635hai_cmos.h          Constants, gain tables, register addresses
    sc635hai_sensor_ctl.c    I2C read/write, init sequence
    sc635hai_cmos.c          ISP/AE/AWB callbacks, ot_isp_sns_obj export
    hi_compat.h              hi_* -> ss_mpi_* compatibility macros
  test/
    sensor_test.c            Standalone test binary (phases 1-4)
    pipeline_test.c          Full pipeline test (SYS/VB/MIPI/VI/ISP/VPSS/VENC)
    check_offsets.c          Struct layout verification
  Makefile                   Cross-compilation build system
```

## Building

Requires WSL (toolchain is Linux x86_64 ELF):

```bash
# From WSL:
cd /mnt/e/Projects/ipc_XMeye_camera/driver
make all
```

Outputs:
- `build/libsns_sc635hai.so` -- sensor driver shared library (~14KB)
- `build/pipeline_test` -- full pipeline test binary (~18KB)
- `build/sensor_test` -- standalone sensor test binary
- `build/ioctl_hook.so` -- LD_PRELOAD ioctl logger (debugging)

## Deploying to Camera

Use the TCP file transfer tools:

```bash
# Start recv daemon on camera (one-time per boot)
python tools/cam_cmd.py "pidof recv || (nohup /progs/rec/00/recv 8888 /progs/rec/00 -d > /dev/null 2>&1 &)"

# Send build artifacts
python tools/send_file.py 192.168.1.153 8888 driver/build/libsns_sc635hai.so driver/build/pipeline_test
```

## Testing

For the pipeline test (current focus -- see `research/PHASE3_CONTINUE.md`):

```bash
python tools/cam_cmd.py "killall superb; sleep 2; cd /progs/rec/00 && \
    LD_PRELOAD='/progs/rec/00/libbnr.so /progs/rec/00/libdrc.so /progs/rec/00/libacs.so /progs/rec/00/libcalcflicker.so /progs/rec/00/libir_auto.so /progs/rec/00/libldci.so /progs/rec/00/libdehaze.so /progs/rec/00/libextend_stats.so' \
    LD_LIBRARY_PATH=/progs/rec/00 \
    ./pipeline_test"
```

For the standalone sensor test:

```bash
python tools/cam_cmd.py "killall mySystem; killall superb; sleep 1; \
    /progs/rec/00/sensor_test 1"
```

### Test Phases (sensor_test)

| Phase | What it does | Risk |
|-------|--------------|------|
| 1 | Read chip ID via I2C | None -- read only |
| 2 | Verify register state | Low -- reads + optional writes |
| 3 | MIPI + ISP pipeline | Medium -- configures hardware |
| 4 | Full video (VPSS+VENC) | Medium -- full pipeline test |

## Architecture

The driver follows the standard HiSilicon 2-file pattern:

**`sc635hai_sensor_ctl.c`** -- hardware layer:
- I2C communication via the SDK's sensor I2C abstraction
- 374-register init sequence for 3200x1800 @ 30fps linear mode (BGGR)
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
- VTS_MAX = 0x1FFFF (17-bit), EXP_MIN = 2

## Known Limitations

1. **Linear mode only** -- WDR (vc_wdr_2t1) not implemented
2. **PQ bins loaded externally** -- ISP loads from `/home/sensor/sc635hai/pqbin/`
   on resfs. Without them, ISP runs with default tuning (functional but
   suboptimal image quality). Three ISP warnings appear at init when PQ data
   isn't loaded -- non-fatal.
3. **Black level assumed** -- 64 (10-bit typical), not confirmed against
   actual sensor calibration
4. **Frame flow** -- pipeline init succeeds end-to-end, but `vpss_get_chn_frame`
   times out. Likely needs VPSS->VENC binding to drive frame consumption.
   See `research/PHASE3_CONTINUE.md`.

## References

- `research/PHASE3_CONTINUE.md` -- Current pipeline bringup status
- `research/SC635HAI_SENSOR_ANALYSIS.md` -- Complete sensor analysis
- `research/RESEARCH.md` -- SDK, toolchain, platform documentation
- `research/sc635hai_rockchip_v4l2.c` -- Rockchip kernel driver (gain model source)
- `research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/include/hisilicon/ot_common_sns.h`
  -- V1.0.2.1 sensor callback structure definitions
- `research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/src/common/sample_comm_*.c`
  -- Reference patterns for VI/ISP/VPSS/VENC init
