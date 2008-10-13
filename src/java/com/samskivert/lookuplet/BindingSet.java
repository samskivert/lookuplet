//
// $Id$

package com.samskivert.lookuplet;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

/**
 * Manages our key bindings.
 */
public class BindingSet
{
    public BindingSet ()
    {
        // TODO: read our bindings from a properties file
        _bindings.add(new Binding(SWT.CONTROL, 'g', Binding.Type.URL, "Google search",
                                  "http://www.google.com/search?client=googlet&q=%U"));
        _bindings.add(new Binding(SWT.CONTROL, 'd', Binding.Type.EXEC, "Dictionary lookup",
                                  "gnome-dictionary --look-up=%T"));
        _bindings.add(new Binding(SWT.CONTROL|SWT.SHIFT, 'd', Binding.Type.URL,
                                  "Debian package search",
                                  "http://cgi.debian.org/cgi-bin/search_contents.pl?word=%U" +
                                  "&case=insensitive&version=unstable&arch=i386" +
                                  "&directories=yes"));
        _bindings.add(new Binding(SWT.CONTROL, 'f', Binding.Type.URL, "Freshmeat search",
                                  "http://freshmeat.net/search/?q=%U"));
        _bindings.add(new Binding(SWT.CONTROL, 'i', Binding.Type.URL, "IMDB Title search",
                                  "http://www.imdb.com/Tsearch?title=%U&restrict=Movies+only"));
    }

    public Binding getMatch (KeyEvent event)
    {
        for (Binding binding : _bindings) {
            if (binding.matches(event)) {
                return binding;
            }
        }
        return null;
    }

    protected List<Binding> _bindings = new ArrayList<Binding>();
}
