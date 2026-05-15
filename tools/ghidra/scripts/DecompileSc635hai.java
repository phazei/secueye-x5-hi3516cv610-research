// Decompile SC635HAI sensor driver functions from superb.
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import java.io.*;
import java.util.*;

public class DecompileSc635hai extends GhidraScript {
    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";
        String suffix = args.length > 1 ? args[1] : "";

        FunctionManager fm = currentProgram.getFunctionManager();
        DecompInterface decomp = new DecompInterface();
        decomp.openProgram(currentProgram);

        String[] nameSubstrings = {
            "sc635hai_",
            "sc235hai_",
            "cmos_get_sns_reg_info",
            "cmos_sns_init",
            "cmos_inttime_update",
            "cmos_gains_update",
            "cmos_sc635hai",
            "cmos_sc235hai",
            "g_sns_sc635hai",
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

        StringBuilder sb = new StringBuilder();
        sb.append("SC635HAI DRIVER DECOMPILATION\n");
        sb.append("Program: ").append(currentProgram.getName()).append("\n");
        sb.append("Functions: ").append(targets.size()).append("\n");
        sb.append("=".repeat(70)).append("\n\n");

        for (Function func : targets) {
            sb.append("=".repeat(70)).append("\n");
            sb.append("FUNCTION: ").append(func.getName()).append("\n");
            sb.append("ADDRESS:  ").append(func.getEntryPoint()).append("\n");
            sb.append("SIZE:     ").append(func.getBody().getNumAddresses()).append(" bytes\n");
            sb.append("=".repeat(70)).append("\n\n");

            DecompileResults res = decomp.decompileFunction(func, 60, monitor);
            if (res != null && res.decompileCompleted() && res.getDecompiledFunction() != null) {
                sb.append(res.getDecompiledFunction().getC());
            } else {
                sb.append("DECOMPILATION FAILED\n");
            }
            sb.append("\n");
        }

        String fname = "sc635hai_driver" + (suffix.isEmpty() ? "" : "_" + suffix) + ".txt";
        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, fname)));
        pw.print(sb.toString());
        pw.close();
        println("[*] Wrote " + fname);
        decomp.dispose();
    }
}
