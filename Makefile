JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	engine/*.java \
	parser/*.java \

all:
	cd engine; make
	cd parser; make

default: classes

classes: $(CLASSES:.java=.class)

clean:
	cd engine; $(RM) *.class
	cd parser; $(RM) *.class

