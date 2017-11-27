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
import static rs.baselib.test.BuilderUtils.$Int;
import static rs.baselib.test.BuilderUtils.$Long;
import static rs.baselib.test.BuilderUtils.$RsYear;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import rs.baselib.util.RsDate;
import rs.baselib.util.RsYear;

/**
 * Tests the {@link RsYearBuilder} class.
 * @author ralph
 *
 */
public class RsYearBuilderTest {

	@Test
	public void testDefault() {
		RsYearBuilder b = $RsYear();
		String expected = new SimpleDateFormat("yyyy").format(new Date());
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", expected, actual.getKey());
	}
	
	@Test
	public void testWithTimeLong() {
		long time = 1000000L;
		RsYearBuilder b = $RsYear().withTime(time);
		String expected = new SimpleDateFormat("yyyy").format(new Date(time));
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", expected, actual.getKey());
	}

	@Test
	public void testWithTimeBuilder() {
		// Get the number of days of current month first
		int numDays = new RsDate().getActualMaximum(Calendar.DAY_OF_YEAR);
		LongBuilder builder = $Long().withStart(System.currentTimeMillis()).withOffset(numDays*DateUtils.MILLIS_PER_DAY);
		RsYearBuilder b = $RsYear().withTime(builder);
		RsDate first   = b.build().getBegin();
		RsDate actual  = b.build().getBegin();
		assertEquals("RsYearBuilder not initialized correctly", first.getTimeInMillis()+numDays*DateUtils.MILLIS_PER_DAY, actual.getTimeInMillis());
	}

	@Test
	public void testWithTimezone() {
		TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"));
		RsYearBuilder b = $RsYear().withTimezone(tz);
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", tz, actual.getTimeZone());
	}

	@Test
	public void testWithYearInt() {
		RsYearBuilder b = $RsYear().withYear(2015);
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", "2015", actual.getKey());
	}

	@Test
	public void testWithYearBuilder() {
		RsYearBuilder b = $RsYear().withYear($Int().withStart(2015).withOffset(2));
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", "2015", actual.getKey());
		actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", "2017", actual.getKey());
	}

}
