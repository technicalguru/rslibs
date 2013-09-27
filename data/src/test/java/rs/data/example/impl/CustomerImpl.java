/**
 * 
 */
package rs.data.example.impl;

import rs.data.example.api.Customer;
import rs.data.impl.bo.AbstractBO;
import rs.data.impl.dto.PropertiesDTO;

/**
 * Example implementation
 * @author ralph
 *
 */
public class CustomerImpl extends AbstractBO<Long,PropertiesDTO<Long>> implements Customer {

	/** Serial UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public CustomerImpl() {
		this(new PropertiesDTO<Long>());
	}

	/**
	 * Constructor.
	 * @param transferObject
	 */
	public CustomerImpl(PropertiesDTO<Long> transferObject) {
		super(transferObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return getTransferObject().getProperty(NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		String oldValue = getAddress();
		getTransferObject().setProperty(NAME, name);
		firePropertyChange(NAME, oldValue, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAddress() {
		return getTransferObject().getProperty(ADDRESS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAddress(String address) {
		String oldValue = getAddress();
		getTransferObject().setProperty(ADDRESS, address);
		firePropertyChange(ADDRESS, oldValue, address);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPhone() {
		return getTransferObject().getProperty(PHONE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPhone(String phone) {
		String oldValue = getPhone();
		getTransferObject().setProperty(PHONE, phone);
		firePropertyChange(PHONE, oldValue, phone);
	}

}
