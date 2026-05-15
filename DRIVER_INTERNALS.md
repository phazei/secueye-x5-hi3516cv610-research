# Driver Internals -- Kernel Forensics Reference

Deep reference for the ISP/I2C kernel sync path. Read `DRIVER.md`
first for the canonical driver documentation. This file contains
kernel module decompilation details, binary comparison results, and
historical investigation context that you'd only need when debugging
the sync path or doing further reverse engineering.

Source: Ghidra decompilation of `ot_isp.ko` and `ot_sensor_i2c.ko`
pulled from the camera. Full analysis in
`tools/ghidra/output/sensor_i2c_kernel.md` and
`research/archive/PHASE9_ISP_I2C_SYNC.md`.

---

## ot_sensor_i2c.ko decompilation

Module identity:
- MD5: `67dfffd6b71459b1cbfdb3fc3680c6a7`
- Size: 5,088 bytes
- Path: `/home/ipc_drv/extdrv/ot_sensor_i2c.ko`
- Arch: ARM Thumb2, Linux 5.10.221 SMP

### Functions

| Offset | Name | Size | Purpose |
|--------|------|------|---------|
| 0x0001 | `ot_sensor_i2c_write` | 260 bytes | I2C dispatch |
| 0x0104 | (unnamed helper) | ~52 bytes | Internal |
| 0x0139 | `i2c_mod_init` | 304 bytes | Module init |
| 0x0269 | `i2c_mod_exit` | 164 bytes | Module cleanup |

### Imports

```
i2c_get_adapter, i2c_new_client_device, i2c_put_adapter,
i2c_unregister_device, osal_spin_lock_init/irqsave/irqrestore/destroy,
cmpi_get_module_func_by_id, bsp_i2c_master_send_mul_reg, OT_LOG
```

### Init flow (`i2c_mod_init`)

1. Creates 3 dummy I2C clients (bus 0..2) with **slave address 0x36**
   as placeholders. These are visible at `/sys/bus/i2c/devices/0-0036`.
   The real sensor address (0x30) is passed at runtime via the
   `dev_addr` argument to `ot_sensor_i2c_write`.

2. Registers the I2C write callback with the ISP framework:
   ```
   cmpi_get_module_func_by_id(28)  // 28 = OT_ID_ISP
     -> returns g_isp_exp_func table
     -> entry [0] = isp_register_bus_callback
   ```

3. Calls `isp_register_bus_callback(pipe, type=0, &fp)` for pipes 0..3.
   Type 0 = WRITE callback, type 1 = READ callback.

### `ot_sensor_i2c_write` signature

```c
int ot_sensor_i2c_write(
    u32 i2c_bus,            // r0; must be 0..2
    u16 dev_addr_8bit,      // r1; internally >> 1 to get 7-bit addr
    void *data_buf,         // r2; per-register data, 24 bytes/entry
    u32 reg_count,          // r3; must be < max_count_arg
    u8  max_count,          // sp+0
    u32 addr_byte_num,      // sp+4
    u32 data_byte_num       // sp+8
);
```

- Retries up to **6 times** per register on `-EAGAIN` (-11)
- Actual I2C transfer via `bsp_i2c_master_send_mul_reg` (HiSilicon BSP
  function, NOT the Linux i2c subsystem's `i2c_transfer`)
- `dev_addr` is 8-bit write address (e.g. 0x60 for SC635HAI); the
  function right-shifts by 1 to get the 7-bit address (0x30)

### Error strings in binary

```
"ot_sensor_i2c_write failed!(bus_num:%d, dev_addr:0x%x, reg_addr:0x%x)"
"i2c_get_adapter(%d) failed!"
"i2c_new_client_device failed!"
```

---

## ot_isp.ko sync path decompilation

Module identity:
- MD5: `1170c0c954ab460aa59a41aa2976c7dd`
- Size: 195,432 bytes
- Path: `/home/ipc_drv/ot_isp.ko`
- `.text` size: 82,620 bytes (MD5 `1574892a2c77e413749f14e1b1b5635a`)
- 941 named symbols exported

### `g_isp_exp_func` table

Location: `.data` offset 0x108, 164 bytes (41 entries).
Entry [0] = `isp_register_bus_callback`.

### `isp_register_bus_callback`

Offset: 0x6bcc, 192 bytes.

```
isp_register_bus_callback(pipe, type, &args):
  if pipe invalid -> return 0xa01c800a
  type == 0: store args->fp_write at ctx[0x7e9c]
  type == 1: store args->fp_read  at ctx[0x7ea0]
  else:      return 0xa01c8007
```

If `ctx[0x7e9c]` is NULL when `isp_drv_write_i2c_data` runs, kernel
logs: `"pfn_isp_write_i2c_data is TD_NULL point!"`

### `isp_drv_write_i2c_data`

Offset: 0x21b4, 396 bytes. The core I2C dispatch function, called from
`isp_drv_reg_config_sensor` in the ISP interrupt path (FE_END or
BE_END).

```
isp_drv_write_i2c_data(ctx, pipe, sns_regs_info, ...):
  1. Read write callback fp from ctx[0x7e9c] -- NULL check
  2. Load sns_regs_info from ctx[pipe*4 + 0x96c]
  3. For each register (0..reg_num):
     a. Call isp_drv_get_sns_cfg_node() -- cross-frame gate
        -> Returns NULL if conditions not met (silently skips)
     b. Check update flag
     c. Check is_sensor_data_has_been_config() -- skip consumed entries
     d. Batch into 60-byte packets
     e. When packet full, dispatch via fp_write (blx callback)
```

The 60-byte batching: registers are accumulated until
`sub_a + sub_b + sl_flag > 59`, then flushed to I2C.

### `isp_drv_get_sns_cfg_node` -- the cross-frame gate

Offset: 0x2108, 70 bytes. This is the function that decides whether
a register update gets dispatched to I2C or silently dropped.

```
isp_drv_get_sns_cfg_node(ctx, pipe, reg_idx):
  max_p = ctx[0x813]          // max_pipes - 1 (byte)
  cur_p = ctx[0x814]          // current_pipe_index (byte)

  // Pipe rotation: distribute regs across pipes in round-robin
  if (reg_idx % (max_p + 1)) != cur_p:
    return NULL

  slot = pipe + (reg_idx / (max_p + 1))
  if slot > 7:
    return NULL

  // Queue depth gate
  if ctx[0x7e8] <= 1:         // must be > 1
    return NULL

  // Dual-pointer equality check
  current = ctx[slot*4 + 0x96c]
  next    = ctx[slot*4 + 0x970]
  if current == NULL || next == NULL:
    return NULL
  if current != next:         // both must point to same cfg node
    return NULL

  return current
```

**Key insight:** If `cros_cnt == 0` (no cross-frame engagement), the
queue head/tail never advance, this function always returns NULL, and
no I2C writes happen. The `be_buf_num=4` and AE route fixes cause the
ISP to engage the cross-frame path, which advances the queue.

Note: `cros_cnt` itself turned out to be a red herring for
diagnostics -- it can stay 0 and the sync path still works after the
Iteration 2 fixes. The field may relate to multi-pipe sync or WDR,
not single-pipe sensor I2C delivery.

### `isp_ioctl_set_sync_cfg`

Offset: 0x7afc, 376 bytes. The userspace ioctl handler that enqueues
sensor register configurations into the kernel ring buffer.

```
isp_ioctl_set_sync_cfg(ctx, pipe, arg):
  // Validation constraints
  if arg[12] > 1:      return error    // reg_type check
  if arg[1756] > 2:    return error    // wdr_mode check
  if arg[20] < 1 or arg[20] > 8: return error  // reg_num check
  if arg[1644] > 8:    return error    // slvs_num check
  if arg[16] > 50:     return error    // max something

  // Ring buffer mechanics
  head = ctx[0x1000 + 0x3f0]    // 3-bit, wraps with & 7
  tail = ctx[0x1000 + 0x3f1]    // 3-bit, wraps with & 7
  // 8 entries deep, each 0xa60 (2656) bytes
  slot_base = ctx + pipe*0xa60 + 0x13e0 + 24

  // Memcpy 0xa60 bytes from arg into slot
  // Advance tail
```

Userspace must call this ioctl once per frame to enqueue the next
frame's sensor register configuration.

### ISP context field map (partial)

Byte offsets into the per-pipe ISP context structure, derived from
Ghidra decompilation:

| Offset | Size | Field |
|--------|------|-------|
| 0x48 | 1 | "remaining packet" flag |
| 0x813 | 1 | max_pipes - 1 |
| 0x814 | 1 | current_pipe_index |
| 0x7e8 | 1 | queue depth threshold (must be > 1) |
| 0x7e9c | 4 | I2C write callback function pointer |
| 0x7ea0 | 4 | I2C read callback function pointer |
| 0x96c..0x97c | 4x4 | per-pipe sns_cfg_node "current" pointers |
| 0x970..0x980 | 4x4 | per-pipe sns_cfg_node "next" pointers |
| +0x1000+0x3f0 | 1 | sync_cfg queue head (0..7) |
| +0x1000+0x3f1 | 1 | sync_cfg queue tail (0..7) |
| +pipe*0xa60+0x13e0+24 | 0xa60 | sync_cfg queue entry base |

### `ot_isp_i2c_data` struct size

24 bytes on ARM (`td_bool` = `int` = 4 bytes). Matches kernel's
`r9 = 24` stride in `isp_drv_write_i2c_data`.

---

## Three-way kernel module comparison

Compared three `ot_isp.ko` variants: OURS (camera), SHUMJJ (shumjj
repo), KOL (SDK V1.0.2.1 KOL package). Full analysis in
`research/archive/PHASE9_KO_DIFF.md`.

### Results

| Property | OURS | SHUMJJ | KOL |
|----------|------|--------|-----|
| File size | 195,432 | 195,496 (+64) | 195,496 (+64) |
| `.text` size | 82,620 | 82,620 | 82,620 |
| `.text` MD5 | `1574892a...` | differs | `1574892a...` (=OURS) |
| Symbols | 941 | 941 | 941 |
| `struct module` | 0x180 (384 bytes) | 0x1C0 (448 bytes) | 0x1C0 (448 bytes) |
| vermagic | `5.10.221 SMP mod_unload ARMv7 thumb2 p2v8` | same | same |
| Toolchain | GCC 10.3.0, musl 1.2.3, CS71.2.10.5.B002 | same | same |

**OURS and KOL have byte-identical `.text` sections.** They are the
same code compiled with the same toolchain.

**SHUMJJ differs from OURS in exactly 2 instructions** in
`isp_drv_write_i2c_data`: two `movw` loading `__LINE__` constants
into registers for `OT_LOG` calls, with a systematic +3 line shift
(SHUMJJ had 3 extra lines at the top of `ot_isp.c`). No functional
difference.

### The 64-byte size difference

Lives entirely in `.gnu.linkonce.this_module` -- the `struct module`
placeholder. OURS: 384 bytes, SHUMJJ/KOL: 448 bytes. The extra 64
bytes are pure zero padding from a `CONFIG_*` difference in the target
kernel build (candidates: `CONFIG_TRACEPOINTS`, `CONFIG_LIVEPATCH`,
`CONFIG_TRACING`). OURS matches the running camera kernel.

### SHUMJJ loadability

SHUMJJ modules have a broken `depends=$symbol_path` placeholder in
`.modinfo` (modpost variable substitution never ran) AND the wrong
`struct module` size. **Cannot be loaded on our camera.**

KOL modules have correct `depends=` but wrong `struct module` size.
**Also cannot be loaded.**

### `ot_sensor_i2c.ko` comparison

Same pattern: OURS and SHUMJJ differ by 5 bytes in `.text`, all
`movs r3, #imm` `__LINE__` literals with exactly +3 offset. 64-byte
file size difference from `struct module` padding. No functional
difference.

### Conclusion

The `.ko` variant hypothesis is **disproved**. The sync-path divergence
was entirely in userspace (the three structural bugs in
`cmos_get_sns_reg_info` and the missing AE/ISP configuration in
`pipeline_test.c`).

---

## Userspace library equivalence

9 ISP sync-path functions compared via Ghidra decompilation between
V1.0.2.1 `libot_mpi_isp.so` and superb's statically-linked V1.0.2.0
ISP code:

| Function | Result |
|----------|--------|
| `isp_check_sns_register` | Identical |
| `isp_sensor_get_sns_reg` | Identical |
| `isp_sensor_update_sns_reg` | Identical |
| `isp_sync_cfg_set` | Identical |
| `isp_run` | Identical |
| `isp_run_thread` | Identical |
| `ot_mpi_isp_run` | Identical |
| `isp_sensor_reg_callback` | Identical |
| `ot_mpi_isp_sensor_reg_callback` | Identical |

Only difference: `fprintf` line numbers differ by 3 (same +3 pattern
as the kernel modules). **Functionally identical.**

`isp_sensor_update_all` was also disassembled to confirm: no hidden
force-write path exists in V1.0.2.x userspace. The function only
calls `get_isp_default` and `get_isp_black_level`, neither of which
touches `regs_info`.

---

## `/proc/umap/isp` target values

Reference values for a correctly-running pipeline (matches superb):

| Field | Value | Notes |
|-------|-------|-------|
| `int_rat` | 20 | Interrupt rate = sensor FPS |
| `int_gap_t` | ~50011 us | Frame interval |
| `sync_cfg_gap` | ~50012 us | Sync config timing |
| `be_buf_num` | 4 | Set via `set_ctrl_param` |
| `hmax_times` | 17780 | Half-line time in ns |
| AE node count | 3 | |
| AE node 0 | int=8, gain=1024 | Brightest (shortest exposure) |
| AE node 1 | int=2802, gain=1024 | Max exposure, min gain |
| AE node 2 | int=2802, gain=196608 | Max exposure, max gain |
| `max_line` | 2802 | VTS - EXP_OFFSET |
| `sensor_cfg_t` | ~58 us | I2C write time per frame |

If `sensor_cfg_t` is 0 or `int_rat` doesn't match sensor FPS, the
kernel sync path is not engaged.

---

## B051 kernel ioctl reference

Command values for the V1.0.2.0 B051 kernel, verified from
`libss_mpi.so` disassembly and `ioctl_hook.so` traces.

### VI (`/dev/vi`)

| Command | Value | Struct size |
|---------|-------|-------------|
| `VI_SET_DEV_ATTR` | `0x40784900` | 120 bytes |
| `VI_ENABLE_DEV` | `0x00004902` | -- |
| `VI_DISABLE_DEV` | `0x00004903` | -- |
| `VI_BIND` | `0x4004490a` | 4 bytes |
| `VI_SET_PIPE_ATTR` | `0x40204910` | 32 bytes |
| `VI_START_PIPE` | `0x0000491e` | -- |
| `VI_STOP_PIPE` | `0x0000491f` | -- |
| `VI_SET_CHN_ATTR` | `0x402c494e` | 44 bytes |
| `VI_ENABLE_CHN` | `0x00004952` | -- |
| `VI_REG_DEV` | `0x40044961` | 4 bytes |

### ISP (`/dev/isp_dev`)

| Command | Value | Struct size |
|---------|-------|-------------|
| `ISP_REG` | `0x40047000` | 4 bytes |
| `ISP_GET_STATUS` | `0x80047022` | 4 bytes |
| `ISP_SET_PUB_ATTR` | `0x4034703a` | 52 bytes |
| `ISP_GET_PUB_ATTR` | `0x8034703b` | 52 bytes |

Note: V1.0.1.x used ioctl type `0x49` for ISP. V1.0.2.x changed to
`0x70`. This is why HIVIEW libraries are ABI-incompatible.

### VPSS (`/dev/vpss`)

| Command | Value | Struct size | Notes |
|---------|-------|-------------|-------|
| `VPSS_REG` | `0x40045000` | 4 bytes | arg = `(grp<<16)\|chn` |
| `VPSS_START_GRP` | `0x00005005` | -- | |
| `VPSS_STOP_GRP` | `0x00005006` | -- | |
| `VPSS_RESET_GRP` | `0x00005007` | -- | **Not CREATE_CHN!** |
| `VPSS_SET_CHN_ATTR` | `0x40605008` | 96 bytes | |
| `VPSS_ENABLE_CHN` | `0x0000500a` | -- | |
| `VPSS_SET_GRP_ATTR` | `0x4038500c` | 56 bytes | |
| `VPSS_DESTROY_GRP` | `0x0000500d` | -- | |

**Critical:** `0x00005007` is `VPSS_RESET_GRP`, not `VPSS_CREATE_CHN`.
Calling it between REG and SET_CHN_ATTR wipes group state. This was a
Phase 3 blocker.

### SYS (`/dev/sys`)

| Command | Value | Struct size |
|---------|-------|-------------|
| `SYS_INIT` | `0x00005900` | -- |
| `SYS_BIND` | `0x40185907` | 24 bytes |
| `SYS_GET_CHIP_ID` | `0x8004590f` | 4 bytes |
| `SYS_SET_VI_VPSS_MODE` | `0x40105910` | 16 bytes |

### MIPI RX (`/dev/ot_mipi_rx`)

All ioctls use type `'m'` (0x6d).

### VPSS struct layouts (verified)

**`ot_vpss_chn_attr`** (96 bytes):

| Field | Type | Offset |
|-------|------|--------|
| `mirror_en` | bool(4) | 0 |
| `flip_en` | bool(4) | 4 |
| `border_en` | bool(4) | 8 |
| `width` | u32 | 12 |
| `height` | u32 | 16 |
| `depth` | u32 | 20 |
| `chn_mode` | enum(4) | 24 |
| `video_format` | enum(4) | 28 |
| `dynamic_range` | enum(4) | 32 |
| `pixel_format` | enum(4) | 36 |
| `compress_mode` | enum(4) | 40 |
| `frame_rate.src` | s32 | 44 |
| `frame_rate.dst` | s32 | 48 |
| `border_attr` | 5xu32 | 52 |
| `aspect_ratio` | {enum,u32,rect} | 72 |

### `libss_mpi.so` VPSS internals (from disassembly)

- `ot_mpi_vpss_reset_grp` (0x17c2c): uses cmd `0x00005007`
- `ot_mpi_vpss_set_chn_attr` (0x182b0): uses cmd `0x40605008`
- Internal helper at 0x1778c: lazily opens `/dev/vpss` and calls REG
- SDK maintains internal fd table indexed by `(6 * grp + chn)`
- REG arg packing: `((grp & 0xFF) << 16) | (chn & 0xFF)`

---

## Historical investigation timeline

### Phase 3: Pipeline bringup

- Built first driver from SDK headers + Rockchip + Ghidra sources
- Hit B040/B051 SDK mismatch: ISP ioctl type 0x49 vs 0x70 (complete
  interface redesign). Resolved by switching to V1.0.2.1 SDK.
- Hit VPSS blocker: misidentified `0x00005007` as CREATE_CHN (was
  RESET_GRP). Resolved via `libss_mpi.so` disassembly.
- Frame timeout (`0xA0078016`) on VPSS get_chn_frame. Resolved in
  Phase 5 by completing the pipeline (VPSS->VENC bind).

### Phase 5: Color, NR, RTSP

- Discovered BGGR bayer pattern (SmartSens standard). RGGB causes
  red/blue swap.
- Discovered PQ bin overrides bayer_format to RGGB -- the "ignored
  by hardware" belief was wrong; PQ bin was silently resetting it.
- Built `awb_dump` tool to extract manufacturer AWB calibration from
  superb's running ISP. Replaced SC4336P approximation values.
- Discovered 3DNR must use VI pipe APIs, not VPSS APIs.
- Discovered NR V2 only (V1 returns ILLEGAL_PARAM).
- Discovered VPSS 3DNR returns NOT_PERM in VI_ONLINE_VPSS_OFFLINE.
- Discovered hardware watchdog was causing "~27s RTSP crash" --
  superb stopped feeding after kill. Only SETTIMEOUT works for
  feeding (KEEPALIVE returns EPERM).
- Corrected sensor framerate: 20 fps (not 15 fps from SystemCfg.ini;
  15 fps is VENC output rate). VTS=2812 confirmed via register read.
- Confirmed superb's "WDR" is ISP DRC tone-mapping, not sensor HDR.
- Integrated xop RTSP library; first H.265 stream.

### Phase 9: I2C sync path resolution

Three iterations over 2026-05-13 to 2026-05-15:

**Iteration 1 (May 13):** Identified three structural bugs in
`cmos_get_sns_reg_info` -- init path writing to wrong struct, update
flags on wrong struct, missing config check. Fixed all three. Kernel
sync path still didn't work.

**Iteration 2 (May 14):** Fixed AE defaults (`hmax_times`, `max_int_time`,
`lines_per500ms`), added three AE callbacks, set `be_buf_num=4` and
`quick_start_en=1` via `set_ctrl_param`, configured 3-node AE route.
All `/proc/umap/isp` fields now matched superb. Kernel path worked
for transient AE changes but still failed the poke test (steady-state).

**Iteration 3 (May 14 evening):** Root cause identified -- diff-based
update flags go FALSE after AE convergence. Applied force-TRUE for
exposure, gain, and group hold registers. Poke test passes.

**Outcome A validation (May 15):** Isolated diff-only test confirmed
force-TRUE is steady-state safety only. Iteration 2 sync-queue fixes
are load-bearing for transient AE response. The specific pathology
of diff-only mode: poked `EXP_H=0xAA` stays stuck indefinitely;
causes blown-out exposure in bright light, coincidentally correct in
dark, and frame-rate drop when VTS forced high.

**Hypotheses investigated and resolved:**

| Hypothesis | Result |
|-----------|--------|
| V1.0.2.0 has hidden force-write | Falsified via disassembly |
| shumjj's ot_isp.ko differs | Falsified via byte-level comparison |
| AE jitter prevents steady-state | Partially verified (AE does freeze) |
| shumjj's quick_start_en bypasses kernel | Confirmed: sc4336p has direct-I2C escape hatch when `quick_start_en && fd >= 0` |

**Why nobody hits the steady-state freeze in practice:** (1) Scene
noise keeps AE jittering, (2) AE bookkeeping fields change even when
registers don't, (3) nobody runs poke tests, (4) manual AE lock is
untested in reference drivers.

---

## Reproducible analysis

To re-run the kernel module analysis:

```bash
# Pull modules from camera
python tools/cam_cmd.py "cat /home/ipc_drv/ot_isp.ko" > ot_isp_ours.ko
python tools/cam_cmd.py "cat /home/ipc_drv/extdrv/ot_sensor_i2c.ko" > ot_sensor_i2c_ours.ko

# Disassemble (from WSL)
TOOLCHAIN=research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi
$TOOLCHAIN/bin/arm-linux-musleabi-objdump -d -j .text ot_isp_ours.ko > isp_disasm.txt
$TOOLCHAIN/bin/arm-linux-musleabi-readelf -sW ot_isp_ours.ko > isp_symbols.txt

# Ghidra project for deeper analysis
# tools/ghidra/project/ (gitignored, regenerated by analyze_superb.cmd)
# tools/ghidra/scripts/ (analysis scripts)
```
