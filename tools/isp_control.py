#!/usr/bin/env python3
"""
ISP CSC (Color Space Conversion) Control via bspmm register access
For SECUEYE X5 / Hi3516CV610

Based on Ghidra decompilation of mpi_isp_set_csc_attr() in superb:
  - CSC registers are at ISP base + (pipe+8)*0x20000 + 0x12xx
  - For pipe 0: offset 0x100000 + 0x12xx from ISP base
  - ISP base physical address: 0x17800000 (Hi3516CV610)
  - So CSC registers at: 0x178012xx

Register map (from decompiled mpi_isp_set_csc_attr):
  +0x12F0: CSC Enable (byte, bit 0) 
  +0x12E0: Color Mode (byte, 0-3: 0=BT601, 1=BT709, 2=user, 3=?)
  +0x12F4: Brightness (byte, 0-100)
  +0x12F1: Contrast   (byte, 0-100)
  +0x12F2: Hue        (byte, 0-100)
  +0x12F3: Saturation (byte, 0-100)
  +0x12F5: Limited Range Y (byte, bool)
  +0x12F6: Limited Range C (byte, bool)
  +0x12F7: Extended Range  (byte, bool)
  +0x12F8: Update trigger  (write 1 to apply)

Usage:
  python isp_control.py read           - Read current CSC values
  python isp_control.py set brightness 60
  python isp_control.py set contrast 55
  python isp_control.py set saturation 50
  python isp_control.py set hue 50
  python isp_control.py reset          - Reset to defaults (50/50/50/50)
"""

import socket
import sys
import time

CAMERA_IP = "192.168.1.153"
CAMERA_PORT = 9999

# The ISP register base is at 0x17800000.
# For pipe 0, CSC block is at 0x17800000 + (0+8)*0x20000 = 0x17800000 + 0x100000 = 0x17900000
# Wait -- that would be VPSS territory. Let me reconsider.
#
# Actually, the io_write8 function in the MPP library maps the ISP virtual registers.
# The calculation (pipe + 8) * 0x20000 is an offset into the ISP's MMIO space.
# The ISP dev reg base for pipe 0 is typically at a fixed offset.
#
# Let's try multiple possible bases:
# Option A: ISP at 0x17800000, CSC at 0x17800000 + 0x1012xx (too high)
# Option B: ISP dev reg at a known offset
#
# From the OpenIPC/HiSilicon SDK for Hi3516CV610:
# The ISP CSC registers are typically at ISP_REG_BASE + 0x12xx per-pipe block
# For this chip, the per-pipe stride is 0x20000
# Pipe 0 base: ISP_REG_BASE + 8*0x20000 = ISP_REG_BASE + 0x100000
#
# But we need to figure out the actual base. Let's probe with bspmm.

# CSC register offsets (within the pipe block)
CSC_OFFSETS = {
    "enable":      0x12F0,
    "color_mode":  0x12E0,
    "brightness":  0x12F4,
    "contrast":    0x12F1,
    "hue":         0x12F2,
    "saturation":  0x12F3,
    "lim_range_y": 0x12F5,
    "lim_range_c": 0x12F6,
    "ext_range":   0x12F7,
    "update":      0x12F8,
}

# Possible ISP base addresses to probe
# The decompiled code: (pipe + 8) * 0x20000 suggests this is an offset
# into a pre-mapped region. We need to find the physical base.
# On Hi3516CV610, typical ISP virtual register base candidates:
PROBE_BASES = [
    0x17800000,   # ISP base from our bspmm scans
    0x17900000,   # VPSS base  
    0x17000000,   # Another common base
    0x11200000,   # Alternative ISP register area
    0x11300000,   # Alternative
    0x17100000,   # ISP FE
]


def cam_cmd(cmd, timeout=3):
    """Send command to camera shell and get response"""
    s = socket.socket()
    s.settimeout(timeout)
    s.connect((CAMERA_IP, CAMERA_PORT))
    # Read welcome banner
    time.sleep(0.3)
    try:
        s.recv(4096)
    except:
        pass
    s.send((cmd + "\n").encode())
    time.sleep(0.5)
    result = b""
    while True:
        try:
            data = s.recv(4096)
            if not data:
                break
            result += data
        except socket.timeout:
            break
    s.close()
    return result.decode(errors="replace")


def bspmm_read(addr):
    """Read a 32-bit register via bspmm"""
    result = cam_cmd(f"bspmm 0x{addr:08x}")
    # Parse output: "0xADDR = 0xVALUE"
    for line in result.split("\n"):
        if "=" in line and "0x" in line:
            parts = line.split("=")
            if len(parts) >= 2:
                try:
                    return int(parts[1].strip(), 16)
                except ValueError:
                    pass
    return None


def bspmm_write(addr, value):
    """Write a 32-bit register via bspmm"""
    result = cam_cmd(f"bspmm 0x{addr:08x} 0x{value:08x}")
    return result


def probe_isp_base():
    """Try to find the ISP CSC register base by probing known addresses"""
    print("Probing for ISP CSC registers...")
    print()
    
    for base in PROBE_BASES:
        # For pipe 0, the per-pipe block starts at base + 0x100000
        pipe_base = base + 0x100000
        csc_enable_addr = pipe_base + 0x12F0
        
        print(f"  Trying base 0x{base:08X} -> CSC enable at 0x{csc_enable_addr:08X} ... ", end="", flush=True)
        val = bspmm_read(csc_enable_addr)
        if val is not None:
            print(f"0x{val:08X}  {'<-- CSC enabled!' if val & 1 else ''}")
            # If we got a value, read more CSC registers to verify
            if val is not None and val != 0xFFFFFFFF:
                bright = bspmm_read(pipe_base + 0x12F4)
                contrast = bspmm_read(pipe_base + 0x12F1)
                sat = bspmm_read(pipe_base + 0x12F3)
                hue = bspmm_read(pipe_base + 0x12F2)
                print(f"    Brightness: {bright}, Contrast: {contrast}, Saturation: {sat}, Hue: {hue}")
                if bright is not None and 0 <= (bright & 0xFF) <= 100:
                    print(f"  *** FOUND! ISP base = 0x{base:08X}, pipe block = 0x{pipe_base:08X}")
                    return pipe_base
        else:
            print("bus error / no response")
    
    # Also try without the +0x100000 offset (maybe the pipe block is directly at ISP base)
    print("\n  Trying direct (no pipe offset)...")
    for base in PROBE_BASES:
        csc_enable_addr = base + 0x12F0
        print(f"  Trying 0x{base:08X} + 0x12F0 = 0x{csc_enable_addr:08X} ... ", end="", flush=True)
        val = bspmm_read(csc_enable_addr)
        if val is not None:
            print(f"0x{val:08X}")
            if val is not None and val != 0xFFFFFFFF:
                bright = bspmm_read(base + 0x12F4)
                contrast = bspmm_read(base + 0x12F1)
                print(f"    Brightness: {bright}, Contrast: {contrast}")
                if bright is not None and 0 <= (bright & 0xFF) <= 100:
                    print(f"  *** FOUND! Pipe block = 0x{base:08X}")
                    return base
        else:
            print("bus error / no response")
    
    return None


def read_csc(pipe_base):
    """Read all CSC values"""
    print(f"\nCSC Registers (pipe block at 0x{pipe_base:08X}):")
    print("-" * 50)
    
    for name, offset in sorted(CSC_OFFSETS.items(), key=lambda x: x[1]):
        addr = pipe_base + offset
        val = bspmm_read(addr)
        if val is not None:
            byte_val = val & 0xFF
            print(f"  {name:15s} [0x{addr:08X}] = 0x{val:08X} ({byte_val})")
        else:
            print(f"  {name:15s} [0x{addr:08X}] = READ ERROR")


def set_csc_value(pipe_base, field, value):
    """Set a single CSC value"""
    if field not in CSC_OFFSETS:
        print(f"Unknown field: {field}")
        print(f"Valid fields: {', '.join(CSC_OFFSETS.keys())}")
        return False
    
    if not (0 <= value <= 100):
        print(f"Value must be 0-100, got {value}")
        return False
    
    addr = pipe_base + CSC_OFFSETS[field]
    
    # Read current 32-bit value at aligned address
    aligned_addr = addr & ~3
    current = bspmm_read(aligned_addr)
    if current is None:
        print(f"Failed to read current value at 0x{aligned_addr:08X}")
        return False
    
    # Modify the specific byte within the 32-bit word
    byte_offset = addr & 3
    mask = 0xFF << (byte_offset * 8)
    new_val = (current & ~mask) | (value << (byte_offset * 8))
    
    print(f"Setting {field} = {value}")
    print(f"  Register 0x{aligned_addr:08X}: 0x{current:08X} -> 0x{new_val:08X}")
    
    bspmm_write(aligned_addr, new_val)
    
    # Trigger update
    update_addr = pipe_base + CSC_OFFSETS["update"]
    aligned_update = update_addr & ~3
    bspmm_write(aligned_update, 1)
    
    # Verify
    time.sleep(0.2)
    verify = bspmm_read(aligned_addr)
    if verify is not None:
        verify_byte = (verify >> (byte_offset * 8)) & 0xFF
        print(f"  Verified: {field} = {verify_byte}")
        return verify_byte == value
    return False


def main():
    if len(sys.argv) < 2:
        print(__doc__)
        return
    
    cmd = sys.argv[1].lower()
    
    if cmd == "probe":
        pipe_base = probe_isp_base()
        if pipe_base:
            read_csc(pipe_base)
        else:
            print("\nCould not find ISP CSC registers.")
            print("The register base may be different on this chip.")
    
    elif cmd == "read":
        pipe_base = probe_isp_base()
        if pipe_base:
            read_csc(pipe_base)
        else:
            print("Could not find ISP CSC registers. Run 'probe' first.")
    
    elif cmd == "set":
        if len(sys.argv) < 4:
            print("Usage: python isp_control.py set <field> <value>")
            print(f"Fields: {', '.join(CSC_OFFSETS.keys())}")
            return
        
        field = sys.argv[2].lower()
        value = int(sys.argv[3])
        
        pipe_base = probe_isp_base()
        if pipe_base:
            set_csc_value(pipe_base, field, value)
            print("\nCurrent values:")
            read_csc(pipe_base)
        else:
            print("Could not find ISP CSC registers.")
    
    elif cmd == "reset":
        pipe_base = probe_isp_base()
        if pipe_base:
            print("Resetting CSC to defaults (50/50/50/50)...")
            for field, val in [("brightness", 50), ("contrast", 50), 
                               ("saturation", 50), ("hue", 50)]:
                set_csc_value(pipe_base, field, val)
            print("\nDone. Current values:")
            read_csc(pipe_base)
    
    else:
        print(f"Unknown command: {cmd}")
        print("Commands: probe, read, set, reset")


if __name__ == "__main__":
    main()
