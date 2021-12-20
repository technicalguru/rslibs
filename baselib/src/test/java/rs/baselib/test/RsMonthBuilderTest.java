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
package rs.baselib.test;

import static org.junit.Assert.assertEquals;
import static rs.baselib.test.BuilderUtils.$Long;
import static rs.baselib.test.BuilderUtils.$RsMonth;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import rs.baselib.util.RsDate;
import rs.baselib.util.RsMonth;

/**
 * Tests the {@link RsMonthBuilder} class.
 * @author ralph
 *
 */
public class RsMonthBuilderTest {

	@Test
	public void testDefault() {
		RsMonthBuilder b = $RsMonth();
		String expected = new SimpleDateFormat("yyyyMM").format(new Date());
		RsMonth actual = b.build();
		assertEquals("RsMonthBuilder not initialized correctly", expected, actual.getKey());
	}
	
	@Test
	public void testWithTimeLong() {
		long time = 1000000L;
		RsMonthBuilder b = $RsMonth().withTime(time);
		String expected = new SimpleDateFormat("yyyyMM").format(new Date(time));
		RsMonth actual = b.build();
		assertEquals("RsMonthBuilder not initialized correctly", expected, actual.getKey());
	}

	@Test
	public void testWithTimeBuilder() {
		// Get the number of days of current month first
		RsDate today = new RsDate();
		int numDays = today.getActualMaximum(Calendar.DAY_OF_MONTH);
		// Subtract the number of days still to go and add a few more
		numDays = numDays - today.get(Calendar.DAY_OF_MONTH) + 10;
		LongBuilder builder = $Long().withStart(System.currentTimeMillis()).withOffset(numDays*DateUtils.MILLIS_PER_DAY);
		RsMonthBuilder b = $RsMonth().withTimezone(TimeZone.getTimeZone("UTC")).withTime(builder);
		RsDate first   = b.build().getBegin();
		RsDate actual  = b.build().getBegin();
		int expected = first.get(Calendar.MONTH)+1;
		if (expected > 11) expected = 0;
		assertEquals("RsMonthBuilder not initialized correctly", expected, actual.get(Calendar.MONTH));
		assertEquals("RsMonthBuilder not initialized correctly", actual.get(Calendar.DAY_OF_MONTH), 1);
	}

	@Test
	public void testWithTimeOffset() {
		RsMonthBuilder b = $RsMonth().withTimezone(TimeZone.getTimeZone("UTC")).withMonthOffset(1);
		RsMonth first = b.build();
		RsMonth actual = b.build();
		int expected = first.get(Calendar.MONTH)+1;
		if (expected > 11) expected = 0;
		assertEquals("RsMonthBuilder not initialized correctly", expected, actual.get(Calendar.MONTH));
		assertEquals("RsMonthBuilder not initialized correctly", actual.getBegin().get(Calendar.DAY_OF_MONTH), 1);
	}

	@Test
	public void testWithTimezone() {
		TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"));
		RsMonthBuilder b = $RsMonth().withTimezone(tz);
		RsMonth actual = b.build();
		assertEquals("RsMonthBuilder not initialized correctly", tz, actual.getTimeZone());
	}

}
