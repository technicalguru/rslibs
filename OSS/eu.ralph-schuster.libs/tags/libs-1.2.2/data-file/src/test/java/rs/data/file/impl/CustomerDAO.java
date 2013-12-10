/**
 * 
 */
package rs.data.file.impl;

import java.util.List;

import rs.baselib.type.Country;
import rs.data.api.dao.ILongDAO;

/**
 * Sample DAO interface for customers.
 * @author ralph
 *
 */
public interface CustomerDAO extends ILongDAO<Customer> {

	/**
	 * Finds all customers of a specific city. 
	 * @param country country
	 * @param city city 
	 * @return all customers of that city
	 */
	public List<Customer> findBy(Country country, String city);
}
