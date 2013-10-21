package rs.data.impl.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import rs.data.impl.test.basic.Customer;
import rs.data.impl.test.basic.CustomerDaoImpl;
import rs.data.util.CID;

/**
 * Tests basic caching functions.
 * @author ralph
 *
 */
public class BasicDaoTest {

	/** The DAO to be tested */
	CustomerDaoImpl dao = null;
	
	@Before
	public void setup() {
		dao = new CustomerDaoImpl();
	}
	
	@Test
	public void testAddCached() {
		int before = dao.getCache().size();
		Customer c = dao.newInstance();
		dao.create(c);
		int after = dao.getCache().size();
		assertEquals("Cache did not increase properly", before+1, after);
	}
	
	@Test
	public void testGetCached() {
		Customer c = dao.newInstance();
		dao.create(c);
		Customer c2 = dao.getCached(c.getCID());
		assertNotNull("DAO cannot deliver BO from cache", c2);
		assertEquals("DAO cannot deliver correct BO from cache", c, c2);
	}

	@Test
	public void testRemoveCached() {
		Customer c = dao.newInstance();
		dao.create(c);
		dao.removeCached(c);
		Customer c2 = dao.getCached(c.getCID());
		assertNull("DAO did not remove from cache", c2);
		
	}

	@Test
	public void testFindById() {
		Customer c = dao.newInstance();
		dao.create(c);
		long id = c.getId();
		Customer c2 = dao.findBy(id);
		assertEquals("DAO cannot find BO", c, c2);
	}

	@Test
	public void testCacheBehaviour() {
		Customer c = dao.newInstance();
		dao.create(c);
		dao._delete(c);
		CID cid = new CID(Customer.class, c.getId());
		System.gc();
		assertNotNull("Cache cleared unexpectedly", dao.getCached(cid));
		c = null;
		System.gc();
		assertNull("Cache was not cleared", dao.getCached(cid));
	}
}
