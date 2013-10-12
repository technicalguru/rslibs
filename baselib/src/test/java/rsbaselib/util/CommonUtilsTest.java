package rsbaselib.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import rs.baselib.util.CommonUtils;

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
		assertTrue("Trace does not start with correct method", lines.get(0).startsWith("at "+getClass().getName()+".testGetStackTrace"));
	}
}
