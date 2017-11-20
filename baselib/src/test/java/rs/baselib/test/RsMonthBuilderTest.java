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
import static org.junit.Assert.assertFalse;
import static rs.baselib.test.BuilderUtils.$RsMonth;
import static rs.baselib.test.BuilderUtils.listOf;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

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
	public void testWithTime() {
		long time = 1000000L;
		RsMonthBuilder b = $RsMonth().withTime(time);
		String expected = new SimpleDateFormat("yyyyMM").format(new Date(time));
		RsMonth actual = b.build();
		assertEquals("RsMonthBuilder not initialized correctly", expected, actual.getKey());
	}

	@Test
	public void testWithTimeOffset() {
		RsMonthBuilder b = $RsMonth().withMonthOffset(1);
		RsMonth first = b.build();
		int days = first.getActualMaximum(Calendar.DAY_OF_MONTH);
		RsMonth actual = b.build();
		assertEquals("RsMonthBuilder not initialized correctly", first.getBegin().getTimeInMillis()+days*DateUtils.MILLIS_PER_DAY, actual.getBegin().getTimeInMillis());
	}

	@Test
	public void testWithTimezone() {
		TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"));
		RsMonthBuilder b = $RsMonth().withTimezone(tz);
		RsMonth actual = b.build();
		assertEquals("RsMonthBuilder not initialized correctly", tz, actual.getTimeZone());
	}

	@Test
	public void testWithMonth() {
		RsMonthBuilder b = $RsMonth().withMonth(0, 2015);
		RsMonth actual = b.build();
		assertEquals("RsMonthBuilder not initialized correctly", "201501", actual.getKey());
	}

	@Test
	public void testList() {
		Set<String> delivered = new HashSet<>();
		for (RsMonth s : listOf(100, $RsMonth().withMonthOffset(1))) {
			assertFalse("RsMonthBuilder does not produce unique dates", delivered.contains(s.getKey()));
			delivered.add(s.getKey());
		}
	}
}
