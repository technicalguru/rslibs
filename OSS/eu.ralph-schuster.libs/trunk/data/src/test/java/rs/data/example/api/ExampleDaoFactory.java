/**
 * 
 */
package rs.data.example.api;

import rs.data.api.IDaoFactory;

/**
 * Example factory.
 * @author ralph
 *
 */
public interface ExampleDaoFactory extends IDaoFactory {

	/**
	 * Returns the DAO for customers.
	 * @return the DAO
	 */
	public CustomerDAO getCustomerDao();
}
