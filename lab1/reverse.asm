#  Lab 1, Program 2
#  Lauren Jones, Sean Tracy
#  CPE 315


##  PROBLEM 2.  
#   Write a program which prints the number that represents reverse-ordered binary of the input number. 
#   Reverse the full 32-bit number (include leading 0's). This means the your program will print the 
#   32-bit number that is generated if the 32-bit input number's bits are written in reverse order 
#   (MSB becomes LSB and so on).  Name your file reverse.asm. 
#   Program 2 only needs to work with positive numbers as input.


## JAVA FUNCTION EQUIVALENT:
# import java.util.Scanner; 
# public class javaReverse {
# public static void main(String[] args) {
# // Create a Scanner object
# Scanner scanner = new Scanner(System.in); 
#     System.out.println("This program takes the reverse-ordered binary of the input number");
    
#     System.out.print("Enter an integer: ");
#     int num = scanner.nextInt(); // Read first integer

#     // Reverses the bits of num
#     int reversed = 0;
#         for (int i = 0; i < 32; i++) {
#             reversed <<= 1;           // Shift result left by 1
#             reversed |= (num & 1);    // Add least significant bit of num
#             num >>>= 1;               // Shift num right by 1 (unsigned)
#         }

#     System.out.println("Reverse:  " + reversed);

#     scanner.close(); 
#     }
# }

## ASSEMBLY CODE:
# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl reverseText

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program takes the reverse-ordered binary of the input number \n\n"

prompt:
	.asciiz " Enter an integer: "

reverseText: 
	.asciiz " \n Reverse = "

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
	lui $a0, 0x1001
	ori $a0, $a0, 0x47   # $a0 = 0x10010047
	syscall

    # Read 1st integer (num) from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Add 1st integer to register t0
	addu    $t0, $v0, $0

    # $t1 will hold the reversed result
    li   $t1, 0   

    # loop counter $t2    
    li   $t2, 32       

    reverse_loop:
        # shift result $t1 left by 1
        sll  $t1, $t1, 1   

        # extract LSB of input
        andi $t3, $t0, 1   

        # add extracted bit to result $t1
        or   $t1, $t1, $t3 

        # shift input right by 1
        srl  $t0, $t0, 1   

        # decrement counter $t2
        sub  $t2, $t2, 1   

        # if counter $t2 is not zero, repeat loop
        bnez $t2, reverse_loop

    # Display the reversed text
	ori     $v0, $0, 4			
    lui $a0, 0x1001
	ori $a0, $a0, 0x5D   # $a0 = 0x10010046
	syscall
	
	# Display the reversed number (in $t1)
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1
	add 	$a0, $t1, $0
	syscall

	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall