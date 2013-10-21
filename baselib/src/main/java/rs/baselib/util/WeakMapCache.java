/**
 * 
 */
package rs.baselib.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A cache implementation using a {@link WeakHashMap}.
 * @author ralph
 *
 */
public class WeakMapCache<K,V> implements Cache<K, V> {

	private Map<K,WeakReference<V>> cache = new WeakHashMap<K,WeakReference<V>>();

	/**
	 * Constructor.
	 */
	public WeakMapCache() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return cache.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean containsValue(Object value) {
		return cache.containsValue(new WeakReference<V>((V)value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(Object key) {
		WeakReference<V> ref = cache.get(key);
		return ref != null ? ref.get() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value) {
		WeakReference<V> ref = cache.put(key, new WeakReference<V>(value));
		return ref != null ? ref.get() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V remove(Object key) {
		WeakReference<V> ref = cache.remove(key);
		return ref != null ? ref.get() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		cache.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<K> keySet() {
		return cache.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<V> values() {
		List<V> rc = new ArrayList<V>();
		for (Map.Entry<K,WeakReference<V>> entry : cache.entrySet()) {
			rc.add(entry.getValue().get());
		}		
		return rc;
	}
	
}
