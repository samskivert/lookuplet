//
// $Id$

package com.samskivert.lookuplet;

import java.io.File;
import java.net.URLEncoder;

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
                terms.startsWith("file://")) {
                terms.replaceAll(" ", ""); // strip out whitespace and
                terms.replaceAll("\n", ""); // carriage returns
                Runtime.getRuntime().exec("gnome-open " + terms);
                return;
            }

            // if they entered an executable file on the file system run that (TODO: search the path
            // for this file?)
            File file = new File(terms);
            if (file.exists() && file.canExecute()) {
                Runtime.getRuntime().exec(terms);
                return;
            }

            // otherwise fire off the words in google
            String url = GOOGLE_URL.replace("%U", URLEncoder.encode(terms, "UTF-8"));
            Runtime.getRuntime().exec("gnome-open " + url);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

   protected static final String GOOGLE_URL = "http://www.google.com/search?client=googlet&q=%U";
}
