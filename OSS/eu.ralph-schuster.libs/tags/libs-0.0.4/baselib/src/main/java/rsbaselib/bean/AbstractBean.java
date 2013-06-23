/**
 * 
 */
package rsbaselib.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract support for beans.
 * @author ralph
 *
 */
public abstract class AbstractBean implements IBean {

	/** The change support. */
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	/**
	 * Constructor.
	 */
	public AbstractBean() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * Returns all listeners.
	 * @return all listeners
	 * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
	 */
	protected PropertyChangeListener[] getPropertyChangeListeners() {
		return changeSupport.getPropertyChangeListeners();
	}

	/**
	 * Returns change listeners on a specific property.
	 * @param propertyName name of property
	 * @return the listeners for this property
	 * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
	 */
	protected PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		return changeSupport.getPropertyChangeListeners(propertyName);
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, int, int)
	 */
	public void firePropertyChange(String propertyName, int oldValue, int newValue) {
		firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, boolean, boolean)
	 */
	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
	}
	
    /**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, Object, Object)
     */
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
		firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
    }


	/**
	 * Fires a change event.
	 * @param event event to be fired
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)
	 */
	protected void firePropertyChange(PropertyChangeEvent event) {
		changeSupport.firePropertyChange(event);
	}

	/**
	 * Fires an indexed change event.
	 * @param propertyName name of property
	 * @param index index of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, java.lang.Object, java.lang.Object)
	 */
	protected void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
		changeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	/**
	 * Fires an indexed change event.
	 * @param propertyName name of property
	 * @param index index of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, int, int)
	 */
	protected void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
		fireIndexedPropertyChange(propertyName, index, (Object)oldValue, (Object)newValue);
	}

	/**
	 * Fires an indexed change event.
	 * @param propertyName name of property
	 * @param index index of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, boolean, boolean)
	 */
	protected void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
		fireIndexedPropertyChange(propertyName, index, (Object)oldValue, (Object)newValue);
	}

	/**
	 * Returns true when the specified property has listeners attached.
	 * @param propertyName name of property
	 * @return true when there are listeners on that property
	 * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
	 */
	protected boolean hasListeners(String propertyName) {
		return changeSupport.hasListeners(propertyName);
	}

	
}
