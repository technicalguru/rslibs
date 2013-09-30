/**
 * 
 */
package rs.data.file.storage;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import rs.baselib.type.Address;
import rs.baselib.type.Country;
import rs.data.file.impl.Customer;
import rs.data.file.impl.CustomerFileImpl;

/**
 * Tests the XML storage strategy.
 * @author ralph
 *
 */
public class XmlStorageStrategyTest {

	/** The strategy under test. */
	private XmlStorageStrategy strategy;
	/** A test object */
	private Customer customer;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		strategy = new XmlStorageStrategy();
		customer = new CustomerFileImpl();
		customer.setName("John Doe");
		customer.addPhone("private", "+49-69-1234567890");
		customer.addPhone("business", "+49-69-9876543210");
		customer.setInvoiceAddress(new Address("42, 2nd Street", "Frankfurt/Main", "60549", Country.GERMANY));
		customer.addDeliveryAddress(new Address("42, 2nd Street", "Frankfurt/Main", "60549", Country.GERMANY));
		customer.addDeliveryAddress(new Address("1, 4th Street", "Darmstadt", "60549", Country.GERMANY));
	}

	@Test
	public void testWrite() throws IOException {
		File f = new File("writeTest.xml");
		strategy.save(customer, f);
		assertTrue("File was not created", f.exists());
	}

}
