// Ghidra headless script: Extract all named functions with addresses and sizes
// Run via: analyzeHeadless ... -postScript ExtractFunctions.java <output_dir>
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import java.io.*;
import java.util.*;

public class ExtractFunctions extends GhidraScript {

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";

        FunctionManager fm = currentProgram.getFunctionManager();
        int total = 0;
        List<String> allFuncs = new ArrayList<>();
        List<String> ispFuncs = new ArrayList<>();
        List<String> sensorFuncs = new ArrayList<>();
        List<String> cloudFuncs = new ArrayList<>();
        List<String> xuidFuncs = new ArrayList<>();
        List<String> ioctlFuncs = new ArrayList<>();
        List<String> interestingFuncs = new ArrayList<>();

        String[] ispPrefixes = {"hi_mpi_isp_", "ot_mpi_isp_", "hi_mpi_vi_", "ot_mpi_vi_",
            "hi_mpi_vpss_", "ot_mpi_vpss_", "hi_mpi_venc_", "ot_mpi_venc_",
            "hi_mpi_sys_", "ot_mpi_sys_", "hi_mpi_ai_", "ot_mpi_ai_",
            "hi_mpi_ao_", "ot_mpi_ao_", "hi_mpi_aenc_", "ot_mpi_aenc_",
            "hi_mpi_adec_", "ot_mpi_adec_"};
        String[] sensorPrefixes = {"secu_sensor_"};
        String[] cloudKeywords = {"mqtt", "ali_", "iot_", "cloud", "thing_", "property",
            "alink", "linkkit", "danale", "tutk", "p2p"};
        String[] xuidKeywords = {"xuid", "hi_xuid"};
        String[] interestingKeywords = {"reboot", "watchdog", "upgrade", "update", "factory",
            "reset", "record", "motion", "detect", "alarm", "night", "ircut",
            "flip", "mirror", "osd", "ptz", "motor", "audio", "speak",
            "brightness", "contrast", "saturation", "sharpness",
            "csc", "exposure", "white_balance", "gamma", "drc", "wdr",
            "encrypt", "decrypt", "password", "license"};

        FunctionIterator funcIter = fm.getFunctions(true);
        while (funcIter.hasNext() && !monitor.isCancelled()) {
            Function func = funcIter.next();
            String name = func.getName();
            String addr = func.getEntryPoint().toString();
            long size = func.getBody().getNumAddresses();
            String entry = addr + "\t" + size + "\t" + name;
            String nameLower = name.toLowerCase();

            allFuncs.add(entry);
            total++;

            for (String p : ispPrefixes) {
                if (nameLower.startsWith(p)) { ispFuncs.add(entry); break; }
            }
            for (String p : sensorPrefixes) {
                if (nameLower.startsWith(p)) { sensorFuncs.add(entry); break; }
            }
            boolean isCloud = false;
            for (String kw : cloudKeywords) {
                if (nameLower.contains(kw)) { cloudFuncs.add(entry); isCloud = true; break; }
            }
            for (String kw : xuidKeywords) {
                if (nameLower.contains(kw)) { xuidFuncs.add(entry); break; }
            }
            if (nameLower.contains("ioctl")) {
                ioctlFuncs.add(entry);
            }
            for (String kw : interestingKeywords) {
                if (nameLower.contains(kw)) { interestingFuncs.add(entry); break; }
            }
        }

        println("[*] Total functions: " + total);
        println("[*] ISP: " + ispFuncs.size() + ", Sensor: " + sensorFuncs.size() +
                ", Cloud: " + cloudFuncs.size() + ", XUID: " + xuidFuncs.size() +
                ", ioctl: " + ioctlFuncs.size() + ", Interesting: " + interestingFuncs.size());

        writeList(outputDir, "functions_all.txt", "All named functions", allFuncs);
        writeList(outputDir, "functions_isp.txt", "ISP/MPP API functions", ispFuncs);
        writeList(outputDir, "functions_sensor.txt", "Sensor control (secu_sensor_*)", sensorFuncs);
        writeList(outputDir, "functions_cloud.txt", "Cloud/MQTT/IoT functions", cloudFuncs);
        writeList(outputDir, "functions_xuid.txt", "Internal IPC (XUID) handlers", xuidFuncs);
        writeList(outputDir, "functions_ioctl.txt", "ioctl-related functions", ioctlFuncs);
        writeList(outputDir, "functions_interesting.txt", "Other interesting functions", interestingFuncs);

        // Summary
        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, "functions_summary.txt")));
        pw.println("SUPERB BINARY FUNCTION ANALYSIS SUMMARY");
        pw.println("=".repeat(60));
        pw.println();
        pw.println("Binary: " + currentProgram.getName());
        pw.println("Image base: " + currentProgram.getImageBase());
        pw.println("Language: " + currentProgram.getLanguageID());
        pw.println("Compiler: " + currentProgram.getCompilerSpec().getCompilerSpecID());
        pw.println();
        pw.println("Function counts:");
        pw.println("  Total named:     " + total);
        pw.println("  ISP/MPP API:     " + ispFuncs.size());
        pw.println("  Sensor control:  " + sensorFuncs.size());
        pw.println("  Cloud/MQTT/IoT:  " + cloudFuncs.size());
        pw.println("  XUID IPC:        " + xuidFuncs.size());
        pw.println("  ioctl-related:   " + ioctlFuncs.size());
        pw.println("  Interesting:     " + interestingFuncs.size());
        pw.close();

        println("[*] ExtractFunctions complete");
    }

    private void writeList(String dir, String filename, String header, List<String> items) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(new File(dir, filename)));
        pw.println("# " + header);
        pw.println("# Address\tSize\tName");
        pw.println("# Extracted from: " + currentProgram.getName());
        pw.println();
        for (String item : items) {
            pw.println(item);
        }
        pw.close();
        println("[*] Wrote " + filename + " (" + items.size() + " entries)");
    }
}
