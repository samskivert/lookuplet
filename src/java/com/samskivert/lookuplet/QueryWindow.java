//
// $Id$

package com.samskivert.lookuplet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Displays the main Lookuplet query window.
 */
public class QueryWindow
{
    public final Shell shell;

    public QueryWindow (Display display)
    {
        shell = new Shell(display);

        RowLayout layout = new RowLayout();
        layout.center = true;
        layout.justify = true;
        layout.spacing = 10;
        layout.pack = true;
        shell.setLayout(layout);
        shell.setText("lookuplet");

        Label label = new Label(shell, SWT.LEFT);
        label.setText("Query");

        Text text = new Text(shell, SWT.SEARCH | SWT.CANCEL);
        text.setLayoutData(new RowData(150, SWT.DEFAULT));

        Button query = new Button(shell, 0);
        query.setText("Prefs");

        shell.pack();
    }
}
