# Kernel Sync Path Forensics (ot_isp.ko + ot_sensor_i2c.ko)

Decompilation & analysis 2026-05-14. Modules pulled from running camera at
`/home/ipc_drv/extdrv/ot_sensor_i2c.ko` (md5 67dfffd6b71459b1cbfdb3fc3680c6a7,
5088 bytes) and `/home/ipc_drv/ot_isp.ko` (md5 1170c0c954ab460aa59a41aa2976c7dd,
195432 bytes). Both ARM Thumb2, Linux 5.10.221 SMP.

## ot_sensor_i2c.ko

Tiny module. Just three functions plus init/exit.

### Symbols
- `ot_sensor_i2c_write` (0x0001, 260 bytes) -- the registered I2C write callback
- `i2c_mod_init` (0x0139, 304 bytes) -- module init
- `i2c_mod_exit` (0x0269, 164 bytes) -- module exit
- helper at 0x0104 (unnamed) -- cleanup loop

Imports: `i2c_get_adapter`, `i2c_new_client_device`, `i2c_put_adapter`,
`i2c_unregister_device`, `osal_spin_lock_init`, `osal_spin_lock_irqsave`,
`osal_spin_unlock_irqrestore`, `osal_spin_lock_destroy`,
`cmpi_get_module_func_by_id`, `bsp_i2c_master_send_mul_reg`, `OT_LOG`.

### Strings (notable)
- `ot_dev_isp_register` -- name as referenced; internal symbol in `ot_isp.ko`
  is `isp_register_bus_callback`
- `ot_dev_isp_unregister` -- the `ot_isp.ko` symbol is `isp_register_piris_callback`'s
  sibling at exp_func[1]; that's actually weird, ignore for now.
- `ot_sensor_i2c_write` -- callback name
- Errors:
  - `i2c dev %u error` -- bsp_i2c_master_send returned wrong byte count
  - `bsp_i2c_master_send_mul_reg error, ret=%d.`
  - `i2c:%d get adapter error!` -- get_adapter returned NULL at init
  - `register i2c_write_callback to isp[%d] failed!`
  - `register i2c_write_callback to isp failed, ot_i2c init is failed!`
  - `unregister i2c_write_callback to isp failed, ot_i2c exit is failed!`

### Init Flow (`i2c_mod_init`)

```c
for (bus = 0; bus < 3; bus++) {
    adapter = i2c_get_adapter(bus);
    if (!adapter) {
        printk("i2c:%d get adapter error!", bus);
        continue;
    }
    g_clients[bus] = i2c_new_client_device(adapter, &g_i2c_board_info[bus]);
    i2c_put_adapter(adapter);
    ret = osal_spin_lock_init(&g_locks[bus]);
    if (ret != 0) {
        printk("osal_spin_lock_init failed");
        // cleanup, return -1
    }
}

// Now register the callback per pipe
isp_funcs = cmpi_get_module_func_by_id(28);   // 28 = OT_ID_ISP
if (!isp_funcs || !isp_funcs->register_bus_callback) {
    printk("register i2c_write_callback to isp failed, ot_i2c init is failed!");
    return -1;
}

reg_args = { .fp_write = ot_sensor_i2c_write, .fp_read = NULL };
for (pipe = 0; pipe < 4; pipe++) {
    ret = isp_register_bus_callback(pipe, /*type=*/0, &reg_args);
    if (ret) {
        printk("register i2c_write_callback to isp[%d] failed!", pipe);
    }
}
printk("load sensor_i2c.ko ....OK!");
```

Notes:
- `type=0` registers the WRITE callback. type=1 would register a READ
  callback at `ctx[0x7ea0]`.
- The 3 i2c bus clients are created merely to "occupy" Linux i2c addresses.
  Actual transfers go through `bsp_i2c_master_send_mul_reg` (HiSilicon BSP),
  not via the Linux i2c subsystem.
- The dummy i2c clients use slave address **0x36** (visible in
  `/sys/bus/i2c/devices/0-0036`). Real sensor is at 0x30; address used for
  transfer comes from the runtime `dev_addr` arg passed by the ISP.

### `ot_sensor_i2c_write` Signature

```c
int ot_sensor_i2c_write(
    u32 i2c_bus,            // r0; must be 0..2
    u16 dev_addr_8bit,      // r1; >> 1 applied to get 7-bit addr stored in client->addr
    void *data_buf,         // r2; per-register data, 24 bytes/entry
    u32 reg_count,          // r3; must be < max_count_arg (r8)
    /* on stack: */
    u8  max_count,          // sp+0 (offset +56 in callee frame after prologue)
    u32 addr_byte_num,      // sp+4
    u32 data_byte_num);     // sp+8
```

Loops up to 6 retries on error -11 (-EAGAIN) per register.

## ot_isp.ko: Sync Path

### Global function table `g_isp_exp_func` (.data offset 0x108, 164 bytes = 41 entries)

`cmpi_get_module_func_by_id(28)` returns a pointer to this table.
Entry [0] = `isp_register_bus_callback`. Following entries: `isp_register_piris_callback`,
`isp_get_dcf_info`, `isp_get_frame_info`, ... (see `.rel.data` relocs in
isp_relocs.txt).

### `isp_register_bus_callback(pipe, type, &args)` (0x6bcc, 192 bytes)

```c
int isp_register_bus_callback(int pipe, int type, struct {void *fp_write; void *fp_read;} *args)
{
    if (!isp_drv_check_pointer(args)) return -1;
    if (isp_drv_check_pipe(pipe) != 0) return 0xa01c800a;
    ctx = isp_drv_get_ctx(pipe);
    lock = isp_drv_get_lock(pipe);
    spin_lock_irqsave(lock);
    switch (type) {
        case 0:  ctx[0x7e9c] = args->fp_write;  break;
        case 1:  ctx[0x7ea0] = args->fp_read;   break;
        default: spin_unlock; printk("type %d not supported"); return 0xa01c8007;
    }
    spin_unlock;
    return 0;
}
```

### `isp_drv_write_i2c_data(ctx, pipe, sns_regs_info, ?, ?, ...)` (0x21b4, 396 bytes)

```c
int isp_drv_write_i2c_data(isp_ctx *ctx, int pipe, sns_regs_info *info, ...)
{
    fp_write = ctx[0x7e9c];
    if (!fp_write) {
        OT_LOG("pfn_isp_write_i2c_data is TD_NULL point!");
        return -1;
    }
    sns = ctx[pipe*4 + 0x96c];   // per-pipe sns_regs_info ptr
    if (!isp_drv_check_pointer(sns)) {
        // silent skip (return 0)
        // ctx[0x48] reset to 0
        isp_dfx_sns_sync_cfg_show(ctx, ..., ..., sl);
        return 0;
    }
    int sl_flag = 0;  // [sp+55]
    for (idx = 0; idx < sns->reg_num; idx++) {
        if (is_sensor_data_has_been_config(sns, idx, ???) == 1) {
            // already consumed; advance
            continue;
        }
        // compute reg_addr offset: sns->data[idx].reg_addr - sl
        addr_off = sns->data[idx].reg_addr - sl;  // truncated u8
        cfg_node = isp_drv_get_sns_cfg_node(ctx, pipe, addr_off);
        if (!cfg_node) continue;
        // check update flags and delay match
        node_entry = cfg_node + idx*24;
        if (node_entry[+16] == 1 && node_entry[+21] == arg_at_sp36)
            ; // matched
        else if (ctx[0x48] != 1)
            continue;
        // build a 60-byte struct on stack from node fields
        sub_a = node[+52];   // [sp+20]
        sub_b = node[+60];   // [sp+24]
        if (sub_a + sub_b + sl_flag > 59) {
            // package full: dispatch
            (*fp_write)(bus, dev_addr, packet, count, max, addr_byte, data_byte);
            sl_flag = 0;
        }
        // accumulate into packet
        memcpy(packet_at_sp24 + offset, node, 60);
    }
    // final dispatch if non-empty
    if (sl_flag) {
        (*fp_write)(bus, dev_addr, packet, count, max, addr_byte, data_byte);
        ctx[0x48] = 0;
    }
    if (ctx[0x48] == 1) {
        // remaining flush?
    }
    isp_dfx_sns_sync_cfg_show(ctx, ..., sl);  // debug show
    return 0;
}
```

Caller is `isp_drv_reg_config_sensor` at offset ~0x26ec. That function is
called from the ISP interrupt path (FE_END or BE_END) during cross-frame
processing.

### `isp_drv_get_sns_cfg_node(ctx, pipe, reg_idx)` (0x2108, 70 bytes)

```c
sns_cfg_node *isp_drv_get_sns_cfg_node(isp_ctx *ctx, int pipe, u8 reg_idx)
{
    u8 max_p = ctx[0x813];          // max pipe count (0..3?)
    u8 cur_p = ctx[0x814];          // current pipe
    int rot = reg_idx % (max_p + 1);
    if (cur_p != rot) return NULL;          // pipe-rotation gate
    int slot = pipe + rot;
    if (slot > 7) return NULL;
    u8 depth = ctx[0x7e8];
    sns_cfg_node *cur = ctx[slot*4 + 0x96c];
    sns_cfg_node *nxt = ctx[slot*4 + 0x970];
    if (!cur || !nxt) return NULL;
    if (cur != nxt) return NULL;             // head==tail gate
    if (depth <= 1) return NULL;             // queue depth gate
    return cur;
}
```

This is the silent-skip path. Returns NULL whenever:
- Cross-pipe rotation doesn't match this frame's expected pipe
- Slot index out of bounds
- Either current or next slot is NULL
- Current and next slots disagree (queue not "synced" to a stable state)
- Queue depth too low

**If `cros_cnt == 0` (no cross-frame events ever), the queue head/tail never
advance, this function always returns NULL, no I2C writes happen, sensor_cfg_t
stays small (just the gate cost).** This is consistent with our observation.

### `isp_ioctl_set_sync_cfg(filp, arg, &pipe)` (0x7afc, 376 bytes)

```c
int isp_ioctl_set_sync_cfg(struct file *filp, void *arg, int *pipe_ptr)
{
    int pipe = *pipe_ptr;
    if (isp_drv_check_pipe(pipe) != 0) return 0xa01c8007;
    if (isp_drv_check_pipe(pipe) != 0) return 0xa01c8007;  // duplicate
    if (!isp_drv_check_pointer(arg)) return 0xa01c800a;
    ctx = isp_drv_get_ctx(pipe);
    isp_state = ctx + 0x1000;
    lock = isp_drv_get_lock(pipe);
    spin_lock_irqsave(lock);
    head = isp_state[0x3f0];
    tail = isp_state[0x3f1];
    if (((head + 1) & 7) == tail) {
        // queue full
        OT_LOG("queue full");
        spin_unlock;
        return -1;
    }
    // validate arg fields
    if (arg[12] > 1                  ||
        arg[1756] > 2                ||
        ((arg[20] & 0xff) - 1) > 7   ||  // arg[20] in 1..8
        (arg[1644] & 0xff) > 8       ||
        arg[16] > 50) {
        spin_unlock;
        OT_LOG("invalid arg ...");
        return -1;
    }
    // memcpy into the ring at slot (head) -- 0xa60 bytes per entry
    slot_base = ctx + pipe*0xa60 + 0x13e0 + 24;  // approximate
    memcpy_s(slot_base, 0xa60, arg, 0xa60);
    spin_lock_irqsave(lock);
    isp_state[0x3f0] = (head + 1) & 7;     // advance head
    spin_unlock;
    return 0;
}
```

Queue is **8 entries deep** (head/tail 3-bit wrap). Slot size 0xa60 = 2656
bytes. Userspace must call this once per frame to enqueue the next-frame
sensor config.

## ctx Field Map (Partial)

Offsets within `isp_drv_get_ctx(pipe)` return value:

| Offset | Field (guessed) |
|--------|-----------------|
| 0x48 | "remaining packet" flag (cleared by `isp_drv_write_i2c_data`) |
| 0x813 | max_pipes - 1 (byte) |
| 0x814 | current_pipe_index (byte) |
| 0x96c..0x97c | per-pipe sns_cfg_node ptr "current" (4 slots, 4 bytes each) |
| 0x970..0x980 | per-pipe sns_cfg_node ptr "next" (4 slots, 4 bytes each) |
| 0x7e8 | queue depth threshold (byte) |
| 0x7e9c | i2c write callback fp |
| 0x7ea0 | i2c read callback fp |
| +0x1000 + 0x3f0 | sync_cfg queue head (byte, 0..7) |
| +0x1000 + 0x3f1 | sync_cfg queue tail (byte, 0..7) |
| +pipe*0xa60 + 0x13e0 + 24 | base of sync_cfg queue entries (~2656 bytes each) |

## Key Conclusion

The kernel-side machinery is well-structured and complete. The mystery is
**what advances ctx[0x814] (`current_pipe`) and what populates both
`ctx[slot*4+0x96c]` (current) and `ctx[slot*4+0x970]` (next) slots so they
are non-NULL AND equal**. That advancement is part of the "cross frame"
interrupt processing in `isp_drv_int_status_process`.

Comparison of `/proc/umap/isp` between our pipeline and superb shows
**`cros_cnt: 1` vs `13+ and growing`** -- the cross-frame interrupt
handling fires 13+ times for superb but only once (at init) for us. This
strongly implicates whatever userspace state controls when the kernel ISP
considers a frame interrupt as a "cross-frame" event.

## Update 2026-05-14 (Iteration 2): cros_cnt STILL 0 after extensive fixes

After applying all of the following userspace fixes -- such that every
field in `/proc/umap/isp` matches superb EXCEPT `cros_cnt` and `int_type`
-- the kernel I2C write callback still does not fire (verified via poke
test: sensor regs poked with marker stay poked indefinitely when direct
I2C in `cmos_inttime_update` is disabled).

Fixes applied:

1. `cmos_get_ae_default` now writes `hmax_times` (= 1e9 / (VTS*fps) = 17780)
2. `max_int_time = VTS - 10` (single-line max), not `2*VTS - 10`
3. Missing AE fields: `full_lines_max`, `max_int_time_target`,
   `max/min_again_target`, `max/min_dgain_target`, `min_isp_dgain_target`
4. `pfn_cmos_ae_quick_start_status_set` callback added (resets sync_init)
5. `pfn_cmos_ae_fast_ae_attr_get/set` callbacks added
6. `ss_mpi_isp_set_ctrl_param` called BEFORE `mem_init`:
   `be_buf_num = 4`, `quick_start_en = 1`
7. `ss_mpi_isp_set_ae_route_attr` called after `isp_init` with 3 nodes
   matching superb: int_time {8, 2804, 2804}, sys_gain {1024, 1024, 196608}
8. `bin_param.stIspEvo.enable = 1` for `OT_PQ_BIN_ImportBinData`

Side-by-side `/proc/umap/isp` matches superb on:
`int_rat=20`, `int_gap_t=50011us`, `sync_cfg_gap=50011us`, `be_buf_num=4`,
`hmax_times=17780`, AE 3-node route, `max_line=2802`.

Side-by-side still differs:
- `cros_cnt = 0` (vs superb's 13+ growing)
- `int_type = start` (vs superb's `other`)

**Conclusion: cross-frame engagement is NOT controlled by AE or ISP
ctrl_param config we've found.** Must be either:

- a VI pipe attribute (not yet compared between us/superb at `/proc/umap/vi`)
- a MIPI RX config (lane count, data rate, sync mode)
- a missing call to an ISP ioctl we haven't tried
- OR: kernel I2C IS engaging but `regs_info[i].update` flags stay FALSE
  because the values in `regs_info[0]` already match the sensor's
  current state (which had been written by direct I2C in a previous run
  -- the sensor power-on state persists). **NEXT TEST**: force
  `i2c_data[i].update = TD_TRUE` unconditionally in `cmos_get_sns_reg_info`
  to make every reg write every frame, with direct I2C disabled.

## Reproducible Analysis

```bash
# Pull modules off camera
python tools/cam_cmd.py "cp /home/ipc_drv/extdrv/ot_sensor_i2c.ko /tmp/; cp /home/ipc_drv/ot_isp.ko /tmp/"
python tools/send_file.py ... -recv  # or base64 + decode

# Disassemble (WSL)
TC=/mnt/e/Projects/ipc_XMeye_camera/research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/arm-v01c02-linux-musleabi-gcc/bin
$TC/arm-linux-musleabi-objdump -d --disassembler-options=force-thumb ot_isp.ko > isp_disasm.txt
$TC/arm-linux-musleabi-objdump -d --disassembler-options=force-thumb ot_sensor_i2c.ko
$TC/arm-linux-musleabi-readelf -sW ot_isp.ko
$TC/arm-linux-musleabi-readelf -rW ot_isp.ko  # for .rel.data => g_isp_exp_func entries
```
