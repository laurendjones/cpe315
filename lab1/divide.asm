# divide_java(int hi, int lo, int divisor) {
# // s0 = hi, s1 = lo, s2 = divisor
#         int k = 0;

#         while (divisor != 1) {
#             divisor = divisor >>> 1;
#             k++;
#         }

#         int quotient_hi = hi >>> k;          
#         int t2 = 32 - k;
#         int t3 = 1 << k;
#         t3 = t3 - 1;
#         int t4 = hi & t3;
#         t4 = t4 << t2;
#         int t5 = lo >>> k;
#         int quotient_lo = t4 | t5;
# 	}

.data

welcome:
	.asciiz "This program divides two numbers\n"

prompt_hi:
	.asciiz "\nEnter the upper 32 bits: "

prompt_lo:
	.asciiz "\nEnter the lower 32 bits: "

prompt_div:
	.asciiz "\nEnter the divisor: "

space_comma: 
	.asciiz "\n,  "

.text
main:
	# Display the welcome message (load 4 into $v0 to display)
	ori $v0, $0, 4	
	lui $a0, 0x1001
	syscall
	# Prompt_hi
	lui $a0, 0x1001
	ori $a0, $a0, 0x22
	syscall
	#save upper 32 bits to s0
	li $v0, 5
    syscall
	addu $s0, $v0, $0
	# Prompt_lo
	li $v0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x3D
	syscall
	#save lower 32 bits to s1
	li $v0, 5
	syscall
	addu $s1, $v0, $0
	# Prompt for divisor
	ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x58
	syscall
	#save divisor to s2
	li $v0, 5
	syscall
	addu $s2, $v0, $0

	#s0 = hi, s1 = lo, s2 = divisor

	li $t0, 0

	loop:
	beq $s2, 1, divide
	srl $s2, $s2, 1
	addi $t0, $t0, 1
	j loop

	divide:

	srl $t1, $s0, $t0 #quo high
	li $t2, 32
	sub $t2, $t2, $t0
	li $t3, 1
	sll $t3, $t3, $t0
	addi $t3, $t3, -1
	and $t4, $s0, $t3
	sll $t4, $t4, $t2
	srl $t5, $s1, $t0
	or $t6, $t4, $t5

	ori $v0, $0, 1			
	addu $a0, $t1, $0
	syscall
	ori $v0, $0, 4			
	lui $a0, 0x1001
	ori $a0, $a0, 0x6E
	syscall
	ori $v0, $0, 1			
	addu $a0, $t6, $0
	syscall

	end_loop:
	j end_loop

	
