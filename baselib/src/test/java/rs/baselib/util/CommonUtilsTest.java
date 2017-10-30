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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import rs.baselib.io.FileFinder;
import rs.baselib.type.Continent;
import rs.baselib.util.CommonUtils;
import rs.baselib.util.RsDate;
import rs.baselib.util.RsMonth;

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
		
		assertTrue("s1 is not recognized as empty", CommonUtils.isEmpty(s1, false));
		assertTrue("s1 is not recognized as empty", CommonUtils.isEmpty(s1, true));
		assertTrue("s2 is not recognized as empty", CommonUtils.isEmpty(s2, false));
		assertTrue("s2 is not recognized as empty", CommonUtils.isEmpty(s2, true));
		assertFalse("s3 is not recognized as non-empty", CommonUtils.isEmpty(s3, false));
		assertTrue("s3 is not recognized as empty", CommonUtils.isEmpty(s3, true));
		assertFalse("s4 is not recognized as non-empty", CommonUtils.isEmpty(s4, false));
		assertFalse("s4 is not recognized as non-empty", CommonUtils.isEmpty(s4, true));
		assertFalse("s5 is not recognized as non-empty", CommonUtils.isEmpty(s5, false));
		assertFalse("s5 is not recognized as non-empty", CommonUtils.isEmpty(s5, true));
		
	}

	/**
	 * Tests the stacktrace dump.
	 */
	@Test
	public void testGetStackTrace() {
		List<String> lines = CommonUtils.getStackTrace(0);
		assertTrue("No stacktrace returned", lines.size() > 0);
		assertTrue("Trace does not start with correct method", lines.get(0).trim().startsWith("at "+getClass().getName()+".testGetStackTrace"));
	}
	
	@Test
	public void testToStringString() {
		assertEquals("toString(String) does not work correct", "", CommonUtils.toString((String)null));
		assertEquals("toString(String) does not work correct", "value", CommonUtils.toString("value"));
	}

	@Test
	public void testToStringFloat() {
		assertEquals("toString(float) does not work correct", "0", CommonUtils.toString(0f));
	}

	@Test
	public void testToStringRsDate() {
		assertEquals("toString(RsDate) does not work correct", "", CommonUtils.toString((RsDate)null));
		assertEquals("toString(RsDate) does not work correct", "", CommonUtils.toString(new RsDate(0)));
		RsDate d = new RsDate();
		assertEquals("toString(RsDate) does not work correct", CommonUtils.DATE_TIME_FORMATTER().format(d.getTime()), CommonUtils.toString(d));
	}

	@Test
	public void testToStringRsDay() {
		assertEquals("toString(RsDay) does not work correct", "", CommonUtils.toString((RsDay)null));
		assertEquals("toString(RsDay) does not work correct", "", CommonUtils.toString(new RsDay(0)));
		RsDay d = new RsDay();
		assertEquals("toString(RsDay) does not work correct", CommonUtils.DATE_FORMATTER().format(d.getBegin().getTime()), CommonUtils.toString(d));
	}

	@Test
	public void testToStringRsMonth() {
		assertEquals("toString(RsMonth) does not work correct", "", CommonUtils.toString((RsDate)null));
		assertEquals("toString(RsMonth) does not work correct", "", CommonUtils.toString(new RsDate(0)));
		RsMonth d = new RsMonth();
		assertEquals("toString(RsMonth) does not work correct", CommonUtils.DATE_FORMATTER().format(d.getBegin().getTime()), CommonUtils.toString(d));
	}

	@Test
	public void testToStringRsYear() {
		assertEquals("toString(RsYear) does not work correct", "", CommonUtils.toString((RsYear)null));
		assertEquals("toString(RsYear) does not work correct", "", CommonUtils.toString(new RsYear(0)));
		RsYear d = new RsYear();
		assertEquals("toString(RsYear) does not work correct", d.getKey(), CommonUtils.toString(d));
	}

	@Test
	public void testToStringByteArray() {
		byte b[] = new byte[] { (byte)0xa0, (byte)0x13, (byte)0xff };
		assertEquals("toString(byte[]) does not work correct", "a013ff", CommonUtils.toString(b));
	}

	@Test
	public void testToStringObjectArray() {
		Object o[] = new Object[] { "prefix", 13L, "suffix" };
		assertEquals("toString(Object[]) does not work correct", "[prefix,13,suffix]", CommonUtils.toString(o));
	}

	@Test
	public void testEqualsObjectObject() {
		String s1 = "value1";
		String s2 = "value2";
		String s3 = "value2";
		assertTrue("equals(Object,Object) does not work correct", CommonUtils.equals(null, null));
		assertFalse("equals(Object,Object) does not work correct", CommonUtils.equals(s1, null));
		assertFalse("equals(Object,Object) does not work correct", CommonUtils.equals(null, s2));
		assertFalse("equals(Object,Object) does not work correct", CommonUtils.equals(s1, s2));
		assertTrue("equals(Object,Object) does not work correct", CommonUtils.equals(s2, s3));
	}

	@Test
	public void testGetOptionsClassOfQextendsEnumOfQLocale() {
		List<Enum<?>> l = CommonUtils.getOptionList(Continent.class);
		assertEquals("getOptions(Enum) does not work correctly", 8, l.size());
	}

	@Test
	public void testGetOptionList() {
		String l[] = CommonUtils.getOptions(Continent.class, Locale.getDefault());
		assertEquals("getOptions(Enum) does not work correctly", 8, l.length);
		for (String s : l) {
			assertNotNull("getOptions(Enum) does not work correctly", Continent.valueOf(s));
		}
	}

	@Test
	public void testIsCompatibleVersion() {
		assertTrue("isCompatibeVersion(String,String,String) does not work correctly", CommonUtils.isCompatibleVersion(null, "1.2.0", null)); 
		assertTrue("isCompatibeVersion(String,String,String) does not work correctly", CommonUtils.isCompatibleVersion(null, "1.2.0", "0.1.0")); 
		assertFalse("isCompatibeVersion(String,String,String) does not work correctly", CommonUtils.isCompatibleVersion("1.0.0", "1.2.0", "0.1.0")); 
		assertFalse("isCompatibeVersion(String,String,String) does not work correctly", CommonUtils.isCompatibleVersion("1.0.0", null, "0.1.0")); 
		assertTrue("isCompatibeVersion(String,String,String) does not work correctly", CommonUtils.isCompatibleVersion(null, null, "0.1.0")); 
		assertTrue("isCompatibeVersion(String,String,String) does not work correctly", CommonUtils.isCompatibleVersion("1.0.0", "1.2.0", "1.1.0")); 
	}

	@Test
	public void testCompareVersion() {
		assertTrue("compareVersion(String,String) does not work correctly", CommonUtils.compareVersion(null, "1.2.0") < 0); 
		assertTrue("compareVersion(String,String) does not work correctly", CommonUtils.compareVersion("1.1", "1.2.0") < 0); 
		assertTrue("compareVersion(String,String) does not work correctly", CommonUtils.compareVersion("1.1", null) > 0); 
		assertTrue("compareVersion(String,String) does not work correctly", CommonUtils.compareVersion("1.1", "1.1.0") < 0); 
		assertTrue("compareVersion(String,String) does not work correctly", CommonUtils.compareVersion("1.1.0", "1.1.0") == 0); 
		assertTrue("compareVersion(String,String) does not work correctly", CommonUtils.compareVersion((String)null, (String)null) == 0); 
	}

	@Test
	public void testJoin() {
		assertEquals("join(char,String[]) does not work correctly", "a-b-c", CommonUtils.join("-", new String[]{ "a", "b", "c" })); 
	}

	@Test
	public void testJoin2() {
		assertEquals("join(char,String[]) does not work correctly", "b-c", CommonUtils.join("-", new String[]{ "a", "b", "c" }, 1)); 
	}

	@Test
	public void testJoin3() {
		assertEquals("join(char,String[]) does not work correctly", "b-c-d", CommonUtils.join("-", new String[]{ "a", "b", "c", "d", "e" }, 1, 3)); 
	}

	@Test
	public void testGetUnixTimestampLong() {
		assertEquals("getUnixTimestame(long) does not work correctly", 1, CommonUtils.getUnixTimestamp(1000L));
		assertEquals("getUnixTimestame(long) does not work correctly", 1, CommonUtils.getUnixTimestamp(1200L));
		assertEquals("getUnixTimestame(long) does not work correctly", 1, CommonUtils.getUnixTimestamp(1999L));
	}

	@Test
	public void testGetUnixTimestampDate() {
		assertEquals("getUnixTimestame(Date) does not work correctly", 1, CommonUtils.getUnixTimestamp(new Date(1000L)));
		assertEquals("getUnixTimestame(Date) does not work correctly", 1, CommonUtils.getUnixTimestamp(new Date(1200L)));
		assertEquals("getUnixTimestame(Date) does not work correctly", 1, CommonUtils.getUnixTimestamp(new Date(1999L)));
	}

	@Test
	public void testGetUnixTimestampRsDate() {
		assertEquals("getUnixTimestame(RsDate) does not work correctly", 1, CommonUtils.getUnixTimestamp(new RsDate(1000L)));
		assertEquals("getUnixTimestame(RsDate) does not work correctly", 1, CommonUtils.getUnixTimestamp(new RsDate(1200L)));
		assertEquals("getUnixTimestame(RsDate) does not work correctly", 1, CommonUtils.getUnixTimestamp(new RsDate(1999L)));
	}


	@Test
	public void testLoadContentString() throws IOException {
		assertEquals("loadContent() does not work correctly", "finder-file4", CommonUtils.loadContent(FileFinder.find("finder-file4.txt").openStream()).trim());
	}

	@Test
	public void testReplaceEnvVariables() throws IOException {
		String path = System.getenv("PATH");
		String s = "abc=$ENV{PATH}+.text";
		String expected = "abc="+path+"+.text";
		assertEquals("simple replaceEnvVariables does not work", expected, CommonUtils.replaceEnvVariables(s));
	}
	
	@Test
	public void testReplaceRuntimeVariables() throws IOException {
		String userHome = System.getProperty("user.home");
		String s = "abc=+$RUNTIME{user.home}.text";
		String expected = "abc=+"+userHome+".text";
		assertEquals("simple replaceRuntimeVariables does not work", expected, CommonUtils.replaceRuntimeVariables(s));
	}

	@Test
	public void testReplaceVariablesSimple() throws IOException {
		String path = System.getenv("PATH");
		String userHome = System.getProperty("user.home");
		String s = "abc=$ENV{PATH}+$RUNTIME{user.home}.text";
		String expected = "abc="+path+"+"+userHome+".text";
		assertEquals("simple replaceVariables does not work", expected, CommonUtils.replaceVariables(s));
	}
	
	@Test
	public void testReplaceVariablesCombined() throws IOException {
		String path = System.getenv("PATH");
		String userHome = System.getProperty("user.home");
		String s = "abc=$ENV{PATH}+$RUNTIME{user.home}.text";
		String expected = "abc="+path+"+"+userHome+".text";
		assertEquals("simple replaceVariables does not work", expected, CommonUtils.replaceVariables(s));
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
