// Ghidra headless script: Resolve DAT_* addresses to actual string values
// Focus on linkkit_set_property_handler and the ISP CSC struct
//@category Analysis

import ghidra.app.script.GhidraScript;
import ghidra.app.decompiler.*;
import ghidra.program.model.listing.*;
import ghidra.program.model.address.*;
import ghidra.program.model.mem.*;
import ghidra.program.model.data.*;
import ghidra.program.model.symbol.*;
import java.io.*;
import java.util.*;

public class ResolveStrings extends GhidraScript {

    @Override
    public void run() throws Exception {
        String[] args = getScriptArgs();
        String outputDir = args.length > 0 ? args[0] : ".";

        Memory mem = currentProgram.getMemory();
        Listing listing = currentProgram.getListing();
        FunctionManager fm = currentProgram.getFunctionManager();
        DecompInterface decomp = new DecompInterface();
        decomp.openProgram(currentProgram);

        StringBuilder sb = new StringBuilder();
        sb.append("RESOLVED STRING ADDRESSES AND ISP STRUCT ANALYSIS\n");
        sb.append("=".repeat(70)).append("\n\n");

        // Part 1: Resolve all DAT_ references from linkkit_set_property_handler
        // The function is at 0x00176918. Its DAT_ references point to data
        // that often contains pointers to strings.
        sb.append("PART 1: linkkit_set_property_handler STRING REFERENCES\n");
        sb.append("-".repeat(70)).append("\n\n");

        // Read the decompiled output and find all DAT_ addresses
        Function linkkit = null;
        for (Function f : fm.getFunctions(true)) {
            if (f.getName().equals("linkkit_set_property_handler")) {
                linkkit = f;
                break;
            }
        }

        if (linkkit != null) {
            // Get all data references from this function
            AddressSet body = new AddressSet(linkkit.getBody());
            ReferenceManager refMgr = currentProgram.getReferenceManager();
            
            // Collect unique DAT addresses referenced
            Set<String> resolvedAddrs = new TreeSet<>();
            
            // Scan instructions for data references
            InstructionIterator instIter = listing.getInstructions(body, true);
            while (instIter.hasNext()) {
                Instruction inst = instIter.next();
                Reference[] refs = inst.getReferencesFrom();
                for (Reference ref : refs) {
                    Address toAddr = ref.getToAddress();
                    if (ref.getReferenceType().isData()) {
                        String resolved = resolveToString(mem, listing, toAddr);
                        if (resolved != null) {
                            String entry = String.format("  %s -> %s", toAddr, resolved);
                            resolvedAddrs.add(entry);
                        }
                    }
                }
            }

            for (String entry : resolvedAddrs) {
                sb.append(entry).append("\n");
            }
            sb.append("\nTotal resolved: ").append(resolvedAddrs.size()).append("\n\n");
        }

        // Part 2: Decompile mpi_isp_set_csc_attr to find the ioctl number
        sb.append("\nPART 2: ISP CSC IOCTL CHAIN\n");
        sb.append("-".repeat(70)).append("\n\n");

        String[] cscFuncs = {
            "mpi_isp_set_csc_attr",
            "mpi_isp_get_csc_attr",
            "hi_mpi_isp_set_csc_attr",
            "hi_mpi_isp_get_csc_attr",
            "ot_mpi_isp_set_csc_attr",
            "ot_mpi_isp_get_csc_attr",
            "isp_check_pipe",
            "isp_mutex_lock",
        };

        for (String name : cscFuncs) {
            for (Function f : fm.getFunctions(true)) {
                if (f.getName().equals(name)) {
                    sb.append("FUNCTION: ").append(name).append(" @ ").append(f.getEntryPoint()).append("\n");
                    sb.append("SIZE: ").append(f.getBody().getNumAddresses()).append(" bytes\n");

                    // Disassembly
                    sb.append("--- ASM ---\n");
                    InstructionIterator ii = listing.getInstructions(new AddressSet(f.getBody()), true);
                    int count = 0;
                    while (ii.hasNext() && count < 60) {
                        Instruction inst = ii.next();
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
                    sb.append("\n\n");
                    break;
                }
            }
        }

        // Part 3: Resolve secu_sensor_saturate_set's calls to understand CSC struct
        sb.append("\nPART 3: CSC STRUCT LAYOUT from secu_sensor_saturate_set\n");
        sb.append("-".repeat(70)).append("\n\n");

        // The key insight: secu_sensor_saturate_set reads the CSC struct, modifies byte at offset 0xB
        // Let's verify by looking at the hi_mpi_isp_get_csc_attr function
        for (Function f : fm.getFunctions(true)) {
            if (f.getName().equals("secu_sensor_saturate_set")) {
                sb.append("secu_sensor_saturate_set calls chain:\n");
                Set<Function> called = f.getCalledFunctions(monitor);
                for (Function c : called) {
                    sb.append("  -> ").append(c.getName()).append(" @ ").append(c.getEntryPoint()).append("\n");
                }
                sb.append("\nCallers:\n");
                Set<Function> callers = f.getCallingFunctions(monitor);
                for (Function c : callers) {
                    sb.append("  <- ").append(c.getName()).append(" @ ").append(c.getEntryPoint()).append("\n");
                }
                sb.append("\n");
                break;
            }
        }

        // Part 4: Find all functions that call hi_mpi_isp_set_csc_attr
        sb.append("\nPART 4: WHO CALLS hi_mpi_isp_set_csc_attr?\n");
        sb.append("-".repeat(70)).append("\n\n");

        for (Function f : fm.getFunctions(true)) {
            if (f.getName().equals("hi_mpi_isp_set_csc_attr")) {
                Set<Function> callers = f.getCallingFunctions(monitor);
                for (Function c : callers) {
                    sb.append("  <- ").append(c.getName()).append(" @ ").append(c.getEntryPoint());
                    sb.append(" (size: ").append(c.getBody().getNumAddresses()).append(")\n");
                }
                sb.append("\n");
                break;
            }
        }

        // Part 5: Find all functions that call hi_mpi_isp_get_csc_attr
        sb.append("\nPART 5: WHO CALLS hi_mpi_isp_get_csc_attr?\n");
        sb.append("-".repeat(70)).append("\n\n");

        for (Function f : fm.getFunctions(true)) {
            if (f.getName().equals("hi_mpi_isp_get_csc_attr")) {
                Set<Function> callers = f.getCallingFunctions(monitor);
                for (Function c : callers) {
                    sb.append("  <- ").append(c.getName()).append(" @ ").append(c.getEntryPoint());
                    sb.append(" (size: ").append(c.getBody().getNumAddresses()).append(")\n");
                }
                sb.append("\n");
                break;
            }
        }

        PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, "resolved_strings.txt")));
        pw.print(sb.toString());
        pw.close();

        println("[*] Wrote resolved_strings.txt");
        decomp.dispose();
    }

    private String resolveToString(Memory mem, Listing listing, Address addr) {
        try {
            // Try reading as a pointer first (ARM 32-bit LE)
            byte[] ptrBytes = new byte[4];
            mem.getBytes(addr, ptrBytes);
            long ptrVal = (ptrBytes[0] & 0xFF) | ((ptrBytes[1] & 0xFF) << 8) |
                         ((ptrBytes[2] & 0xFF) << 16) | ((ptrBytes[3] & 0xFF) << 24);

            if (ptrVal > 0x10000 && ptrVal < 0x800000) {
                // Looks like a valid pointer, try to read string at target
                Address strAddr = addr.getNewAddress(ptrVal);
                String str = readStringAt(mem, strAddr);
                if (str != null && str.length() > 0 && isPrintable(str)) {
                    return "\"" + str + "\" (via ptr at " + String.format("0x%08x", ptrVal) + ")";
                }
            }

            // Try reading directly as string
            String directStr = readStringAt(mem, addr);
            if (directStr != null && directStr.length() > 1 && isPrintable(directStr)) {
                return "\"" + directStr + "\" (direct)";
            }

            // Return raw value
            return String.format("[0x%08x]", ptrVal);
        } catch (Exception e) {
            return null;
        }
    }

    private String readStringAt(Memory mem, Address addr) {
        try {
            byte[] buf = new byte[128];
            int read = mem.getBytes(addr, buf);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < read && i < 128; i++) {
                if (buf[i] == 0) break;
                sb.append((char)(buf[i] & 0xFF));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isPrintable(String s) {
        for (char c : s.toCharArray()) {
            if (c < 0x20 || c > 0x7e) return false;
        }
        return true;
    }
}
