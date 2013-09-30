JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java


CLASSES = \
        CopyOnWriteBuffer.java \
        CopyOnWriteByteBuffer.java \
        CopyOnWriteBufferProvider.java \
        CopyOnWriteByteBufferProvider.java \
        CopyOnWriteRunner.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
