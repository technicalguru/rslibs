/**
 * 
 */
package rs.data.file.impl;

import rs.baselib.type.Address;
import rs.data.file.bo.AbstractPropertiesFileBO;

/**
 * @author ralph
 *
 */
public class Customer extends AbstractPropertiesFileBO<Long> implements ICustomer {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public Customer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return (String)get(PROPERTY_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		set(PROPERTY_NAME, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Address getAddress() {
		return (Address)get(PROPERTY_ADDRESS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAddress(Address address) {
		set(PROPERTY_ADDRESS, address);
	}

}
