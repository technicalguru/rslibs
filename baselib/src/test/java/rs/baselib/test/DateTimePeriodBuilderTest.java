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
import static org.junit.Assert.assertNull;
import static rs.baselib.test.BuilderUtils.$DateTimePeriod;
import static rs.baselib.test.BuilderUtils.$RsDate;

import org.junit.Test;

import rs.baselib.util.DateTimePeriod;
import rs.baselib.util.RsDate;

/**
 * Tests the {@link DateTimePeriodBuilder} class.
 * @author ralph
 *
 */
public class DateTimePeriodBuilderTest {

	@Test
	public void testWithFromRsDate() {
		long time = 1000000L;
		RsDate from = new RsDate(time);
		DateTimePeriodBuilder b = $DateTimePeriod().withFrom(from);
		DateTimePeriod actual = b.build();
		assertEquals("DateTimePeriodBuilder not initialized correctly", time, actual.getFrom().getTimeInMillis());
		assertNull("DateTimePeriodBuilder not initialized correctly", actual.getUntil());
	}

	@Test
	public void testWithUntilRsDate() {
		long time = 1000000L;
		RsDate until = new RsDate(time);
		DateTimePeriodBuilder b = $DateTimePeriod().withUntil(until);
		DateTimePeriod actual = b.build();
		assertEquals("DateTimePeriodBuilder not initialized correctly", time, actual.getUntil().getTimeInMillis());
		assertNull("DateTimePeriodBuilder not initialized correctly", actual.getFrom());
	}

	@Test
	public void testWithFromBuilder() {
		long time = 1000000L;
		RsDateBuilder dateBuilder = $RsDate().withTime(time);
		DateTimePeriodBuilder b = $DateTimePeriod().withFrom(dateBuilder);
		DateTimePeriod actual = b.build();
		assertEquals("DateTimePeriodBuilder not initialized correctly", time, actual.getFrom().getTimeInMillis());
		assertNull("DateTimePeriodBuilder not initialized correctly", actual.getUntil());
	}

	@Test
	public void testWithUntilBuilder() {
		long time = 1000000L;
		RsDateBuilder dateBuilder = $RsDate().withTime(time);
		DateTimePeriodBuilder b = $DateTimePeriod().withUntil(dateBuilder);
		DateTimePeriod actual = b.build();
		assertEquals("DateTimePeriodBuilder not initialized correctly", time, actual.getUntil().getTimeInMillis());
		assertNull("DateTimePeriodBuilder not initialized correctly", actual.getFrom());
	}

}
