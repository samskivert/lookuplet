/**
 * $Id$
 * 
 * lookuplet - a utility for quickly looking up information
 * Copyright (C) 2001 Michael Bayne
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

#ifndef _PREFERENCES_H_
#define _PREFERENCES_H_

#include <gnome.h>

extern void
lk_prefs_init (void);

extern GPtrArray*
lk_prefs_get_bindings (void);

extern void
lk_prefs_cleanup (void);

extern void
lk_prefs_display (void);

#endif /* _PREFERENCES_H_ */
