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
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                fileLines.add(scanner.nextLine()); // Store each line of the file in a list for processing in the second pass
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileLines;
    }

    public static void processInput(String fileLines) {

        // 6.  Your assembler must support the following instructions:  and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal.
        Set<String> keywords = new HashSet<>(Arrays.asList("and", "or", "add", "addi", "sll", "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"));
        Set<String> registers = new HashSet<>(Arrays.asList("$zero", "$at", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$k0", "$k1", "$gp", "$sp", "$fp", "$ra"));
        
        Set<String> foundKeywords = new HashSet<>();
        Set<String> foundRegisters = new HashSet<>();

        if (keywords.contains(fileLines)) { 
            
        }

    }

    public static void main(String[] args) {
        readFile("lab2/testprog1.asm");
        processInput();
    }

}

