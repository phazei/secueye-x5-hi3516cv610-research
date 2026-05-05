// Ghidra headless script: Find key strings and trace xrefs with decompilation
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import ghidra.program.model.data.*;
import ghidra.program.model.symbol.*;
import java.io.*;
import java.util.*;

public class ExtractStringXrefs extends GhidraScript {

    private DecompInterface decomp;
    private Map<String, String> decompCache = new HashMap<>();

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";

        FunctionManager fm = currentProgram.getFunctionManager();
        ReferenceManager refMgr = currentProgram.getReferenceManager();
        Listing listing = currentProgram.getListing();

        decomp = new DecompInterface();
        decomp.openProgram(currentProgram);

        String[] targetStrings = {
            // Device paths
            "/dev/isp_dev", "/dev/vi_dev", "/dev/vpss_dev",
            "/dev/venc", "/dev/sys", "/dev/motor",
            // ISP strings
            "Csc Isp Saturate", "csc_attr", "set_csc",
            // Cloud properties
            "NightVisionMode", "ImageFlipState", "MotionDetectSensitivity",
            "AlarmSwitch", "IRLightBrightness", "WhiteLightBrightness",
            "FloodlightSwitch", "StatusLightSwitch", "StorageRecordMode",
            "StreamVideoQuality", "SubStreamVideoQuality", "FaceDetectSensitivity",
            // MQTT
            "thing/service/property/set", "thing/event/property/post",
            // Reboot
            "reboot", "Reboot", "watchdog",
            // XUID
            "HI_XUID_SET_NIGHTVISION", "HI_XUID_SET_FLIP",
            "HI_XUID_SET_LED", "HI_XUID_SET_ALARM",
            "HI_XUID_AI_DETECT", "HI_XUID_STREAM_ATTR",
            "HI_XUID_SLAVE_REBOOT", "HI_XUID_KEEP_ALIVE",
            // Config
            "SystemCfg.ini", "bRecEnable", "bMDEnable", "IVPEnable",
        };

        println("[*] Scanning all defined data for target strings...");

        // Build a map of target -> [(address, full_string)]
        Map<String, List<String[]>> stringMatches = new LinkedHashMap<>();
        for (String t : targetStrings) {
            stringMatches.put(t, new ArrayList<>());
        }

        // Iterate all defined data looking for strings
        DataIterator dataIter = listing.getDefinedData(true);
        int scanned = 0;
        while (dataIter.hasNext() && !monitor.isCancelled()) {
            Data data = dataIter.next();
            if (data.hasStringValue()) {
                String val = data.getDefaultValueRepresentation();
                // Strip quotes
                if (val.startsWith("\"") && val.endsWith("\"")) {
                    val = val.substring(1, val.length() - 1);
                }
                String valLower = val.toLowerCase();

                for (String target : targetStrings) {
                    if (valLower.contains(target.toLowerCase())) {
                        stringMatches.get(target).add(
                            new String[]{data.getAddress().toString(), val});
                    }
                }
            }
            scanned++;
            if (scanned % 50000 == 0) {
                println("[*] Scanned " + scanned + " data items...");
            }
        }

        int totalMatches = 0;
        for (List<String[]> v : stringMatches.values()) totalMatches += v.size();
        println("[*] Found " + totalMatches + " string matches across " + targetStrings.length + " patterns");

        // Trace xrefs and decompile callers
        StringBuilder sb = new StringBuilder();
        sb.append("STRING CROSS-REFERENCE ANALYSIS WITH DECOMPILATION\n");
        sb.append("Binary: superb\n");
        sb.append("Target strings: ").append(targetStrings.length).append("\n");
        sb.append("Total matches: ").append(totalMatches).append("\n");
        sb.append("=".repeat(70)).append("\n\n");

        for (String target : targetStrings) {
            List<String[]> matches = stringMatches.get(target);
            if (matches.isEmpty()) continue;

            sb.append("=".repeat(70)).append("\n");
            sb.append("STRING: \"").append(target).append("\"\n");
            sb.append("MATCHES: ").append(matches.size()).append("\n");
            sb.append("=".repeat(70)).append("\n\n");

            for (String[] match : matches) {
                String addrStr = match[0];
                String fullStr = match[1];
                Address addr = currentProgram.getAddressFactory().getAddress(addrStr);

                sb.append("  String at ").append(addrStr).append(": \"");
                sb.append(fullStr.length() > 120 ? fullStr.substring(0, 120) + "..." : fullStr);
                sb.append("\"\n");

                // Get references to this address
                ReferenceIterator refsIter = refMgr.getReferencesTo(addr);
                int refCount = 0;
                while (refsIter.hasNext()) {
                    Reference ref = refsIter.next();
                    refCount++;
                    Address fromAddr = ref.getFromAddress();
                    Function func = fm.getFunctionContaining(fromAddr);
                    if (func != null) {
                        sb.append("    Ref by: ").append(func.getName());
                        sb.append(" @ ").append(fromAddr).append("\n");

                        // Decompile first 2 callers per string match
                        if (refCount <= 2) {
                            String cCode = decompileFunc(func);
                            if (cCode != null) {
                                sb.append("    --- Decompiled ").append(func.getName()).append(" ---\n");
                                for (String line : cCode.split("\n")) {
                                    sb.append("    ").append(line).append("\n");
                                }
                                sb.append("    --- End ---\n");
                            }
                        }
                    } else {
                        sb.append("    Ref from: ").append(fromAddr).append(" (no function)\n");
                    }
                }
                if (refCount == 0) {
                    sb.append("    No references found\n");
                }
                sb.append("\n");
            }
        }

        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, "string_xref_analysis.txt")));
        pw.print(sb.toString());
        pw.close();

        println("[*] Wrote string_xref_analysis.txt");
        println("[*] Decompiled " + decompCache.size() + " unique functions");

        decomp.dispose();
        println("[*] ExtractStringXrefs complete");
    }

    private String decompileFunc(Function func) {
        String key = func.getEntryPoint().toString();
        if (decompCache.containsKey(key)) return decompCache.get(key);
        try {
            DecompileResults res = decomp.decompileFunction(func, 30, monitor);
            if (res != null && res.decompileCompleted() && res.getDecompiledFunction() != null) {
                String c = res.getDecompiledFunction().getC();
                decompCache.put(key, c);
                return c;
            }
        } catch (Exception e) {
            // ignore
        }
        decompCache.put(key, null);
        return null;
    }
}
