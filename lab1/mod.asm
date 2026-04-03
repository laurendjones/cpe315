#  1.  Write the following fast "mod" function.  This function uses no modulus operator, multiplication,
# or division - it uses only basic arithmetic/logical operations (add, sub, and...).  
# The function takes two integers as inputs - a number (num), and a divisor (div).  
# You are guaranteed that div is a power of 2.  You want the remainder of num / div.  
# For example, if num=22 (00010110 in binary) and div = 4 (100) would return 2 (10). 
# Your algorithm should *not* repeatedly subtract (or add) div from num. Name your file mod.asm.  
# Program 1 only needs to work with positive numbers.

#  Lab 1, Program 1
#  Lauren Jones, Sean Tracy
#  CPE 315


# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl sumText

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program takes the modulus of two numbers \n\n"

prompt:
	.asciiz " Enter an integer: "

sumText: 
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
