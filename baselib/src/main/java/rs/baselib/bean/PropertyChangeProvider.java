/**
 * 
 */
package rs.baselib.bean;

import java.beans.PropertyChangeListener;

/**
 * An interface telling that the object informs about property changes.
 * @author ralph
 *
 */
public interface PropertyChangeProvider {

	/**
	 * Add a change listener.
	 * @param listener the listener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Remove a change listener.
	 * @param listener the listener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Add a change listener.
	 * @param propertyName the property name the listener will be registered for
	 * @param listener the listener to be added
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	/**
	 * Remove a change listener.
	 * @param propertyName the property name the listener will not listen for anymore
	 * @param listener the listener to be removed
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
	
}
