//
// $Id$

#ifndef _LOOKUPLET_HH
#include "lookuplet_glade.hh"
#define _LOOKUPLET_HH

class lookuplet : public lookuplet_glade
{  
    friend class lookuplet_glade;
    bool on_query_key_press_event(GdkEventKey *ev);
    void on_prefs_clicked();
    void exit_lookuplet();
public:
    void on_clip_text_received(const gchar* text);
};

#endif