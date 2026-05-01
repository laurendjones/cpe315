package lab3;

   // import java.util.*;
   // import java.io.*;

public class lab3 {

    
    private static int pc = 0;
    private static int[] registers = new int[32]; // $0 is index 0, $ra is index 31
   // private static int[] dataMemory = new int[8192];


    public static void dumpRegisters() {
        System.out.println("\npc = " + pc);
        System.out.printf("$0 = %-10d $v0 = %-10d $v1 = %-10d $a0 = %-10d\n", registers[0], registers[2], registers[3], registers[4]);
        System.out.printf("$a1 = %-10d $a2 = %-10d $a3 = %-10d $t0 = %-10d\n", registers[5], registers[6], registers[7], registers[8]);
        // ... continue for all registers based on the MIPS register map
        System.out.println();
    }

    public void executeCommand(String command) {

        // The program should accept the following commands:
        // h = show help
        // d = dump register state
        // s = single step through the program (i.e. execute 1 instruction and stop)
        // s num = step through num instructions of the program
        // r = run until the program ends
        // m num1 num2 = display data memory from location num1 to num2
        // c = clear all registers, memory, and the program counter to 0
        // q = exit the program

        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "h":
                System.out.println("// The program should accept the following commands:\r\n" + //
                                        "        // h = show help\r\n" + //
                                        "        // d = dump register state\r\n" + //
                                        "        // s = single step through the program (i.e. execute 1 instruction and stop)\r\n" + //
                                        "        // s num = step through num instructions of the program\r\n" + //
                                        "        // r = run until the program ends\r\n" + //
                                        "        // m num1 num2 = display data memory from location num1 to num2\r\n" + //
                                        "        // c = clear all registers, memory, and the program counter to 0\r\n" + //
                                        "        // q = exit the program");
                break;
            case "d":
                dumpRegisters();
                break;
        }
    }

    public static void main (String[] args) {
        // Your program should run from the command line with 1 optional argument: java lab3 assembly_file.asm script_file
        if (args.length < 1) {
            System.out.println("Usage: java lab3 assembly_file.asm [script_file]");
            return;
        }

        // 1. Load and Parse assembly_file.asm using your Lab 2 code here
        
        /* // 2. Check for script file
        if (args.length == 2) {
            runScriptMode(args[1]);
        } else {
            runInteractiveMode();
        } */
    //}
    }
}
