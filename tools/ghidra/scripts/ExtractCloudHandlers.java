// Ghidra headless script: Decompile cloud/MQTT/XUID handler functions
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import java.io.*;
import java.util.*;

public class ExtractCloudHandlers extends GhidraScript {

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";

        FunctionManager fm = currentProgram.getFunctionManager();
        DecompInterface decomp = new DecompInterface();
        decomp.openProgram(currentProgram);

        String[] patterns = {
            "property_set", "property_get", "thing_service",
            "alink_", "linkkit_", "ali_iot", "ali_sdk", "ali_link",
            "mqtt_", "iot_dm_", "iot_gateway",
            "cloud_", "danale_", "tutk_",
            "xuid", "hi_xuid",
            "nightvision", "night_vision",
            "flip_mirror", "set_flip", "set_mirror",
            "set_led", "alarm_attr", "detect_attr", "detect_mode",
            "stream_attr", "switch_resolution", "set_idr",
            "draw_osd", "factory_reset",
            "slave_reboot", "keep_alive",
        };

        Map<String, Function> targets = new TreeMap<>();

        FunctionIterator funcIter = fm.getFunctions(true);
        while (funcIter.hasNext() && !monitor.isCancelled()) {
            Function func = funcIter.next();
            String name = func.getName();
            String nameLower = name.toLowerCase();

            for (String p : patterns) {
                if (nameLower.contains(p)) {
                    targets.put(name, func);
                    break;
                }
            }
        }

        println("[*] Found " + targets.size() + " cloud/control functions to decompile");

        StringBuilder sb = new StringBuilder();
        List<String> propertyRefs = new ArrayList<>();

        sb.append("CLOUD / MQTT / XUID HANDLER ANALYSIS\n");
        sb.append("Binary: superb\n");
        sb.append("Functions decompiled: ").append(targets.size()).append("\n");
        sb.append("=".repeat(70)).append("\n\n");

        for (Map.Entry<String, Function> entry : targets.entrySet()) {
            String name = entry.getKey();
            Function func = entry.getValue();

            sb.append("=".repeat(70)).append("\n");
            sb.append("FUNCTION: ").append(name).append("\n");
            sb.append("ADDRESS:  ").append(func.getEntryPoint()).append("\n");
            sb.append("SIZE:     ").append(func.getBody().getNumAddresses()).append(" bytes\n");
            sb.append("=".repeat(70)).append("\n");

            DecompileResults res = decomp.decompileFunction(func, 60, monitor);
            if (res != null && res.decompileCompleted() && res.getDecompiledFunction() != null) {
                String cCode = res.getDecompiledFunction().getC();
                sb.append(cCode).append("\n");

                // Look for property name references
                String[] propKeywords = {
                    "NightVision", "ImageFlip", "MotionDetect", "AlarmSwitch",
                    "Floodlight", "StatusLight", "StorageRecord", "StreamVideo",
                    "SubStream", "IRLight", "WhiteLight", "FaceDetect",
                    "CrossLine", "RegionDetect", "IvpAbility", "IntelligentTrack",
                    "CustomCmd", "RebootSchedule", "XUID", "property"
                };
                for (String line : cCode.split("\n")) {
                    String trimmed = line.trim();
                    for (String kw : propKeywords) {
                        if (trimmed.contains(kw)) {
                            propertyRefs.add(name + ": " + trimmed);
                            break;
                        }
                    }
                }
            } else {
                sb.append("DECOMPILATION FAILED\n");
            }
            sb.append("\n");
        }

        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, "cloud_handler_analysis.txt")));
        pw.print(sb.toString());
        pw.close();

        pw = new PrintWriter(new FileWriter(new File(outputDir, "property_dispatch.txt")));
        pw.println("PROPERTY/XUID DISPATCH REFERENCES");
        pw.println("=".repeat(70));
        pw.println();
        for (String line : propertyRefs) {
            pw.println(line);
        }
        pw.close();

        println("[*] Wrote cloud_handler_analysis.txt (" + targets.size() + " functions)");
        println("[*] Wrote property_dispatch.txt (" + propertyRefs.size() + " entries)");

        decomp.dispose();
        println("[*] ExtractCloudHandlers complete");
    }
}
