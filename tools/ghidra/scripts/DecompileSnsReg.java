// Ghidra headless script: Decompile sensor reg sync path functions.
// Targets functions known by name; also follows their call graphs one level.
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import ghidra.program.model.symbol.*;
import java.io.*;
import java.util.*;

public class DecompileSnsReg extends GhidraScript {

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";
        String suffix = args.length > 1 ? args[1] : "";

        FunctionManager fm = currentProgram.getFunctionManager();
        Listing listing = currentProgram.getListing();
        DecompInterface decomp = new DecompInterface();
        decomp.setOptions(new DecompileOptions());
        decomp.openProgram(currentProgram);

        // Primary targets we want to find by exact name (case-insensitive contains)
        String[] nameSubstrings = {
            "isp_sensor_reg_callback",
            "isp_sensor_unreg_callback",
            "isp_sensor_ctx_init",
            "isp_sensor_update_all",
            "isp_sensor_update_all_yuv",
            "ot_mpi_isp_sensor_reg_callback",
            "ot_mpi_isp_sensor_unreg_callback",
            "ot_mpi_ae_sensor_reg_callback",
            "ot_mpi_awb_sensor_reg_callback",
            "isp_sensor_init_standby_cfg",
        };

        Set<Function> targets = new LinkedHashSet<>();
        for (Function f : fm.getFunctions(true)) {
            String n = f.getName().toLowerCase();
            for (String s : nameSubstrings) {
                if (n.contains(s.toLowerCase())) {
                    targets.add(f);
                    break;
                }
            }
        }

        // Follow callees one level from key registration functions
        String[] roots = {"isp_sensor_reg_callback", "ot_mpi_isp_sensor_reg_callback", "isp_sensor_ctx_init"};
        Set<Function> initialTargets = new LinkedHashSet<>(targets);
        for (Function f : initialTargets) {
            for (String r : roots) {
                if (f.getName().equalsIgnoreCase(r)) {
                    for (Function callee : f.getCalledFunctions(monitor)) targets.add(callee);
                    for (Function caller : f.getCallingFunctions(monitor)) targets.add(caller);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SNS REG SYNC PATH DECOMPILATION\n");
        sb.append("Program: ").append(currentProgram.getName()).append("\n");
        sb.append("Total target functions: ").append(targets.size()).append("\n");
        sb.append("=".repeat(70)).append("\n\n");

        for (Function func : targets) {
            Address entry = func.getEntryPoint();
            long size = func.getBody().getNumAddresses();

            sb.append("=".repeat(70)).append("\n");
            sb.append("FUNCTION: ").append(func.getName()).append("\n");
            sb.append("ADDRESS:  ").append(entry).append("\n");
            sb.append("SIZE:     ").append(size).append(" bytes\n");
            sb.append("=".repeat(70)).append("\n\n");

            // Decompile
            sb.append("--- DECOMPILED C ---\n");
            DecompileResults res = decomp.decompileFunction(func, 120, monitor);
            if (res != null && res.decompileCompleted() && res.getDecompiledFunction() != null) {
                sb.append(res.getDecompiledFunction().getC());
            } else {
                sb.append("DECOMPILATION FAILED");
                if (res != null && res.getErrorMessage() != null) {
                    sb.append(": ").append(res.getErrorMessage());
                }
            }
            sb.append("\n");

            // Calls / callers
            sb.append("--- CALLS TO ---\n");
            for (Function called : func.getCalledFunctions(monitor)) {
                sb.append("  -> ").append(called.getName()).append(" @ ").append(called.getEntryPoint()).append("\n");
            }
            sb.append("--- CALLED BY ---\n");
            for (Function caller : func.getCallingFunctions(monitor)) {
                sb.append("  <- ").append(caller.getName()).append(" @ ").append(caller.getEntryPoint()).append("\n");
            }
            sb.append("\n");
        }

        String fname = "sns_reg_registration" + (suffix.isEmpty() ? "" : "_" + suffix) + ".txt";
        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, fname)));
        pw.print(sb.toString());
        pw.close();
        println("[*] Wrote " + fname);
        decomp.dispose();
    }
}
