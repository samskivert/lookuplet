#
# $Id$

AUTOMAKE_OPTIONS = no-dependencies

INCLUDES = \
	$(GNOME_INCLUDEDIR)

bin_PROGRAMS = lookuplet
bin_DIR = ../install.d/

lookuplet_SOURCES = \
	launcher.c \
	lookuplet.c \
	querybox.c

lookuplet_LDADD = \
	$(GNOME_LIBDIR) \
	$(GTK_LIBS) \
	$(GNOMEGNORBA_LIBS) \
	-lpanel_applet
