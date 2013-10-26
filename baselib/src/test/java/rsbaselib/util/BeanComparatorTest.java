/**
 * 
 */
package rsbaselib.util;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.junit.Test;

import rs.baselib.bean.impl.TestBean;
import rs.baselib.util.BeanComparator;
import rs.baselib.util.BeanValueProvider;
import rs.baselib.util.DefaultComparator;
import rs.baselib.util.IDirtyable;
import rs.baselib.util.IValueProvider;
import rs.baselib.util.RsDate;

/**
 * Test for {@link BeanComparator}.
 * @author ralph
 *
 */
public class BeanComparatorTest {

	/**
	 * Test method for {@link BeanComparator#BeanComparator(String[])}.
	 */
	@Test
	public void testConstructorStringArray() {
		BeanComparator<TestBean> cmp = new BeanComparator<TestBean>(TestBean.NAME, TestBean.CHANGE_DATE, IDirtyable.DIRTY);
		IValueProvider providers[] = cmp.getValueProviders();
		assertNotNull("No value providers created", providers);
		assertEquals("Not enough value providers created", 3, providers.length);
		for (IValueProvider p : providers) {
			assertTrue("Not a BeanValueProvider", p instanceof BeanValueProvider);
		}
		assertEquals("Not the correct provider", TestBean.NAME, ((BeanValueProvider)providers[0]).getBeanProperty());
		assertEquals("Not the correct provider", TestBean.CHANGE_DATE, ((BeanValueProvider)providers[1]).getBeanProperty());
		assertEquals("Not the correct provider", IDirtyable.DIRTY, ((BeanValueProvider)providers[2]).getBeanProperty());
		assertTrue("Not the correct comparator", cmp.getComparator() instanceof DefaultComparator);
	}

	/**
	 * Test method for {@link BeanComparator#BeanComparator(Comparator, String[])}.
	 */
	@Test
	public void testConstructorComparatorStringArray() {
		BeanComparator<TestBean> cmp = new BeanComparator<TestBean>(new TestComparator(), TestBean.NAME, TestBean.CHANGE_DATE, IDirtyable.DIRTY);
		IValueProvider providers[] = cmp.getValueProviders();
		assertNotNull("No value providers created", providers);
		assertEquals("Not enough value providers created", 3, providers.length);
		for (IValueProvider p : providers) {
			assertTrue("Not a BeanValueProvider", p instanceof BeanValueProvider);
		}
		assertEquals("Not the correct provider", TestBean.NAME, ((BeanValueProvider)providers[0]).getBeanProperty());
		assertEquals("Not the correct provider", TestBean.CHANGE_DATE, ((BeanValueProvider)providers[1]).getBeanProperty());
		assertEquals("Not the correct provider", IDirtyable.DIRTY, ((BeanValueProvider)providers[2]).getBeanProperty());
		assertTrue("Not the correct comparator", cmp.getComparator() instanceof TestComparator);
	}

	/**
	 * Test method for {@link BeanComparator#BeanComparator(IValueProvider[])}.
	 */
	@Test
	public void testConstructorIValueProviderArray() {
		IValueProvider origProviders[] = new IValueProvider[] {
				new BeanValueProvider(TestBean.NAME),
				new BeanValueProvider(TestBean.CHANGE_DATE),
				new BeanValueProvider(IDirtyable.DIRTY)
		};
		BeanComparator<TestBean> cmp = new BeanComparator<TestBean>(origProviders);
		IValueProvider providers[] = cmp.getValueProviders();
		assertNotNull("No value providers created", providers);
		assertEquals("Not enough value providers created", 3, providers.length);
		for (IValueProvider p : providers) {
			assertTrue("Not a BeanValueProvider", p instanceof BeanValueProvider);
		}
		assertEquals("Not the correct provider", TestBean.NAME, ((BeanValueProvider)providers[0]).getBeanProperty());
		assertEquals("Not the correct provider", TestBean.CHANGE_DATE, ((BeanValueProvider)providers[1]).getBeanProperty());
		assertEquals("Not the correct provider", IDirtyable.DIRTY, ((BeanValueProvider)providers[2]).getBeanProperty());
		assertTrue("Not the correct comparator", cmp.getComparator() instanceof DefaultComparator);
	}

	/**
	 * Test method for {@link BeanComparator#BeanComparator(Comparator, IValueProvider[])}.
	 */
	@Test
	public void testConstructorComparatorIValueProviderArray() {
		IValueProvider origProviders[] = new IValueProvider[] {
				new BeanValueProvider(TestBean.NAME),
				new BeanValueProvider(TestBean.CHANGE_DATE),
				new BeanValueProvider(IDirtyable.DIRTY)
		};
		BeanComparator<TestBean> cmp = new BeanComparator<TestBean>(new TestComparator(), origProviders);
		IValueProvider providers[] = cmp.getValueProviders();
		assertNotNull("No value providers created", providers);
		assertEquals("Not enough value providers created", 3, providers.length);
		for (IValueProvider p : providers) {
			assertTrue("Not a BeanValueProvider", p instanceof BeanValueProvider);
		}
		assertEquals("Not the correct provider", TestBean.NAME, ((BeanValueProvider)providers[0]).getBeanProperty());
		assertEquals("Not the correct provider", TestBean.CHANGE_DATE, ((BeanValueProvider)providers[1]).getBeanProperty());
		assertEquals("Not the correct provider", IDirtyable.DIRTY, ((BeanValueProvider)providers[2]).getBeanProperty());
		assertTrue("Not the correct comparator", cmp.getComparator() instanceof TestComparator);
	}

	/**
	 * Test method for {@link BeanComparator#compare(Object, Object)}.
	 */
	@Test
	public void testCompare() {
		BeanComparator<TestBean> cmp = new BeanComparator<TestBean>(TestBean.NAME, TestBean.CHANGE_DATE, IDirtyable.DIRTY);
		TestBean bean1 = new TestBean("bean1"); bean1.setChangeDate(new RsDate(30000L));
		TestBean bean2 = new TestBean("bean2"); bean2.setChangeDate(new RsDate(20000L));
		TestBean bean3 = new TestBean("bean3"); bean3.setChangeDate(new RsDate(20000L));
		assertTrue("compare does not work correctly", cmp.compare(bean1, bean2) < 0);
		assertTrue("compare does not work correctly", cmp.compare(bean2, bean3) < 0);
		assertTrue("compare does not work correctly", cmp.compare(bean1, bean3) < 0);
	}

	protected static class TestComparator implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			return o1.hashCode()-o2.hashCode();
		}
		
	}
}
