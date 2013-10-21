/**
 * 
 */
package rs.data.impl.test.basic;

import rs.data.impl.dao.AbstractMemoryDao;

/**
 * A sample customer DAO for testing.
 * @author ralph
 *
 */
public class CustomerDaoImpl extends AbstractMemoryDao<Long,CustomerImpl,Customer> implements CustomerDao {

	long counter = 0;
	
	/**
	 * Constructor.
	 */
	public CustomerDaoImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getNewId() {
		counter++;
		return counter;
	}

}
