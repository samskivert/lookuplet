#!/bin/sh
#
# $Id$
#
# Copies the Debian files to the appropriate place on my web server for
# distribution.

DEBDIR=/export/wayward/pages/code/debian

if [ -f ../lookuplet_*.deb ]; then
    # remove any old files
    ssh waywardgeeks.org "rm $DEBDIR/pool/main/l/lookuplet/lookuplet_*"
    # copy over the new files
    scp ../lookuplet_* waywardgeeks.org:$DEBDIR/pool/main/l/lookuplet/
    # update the packages file
    ssh waywardgeeks.org "/export/wayward/bin/update_packages.pl"
    # blow away our local copy
    rm -f ../lookuplet_*
else
    echo "No files to install."
fi
