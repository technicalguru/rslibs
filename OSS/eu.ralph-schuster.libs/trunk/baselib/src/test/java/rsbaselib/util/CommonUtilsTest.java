package rsbaselib.util;

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
		assertEquals("toString(RsDate) does not work correct", CommonUtils.DATE_FORMATTER.format(d.getTime()), CommonUtils.toString(d));
	}

	@Test
	public void testToStringRsMonth() {
		assertEquals("toString(RsMonth) does not work correct", "", CommonUtils.toString((RsDate)null));
		assertEquals("toString(RsMonth) does not work correct", "", CommonUtils.toString(new RsDate(0)));
		RsMonth d = new RsMonth();
		assertEquals("toString(RsMonth) does not work correct", CommonUtils.DATE_FORMATTER.format(d.getBegin().getTime()), CommonUtils.toString(d));
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


}
