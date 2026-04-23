package lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class TwoPassAssembler {

    public static final Map<String, Integer> Register_map = new HashMap<>();
    static {
        Register_map.put("$zero", 0);
        Register_map.put("$0", 0);
        Register_map.put("$v0", 2);  
        Register_map.put("$v1", 3);
        Register_map.put("$a0", 4); 
        Register_map.put("$a1", 5);
        Register_map.put("$a2", 6);  
        Register_map.put("$a3", 7);
        Register_map.put("$t0", 8);  
        Register_map.put("$t1", 9);
        Register_map.put("$t2", 10);  
        Register_map.put("$t3", 11);
        Register_map.put("$t4", 12);  
        Register_map.put("$t5", 13);
        Register_map.put("$t6", 14);  
        Register_map.put("$t7", 15);
        Register_map.put("$s0", 16);  
        Register_map.put("$s1", 17);
        Register_map.put("$s2", 18);  
        Register_map.put("$s3", 19);
        Register_map.put("$s4", 20);  
        Register_map.put("$s5", 21);
        Register_map.put("$s6", 22);  
        Register_map.put("$s7", 23);
        Register_map.put("$t8", 24);  
        Register_map.put("$t9", 25);
        Register_map.put("$sp", 29);
        Register_map.put("$ra", 31);
    }

    public static final Map<String, Integer> FUNCT_MAP = new HashMap<>(); 
    static {
        FUNCT_MAP.put("add", 0x20);  // 100000
        FUNCT_MAP.put("sub", 0x22);  // 100010
        FUNCT_MAP.put("and", 0x24);  // 100100
        FUNCT_MAP.put("or", 0x25);  // 100101
        FUNCT_MAP.put("slt", 0x2A);  // 101010
        FUNCT_MAP.put("sll", 0x00);  // 000000
        FUNCT_MAP.put("jr", 0x08);  // 001000
    }

    public static final Map<String, Integer> OPCODE_MAP = new HashMap<>();
    static {
        OPCODE_MAP.put("addi", 0x08); // 001000
        OPCODE_MAP.put("lw",   0x23); // 100011
        OPCODE_MAP.put("sw",   0x2B); // 101011
        OPCODE_MAP.put("beq",  0x04); // 000100
        OPCODE_MAP.put("bne",  0x05); // 000101
        OPCODE_MAP.put("j",    0x02); // 000010
        OPCODE_MAP.put("jal",  0x03); // 000011
    }

    public static int reg(String name) {
        if (!Register_map.containsKey(name)) {
            System.out.print("Unknown register: " + name);
        }
        return Register_map.get(name);
    }

    public static int encodeRType(int rs, int rt, int rd, int shamt, int funct) {
        return (0 << 26) | (rs << 21) | (rt << 16) | (rd << 11) | (shamt << 6) | funct;
    }

    public static int encodeIType(int opcode, int rs, int rt, int imm) {
        return (opcode << 26) | (rs << 21) | (rt << 16) | (imm & 0xFFFF);
    }

    public static int encodeJType(int opcode, int target) {
        return (opcode << 26) | (target & 0x03FFFFFF);
    }

    public static String toBinary(int word) {
        return String.format("%32s", Integer.toBinaryString(word)).replace(' ', '0');
    }
 
    public static String toHex(int word) {
        return String.format("%08X", word);
    }

    // Method to read in the file
    public static List<String> readFile (String filename) {
        List<String> fileLines = new ArrayList<>(); 
        // System.out.println("\n ---- File contents: ----");
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileLines.add(line); // Store each line of the file in a list for processing in the second pass
                // System.out.println(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return fileLines;
    }

    // Pass 1: 
    // The first pass should run through all the lines of the file to compute the address of each label.  
    public static Map<String, Object> pass1(List<String> fileLines) {

        // 6.  Your assembler must support the following instructions:  and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal.
        Set<String> keywords = new HashSet<>(Arrays.asList("and", "or", "addi", "add", "sll", "sub", "slt", "beq", "bne", "lw", "sw", "jal", "jr", "j"));
        
        // 7. Need to support 27/32 registers. You do NOT need to support the following registers: $at, $k0, $k1, $gp, $fp.
        Set<String> registers = new HashSet<>(Arrays.asList("$zero", "$0", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$sp", "$ra"));

        // Key: Address number | Value: Instruction + registers 
        Map<Integer, String> instructionMap = new HashMap<>(); 
        
        // Key: Address number | Value: Label name 
        Map<Integer, String> labelMap = new HashMap<>();

        // Key: Address number | Value: Invalid instructions + registers
        Map<Integer, String> invalidInstructionsMap = new HashMap<>();

        int addressCounter = 0;

        // System.out.println("\n ---- Cleaned Code (Comments Stripped): ---- ");

        for (String line : fileLines) {
            // 1. Code cleanup
            //  Strip comments
            int commentIndex = line.indexOf('#');
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex); // Remove comments from the line
            }
            // Trim
            line = line.trim();
            if (line.isEmpty()) continue; // Skip empty lines after trimming

            // Add space 
            for (String instruction : keywords) {
                if (line.startsWith(instruction)) {
                    if (line.startsWith(instruction)) {
                        int len = instruction.length();

                        if (line.length() > len) {
                            char nextChar = line.charAt(len);

                            // Only add a space if the next character is NOT whitespace                        }
                            // If next char is not whitespace, insert a space
                            if (!Character.isWhitespace(nextChar) && !Character.isLetter(nextChar)) {
                                line = instruction + " " + line.substring(instruction.length());
                                break;
                            }
                        }
                    }
                    break;   
                }

            }
            
            // System.out.println(line); // Print the cleaned line

            // Split line into tokens
            String[] tokens = line.split("[\\s,()]+");

            boolean isInstructionLine = false;
            String lineContent = "";

            // 2. Search for keywords, registers, and labels in the cleaned line
            for (String word : tokens) {
                if (word.isEmpty()) continue; // Skip empty strings from extra spaces

                if (word.endsWith(":")) { // words ending with ':'
                    String labelName = word.substring(0, word.length() - 1); // Remove the colon to get the label name
                    labelMap.put(addressCounter, labelName);
                    // addressCounter += 4;
                } else if (keywords.contains(word)) {
                    isInstructionLine = true;
                    lineContent += word + " ";
                } else if (registers.contains(word)) {
                    lineContent += word + " ";
                } else {
                    try {
                        Integer.parseInt(word); // Check if it's a number (immediate value)
                        lineContent += word + " ";
                    } catch (NumberFormatException e) {
                        boolean isJumpOrBranch = lineContent.startsWith("j") ||
                                                 lineContent.startsWith("jal") ||
                                                 lineContent.startsWith("jr") ||
                                                 lineContent.startsWith("beq") ||
                                                 lineContent.startsWith("bne");
                        if (isJumpOrBranch) {
                            if (registers.contains(word) || (!word.contains("$") && !Character.isDigit(word.charAt(0)))) {
                                lineContent += word + " ";
                            } else {
                                System.out.println("Warning: Unrecognized token '" + word + "' in line: " + line + "\n");
                                isInstructionLine = false;
                                
                                invalidInstructionsMap.put(addressCounter, line.trim());
                                
                                addressCounter +=4;
                                break;
                            }
                        }
                    }
                }
            }

            // 3. Map keywords + registers together
            if (isInstructionLine) {                    
                instructionMap.put(addressCounter, lineContent.trim());
                // System.out.println(addressCounter + ": " + lineContent.trim() + "\n");
                    
                addressCounter += 4; 
            }
            
        }

        // 4. Print output
        // System.out.println("\n ----- Pass 1 ----");

        // System.out.println("\nlabelMap Table (Label : Line):");
        // if (labelMap.isEmpty()) {
            // System.out.println("  (No labels found)");
        // } else {
            // Iterates through labels and prints name and address
            // labelMap.forEach((address, label) -> {
                // System.out.println("Address: " + address + " : " + label);
            // });
        // }

        // System.out.println("\ninstructionMap Table (Line | Instruction):");
        // if (instructionMap.isEmpty()) {
        //     System.out.println("  (No instructions found)");
        // } else {
        //     // Sorts by address so the program prints in the correct order
        //     instructionMap.keySet().stream().sorted().forEach(address -> {
        //         System.out.println("  Line " + (address/4) + " | " + "  Address " + address + " | " + instructionMap.get(address));
        //     });
        // }

        // System.out.println("\ninvalidInstructionsMap Table (Line | Invalid Instruction):");
        // if (invalidInstructionsMap.isEmpty()) {
        //     System.out.println("  (No instructions found)");
        // } else {
        //     // Sorts by address so the program prints in the correct order
        //     invalidInstructionsMap.keySet().stream().sorted().forEach(address -> {
        //         System.out.println("  Line " + (address/4) + " | " + "  Address " + address + " | " + invalidInstructionsMap.get(address));
        //     });
        // }

        // System.out.println("\n--------------------------\n");

        // 5. Return the results
        Map<String, Object> results = new HashMap<>();
        results.put("instructions", instructionMap);
        results.put("labels", labelMap);
        return results;
    }

    // Pass 2:
    // During the second pass, all the instructions are converted to machine code
    public static Map<Integer, String> pass2(Map<Integer, String> instructionsPass1, Map<Integer, String> labelsPass1) {
        List<Integer> sortedAddresses = new ArrayList<>(instructionsPass1.keySet());

        Map<Integer, String> machineCode = new HashMap<>();

        for (Integer address : sortedAddresses) {
            // 1. Get string
            String instruction = instructionsPass1.get(address);

            // 2. Split the string (Opcode, Registers, Immediates/Labels)
            String[] splitInstruction = instruction.split("\\s+");
            String opcode = splitInstruction[0];

            int encoded = 0;

            // 3. Identify opcode
            // R-Type : opcode rs rt rd shamt funct
            if (opcode.equals("add") || opcode.equals("sub") || opcode.equals("or") || opcode.equals("and") || opcode.equals("slt")) {
                int rd = reg(splitInstruction[1]);
                int rs = reg(splitInstruction[2]);
                int rt = reg(splitInstruction[3]);

                encoded = encodeRType(rs, rt, rd, 0, FUNCT_MAP.get(opcode));
                // System.out.println("Processing opcode [ " + opcode + " " + rd + " " + rs + " " + rt + " ] at address " + address);
            }
            // I-Type : addi
            else if (opcode.equals("addi")) {
            	int rt = reg(splitInstruction[1]);
                int rs = reg(splitInstruction[2]);
                int imm = Integer.parseInt(splitInstruction[3]);

                encoded = encodeIType(OPCODE_MAP.get(opcode), rs, rt, imm);
                // System.out.println("Processing opcode [ " + opcode + " " + rt + " " + rs + " " + imm + " ] at address " + address);
            }

            else if (opcode.equals("sll")) {
                int rd = reg(splitInstruction[1]);
                int rt = reg(splitInstruction[2]);
                int shamt = Integer.parseInt(splitInstruction[3]);

                encoded = encodeRType(0, rt, rd, shamt, FUNCT_MAP.get(opcode));
                // System.out.println("Processing opcode [ " + opcode + " " + rd + " " + rt + " " + shamt + " ] at address " + address);
            }
                
            else if (opcode.equals("lw") || opcode.equals("sw") ) {
                int rt = reg(splitInstruction[1]);
                int offset = Integer.parseInt(splitInstruction[2]);
                int rs = reg(splitInstruction[3]);

                encoded = encodeIType(OPCODE_MAP.get(opcode), rs, rt, offset);
                // System.out.println("Processing opcode [ " + opcode + " " + rt + " " + offset + "(" + rs + ") ] at address " + address);
            }
            // Branch : I-type with label lookup
            else if (opcode.equals("beq") || opcode.equals("bne")) {
                int rs = reg(splitInstruction[1]);
                int rt = reg(splitInstruction[2]);
                String label = splitInstruction[3];

                // Label lookup
                Integer labelAddress = null;
                for (Map.Entry<Integer, String> entry : labelsPass1.entrySet()) {
                    if (entry.getValue().equals(label)) {
                        labelAddress = entry.getKey();
                        break;
                    }
                }

                if (labelAddress == null) {
                    System.out.println("Error: Label '" + label + "' not found");
                    continue; // Skip this instruction
                }

                int offset = (labelAddress - (address + 4)) / 4;

                encoded = encodeIType(OPCODE_MAP.get(opcode), rs, rt, offset);
                // System.out.println("Processing opcode [ " + opcode + " " + rs + " " + rt + " " + label + "(offset: " + offset + ") ] at address " + address);
            }
            // J-Type : Jump
            else if ((opcode.equals("j")) || opcode.equals("jal")) {
                String label = splitInstruction[1];

                // Label lookup
                Integer labelAddress = null;
                for (Map.Entry<Integer, String> entry : labelsPass1.entrySet()) {
                    if (entry.getValue().equals(label)) {
                        labelAddress = entry.getKey();
                        break;
                    }
                }

                if (labelAddress == null) {
                    System.out.println("Error: Label '" + label + "' not found");
                    continue; // Skip this instruction
                }

                int target = labelAddress / 4;

                encoded = encodeJType(OPCODE_MAP.get(opcode), target);
                // System.out.println("Processing opcode [ " + opcode + " " + label + "(target: " + target + ") ] at address " + address);
            }

            else if (opcode.equals("jr")) {
                int rs = reg(splitInstruction[1]);

                encoded = encodeRType(rs, 0, 0,0, FUNCT_MAP.get(opcode));
                // System.out.println("Processing opcode [ " + opcode + " " + rs + " ] at address " + address);
            } else {
                    System.out.println("Error: Unrecognized opcode '" + opcode + "' in instruction: " + instruction);
                    continue; // Skip this instruction
                }

            String hexString = toHex(encoded);
            machineCode.put(address, hexString);

    }
    return machineCode;
        
    }
    public static String formatBinaryByType(String instruction, int word) {
    String bin = toBinary(word);
    String opcode = instruction.split("\\s+")[0];

    // R-type
        if (opcode.equals("add") || opcode.equals("sub") || opcode.equals("and") || opcode.equals("or") || opcode.equals("slt") || opcode.equals("sll") || opcode.equals("jr")) {
            return bin.substring(0, 6) + " " +
                bin.substring(6, 11) + " " +
                bin.substring(11, 16) + " " +
                bin.substring(16, 21) + " " +
                bin.substring(21, 26) + " " +
                bin.substring(26, 32);
        }

        // J-type
        else if (opcode.equals("j") || opcode.equals("jal")) {
            return bin.substring(0, 6) + " " +
                bin.substring(6, 32);
        }

        // I-type
        else {
            return bin.substring(0, 6) + " " +
                bin.substring(6, 11) + " " +
                bin.substring(11, 16) + " " +
                bin.substring(16, 32);
        }
}

    public static void printToScreen(Map<Integer, String> machineCode, Map<Integer, String> instructions) {
        machineCode.keySet().stream().sorted().forEach(addr -> {
        String hex = machineCode.get(addr);
        int word = (int) Long.parseLong(hex, 16);
        String instruction = instructions.get(addr);

        System.out.println(formatBinaryByType(instruction, word));
        });
    }
    // Print output to screen (make new file)


    public static void main(String[] args) {
        // Read File
        List<String> fileLines = readFile("lab2/testprog4.asm");
        
        // Pass 1:
        Map<String, Object> pass1Results = pass1(fileLines);

        // Extract Maps
        Map<Integer, String> instructions = (Map<Integer, String>) pass1Results.get("instructions");
        Map<Integer, String> labels = (Map<Integer, String>) pass1Results.get("labels");

        // Pass 2:
        Map<Integer, String> machineCode = pass2(instructions, labels);

        // Print to screen
        printToScreen(machineCode, instructions);
    }

}
