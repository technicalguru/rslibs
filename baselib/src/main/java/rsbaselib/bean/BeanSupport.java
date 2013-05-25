/**
 * 
 */
package rsbaselib.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support Bean property actions.
 * @author ralph
 *
 */
public class BeanSupport {

	private static Logger log = LoggerFactory.getLogger(BeanSupport.class);
	
	public static BeanSupport INSTANCE = new BeanSupport();
	
	private Map<Class<?>,Map<String,String>> beanPropertyMappings;
	private Map<Class<?>,Set<String>> forbiddenCopies;
	
	/**
	 * Constructor.
	 */
	protected BeanSupport() {
		beanPropertyMappings = new HashMap<Class<?>, Map<String,String>>();
		forbiddenCopies = new HashMap<Class<?>, Set<String>>();
	}

	/**
	 * Adds the mapping for the specified property.
	 * @param beanClass the class where the property occurs
	 * @param propertyName the name of the bean property
	 * @param propertyEventName the name to appear in change events
	 */
	public void addPropertyName(Class<?> beanClass, String propertyName, String propertyEventName) {
		Map<String,String> propertyMap = getPropertyMap(beanClass);
		propertyMap.put(propertyName, propertyEventName);
	}
	
	/**
	 * Returns the map for the bean class.
	 * This method will always create a map if not already present.
	 * @param beanClass the class of the bean
	 * @return the property map.
	 */
	public synchronized Map<String,String> getPropertyMap(Class<?> beanClass) {
		Map<String,String> rc = beanPropertyMappings.get(beanClass);
		if (rc == null) {
			rc = new HashMap<String, String>();
			beanPropertyMappings.put(beanClass, rc);
		}
		return rc;
	}
	
	/**
	 * Fires property change events to the listeners for the given bean.
	 * @param listeners listeners
	 * @param bean bean
	 */
	public void firePropertyEvents(Collection<PropertyChangeListener> listeners, Object bean) {
		if (bean == null) return;
		Map<String,String> propertyMap = getPropertyMap(bean.getClass());
		for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
			try {
				Object oldValue = null;
				Object newValue = PropertyUtils.getSimpleProperty(bean, entry.getKey());
				PropertyChangeEvent evt = new PropertyChangeEvent(bean, entry.getValue(), oldValue, newValue);
				for (PropertyChangeListener l : listeners) {
					l.propertyChange(evt);
				}
			} catch (Throwable t) {
				log.error("Error while firing property event: "+entry.getKey(), t);
			}
		}
	}
	
	/**
	 * Adds the given property to the list of forbidden properties for copies.
	 * The property will not be copied anymore by {@link IBean#copyTo(Object)}.
	 * @param beanClass the bean class
	 * @param propertyName the name of the property
	 * @see IBean#copyTo(Object)
	 */
	public void addForbiddenCopy(Class<?> beanClass, String propertyName) {
		getForbiddenList(beanClass, true).add(propertyName);
	}
	
	/**
	 * Removes the given property from the list of forbidden properties for copies.
	 * The property will be copied by {@link IBean#copyTo(Object)}.
	 * @param beanClass the bean class
	 * @param propertyName the name of the property
	 * @see IBean#copyTo(Object)
	 */
	public void removeForbiddenCopy(Class<?> beanClass, String propertyName) {
		getForbiddenList(beanClass, true).add(propertyName);
	}
	
	/**
	 * Returns whether the given property is forbidden to be copied.
	 * @param beanClass the bean class
	 * @param propertyName the name of the property
	 * @return <code>true</code> when {@link IBean#copyTo(Object)} must not copy this property
	 */
	public boolean isCopyForbidden(Class<?> beanClass, String propertyName) {
		if (propertyName.equals("class")) return false;
		// We need to check all super classes
		while (beanClass != null) {
			if (getForbiddenList(beanClass, false).contains(propertyName)) {
				return true;
			}
			beanClass = beanClass.getSuperclass();
		}
		return false;
	}

	/**
	 * Returns the set of property names forbidden to be copied.
	 * @param beanClass the class
	 * @param create whether the list shall be created if it doesn't exist yet
	 * @return the set of forbidden properties
	 */
	protected Set<String> getForbiddenList(Class<?> beanClass, boolean create) {
		Set<String> rc = forbiddenCopies.get(beanClass);
		if ((rc == null) && create) {
			rc = new HashSet<String>();
			forbiddenCopies.put(beanClass, rc);
		}
		
		if (rc == null) return Collections.emptySet();
		return rc;
	}
}
