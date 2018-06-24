/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;

import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;

/**
 * Abstract support for beans.
 * @author ralph
 *
 */
public abstract class AbstractBean implements IBean {

	/** The change support. */
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	/** Object is dirty? */
	private boolean dirty = false;
	/** all property names (cached) */
	private List<String> propertyNames = null;
	/** All registed changes */
	private Map<String, PropertyChangeEvent> registeredChanges = new HashMap<String, PropertyChangeEvent>();

	/**
	 * Constructor.
	 */
	public AbstractBean() {
		init();
	}

	/**
	 * Initializes.
	 */
	private void init() {
		dirty = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transient
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets the dirty flag.
	 * @param dirty <code>true</code> when bean shall be marked dirty. If the bean is marked as not dirty
	 * then all registered changes will be removed.
	 */
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
		if (!dirty) {
			registeredChanges.clear();
		}
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
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		return firePropertyChange(propertyName, oldValue, newValue, true);
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @param makeDirty whether to make this object dirty (register the change)
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(String propertyName, Object oldValue, Object newValue, boolean makeDirty) {
		return firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue), makeDirty);
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(String propertyName, int oldValue, int newValue) {
		return firePropertyChange(propertyName, oldValue, newValue, true);
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @param makeDirty {@code true} when the object shall be marked dirty
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(String propertyName, int oldValue, int newValue, boolean makeDirty) {
		return firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue), makeDirty);
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		return firePropertyChange(propertyName, oldValue, newValue, true);
	}


	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @param makeDirty {@code true} when the object shall be marked dirty
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(String propertyName, boolean oldValue, boolean newValue, boolean makeDirty) {
		return firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue), makeDirty);
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	public boolean firePropertyChange(String propertyName, float oldValue, float newValue) {
		return firePropertyChange(propertyName, oldValue, newValue, true);
	}

	/**
	 * Fires an event if property changed.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @param makeDirty {@code true} when the object shall be marked dirty
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	public boolean firePropertyChange(String propertyName, float oldValue, float newValue, boolean makeDirty) {
		return firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue), makeDirty);
	}

	/**
	 * Fires a change event.
	 * @param event event to be fired
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(PropertyChangeEvent event) {
		return firePropertyChange(event, true);
	}

	/**
	 * Fires a change event.
	 * @param event event to be fired
	 * @param makeDirty {@code true} when the object shall be marked dirty
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean firePropertyChange(PropertyChangeEvent event, boolean makeDirty) {
		if (!CommonUtils.equals(event.getOldValue(), event.getNewValue())) {
			if (makeDirty) {
				registerChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
				setDirty(true);
			}
			changeSupport.firePropertyChange(event);
			return true;
		}
		return false;
	}

	/**
	 * Fires an indexed change event.
	 * @param propertyName name of property
	 * @param index index of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
		changeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
		return true;
	}

	/**
	 * Fires an indexed change event.
	 * @param propertyName name of property
	 * @param index index of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
		fireIndexedPropertyChange(propertyName, index, (Object)oldValue, (Object)newValue);
		return true;
	}

	/**
	 * Fires an indexed change event.
	 * @param propertyName name of property
	 * @param index index of property
	 * @param oldValue old value
	 * @param newValue new value
	 * @return <code>true</code> when the event was fired (because values were not equal)
	 */
	protected boolean fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
		fireIndexedPropertyChange(propertyName, index, (Object)oldValue, (Object)newValue);
		return true;
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

	/**
	 * Register the property change.
	 * @param propertyName name of property
	 * @param oldValue old value
	 * @param newValue new value
	 */
	protected void registerChange(String propertyName, Object oldValue, Object newValue) {
		PropertyChangeEvent evt = registeredChanges.get(propertyName);
		if (evt != null) {
			if (CommonUtils.equals(newValue, evt.getOldValue())) {
				// The change is being reverted
				registeredChanges.remove(propertyName);
			} else {
				// Another value being put on
				evt = new PropertyChangeEvent(this, propertyName, evt.getOldValue(), newValue);
				registeredChanges.put(propertyName, evt);
			}
		} else {
			if (!CommonUtils.equals(newValue, oldValue)) {
				evt = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
				registeredChanges.put(propertyName, evt);
			}
		}
	}

	/**
	 * Returns the original value.
	 * The original value is the value of a property that existed at the
	 * time when {@link #isDirty()} returned false.
	 * The value will be compared to the value currently held by this object
	 * in order to detect any changes.
	 * @param propertyName name of property
	 * @return persistent value
	 */
	protected Object getOriginalValue(String propertyName) {
		PropertyChangeEvent evt = registeredChanges.get(propertyName);
		if (evt != null) return evt.getOldValue();
		return get(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(String name, Object value) {
		try {
			PropertyUtils.setProperty(this, name, value);
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (Exception e) {
			throw new RuntimeException("Cannot set property: "+name+"="+value, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(String name) {
		try {
			return PropertyUtils.getProperty(this, name);
		} catch (NoSuchMethodException e) {
			// Ignore
		} catch (Exception e) {
			throw new RuntimeException("Cannot get property: "+name, e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transient
	public Collection<PropertyChangeEvent> getChanges() {
		return new ArrayList<PropertyChangeEvent>(registeredChanges.values());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The method will ask {@link #isCopyAllowed(PropertyDescriptor)} for each property of this bean.
	 * </p>
	 * @throws IllegalArgumentException when the destination is not of same type as this class
	 */
	@Override
	public void copyTo(Object destination) {
		if (!getClass().equals(destination.getClass())) {
			throw new IllegalArgumentException("The destination must be of same type but is of "+destination.getClass().getName());
		}

		for (String name : getPropertyNames()) {

			if (isCopyAllowed(name) && PropertyUtils.isReadable(this, name) && PropertyUtils.isWriteable(destination, name)) {
				try {
					Object value = PropertyUtils.getProperty(this, name);
					PropertyUtils.setProperty(destination, name, value);
				} catch (Exception e) {

				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<String> getPropertyNames() {
		if (propertyNames == null) {
			synchronized (this) {
				if (propertyNames == null) {
					propertyNames = new ArrayList<String>();
					PropertyDescriptor descriptors[] = PropertyUtils.getPropertyDescriptors(getClass());
					for (PropertyDescriptor descriptor : descriptors) {
						String name = descriptor.getName();
						propertyNames.add(name);
					}
				}
			}
		}
		return propertyNames;
	}
	
	/**
	 * Returns true when {@link #copyTo(Object)} is allowed to copy the given property.
	 * <p>
	 * This method relies on the {@link NoCopy} and {@link Transient} annotations on read methods.
	 * </p>
	 * @param descriptor the descriptor of th eproperty to be checked
	 * @return <code>true</code> when copy is allowed
	 * @see BeanSupport#isCopyForbidden(Class, String)
	 */
	protected boolean isCopyAllowed(PropertyDescriptor descriptor) {
		return isCopyAllowed(descriptor.getName());
	}

	/**
	 * Returns true when {@link #copyTo(Object)} is allowed to copy the given property.
	 * <p>
	 * This method relies on the {@link NoCopy} and {@link Transient} annotations on read methods.
	 * </p>
	 * @param name - the name of the property to be checked
	 * @return <code>true</code> when copy is allowed
	 * @see BeanSupport#isCopyForbidden(Class, String)
	 */
	protected boolean isCopyAllowed(String name) {
		return !BeanSupport.INSTANCE.isCopyForbidden(getClass(), name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		Collection<PropertyChangeEvent> changes = getChanges();
		for (PropertyChangeEvent evt : changes) {
			set(evt.getPropertyName(), evt.getOldValue());
		}
		setDirty(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		List<Object> properties = new ArrayList<Object>();
		PropertyDescriptor descriptors[] = PropertyUtils.getPropertyDescriptors(getClass());
		for (PropertyDescriptor descriptor : descriptors) {
			if (!BeanSupport.INSTANCE.isTransient(this, descriptor.getName())) {
				try {
					String name = descriptor.getName();
					Object value = PropertyUtils.getProperty(this, name);
					properties.add(name);
					properties.add(value);
				} catch (Exception e) { // Ignore }
				}
			}
		}
		return LangUtils.toString(getClass().getSimpleName(), properties.toArray());
	}


}
