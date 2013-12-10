/**
 * 
 */
package rs.baselib.util;

/**
 * Interface for an object that can retrieve a certain
 * property from an object. ValueProviders serve as supporting
 * objects e.g. when comparing objects or providing
 * cell labels in a table.
 * @author ralph
 *
 */
public interface IValueProvider {

	/**
	 * Return the value for the given object.
	 * @param o the object
	 * @return the value from the object.
	 */
	public Object getValue(Object o);
}
