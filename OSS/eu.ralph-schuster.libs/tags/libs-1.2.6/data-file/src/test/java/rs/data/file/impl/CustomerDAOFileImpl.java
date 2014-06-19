/*
 * This file is part of RS Library (Data File Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
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
public class CustomerDAOFileImpl extends AbstractFileDAO<Long, CustomerFileImpl, Customer> implements CustomerDAO {

	/**
	 * Constructor.
	 */
	public CustomerDAOFileImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Customer> findBy(Country country, String city) {
		List<Customer> rc = new ArrayList<Customer>();
		// There is no index in this reference implementation
		// So we need to iterate all customers.
		for (Customer customer : findAll()) {
			Address address = customer.getInvoiceAddress();
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
