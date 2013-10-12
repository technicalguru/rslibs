/**
 * 
 */
package rs.baselib.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.lang.LangUtils;

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
	private Map<Class<?>,List<String>> nonTransientProperties;
	private Map<Class<?>,List<String>> transientProperties;
	
	/**
	 * Constructor.
	 */
	protected BeanSupport() {
		beanPropertyMappings = new HashMap<Class<?>, Map<String,String>>();
		forbiddenCopies = new HashMap<Class<?>, Set<String>>();
		nonTransientProperties = new HashMap<Class<?>, List<String>>();
		transientProperties = new HashMap<Class<?>, List<String>>();
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
	 * Returns whether the given property is forbidden to be copied.
	 * The property was either marked as {@link NoCopy} or is {@link #isTransient(Object, String)}.
	 * @param beanClass the bean class
	 * @param propertyName the name of the property
	 * @return <code>true</code> when {@link IBean#copyTo(Object)} must not copy this property
	 */
	public boolean isCopyForbidden(Class<?> beanClass, String propertyName) {
		if (propertyName.equals("class")) return false;
		if (isTransient(beanClass, propertyName)) return true;

		return getForbiddenList(beanClass, false).contains(propertyName);
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
			collectForbiddenCopies(rc, beanClass);
			forbiddenCopies.put(beanClass, Collections.unmodifiableSet(rc));
		}
		
		if (rc == null) return Collections.emptySet();
		return rc;
	}

	/**
	 * Collects the {@link NoCopy} properties (self-recursive).
	 * @param rc collection where properties need to be collected
	 * @param clazz class to be inspected
	 * @see LangUtils#isForbiddenCopy(PropertyDescriptor)
	 */
	protected void collectForbiddenCopies(Set<String> rc, Class<?> clazz) {
		PropertyDescriptor arr[] = PropertyUtils.getPropertyDescriptors(clazz);
		for (PropertyDescriptor desc : arr) {
			if (LangUtils.isNoCopy(desc) && !rc.contains(desc.getName())) {
				rc.add(desc.getName());
			}
		}
		for (Class<?> clazz2 : clazz.getInterfaces()) {
			collectForbiddenCopies(rc, clazz2);
		}
		Class<?> clazz2  = clazz.getSuperclass();
		if (clazz2 != null) {
			collectForbiddenCopies(rc, clazz2);
		}
	}

	/**
	 * Returns true when the given property is transient.
	 * The method is a cached version of {@link LangUtils#isTransient(PropertyDescriptor)}.
	 * @param bean Bean or class of bean
	 * @param propertyName name of property
	 * @return true when property is transient
	 */
	public boolean isTransient(Object bean, String propertyName) {
		return getTransientProperties(bean).contains(propertyName);
	}
	
	/**
	 * Returns the transient properties of the bean or class.
	 * @param bean bean or class of bean
	 * @return list of non-transient properties
	 * @see #isTransient(Object, String)
	 */
	public List<String> getTransientProperties(Object bean) {
		Class<?> clazz = bean instanceof Class ? (Class<?>)bean : bean.getClass();
		List<String> rc = transientProperties.get(clazz);
		if (rc == null) {
			rc = new ArrayList<String>();
			collectTransientProperties(rc, clazz);
			transientProperties.put(clazz, Collections.unmodifiableList(rc));
		}
		return rc;
	}
	
	/**
	 * Returns the non-transient properties of the bean or class.
	 * @param bean bean or class of bean
	 * @return list of non-transient properties
	 * @see #isTransient(Object, String)
	 */
	public List<String> getNonTransientProperties(Object bean) {
		Class<?> clazz = bean instanceof Class ? (Class<?>)bean : bean.getClass();
		List<String> rc = nonTransientProperties.get(clazz);
		if (rc == null) {
			rc = new ArrayList<String>();
			List<String> transientProperties = getTransientProperties(clazz);
			for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(clazz)) {
				if (!transientProperties.contains(desc.getName())) {
					rc.add(desc.getName());
				}
			}
			nonTransientProperties.put(clazz, Collections.unmodifiableList(rc));
		}
		return rc;
	}
	
	/**
	 * Collects the non-transient properties (self-recursive).
	 * @param rc collection where properties need to be collected
	 * @param clazz class to be inspected
	 * @see #isTransient(Object, String)
	 */
	protected void collectTransientProperties(List<String> rc, Class<?> clazz) {
		for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(clazz)) {
			if (LangUtils.isTransient(desc) && !rc.contains(desc.getName())) {
				rc.add(desc.getName());
			}
		}
		for (Class<?> clazz2 : clazz.getInterfaces()) {
			collectTransientProperties(rc, clazz2);
		}
		Class<?> clazz2  = clazz.getSuperclass();
		if (clazz2 != null) {
			collectTransientProperties(rc, clazz2);
		}
	}
}
