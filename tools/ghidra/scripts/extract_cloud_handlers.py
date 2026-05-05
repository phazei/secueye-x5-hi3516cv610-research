# Ghidra headless script: Decompile cloud/MQTT property handlers
# Find how thing.service.property.set commands flow to ISP control
# Also finds XUID internal command dispatch

import os
import sys
from ghidra.app.decompiler import DecompInterface
from ghidra.util.task import ConsoleTaskMonitor

args = getScriptArgs()
output_dir = args[0] if args else "."

program = getCurrentProgram()
fm = program.getFunctionManager()
monitor = ConsoleTaskMonitor()

decomp = DecompInterface()
decomp.openProgram(program)

print("[*] Cloud handler analysis starting...")

# Patterns to match
cloud_patterns = [
    "property_set",
    "property_get",
    "thing_service",
    "alink_property",
    "linkkit_",
    "ali_iot",
    "ali_sdk",
    "ali_link",
    "mqtt_",
    "iot_",
    "cloud_",
    "danale_",
    "tutk_",
]

xuid_patterns = [
    "xuid",
    "hi_xuid",
]

control_patterns = [
    "nightvision",
    "night_vision",
    "flip_mirror",
    "set_flip",
    "set_mirror",
    "set_led",
    "alarm_attr",
    "detect_attr",
    "detect_mode",
    "stream_attr",
    "switch_resolution",
    "set_idr",
    "draw_osd",
    "reboot",
    "watchdog",
    "factory_reset",
    "upgrade",
]

found_funcs = {}

for func in fm.getFunctions(True):
    name = func.getName()
    name_lower = name.lower()
    
    matched = False
    for p in cloud_patterns + xuid_patterns + control_patterns:
        if p in name_lower:
            matched = True
            break
    
    if matched:
        found_funcs[name] = func

print("[*] Found %d cloud/control functions to decompile" % len(found_funcs))

results = []
property_handlers = []

for name in sorted(found_funcs.keys()):
    func = found_funcs[name]
    addr = func.getEntryPoint()
    size = func.getBody().getNumAddresses()
    
    results.append("=" * 70)
    results.append("FUNCTION: %s" % name)
    results.append("ADDRESS:  %s" % addr)
    results.append("SIZE:     %d bytes" % size)
    results.append("=" * 70)
    
    try:
        res = decomp.decompileFunction(func, 60, monitor)
        if res and res.getDecompiledFunction():
            c_code = res.getDecompiledFunction().getC()
            results.append(c_code)
            
            # Look for property name strings and dispatch patterns
            for line in c_code.split("\n"):
                ls = line.strip()
                if any(kw in ls for kw in ['"NightVision', '"ImageFlip', '"MotionDetect',
                    '"AlarmSwitch', '"Floodlight', '"StatusLight', '"StorageRecord',
                    '"StreamVideo', '"SubStream', '"IRLight', '"WhiteLight',
                    '"FaceDetect', '"CrossLine', '"RegionDetect', '"IvpAbility',
                    '"IntelligentTrack', '"CustomCmd', '"RebootSchedule',
                    'property', 'XUID']):
                    property_handlers.append("%s: %s" % (name, ls))
        else:
            results.append("DECOMPILATION FAILED")
    except Exception as e:
        results.append("ERROR: %s" % str(e))
    
    results.append("")

outpath = os.path.join(output_dir, "cloud_handler_analysis.txt")
with open(outpath, "w") as f:
    f.write("CLOUD / MQTT / XUID HANDLER ANALYSIS\n")
    f.write("Binary: superb\n")
    f.write("Functions decompiled: %d\n" % len(found_funcs))
    f.write("=" * 70 + "\n\n")
    for line in results:
        f.write(line + "\n")

proppath = os.path.join(output_dir, "property_dispatch.txt")
with open(proppath, "w") as f:
    f.write("PROPERTY/XUID DISPATCH REFERENCES\n")
    f.write("=" * 70 + "\n\n")
    for line in property_handlers:
        f.write(line + "\n")

print("[*] Wrote cloud_handler_analysis.txt (%d functions)" % len(found_funcs))
print("[*] Wrote property_dispatch.txt (%d entries)" % len(property_handlers))

decomp.dispose()
print("[*] extract_cloud_handlers.py complete")
