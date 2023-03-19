.data
	arr: .word 7, 9, 4, 3, 8, 1, 6, 2, 5
	countArr: .word 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	output: .space 36
	sizeOfArr: .word 36
	sizeOfElement: .word 4
.text
		
#Uses S1-Array Address S2-Array Size			
main:
	la $s0, arr
	lw $t0, sizeOfArr
	lw $t1, sizeOfElement
	div $t0, $t1
	mflo $s1
	la $a0, ($s0)
	move $a1, $s1
	jal radixSort
	jal printData
	li $v0, 10
	syscall
	
#All temp variables
getMax:
	move $t0, $a0
	lw $v0, ($t0)
	move $t1, $a1
	addi $t0, $t0, 4
	addi $t1, -1
maxLoop:
	lw $t2, ($t0)
	slt $t3, $t2, $v0
	bgtz $t3, noDiff
	move $v0, $t2
noDiff:	addi $t1, -1
	addi $t0, $t0, 4
	blez $t1, printEnd
	j maxLoop
maxEnd:
	jr $ra	
	
#Uses S3-Max S4-Loop Variable S5-Return Address
radixSort:
	li $s4, 1
	move $s5, $ra
	li $s6, 10
	jal getMax
	move $s3, $v0
	move $a2, $s4
radixLoop:   		
	div $s3, $s4		
	mflo $t0
	blez $t0, radixEnd
	jal countSort
	mult $s4, $s6
	mflo $s4
	j radixLoop
radixEnd: 
	move $ra, $s5
	jr $ra

#All temp 
printData:
	move $t0, $a0
	move $t1, $a1
	li $v0, 1
printLoop:
	lw $a0, ($t0)
	syscall
	addi $t1, -1
	addi $t0, $t0, 4
	blez $t1, printEnd
	j printLoop
printEnd:
	jr $ra	

#t0- count address t1- output address t2- loop index t4- const(10) s7- const(4) 
countSort:	
	li $s7, 4
	la $t0, countArr
	la $t1, output
	li $t2, 0
	li $t3, 0
	li $t4, 10
initializeLoop: 
	beq $t2, $t4, initializeEnd
	sw $t3, countArr($t2)
	addi $t2, 1
initializeEnd:
	li $t2, 0
firstLoop:
	beq $t2, $a1, firstLoopEnd
	mult $t2, $s7
	mflo $t5
	add $t5, $a0, $t5
	lw $t5, ($t5)
	div $t5, $a2
	mflo $t5
	div $t5, $t4	
	mfhi $t5
	mult $t5, $s7
	mflo $t5
	add $t5, $t5, $t0
	lw $t6, ($t5)
	addi $t6, 1
	sw $t6, ($t5)	
	addi $t2, 1
	j firstLoop
firstLoopEnd:
	li $t2, 1
secondLoop:
	beq $t2, $t4, secondLoopEnd
	mult $t2, $s7
	mflo $t5
	add $t5, $t0, $t5
	addi $t5, -4
	lw $t3, ($t5)
	addi $t5, 4
	lw $t7, ($t5)
	add $t6, $t7, $t3
	sw $t6, ($t5)
	addi $t2, 1
	j secondLoop
secondLoopEnd:
	move $t2, $a1
	addi $t2, -1
	li $t3, -1
thirdLoop:
	beq $t2, $t3, thirdLoopEnd
	mult $t2, $s7
	mflo $t5
	add $t5, $t5, $a0
	lw $t5, ($t5)
	div $t5, $a2
	mflo $t6
	div $t6, $t4
	mfhi $t6
	mult $t6, $s7
	mflo $t6
	add $t6, $t6, $t0
	lw $t7, ($t6)
	addi $t7, -1
	mult $t7, $s7
	mflo $t7
	add $t7, $t7, $t1
	sw $t5, ($t7)
	lw $t5, ($t6)
	addi $t5, -1 
	sw $t5, ($t6)
	addi $t2, -1
	j thirdLoop
thirdLoopEnd:
	li $t2, 0
fourthLoop:
	beq $t2, $a1, countSortEnd
	mult $t2, $s7
	mflo $t3
	add $t4, $a0, $t3
	add $t5, $t1, $t3
	lw $t5, ($t5)
	sw $t5, ($t4)
	addi $t2, 1
	j fourthLoop
countSortEnd:
	jr $ra




