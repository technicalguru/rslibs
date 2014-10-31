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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test the {@link CronSchedule}.
 * @author ralph
 *
 */
public class CronScheduleTest {

	@Test
	public void testHashCode() {
		CronSchedule s1 = new CronSchedule("*/5 */3 * * *");
		CronSchedule s2 = new CronSchedule("*/5 */3 * * *");
		assertEquals("hashCode() is not equal", s1.hashCode(), s2.hashCode());
	}

	@Test
	public void testEquals() {
		CronSchedule s1 = new CronSchedule("*/5 */3 * * *");
		CronSchedule s2 = new CronSchedule("*/5 */3 * * *");
		assertEquals("hashCode() is not equal", s1, s2);
	}
}
