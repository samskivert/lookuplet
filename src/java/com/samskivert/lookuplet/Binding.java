//
// $Id$

package com.samskivert.lookuplet;

import java.net.URLEncoder;

import org.eclipse.swt.events.KeyEvent;

/**
 * Represents a key binding.
 */
public class Binding
{
    /** Represents our two types of actions. */
    public enum Type { URL, EXEC };

    /** The {@link KeyEvent#stateMask} required to trigger this binding. */
    public final int stateMask;

    /** The {@link KeyEvent#keyCode} required to trigger this binding. */
    public final int keyCode;

    /** The type of action to be taken when this binding is triggered. */
    public final Type type;

    /** A friendly name for this binding. */
    public final String name;

    /** The URL to be shown or command to be executed when this binding is triggered. */
    public final String argument;

    /**
     * Creates and configures a binding instance.
     */
    public Binding (int stateMask, int keyCode, Type type, String name, String argument)
    {
        this.stateMask = stateMask;
        this.keyCode = keyCode;
        this.type = type;
        this.name = name;
        this.argument = argument;
    }

    /**
     * Returns true if the supplied key event matches this binding. Note: the {@link
     * KeyEvent#stateMask} must match exactly, so Shift-Ctrl-M will not match Ctrl-M.
     */
    public boolean matches (KeyEvent event)
    {
        return (event.stateMask == stateMask && event.keyCode == keyCode);
    }

    /**
     * Displays the URL or invokes the command associated with this binding.
     */
    public void invoke (String terms)
    {
        try {
            String command = argument.replace("%T", terms);
            command = command.replace("%U", URLEncoder.encode(terms, "UTF-8"));
            switch (type) {
            case URL:
                Runtime.getRuntime().exec("gnome-open " + command);
                break;
            case EXEC:
                Runtime.getRuntime().exec(command);
                break;
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
