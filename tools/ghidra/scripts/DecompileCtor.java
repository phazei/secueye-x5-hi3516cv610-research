// Decompile the .init_array constructor of libot_mpi_isp.so
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import ghidra.program.model.symbol.*;
import java.io.*;
import java.util.*;

public class DecompileCtor extends GhidraScript {
    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";

        FunctionManager fm = currentProgram.getFunctionManager();
        DecompInterface decomp = new DecompInterface();
        decomp.openProgram(currentProgram);

        // Find the constructor at address 0x8888 (Thumb, from init_array value 0x8889)
        Address ctorAddr = currentProgram.getAddressFactory().getDefaultAddressSpace().getAddress(0x18888);
        Function ctor = fm.getFunctionAt(ctorAddr);
        if (ctor == null) {
            // Try to disassemble there
            ctor = fm.getFunctionContaining(ctorAddr);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("LIBOT_MPI_ISP.SO INIT_ARRAY CONSTRUCTOR\n");
        sb.append("=".repeat(70)).append("\n");

        if (ctor != null) {
            sb.append("FUNCTION: ").append(ctor.getName()).append("\n");
            sb.append("ADDRESS:  ").append(ctor.getEntryPoint()).append("\n");
            sb.append("SIZE:     ").append(ctor.getBody().getNumAddresses()).append(" bytes\n\n");
            DecompileResults res = decomp.decompileFunction(ctor, 60, monitor);
            if (res != null && res.decompileCompleted() && res.getDecompiledFunction() != null) {
                sb.append(res.getDecompiledFunction().getC());
            }
            sb.append("\n--- CALLS TO ---\n");
            for (Function called : ctor.getCalledFunctions(monitor)) {
                sb.append("  -> ").append(called.getName()).append(" @ ").append(called.getEntryPoint()).append("\n");
            }
        } else {
            sb.append("Function at 0x8888 not found in current program\n");
        }

        // Also enumerate all functions with names like "register" or "init" near startup
        sb.append("\n\n=== Functions named *register* or *init* ===\n");
        int count = 0;
        for (Function f : fm.getFunctions(true)) {
            String n = f.getName().toLowerCase();
            if ((n.contains("register") || n.contains("__cxa") || n.contains("frame") ||
                 n.equals("_init") || n.contains("call_init") || n.contains("__libc_start")) &&
                !n.startsWith("fun_") && count < 50) {
                sb.append("  ").append(f.getName()).append(" @ ").append(f.getEntryPoint()).append("\n");
                count++;
            }
        }

        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, "libisp_ctor.txt")));
        pw.print(sb.toString());
        pw.close();
        println("[*] Wrote libisp_ctor.txt");
        decomp.dispose();
    }
}
