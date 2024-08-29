/**
 * 
 */
package rs.baselib.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides various standard string filters.
 * 
 * @author ralph
 *
 */
public class StringFilters {

	/** A filter for removing any HTML tags */
	public static final StringFilter NO_HTML = NoHtmlStringFilter.INSTANCE;
	
	/**
	 * Cleans input parameters from any HTML.
	 * @param list list of strings to clean
	 * @return the cleaned list
	 */
	public static List<String> cleanInput(List<String> list) {
		List<String> rc = new ArrayList<>();
		list.forEach(s -> {
			rc.add(cleanInput(s, null));
		});
		return rc;
	}
	
	/**
	 * Cleans input parameter from HTML
	 * @param s the input string to be cleaned
	 * @return the cleaned string
	 */
	public static String cleanInput(String s) {
		return cleanInput(s, null);
	}
	
	/**
	 * Cleans input string using a specified filter, usually {@link StringFilters#NO_HTML}.
	 * @param s the string to clean
	 * @param filter the filter to be used
	 * @return the cleaned string
	 */
	public static String cleanInput(String s, StringFilter filter) {
		if (s == null) return null;
		if (filter == null) filter = NO_HTML;
		return filter.filter(s);
	}
}
