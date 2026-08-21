/**
 * 
 */
package rs.mail.templates.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Test;

import rs.mail.templates.cache.Cache;

/**
 * Test the default cache.
 * 
 * @author ralph
 *
 */
public class DefaultCacheTest {

	@Test
	public void testIsEmpty() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		assertTrue(cache.isEmpty());
		cache.put("Key1", "Value1");
		assertFalse(cache.isEmpty());
		cache.put("Key2", "Value2");
		assertFalse(cache.isEmpty());
	}

	@Test
	public void testSize() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		assertEquals(0, cache.size());
		cache.put("Key1", "Value1");
		assertEquals(1, cache.size());
		cache.put("Key2", "Value2");
		assertEquals(2, cache.size());
	}
	
	@Test
	public void testContainsKey() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		cache.put("Key1", "Value1");
		assertTrue(cache.containsKey("Key1"));
		assertFalse(cache.containsKey("Key2"));
	}
	
	@Test
	public void testContainsValue() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		cache.put("Key1", "Value1");
		assertTrue(cache.containsValue("Value1"));
		assertFalse(cache.containsValue("Value2"));
	}
	
	@Test
	public void testGet() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		cache.put("Key1", "Value1");
		String value = cache.get("Key1");
		assertNotNull(value);
		assertEquals("Value1", value);
		assertNull(cache.get("Key2"));
	}
	
	@Test
	public void testRemove() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		cache.put("Key1", "Value1");
		cache.put("Key2", "Value2");
		assertEquals(2, cache.size());
		String value = cache.remove("Key1");
		assertEquals(1, cache.size());
		assertNotNull(value);
		assertEquals("Value1", value);
		assertFalse(cache.containsKey("Key1"));
	}

	@Test
	public void testClear() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		cache.put("Key1", "Value1");
		cache.put("Key2", "Value2");
		assertEquals(2, cache.size());
		cache.clear();
		assertEquals(0, cache.size());
	}

	@Test
	public void testKeySet() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		cache.put("Key1", "Value1");
		cache.put("Key2", "Value2");
		assertEquals(2, cache.size());
		Set<String> keys = cache.keySet();
		assertEquals(2, keys.size());
		assertTrue(keys.contains("Key1"));
		assertTrue(keys.contains("Key2"));
	}

	@Test
	public void testValues() {
		Cache<String, String> cache = new DefaultCache<>(new LruCacheManager());
		cache.put("Key1", "Value1");
		cache.put("Key2", "Value2");
		assertEquals(2, cache.size());
		Collection<String> values = cache.values();
		assertEquals(2, values.size());
		assertTrue(values.contains("Value1"));
		assertTrue(values.contains("Value2"));
	}

}
