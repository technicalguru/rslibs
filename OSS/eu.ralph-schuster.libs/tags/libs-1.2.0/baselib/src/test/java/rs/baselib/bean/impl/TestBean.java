/**
 * 
 */
package rs.baselib.bean.impl;

import rs.baselib.bean.AbstractBean;
import rs.baselib.bean.NoCopy;
import rs.baselib.util.RsDate;

/**
 * A simple abstract bean.
 * @author ralph
 *
 */
public class TestBean extends AbstractBean {

	/** property for {@link #name} property */
	public static final String NAME = "name";
	/** property for {@link #changeDate} property */
	public static final String CHANGE_DATE = "changeDate";
	
	/** The name property */
	private String name;
	/** The change date property */
	private RsDate changeDate;
	
	public TestBean() {
		this(null);
	}

	public TestBean(String name) {
		setName(name);
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		String oldValue = getName();
		this.name = name;
		firePropertyChange(NAME, oldValue, name);
	}

	/**
	 * Returns the changeDate.
	 * @return the changeDate
	 */
	@NoCopy
	public RsDate getChangeDate() {
		return changeDate;
	}

	/**
	 * Sets the changeDate.
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(RsDate changeDate) {
		RsDate oldValue = getChangeDate();
		this.changeDate = changeDate;
		firePropertyChange(CHANGE_DATE, oldValue, changeDate);
	}
	
	
}
