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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import rs.baselib.bean.impl.TestBean;
import rs.baselib.util.IDirtyable;
import rs.baselib.util.RsDate;

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
		assertNotNull("Type arguments are null", l);
		assertEquals("Type arguments are incorrect", 2, l.size());
		assertEquals("Type arguments are incorrect", String.class, l.get(0));
		assertEquals("Type arguments are incorrect", Long.class, l.get(1));
	}

	/**
	 * Test method for {@link LangUtils#getString(Object)}.
	 */
	@Test
	public void testGetString() {
		String s = "Hello";
		assertEquals("Cannot detect string", s, LangUtils.getString(s));
		assertNull("Cannot detect null string", LangUtils.getString(null));
		assertEquals("Cannot detect string", toString(), LangUtils.getString(this));
	}

	/**
	 * Test method for {@link LangUtils#getInt(Object)}.
	 */
	@Test
	public void testGetInt() {
		assertEquals("Cannot detect null int", 0, LangUtils.getInt(null));
		assertEquals("Cannot detect int", 45, LangUtils.getInt("45"));
		assertEquals("Cannot detect int", testNumber.intValue(), LangUtils.getInt(testNumber));
	}

	/**
	 * Test method for {@link LangUtils#getLong(Object)}.
	 */
	@Test
	public void testGetLong() {
		assertEquals("Cannot detect null long", 0, LangUtils.getLong(null));
		assertEquals("Cannot detect long", 69, LangUtils.getLong("69"));
		assertEquals("Cannot detect long", testNumber.longValue(), LangUtils.getLong(testNumber));
	}

	/**
	 * Test method for {@link LangUtils#getDouble(Object)}.
	 */
	@Test
	public void testGetDouble() {
		assertEquals("Cannot detect null double", 0, LangUtils.getDouble(null), 0d);
		assertEquals("Cannot detect double", 0, LangUtils.getDouble("0"), EPSILON);
		assertEquals("Cannot detect double", testNumber.doubleValue(), LangUtils.getDouble(testNumber), EPSILON);
	}

	/**
	 * Test method for {@link LangUtils#getBoolean(Object)}.
	 */
	@Test
	public void testGetBoolean() {
		Object trueValues[] = new Object[] { "1", "trUe", "oN", "YES", "y", Boolean.TRUE };
		Object falseValues[] = new Object[] { "0", "15", "Off", "No", "n", Boolean.FALSE, this };
		for (Object o : trueValues) {
			assertTrue("Cannot convert to boolean: "+o, LangUtils.getBoolean(o));
		}
		for (Object o : falseValues) {
			assertFalse("Cannot convert to boolean: "+o, LangUtils.getBoolean(o));
		}
	}

	/**
	 * Test method for {@link LangUtils#getRsDate(Object, DateFormat[], RsDate)}.
	 */
	@Test
	public void testGetRsDateObjectDateFormatArrayRsDate() {
		RsDate d = new RsDate();
		RsDate defDate = new RsDate(0);
		DateFormat formats[] = new DateFormat[] {
				DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG),
				DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG),
				DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG),
		};
		assertNull("Cannot get date", LangUtils.getRsDate("123456", formats, null));
		assertNull("Cannot get date", LangUtils.getRsDate(null, formats, null));
		assertEquals("Cannot get date", defDate, LangUtils.getRsDate("123456", formats, defDate));
		assertEquals("Cannot get date", defDate, LangUtils.getRsDate(null, formats, defDate));
		assertEquals("Cannot get date", d, LangUtils.getRsDate(d, formats, null));
		assertEquals("Cannot get date", d, LangUtils.getRsDate(d, formats, defDate));
		assertEquals("Cannot get date", d.getTime().toString(), LangUtils.getRsDate(formats[2].format(d.getTime()), formats, null).getTime().toString());
		assertEquals("Cannot get date", d.getTime().toString(), LangUtils.getRsDate(formats[2].format(d.getTime()), formats, defDate).getTime().toString());	
	}

	/**
	 * Test method for {@link LangUtils#getRsDate(Object, DateFormat, RsDate)}.
	 */
	@Test
	public void testGetRsDateObjectDateFormatRsDate() {
		RsDate d = new RsDate();
		RsDate defDate = new RsDate(0);
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
		assertNull("Cannot get date", LangUtils.getRsDate("123456", format, null, false));
		assertNull("Cannot get date", LangUtils.getRsDate(null, format, null, false));
		assertEquals("Cannot get date", defDate, LangUtils.getRsDate("123456", format, defDate, false));
		assertEquals("Cannot get date", defDate, LangUtils.getRsDate(null, format, defDate, false));
		assertEquals("Cannot get date", d, LangUtils.getRsDate(d, format, null, false));
		assertEquals("Cannot get date", d, LangUtils.getRsDate(d, format, defDate, false));
		assertEquals("Cannot get date", d.getTime().toString(), LangUtils.getRsDate(format.format(d.getTime()), format, null, false).getTime().toString());
		assertEquals("Cannot get date", d.getTime().toString(), LangUtils.getRsDate(format.format(d.getTime()), format, defDate, false).getTime().toString());	
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
		assertNull("Cannot get date", LangUtils.getDate("123456", formats, null));
		assertNull("Cannot get date", LangUtils.getDate(null, formats, null));
		assertEquals("Cannot get date", defDate, LangUtils.getDate("123456", formats, defDate));
		assertEquals("Cannot get date", defDate, LangUtils.getDate(null, formats, defDate));
		assertEquals("Cannot get date", d, LangUtils.getDate(d, formats, null));
		assertEquals("Cannot get date", d, LangUtils.getDate(d, formats, defDate));
		assertEquals("Cannot get date", d.toString(), LangUtils.getDate(formats[2].format(d), formats, null).toString());
		assertEquals("Cannot get date", d.toString(), LangUtils.getDate(formats[2].format(d), formats, defDate).toString());	
	}

	/**
	 * Test method for {@link LangUtils#getDate(Object, DateFormat, Date)}.
	 */
	@Test
	public void testGetDateObjectDateFormatDate() {
		Date d = new Date();
		Date defDate = new Date(0);
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
		assertNull("Cannot get date", LangUtils.getDate("123456", format, null, false));
		assertNull("Cannot get date", LangUtils.getDate(null, format, null, false));
		assertEquals("Cannot get date", defDate, LangUtils.getDate("123456", format, defDate, false));
		assertEquals("Cannot get date", defDate, LangUtils.getDate(null, format, defDate, false));
		assertEquals("Cannot get date", d, LangUtils.getDate(d, format, null, false));
		assertEquals("Cannot get date", d, LangUtils.getDate(d, format, defDate, false));
		assertEquals("Cannot get date", d.toString(), LangUtils.getDate(format.format(d), format, null, false).toString());
		assertEquals("Cannot get date", d.toString(), LangUtils.getDate(format.format(d), format, defDate, false).toString());	
	}

	/**
	 * Test method for {@link LangUtils#isJava6()}.
	 */
	@Test
	public void testIsJava6() {
		String javaVersion = System.getProperty("java.specification.version");
		boolean is6 = javaVersion.contains("1.6");
		assertEquals("Cannot correct determine Java6 from \""+javaVersion+"\"", is6, LangUtils.isJava6());
	}

	/**
	 * Test method for {@link LangUtils#serializeBase64(Object)}.
	 */
	@Test
	public void testSerializeBase64() throws IOException, ClassNotFoundException {
		Long l = Long.valueOf(23L);
		assertEquals("Cannot serialize/unserialize", l, LangUtils.unserialize(LangUtils.serializeBase64(l)));
	}

	/**
	 * Test method for {@link LangUtils#serialize(Object)}.
	 */
	@Test
	public void testSerialize() throws IOException, ClassNotFoundException {
		Long l = Long.valueOf(23L);
		assertEquals("Cannot serialize/unserialize", l, LangUtils.unserialize(LangUtils.serialize(l)));
	}

	/**
	 * Test method for {@link LangUtils#toString(String, Object[])}.
	 */
	@Test
	public void testToStringStringObjectArray() {
		assertEquals("Cannot format properly", "rs.baselib.bean.impl.TestBean[property1=value1;property2=null;property3=value3]", LangUtils.toString(TestBean.class.getName(), "property1", "value1", "property2", null, "property3", "value3"));	
	}

	/**
	 * Test method for {@link LangUtils#isTransient(PropertyDescriptor)}.
	 */
	@Test
	public void testIsTransient() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		TestBean bean = new TestBean();
		assertTrue("dirty was not recognized as transient", LangUtils.isTransient(PropertyUtils.getPropertyDescriptor(bean, IDirtyable.DIRTY)));
		assertFalse("name was recognized as transient", LangUtils.isTransient(PropertyUtils.getPropertyDescriptor(bean, TestBean.NAME)));
	}

	/**
	 * Test method for {@link LangUtils#isNoCopy(PropertyDescriptor)}.
	 */
	@Test
	public void testIsNoCopy() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		TestBean bean = new TestBean();
		assertTrue("changeDate was not recognized as no copy", LangUtils.isNoCopy(PropertyUtils.getPropertyDescriptor(bean, TestBean.CHANGE_DATE)));
		assertFalse("name was recognized as no copy", LangUtils.isNoCopy(PropertyUtils.getPropertyDescriptor(bean, TestBean.NAME)));
	}

	/**
	 * Test method for {@link LangUtils#sleep(long)}.
	 */
	@Test
	public void testSleep() {
		long start = System.currentTimeMillis();
		LangUtils.sleep(500L);
		assertTrue("sleep is not long enough", System.currentTimeMillis() - start >= 500L);
	}

	@Test
	public void testNullClass() {
		assertFalse("NULL objects must be false in isInstanceOf check", ReflectionUtils.isInstanceOf((Object)null, "java.lang.Object"));
	}

	@Test
	public void testAlwaysObjectClass() {
		assertTrue("Object is not recognized", ReflectionUtils.isInstanceOf(4L, "java.lang.Object"));
	}

	@Test
	public void testInterfaceClass() {
		assertTrue("Interface is not recognized", ReflectionUtils.isInstanceOf(4L, "java.lang.Comparable"));
	}
	
	@Test
	public void testSuperInterfaceClass() {
		assertTrue("Interface of superclass is not recognized", ReflectionUtils.isInstanceOf(new GregorianCalendar(), "java.lang.Comparable"));
	}
	
	@Test
	public void testInterfaceSuperClass() {
		assertTrue("Superclass of interface is not recognized", ReflectionUtils.isInstanceOf(new ArrayList<Object>(), "java.util.Collection"));
	}

	@Test
	public void testNoInstanceClass() {
		assertFalse("Interface shall not be detecetd", ReflectionUtils.isInstanceOf(4L, "java.util.Collection"));
	}

}
