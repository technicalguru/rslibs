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
package rs.baselib.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import rs.baselib.bean.impl.TestBean;

public class BeanSupportTest {

	@Test
	public void testIsTransient() {
		assertTrue("dirty is not recognized as transient", BeanSupport.INSTANCE.isTransient(TestBean.class, "dirty"));
		assertFalse("name is recognized as transient", BeanSupport.INSTANCE.isTransient(TestBean.class, TestBean.NAME));
	}

	/**
	 * Test transient properties.
	 * Expected transient: dirty, propertyNames, changes."class" is not transient anymore (since 1.4.0).
	 */
	@Test
	public void testGetTransientProperties() {
		Collection<String> properties = BeanSupport.INSTANCE.getTransientProperties(TestBean.class);
		assertEquals("Not enough transient properties recognized", 3, properties.size());
		assertTrue("dirty not recognized as transient", properties.contains("dirty"));
		assertTrue("changes not recognized as transient", properties.contains("changes"));
	}

	@Test
	public void testGetNonTransientProperties() {
		Collection<String> properties = BeanSupport.INSTANCE.getNonTransientProperties(TestBean.class);
		assertEquals("Not enough non-transient properties recognized", 2, properties.size());
		assertTrue("name recognized as transient", properties.contains("name"));
		assertTrue("changeDate recognized as transient", properties.contains("changeDate"));
	}

	@Test
	public void testisCopyForbidden() {
		assertTrue("dirty not recognized as NoCopy", BeanSupport.INSTANCE.isCopyForbidden(TestBean.class, "dirty"));
		assertTrue("changes not recognized as NoCopy", BeanSupport.INSTANCE.isCopyForbidden(TestBean.class, "changes"));
		assertTrue("changeDate not recognized as NoCopy", BeanSupport.INSTANCE.isCopyForbidden(TestBean.class, "changeDate"));
	}

	/**
	 * Tests forbiddden properties.
	 * Expected: dirty, propertyNames, changeDate, changes. "class" is not a property anymore (since v1.4.0)
	 */
	@Test
	public void testGetForbiddenList() {
		Collection<String> properties = BeanSupport.INSTANCE.getForbiddenList(TestBean.class, true);
		assertEquals("Not enough forbidden properties recognized", 4, properties.size());
		assertTrue("dirty not recognized as NoCopy", properties.contains("dirty"));
		assertTrue("changes not recognized as NoCopy", properties.contains("changes"));
		assertTrue("changeDate not recognized as NoCopy", properties.contains("changeDate"));
	}


}
