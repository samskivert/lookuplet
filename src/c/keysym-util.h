/**
 * $Id$
 */

#ifndef _KEYSYM_UTIL_H_
#define _KEYSYM_UTIL_H_

#include <glib.h>

gboolean
convert_string_to_keysym_state (const char* string,
			        guint* keysym,
				guint* state);

char*
convert_keysym_state_to_string (guint keysym,
				guint state);

#endif /* _KEYSYM_UTIL_H_ */
