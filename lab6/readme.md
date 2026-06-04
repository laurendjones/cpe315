CPE 315

Spring 2026

Seng

Laboratory #6

Complete by 11:59pm Saturday 6/6/26

Objectives:

Build a cache simulator.

Description:


For this lab, you will implement a cache simulator.  This simulator will model 7 different cache configurations and print out the number of hits and the hit rate.  Simulate the following cache configurations (using LRU when appropriate and the cache is byte addressable):


2KB, direct mapped, 1-word blocks
2KB, direct mapped, 2-word blocks
2KB, direct mapped, 4-word blocks
2KB, 2-way set associative, 1-word blocks
2KB, 4-way set associative, 1-word blocks
2KB, 4-way set associative, 4-word blocks
4KB, direct mapped, 1-word blocks

Use the following 2 data files for your testing.  Each file contains 5 million memory reference addresses taken from actual running programs.  

Each line of the file contains a single 32-bit memory reference address.
Work with each address as an integer - (25% grade deduction - DO NOT store addresses as strings, array of chars, or array of individual bits)
Your code should be reasonably fast (less than 10-15 seconds on your local computer)

mem_stream1.zip (if the link does not work, use this address:  http://www.csc.calpoly.edu/~jseng/Spring26/lab6/mem_stream1.zip)
mem_stream2.zip (http://www.csc.calpoly.edu/~jseng/Spring26/lab6/mem_stream2.zip)

Your program should run in the following manner.  This command will run all 7 cache configurations with the mem_stream.1 file:


/opt/jdk-16/bin/java lab6 mem_stream.1


Output


Diff your program output against the following files:


mem_stream1.out
mem_stream2.out


Handin

 

Handin the following files (directory 315_lab6_1):


lab6.java (and any other header/support files)
Makefile