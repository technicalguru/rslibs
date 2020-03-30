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

import java.util.TimeZone;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test the {@link RsDate} class.
 * @author ralph
 *
 */
public class RsDateTest {

	/** The reference value */
	private static final long currentTime = System.currentTimeMillis();
	/** The reference timezone */
	private static final String timezone  = "Asia/Bangkok";
	
	/**
	 * Test the constructor for Calendars.
	 * <p>The timezone information must be set correctly.</p>
	 */
	@Test
	public void testRsDateTimezone() {
		RsDate original = new RsDate(TimeZone.getTimeZone(timezone), currentTime);
		assertEquals("Timezone was not initialized correctly", timezone, original.getTimeZone().getID());
	}
	
	/**
	 * Test the constructor for Calendars.
	 * <p>The timezone information must be equal.</p>
	 */
	@Test
	public void testRsDateCalendar() {
		RsDate original = new RsDate(TimeZone.getTimeZone(timezone), currentTime);
		RsDate cloned   = new RsDate(original);
		assertEquals("Time is not equal via Calendar constructor", currentTime, cloned.getTimeInMillis());
		assertEquals("Timezone is not equal via Calendar constructor", original.getTimeZone(), cloned.getTimeZone());
	}
}
