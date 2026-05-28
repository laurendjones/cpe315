To run:
- javac --release 8 lab5\lab5.java lab5\assembler.java
- java -cp lab5 lab5 C:\Users\laure\OneDrive\Documents\Cal_Poly_CPE\CPE_315\cpe315\lab4\tests\lab4_fib20.asm lab5\tests\lab5.script 8 
- java lab5 tests\lab4_fib20.asm tests\lab4_test1.script

To submit in server:
- ssh ljones46@unix1.csc.calpoly.edu
- handin jseng CPE315_lab4
- git pull [inside directory]

Run makefile in server:
- make run ARGS="tests/lab4_test1.asm tests/lab4_test1.script"


CPE 315
Spring 2026
Seng
Lab #5
Complete by 11:59pm Thursday night (5/28/26)
Description:

For this lab, you will implement a correlating branch predictor in your simulator.  You only need to obtain the branch prediction accuracy.  When implementing the branch predictor, remember to use the GHR (Global History Register - the shift register) as an index into the array of 2-bit counters.  Initialize all 2-bit counters and the GHR to 0.  Provide accuracy results for when the GHR is 2, 4, and 8 bits. 


Implement the additional command in your simulator (should work in interactive and script):
   b = output the branch predictor accuracy.
With this additional branch predictor support, your simulator should accept a third optional argument which is the size of the GHR. If no GHR size argument is supplied, default to a size of 2. Here is an example command to run your simulator for this lab with a GHR size of 8:
java lab5 lab4_fib20.asm lab5.script 8
Results

lab5.script

lab5_ghr2.output

lab5_ghr4.output

lab5_ghr8.output

2-bit GHR lab4_fib20.asm: 61.79% (8360 correct predictions, 13529 predictions)
4-bit GHR lab4_fib20.asm: 80.83% (10935 correct predictions, 13529 predictions)
8-bit GHR lab4_fib20.asm: 91.34% (12357 correct predictions, 13529 predictions)

Hand in
Hand in all source files and Makefile.
