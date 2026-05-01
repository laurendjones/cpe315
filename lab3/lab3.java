package lab3;

import java.util.Scanner;

public class lab3 {

    public int[] reg = new int[32]; 
    public int[] mem;                     
    public int pc = 0;
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

    public static void dumpRegisterState(int[] reg) {
        for (int i = 0; i < reg.length; i++) {
            System.out.println("R" + i + ": " + reg[i]);
        }
    }

    public static void interactiveMode() {
        // display prompt
        Scanner input = new Scanner(System.in);
        
        System.out.println("mips>");
        String command = input.nextLine(); // Read String input

        // The program should accept the following commands:
        // h = show help
        // d = dump register state
        // s = single step through the program (i.e. execute 1 instruction and stop)
        // s num = step through num instructions of the program
        // r = run until the program ends
        // m num1 num2 = display data memory from location num1 to num2
        // c = clear all registers, memory, and the program counter to 0
        // q = exit the program

        if ((command == "h") || (command == "d") || (command == "s") || (command == "s num") || (command == "r") || (command == "m num1 num2") || (command == "c") || (command == "q")) {
            switch (command) {
                case "h":
                    printHelp();
                    break;
                case "d":
                    // dump register state
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

        input.close(); 



    }

    public static void scriptMode() {

    }

    public static void main (String[] args) {
        // Your program should run from the command line with 1 optional argument: java lab3 assembly_file.asm script_file
    }
}
