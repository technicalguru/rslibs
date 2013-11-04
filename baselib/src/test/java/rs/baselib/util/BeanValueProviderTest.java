/**
 * 
 */
package rs.baselib.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rs.baselib.bean.impl.TestBean;
import rs.baselib.util.BeanValueProvider;
import rs.baselib.util.IDirtyable;

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
	 * @throws java.lang.Exception
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
