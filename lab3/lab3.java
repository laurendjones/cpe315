package lab3;
// import java.util.*;
   // import java.io.*;

public class lab3 {

    public static int[] reg = new int[32]; 
    public static int[] mem;                     
    public static int pc = 0;
    public static final int memSize = 8192;

    public static void printHelp() {
        System.out.println("h = show help");
        System.out.println("d = dump register state");
        System.out.println("s = single step through the program (execute 1 instruction and stop)");
        System.out.println("s num = step through num instructions");
        System.out.println("r = run until the program ends");
        System.out.println("m num1 num2 = display data memory from location num1 to num2");
        System.out.println("c = clear all registers, memory, and the program counter to 0");
        System.out.println("q = exit the program");
    }

    public static void dumpRegisters() {
        System.out.println("\npc = " + pc);
        System.out.printf("$0 = %-10d $v0 = %-10d $v1 = %-10d $a0 = %-10d\n", reg[0], reg[2], reg[3], reg[4]);
        System.out.printf("$a1 = %-10d $a2 = %-10d $a3 = %-10d $t0 = %-10d\n", reg[5], reg[6], reg[7], reg[8]);
        //continue for all registers based on the MIPS register map
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
                printHelp();
                break;
            case "d":
                dumpRegisters();
                break;
            case "s":
                // single step through the program (i.e. execute 1 instruction and stop)
                break;
            case "s num":
                // step through num instructions of the program
                break;
            case "r":
                // run until the program ends
                break;
            case "m num1 num2":
                // display data memory from location num1 to num2
                break;
            case "c":
                // clear all registers, memory, and the program counter to 0
                break;
            case "q":
                // exit the program
                break;
            default:
                break;
        }
    } 

    public static void scriptMode() {
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
