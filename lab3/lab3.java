package lab3;

import java.util.Scanner;

public class lab3 {

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

        }
        
        input.close(); 



    }

    public static void scriptMode() {

    }

    public static void main (String[] args) {
        // Your program should run from the command line with 1 optional argument: java lab3 assembly_file.asm script_file
    }
}
