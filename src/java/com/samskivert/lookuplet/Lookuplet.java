//
// $Id$

package com.samskivert.lookuplet;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * The main entry point for the Lookuplet application.
 */
public class Lookuplet
{
    public static enum OS { LINUX, MACOS, WINDOWS, OTHER };

    public static OS opsys;

    public static void main (String[] args)
    {
        Display.setAppName("Lookuplet");
        Display display = new Display();

        // determine which OS we're on
        String osname = System.getProperty("os.name", "");
        if (osname.indexOf("Mac OS") != -1 || osname.indexOf("MacOS") != -1) {
            opsys = OS.MACOS;
        } else if (osname.indexOf("Linux") != -1) {
            opsys = OS.LINUX;
        } else if (osname.indexOf("Windows") != -1) {
            opsys = OS.WINDOWS;
        } else {
            opsys = OS.OTHER;
        }

        // load up our bindings
        BindingSet bindings = BindingSet.load();

        // create our query window, center and display it
        QueryWindow query = new QueryWindow(display, bindings);
        centerAndShow(display, query.shell);

        // run the main event loop
        while (!query.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    protected static final void centerAndShow (Display display, Shell shell)
    {
	Monitor primary = display.getPrimaryMonitor();
	Rectangle bounds = primary.getBounds();
	Rectangle rect = shell.getBounds ();
	int x = bounds.x + (bounds.width - rect.width) / 2;
	int y = bounds.y + (bounds.height - rect.height) / 2;
	shell.setLocation(x, y);
        shell.open();
    }
}
