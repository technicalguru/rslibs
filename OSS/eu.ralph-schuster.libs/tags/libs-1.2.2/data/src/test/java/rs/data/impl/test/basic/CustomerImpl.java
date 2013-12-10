/**
 * 
 */
package rs.data.impl.test.basic;

import rs.baselib.util.RsDate;
import rs.data.impl.bo.AbstractGeneralBO;

/**
 * A basic BO implementation.
 * @author ralph
 *
 */
public class CustomerImpl extends AbstractGeneralBO<Long> implements Customer {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private RsDate creationDate;
	private RsDate changeDate;
	private String name;
	
	/**
	 * Constructor.
	 */
	public CustomerImpl() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getCreationDate() {
		return creationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationDate(RsDate creationDate) {
		RsDate oldValue = getCreationDate();
		this.creationDate = creationDate;
		firePropertyChange(PROPERTY_CREATION_DATE, oldValue, creationDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getChangeDate() {
		return changeDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChangeDate(RsDate changeDate) {
		RsDate oldValue = getChangeDate();
		this.changeDate = changeDate;
		firePropertyChange(PROPERTY_CHANGE_DATE, oldValue, changeDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		String oldValue = getName();
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldValue, name);
	}

	
}
