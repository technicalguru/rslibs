/**
 * 
 */
package rs.baselib.licensing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of an {@link ILicenseContext}.
 * @author ralph
 *
 */
public class DefaultLicenseContext implements ILicenseContext {

	/** The values */
	private Map<String, Object> values;
	
	/**
	 * Constructor.
	 */
	public DefaultLicenseContext() {
		values = new HashMap<String, Object>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(String key) {
		return values.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		return (T)get(clazz.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasKey(Class<?> clazz) {
		return hasKey(clazz.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasKey(String key) {
		return values.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(String key, Object value) {
		values.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> void set(Class<T> clazz, T value) {
		set(clazz.getName(), value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<String> getKeys() {
		return Collections.unmodifiableSet(values.keySet());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object remove(String key) {
		return values.remove(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T remove(Class<T> clazz) {
		return (T)remove(clazz.getName());
	}

	
}
