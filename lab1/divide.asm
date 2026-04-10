# Lab 1, Problem 3
#Java code
#method int divide(int a, int b) {
#    return a / b;
#}

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz "This program divides two numbers \n\n"

prompt_hi:
	.asciiz "\nEnter the upper 32 bits: "

prompt_lo:
	.asciiz "\nEnter the lower 32 bits: "

prompt_div:
	.asciiz "Enter the divisor: "

quotient_hi: 
	.asciiz "\nQuotient high = "
quotient_lo: 
    .asciiz "\nQuotient low = "
.text
main:
	# Display the welcome message (load 4 into $v0 to display)
	ori $v0, $0, 4	
	lui $a0, 0x1001
	syscall
	# Prompt_hi
	lui $a0, 0x1001
	addi $a0, $a0, 0x24
	syscall
	#save upper 32 bits to s0
	li $v0, 5
    syscall
	addu $s0, $v0, $0
	# Prompt_lo
	li $v0, 4
	lui $a0, 0x1001
	addi $a0, $a0, 0x40
	syscall
	#save lower 32 bits to s1
	li $v0, 5
	syscall
	addu $s1, $v0, $0
	# Prompt for divisor
	ori $v0, $0, 4
	lui $a0, 0x1001
	addi $a0, $a0, 0x5C
	syscall
	#save divisor to s2
	li $v0, 5
	syscall
	addu $s2, $v0, $0

	#s0 = hi, s1 = lo, s2 = divisor
