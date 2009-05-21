//
// $Id$

package com.samskivert.lookuplet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Manages Lookuplet's history.
 */
public class History
{
    /**
     * Loads our history from a properties file in the user's home directory.
     */
    public static History load (String selection)
    {
        List<String> history = new ArrayList<String>();

        // start with our current selection in the zeroth position
        history.add(selection);

        // then load in the rest of the history after that
        File hfile = new File(System.getProperty("user.home") + HISTORY_PATH);
        try {
            if (hfile.exists()) {
                // load our binding properties file
                Properties hprops = new Properties();
                hprops.load(new BufferedInputStream(new FileInputStream(hfile)));

                // load up our history entries
                for (int ii = 0; ii < MAX_HISTORY_LENGTH; ii++) {
                    String entry = hprops.getProperty(String.valueOf(ii));
                    if (entry != null) {
                        history.add(entry);
                    }
                }
            }

        } catch (IOException ioe) {
            System.err.println("Failed to load '" + hfile + "': " + ioe);
        }

        return new History(history);
    }

    /**
     * Returns the size of our history.
     */
    public int size ()
    {
        return _history.size();
    }

    /**
     * Returns the specified history entry.
     */
    public String getEntry (int index)
    {
        String modded = _mods.get(index);
        return (modded != null) ? modded : _history.get(index);
    }

    /**
     * Notes that the user has navigated away from, or executed a modified history entry. The entry
     * will be modified in memory, and if this index is later supplied to {@link #usedEntry} then
     * the entry will be appended to the end of the history properly such that the modified entry
     * is written out to disk when the history is saved.
     */
    public void modifiedEntry (int index, String entry)
    {
        _mods.put(index, entry);
    }

    /**
     * Notes that the specified entry from the history was used. The entry is written at the zeroth
     * position, which contains our current selection.
     */
    public void usedEntry (String text)
    {
        // if this text appears anywhere previously in our history, move that entry to the most
        // recent slot
        for (int ii = 0; ii < _history.size(); ii++) {
            if (text.equals(_history.get(ii))) {
                _history.set(0, _history.remove(ii));
                return;
            }
        }

        // otherwise just set this text in the most recent slot
        _history.set(0, text);
    }

    /**
     * Writes the supplied history out to a properties file in the user's home directory.
     */
    public void save ()
    {
        // create our .lookuplet directory if necessary
        File ldir = new File(System.getProperty("user.home") + LOOKUPLET_PATH);
        if (!ldir.exists()) {
            ldir.mkdir();
        }

        Properties hprops = new Properties();
        for (int ii = 0, ll = Math.min(_history.size(), MAX_HISTORY_LENGTH); ii < ll; ii++) {
            hprops.setProperty(String.valueOf(ii), _history.get(ii));
        }

        File hfile = new File(ldir, HISTORY_FILE);
        try {
            hprops.store(new BufferedOutputStream(new FileOutputStream(hfile)), HISTORY_COMMENT);

        } catch (IOException ioe) {
            System.err.println("Failed to load '" + hfile + "': " + ioe);
        }
    }

    protected History (List<String> history)
    {
        _history = history;
    }

    protected List<String> _history;
    protected Map<Integer, String> _mods = new HashMap<Integer, String>();

    protected static final String LOOKUPLET_PATH = File.separator + ".lookuplet";
    protected static final String HISTORY_FILE = "history.properties";
    protected static final String HISTORY_PATH = LOOKUPLET_PATH + File.separator + HISTORY_FILE;
    protected static final String HISTORY_COMMENT = "Lookuplet history";
    protected static final int MAX_HISTORY_LENGTH = 32;
}
