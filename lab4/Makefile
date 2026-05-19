JC = javac
JVM = java
FLAGS = -encoding UTF-8

SRCS = assembler.java lab4.java
CLASSES = assembler.class lab4.class

all: $(CLASSES)

assembler.class: assembler.java
	$(JC) $(FLAGS) assembler.java

lab4.class: lab4.java assembler.class
	$(JC) $(FLAGS) lab4.java

run: $(CLASSES)
	$(JVM) lab4 $(ASM) $(SCRIPT)

clean:
	rm -f *.class

.PHONY: all run clean
