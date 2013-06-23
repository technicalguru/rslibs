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
	 * @param listener
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * @param propertyName
	 * @param listener
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * @param propertyName
	 * @param listener
	 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * @return
	 * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
	 */
	protected PropertyChangeListener[] getPropertyChangeListeners() {
		return changeSupport.getPropertyChangeListeners();
	}

	/**
	 * @param propertyName
	 * @return
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
	 * @param event
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)
	 */
	protected void firePropertyChange(PropertyChangeEvent event) {
		changeSupport.firePropertyChange(event);
	}

	/**
	 * @param propertyName
	 * @param index
	 * @param oldValue
	 * @param newValue
	 * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, java.lang.Object, java.lang.Object)
	 */
	protected void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
		changeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	/**
	 * @param propertyName
	 * @param index
	 * @param oldValue
	 * @param newValue
	 * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, int, int)
	 */
	protected void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
		fireIndexedPropertyChange(propertyName, index, (Object)oldValue, (Object)newValue);
	}

	/**
	 * @param propertyName
	 * @param index
	 * @param oldValue
	 * @param newValue
	 * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, boolean, boolean)
	 */
	protected void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
		fireIndexedPropertyChange(propertyName, index, (Object)oldValue, (Object)newValue);
	}

	/**
	 * @param propertyName
	 * @return
	 * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
	 */
	protected boolean hasListeners(String propertyName) {
		return changeSupport.hasListeners(propertyName);
	}

	
}
