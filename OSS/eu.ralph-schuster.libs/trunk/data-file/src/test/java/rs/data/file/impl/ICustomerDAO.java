/**
 * 
 */
package rs.data.file.impl;

import java.util.List;

import rs.baselib.type.Country;
import rs.data.api.dao.ILongDAO;

/**
 * Reference implementation for a file-based DAO interface.
 * @author ralph
 *
 */
public interface ICustomerDAO extends ILongDAO<ICustomer> {

	/**
	 * Find all customers in a specific city.
	 * @param country country of customers
	 * @param city city of customers
	 * @return all customer record in that city
	 */
	public List<ICustomer> findBy(Country country, String city);
	
}
