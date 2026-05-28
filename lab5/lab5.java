import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

// Lauren Jones, Sean Tracy 


public class lab5 {

    public static int[] reg = new int[32]; 
    public static int[] mem;                     
    public static int pc = 0;
    public static final int memSize = 8192;
    public static int fetchPC = 0;
    public static boolean branchTaken = false;
    public static int branchTarget = -1;

    public static String if_id = "empty";
    public static String id_exe = "empty";
    public static String exe_mem = "empty";
    public static String mem_wb = "empty";

    // Counters
    public static int cycles = 0;
    public static int stallCycles = 0;
    public static int instructionsCount = 0;

    public static Map<Integer, String> instructionMap = new HashMap<>();
    public static Map<Integer, String> labelMap = new HashMap<>();
    public static Map<String, Integer> newLabelMap = new HashMap<>();

    public static int ghrSize = 2;
    public static int ghr = 0;
    public static int[] predictionTable;
    public static int branchTotal = 0;
    public static int correctPredictions = 0;

    public static void predictorInit() {
        int tableSize = 1 << ghrSize;
        predictionTable = new int[tableSize];
        ghr = 0;
    }

    public static boolean predict() {
        int index = ghr & ((1 << ghrSize) - 1);
        return predictionTable[index] >= 2;
    }

    public static void updatePredictor(boolean taken) {
        int index = ghr & ((1 << ghrSize) - 1);
        if (taken) {
            if (predictionTable[index] < 3) {
                predictionTable[index]++;
            }
        } else {
            if (predictionTable[index] > 0) {
                predictionTable[index]--;
            }
        }
        ghr = ((ghr << 1) | (taken ? 1 : 0)) & ((1 << ghrSize) - 1);
    }


    public static void stepCycle() {
        cycles++;

        // 1. WB
        if (!mem_wb.equals("empty") && !mem_wb.equals("squash") && !mem_wb.equals("stall")) {
            instructionsCount++;
        }    

        // 2. MEM
        mem_wb = exe_mem;

       if (stallCycles > 0 ) {
            if (branchTaken && stallCycles == 1) {
                if_id = "squash";
                id_exe = "squash";
                exe_mem = "squash";
                pc = branchTarget;
                fetchPC = (pc / 4);
                stallCycles = 0;
                return;
            } else if (branchTaken) {
                stallCycles--;
            } else {
                exe_mem = id_exe;;
                id_exe = if_id;
                if_id = "squash";
                fetchPC = (pc / 4);
                stallCycles--;
                return;
            }
       }

        if (id_exe != null && !id_exe.equals("empty") && !id_exe.equals("stall") && id_exe.split(" ")[0].equals("lw")) {
            String[] lwParts = id_exe.split(" ");
            int lwRt = assembler.reg(lwParts[1]);

            if (!if_id.equals("empty") && !if_id.equals("stall")) {
                String[] instr = if_id.split(" ");
                String opcode = instr[0];
                int currentRs = -1;
                int currentRt = -1;

                if (instr.length > 2) {
                    if (opcode.equals("addi") || opcode.equals("lw") || opcode.equals("sw")){
                        currentRs = assembler.reg(instr[2]);
                    }
                } 
                if (opcode.equals("add") || opcode.equals("sub") || opcode.equals("slt") || opcode.equals("and") || opcode.equals("or")) {
                    currentRs = assembler.reg(instr[2]);
                    currentRt = assembler.reg(instr[3]);
                }

                if (lwRt == currentRs || lwRt == currentRt) {           
                    exe_mem = id_exe;
                    id_exe = "stall";
                    return;
                }
            }
        }

        // 3. EX
        exe_mem = id_exe;
        
        // 4. ID
        id_exe = if_id;

        // 5. IF
        if (instructionMap.containsKey(pc)) {
            String instruction = instructionMap.get(pc);
            fetchPC = (pc / 4) + 1;
            if_id = instruction; //was opcode

            int penalty = handleHazard(instruction);

            pc += 4;
            if (!branchTaken) {
                executeInstruction(instruction);
            }
    
            if (stallCycles == 0) {
                stallCycles = penalty;
                if (penalty == 3) {
                    branchTaken = true;
                    branchTarget = pc;
                    pc = fetchPC * 4;
                } else {
                    branchTaken = false;
                }
            }
        } else {
            if_id = "empty";
        }
    }

    public static int handleHazard(String currentInstruction) {
        if (currentInstruction == null || currentInstruction.equals("empty")) {
            return 0;
        }
       
        String[] instr = currentInstruction.split(" ");
        String opcode = instr[0];
       
        // 1. Conditional branches (3 cycles)
        if (opcode.equals("beq") || opcode.equals("bne")) {
            int rs = assembler.reg(instr[1]);
            int rt = assembler.reg(instr[2]);
            boolean branchTaken;
            if (opcode.equals("beq")) {
                branchTaken = (reg[rs] == reg[rt]);
            } else { // bne
                branchTaken = (reg[rs] != reg[rt]);
            }

            boolean prediction = predict();
            branchTotal++;
            if (prediction == branchTaken) {
                correctPredictions++;
                updatePredictor(branchTaken);
            }
            if (branchTaken) {
                return 3;
            }
            return 0;
        }

        // 2. Use-after-load condition (1 cycle)
        if (id_exe != null && !id_exe.equals("empty") && id_exe.split(" ")[0].equals("lw")) {
            String[] lwParts = id_exe.split(" ");
            int lwRt = assembler.reg(lwParts[1]);

            int currentRs = -1;
            int currentRt = -1;

            // I-type (addi, lw, sw)
            if (instr.length > 2) {
                if (opcode.equals("addi") || opcode.equals("lw") || opcode.equals("sw")){
                    currentRs = assembler.reg(instr[2]);
                }
            } 
            
            //// R-type (add, sub, slt, and, or)
            if (opcode.equals("add") || opcode.equals("sub") || opcode.equals("slt") || opcode.equals("and") || opcode.equals("or")) {
                    currentRs = assembler.reg(instr[2]);
                    currentRt = assembler.reg(instr[3]);
                }

            if (lwRt == currentRs || lwRt == currentRt) {
                return 0;
            }
        }
    

        // 3. Unconditional branch (j, jal, jr) (1 cycle)
        if (opcode.equals("j") || opcode.equals("jal") || opcode.equals("jr")) {
            return 1;
        }

        return 0;
    }

    public static void printSummary() {
        double cpi = (instructionsCount == 0) ? 0 : (double) cycles / instructionsCount;
        System.out.println("\nProgram complete");
        System.out.printf("CPI = %.3f\tCycles  = %d\tInstructions = %d\n\n", cpi, cycles, instructionsCount);
    }

    public static void printHelp() {
        System.out.println("h = show help");
        System.out.println("d = dump register state");
        System.out.println("p = show pipeline registers");
        System.out.println("s = single step through the program (execute 1 instruction and stop)");
        System.out.println("s num = step through num instructions");
        System.out.println("r = run until the program ends and display timing summary");
        System.out.println("m num1 num2 = display data memory from location num1 to num2");
        System.out.println("c = clear all registers, memory, and the program counter to 0");
        System.out.println("q = exit the program");
    }

    public static void dumpRegisters() {
        System.out.println("\npc = " + (pc / 4));
        System.out.printf("$0 = %-10d $v0 = %-10d $v1 = %-10d $a0 = %-10d\n", reg[0], reg[2], reg[3], reg[4]);
        System.out.printf("$a1 = %-10d $a2 = %-10d $a3 = %-10d $t0 = %-10d\n", reg[5], reg[6], reg[7], reg[8]);
        System.out.printf("$t1 = %-10d $t2 = %-10d $t3 = %-10d $t4 = %-10d\n", reg[9], reg[10], reg[11], reg[12]);
        System.out.printf("$t5 = %-10d $t6 = %-10d $t7 = %-10d $s0 = %-10d\n", reg[13], reg[14], reg[15], reg[16]);
        System.out.printf("$s1 = %-10d $s2 = %-10d $s3 = %-10d $s4 = %-10d\n", reg[17], reg[18], reg[19], reg[20]);
        System.out.printf("$s5 = %-10d $s6 = %-10d $s7 = %-10d $t8 = %-10d\n", reg[21], reg[22], reg[23], reg[24]);
        System.out.printf("$t9 = %-10d $sp = %-10d $ra = %-10d\n", reg[25], reg[29], reg[31]);
        System.out.println();
    }

    public static void dumpPipelineRegisters() {
        System.out.println("\npc\tif/id\tid/exe\texe/mem\tmem/wb");
        String if_id_op = (if_id.equals("empty") || if_id.equals("squash") || if_id.equals("stall")) ? if_id : if_id.split(" ")[0];
        String id_exe_op = (id_exe.equals("empty") || id_exe.equals("squash") || id_exe.equals("stall")) ? id_exe : id_exe.split(" ")[0];
        String exe_mem_op = (exe_mem.equals("empty") || exe_mem.equals("squash") || exe_mem.equals("stall")) ? exe_mem : exe_mem.split(" ")[0];
        String mem_wb_op = (mem_wb.equals("empty") || mem_wb.equals("squash") || mem_wb.equals("stall")) ? mem_wb : mem_wb.split(" ")[0];
        System.out.printf("%d\t%s\t%s\t%s\t%s\n", fetchPC, if_id_op, id_exe_op, exe_mem_op, mem_wb_op);
        System.out.println();
        }
    

    public static void clearState() {
        reg = new int[32];
        mem = new int[memSize];
        pc = 0;
        fetchPC = 0;
        if_id = "empty";
        id_exe = "empty";
        exe_mem = "empty";
        mem_wb = "empty";
        cycles = 0;
        stallCycles = 0;
        instructionsCount = 0;
        branchTaken = false;
        branchTarget = -1;
        branchTotal = 0;
        correctPredictions = 0;
         predictorInit();
        // System.out.println("Simulator reset");
    }

    public static void PrintBranchAccuracy() {
        if (branchTotal == 0) {
            System.out.println("No branches executed.");
        } else {
            double accuracy = (double) correctPredictions / branchTotal * 100;
            System.out.printf("Branch Prediction Accuracy: %.2f%% (%d/%d)\n", accuracy, correctPredictions, branchTotal);
        }
    }

    public static int labelToAddress(String label) {
        if (!newLabelMap.containsKey(label)) {
            System.out.println("Label not found: " + label);
            System.exit(1);
        }
        return newLabelMap.get(label);
    }

    public static boolean executeInstruction(String instruction) {
        if (instruction == null || instruction.equals("empty")) {
                return false;
        }
            

            String[] parts = instruction.split(" ");
            String opcode = parts[0];

            //pc += 4;
            switch (opcode) {
                case "add":
                    int rd = assembler.reg(parts[1]);
                    int rs = assembler.reg(parts[2]);
                    int rt = assembler.reg(parts[3]);
                    reg[rd] = reg[rs] + reg[rt];
                    break;
                case "addi":
                    rt = assembler.reg(parts[1]);
                    rs = assembler.reg(parts[2]);
                    int imm = Integer.parseInt(parts[3]);
                    reg[rt] = reg[rs] + imm;
                    break;
                case "sub":
                    rd = assembler.reg(parts[1]);
                    rs = assembler.reg(parts[2]);
                    rt = assembler.reg(parts[3]);
                    reg[rd] = reg[rs] - reg[rt];
                    break;
                case "and":
                    rd = assembler.reg(parts[1]);
                    rs = assembler.reg(parts[2]);
                    rt = assembler.reg(parts[3]);
                    reg[rd] = reg[rs] & reg[rt];
                    break;
                case "or":
                    rd = assembler.reg(parts[1]);
                    rs = assembler.reg(parts[2]);
                    rt = assembler.reg(parts[3]);
                    reg[rd] = reg[rs] | reg[rt];
                    break;
                case "slt":
                    rd = assembler.reg(parts[1]);
                    rs = assembler.reg(parts[2]);
                    rt = assembler.reg(parts[3]);
                    reg[rd] = (reg[rs] < reg[rt]) ? 1 : 0;
                    break;
                case "sll":
                    rd = assembler.reg(parts[1]);
                    rt = assembler.reg(parts[2]);
                    int shamt = Integer.parseInt(parts[3]);
                    reg[rd] = reg[rt] << shamt;
                    break;
                case "jr":
                    rs = assembler.reg(parts[1]);
                    pc = (reg[rs] * 4);
                    break;
                case "lw":
                    rt = assembler.reg(parts[1]);
                    int offset = Integer.parseInt(parts[2]);
                    rs = assembler.reg(parts[3]);
                    int address = (reg[rs] + offset);
                    if (address < 0 || address >= memSize) {
                        System.out.println("Memory access out of bounds at address: " + address);
                    } else {
                        reg[rt] = mem[address];
                    }
                    break;
                case "sw":
                    rt = assembler.reg(parts[1]);
                    offset = Integer.parseInt(parts[2]);
                    rs = assembler.reg(parts[3]);
                    address = (reg[rs] + offset);
                    if (address < 0 || address >= memSize) {
                        System.out.println("Memory access out of bounds at address: " + address);
                    } else {
                        mem[address] = reg[rt];
                    }
                    break;
                case "beq":
                    rs = assembler.reg(parts[1]);
                    rt = assembler.reg(parts[2]);
                    String label = parts[3];
                    if (reg[rs] == reg[rt]) {
                        pc = labelToAddress(label);
                    }
                    break;
                case "bne":
                    rs = assembler.reg(parts[1]);
                    rt = assembler.reg(parts[2]);
                    label = parts[3];
                    if (reg[rs] != reg[rt]) {
                        pc = labelToAddress(label);
                    }
                    break;
                case "j":
                    label = parts[1];
                    pc = labelToAddress(label);
                    break;
                case "jal":
                    label = parts[1];
                    reg[31] = (pc / 4);
                    pc = labelToAddress(label);
                    break;
                default:
                    System.out.println("Unknown instruction: " + opcode);
            }
            reg[0] = 0;
            return true;
        }



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
            case "p":
                dumpPipelineRegisters();
                break;
            case "b":
                // accuracy 61.79% (8360 correct predictions, 13529 predictions) 
                float accuracy = correctPredictions / predictions;
                System.out.println("accuracy ", accuracy, "% (", correctPredictions, " correct predictions, ", predictions, " predictions)");
                break;
            case "s":
                if (parts.length == 1) {
                    stepCycle();
                    dumpPipelineRegisters();
                } else { 
                    int num = Integer.parseInt(parts[1]);
                    for (int i = 0; i < num; i++) {
                        stepCycle();
                    }
                    dumpPipelineRegisters();
                }
                break;
            case "r":
                while (instructionMap.containsKey(pc) || stallCycles > 0 ||
                    branchTaken || !if_id.equals("empty") || !id_exe.equals("empty") ||
                    !exe_mem.equals("empty") || !mem_wb.equals("empty")) {
                        stepCycle();
                }
                printSummary();
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
                break;
            case "b":
                PrintBranchAccuracy();
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
            break;
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
        if (args.length < 1) {
            System.out.println("Usage: java lab4 assembly_file.asm [script_file]");
            return;
        }

        clearState();

        assembler myAssembler = new assembler();

        // Get assembler result
        Map<String, Object> assemblerOutput = myAssembler.parseFile(args[0]);

        instructionMap = (Map<Integer, String>) assemblerOutput.get("instructions");
        labelMap = (Map<Integer, String>) assemblerOutput.get("labels");


        for (Map.Entry<Integer, String> entry : labelMap.entrySet()) {
            newLabelMap.put(entry.getValue(), entry.getKey());
        }

        try {
            if (args.length == 2) {
                // SCRIPT MODE: Read from the file provided in args[1]
                File scriptFile = new File(args[1]);
                Scanner scriptScanner = new Scanner(scriptFile);
                //System.out.println("Running in Script Mode...");
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

