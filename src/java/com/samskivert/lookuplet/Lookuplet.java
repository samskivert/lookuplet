//
// $Id$

package com.samskivert.lookuplet;

import org.eclipse.swt.widgets.Display;

/**
 * The main entry point for the Lookuplet application.
 */
public class Lookuplet
{
    public static void main (String[] args)
    {
        Display display = new Display();

        // create our query window and display it
        QueryWindow query = new QueryWindow(display);
        query.shell.open();

        // run the main event loop
        while (!query.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
