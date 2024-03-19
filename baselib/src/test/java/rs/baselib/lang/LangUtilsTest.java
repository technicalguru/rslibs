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
package rs.baselib.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link LangUtils}.
 * @author ralph
 *
 */
public class LangUtilsTest {

	protected static class BaseClass<A, B> { };
	protected static class ChildClass<A> extends BaseClass<A,Long> { };
	protected static class GrandChildClass extends BaseClass<String,Long> { };
	
	private static Double testNumber = Double.valueOf(128.025);
	private static double EPSILON = 0.026d;
	
	/**
	 * Test method for {@link LangUtils#getTypeArguments(Class, Class)}.
	 */
	@Test
	public void testGetTypeArguments() {
		List<Class<?>> l = ReflectionUtils.getTypeArguments(BaseClass.class, GrandChildClass.class);
		assertNotNull(l);
		assertEquals(2, l.size());
		assertEquals(String.class, l.get(0));
		assertEquals(Long.class, l.get(1));
	}

	/**
	 * Test method for {@link LangUtils#getString(Object)}.
	 */
	@Test
	public void testGetString() {
		String s = "Hello";
		assertEquals(s, LangUtils.getString(s));
		assertNull(LangUtils.getString(null));
		assertEquals(toString(), LangUtils.getString(this));
	}

	/**
	 * Test method for {@link LangUtils#getInt(Object)}.
	 */
	@Test
	public void testGetInt() {
		assertEquals(0, LangUtils.getInt(null));
		assertEquals(45, LangUtils.getInt("45"));
		assertEquals(testNumber.intValue(), LangUtils.getInt(testNumber));
	}

	/**
	 * Test method for {@link LangUtils#getLong(Object)}.
	 */
	@Test
	public void testGetLong() {
		assertEquals(0, LangUtils.getLong(null));
		assertEquals(69, LangUtils.getLong("69"));
		assertEquals(testNumber.longValue(), LangUtils.getLong(testNumber));
	}

	/**
	 * Test method for {@link LangUtils#getDouble(Object)}.
	 */
	@Test
	public void testGetDouble() {
		assertEquals(0, LangUtils.getDouble(null), 0d);
		assertEquals(0, LangUtils.getDouble("0"), EPSILON);
		assertEquals(testNumber.doubleValue(), LangUtils.getDouble(testNumber), EPSILON);
	}

	/**
	 * Test method for {@link LangUtils#getBoolean(Object)}.
	 */
	@Test
	public void testGetBoolean() {
		Object trueValues[] = new Object[] { "1", "trUe", "oN", "YES", "y", Boolean.TRUE };
		Object falseValues[] = new Object[] { "0", "15", "Off", "No", "n", Boolean.FALSE, this };
		for (Object o : trueValues) {
			assertTrue(LangUtils.getBoolean(o));
		}
		for (Object o : falseValues) {
			assertFalse(LangUtils.getBoolean(o));
		}
	}

	/**
	 * Test method for {@link LangUtils#getDate(Object, DateFormat[], Date)}.
	 */
	@Test
	public void testGetDateObjectDateFormatArrayDate() {
		Date d = new Date();
		Date defDate = new Date(0);
		DateFormat formats[] = new DateFormat[] {
				DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG),
				DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG),
				DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG),
		};
		assertNull(LangUtils.getDate("123456", formats, null));
		assertNull(LangUtils.getDate(null, formats, null));
		assertEquals(defDate, LangUtils.getDate("123456", formats, defDate));
		assertEquals(defDate, LangUtils.getDate(null, formats, defDate));
		assertEquals(d, LangUtils.getDate(d, formats, null));
		assertEquals(d, LangUtils.getDate(d, formats, defDate));
		assertEquals(d.toString(), LangUtils.getDate(formats[2].format(d), formats, null).toString());
		assertEquals(d.toString(), LangUtils.getDate(formats[2].format(d), formats, defDate).toString());	
	}

	/**
	 * Test method for {@link LangUtils#getDate(Object, DateFormat, Date)}.
	 */
	@Test
	public void testGetDateObjectDateFormatDate() {
		Date d = new Date();
		Date defDate = new Date(0);
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
		assertNull(LangUtils.getDate("123456", format, null, false));
		assertNull(LangUtils.getDate(null, format, null, false));
		assertEquals(defDate, LangUtils.getDate("123456", format, defDate, false));
		assertEquals(defDate, LangUtils.getDate(null, format, defDate, false));
		assertEquals(d, LangUtils.getDate(d, format, null, false));
		assertEquals(d, LangUtils.getDate(d, format, defDate, false));
		assertEquals(d.toString(), LangUtils.getDate(format.format(d), format, null, false).toString());
		assertEquals(d.toString(), LangUtils.getDate(format.format(d), format, defDate, false).toString());	
	}

	/**
	 * Test method for {@link LangUtils#serializeBase64(Object)}.
	 * 
	 * @throws IOException - when the test data cannot be serialized
	 * @throws ClassNotFoundException - when unserializing cannot find the test class
	 */
	@Test
	public void testSerializeBase64() throws IOException, ClassNotFoundException {
		Long l = Long.valueOf(23L);
		assertEquals(l, LangUtils.unserialize(LangUtils.serializeBase64(l)));
	}

	/**
	 * Test method for {@link LangUtils#serialize(Object)}.
	 * @throws IOException - when the test data cannot be unserialized
	 * @throws ClassNotFoundException - when serializing cannot find the test class
	 */
	@Test
	public void testSerialize() throws IOException, ClassNotFoundException {
		Long l = Long.valueOf(23L);
		assertEquals(l, LangUtils.unserialize(LangUtils.serialize(l)));
	}

	/**
	 * Test method for {@link LangUtils#sleep(long)}.
	 */
	@Test
	public void testSleep() {
		long start = System.currentTimeMillis();
		LangUtils.sleep(500L);
		assertTrue(System.currentTimeMillis() - start >= 500L);
	}

	@Test
	public void testNullClass() {
		assertFalse(ReflectionUtils.isInstanceOf((Object)null, "java.lang.Object"));
	}

	@Test
	public void testAlwaysObjectClass() {
		assertTrue(ReflectionUtils.isInstanceOf(4L, "java.lang.Object"));
	}

	@Test
	public void testInterfaceClass() {
		assertTrue(ReflectionUtils.isInstanceOf(4L, "java.lang.Comparable"));
	}
	
	@Test
	public void testSuperInterfaceClass() {
		assertTrue(ReflectionUtils.isInstanceOf(new GregorianCalendar(), "java.lang.Comparable"));
	}
	
	@Test
	public void testInterfaceSuperClass() {
		assertTrue(ReflectionUtils.isInstanceOf(new ArrayList<Object>(), "java.util.Collection"));
	}

	@Test
	public void testNoInstanceClass() {
		assertFalse(ReflectionUtils.isInstanceOf(4L, "java.util.Collection"));
	}

}
