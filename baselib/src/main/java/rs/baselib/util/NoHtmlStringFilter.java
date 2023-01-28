/**
 * 
 */
package rs.baselib.util;

import org.jsoup.Jsoup;

/**
 * Filters a string from any HTML tags.
 * 
 * @author ralph
 *
 */
public class NoHtmlStringFilter implements StringFilter {

	/**
	 * Singleton instance.
	 */
	public static final StringFilter INSTANCE = new NoHtmlStringFilter();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String filter(String s) {
		if (s == null) return s;
		return Jsoup.parse(s).text();
	}


}
