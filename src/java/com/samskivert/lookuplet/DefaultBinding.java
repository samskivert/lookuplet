//
// $Id$

package com.samskivert.lookuplet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

/**
 * A default binding (executed when the user presses return) that tries to figure out the "right
 * thing" to do with the provided text.
 */
public class DefaultBinding extends Binding
{
    public DefaultBinding ()
    {
        super(0, SWT.CR, Type.EXEC, "Default", "");
    }

    @Override // from Binding
    public boolean matches (KeyEvent event)
    {
        return event.keyCode == SWT.CR;
    }

    @Override // from Binding
    public void invoke (String terms)
    {
        try {
            // if the file looks like a URL, fire it off in firefox
            if (terms.startsWith("http://") || terms.startsWith("https://") ||
                terms.startsWith("file://") || terms.startsWith("mailto:")) {
                terms.replaceAll(" ", ""); // strip out whitespace and
                terms.replaceAll("\n", ""); // carriage returns
                showURL(terms);
                return;
            }

            // if it contains something that looks like an email address, open a compose window to
            // that address
            Matcher m = _emailre.matcher(terms);
            if (m.find()) {
                showURL("mailto:" + m.group());
                return;
            }

            // if they entered an executable file on the file system run that
            String command = terms.split(" ")[0];
            if (maybeExecute(command, terms)) {
                return;
            }

            // see if the first word is an executable in our path
            String output = execAndRead("which " + command);
            if (output != null && maybeExecute(output, terms)) {
                return;
            }

            // otherwise fire off the words in google
            String url = GOOGLE_URL.replace("%U", URLEncoder.encode(terms, "UTF-8"));
            showURL(url);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    protected boolean maybeExecute (String command, String terms)
        throws Exception
    {
        File file = new File(command);
        if (file.exists() && file.canExecute()) {
            Runtime.getRuntime().exec(terms);
            return true;
        }
        return false;
    }

    protected String execAndRead (String command)
    {
        try {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader bin = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            if (proc.waitFor() == 0) {
                return bin.readLine();
            }
        } catch (Exception e) {
            System.err.println("Failed to run '" + command + "': " + e);
        }
        return null;
    }

    /** Originally formulated by lambert@nas.nasa.gov. */
    protected static final String EMAIL_REGEX =
        "([-A-Za-z0-9_.!%+]+@[-a-zA-Z0-9]+(\\.[-a-zA-Z0-9]+)*\\.[-a-zA-Z0-9]+)";

    /** A compiled version of our email regular expression. */
    protected static final Pattern _emailre = Pattern.compile(EMAIL_REGEX);

    /** The URL via which to do a Google search. */
    protected static final String GOOGLE_URL = "http://www.google.com/search?client=googlet&q=%U";
}
