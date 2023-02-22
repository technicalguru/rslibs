/**
 * 
 */
package rs.baselib.util;

/**
 * StringFilter strings from evil input.
 * 
 * @author ralph
 *
 */
public interface StringFilter {

	/**
	 * StringFilters the input.
	 * @param s the string to be filtered (can be null)
	 * @return the filtered string
	 */
	public String filter(String s);
}
