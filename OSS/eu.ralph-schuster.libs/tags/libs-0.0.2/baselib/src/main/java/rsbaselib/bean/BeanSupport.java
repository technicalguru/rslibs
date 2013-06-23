/**
 * 
 */
package rsbaselib.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	
	/**
	 * Constructor.
	 */
	protected BeanSupport() {
		beanPropertyMappings = new HashMap<Class<?>, Map<String,String>>();
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
}
