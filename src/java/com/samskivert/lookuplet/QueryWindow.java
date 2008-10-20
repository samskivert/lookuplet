//
// $Id$

package com.samskivert.lookuplet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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

    public QueryWindow (Display display, BindingSet bindings)
    {
        shell = new Shell(display);
        _bindings = bindings;
        _clipboard = new Clipboard(display);

        RowLayout layout = new RowLayout();
        layout.center = true;
        layout.justify = true;
        layout.spacing = 10;
        layout.pack = true;
        shell.setLayout(layout);
        shell.setText("lookuplet");

        Label label = new Label(shell, SWT.LEFT);
        label.setText("Query");

        _text = new Text(shell, SWT.SEARCH | SWT.CANCEL);
        _text.setLayoutData(new RowData(150, SWT.DEFAULT));
        _text.addKeyListener(new KeyListener() {
            public void keyPressed (KeyEvent e) {
                onTextKeyPressed(e);
            }
            public void keyReleased (KeyEvent e) {
                // nada
            }
        });

        Button query = new Button(shell, 0);
        query.setText("Prefs");

        shell.addShellListener(new ShellAdapter() {
            public void shellActivated (ShellEvent event) {
                onShellActivated(event);
            }
        });
        shell.pack();
    }

    protected void onShellActivated (ShellEvent event)
    {
        String text = (String)_clipboard.getContents(
            TextTransfer.getInstance(), DND.SELECTION_CLIPBOARD);
        if (text == null) {
            text = "";
        }
        if (text != null && text.length() > 0) {
            _text.setText(text);
            _text.selectAll();
        }
        _history = History.load(text);
    }

    protected void onTextKeyPressed (KeyEvent e)
    {
        // escape cancels the whole business
        if (e.keyCode == SWT.ESC) {
            shell.close();
            return;
        }

        // handle history
        if (e.keyCode == SWT.ARROW_UP) {
            if (_hpos < _history.size()-1) {
                maybeFlushHistory();
                _text.setText(_history.getEntry(++_hpos));
            }
            return;

        } else if (e.keyCode == SWT.ARROW_DOWN) {
            if (_hpos > 0) {
                maybeFlushHistory();
                _text.setText(_history.getEntry(--_hpos));
            }
            return;
        }

        // TODO: tab completion

        // ignore plain or shifted-only keystroes
        if (e.stateMask == 0 || e.stateMask == SWT.SHIFT) {
            return;
        }

        // locate a binding for the pressed key
        Binding binding = _bindings.getMatch(e);
        if (binding != null) {
            maybeFlushHistory();
            _history.usedEntry(_hpos);
            binding.invoke(_text.getText().trim());
            _history.save();
            shell.close();
        }
    }

    protected void maybeFlushHistory ()
    {
        String text = _text.getText().trim();
        if (!text.equals(_history.getEntry(_hpos))) {
            _history.modifiedEntry(_hpos, text);
        }
    }

    protected BindingSet _bindings;
    protected Clipboard _clipboard;
    protected Text _text;

    protected History _history;
    protected int _hpos = 0;
}
