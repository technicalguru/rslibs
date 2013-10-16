/**
 * 
 */
package rs.data.file.impl;

import rs.data.impl.AbstractDaoFactory;

/**
 * @author ralph
 *
 */
public class ExampleDaoFactoryImpl extends AbstractDaoFactory implements ExampleDaoFactory {

	/**
	 * Constructor.
	 */
	public ExampleDaoFactoryImpl() {
		registerDao(new CustomerDAOFileImpl());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerDAO getCustomerDao() {
		return getDao(CustomerDAO.class);
	}

}
