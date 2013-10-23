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

	@Test
	public void testGetTransientProperties() {
		Collection<String> properties = BeanSupport.INSTANCE.getTransientProperties(TestBean.class);
		assertEquals("Not enough transient properties recognized", 3, properties.size());
		assertTrue("dirty not recognized as transient", properties.contains("dirty"));
		assertTrue("changes not recognized as transient", properties.contains("changes"));
		assertTrue("class not recognized as transient", properties.contains("class"));
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
		assertTrue("class not recognized as NoCopy", BeanSupport.INSTANCE.isCopyForbidden(TestBean.class, "class"));
		assertTrue("changeDate not recognized as NoCopy", BeanSupport.INSTANCE.isCopyForbidden(TestBean.class, "changeDate"));
	}

	@Test
	public void testGetForbiddenList() {
		Collection<String> properties = BeanSupport.INSTANCE.getForbiddenList(TestBean.class, true);
		assertEquals("Not enough forbidden properties recognized", 4, properties.size());
		assertTrue("dirty not recognized as NoCopy", properties.contains("dirty"));
		assertTrue("changes not recognized as NoCopy", properties.contains("changes"));
		assertTrue("class not recognized as NoCopy", properties.contains("class"));
		assertTrue("changeDate not recognized as NoCopy", properties.contains("changeDate"));
	}


}
