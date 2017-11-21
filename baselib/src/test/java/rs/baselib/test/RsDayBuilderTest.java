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
import static rs.baselib.test.BuilderUtils.$Long;
import static rs.baselib.test.BuilderUtils.$RsDay;
import static rs.baselib.test.BuilderUtils.listOf;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import rs.baselib.util.RsDate;
import rs.baselib.util.RsDay;

/**
 * Tests the {@link RsDayBuilder} class.
 * @author ralph
 *
 */
public class RsDayBuilderTest {

	@Test
	public void testDefault() {
		RsDayBuilder b = $RsDay();
		String expected = new SimpleDateFormat("yyyyMMdd").format(new Date());
		RsDay actual = b.build();
		assertEquals("RsDayBuilder not initialized correctly", expected, actual.getKey());
	}
	
	@Test
	public void testWithTimeLong() {
		long time = 1000000L;
		RsDayBuilder b = $RsDay().withTime(time);
		String expected = new SimpleDateFormat("yyyyMMdd").format(new Date(time));
		RsDay actual = b.build();
		assertEquals("RsDayBuilder not initialized correctly", expected, actual.getKey());
	}

	@Test
	public void testWithTimeBuilder() {
		LongBuilder builder = $Long().withStart(100000L).withOffset(2*DateUtils.MILLIS_PER_DAY);
		RsDayBuilder b = $RsDay().withTime(builder);
		RsDate first   = b.build().getBegin();
		RsDate actual  = b.build().getBegin();
		assertEquals("RsDayBuilder not initialized correctly", first.getTimeInMillis()+2*DateUtils.MILLIS_PER_DAY, actual.getTimeInMillis());
	}

	@Test
	public void testWithTimeOffset() {
		RsDayBuilder b = $RsDay().withDayOffset(1);
		RsDate first  = b.build().getBegin();
		RsDate actual = b.build().getBegin()	;
		assertEquals("RsDayBuilder not initialized correctly", first.getTimeInMillis()+DateUtils.MILLIS_PER_DAY, actual.getTimeInMillis());
	}

	@Test
	public void testWithTimezone() {
		TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"));
		RsDayBuilder b = $RsDay().withTimezone(tz);
		RsDay actual = b.build();
		assertEquals("RsDayBuilder not initialized correctly", tz, actual.getTimeZone());
	}

	@Test
	public void testWithDay() {
		RsDayBuilder b = $RsDay().withDay(1, 0, 2015);
		RsDay actual = b.build();
		assertEquals("RsDayBuilder not initialized correctly", "20150101", actual.getKey());
	}

	@Test
	public void testList() {
		Set<String> delivered = new HashSet<>();
		for (RsDay s : listOf(100, $RsDay().withDayOffset(1))) {
			assertFalse("RsDayBuilder does not produce unique dates", delivered.contains(s.getKey()));
			delivered.add(s.getKey());
		}
	}
}
