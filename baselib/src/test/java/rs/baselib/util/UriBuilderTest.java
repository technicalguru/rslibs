/**
 * 
 */
package rs.baselib.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.jupiter.api.Test;

/**
 * Tests the URI builder
 */
public class UriBuilderTest {

	@Test
	public void testParsing() {
		String     uriString = "https://us%20er:pass%20word@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		assertEquals("https", builder.scheme());
		assertEquals("us er", builder.user());
		assertEquals("pass word", builder.password());
		assertEquals("myexample.com", builder.host());
		assertEquals("8080", builder.port());
		assertEquals("/path/sub-path 1/index.html", builder.path());
		MultiValuedMap<String, String> queryParams = builder.queryParams();
		assertNotNull(queryParams);
		assertEquals(2, queryParams.size());
		assertTrue(queryParams.containsKey("param1"));
		assertEquals(1, queryParams.get("param1").size());
		assertEquals("value ", queryParams.get("param1").iterator().next());
		assertTrue(queryParams.containsKey("param2"));
		assertEquals(1, queryParams.get("param2").size());
		assertEquals("value", queryParams.get("param2").iterator().next());
		assertEquals("afragment with encodings", builder.fragment());
	}
	
	@Test
	public void testBuilding() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		String     expected  = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulateScheme() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.scheme("http");
		String     expected  = "http://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulateUser() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.user("us er");
		String     expected  = "https://us%20er:password@myexample.com:8080/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulatePassword() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.password("pass word");
		String     expected  = "https://user:pass%20word@myexample.com:8080/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
		
	@Test
	public void testManipulateHost() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.host("my-example.com");
		String     expected  = "https://user:password@my-example.com:8080/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}

	@Test
	public void testManipulatePort_withEmptyString() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.port("");
		String     expected  = "https://user:password@myexample.com/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulatePort_withString() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.port("443");
		String     expected  = "https://user:password@myexample.com:443/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulatePort_withNegativePort() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.port(-5);
		String     expected  = "https://user:password@myexample.com/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulatePort_withPort() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.port(443);
		String     expected  = "https://user:password@myexample.com:443/path/sub-path%201/index.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulatePath_withPath() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.path("another/path to file.html");
		String     expected  = "https://user:password@myexample.com:8080/another/path%20to%20file.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulatePath_withSegments() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.appendSegments("and", "another/path to file.html");
		String     expected  = "https://user:password@myexample.com:8080/path/sub-path%201/index.html/and/another/path%20to%20file.html?param1=value+&param2=value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulateQueryParameters_replace() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.query("myParam=another value");
		String     expected  = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?myParam=another+value#afragment%20with%20encodings";
		assertEquals(expected, builder.build().toString());
	}
	
	@Test
	public void testManipulateQueryParameters_addParams() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.queryParam("myParam", "another value");
		String actual = builder.build().toString();
		assertTrue(actual.startsWith("https://user:password@myexample.com:8080/path/sub-path%201/index.html?"));
		assertTrue(actual.contains("param1=value+"));
		assertTrue(actual.contains("param2=value"));
		assertTrue(actual.contains("myParam=another+value"));
		assertTrue(actual.endsWith("#afragment%20with%20encodings"));
	}
	
	@Test
	public void testManipulateFragment() {
		String     uriString = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value%20&param2=value#afragment%20with%20encodings";
		UriBuilder builder   = UriBuilder.from(uriString);
		builder.fragment("another fragment");
		String     expected  = "https://user:password@myexample.com:8080/path/sub-path%201/index.html?param1=value+&param2=value#another%20fragment";
		assertEquals(expected, builder.build().toString());
	}

}
