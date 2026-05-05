import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class lab3 {

    public static int[] reg = new int[32]; 
    public static int[] mem;                     
    public static int pc = 0;
    public static final int memSize = 8192;

    public static Map<Integer, String> instructionMap = new HashMap<>();

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
        System.out.printf("$t1 = %-10d $t2 = %-10d $t3 = %-10d $t4 = %-10d\n", reg[9], reg[10], reg[11], reg[12]);
        System.out.printf("$t5 = %-10d $t6 = %-10d $t7 = %-10d $s0 = %-10d\n", reg[13], reg[14], reg[15], reg[16]);
        System.out.printf("$s1 = %-10d $s2 = %-10d $s3 = %-10d $s4 = %-10d\n", reg[17], reg[18], reg[19], reg[20]);
        System.out.printf("$s5 = %-10d $s6 = %-10d $s7 = %-10d $t8 = %-10d\n", reg[21], reg[22], reg[23], reg[24]);
        System.out.printf("$t9 = %-10d $sp = %-10d $ra = %-10d\n", reg[25], reg[29], reg[31]);
        System.out.println();
    }

    public static void clearState() {
        reg = new int[32];
        mem = new int[memSize];
        pc = 0;
    }

    public static boolean executeInstruction() {
        if (!instructionMap.containsKey(pc)) {
                System.out.println("No instruction at pc: " + pc);
                return false;
        } else {
            int word = parsedInstructions.get(pc);
            

    //         String instruction = instructionMap.get(pc);
    //         String[] parts = instruction.split(" ");
    //         String opcode = parts[0];

    //         pc += 4;
    //         switch (opcode) {
    //             case "add":
    //                 int rd = assembler.reg(parts[1]);
    //                 int rs = assembler.reg(parts[2]);
    //                 int rt = assembler.reg(parts[3]);
    //                 reg[rd] = reg[rs] + reg[rt];
    //                 break;
    //             case "addi":
    //                 rt = assembler.reg(parts[1]);
    //                 rs = assembler.reg(parts[2]);
    //                 int imm = Integer.parseInt(parts[3]);
    //                 reg[rt] = reg[rs] + imm;
    //                 break;
    //             case "sub":
    //                 rd = assembler.reg(parts[1]);
    //                 rs = assembler.reg(parts[2]);
    //                 rt = assembler.reg(parts[3]);
    //                 reg[rd] = reg[rs] - reg[rt];
    //                 break;
    //             case "and":
    //                 rd = assembler.reg(parts[1]);
    //                 rs = assembler.reg(parts[2]);
    //                 rt = assembler.reg(parts[3]);
    //                 reg[rd] = reg[rs] & reg[rt];
    //                 break;
    //             case "or":
    //                 rd = assembler.reg(parts[1]);
    //                 rs = assembler.reg(parts[2]);
    //                 rt = assembler.reg(parts[3]);
    //                 reg[rd] = reg[rs] | reg[rt];
    //                 break;
    //             case "slt":
    //                 rd = assembler.reg(parts[1]);
    //                 rs = assembler.reg(parts[2]);
    //                 rt = assembler.reg(parts[3]);
    //                 reg[rd] = (reg[rs] < reg[rt]) ? 1 : 0;
    //                 break;
    //             case "sll":
    //                 rd = assembler.reg(parts[1]);
    //                 rt = assembler.reg(parts[2]);
    //                 int shamt = Integer.parseInt(parts[3]);
    //                 reg[rd] = reg[rt] << shamt;
    //                 break;
    //             case "jr":
    //                 rs = assembler.reg(parts[1]);
    //                 pc = reg[rs];
    //                 break;
    //             case "lw":
    //                 rt = assembler.reg(parts[1]);
    //                 int offset = Integer.parseInt(parts[2]);
    //                 rs = assembler.reg(parts[3]);
    //                 int address = (reg[rs] + offset) / 4;
    //                 if (address < 0 || address >= memSize) {
    //                     System.out.println("Memory access out of bounds at address: " + address);
    //                 } else {
    //                     reg[rt] = mem[address];
    //                 }
    //                 break;
    //             case "sw":
    //                 rt = assembler.reg(parts[1]);
    //                 offset = Integer.parseInt(parts[2]);
    //                 rs = assembler.reg(parts[3]);
    //                 address = (reg[rs] + offset) / 4;
    //                 if (address < 0 || address >= memSize) {
    //                     System.out.println("Memory access out of bounds at address: " + address);
    //                 } else {
    //                     mem[address] = reg[rt];
    //                 }
    //                 break;
    //             case "beq":
    //                 rs = assembler.reg(parts[1]);
    //                 rt = assembler.reg(parts[2]);
    //                 //Need to add here
    //                 break;
    //             case "bne":
    //                 rs = assembler.reg(parts[1]);
    //                 rt = assembler.reg(parts[2]);
    //                 //Need to add here
    //                 break;
    //             case "j":
    //                 // j instruction logic
    //                 break;
    //             case "jal":
    //                 String label = parts[1];
    //                 reg[31] = pc;
    //                 // Jump to label logic
    //                 break;
    //             default:
    //                 System.out.println("Unknown instruction: " + opcode);
    //         }
    //     }
    //     reg[0] = 0;
    //     return true;
    // }



    public static void executeCommand(String command) {


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
                if (parts.length == 1) {
                    if (!executeInstruction()) {
                        System.out.println("Program has ended");
                        break;
                    }
                } else { //s num
                    int num = Integer.parseInt(parts[1]);
                    for (int i = 0; i < num; i++) {
                        if (!executeInstruction()) {
                            System.out.println("Program has ended");
                            break;
                        }
                    }
                }
                break;
            case "r":
                while (executeInstruction()) {
                }
                System.out.println("Program has ended");
                break;
            case "m":
                if (parts.length != 3) {
                    System.out.println("Invalid command. Use format: m num1 num2");
                    break;
                } else {
                    int num1 = Integer.parseInt(parts[1]);
                    int num2 = Integer.parseInt(parts[2]);
                    for (int i = num1; i <= num2; i++) {
                        if (i < 0 || i >= memSize) {
                            System.out.println("Memory access out of bounds");
                        } else {
                            System.out.println("memory[" + i + "] = " + mem[i]);
                        }
                    }
                }
                // display data memory from location num1 to num2
                break;
            case "c":
                clearState();
                break;
            case "q":
                System.exit(0);
                break;
            default:
                break;
        }
    } 

    public static void runMode(Scanner scanner, boolean isInteractive) {
    while (true) {
        if (isInteractive) {
            System.out.print("mips> ");
            System.out.flush();
        }

        if (!scanner.hasNextLine()) {
            break; // End of file or input stream
        }

        String command = scanner.nextLine().trim();
        
        // SCRIPT MODE 
        if (!isInteractive) {
            System.out.println("mips> " + command);
        }

        if (command.equals("q")) {
            break;
        }

        if (!command.isEmpty()) {
            executeCommand(command);
        }
    }
}

    public static void main (String[] args) {
        // Your program should run from the command line with 1 optional argument: java lab3 assembly_file.asm script_file
        if (args.length < 1) {
            System.out.println("Usage: java lab3 assembly_file.asm [script_file]");
            return;
        }

        clearState();

        assembler myAssembler = new assembler();
        instructionMap = myAssembler.parseFile(args[0]);

        try {
            if (args.length == 2) {
                // SCRIPT MODE: Read from the file provided in args[1]
                File scriptFile = new File(args[1]);
                Scanner scriptScanner = new Scanner(scriptFile);
                System.out.println("Running in Script Mode...");
                runMode(scriptScanner, false); 
            } else {
                // INTERACTIVE MODE: Read from System.in (Keyboard)
                Scanner interactiveScanner = new Scanner(System.in);
                runMode(interactiveScanner, true);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Script file not found.");
        }
    }

}

