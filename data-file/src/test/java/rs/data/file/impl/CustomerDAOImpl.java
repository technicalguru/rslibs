/**
 * 
 */
package rs.data.file.impl;

import java.util.ArrayList;
import java.util.List;

import rs.baselib.type.Address;
import rs.baselib.type.Country;
import rs.data.file.dao.AbstractFileDAO;

/**
 * Reference implementation of file-based DAO.
 * @author ralph
 *
 */
public class CustomerDAOImpl extends AbstractFileDAO<Long, Customer, ICustomer> implements ICustomerDAO {

	/**
	 * Constructor.
	 */
	public CustomerDAOImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ICustomer> findBy(Country country, String city) {
		List<ICustomer> rc = new ArrayList<ICustomer>();
		// There is no index in this reference implementation
		// So we need to iterate all customers.
		for (ICustomer customer : findAll()) {
			Address address = customer.getAddress();
			if (address != null) {
				Country cc = address.getCountry();
				if ((cc != null) && cc.equals(country)) {
					String ccity = address.getCity();
					if ((ccity != null) && ccity.equalsIgnoreCase(city)) {
						rc.add(customer);
					}
				}
			}
		}
		return rc;
	}


}
