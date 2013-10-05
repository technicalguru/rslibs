/**
 * 
 */
package rs.data.file.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import rs.baselib.io.FileFinder;
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
		strategy = new XmlStorageStrategy(null);
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

	@Test
	public void testLoad() throws IOException, URISyntaxException {
		File f = new File(FileFinder.find("xmlStorageTest1.xml").toURI());
		Customer loaded = new CustomerFileImpl();
		strategy.load(loaded, f);
		assertEquals("changeDate does not match", customer.getChangeDate(), loaded.getChangeDate());
		assertEquals("creationDate does not match", customer.getCreationDate(), loaded.getCreationDate());
		assertEquals("name does not match", customer.getName(), loaded.getName());
		assertEquals("invoiceAddress does not match", customer.getInvoiceAddress(), loaded.getInvoiceAddress());
		assertEquals("phones count does not match", customer.getPhones().size(), loaded.getPhones().size());
		for (Map.Entry<String,String> entry : customer.getPhones().entrySet()) {
			assertTrue("phones do not contain \""+entry.getKey()+"\"", loaded.getPhones().containsKey(entry.getKey()));
			assertEquals("\""+entry.getKey()+"\" phone does not match", customer.getPhones().get(entry.getKey()), loaded.getPhones().get(entry.getKey()));
		}
		assertEquals("deliveryAddress count does not match", customer.getDeliveryAddresses().size(), loaded.getDeliveryAddresses().size());
		for (Address a : customer.getDeliveryAddresses()) {
			assertTrue("deliveryAddress not found: "+a, loaded.getDeliveryAddresses().contains(a));
		}
	}
}
