/**
 * 
 */
package rsbaselib.bean;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import rsbaselib.util.Dirtyable;


/**
 * A general interface for bean support.
 * @author ralph
 *
 */
public interface IBean extends PropertyChangeProvider, Dirtyable {

	/**
	 * Set the property with given name to the value
	 * @param name property name
	 * @param value value
	 */
	public void set(String name, Object value);

	/**
	 * Gets the property with given name
	 * @param name property name
	 */
	public Object get(String name);

	/**
	 * Returns the list of changes that this bean has performed since loading.
	 * @return the list of changes so far
	 */
	public Collection<PropertyChangeEvent> getChanges();

	/**
	 * Copies all properties to the given object.
	 * @param destination destination object
	 */
	public void copyTo(Object destination);

	/**
	 * Reset all changes.
	 */
	public void reset();
	
}
