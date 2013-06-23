/**
 * 
 */
package rsbaselib.util;

import java.util.Locale;

/**
 * Defines the possibility to return a displayable string for users.
 * @author ralph
 *
 */
public interface Displayable {

	/**
	 * Return the display string in given locale.
	 * @param locale locale
	 * @return display string for locale
	 */
	public String toString(Locale locale);
	
}
