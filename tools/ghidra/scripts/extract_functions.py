# Ghidra headless script: Extract all named functions with addresses and sizes
# Usage: Run via analyzeHeadless -postScript extract_functions.py <output_dir>
#
# Outputs:
#   functions_all.txt       - All named functions (addr, size, name)
#   functions_isp.txt       - ISP-related functions (hi_mpi_isp_*, secu_sensor_*, ot_mpi_*)
#   functions_cloud.txt     - Cloud/MQTT-related functions
#   functions_xuid.txt      - Internal IPC command handlers (HI_XUID_*)
#   functions_summary.txt   - Statistics summary

import os
import sys

args = getScriptArgs()
output_dir = args[0] if args else "."

program = getCurrentProgram()
fm = program.getFunctionManager()
listing = program.getListing()

print("[*] Extracting functions from: %s" % program.getName())

all_funcs = []
isp_funcs = []
cloud_funcs = []
xuid_funcs = []
ioctl_funcs = []
sensor_funcs = []
misc_interesting = []

isp_prefixes = ("hi_mpi_isp_", "ot_mpi_isp_", "hi_mpi_vi_", "ot_mpi_vi_",
                "hi_mpi_vpss_", "ot_mpi_vpss_", "hi_mpi_venc_", "ot_mpi_venc_",
                "hi_mpi_sys_", "ot_mpi_sys_")
sensor_prefixes = ("secu_sensor_",)
cloud_keywords = ("mqtt", "ali_", "iot_", "cloud", "thing_", "property",
                   "alink", "linkkit", "danale", "tutk", "p2p")
xuid_keywords = ("xuid", "HI_XUID", "hi_xuid")
ioctl_keywords = ("ioctl",)

func = fm.getFunctionAt(program.getMinAddress())
if func is None:
    func_iter = fm.getFunctions(True)
else:
    func_iter = fm.getFunctions(True)

count = 0
for func in func_iter:
    name = func.getName()
    addr = func.getEntryPoint()
    body = func.getBody()
    size = body.getNumAddresses()
    
    entry = "%s\t%d\t%s" % (addr, size, name)
    all_funcs.append(entry)
    count += 1
    
    name_lower = name.lower()
    
    # Categorize
    if any(name_lower.startswith(p) for p in isp_prefixes):
        isp_funcs.append(entry)
    
    if any(name_lower.startswith(p) for p in sensor_prefixes):
        sensor_funcs.append(entry)
    
    if any(kw in name_lower for kw in cloud_keywords):
        cloud_funcs.append(entry)
    
    if any(kw in name_lower for kw in xuid_keywords):
        xuid_funcs.append(entry)
    
    if any(kw in name_lower for kw in ioctl_keywords):
        ioctl_funcs.append(entry)
    
    # Other interesting functions
    for kw in ("reboot", "watchdog", "upgrade", "update", "factory", "reset",
               "record", "motion", "detect", "alarm", "night", "ircut",
               "flip", "mirror", "osd", "ptz", "motor", "audio", "speak",
               "brightness", "contrast", "saturation", "sharpness",
               "csc", "exposure", "white_balance", "gamma", "drc", "wdr",
               "encrypt", "decrypt", "password", "license", "key"):
        if kw in name_lower:
            misc_interesting.append(entry)
            break

print("[*] Total functions: %d" % count)
print("[*] ISP functions: %d" % len(isp_funcs))
print("[*] Sensor functions: %d" % len(sensor_funcs))
print("[*] Cloud functions: %d" % len(cloud_funcs))
print("[*] XUID functions: %d" % len(xuid_funcs))
print("[*] ioctl functions: %d" % len(ioctl_funcs))
print("[*] Other interesting: %d" % len(misc_interesting))

# Write output files
def write_list(filename, header, items):
    path = os.path.join(output_dir, filename)
    with open(path, "w") as f:
        f.write("# %s\n" % header)
        f.write("# Address\tSize\tName\n")
        f.write("# Extracted from: %s\n\n" % program.getName())
        for item in items:
            f.write(item + "\n")
    print("[*] Wrote %s (%d entries)" % (filename, len(items)))

write_list("functions_all.txt", "All named functions", all_funcs)
write_list("functions_isp.txt",
           "ISP/MPP functions (hi_mpi_isp_*, ot_mpi_*)",
           isp_funcs + sensor_funcs)
write_list("functions_cloud.txt",
           "Cloud/MQTT/IoT functions",
           cloud_funcs)
write_list("functions_xuid.txt",
           "Internal IPC (XUID) command handlers",
           xuid_funcs)
write_list("functions_ioctl.txt",
           "ioctl-related functions",
           ioctl_funcs)
write_list("functions_interesting.txt",
           "Other interesting functions (reboot, detect, record, etc.)",
           misc_interesting)

# Summary
summary_path = os.path.join(output_dir, "functions_summary.txt")
with open(summary_path, "w") as f:
    f.write("SUPERB BINARY FUNCTION ANALYSIS SUMMARY\n")
    f.write("=" * 60 + "\n\n")
    f.write("Binary: %s\n" % program.getName())
    f.write("Image base: %s\n" % program.getImageBase())
    f.write("Language: %s\n" % program.getLanguageID())
    f.write("Compiler: %s\n" % program.getCompilerSpec().getCompilerSpecID())
    f.write("\nFunction counts:\n")
    f.write("  Total named functions:  %d\n" % count)
    f.write("  ISP/MPP API:           %d\n" % len(isp_funcs))
    f.write("  Sensor control:        %d\n" % len(sensor_funcs))
    f.write("  Cloud/MQTT/IoT:        %d\n" % len(cloud_funcs))
    f.write("  XUID IPC handlers:     %d\n" % len(xuid_funcs))
    f.write("  ioctl-related:         %d\n" % len(ioctl_funcs))
    f.write("  Other interesting:     %d\n" % len(misc_interesting))

print("[*] Summary written to functions_summary.txt")
print("[*] extract_functions.py complete")
