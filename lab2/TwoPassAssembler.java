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
        Set<String> keywords = new HashSet<>(Arrays.asList("and", "or", "add", "addi", "sll", "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"));
        
        // 7. Need to support 27/32 registers. You do NOT need to support the following registers: $at, $k0, $k1, $gp, $fp.
        Set<String> registers = new HashSet<>(Arrays.asList("$zero", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$sp", "$ra"));

        // Key: Instruction name | Value: List of registers used in that instruction
        Map<String, List<String>> instructionMap = new HashMap<>(); 
        
        // Key: Label name | Value: List of line addresses where the label is referenced 
        Map<String, List<String>> labelMap = new HashMap<>();

        // To keep track of the most recent label for mapping purposes
        String activeLabel = null; 
        System.out.println("\n ---- Cleaned Code (Comments Stripped): ---- ");

        for (String line : fileLines) {
            // Strip comments
            int commentIndex = line.indexOf('#');
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex); // Remove comments from the line
            }

            // Clean up whitespace 
            line = line.trim();
            if (line.isEmpty()) continue; // Skip empty lines after trimming

            for (String instruction : keywords) {
                if (line.startsWith(instruction)) {
                    if (line.length() > instruction.length()) {
                        char nextChar = line.charAt(instruction.length());
                        // If next char is not whitespace, insert a space
                        if (!Character.isWhitespace(nextChar)) {
                            line = instruction + " " + line.substring(instruction.length());
                        }
                    }
                }
            }
            System.out.println(line); // Print the cleaned line

            // Split line into tokens
            String[] tokens = line.split("[\\s,()]+");

            String currentInstruction = null;
            List<String> lineRegisters = new ArrayList<>();

            // Looking for keywords, registers, and labels in the cleaned line
            for (String word : tokens) {
                if (word.isEmpty()) continue; // Skip empty strings from extra spaces

                if (word.endsWith(":")) { // words ending with ':'
                    activeLabel = word.substring(0, word.length() - 1); // Remove the colon to get the label name
                    labelMap.put(activeLabel, new ArrayList<>());
                } else if (keywords.contains(word)) {
                    currentInstruction = word;
                } else if (registers.contains(word)) {
                    lineRegisters.add(word);
                }
            }

            // Map keywords + registers together
            if (currentInstruction != null) {
                if (instructionMap.containsKey(currentInstruction)) {
                    instructionMap.get(currentInstruction).addAll(lineRegisters);
                } else {
                    instructionMap.put(currentInstruction, new ArrayList<>(lineRegisters));
                }
            }
        }
            // Print output
            System.out.println("\n ----- Pass 1: ----");
            System.out.println("Symbol table (Labels -> Addresses: ) ");
            System.out.println(labelMap);
            System.out.println("Instruction table (Instructions -> Registers: ) ");
            System.out.println(instructionMap + "\n\n");
    }

    public static void main(String[] args) {
        List<String> fileLines = readFile("lab2/testprog1.asm");
        processInput(fileLines);
    }

}
