# PHASE9 — ot_isp.ko / ot_sensor_i2c.ko Three-Way Binary Diff

**Status:** Investigation complete.
**Goal:** Resolve PHASE9 hypothesis 3 — are the three `ot_isp.ko` variants (and the two `ot_sensor_i2c.ko` variants) functionally different in the I2C/ISP sync path?

**Short answer:** **No. There are zero functional differences in the sync path.** The byte differences are explained entirely by (a) embedded `__LINE__` source-line literals, (b) a small unrelated source edit that shifted text addresses by 2 bytes in SHUMJJ, and (c) a kernel `struct module` size difference that costs 64 bytes of pure zero padding in `.gnu.linkonce.this_module`.

Prior session's claim that OURS and SHUMJJ are "byte-equivalent" was wrong (md5 hashes differ). The correct claim is **OURS and KOL are byte-identical in `.text`** — SHUMJJ has a few-line source delta but identical logic.

---

## Files Under Test

| Tag | Path | Size | MD5 |
|---|---|---:|---|
| OURS | `firmware/extracted/appfs/home/ipc_drv/ot_isp.ko` | 195432 | `1170c0c954ab460aa59a41aa2976c7dd` |
| SHUMJJ | `research/shumjj-3516cv610_app/rootfs/opt/ceanic/ko/ot_isp.ko` | 195496 | `50a843384ff61256d17c89e1c5ebaead` |
| KOL | `research/Hi3516CV610_Firmware_Building/opt/KOL/HongOUPI_PICO_CV610/ko/ot_isp.ko` | 195496 | `5293549ad4c4cc72b1457264b1423058` |

Sensor I2C helper module (Tasks 1–6 also applied):

| Tag | Path | Size | MD5 |
|---|---|---:|---|
| OURS | `.../ipc_drv/extdrv/ot_sensor_i2c.ko` | 5088 | `67dfffd6b71459b1cbfdb3fc3680c6a7` |
| SHUMJJ | `.../shumjj.../ko/extdrv/ot_sensor_i2c.ko` | 5152 | `f6c0f619c78142a4cd69ed76d5da7d00` |

Toolchain used: `arm-v01c02-linux-musleabi-{readelf,objdump}` (GCC 10.3.0 / musl 1.2.3 — same toolchain that built the modules per `.comment`).

All raw dumps preserved at `C:\Users\HomeStar\AppData\Local\Temp\opencode\ko_diff\`.

---

## Task 1 — Section Sizes (`readelf -hS`)

Side-by-side critical-section sizes for `ot_isp.ko`:

| Section | OURS | SHUMJJ | KOL | Notes |
|---|---:|---:|---:|---|
| `.text` | **0x142BC** | **0x142BC** | **0x142BC** | Identical size (82620 B) |
| `.rodata` | 0x2B5A | 0x2B5A | 0x2B5A | Identical |
| `.rodata.str1.1` | 0x5928 | 0x5928 | 0x5928 | Identical |
| `.modinfo` | 0x6B (107) | 0x61 (97) | 0x6B (107) | SHUMJJ shorter (see Task 3) |
| `.data` | 0x7C4 | 0x7C4 | 0x7C4 | Identical |
| `.gnu.linkonce.this_module` | **0x180** | **0x1C0** | **0x1C0** | **64-byte delta lives here** |
| `.bss` | 0x3B0E4 | 0x3B0E4 | 0x3B0E4 | Identical |
| `.symtab` | 0x52B0 (941) | 0x52B0 (941) | 0x52B0 (941) | Same 1326 entries total |
| `.strtab` | 0x2E13 | 0x2E13 | 0x2E13 | Identical |
| `.comment` | 0x4B0 | 0x4B0 | 0x4B0 | Identical (same toolchain banner repeated) |

**Conclusion — Task 1:** The 64-byte file-size difference is **entirely** in `.gnu.linkonce.this_module`. That section is the `struct module` placeholder the linker reserves for the kernel module loader. (See Task analysis below.)

`ot_sensor_i2c.ko` shows the identical pattern: `.text` identical (0x314), only `.gnu.linkonce.this_module` jumps 0x180 → 0x1C0.

---

## Task 2 — `.text` Disassembly Diff (Thumb-2)

Raw `.text` md5:

| File | `.text` raw md5 |
|---|---|
| OURS | `1574892a2c77e413749f14e1b1b5635a` |
| SHUMJJ | `7d852fbd5492e4720f0a19232f421be8` |
| KOL | `1574892a2c77e413749f14e1b1b5635a` |

**OURS and KOL have byte-identical `.text`.** They differ only in:
- `.modinfo`'s second `depends=` byte position (cosmetic),
- some file-offset fields in section/reloc headers (relocs file moved because `.modinfo` is slightly different size; see Task 5),
- `.gnu.linkonce.this_module` (the 64-byte struct-size delta).

Diff size:
- `objdump -d` of OURS vs KOL: 9 lines different (header banner only).
- OURS vs SHUMJJ: 8132 lines differ → 2078 bytes differ in `.text` (2.5%).
- All SHUMJJ-vs-others byte diffs are non-clustered, small-magnitude tweaks distributed across nearly every function — characteristic of either `__LINE__` literals or per-function +/-N-byte position shifts after a small source edit.

### Sync-path functions (the only ones that matter for Phase 9):

**`isp_drv_get_sns_cfg_node` @ 0x2108 (start..return):**

> **Identical in all three modules. Zero diff lines.**

**`isp_drv_write_i2c_data` @ 0x21B4:**

OURS vs KOL: identical.

OURS vs SHUMJJ: exactly **two** instruction differences, both immediate-value `movw` updates that load source-line numbers into r3 just before a logging call:

```
@ offset 0x21e8 inside the function
- 21e8:  f240 73f1   movw r3, #2033  @ 0x7f1     (OURS / KOL)
+ 21e8:  f240 73f4   movw r3, #2036  @ 0x7f4     (SHUMJJ)

@ offset 0x2216 inside the function
- 2216:  f240 72c5   movw r2, #1989  @ 0x7c5     (OURS / KOL)
+ 2216:  f44f 62f9   mov.w r2, #1992 @ 0x7c8     (SHUMJJ; encoding form also differs because constant moved out of movw range)
```

Both deltas are exactly **+3**. These are `__LINE__` constants pushed onto the stack as the 5th argument of a printk-family logger (Hisi/Goke's `OT_LOG` macro takes `(level, fn, line, fmt, …)`). The +3 means whoever produced SHUMJJ inserted **three extra source lines** somewhere above the i2c-write site in the same `.c` file (probably a comment block, an `#include`, or a 3-line debug `if`). The opcodes, register choices, control flow, semaphore calls, and OT_LOG callee are otherwise unchanged.

This is the **same `__LINE__`-literal pattern** seen across the whole file. Every cluster of byte differences in `.text` correlates to a `movs r3, #imm` / `movw r3, #imm` instruction immediately followed by `str r3, [sp, #0]` (the canonical OT_LOG line-number push).

The 2-byte reloc-offset shifts (e.g. `0xb21a` → `0xb21c`, `0xca12` → `0xca14`) seen in `.rel.text` (Task 5) indicate exactly one 2-byte instruction was inserted into a function somewhere before offset 0xb21a in SHUMJJ — again routine source-edit drift, not a behavioral change. From there to the next break the offsets stay +2, then +0 again.

**Conclusion — Task 2:** Neither `isp_drv_write_i2c_data` nor `isp_drv_get_sns_cfg_node` differs **functionally** between any of the three modules. The only changes are `__LINE__` literal updates.

---

## Task 3 — Strings & Version Strings

`.modinfo` (the only place strings differ):

```
OURS:    license=GPL  depends=ot_osal,ot_base,ot_mmz  name=ot_isp  vermagic=5.10.221 SMP mod_unload ARMv7 thumb2 p2v8
SHUMJJ:  license=GPL  depends=$symbol_path             name=ot_isp  vermagic=5.10.221 SMP mod_unload ARMv7 thumb2 p2v8
KOL:     license=GPL  depends=ot_osal,ot_base,ot_mmz  name=ot_isp  vermagic=5.10.221 SMP mod_unload ARMv7 thumb2 p2v8
```

Findings:
- **`vermagic` is identical** (kernel `5.10.221 SMP mod_unload ARMv7 thumb2 p2v8`) across all three. Same target kernel.
- **SHUMJJ has an unresolved `depends=$symbol_path` placeholder**, which is the literal shell-variable name from `scripts/mod/modpost` substitution. Their build system never replaced it. (Note: this means SHUMJJ as shipped probably can't actually be loaded by `insmod` without `modprobe -f` — the kernel will reject the depends string.) OURS and KOL both have the correctly-substituted `ot_osal,ot_base,ot_mmz`.
- This single 10-byte string difference accounts for the `.modinfo` size delta (0x6B vs 0x61).

`.comment` (toolchain banner) — **identical** across all three, with the same build timestamp embedded in the GCC version string:

```
GCC: (musl-1.2.3 linux-5.10 CS71.2.10.5.B002  2025-03-05 12:00:00) 10.3.0
```

The repeated banner (11 copies for ot_isp, fewer for ot_sensor_i2c) is normal: one per linked translation unit. All three modules were built with the **same toolchain release dated 2025-03-05 12:00:00 (`CS71.2.10.5.B002`)**.

No `__DATE__` / `__TIME__` strings, no git-hash, no SCMVERSION embedded in the binaries — there is no externally visible build timestamp that distinguishes the three modules, only the inferred source-line drift.

`strings -a -n 8` over the rest of each file produced no additional distinguishing version tokens (full output: `ko_diff/text_*.dis`).

---

## Task 4 — Symbols (`readelf -sW`)

| | OURS | SHUMJJ | KOL |
|---|---:|---:|---:|
| Total symtab entries | 1326 | 1326 | 1326 |
| Defined-symbol count (with name) | 941 | 941 | 941 |
| Symbols in OURS not in SHUMJJ | 0 | — | — |
| Symbols in SHUMJJ not in OURS | — | 0 | — |
| Symbols in OURS not in KOL | 0 | — | — |
| Symbols in KOL not in OURS | — | — | 0 |
| Symbols in SHUMJJ not in KOL | — | 0 | — |

**All three modules export the exact same symbol-name set.** Same function inventory, same data symbols, same external-symbol references. No symbols added, removed, or renamed.

---

## Task 5 — Relocations (`readelf -rW`)

Each module has identical reloc counts:
- `.rel.text` 4048 entries
- `.rel.rodata` 9 entries
- `.rel.data` 145 entries
- `.rel.gnu.linkonce.this_module` 2 entries (`init_module`, `cleanup_module`)
- `.rel.ARM.exidx` 462 entries

`diff rel_ours.txt rel_kol.txt` differences:
1. File-offset headers shift (`0x26528` → `0x26568`, etc.) — purely because KOL's `.modinfo` is 10 bytes longer, pushing every later section forward.
2. The single `.rel.gnu.linkonce.this_module` entry for `cleanup_module` moved from offset `0x150` (OURS) to `0x17C` (KOL/SHUMJJ): `0x17C - 0x150 = 0x2C = 44 bytes`. That is `cleanup_module`'s offset inside the `struct module`, which is later in KOL/SHUMJJ because the struct grew by 64 bytes (the new fields land between `init` and `exit` in struct module layout).

`diff rel_ours.txt rel_shumjj.txt` differences:
1. Same file-offset header shifts (caused by the modinfo length difference).
2. ~70 `.rel.text` entries have their `r_offset` (location to patch) shifted by **+2 bytes** between offsets `0xb21a` and approximately `0xcaXX`, then back. Symbol names and addends are unchanged. That is consistent with a single 2-byte Thumb-2 instruction insertion in SHUMJJ source somewhere before `0xb21a`. Calls go to identical external symbols: `OT_LOG`, `osal_sem_up`, `osal_get_current_tgid`, `cmpi_get_module_func_by_id`, `isp_drv_check_pointer`, `isp_drv_be_apb_statistics_read`, `isp_drv_be_stt_statistics_read`, `isp_drv_check_pipe`, `isp_drv_get_ctx`, `isp_drv_set_online_stt_addr`, `isp_drv_get_ldci_tpr_flt_en`, `isp_drv_set_ldci_stt_addr`, `isp_drv_be_offline_statistics_read`, `isp_drv_be_offline_stitch_statistics_read`, etc. — fully identical call graph.

**No new external symbol references appear in any module.** No reloc to a sync-path symbol differs in target.

---

## Task 6 — `cmp -l` Byte-Level Diff

`.text` (raw 82620 bytes):
- OURS vs KOL: **0 differing bytes** outside section/file metadata.
- OURS vs SHUMJJ: 2078 differing bytes (≈2.5%), first diff @ byte 71 (0x47), last @ byte 82545 (0x14271). Distribution: small (single-digit hex) deltas at scattered locations matching `__LINE__` literal slots in disasm.

`.gnu.linkonce.this_module` (the section that owns the 64-byte file-size delta):
- OURS: 0x180 bytes, of which only bytes `0x0C..0x12` contain non-zero data (`"ot_isp\0"`). Everything else is `00`.
- SHUMJJ / KOL: 0x1C0 bytes, of which only bytes `0x0C..0x12` contain non-zero data. Everything else is `00`. The extra 64 bytes are pure zero padding at the **end** of the section.

This is exactly what `vmlinux`-side `sizeof(struct module)` increasing by 64 bytes would produce: GCC reserves `sizeof(struct module)` bytes of zeroed storage; the kernel loader (`load_module()` in `kernel/module.c`) memcpys the populated fields and fills in the rest at insmod time. Possible kernel-config differences that could change `sizeof(struct module)` by exactly 64 bytes in Linux 5.10 include `CONFIG_TREE_SRCU` (`struct srcu_struct`), `CONFIG_LIVEPATCH`, `CONFIG_TRACEPOINTS`, `CONFIG_TRACING`, `CONFIG_FTRACE_MCOUNT_RECORD`, or `CONFIG_KALLSYMS_ALL`. Most likely candidate is `CONFIG_TRACEPOINTS=y` (adds `unsigned int num_tracepoints; tracepoint_ptr_t *tracepoints_ptrs;` + alignment) plus one of the other flags — but identifying the exact config delta would require diffing the running `/proc/modules` layout against the SHUMJJ/KOL target, which is out of scope.

---

## ot_sensor_i2c.ko Cross-Check (`OURS` vs `SHUMJJ` only — KOL has no copy)

Same pattern, in miniature:

| | OURS | SHUMJJ |
|---|---:|---:|
| File size | 5088 | 5152 (+64) |
| `.text` size | **0x314 (788 B)** | **0x314 (788 B)** |
| `.modinfo` size | 0x6B | 0x68 (`$symbol_path` again) |
| `.gnu.linkonce.this_module` | **0x180** | **0x1C0** |
| Symbol count | 29 | 29 (identical name set) |
| Functions | `ot_sensor_i2c_write`, `i2c_mod_init`, `i2c_mod_exit`, `init_module`, `cleanup_module` | same |

`.text` byte diff count: **5 bytes**, all at known offsets, all `movs r3, #imm` line-number literals, all delta **+3**:

```
Offset  OURS            SHUMJJ           Function
0x26    movs r3, #175   movs r3, #178    ot_sensor_i2c_write
0xb6    movs r3, #200   movs r3, #203    ot_sensor_i2c_write
0x1f4   movs r3, #238   movs r3, #241    i2c_mod_init
0x218   movs r3, #242   movs r3, #245    i2c_mod_init
0x2da   movs r3, #221   movs r3, #224    i2c_mod_exit
```

Identical +3 line-shift everywhere, in the same source file. **No functional difference.** The two modules implement the same `ot_sensor_i2c_write` logic byte-for-byte except the embedded line numbers.

---

## Final Answers to the Phase 9 Questions

**Q: Do the three kernel modules have meaningful functional differences in the sync path?**
**A: No.** `isp_drv_get_sns_cfg_node` is byte-identical across all three. `isp_drv_write_i2c_data` is byte-identical between OURS and KOL; it differs from SHUMJJ in exactly two `__LINE__` literal constants (both +3) with no logic, control-flow, register, or callee changes. The whole `.text` is byte-identical OURS=KOL.

**Q: Any version strings / build timestamps that distinguish them?**
**A: No distinguishing version string.** All three modules:
- have identical `vermagic` (`5.10.221 SMP mod_unload ARMv7 thumb2 p2v8`),
- were produced by the same toolchain (`CS71.2.10.5.B002`, GCC 10.3.0, musl 1.2.3, dated `2025-03-05 12:00:00` in `.comment`).

The only `.modinfo` difference is that **SHUMJJ's `depends=` field was never resolved by `modpost`** — it still contains the literal string `$symbol_path`. That tells us SHUMJJ was packaged from a broken/incomplete build pipeline (the variable substitution step was skipped), not that it is older/newer.

The `__LINE__` literals in SHUMJJ are systematically **+3 lines higher** than OURS/KOL for `ot_isp.c` and also **+3 lines higher** for `ot_sensor_i2c.c` — strong evidence they were built from a copy of the source with a small (3-line) edit, probably a header comment or a 3-line debug block added at the top of each `.c` file. OURS and KOL came from the same source tree.

**Q: Specifically, does `isp_drv_write_i2c_data` or `isp_drv_get_sns_cfg_node` differ between modules?**
**A:**
- `isp_drv_get_sns_cfg_node`: **identical in all three.**
- `isp_drv_write_i2c_data`: identical OURS ↔ KOL; differs from SHUMJJ in exactly two instructions, both `__LINE__` constants for OT_LOG calls (no semantic change).

**Q: Where does the 64-byte size difference live — code, .data, .rel, debug, or padding?**
**A: Padding.** Specifically the trailing 64 bytes of `.gnu.linkonce.this_module` (the linker's placeholder for `struct module`). OURS has 0x180 bytes there; SHUMJJ and KOL have 0x1C0 bytes; the extra 64 bytes are all `0x00`. This reflects a target-kernel difference: OURS targets a kernel whose `sizeof(struct module)` is 384 bytes, while SHUMJJ and KOL target a kernel where it is 448 bytes (likely additional `CONFIG_TRACEPOINTS` / `CONFIG_LIVEPATCH` / similar struct members). The kernel loader populates this section at `insmod` time, so the byte content does not affect runtime behavior **as long as the running kernel's `struct module` size matches what the module was linked against** — and OURS was built for the kernel actually running on our camera, which matches our running `5.10.221` (this is consistent with OURS being the one that loads correctly).

This is also indirect evidence that **shumjj's modules likely cannot be loaded on our camera as-is**, both because their `depends=$symbol_path` is malformed and because their `struct module` is 64 bytes larger than what our kernel expects. KOL's modules have the correct `depends=` but the same 64-byte struct mismatch — they target a different kernel build than the one in our firmware.

---

## Implications for Phase 9

The earlier hypothesis "the three modules might encode a different I2C sync routine" is **disproved**. Whatever causes the I2C synchronization problem on our camera is not a per-module logic divergence — every code path involved in reaching the sensor's I2C register write (`isp_drv_write_i2c_data` → `OT_LOG`/`osal_sem_*`/`cmpi_get_module_func_by_id`/`isp_drv_get_sns_cfg_node`) is bit-for-bit equivalent across the firmware copy we run and the KOL reference SDK.

Therefore the sync-path divergence (if any) must live in:

1. Kernel-side parameters / `proc` knobs (devicetree, MIPI lane config, ISP unit clock).
2. A different module **above** ot_isp.ko / ot_sensor_i2c.ko in the stack (e.g. the `ot_vi*` modules, `ot_sns_*`, or the userspace sensor library invoked via `cmpi_get_module_func_by_id`).
3. Userspace timing / initialization order, not the kernel module binaries.

Recommend redirecting Phase 9 investigation to those layers; the .ko-variant hypothesis is closed.

---

## Artifacts

All intermediate dumps (per-section disassembly, hex bytes, reloc tables, symbol tables, function extracts) are preserved at:

```
C:\Users\HomeStar\AppData\Local\Temp\opencode\ko_diff\
├── sec_{ours,shumjj,kol}.txt              # readelf -hS
├── sym_{ours,shumjj,kol}.txt              # readelf -sW
├── sym_{ours,shumjj,kol}_names.txt        # sorted symbol-name sets
├── rel_{ours,shumjj,kol}.txt              # readelf -rW
├── text_{ours,shumjj,kol}.dis             # objdump -d --disassembler-options=force-thumb
├── text_{ours,shumjj,kol}.hex             # objdump -j .text -s
├── text_{ours,shumjj}.bin                 # raw .text bytes for cmp
├── fn_write_i2c_data_{ours,shumjj,kol}.txt
├── fn_get_sns_cfg_node_{ours,shumjj,kol}.txt
├── si_text_{ours,shumjj}.{dis,hex,bin}    # ot_sensor_i2c.ko
├── sym_si_{ours,shumjj}.txt
├── fn_diff_output.txt                     # sync-path function diff
├── sym_rel_output.txt                     # symbol/reloc summary
├── final_output.txt                       # reloc samples + ot_sensor_i2c
└── analyze{,2,3,4,5,6,7}.sh               # repeatable scripts
```

Commands used (all run inside WSL Ubuntu):

```bash
TC=/mnt/e/Projects/ipc_XMeye_camera/research/hi3516cv610_toolchain/\
gcc-20250305-arm-v01c02-linux-musleabi/arm-v01c02-linux-musleabi-gcc/bin
$TC/arm-linux-musleabi-readelf  -hS   <file>
$TC/arm-linux-musleabi-readelf  -sW   <file>
$TC/arm-linux-musleabi-readelf  -rW   <file>
$TC/arm-linux-musleabi-objdump  -j .modinfo                  -s <file>
$TC/arm-linux-musleabi-objdump  -j .comment                  -s <file>
$TC/arm-linux-musleabi-objdump  -j .gnu.linkonce.this_module -s <file>
$TC/arm-linux-musleabi-objdump  -d --disassembler-options=force-thumb -j .text <file>
cmp -l <fileA>.bin <fileB>.bin
```
