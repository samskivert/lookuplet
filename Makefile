#
# $Id$
#
# Simple Makefile for lookuplet

GCJ=gcj-4.2

SRCDIR=src/java/com/samskivert/lookuplet
SOURCES=\
	Lookuplet.java \
	Binding.java \
	BindingSet.java \
	QueryWindow.java

SRCFILES=$(patsubst %.java,$(SRCDIR)/%.java,$(SOURCES))

LDFLAGS=\
	--CLASSPATH=swt.jar \
	--main=com.samskivert.lookuplet.Lookuplet

lookuplet: $(SRCFILES) swt.o
	$(GCJ) -o lookuplet $(LDFLAGS) $(SRCFILES) swt.o

libswt.so: swt.jar
	$(GCJ)  -fjni -fPIC -shared -o libswt.so swt.jar

swt.o: swt.jar
	$(GCJ)  -fjni -c swt.jar

install: lookuplet
	cp lookuplet $(HOME)/bin
	strip $(HOME)/bin/lookuplet
