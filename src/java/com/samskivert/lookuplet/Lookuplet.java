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
    public static void main (String[] args)
    {
        Display display = new Display();

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
