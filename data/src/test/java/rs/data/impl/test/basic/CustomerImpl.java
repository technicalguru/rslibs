/*
 * This file is part of RS Library (Data Base Library).
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
		firePropertyChange(CREATION_DATE, oldValue, creationDate);
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
		firePropertyChange(CHANGE_DATE, oldValue, changeDate);
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
		firePropertyChange(NAME, oldValue, name);
	}

	
}
