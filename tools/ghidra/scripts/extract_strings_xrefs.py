# Ghidra headless script: Find key strings and their cross-references
# Traces from string -> calling function -> decompile callers
# Focus: ISP control strings, cloud property names, ioctl paths, reboot triggers

import os
import sys
from ghidra.app.decompiler import DecompInterface
from ghidra.util.task import ConsoleTaskMonitor
from ghidra.program.util import DefinedDataIterator

args = getScriptArgs()
output_dir = args[0] if args else "."

program = getCurrentProgram()
fm = program.getFunctionManager()
listing = program.getListing()
refMgr = program.getReferenceManager()
monitor = ConsoleTaskMonitor()

decomp = DecompInterface()
decomp.openProgram(program)

print("[*] String xref analysis starting...")

# Key strings to trace
target_strings = [
    # ISP device paths
    "/dev/isp_dev",
    "/dev/vi_dev",
    "/dev/vpss_dev",
    "/dev/venc",
    "/dev/sys",
    "/dev/motor",
    # ISP error/debug messages  
    "Csc Isp Saturate",
    "csc_attr",
    "set_csc",
    "brightness",
    "contrast",
    "saturation",
    "sharpness",
    # Cloud properties
    "NightVisionMode",
    "ImageFlipState",
    "MotionDetectSensitivity",
    "AlarmSwitch",
    "IRLightBrightness",
    "WhiteLightBrightness",
    "FloodlightSwitch",
    "StatusLightSwitch",
    "StorageRecordMode",
    "StreamVideoQuality",
    "SubStreamVideoQuality",
    "FaceDetectSensitivity",
    # MQTT topics
    "thing/service/property/set",
    "thing/event/property/post",
    # Reboot/crash related
    "reboot",
    "Reboot",
    "REBOOT",
    "watchdog",
    "wdt",
    # XUID commands
    "HI_XUID_SET_NIGHTVISION",
    "HI_XUID_SET_FLIP",
    "HI_XUID_SET_LED",
    "HI_XUID_SET_ALARM",
    "HI_XUID_AI_DETECT",
    "HI_XUID_STREAM_ATTR",
    "HI_XUID_SLAVE_REBOOT",
    "HI_XUID_KEEP_ALIVE",
    # Config
    "SystemCfg.ini",
    "bRecEnable",
    "bMDEnable",
    "IVPEnable",
    # Process
    "superb",
    "mySystem",
]

results = []
decompiled_cache = {}

def decompile_func(func):
    """Decompile a function, using cache"""
    key = func.getEntryPoint().toString()
    if key in decompiled_cache:
        return decompiled_cache[key]
    try:
        res = decomp.decompileFunction(func, 30, monitor)
        if res and res.getDecompiledFunction():
            c_code = res.getDecompiledFunction().getC()
            decompiled_cache[key] = c_code
            return c_code
    except:
        pass
    decompiled_cache[key] = None
    return None

# Search through all defined strings in the program
print("[*] Scanning defined strings...")
string_matches = {}

for data in DefinedDataIterator.definedStrings(program):
    val = data.getDefaultValueRepresentation()
    # Remove quotes
    if val.startswith('"') and val.endswith('"'):
        val = val[1:-1]
    
    for target in target_strings:
        if target.lower() in val.lower():
            addr = data.getAddress()
            if target not in string_matches:
                string_matches[target] = []
            string_matches[target].append((addr, val))

print("[*] Found %d string pattern matches" % sum(len(v) for v in string_matches.values()))

# For each matched string, find xrefs and decompile the calling functions
for target in target_strings:
    if target not in string_matches:
        continue
    
    matches = string_matches[target]
    results.append("=" * 70)
    results.append("STRING PATTERN: \"%s\"" % target)
    results.append("MATCHES: %d" % len(matches))
    results.append("=" * 70)
    
    for addr, val in matches:
        results.append("")
        results.append("  String at %s: \"%s\"" % (addr, val[:100]))
        
        # Get references to this string
        refs = refMgr.getReferencesTo(addr)
        ref_count = 0
        for ref in refs:
            ref_count += 1
            from_addr = ref.getFromAddress()
            func = fm.getFunctionContaining(from_addr)
            if func:
                results.append("    Referenced by: %s @ %s" % (func.getName(), from_addr))
                
                # Decompile the referencing function (limit to avoid huge output)
                if ref_count <= 3:  # Only decompile first 3 callers per string
                    c_code = decompile_func(func)
                    if c_code:
                        # Include just a snippet around the string reference
                        lines = c_code.split("\n")
                        results.append("    --- Decompiled %s ---" % func.getName())
                        for line in lines:
                            results.append("    " + line)
                        results.append("    --- End ---")
            else:
                results.append("    Referenced from: %s (no function)" % from_addr)
        
        if ref_count == 0:
            results.append("    No references found")
    
    results.append("")

# Write output
outpath = os.path.join(output_dir, "string_xref_analysis.txt")
with open(outpath, "w") as f:
    f.write("STRING CROSS-REFERENCE ANALYSIS WITH DECOMPILATION\n")
    f.write("Binary: superb\n")
    f.write("Target strings searched: %d\n" % len(target_strings))
    f.write("Strings found: %d\n" % len(string_matches))
    f.write("Functions decompiled: %d\n" % len(decompiled_cache))
    f.write("=" * 70 + "\n\n")
    for line in results:
        f.write(line + "\n")

print("[*] Wrote string_xref_analysis.txt")
print("[*] Decompiled %d unique functions" % len(decompiled_cache))

decomp.dispose()
print("[*] extract_strings_xrefs.py complete")
