/**
 * 
 */
package rs.baselib.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A cache implementation using a {@link HashMap} with {@link WeakReference}s.
 * @author ralph
 *
 */
public class WeakMapCache<K,V> implements ICache<K, V> {

	private static int DEFAULT_CLEAR_COUNT = 10;
	
	private Map<K,WeakReference<V>> cache = new HashMap<K,WeakReference<V>>();
	private ReferenceQueue<V> referenceQueue = new ReferenceQueue<V>();
	private int clearCounter = DEFAULT_CLEAR_COUNT;
	
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
	@Override
	public boolean containsValue(Object value) {
		for (Map.Entry<K,WeakReference<V>> entry : cache.entrySet()) {
			if (CommonUtils.equals(entry.getValue().get(), value)) return true; 
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(Object key) {
		if (clearCounter-- <= 0) clearMap();
		WeakReference<V> ref = cache.get(key);
		return ref != null ? ref.get() : null;
	}

	/**
	 * Clears the map by working on the reference queue.
	 */
	protected void clearMap() {
		Reference<? extends V> rf = referenceQueue.poll();
		while (rf != null) {
			cache.remove(rf); 
			rf = referenceQueue.poll();
		}
		clearCounter = DEFAULT_CLEAR_COUNT;
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
