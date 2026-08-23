/**
 * 
 */
package csv.util;

/**
 * Converts WorkItemResults into table rows.
 * @author ralph
 *
 */
public interface ObjectConverter<T> {

	/**
	 * Convert the given object into a row.
	 * @param object
	 * @return the converted object as a row
	 */
	public Object[] convert(T object);

}
