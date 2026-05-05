// Find io_write8, io_write16, and trace the ISP mmap region
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import java.io.*;
import java.util.*;

public class FindIoWrite extends GhidraScript {
    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";

        FunctionManager fm = currentProgram.getFunctionManager();
        Listing listing = currentProgram.getListing();
        DecompInterface decomp = new DecompInterface();
        decomp.openProgram(currentProgram);

        StringBuilder sb = new StringBuilder();
        sb.append("IO_WRITE AND ISP MMAP ANALYSIS\n");
        sb.append("=".repeat(70)).append("\n\n");

        // Find io_write8, io_write16, io_read8, io_read16
        // Also isp_check_dev_open, isp_check_mem_init_func, isp_check_vreg_permission
        String[] targets = {
            "io_write8", "io_write16", "io_write32",
            "io_read8", "io_read16", "io_read32",
            "isp_check_dev_open", "isp_check_mem_init_func", 
            "isp_check_vreg_permission",
            "isp_mutex_lock", "isp_mutex_unlock",
            "isp_drv_vreg_init", "isp_drv_init",
            "isp_lib_init", "isp_main_init",
        };

        for (String name : targets) {
            for (Function f : fm.getFunctions(true)) {
                if (f.getName().equals(name)) {
                    sb.append("=".repeat(50)).append("\n");
                    sb.append("FUNCTION: ").append(name).append(" @ ").append(f.getEntryPoint()).append("\n");
                    sb.append("SIZE: ").append(f.getBody().getNumAddresses()).append(" bytes\n");

                    // Disassembly
                    sb.append("--- ASM ---\n");
                    var ii = listing.getInstructions(new ghidra.program.model.address.AddressSet(f.getBody()), true);
                    int count = 0;
                    while (ii.hasNext() && count < 40) {
                        var inst = ii.next();
                        sb.append(String.format("  %s: %s\n", inst.getAddress(), inst.toString()));
                        count++;
                    }

                    // Decompile
                    sb.append("--- C ---\n");
                    DecompileResults res = decomp.decompileFunction(f, 60, monitor);
                    if (res != null && res.decompileCompleted() && res.getDecompiledFunction() != null) {
                        sb.append(res.getDecompiledFunction().getC());
                    } else {
                        sb.append("DECOMPILE FAILED\n");
                    }

                    sb.append("\n--- Calls ---\n");
                    for (Function c : f.getCalledFunctions(monitor)) {
                        sb.append("  -> ").append(c.getName()).append(" @ ").append(c.getEntryPoint()).append("\n");
                    }
                    sb.append("\n\n");
                    break;
                }
            }
        }

        // Also find any function whose name contains "vreg" or "mmap" or "devmem"
        sb.append("\nFUNCTIONS WITH 'vreg', 'mmap', 'devmem' IN NAME:\n");
        sb.append("-".repeat(50)).append("\n");
        for (Function f : fm.getFunctions(true)) {
            String n = f.getName().toLowerCase();
            if (n.contains("vreg") || n.contains("mmap") || n.contains("devmem") || n.contains("isp_drv")) {
                sb.append(String.format("  %s @ %s (%d bytes)\n", f.getName(), f.getEntryPoint(), f.getBody().getNumAddresses()));
            }
        }

        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, "io_write_analysis.txt")));
        pw.print(sb.toString());
        pw.close();

        println("[*] Wrote io_write_analysis.txt");
        decomp.dispose();
    }
}
