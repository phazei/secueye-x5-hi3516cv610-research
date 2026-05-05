// Ghidra headless script: Decompile ISP + sensor functions to find ioctl numbers
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import java.io.*;
import java.util.*;

public class ExtractISP extends GhidraScript {

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";

        FunctionManager fm = currentProgram.getFunctionManager();
        DecompInterface decomp = new DecompInterface();
        decomp.openProgram(currentProgram);

        // Target patterns
        String[] patterns = {
            "hi_mpi_isp_set_csc", "hi_mpi_isp_get_csc",
            "ot_mpi_isp_set_csc", "ot_mpi_isp_get_csc",
            "hi_mpi_isp_set_saturation", "ot_mpi_isp_set_saturation",
            "hi_mpi_isp_set_sharpen", "ot_mpi_isp_set_sharpen",
            "hi_mpi_isp_set_exposure", "ot_mpi_isp_set_exposure",
            "hi_mpi_isp_set_wb", "ot_mpi_isp_set_wb",
            "hi_mpi_isp_set_gamma", "hi_mpi_isp_set_drc",
            "hi_mpi_isp_set_dehaze", "hi_mpi_isp_set_nr",
            "hi_mpi_sys_ioctl", "ot_mpi_sys_ioctl",
            "secu_sensor_brightness", "secu_sensor_contrast",
            "secu_sensor_saturation", "secu_sensor_saturate",
            "secu_sensor_sharpness", "secu_sensor_chroma",
            "secu_sensor_set_security_image_effect",
            "secu_sensor_set_nightmode", "secu_sensor_set_daynight",
            "secu_sensor_set_ircut", "secu_sensor_mirror_flip",
            "secu_sensor_light", "secu_sensor_set_fps",
            "secu_sensor_digital_wdr", "secu_sensor_drc",
            "secu_sensor_ae_set", "secu_sensor_awb",
            "secu_sensor_rotate", "secu_sensor_antifog",
            "secu_sensor_nocolour",
        };

        // Also match any function with "ioctl" in name
        Map<String, Function> targets = new TreeMap<>();

        FunctionIterator funcIter = fm.getFunctions(true);
        while (funcIter.hasNext() && !monitor.isCancelled()) {
            Function func = funcIter.next();
            String name = func.getName();
            String nameLower = name.toLowerCase();

            for (String p : patterns) {
                if (nameLower.startsWith(p) || nameLower.equals(p)) {
                    targets.put(name, func);
                    break;
                }
            }
            if (nameLower.contains("ioctl")) {
                targets.put(name, func);
            }
            if (nameLower.contains("mpi_sys") && !targets.containsKey(name)) {
                targets.put(name, func);
            }
        }

        println("[*] Found " + targets.size() + " ISP/ioctl functions to decompile");

        StringBuilder sb = new StringBuilder();
        List<String> ioctlConstants = new ArrayList<>();

        sb.append("ISP IOCTL ANALYSIS - DECOMPILED FUNCTIONS\n");
        sb.append("Binary: superb (7.8MB ARM ELF)\n");
        sb.append("Functions decompiled: ").append(targets.size()).append("\n");
        sb.append("=".repeat(70)).append("\n\n");

        for (Map.Entry<String, Function> entry : targets.entrySet()) {
            String name = entry.getKey();
            Function func = entry.getValue();
            String addr = func.getEntryPoint().toString();
            long size = func.getBody().getNumAddresses();

            sb.append("=".repeat(70)).append("\n");
            sb.append("FUNCTION: ").append(name).append("\n");
            sb.append("ADDRESS:  ").append(addr).append("\n");
            sb.append("SIZE:     ").append(size).append(" bytes\n");
            sb.append("=".repeat(70)).append("\n");

            DecompileResults res = decomp.decompileFunction(func, 60, monitor);
            if (res != null && res.decompileCompleted() && res.getDecompiledFunction() != null) {
                String cCode = res.getDecompiledFunction().getC();
                sb.append(cCode).append("\n");

                // Extract ioctl-related lines
                for (String line : cCode.split("\n")) {
                    String trimmed = line.trim();
                    if (trimmed.toLowerCase().contains("ioctl")) {
                        ioctlConstants.add(name + ": " + trimmed);
                    }
                }
            } else {
                sb.append("DECOMPILATION FAILED\n");
                if (res != null && res.getErrorMessage() != null) {
                    sb.append("Error: ").append(res.getErrorMessage()).append("\n");
                }
            }
            sb.append("\n");
        }

        // Write main output
        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, "isp_ioctl_analysis.txt")));
        pw.print(sb.toString());
        pw.close();

        // Write ioctl constants
        pw = new PrintWriter(new FileWriter(new File(outputDir, "ioctl_constants.txt")));
        pw.println("IOCTL CONSTANTS FOUND IN ISP FUNCTIONS");
        pw.println("=".repeat(70));
        pw.println();
        for (String line : ioctlConstants) {
            pw.println(line);
        }
        pw.close();

        println("[*] Wrote isp_ioctl_analysis.txt (" + targets.size() + " functions)");
        println("[*] Wrote ioctl_constants.txt (" + ioctlConstants.size() + " entries)");

        decomp.dispose();
        println("[*] ExtractISP complete");
    }
}
