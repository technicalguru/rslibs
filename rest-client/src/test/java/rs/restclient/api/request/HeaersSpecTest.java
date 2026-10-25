package rs.restclient.api.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.jupiter.api.Test;

import rs.restclient.core.api.request.HeadersSpec;

/**
 * Test functionality
 * @author ralph
 *
 */
public class HeaersSpecTest {

	private static HeadersSpecBuilder builder = new HeadersSpecBuilder();
	
	@Test
	public void testConstructor() {
		HeadersSpec expected = builder.build();
		HeadersSpec actual   = new HeadersSpec(expected.getHeaders());
		
		testEqual(expected, actual);
	}
	
	@Test
	public void testAdd() {
		HeadersSpec spec = builder.build();
		spec.add("aKey", "aValue");
		assertTrue(spec.getHeaders().containsKey("aKey"));
		Collection<Object> values = spec.getHeaders().get("aKey");
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("aValue", values.iterator().next());
	}
	
	@Test
	public void testAddMultiple() {
		HeadersSpec spec = builder.build();
		spec.add("aKey", "aValue", "aValue2");
		assertTrue(spec.getHeaders().containsKey("aKey"));
		Collection<Object> values = spec.getHeaders().get("aKey");
		assertNotNull(values);
		assertEquals(2, values.size());
		assertTrue(values.contains("aValue"));
		assertTrue(values.contains("aValue2"));
	}
	
	@Test
	public void testAddToExisting() {
		HeadersSpec spec = builder.build();
		spec.add("aKey", "aValue");
		spec.add("aKey", "aValue2");
		assertTrue(spec.getHeaders().containsKey("aKey"));
		Collection<Object> values = spec.getHeaders().get("aKey");
		assertNotNull(values);
		assertEquals(2, values.size());
		assertTrue(values.contains("aValue"));
		assertTrue(values.contains("aValue2"));
	}
	
	@Test
	public void testReplace() {
		HeadersSpec spec = builder.build();
		spec.add("aKey", "something", "somethingElse");
		spec.replace("aKey", "aValue");
		assertTrue(spec.getHeaders().containsKey("aKey"));
		Collection<Object> values = spec.getHeaders().get("aKey");
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("aValue", values.iterator().next());
	}
	
	@Test
	public void testReplaceMultiple() {
		HeadersSpec spec = builder.build();
		spec.add("aKey", "something", "somethingElse");
		spec.replace("aKey", "aValue", "aValue2");
		assertTrue(spec.getHeaders().containsKey("aKey"));
		Collection<Object> values = spec.getHeaders().get("aKey");
		assertNotNull(values);
		assertEquals(2, values.size());
		assertTrue(values.contains("aValue"));
		assertTrue(values.contains("aValue2"));
	}
	
	@Test
	public void testRemoveMultiple() {
		HeadersSpec spec = builder.build();
		spec.add("aKey", "something", "somethingElse");
		spec.remove("aKey");
		assertFalse(spec.getHeaders().containsKey("aKey"));
	}
	
	
	protected static void testEqual(HeadersSpec expected, HeadersSpec actual) {
		if ((expected == null) && (actual != null)) fail("expected: null, but got: "+actual);
		if ((expected != null) && (actual == null)) fail("expected: "+expected+", but got: null");
		if ((expected != null) && (actual != null)) {
			MultiValuedMap<String,Object> expectedMap  = expected.getHeaders();
			MultiValuedMap<String,Object> actualMap    = actual.getHeaders();
			Set<String>                   expectedKeys = expectedMap.keySet();
			Set<String>                   actualKeys   = actualMap.keySet();
			assertEquals(expectedKeys.size(), actualKeys.size());
			for (String key : expectedKeys) {
				Collection<Object> expectedValues = expectedMap.get(key);
				Collection<Object> actualValues   = actualMap.get(key);
				if (actualValues == null) fail("Expected key: \""+key+"\", but got: null");
				assertEquals(expectedValues.size(), actualValues.size());
				for (Object value : expectedValues) {
					assertTrue(actualValues.contains(value));
				}
			}
		}
	}
}
