/**
 * 
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
		return (String)getData(NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		setData(NAME, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Address getInvoiceAddress() {
		return (Address)getData(INVOICE_ADDRESS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInvoiceAddress(Address address) {
		setData(INVOICE_ADDRESS, address);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,String> getPhones() {
		Map<String,String> rc = (Map<String,String>)getData(PHONES);
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
		setData(PHONES, phone);
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
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Address> getDeliveryAddresses() {
		Collection<Address> rc = (Collection<Address>)getData(DELIVERY_ADDRESSES);
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
		setData(DELIVERY_ADDRESSES, addresses);
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
