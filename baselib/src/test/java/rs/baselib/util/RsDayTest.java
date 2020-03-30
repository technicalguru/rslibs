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

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Tests the {@link RsDay} class.
 * @author ralph
 *
 */
public class RsDayTest {

	/** The reference value */
	private static final long currentTime = System.currentTimeMillis();
	/** The reference timezone */
	private static final String timezone  = "Asia/Bangkok";
	
	/**
	 * Test method for {@link rs.baselib.util.RsDay#getEnd()}.
	 */
	@Test
	public void testGetEnd() {
		RsDay d = new RsDay(currentTime);
		RsDate end = d.getEnd();
		// BASELIB-5 Test: make sure it is today 23:59:59.999
		assertEquals("Year mismatch", d.get(Calendar.YEAR), end.get(Calendar.YEAR));
		assertEquals("Month mismatch", d.get(Calendar.MONTH), end.get(Calendar.MONTH));
		assertEquals("Day mismatch", d.get(Calendar.DAY_OF_MONTH), end.get(Calendar.DAY_OF_MONTH));
		assertEquals("Hour mismatch", 23, end.get(Calendar.HOUR_OF_DAY));
		assertEquals("Minute mismatch", 59, end.get(Calendar.MINUTE));
		assertEquals("Second mismatch", 59, end.get(Calendar.SECOND));
		assertEquals("Millisecond mismatch", 999L, end.get(Calendar.MILLISECOND));
	}

	/**
	 * Test the constructor for Calendars.
	 * <p>The timezone information must be equal.</p>
	 */
	@Test
	public void testRsDayCalendar() {
		RsDay original = new RsDay(TimeZone.getTimeZone(timezone), currentTime);
		RsDay cloned   = new RsDay(original);
		assertEquals("Time is not equal via Calendar constructor", original.getTimeInMillis(), cloned.getTimeInMillis());
		assertEquals("Timezone is not equal via Calendar constructor", original.getTimeZone(), cloned.getTimeZone());
	}

}
