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

    // Method to read in the file
    public static List<String> readFile (String filename) {
        List<String> fileLines = new ArrayList<>(); 
        System.out.println("\n ---- File contents: ----");
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileLines.add(line); // Store each line of the file in a list for processing in the second pass
                System.out.println(line);
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
        Set<String> registers = new HashSet<>(Arrays.asList("$zero", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$sp", "$ra"));

        // Key: Address number | Value: Instruction + registers 
        Map<Integer, String> instructionMap = new HashMap<>(); 
        
        // Key: Address number | Value: Label name 
        Map<Integer, String> labelMap = new HashMap<>();

        // Key: Address number | Value: Invalid instructions + registers
        Map<Integer, String> invalidInstructionsMap = new HashMap<>();

        int addressCounter = 0;

        System.out.println("\n ---- Cleaned Code (Comments Stripped): ---- ");

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
            
            System.out.println(line); // Print the cleaned line

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
                    addressCounter += 4;
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
                                lineContent += word + "";
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
                System.out.println(addressCounter + ": " + lineContent.trim() + "\n");
                    
                addressCounter += 4; 
            }
            
        }

        // 4. Print output
        System.out.println("\n ----- Pass 1 ----");

        System.out.println("\nlabelMap Table (Label : Line):");
        if (labelMap.isEmpty()) {
            System.out.println("  (No labels found)");
        } else {
            // Iterates through labels and prints name and address
            labelMap.forEach((address, label) -> {
                System.out.println("Address: " + address + " : " + label);
            });
        }

        System.out.println("\ninstructionMap Table (Line | Instruction):");
        if (instructionMap.isEmpty()) {
            System.out.println("  (No instructions found)");
        } else {
            // Sorts by address so the program prints in the correct order
            instructionMap.keySet().stream().sorted().forEach(address -> {
                System.out.println("  Line " + (address/4) + " | " + "  Address " + address + " | " + instructionMap.get(address));
            });
        }

        System.out.println("\ninvalidInstructionsMap Table (Line | Invalid Instruction):");
        if (invalidInstructionsMap.isEmpty()) {
            System.out.println("  (No instructions found)");
        } else {
            // Sorts by address so the program prints in the correct order
            invalidInstructionsMap.keySet().stream().sorted().forEach(address -> {
                System.out.println("  Line " + (address/4) + " | " + "  Address " + address + " | " + invalidInstructionsMap.get(address));
            });
        }

        System.out.println("\n--------------------------\n");

        // 5. Return the results
        Map<String, Object> results = new HashMap<>();
        results.put("instructions", instructionMap);
        results.put("labels", labelMap);
        return results;
    }

    // Pass 2:
    // During the second pass, all the instructions are converted to machine code
    public static void pass2(Map<Integer, String> instructionsPass1, Map<Integer, String> labelsPass1) {
        List<Integer> sortedAddresses = new ArrayList<>(instructionsPass1.keySet());

        for (Integer address : sortedAddresses) {
            // 1. Get string
            String instruction = instructionsPass1.get(address);

            // 2. Split the string (Opcode, Registers, Immediates/Labels)
            String[] splitInstruction = instruction.split("\\s+");
            String opcode = splitInstruction[0];

            // 3. Identify opcode
            // R-Type : opcode rs rt rd shamt funct
            if (opcode.equals("add") || opcode.equals("sub") || opcode.equals("or") || opcode.equals("and") || opcode.equals("slt")) {
                String rd = splitInstruction[1];
                String rs = splitInstruction[2];
                String rt = splitInstruction[3];

                System.out.println("Processing opcode [ " + opcode + " " + rd + " " + rs + " " + rt + " ] at address " + address);
            }
            // I-Type : opcode rs rt immediate
            else if (opcode.equals("addi") || opcode.equals("lw") || opcode.equals("sw") ) {

            }
            // Branch : I-type with label lookup
            else if (opcode.equals("beq") || opcode.equals("bne")) {

            }
            // J-Type : Jump
            else if ((opcode.equals("j")) || opcode.equals("jal")) {

            }
        }

            // 4. Lookup label
            // 5. Calculate Offset
            // 6. Store machine code ouput

            // Print output
        
    }

    public static void printToScreen() {}
    // Print output to screen (make new file)

    public static void main(String[] args) {
        // Read File
        List<String> fileLines = readFile("lab2/testprog1.asm");
        
        // Pass 1:
        Map<String, Object> pass1Results = pass1(fileLines);

        // Extract Maps
        Map<Integer, String> instructions = (Map<Integer, String>) pass1Results.get("instructions");
        Map<Integer, String> labels = (Map<Integer, String>) pass1Results.get("labels");

        // Pass 2:
        pass2(instructions, labels);

        // Print to screen
        printToScreen();
    }

}
