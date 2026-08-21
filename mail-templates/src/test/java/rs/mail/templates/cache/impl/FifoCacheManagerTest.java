/**
 * 
 */
package rs.mail.templates.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link FifoCacheManager}.
 * 
 * @author ralph
 *
 */
public class FifoCacheManagerTest {

	@Test
	public void testGetCleanupPriority() throws Exception {
		FifoCacheManager manager = new FifoCacheManager();
		DefaultCache<String, String> cache = new DefaultCache<>(manager);
		cache.put("Key1", "Value1");
		Thread.sleep(500L);
		cache.put("Key2", "Value2");
		Thread.sleep(500L);
		cache.put("Key3", "Value3");
		
		List<String> prio = manager.getCleanupPriority(cache.getMeta());
		assertEquals(3, prio.size());
		assertEquals("Key1", prio.get(0));
		assertEquals("Key2", prio.get(1));
		assertEquals("Key3", prio.get(2));
	}
}
