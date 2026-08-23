/**
 * 
 */
package rs.mail.templates.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link LruCacheManager}.
 * 
 * @author ralph
 *
 */
public class LruCacheManagerTest {

	@Test
	public void testGetCleanupPriority() throws Exception {
		LruCacheManager manager = new LruCacheManager();
		DefaultCache<String, String> cache = new DefaultCache<>(manager);
		cache.put("Key1", "Value1");
		cache.put("Key2", "Value2");
		cache.put("Key3", "Value3");
		Thread.sleep(1000L);
		cache.get("Key1");
		cache.get("Key3");
		
		List<String> prio = manager.getCleanupPriority(cache.getMeta());
		assertEquals(3, prio.size());
		assertEquals("Key2", prio.get(0));
	}
}
