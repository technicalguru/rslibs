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
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import rs.baselib.bean.impl.TestBean;

/**
 * Tests {@link BeanValueProvider}.
 * @author ralph
 *
 */
public class BeanValueProviderTest {

	private BeanValueProvider provider;
	private TestBean bean;
	
	/**
	 * Sets up a value provider
	 * @throws java.lang.Exception - when the provider cannot be setup
	 */
	@Before
	public void setUp() throws Exception {
		provider = new BeanValueProvider(TestBean.NAME);
		bean = new TestBean();
	}

	/**
	 * Test method for {@link BeanValueProvider#getValue(Object)}.
	 */
	@Test
	public void testGetValue1() {
		assertNull("Provider delivered invalid value", provider.getValue(bean));
	}

	/**
	 * Test method for {@link BeanValueProvider#getValue(Object)}.
	 */
	@Test
	public void testGetValue2() {
		provider.setNullValue("default");
		assertEquals("Provider delivered invalid value", "default", provider.getValue(bean));
	}

	/**
	 * Test method for {@link BeanValueProvider#getValue(Object)}.
	 */
	@Test
	public void testGetValue3() {
		bean.setName("aName");
		assertEquals("Provider delivered invalid value", "aName", provider.getValue(bean));
	}

	/**
	 * Test method for {@link BeanValueProvider#getValueProviders(String...)}.
	 */
	@Test
	public void testGetValueProviders() {
		BeanValueProvider providers[] = BeanValueProvider.getValueProviders(TestBean.NAME, TestBean.CHANGE_DATE, IDirtyable.DIRTY);
		assertEquals("Not enough providers", 3, providers.length);
		assertEquals("Invalid provider", TestBean.NAME, providers[0].getBeanProperty());
		assertEquals("Invalid provider", TestBean.CHANGE_DATE, providers[1].getBeanProperty());
		assertEquals("Invalid provider", IDirtyable.DIRTY, providers[2].getBeanProperty());
		
	}
}
