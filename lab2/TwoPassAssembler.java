package lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TwoPassAssembler {

    public static List<String> readFile (String filename) {
        List<String> fileLines = new ArrayList<>(); 
        System.out.println("File contents:");
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
        
        // 7. Need to support 27 registers. You do NOT need to support the following registers: $at, $k0, $k1, $gp, $fp.
        Set<String> registers = new HashSet<>(Arrays.asList("$zero", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$sp", "$ra"));
        
        Set<String> foundKeywords = new HashSet<>();
        Set<String> foundRegisters = new HashSet<>();

        for (String line : fileLines) {
<<<<<<< HEAD
            // Ignoring spaces, commas, parentheses for keyword/register extraction
=======
            int conmmentIndex = line.indexOf('#');
            if (conmmentIndex != -1) {
                line = line.substring(0, conmmentIndex); // Remove comments from the line
            }
>>>>>>> 21e91f3a7955c880d0cbad1ae773911d62093a30
            String[] tokens = line.split("[\\s,()]+");

            for (String word : tokens) {
                if (word.isEmpty()) continue; // Skip empty strings from extra spaces

                if (keywords.contains(word)) {
                    foundKeywords.add(word);
                } else if (registers.contains(word)) {
                    foundRegisters.add(word);
                
                
    
                }
            }
        }
        System.out.println("\n\nKeywords found: " + foundKeywords);
        System.out.println("Registers found: " + foundRegisters);
    }

    public static void main(String[] args) {
        List<String> fileLines = readFile("lab2/testprog1.asm");
        processInput(fileLines);
    }

}

