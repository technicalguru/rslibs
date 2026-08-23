/**
 * 
 */
package csv.util;

/**
 * Returns a table header that be used in streams.
 * @author ralph
 *
 */
public interface TableHeaderProvider {

	/**
	 * The table header to be written in streams
	 */
	public String[] getTableHeader();
	
}
