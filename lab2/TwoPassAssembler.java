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

    public static void processInput(List<String> fileLines) {

        // 6.  Your assembler must support the following instructions:  and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal.
        Set<String> keywords = new HashSet<>(Arrays.asList("and", "or", "addi", "add", "sll", "sub", "slt", "beq", "bne", "lw", "sw", "jal", "jr", "j"));
        
        // 7. Need to support 27/32 registers. You do NOT need to support the following registers: $at, $k0, $k1, $gp, $fp.
        Set<String> registers = new HashSet<>(Arrays.asList("$zero", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$sp", "$ra"));

        // Key: Line number | Value: Instruction + registers 
        Map<Integer, String> instructionMap = new HashMap<>(); 
        
        // Key: Line number | Value: Label name 
        Map<Integer, String> labelMap = new HashMap<>();

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

            for(int i = line.length(); i > 0; i--) {
                String inst = line.substring(0, i);
                if (keywords.contains(inst)) {
                    if (line.length() > inst.length()) {
                        char nextChar = line.charAt(inst.length());
                        // If next char is not whitespace, insert a space
                        if (!Character.isWhitespace(nextChar)) {
                            line = inst + " " + line.substring(inst.length());
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

            // Looking for keywords, registers, and labels in the cleaned line
            for (String word : tokens) {
                if (word.isEmpty()) continue; // Skip empty strings from extra spaces

                if (word.endsWith(":")) { // words ending with ':'
                    String labelName = word.substring(0, word.length() - 1); // Remove the colon to get the label name
                    labelMap.put(addressCounter, labelName);
                } else if (keywords.contains(word)) {
                    isInstructionLine = true;
                    lineContent += word + " ";
                } else if (registers.contains(word)) {
                    lineContent += (word + "");
                } else {
                    try {
                        Integer.parseInt(word); // Check if it's a number (immediate value)
                    } catch (NumberFormatException e) {
                        // If it's not a number, it could be a label reference
                        if (!(currentInstruction != null &&
                            (currentInstruction.equals("j") || 
                            currentInstruction.equals("jal") || 
                            currentInstruction.equals("beq") || 
                            currentInstruction.equals("bne")
                        ))) {
                            System.out.println("Warning: Unrecognized token '" + word + "' in line: " + line);
                        }
                    }
                }

            // Map keywords + registers together
            if (currentInstruction != null) {
                if (instructionMap.containsKey(currentInstruction)) {
                    instructionMap.get(currentInstruction).addAll(lineRegisters);
                } else {
                    instructionMap.put(currentInstruction, new ArrayList<>(lineRegisters));
                        System.out.println("Warning: Unrecognized token '" + word + "' in line: " + line);
                    }
                    //Need to check for invalid instructions or registers
                    if (!currentInstruction.equals("j") && !currentInstruction.equals("jr") && !currentInstruction.equals("jal")) {
                        System.out.println("Warning: Unrecognized token '" + word + "' in line: " + line);
                    }
                }
            }
            
                // Map keywords + registers together
                if (isInstructionLine) {                    
                    instructionMap.put(addressCounter, lineContent.trim());
                    System.out.println(addressCounter + ": " + lineContent.trim());
                    
                    addressCounter += 4; 
                }
            
        }

        // Print output
        System.out.println("\n ----- Pass 1 ----");

        System.out.println("\nSymbol Table (Label : Line):");
        if (labelMap.isEmpty()) {
            System.out.println("  (No labels found)");
        } else {
            // Iterates through labels and prints name and address
            labelMap.forEach((label, address) -> {
                System.out.println("  " + address + " : " + address);
            });
        }

        System.out.println("\nInstruction Table (Line | Instruction):");
        if (instructionMap.isEmpty()) {
            System.out.println("  (No instructions found)");
        } else {
            // Sorts by address so the program prints in the correct order
            instructionMap.keySet().stream().sorted().forEach(address -> {
                System.out.println("  Line " + (address/4) + " | " + "  Address " + address + " | " + instructionMap.get(address));
            });
        }
        System.out.println("\n--------------------------\n");
    }


    public static void main(String[] args) {
        List<String> fileLines = readFile("lab2/testprog1.asm");
        processInput(fileLines);
    }

}
