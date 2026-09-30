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

import rs.restclient.core.api.request.QueryParamsSpec;

/**
 * Test functionality
 * @author ralph
 *
 */
public class QueryParamsSpecTest {

	private static QueryParamsSpecBuilder builder = new QueryParamsSpecBuilder();
	
	@Test
	public void testConstructor() {
		QueryParamsSpec expected = builder.build();
		QueryParamsSpec actual   = new QueryParamsSpec(expected.getParams());
		
		testEqual(expected, actual);
	}
	
	@Test
	public void testAdd() {
		QueryParamsSpec spec = builder.build();
		spec.add("aKey", "aValue");
		assertTrue(spec.getParams().containsKey("aKey"));
		Collection<Object> values = spec.getParams().get("aKey");
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("aValue", values.iterator().next());
	}
	
	@Test
	public void testAddMultiple() {
		QueryParamsSpec spec = builder.build();
		spec.add("aKey", "aValue", "aValue2");
		assertTrue(spec.getParams().containsKey("aKey"));
		Collection<Object> values = spec.getParams().get("aKey");
		assertNotNull(values);
		assertEquals(2, values.size());
		assertTrue(values.contains("aValue"));
		assertTrue(values.contains("aValue2"));
	}
	
	@Test
	public void testAddToExisting() {
		QueryParamsSpec spec = builder.build();
		spec.add("aKey", "aValue");
		spec.add("aKey", "aValue2");
		assertTrue(spec.getParams().containsKey("aKey"));
		Collection<Object> values = spec.getParams().get("aKey");
		assertNotNull(values);
		assertEquals(2, values.size());
		assertTrue(values.contains("aValue"));
		assertTrue(values.contains("aValue2"));
	}
	
	@Test
	public void testSet() {
		QueryParamsSpec spec = builder.build();
		spec.add("aKey", "something", "somethingElse");
		spec.set("aKey", "aValue");
		assertTrue(spec.getParams().containsKey("aKey"));
		Collection<Object> values = spec.getParams().get("aKey");
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("aValue", values.iterator().next());
	}
	
	@Test
	public void testSetMultiple() {
		QueryParamsSpec spec = builder.build();
		spec.add("aKey", "something", "somethingElse");
		spec.set("aKey", "aValue", "aValue2");
		assertTrue(spec.getParams().containsKey("aKey"));
		Collection<Object> values = spec.getParams().get("aKey");
		assertNotNull(values);
		assertEquals(2, values.size());
		assertTrue(values.contains("aValue"));
		assertTrue(values.contains("aValue2"));
	}
	
	@Test
	public void testRemoveMultiple() {
		QueryParamsSpec spec = builder.build();
		spec.add("aKey", "something", "somethingElse");
		spec.remove("aKey");
		assertFalse(spec.getParams().containsKey("aKey"));
	}
	
	
	protected static void testEqual(QueryParamsSpec expected, QueryParamsSpec actual) {
		if ((expected == null) && (actual != null)) fail("expected: null, but got: "+actual);
		if ((expected != null) && (actual == null)) fail("expected: "+expected+", but got: null");
		if ((expected != null) && (actual != null)) {
			MultiValuedMap<String,Object> expectedMap  = expected.getParams();
			MultiValuedMap<String,Object> actualMap    = actual.getParams();
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
