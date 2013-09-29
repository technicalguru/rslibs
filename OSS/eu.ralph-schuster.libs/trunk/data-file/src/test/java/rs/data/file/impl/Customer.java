/**
 * 
 */
package rs.data.file.impl;

import rs.baselib.type.Address;
import rs.data.file.bo.AbstractFileBO;

/**
 * Reference Implementation for file-based business objects.
 * @author ralph
 *
 */
public class Customer extends AbstractFileBO<Long> implements ICustomer {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public Customer() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return (String)getData(PROPERTY_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		setData(PROPERTY_NAME, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Address getAddress() {
		return (Address)getData(PROPERTY_ADDRESS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAddress(Address address) {
		setData(PROPERTY_ADDRESS, address);
	}

}
