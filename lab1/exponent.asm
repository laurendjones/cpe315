#Java code
# exponentiate(int base, int exp) {
#     int result = 1;
#     for (int i = 0; i < exp; i++) {
#         result *= base;
#     }
#     return result;
# }



.data

welcome:
	.asciiz "This program exponentiates two numbers\n"

prompt_base:
	.asciiz "\nEnter the base: "

prompt_exp:
	.asciiz "\nEnter the exponent: "

result_text:
    .asciiz "\nResult = "
.text
main:
	ori $v0, $0, 4	
	lui $a0, 0x1001
	syscall
	# Prompt_base
    ori $v0, $0, 4
	lui $a0, 0x1001
	ori $a0, $a0, 0x28
	syscall
    ori $v0, $0, 5
    syscall
	addu $a1, $v0, $0
    # Prompt_exp
    ori $v0, $0, 4
    lui $a0, 0x1001
    ori $a0, $a0, 0x3A
    syscall
    ori $v0, $0, 5
    syscall
    addu $a2, $v0, $0

    # $a1 = base, $a2 = exp, $a3 = result

    # Initialize to 1
    li $t0, 1
    # Loop until exp is 0
    exp_loop:
    beqz $a2, exp_done
    li $t2, 0
    add $t1, $a1, $0

    mul_loop:
    beqz $t1, mul_done
    addu $t2, $t2, $t0
    addi $t1, $t1, -1
    j mul_loop
    mul_done:
    addu $t0, $t2, $0
    addi $a2, $a2, -1
    j exp_loop

    exp_done:
    # Display result
    ori $v0, $0, 4
    lui $a0, 0x1001
    ori $a0, $a0, 0x50
    syscall
    ori $v0, $0, 1    # syscall 1 = print integer
    addu $a0, $t0, $0 # move result into $a0
    syscall

    end_loop:
    j end_loop




