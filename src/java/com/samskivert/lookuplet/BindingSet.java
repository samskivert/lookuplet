//
// $Id$

package com.samskivert.lookuplet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

/**
 * Manages our key bindings.
 */
public class BindingSet
{
    /**
     * Creates a binding set, reading the binding configuration from a properties file in the
     * user's home directory and creating a default binding set if that reading fails or the
     * properties file does not exist.
     */
    public static BindingSet load ()
    {
        List<Binding> bindings = new ArrayList<Binding>();

        File bfile = new File(System.getProperty("user.home") + BINDINGS_PATH);
        try {
            if (bfile.exists()) {
                // load our binding properties file
                Properties bprops = new Properties();
                bprops.load(new BufferedInputStream(new FileInputStream(bfile)));

                // enumerate all of our keys
                Set<String> bkeys = new HashSet<String>();
                for (Enumeration<?> penum  = bprops.propertyNames(); penum.hasMoreElements(); ) {
                    String name = (String)penum.nextElement();
                    int didx = name.indexOf(".");
                    if (didx != -1) {
                        bkeys.add(name.substring(0, didx));
                    }
                }

                for (String key : bkeys) {
                    Binding binding = makeBinding(bprops, key);
                    if (binding != null) {
                        bindings.add(binding);
                    }
                }
            }

        } catch (IOException ioe) {
            System.err.println("Failed to load '" + bfile + "': " + ioe);
        }

        // if we failed to load any bindings, use our defaults
        if (bindings.size() == 0) {
            for (Binding binding : DEF_BINDINGS) {
                bindings.add(binding);
            }
        }

        // finally add the default binding in every case
        bindings.add(new DefaultBinding());

        return new BindingSet(bindings);
    }

    /**
     * Returns the binding that matches the supplied key combination or null.
     */
    public Binding getMatch (KeyEvent event)
    {
        for (Binding binding : _bindings) {
            if (binding.matches(event)) {
                return binding;
            }
        }
        return null;
    }

    protected BindingSet (List<Binding> bindings)
    {
        _bindings = bindings;
    }

    /**
     * Parses a binding from the supplied properties file. A string definition of the key stroke is
     * the key prefix for a binding's configuration in the properties file. An example:
     *
     * <pre>
     * control-g.type = URL
     * control-g.name = Google search
     * control-g.argument = http://www.google.com/search?client=googlet&q=%U
     * </pre>
     *
     * @return null if the binding parsing failed, a {@link Binding} on success.
     */
    protected static Binding makeBinding (Properties bprops, String key)
    {
        try {
            String btype = bprops.getProperty(key + ".type", "").trim();
            Binding.Type type = Enum.valueOf(Binding.Type.class, btype);
            String name = bprops.getProperty(key + ".name");
            if (name == null) {
                throw new Exception("Missing " + key + ".name property.");
            }
            String argument = bprops.getProperty(key + ".argument");
            if (name == null) {
                throw new Exception("Missing " + key + ".argument property.");
            }

            String[] kbits = key.split("-");
            int mask = 0;
            for (int ii = 0; ii < kbits.length-1; ii++) {
                Integer mod = MOD_MAP.get(kbits[ii].toLowerCase());
                if (mod == null) {
                    throw new Exception(
                        "Unknown key modifier '" + kbits[ii] + "' for key '" + key + "'.");
                }
                mask |= mod;
            }
            String kchar = kbits[kbits.length-1];
            if (kchar.length() > 1) {
                throw new Exception("Invalid key character '" + kchar + "' for key '" + key + "'.");
            }
            return new Binding(mask, kchar.charAt(0), type, name, argument);

        } catch (Exception e) {
            System.err.println("Failed to parse binding '" + key + "': " + e);
            return null;
        }
    }

    protected List<Binding> _bindings;

    protected static final String BINDINGS_PATH =
        File.separator + ".lookuplet" + File.separator + "bindings.properties";

    protected static Binding[] DEF_BINDINGS = new Binding[] {
        new Binding(SWT.CONTROL, 'g', Binding.Type.URL, "Google search",
                    "http://www.google.com/search?client=googlet&q=%U"),
        new Binding(SWT.CONTROL, 'd', Binding.Type.EXEC, "Dictionary lookup",
                    "gnome-dictionary --look-up=%T"),
        new Binding(SWT.CONTROL|SWT.SHIFT, 'd', Binding.Type.URL, "Debian package search",
                    "http://cgi.debian.org/cgi-bin/search_contents.pl?word=%U&case=insensitive" +
                    "&version=unstable&arch=i386&directories=yes"),
        new Binding(SWT.CONTROL, 'f', Binding.Type.URL, "Freshmeat search",
                    "http://freshmeat.net/search/?q=%U"),
        new Binding(SWT.CONTROL, 'i', Binding.Type.URL, "IMDB Title search",
                    "http://www.imdb.com/Tsearch?title=%U&restrict=Movies+only"),
    };

    protected static final Map<String, Integer> MOD_MAP = new HashMap<String, Integer>();
    static {
        MOD_MAP.put("comman", SWT.COMMAND);
        MOD_MAP.put("control", SWT.CONTROL);
        MOD_MAP.put("ctrl", SWT.CTRL);
        MOD_MAP.put("shift", SWT.SHIFT);
        MOD_MAP.put("alt", SWT.ALT);
    }
}
