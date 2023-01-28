/**
 * 
 */
package rs.baselib.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test the {@link NoHtmlStringFilter}.
 * 
 * @author ralph
 *
 */
public class NoHtmlStringFilterTest {

	@Test
	public void testSimpleString() {
		String s = "A simple text without issue";
		assertEquals("Simple string test failed", s, NoHtmlStringFilter.INSTANCE.filter(s));
	}
	
	@Test
	public void testSimpleStartTag() {
		String s = "A <a>text with HTML tags.";
		assertEquals("HTML string test failed", "A text with HTML tags.", NoHtmlStringFilter.INSTANCE.filter(s));
	}
	
	@Test
	public void testSimpleEndTag() {
		String s = "A text with</a> HTML tags.";
		assertEquals("HTML string test failed", "A text with HTML tags.", NoHtmlStringFilter.INSTANCE.filter(s));
	}
	
	@Test
	public void testCombinedStartEndTag() {
		String s = "A <a />text with HTML tags.";
		assertEquals("HTML string test failed", "A text with HTML tags.", NoHtmlStringFilter.INSTANCE.filter(s));
	}
	
	@Test
	public void testCombinedStartEndTagWithAttributes() {
		String s = "A <a href=\"#\"/>text with HTML tags.";
		assertEquals("HTML string test failed", "A text with HTML tags.", NoHtmlStringFilter.INSTANCE.filter(s));
	}
	
	@Test
	public void testStartTagWithAttributes() {
		String s = "A <a href=\"#\">text with HTML tags.";
		assertEquals("HTML string test failed", "A text with HTML tags.", NoHtmlStringFilter.INSTANCE.filter(s));
	}
	
	@Test
	public void testStartEndTagWithAttributes() {
		String s = "A <a href=\"#\">text with</a> HTML tags.";
		assertEquals("HTML string test failed", "A text with HTML tags.", NoHtmlStringFilter.INSTANCE.filter(s));
	}
	
	@Test
	public void testJavascript() {
		String s = "A <script type=\"text/javascript\">text with</script> HTML tags.";
		assertEquals("HTML string test failed", "A HTML tags.", NoHtmlStringFilter.INSTANCE.filter(s));
	}
	
	
}
