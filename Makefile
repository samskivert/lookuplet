#
# $Id$
#
# Simple Makefile for lookuplet

GCJ=gcj-4.3

SRCDIR=src/java/com/samskivert/lookuplet
SOURCES=\
	Lookuplet.java \
	Binding.java \
	BindingSet.java \
	DefaultBinding.java \
	History.java \
	QueryWindow.java

SRCFILES=$(patsubst %.java,$(SRCDIR)/%.java,$(SOURCES))

LDFLAGS=\
	--CLASSPATH=lib/swt.jar \
	--main=com.samskivert.lookuplet.Lookuplet

lookuplet: $(SRCFILES) swt.o
	$(GCJ) -o lookuplet $(LDFLAGS) $(SRCFILES) swt.o

libswt.so: lib/swt.jar
	$(GCJ)  -fjni -fPIC -shared -o libswt.so lib/swt.jar

swt.o: lib/swt.jar
	$(GCJ)  -fjni -c lib/swt.jar

install: lookuplet
	cp lookuplet $(HOME)/bin
	strip $(HOME)/bin/lookuplet
