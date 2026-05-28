JC = javac
JVM = java
SRCS = assembler.java lab5.java
CLASSES = assembler.class lab5.class
MAIN = lab5

all: $(CLASSES)

%.class: %.java
	$(JC) $<

assembler.class: assembler.java
	$(JC) assembler.java

lab5.class: lab5.java assembler.class
	$(JC) lab5.java

run: $(CLASSES)
	$(JVM) $(MAIN) $(ASM) $(SCRIPT) $(GHR)

clean:
	rm -f $(CLASSES)

.PHONY: all run clean
