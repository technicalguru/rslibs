/**
 * 
 */
package rs.data.file.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rs.baselib.lang.LangUtils;
import rs.baselib.type.Address;
import rs.baselib.type.Country;
import rs.data.file.impl.Customer;
import rs.data.file.impl.CustomerDAOFileImpl;
import rs.data.file.impl.ExampleDaoFactory;
import rs.data.file.impl.ExampleDaoFactoryImpl;

/**
 * Testing the DAO.
 * @author ralph
 *
 */
public class FileDaoTest {

	private static ExampleDaoFactory factory;
	private CustomerDAOFileImpl customerDao;
	
	@BeforeClass
	public static void beforeClass() {
		factory = new ExampleDaoFactoryImpl();
	}
	

	@AfterClass
	public static void afterClass() {
		
	}
	
	@Before
	public void setup() {
		customerDao = (CustomerDAOFileImpl)factory.getCustomerDao();
		customerDao.deleteAll();
		LangUtils.sleep(500L);
	}
	
	@After
	public void cleanup() {
		customerDao.deleteAll();
		LangUtils.sleep(500L);
	}
	
	@Test
	public void test01_newInstance() {
		Customer customer = customerDao.newInstance();
		assertNotNull("Cannot create new customer", customer);
	}
	
	@Test
	public void test02_getNewId() {
		assertNotNull("Cannot create a new ID", customerDao.getNewId());
	}
	
	@Test
	public void test03_create() {
		Customer customer = customerDao.newInstance();
		setup(customer);
		customerDao.create(customer);
		LangUtils.sleep(500L);
		
		assertNotNull("Customer did not receive an ID", customer.getId());
		File f = customerDao.getFilenameStrategy().getFile(customer.getId());
		assertTrue("Customer was not saved", f.exists());
	}
	
	protected void setup(Customer customer) {
		customer.setName("John Doe");
		customer.addPhone("private", "+49-69-1234567890");
		customer.addPhone("business", "+49-69-9876543210");
		customer.setInvoiceAddress(new Address("42, 2nd Street", "Frankfurt/Main", "60549", Country.GERMANY));
		customer.addDeliveryAddress(new Address("42, 2nd Street", "Frankfurt/Main", "60549", Country.GERMANY));
		customer.addDeliveryAddress(new Address("1, 4th Street", "Darmstadt", "60549", Country.GERMANY));
	}
	
	@Test
	public void test04_findBy() {
		Customer customer = createCustomer();
		Customer customer2 = customerDao.findBy(customer.getId()); 
		assertNotNull("Customer cannot be found", customer2);
	}
	
	@Test
	public void test05_findAll() {
		createCustomer();
		List<Customer> l = customerDao.findAll();
		assertEquals("Cannot find all customers correctly", 1, l.size());
	}
	
	@Test
	public void test06_save() {
		Customer customer = createCustomer();
		Long id = customer.getId();
		long beforeChange = customer.getChangeDate().getTimeInMillis();
		customer.setName("John Doe2");
		customerDao.save(customer);
		long afterChange = customer.getChangeDate().getTimeInMillis();
		assertTrue("changeDate was not modified", beforeChange < afterChange);
		customer = customerDao.findBy(id);
		assertEquals("Name was not saved correctly", "John Doe2", customer.getName());
	}
	
	@Test
	public void test07_delete() {
		Customer customer = createCustomer();
		Long id = customer.getId();
		File f = customerDao.getFilenameStrategy().getFile(id);
		customerDao.delete(customer);
		LangUtils.sleep(500L);
		assertFalse("File was not deleted", f.exists());
		customer = customerDao.findBy(id);
		assertNull("Customer still exists in DAO", customer);
	}
	
	/** Helper method */
	protected Customer createCustomer() {
		Customer customer = customerDao.newInstance();
		setup(customer);
		customerDao.create(customer);
		LangUtils.sleep(500);
		return customer;
	}
	
}
