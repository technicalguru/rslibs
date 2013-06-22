/**
 * 
 */
package rs.baselib.util;

import java.util.Locale;

/**
 * Marks an object as providing various displays for locale.
 * @author ralph
 *
 */
public interface LocaleDisplayProvider {
	
	/**
	 * Returns the display.
	 * @param locale locale object
	 * @return the display for the locale
	 */
	public String getDisplay(Locale locale);
}
