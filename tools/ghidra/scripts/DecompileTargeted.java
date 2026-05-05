// Ghidra headless script: Disassemble and decompile specific targeted functions
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import java.io.*;
import java.util.*;

public class DecompileTargeted extends GhidraScript {

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";

        FunctionManager fm = currentProgram.getFunctionManager();
        Listing listing = currentProgram.getListing();
        DecompInterface decomp = new DecompInterface();
        decomp.openProgram(currentProgram);

        // Specific functions we want full detail on
        String[] targets = {
            "isp_ioctl",
            "osal_ioctldev",
            "secu_sensor_brightness",
            "secu_sensor_contrast",
            "secu_sensor_saturation",
            "secu_sensor_sharpness",
            "secu_sensor_nocolour",
            "secu_sensor_mirror_flip_set",
            "secu_sensor_set_nightmode",
            "secu_sensor_light",
            "secu_sensor_set_ircut",
            "secu_sensor_b_night_auto",
            "secu_sensor_set_security_image_effect",
            "secu_sensor_saturate_set",
            "linkkit_set_property_handler",
            "dm_msg_property_set",
            "dm_client_thing_service_property_set",
            "sub_ipc_slave_property_set",
            "media_sensor_mirrorflip",
            "sp_dev_reboot",
            "WriteConfigInfo",
        };

        StringBuilder sb = new StringBuilder();
        sb.append("TARGETED DECOMPILATION + DISASSEMBLY\n");
        sb.append("=".repeat(70)).append("\n\n");

        for (String targetName : targets) {
            // Find function by name
            Function func = null;
            for (Function f : fm.getFunctions(true)) {
                if (f.getName().equals(targetName)) {
                    func = f;
                    break;
                }
            }

            if (func == null) {
                sb.append("FUNCTION NOT FOUND: ").append(targetName).append("\n\n");
                continue;
            }

            Address entry = func.getEntryPoint();
            long size = func.getBody().getNumAddresses();

            sb.append("=".repeat(70)).append("\n");
            sb.append("FUNCTION: ").append(targetName).append("\n");
            sb.append("ADDRESS:  ").append(entry).append("\n");
            sb.append("SIZE:     ").append(size).append(" bytes\n");
            sb.append("=".repeat(70)).append("\n\n");

            // Get raw disassembly listing
            sb.append("--- DISASSEMBLY ---\n");
            AddressSet body = new AddressSet(func.getBody());
            InstructionIterator instIter = listing.getInstructions(body, true);
            int instCount = 0;
            while (instIter.hasNext() && instCount < 200) {
                Instruction inst = instIter.next();
                sb.append(String.format("  %s: %s\n", inst.getAddress(), inst.toString()));
                instCount++;
            }
            sb.append("\n");

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
            sb.append("\n\n");

            // Get called functions
            sb.append("--- CALLS TO ---\n");
            Set<Function> calledFuncs = func.getCalledFunctions(monitor);
            for (Function called : calledFuncs) {
                sb.append("  -> ").append(called.getName()).append(" @ ").append(called.getEntryPoint()).append("\n");
            }
            sb.append("\n--- CALLED BY ---\n");
            Set<Function> callers = func.getCallingFunctions(monitor);
            for (Function caller : callers) {
                sb.append("  <- ").append(caller.getName()).append(" @ ").append(caller.getEntryPoint()).append("\n");
            }
            sb.append("\n\n");
        }

        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, "targeted_analysis.txt")));
        pw.print(sb.toString());
        pw.close();

        println("[*] Wrote targeted_analysis.txt");
        decomp.dispose();
    }
}
