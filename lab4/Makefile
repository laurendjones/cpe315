JAVAC = javac
JAVA  = java

SRCS  = assembler.java lab4.java
MAIN  = lab4

.PHONY: all run clean

all: $(SRCS:.java=.class)

%.class: %.java
	$(JAVAC) $<

assembler.class: assembler.java
	$(JAVAC) assembler.java

lab4.class: lab4.java assembler.class
	$(JAVAC) lab4.java

run: all
	$(JAVA) $(MAIN) $(ARGS)

clean:
	rm -f *.class
