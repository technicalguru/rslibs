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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rs.baselib.test.BuilderUtils.$LocalDate;

import java.time.LocalDate;
import java.time.Period;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link LocalDateBuilder}.
 * 
 * @author ralph
 *
 */
public class LocalDateBuilderTest {

	@Test
	public void testDefaultStart() {
		LocalDateBuilder b      = $LocalDate();
		LocalDate        now    = LocalDate.now();
		LocalDate        actual = b.build();
		assertEquals(now, actual);
	}
	
	@Test
	public void testStart() {
		LocalDate        start  = LocalDate.now().plusDays(1);
		LocalDateBuilder b      = $LocalDate().withStart(start);
		LocalDate        actual = b.build();
		assertEquals(start, actual);
	}
	
	@Test
	public void testStep() {
		LocalDateBuilder b      = $LocalDate().withStep(Period.ofDays(1));
		LocalDate        first  = b.build();
		LocalDate        actual = b.build();
		assertEquals(first.plusDays(1), actual);
	}
	
	@Test
	public void testRandom() {
		LocalDate        now    = LocalDate.now();
		LocalDate        start  = now.minusDays(10);
		LocalDateBuilder b      = $LocalDate().withRandom().withStart(start).withEnd(now);
		for (int i=0; i<100; i++) {
			LocalDate        actual = b.build();
			assertTrue(start.isEqual(actual) || start.isBefore(actual));
			assertTrue(now.isAfter(actual));
		}
	}
	
	@Test
	public void testRandom_withStartGreaterThanEnd() {
		LocalDate        now    = LocalDate.now();
		LocalDate        start  = now.minusDays(10);
		LocalDateBuilder b      = $LocalDate().withRandom().withStart(now).withEnd(start);
		for (int i=0; i<100; i++) {
			LocalDate        actual = b.build();
			assertTrue(start.isEqual(actual) || start.isBefore(actual));
			assertTrue(now.isAfter(actual));
		}
	}
	

}
