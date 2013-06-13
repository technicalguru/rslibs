/**
 * 
 */
package rsbaselib.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

import rs.baselib.util.RsDate;
import rs.baselib.util.RsMonth;

/**
 * Tests the {@link RsMonth} class.
 * @author ralph
 *
 */
public class RsMonthTest {

	/** The reference value */
	private static final long currentTime = System.currentTimeMillis();
	
	/**
	 * Test method for {@link rs.baselib.util.RsMonth#getEnd()}.
	 */
	@Test
	public void testGetEnd() {
		RsMonth d = new RsMonth(currentTime);
		RsDate end = d.getEnd();
		// BASELIB-5 Test: make sure it is last day in month at 23:59:59.999
		assertEquals("Year mismatch", d.get(Calendar.YEAR), end.get(Calendar.YEAR));
		assertEquals("Month mismatch", d.get(Calendar.MONTH), end.get(Calendar.MONTH));
		assertEquals("Day mismatch", d.getActualMaximum(Calendar.DAY_OF_MONTH), end.get(Calendar.DAY_OF_MONTH));
		assertEquals("Hour mismatch", 23, end.get(Calendar.HOUR_OF_DAY));
		assertEquals("Minute mismatch", 59, end.get(Calendar.MINUTE));
		assertEquals("Second mismatch", 59, end.get(Calendar.SECOND));
		assertEquals("Millisecond mismatch", 999L, end.get(Calendar.MILLISECOND));
	}

}
