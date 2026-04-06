#  Lab 1, Program 1
#  Lauren Jones, Sean Tracy
#  CPE 315


##  PROBLEM 1.
#   Write the following fast "mod" function.  This function uses no modulus operator, multiplication,
#   or division - it uses only basic arithmetic/logical operations (add, sub, and...).  
#   The function takes two integers as inputs - a number (num), and a divisor (div).  
#   You are guaranteed that div is a power of 2.  You want the remainder of num / div.  
#   For example, if num=22 (00010110 in binary) and div = 4 (100) would return 2 (10). 
#   Your algorithm should *not* repeatedly subtract (or add) div from num. Name your file mod.asm.  
#   Program 1 only needs to work with positive numbers.


## JAVA FUNCTION EQUIVALENT:
# import java.util.Scanner; 
#
# public class javamod {
#     public static void main(String[] args) {
#         // Create a Scanner object
#         Scanner scanner = new Scanner(System.in); 
#         System.out.println("This program takes the modulus of two numbers");

#         System.out.print("Enter an integer: ");
#         int num = scanner.nextInt(); // Read first integer

#         System.out.print("Enter an integer: ");
#         int div = scanner.nextInt(); // Read second integer
        
#         // Compute modulus using bitwise AND  
#         int modulus = num & (div - 1);

#         System.out.println("Modulus:  " + modulus);

#         scanner.close(); 
#     }
# }


## ASSEMBLY CODE:
# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl modulusText

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program takes the modulus of two numbers \n\n"

prompt:
	.asciiz " Enter an integer: "

modulusText: 
	.asciiz " \n Modulus = "

#Text Area (i.e. instructions)
.text

main:
# Display the welcome message (load 4 into $v0 to display)
	ori     $v0, $0, 4			

	# This generates the starting address for the welcome message.
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Display prompt
	ori     $v0, $0, 4		

    # This is the starting address of the prompt 
	la $a0, prompt
	syscall

	# Read 1st integer (num) from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Add 1st integer to register t0
	addu    $t0, $v0, $0

	# Display prompt (4 is loaded into $v0 to display)
	# 0x10010022 is hexidecimal for 34 decimal (the length of the previous welcome message)
	ori     $v0, $0, 4			
	la $a0, prompt
	syscall

	# Read 2nd integer 
	ori	$v0, $0, 5			
	syscall
	# $v0 now has the value of the second integer

	# Add 2nd integer to register t1
	addu    $t1, $v0, $0

    # Assume $t0 = num, $t1 = div
    # Compute mask = div - 1
    addi $t2, $t1, -1

    # Compute modulus: result = num & mask
    and $t3, $t0, $t2

    # $t3 now contains the result (num % div)

    # Display the modulus text
	ori     $v0, $0, 4			
    la      $a0, modulusText
	syscall
	
	# Display the modulus (in register t3)
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1
	add 	$a0, $t3, $0
	syscall

	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall
