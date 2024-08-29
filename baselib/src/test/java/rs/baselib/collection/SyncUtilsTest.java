package rs.baselib.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link SyncUtils}.
 */
public class SyncUtilsTest {

	@Test
	public void testSync() throws Exception {
		SyncTestHelper helper = new SyncTestHelper("one", "two", "three");
		Set<String> newValues = Arrays.stream(new String[]{"two", "three", "four"}).collect(Collectors.toSet());
		Collection<String> synced = helper.sync(newValues);
		assertNotNull(synced);
		assertEquals(3, synced.size());
		assertTrue(synced.contains("two"));
		assertTrue(synced.contains("three"));
		assertTrue(synced.contains("four"));
		assertNotNull(helper.added);
		assertEquals(1, helper.added.size());
		assertTrue(helper.added.contains("four"));
		assertNotNull(helper.removed);
		assertEquals(1, helper.removed.size());
		assertTrue(helper.removed.contains("one"));
	}
	
	protected static class SyncTestHelper extends AbstractSyncHelper<String> {
		public Set<String> added   = new HashSet<>();
		public Set<String> removed = new HashSet<>();
		
		public SyncTestHelper(String ...members) {
			super(Arrays.stream(members).collect(Collectors.toSet()));
		}
		
		@Override
		public String add(String value) {
			added.add(value);
			return value;
		}

		@Override
		public String remove(String value) {
			removed.add(value);
			return value;
		}
		
	}
}
