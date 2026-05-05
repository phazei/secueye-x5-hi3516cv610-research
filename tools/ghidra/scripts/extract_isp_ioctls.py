# Ghidra headless script: Decompile ISP-related functions to find ioctl numbers
# Targets: hi_mpi_isp_set_csc_attr, secu_sensor_brightness/contrast/saturation,
#          and any function calling ioctl with /dev/isp_dev
#
# Output: isp_ioctl_analysis.txt - decompiled C for all ISP control functions

import os
import sys
from ghidra.app.decompiler import DecompInterface
from ghidra.util.task import ConsoleTaskMonitor

args = getScriptArgs()
output_dir = args[0] if args else "."

program = getCurrentProgram()
fm = program.getFunctionManager()
listing = program.getListing()
mem = program.getMemory()
monitor = ConsoleTaskMonitor()

# Set up decompiler
decomp = DecompInterface()
decomp.openProgram(program)

print("[*] ISP ioctl analysis starting...")

# Target function name patterns
isp_targets = [
    "hi_mpi_isp_set_csc_attr",
    "hi_mpi_isp_get_csc_attr",
    "hi_mpi_isp_set_saturation_attr",
    "hi_mpi_isp_set_sharpen_attr",
    "hi_mpi_isp_set_exposure_attr",
    "hi_mpi_isp_set_wb_attr",
    "hi_mpi_isp_set_gamma_attr",
    "hi_mpi_isp_set_drc_attr",
    "hi_mpi_isp_set_dehaze_attr",
    "hi_mpi_isp_set_nr_attr",
    "ot_mpi_isp_set_csc_attr",
    "ot_mpi_isp_get_csc_attr",
    "ot_mpi_isp_set_saturation_attr",
    "ot_mpi_sys_ioctl",
]

sensor_targets = [
    "secu_sensor_brightness",
    "secu_sensor_contrast",
    "secu_sensor_saturation",
    "secu_sensor_saturate_set",
    "secu_sensor_sharpness",
    "secu_sensor_chroma",
    "secu_sensor_set_security_image_effect",
    "secu_sensor_set_nightmode",
    "secu_sensor_set_daynight_mode",
    "secu_sensor_set_ircut",
    "secu_sensor_mirror_flip",
    "secu_sensor_mirror_flip_set",
    "secu_sensor_light",
    "secu_sensor_set_fps",
    "secu_sensor_digital_wdr_set",
    "secu_sensor_drc_set",
    "secu_sensor_ae_set",
    "secu_sensor_awb",
]

# Also find any function with "ioctl" in name
ioctl_targets = []

all_targets = set()
found_funcs = {}

# Scan all functions for matches
for func in fm.getFunctions(True):
    name = func.getName()
    name_lower = name.lower()
    
    # Exact or prefix match for ISP targets
    for t in isp_targets:
        if name_lower == t or name_lower.startswith(t):
            all_targets.add(name)
            found_funcs[name] = func
    
    # Exact or prefix match for sensor targets
    for t in sensor_targets:
        if name_lower == t or name_lower.startswith(t):
            all_targets.add(name)
            found_funcs[name] = func
    
    # ioctl-related
    if "ioctl" in name_lower:
        all_targets.add(name)
        found_funcs[name] = func
    
    # Also catch mpi_sys or sys_ioctl patterns
    if "mpi_sys" in name_lower or "sys_ioctl" in name_lower:
        all_targets.add(name)
        found_funcs[name] = func

print("[*] Found %d target functions to decompile" % len(found_funcs))

# Decompile each and collect output
results = []
ioctl_constants = []

for name in sorted(found_funcs.keys()):
    func = found_funcs[name]
    addr = func.getEntryPoint()
    size = func.getBody().getNumAddresses()
    
    results.append("=" * 70)
    results.append("FUNCTION: %s" % name)
    results.append("ADDRESS:  %s" % addr)
    results.append("SIZE:     %d bytes" % size)
    results.append("=" * 70)
    
    # Decompile
    try:
        res = decomp.decompileFunction(func, 60, monitor)
        if res and res.depiledFunction():
            c_code = res.getDecompiledFunction().getC()
            results.append(c_code)
            
            # Try to extract ioctl constants from the C code
            for line in c_code.split("\n"):
                line_stripped = line.strip()
                if "ioctl" in line_stripped.lower() or "0x" in line_stripped:
                    if "ioctl" in line_stripped.lower():
                        ioctl_constants.append("%s: %s" % (name, line_stripped))
        elif res:
            c_code = res.getDecompiledFunction().getC() if res.getDecompiledFunction() else "DECOMPILATION FAILED"
            results.append(c_code)
        else:
            results.append("DECOMPILATION RETURNED NULL")
    except Exception as e:
        results.append("DECOMPILATION ERROR: %s" % str(e))
    
    results.append("")

# Write main output
outpath = os.path.join(output_dir, "isp_ioctl_analysis.txt")
with open(outpath, "w") as f:
    f.write("ISP IOCTL ANALYSIS - DECOMPILED FUNCTIONS\n")
    f.write("Binary: superb (7.8MB ARM ELF)\n")
    f.write("Functions decompiled: %d\n" % len(found_funcs))
    f.write("=" * 70 + "\n\n")
    for line in results:
        f.write(line + "\n")

# Write ioctl constants summary
constpath = os.path.join(output_dir, "ioctl_constants.txt")
with open(constpath, "w") as f:
    f.write("IOCTL CONSTANTS FOUND IN ISP FUNCTIONS\n")
    f.write("=" * 70 + "\n\n")
    for line in ioctl_constants:
        f.write(line + "\n")

print("[*] Wrote isp_ioctl_analysis.txt (%d functions)" % len(found_funcs))
print("[*] Wrote ioctl_constants.txt (%d entries)" % len(ioctl_constants))

decomp.dispose()
print("[*] extract_isp_ioctls.py complete")
