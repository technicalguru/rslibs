/**
 * 
 */
package rs.data.file.storage;

import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.binary.Base64;

import rs.baselib.lang.LangUtils;

/**
 * Abstract implementation of a storage stratey.
 * @author ralph
 *
 */
public abstract class AbstractStorageStrategy<S> implements IStorageStrategy<S> {

	/** Cache of transient properties per class */
	private static Map<Class<?>, Set<String>> transientCache = new HashMap<Class<?>, Set<String>>();

	/** Local storage of transient properties */
	private ThreadLocal<Set<String>> transientProperties = new ThreadLocal<Set<String>>();

	/** The encoding used */
	private String encoding = Charset.defaultCharset().name();
	
	/**
	 * Constructor.
	 */
	public AbstractStorageStrategy() {
	}

	/**
	 * Serializes the given value.
	 * Collections, Maps and other non-serializable objects cannot be serialized by this method.
	 * @param value value to be serialized
	 * @return the serialized string or null if it cannot be serialized safely. Values encoded in BASE64 are
	 *    prefixed with <code>"BASE64"</code>.
	 */
	protected String serialize(Object value) throws IOException {
		if (value instanceof Number) {
			return value.toString();
		} else if (value instanceof Boolean) {
			return value.toString();
		} else if (value instanceof Enum) {
			return value.toString();
		} else if (value instanceof String) {
			// encode only when out of sight characters
			boolean isClean = true;
			for (char c : ((String) value).toCharArray()) {
				if (c > '\u007f') {
					isClean = false;
					break;
				}
			}
			if (isClean) {
				return value.toString();
			}
			return "BASE64:"+new String(Base64.encodeBase64(((String) value).getBytes(getEncoding())));
		} else if (value instanceof Serializable) {
			return "BASE64:"+LangUtils.serializeBase64(value);
		}
		return null;
	}
	
	/**
	 * Unserializes the given value.
	 * Collections, Maps and other non-serializable objects cannot be unserialized by this method.
	 * @param className the expected class to be produced
	 * @param value the serialized string. Values encoded in BASE64 must be prefixed with <code>"BASE64"</code>.
	 * @return the object (or null)
	 */
	protected Object unserialize(String className, String value) throws IOException, ReflectiveOperationException {
		Class<?> clazz = Class.forName(className);
		if (Number.class.isAssignableFrom(clazz)) {
			return (Number)clazz.getConstructor(String.class).newInstance(value);
		} else if (Boolean.class.isAssignableFrom(clazz) ) {
			return Boolean.valueOf(value);
		} else if (String.class.isAssignableFrom(clazz)) {
			if (value.startsWith("BASE64:")) return Base64.decodeBase64(value.substring(7));
			return value;
		} else if (Serializable.class.isAssignableFrom(clazz)) {
			return LangUtils.unserialize(value);
		}
		return null;
	}
	
	/**
	 * Returns all properties as defined in the BO interface.
	 * @param object the object
	 * @return the names of all relevant interfaces
	 */
	protected Collection<String> getPropertyNames(Object object) {
		Set<String> rc = transientCache.get(object.getClass());
		if (rc == null) {
			rc = new HashSet<String>();
			transientCache.put(object.getClass(), rc);
			
			// Initialize transient properties for this run
			Set<String> transients = transientProperties.get();
			if (transients != null) {
				transients.clear();
			} else {
				transients = new HashSet<String>();
				transientProperties.set(transients);
			}
			// Collect on interfaces only
			for (Class<?> clazz2 : object.getClass().getInterfaces()) {
				collectPropertyNames(rc, clazz2);
			}
			// Remove all transient properties
			for (String s : transients) {
				rc.remove(s);
			}
		}
		return Collections.unmodifiableSet(rc);
	}

	/**
	 * Returns all properties as defined in the BO interface.
	 * @param object the object
	 * @return the names of all relevant interfaces
	 */
	protected Collection<String> getBeanPropertyNames(Object object) {
		Set<String> rc = transientCache.get(object.getClass());
		if (rc == null) {
			rc = new HashSet<String>();
			transientCache.put(object.getClass(), rc);
			
			// Initialize transient properties for this run
			Set<String> transients = transientProperties.get();
			if (transients != null) {
				transients.clear();
			} else {
				transients = new HashSet<String>();
				transientProperties.set(transients);
			}
			// Collect all
			collectPropertyNames(rc, object.getClass());
			// Remove all transient properties
			for (String s : transients) {
				rc.remove(s);
			}
		}
		return Collections.unmodifiableSet(rc);
	}

	/**
	 * Returns all properties as defined in the BO interface.
	 * @param object the object
	 * @return the names of all relevant interfaces
	 */
	protected void collectPropertyNames(Collection<String> rc, Class<?> clazz) {
		for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(clazz)) {
			if (isTransient(desc)) {
				transientProperties.get().add(desc.getName());
			} else {
				if (!rc.contains(desc.getName())) {
					rc.add(desc.getName());
				}
			}
		}
		for (Class<?> clazz2 : clazz.getInterfaces()) {
			collectPropertyNames(rc, clazz2);
		}
		Class<?> clazz2  = clazz.getSuperclass();
		if (clazz2 != null) {
			collectPropertyNames(rc, clazz2);
		}
	}

	/**
	 * Returns true when given property is transient.
	 * @param desc property descriptor
	 * @return true when property is transient and must not be persisted
	 */
	protected boolean isTransient(PropertyDescriptor desc) {
		Method rm = desc.getReadMethod();
		Method wm = desc.getWriteMethod();
		if ((rm == null) || (wm == null)) return true;
		return rm.isAnnotationPresent(Transient.class);
	}

	/**
	 * Returns the {@link #encoding}.
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Sets the {@link #encoding}.
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	
}
