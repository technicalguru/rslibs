/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import rs.baselib.io.FileFinder;

/**
 * Test the utilities.
 * @author ralph
 *
 */
public class CommonUtilsTest {

	/**
	 * Test the emptyness of strings.
	 */
	@Test
	public void testIsEmptyStringBoolean() {
		String s1 = null;
		String s2 = "";
		String s3 = "   ";
		String s4 = "4";
		String s5 = " 5 ";
		
		assertTrue(CommonUtils.isEmpty(s1, false));
		assertTrue(CommonUtils.isEmpty(s1, true));
		assertTrue(CommonUtils.isEmpty(s2, false));
		assertTrue(CommonUtils.isEmpty(s2, true));
		assertFalse(CommonUtils.isEmpty(s3, false));
		assertTrue(CommonUtils.isEmpty(s3, true));
		assertFalse(CommonUtils.isEmpty(s4, false));
		assertFalse(CommonUtils.isEmpty(s4, true));
		assertFalse(CommonUtils.isEmpty(s5, false));
		assertFalse(CommonUtils.isEmpty(s5, true));
		
	}

	/**
	 * Tests the stacktrace dump.
	 */
	@Test
	public void testGetStackTrace() {
		List<String> lines = CommonUtils.getStackTrace(0);
		assertTrue(lines.size() > 0);
		assertTrue(lines.get(0).trim().startsWith("at "+getClass().getName()+".testGetStackTrace"));
	}
	
	@Test
	public void testToStringString() {
		assertEquals("", CommonUtils.toString((String)null));
		assertEquals("value", CommonUtils.toString("value"));
	}

	@Test
	public void testToStringFloat() {
		assertEquals("0", CommonUtils.toString(0f));
	}

	@Test
	public void testToStringByteArray() {
		byte b[] = new byte[] { (byte)0xa0, (byte)0x13, (byte)0xff };
		assertEquals("a013ff", CommonUtils.toString(b));
	}

	@Test
	public void testToStringObjectArray() {
		Object o[] = new Object[] { "prefix", 13L, "suffix" };
		assertEquals("[prefix,13,suffix]", CommonUtils.toString(o));
	}

	@Test
	public void testEqualsObjectObject() {
		String s1 = "value1";
		String s2 = "value2";
		String s3 = "value2";
		assertTrue(CommonUtils.equals(null, null));
		assertFalse(CommonUtils.equals(s1, null));
		assertFalse(CommonUtils.equals(null, s2));
		assertFalse(CommonUtils.equals(s1, s2));
		assertTrue(CommonUtils.equals(s2, s3));
	}

	@Test
	public void testIsCompatibleVersion() {
		assertTrue(CommonUtils.isCompatibleVersion(null, "1.2.0", null)); 
		assertTrue(CommonUtils.isCompatibleVersion(null, "1.2.0", "0.1.0")); 
		assertFalse(CommonUtils.isCompatibleVersion("1.0.0", "1.2.0", "0.1.0")); 
		assertFalse(CommonUtils.isCompatibleVersion("1.0.0", null, "0.1.0")); 
		assertTrue(CommonUtils.isCompatibleVersion(null, null, "0.1.0")); 
		assertTrue(CommonUtils.isCompatibleVersion("1.0.0", "1.2.0", "1.1.0")); 
	}

	@Test
	public void testCompareVersion() {
		assertTrue(CommonUtils.compareVersion(null, "1.2.0") < 0); 
		assertTrue(CommonUtils.compareVersion("1.1", "1.2.0") < 0); 
		assertTrue(CommonUtils.compareVersion("1.1", null) > 0); 
		assertTrue(CommonUtils.compareVersion("1.1", "1.1.0") < 0); 
		assertTrue(CommonUtils.compareVersion("1.1.0", "1.1.0") == 0); 
		assertTrue(CommonUtils.compareVersion((String)null, (String)null) == 0); 
	}

	@Test
	public void testJoin() {
		assertEquals("a-b-c", CommonUtils.join("-", new String[]{ "a", "b", "c" })); 
	}

	@Test
	public void testJoin2() {
		assertEquals("b-c", CommonUtils.join("-", new String[]{ "a", "b", "c" }, 1)); 
	}

	@Test
	public void testJoin3() {
		assertEquals("b-c-d", CommonUtils.join("-", new String[]{ "a", "b", "c", "d", "e" }, 1, 3)); 
	}

	@Test
	public void testGetUnixTimestampLong() {
		assertEquals(1, CommonUtils.getUnixTimestamp(1000L));
		assertEquals(1, CommonUtils.getUnixTimestamp(1200L));
		assertEquals(1, CommonUtils.getUnixTimestamp(1999L));
	}

	@Test
	public void testGetUnixTimestampDate() {
		assertEquals(1, CommonUtils.getUnixTimestamp(new Date(1000L)));
		assertEquals(1, CommonUtils.getUnixTimestamp(new Date(1200L)));
		assertEquals(1, CommonUtils.getUnixTimestamp(new Date(1999L)));
	}

	@Test
	public void testLoadContentString() throws IOException {
		assertEquals("finder-file4", CommonUtils.loadContent(FileFinder.find("finder-file4.txt").openStream()).trim());
	}

	@Test
	public void testLoadContentWithNewline() throws IOException {
		String actual = CommonUtils.loadContent(new File("src/test/resources/finder-file5.txt"));
		if (actual.indexOf('\r') > 0) actual = actual.replaceAll("\\r", "");
		assertEquals("finder-file5\n", actual);
	}
	
	@Test
	public void testLoadContentWithoutNewline() throws IOException {
		assertEquals("finder-file6", CommonUtils.loadContent(new File("src/test/resources/finder-file6.txt")));
	}

	@Test
	public void testReplaceEnvVariables() throws IOException {
		String path = System.getenv("PATH");
		String s = "abc=$ENV{PATH}+.text";
		String expected = "abc="+path+"+.text";
		assertEquals(expected, CommonUtils.replaceEnvVariables(s));
	}
	
	@Test
	public void testReplaceRuntimeVariables() throws IOException {
		String userHome = System.getProperty("user.home");
		String s = "abc=+$RUNTIME{user.home}.text";
		String expected = "abc=+"+userHome+".text";
		assertEquals(expected, CommonUtils.replaceRuntimeVariables(s));
	}

	@Test
	public void testReplaceVariablesSimple() throws IOException {
		String path = System.getenv("PATH");
		String userHome = System.getProperty("user.home");
		String s = "abc=$ENV{PATH}+$RUNTIME{user.home}.text";
		String expected = "abc="+path+"+"+userHome+".text";
		assertEquals(expected, CommonUtils.replaceVariables(s));
	}
	
	@Test
	public void testReplaceVariablesCombined() throws IOException {
		String path = System.getenv("PATH");
		String userHome = System.getProperty("user.home");
		String s = "abc=$ENV{PATH}+$RUNTIME{user.home}.text";
		String expected = "abc="+path+"+"+userHome+".text";
		assertEquals(expected, CommonUtils.replaceVariables(s));
	}
	
    /**
     * Tests the marker replacement.
     */
    @Test
    public void testSetMarkers() {
    	TestData data = new TestData();
    	data.setSubject("A subject");
    	data.setReplyTo("Hans Mustermann <hans.mustermann@example.com>");
    	data.setLanguage("de");
    	String template = "subject={@email:subject} replyTo={@email:replyTo} lang={@email:language}";
    	String result = CommonUtils.setMarkers(template, "email", data);
    	assertEquals("subject=A subject replyTo=Hans Mustermann <hans.mustermann@example.com> lang=de", result);
    }
    
    /**
     * Tests the marker replacement with NULL argument.
     */
    @Test
    public void testSetMarkersNull() {
    	String template = "subject={@email:subject} replyTo={@email:replyTo} lang={@email:language}";
    	String result = CommonUtils.setMarkers(template, "email", null);
    	assertEquals(template, result);
    }
    
    /**
     * Tests the marker replacement with NULL argument.
     */
    @Test
    public void testSetMarkersNullTemplate() {
    	TestData data = new TestData();
    	data.setSubject("A subject");
    	data.setReplyTo("Hans Mustermann <hans.mustermann@example.com>");
    	data.setLanguage("de");
    	String result = CommonUtils.setMarkers(null, "email", data);
    	assertNull(result);
    }
    
    /**
     * Tests the marker replacement.
     */
    @Test
    public void testSetMarkersMap() {
    	Map<String,String> data = new HashMap<String, String>();
    	data.put("subject", "A subject");
    	data.put("replyTo", "Hans Mustermann <hans.mustermann@example.com>");
    	data.put("language", "de");
    	String template = "subject={@email:subject} replyTo={@email:replyTo} lang={@email:language}";
    	String result = CommonUtils.setMarkers(template, "email", data);
    	assertEquals("subject=A subject replyTo=Hans Mustermann <hans.mustermann@example.com> lang=de", result);
    }
    
    /** Test class object */
    public static class TestData {
    	String subject;
    	String replyTo;
    	String language;
		/**
		 * Returns the subject.
		 * @return the subject
		 */
		public String getSubject() {
			return subject;
		}
		/**
		 * Sets the subject.
		 * @param subject the subject to set
		 */
		public void setSubject(String subject) {
			this.subject = subject;
		}
		/**
		 * Returns the replyTo.
		 * @return the replyTo
		 */
		public String getReplyTo() {
			return replyTo;
		}
		/**
		 * Sets the replyTo.
		 * @param replyTo the replyTo to set
		 */
		public void setReplyTo(String replyTo) {
			this.replyTo = replyTo;
		}
		/**
		 * Returns the language.
		 * @return the language
		 */
		public String getLanguage() {
			return language;
		}
		/**
		 * Sets the language.
		 * @param language the language to set
		 */
		public void setLanguage(String language) {
			this.language = language;
		}
    	
    }
}
