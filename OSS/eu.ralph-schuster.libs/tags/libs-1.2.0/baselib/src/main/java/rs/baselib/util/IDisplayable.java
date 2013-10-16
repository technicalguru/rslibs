/**
 * 
 */
package rs.baselib.util;

import java.util.Locale;

/**
 * Defines the possibility to return a displayable string for users in a specific {@link Locale}.
 * @author ralph
 *
 */
public interface IDisplayable {

	/**
	 * Return the display string in given locale.
	 * @param locale locale
	 * @return display string for locale
	 */
	public String toString(Locale locale);
	
}
