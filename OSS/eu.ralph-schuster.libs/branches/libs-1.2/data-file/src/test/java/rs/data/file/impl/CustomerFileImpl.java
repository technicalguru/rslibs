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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import rs.baselib.type.Address;
import rs.data.file.bo.AbstractFileBO;

/**
 * Reference Implementation for file-based business objects.
 * @author ralph
 *
 */
public class CustomerFileImpl extends AbstractFileBO<Long> implements Customer {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public CustomerFileImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return getProperty(NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		setProperty(NAME, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Address getInvoiceAddress() {
		return getProperty(INVOICE_ADDRESS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInvoiceAddress(Address address) {
		setProperty(INVOICE_ADDRESS, address);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String,String> getPhones() {
		Map<String,String> rc = getProperty(PHONES);
		if (rc == null) {
			rc = new HashMap<String, String>();
			setPhones(rc);
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPhones(Map<String,String> phone) {
		setProperty(PHONES, phone);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPhone(String purpose, String phone) {
		getPhones().put(purpose, phone);
		firePropertyChange(PHONES, null, purpose);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePhone(String purpose) {
		getPhones().remove(purpose);
		firePropertyChange(PHONES, purpose, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Address> getDeliveryAddresses() {
		Collection<Address> rc = getProperty(DELIVERY_ADDRESSES);
		if (rc == null) {
			rc = new HashSet<Address>();
			setDeliveryAddresses(rc);
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeliveryAddresses(Collection<Address> addresses) {
		setProperty(DELIVERY_ADDRESSES, addresses);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDeliveryAddress(Address address) {
		getDeliveryAddresses().add(address);
		firePropertyChange(DELIVERY_ADDRESS, null, address);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeDeliveryAddress(Address address) {
		getDeliveryAddresses().remove(address);
		firePropertyChange(DELIVERY_ADDRESS, address, null);
	}


}
