/**
 * 
 */
package rsbaselib.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

import rs.baselib.util.RsDate;
import rs.baselib.util.RsYear;

/**
 * Tests the {@link RsYear} class.
 * @author ralph
 *
 */
public class RsYearTest {

	/** The reference value */
	private static final long currentTime = System.currentTimeMillis();
	
	/**
	 * Test method for {@link rs.baselib.util.RsYear#getEnd()}.
	 */
	@Test
	public void testGetEnd() {
		RsYear d = new RsYear(currentTime);
		RsDate end = d.getEnd();
		// BASELIB-5 Test: make sure it is 31.12. of year at 23:59:59.999
		assertEquals("Year mismatch", d.get(Calendar.YEAR), end.get(Calendar.YEAR));
		assertEquals("Month mismatch", 11, end.get(Calendar.MONTH));
		assertEquals("Day mismatch", 31, end.get(Calendar.DAY_OF_MONTH));
		assertEquals("Hour mismatch", 23, end.get(Calendar.HOUR_OF_DAY));
		assertEquals("Minute mismatch", 59, end.get(Calendar.MINUTE));
		assertEquals("Second mismatch", 59, end.get(Calendar.SECOND));
		assertEquals("Millisecond mismatch", 999L, end.get(Calendar.MILLISECOND));
	}

}
