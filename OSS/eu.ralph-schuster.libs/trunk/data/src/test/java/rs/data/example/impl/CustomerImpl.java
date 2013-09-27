/**
 * 
 */
package rs.data.example.impl;

import rs.data.example.api.Customer;
import rs.data.impl.bo.AbstractMapBO;
import rs.data.impl.dto.MapDTO;

/**
 * Example implementation
 * @author ralph
 *
 */
public class CustomerImpl extends AbstractMapBO<Long> implements Customer {

	/** Serial UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public CustomerImpl() {
		this(new MapDTO<Long>());
	}

	/**
	 * Constructor.
	 * @param transferObject
	 */
	public CustomerImpl(MapDTO<Long> transferObject) {
		super(transferObject);
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
	public String getAddress() {
		return (String)getData(ADDRESS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAddress(String address) {
		setData(ADDRESS, address);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPhone() {
		return (String)getData(PHONE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPhone(String phone) {
		setData(PHONE, phone);
	}

}
