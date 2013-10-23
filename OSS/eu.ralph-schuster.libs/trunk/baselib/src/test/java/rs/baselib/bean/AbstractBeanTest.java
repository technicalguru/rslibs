/**
 * 
 */
package rs.baselib.bean;

import static org.junit.Assert.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

import rs.baselib.bean.impl.TestBean;
import rs.baselib.util.IDirtyable;
import rs.baselib.util.RsDate;

/**
 * Tests the {@link AbstractBean}.
 * @author ralph
 *
 */
public class AbstractBeanTest {

	private TestBean bean;
	
	/**
	 * Sets up the {@link TestBean}.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		bean = new TestBean();
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#isDirty()}.
	 */
	@Test
	public void testIsDirty() {
		assertFalse("TestBean is dirty.", bean.isDirty());
		bean.setName("A new name");
		assertTrue("TestBean is not dirty.", bean.isDirty());
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#setDirty(boolean)}.
	 */
	@Test
	public void testSetDirty() {
		assertFalse("TestBean is dirty.", bean.isDirty());
		bean.setDirty(true);
		assertTrue("TestBean is not dirty.", bean.isDirty());
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#firePropertyChange(java.beans.PropertyChangeEvent)}.
	 */
	@Test
	public void testFirePropertyChangePropertyChangeEvent() {
		TestPropertyChangeListener myListener = new TestPropertyChangeListener();
		bean.addPropertyChangeListener(myListener);
		
		// Test a no change
		PropertyChangeEvent evt = new PropertyChangeEvent(bean, TestBean.NAME, "name1", "name1");
		bean.firePropertyChange(evt);
		assertNull("PropertyChangeEvent was fired although no change happened", myListener.received);
		
		// Test a change
		evt = new PropertyChangeEvent(bean, TestBean.NAME, "name1", "name2");
		bean.firePropertyChange(evt);
		assertTrue("PropertyChangeEvent was not fired correctly", myListener.received == evt);
	}
	
	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#firePropertyChange(java.beans.PropertyChangeEvent, boolean)}.
	 */
	@Test
	public void testFirePropertyChangePropertyChangeEventTrue() {
		// Test a no change
		PropertyChangeEvent evt = new PropertyChangeEvent(bean, TestBean.NAME, "name1", "name1");
		bean.firePropertyChange(evt, true);
		assertFalse("dirty is true although no change happened", bean.isDirty());
		
		// Test a change
		evt = new PropertyChangeEvent(bean, TestBean.NAME, "name1", "name2");
		bean.firePropertyChange(evt, true);
		assertTrue("dirty is false although a change happened", bean.isDirty());
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#firePropertyChange(java.beans.PropertyChangeEvent, boolean)}.
	 */
	@Test
	public void testFirePropertyChangePropertyChangeEventFalse() {
		// Test a no change
		PropertyChangeEvent evt = new PropertyChangeEvent(bean, TestBean.NAME, "name1", "name1");
		bean.firePropertyChange(evt, false);
		assertFalse("dirty is true although makeDirty was false", bean.isDirty());
		
		// Test a change
		evt = new PropertyChangeEvent(bean, TestBean.NAME, "name1", "name2");
		bean.firePropertyChange(evt, false);
		assertFalse("dirty is true although makeDirty was false", bean.isDirty());
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#registerChange(java.lang.String, java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testRegisterChange() {
		Collection<PropertyChangeEvent> changes = bean.getChanges();
		assertEquals("The change list is not empty", 0, changes.size());
		
		// Test a no change
		bean.registerChange(TestBean.NAME, null, null);
		changes = bean.getChanges();
		assertEquals("The change list is not empty", 0, changes.size());
		
		// Test new change
		bean.registerChange(TestBean.NAME, null, "name1");
		changes = bean.getChanges();
		assertEquals("The change list is incorrect", 1, changes.size());
		PropertyChangeEvent evt = changes.iterator().next();
		assertEquals("The change list does not have NAME change", TestBean.NAME, evt.getPropertyName());
		assertNull("The change list does not have correct old value", evt.getOldValue());
		assertEquals("The change list does not have correct new value", "name1", evt.getNewValue());
		
		// Test another change
		bean.registerChange(TestBean.NAME, "name1", "name2");
		changes = bean.getChanges();
		assertEquals("The change list is incorrect", 1, changes.size());
		evt = changes.iterator().next();
		assertEquals("The change list does not have NAME change", TestBean.NAME, evt.getPropertyName());
		assertNull("The change list does not have correct old value", evt.getOldValue());
		assertEquals("The change list does not have correct new value", "name2", evt.getNewValue());
		
		// Test a change that reverts all
		bean.registerChange(TestBean.NAME, "name2", null);
		changes = bean.getChanges();
		assertEquals("The change list is not empty", 0, changes.size());
		
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#getOriginalValue(java.lang.String)}.
	 */
	@Test
	public void testGetOriginalValue() {
		bean.setName("name1");
		bean.setName("name2");
		assertNull("The originalValue is not null", bean.getOriginalValue(TestBean.NAME));
		bean.setDirty(false);
		assertEquals("The originalValue is not correct", "name2", bean.getOriginalValue(TestBean.NAME));
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#set(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testSet() {
		bean.set(TestBean.NAME, "name1");
		assertEquals("The name is not correct", "name1", bean.getName());
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
		bean.setName("name1");
		assertEquals("The name is not correct", "name1", bean.get(TestBean.NAME));
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#getChanges()}.
	 */
	@Test
	public void testGetChanges() {
		Collection<PropertyChangeEvent> changes = bean.getChanges();
		assertEquals("The change list is not empty", 0, changes.size());
		
		bean.setName("name1");
		bean.setName("name2");
		changes = bean.getChanges();
		assertEquals("The change list is not correct", 1, changes.size());
		PropertyChangeEvent evt = changes.iterator().next();
		assertEquals("The change list does not have NAME change", TestBean.NAME, evt.getPropertyName());
		assertNull("The change list does not have correct old value", evt.getOldValue());
		assertEquals("The change list does not have correct new value", "name2", evt.getNewValue());
		
		bean.setDirty(false);
		changes = bean.getChanges();
		assertEquals("The change list is not empty", 0, changes.size());

	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#copyTo(java.lang.Object)}.
	 */
	@Test
	public void testCopyTo() {
		bean.setName("name1");
		bean.setChangeDate(new RsDate());
		bean.setDirty(false);
		TestBean bean2 = new TestBean();
		bean.copyTo(bean2);
		assertEquals("Name was not copied", bean.getName(), bean2.getName());
		assertNull("ChangeDate was copied", bean2.getChangeDate());
		assertTrue("bean2 is not dirty", bean2.isDirty());
		Collection<PropertyChangeEvent> changes = bean2.getChanges();
		assertEquals("Change list is not correct", 1, changes.size());
		PropertyChangeEvent evt = changes.iterator().next();
		assertEquals("The change list does not have NAME change", TestBean.NAME, evt.getPropertyName());
		assertNull("The change list does not have correct old value", evt.getOldValue());
		assertEquals("The change list does not have correct new value", "name1", evt.getNewValue());
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#isCopyAllowed(java.beans.PropertyDescriptor)}.
	 */
	@Test
	public void testIsCopyAllowed() throws Exception {
		assertTrue("name cannot be copied", bean.isCopyAllowed(PropertyUtils.getPropertyDescriptor(bean, TestBean.NAME)));
		assertFalse("changeDate can be copied", bean.isCopyAllowed(PropertyUtils.getPropertyDescriptor(bean, TestBean.CHANGE_DATE)));
		assertFalse("dirty can be copied", bean.isCopyAllowed(PropertyUtils.getPropertyDescriptor(bean, "dirty")));
	}

	/**
	 * Test method for {@link rs.baselib.bean.AbstractBean#reset()}.
	 */
	@Test
	public void testReset() {
		bean.setName("name1");
		bean.setChangeDate(new RsDate());
		bean.reset();
		assertNull("The name is not reset", bean.getName());
		assertNull("The changeDate is not reset", bean.getChangeDate());
		assertFalse("The bean is still dirty", bean.isDirty());
	}

	/**
	 * Test listener to check the received {@link PropertyChangeEvent}.
	 * @author ralph
	 *
	 */
	protected class TestPropertyChangeListener implements PropertyChangeListener {
		PropertyChangeEvent received;
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			this.received = evt;
		}
		
	}
}
