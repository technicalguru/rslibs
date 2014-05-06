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
package rs.data.impl.dto;

import java.io.Serializable;

import rs.baselib.util.RsDate;

/**
 * Abstract Implementation for Data Transfer Objects.
 * @param <K> type of primary key
 * @author ralph
 *
 */
public abstract class GeneralDTO<K extends Serializable> implements Serializable {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6017060445792763535L;

	private K id;
	private RsDate creationDate;
	private RsDate changeDate;
	private String name;
	
	/**
	 * Constructor.
	 */
	public GeneralDTO() {
	}

	/**
	 * Returns the ID.
	 * @return the ID
	 */
	public K getId() {
		return id;
	}

	/**
	 * Sets the ID.
	 * @param id the ID to set
	 */
	public void setId(K id) {
		this.id = id;
	}

	/**
	 * Returns the creation date.
	 * @return the creation date
	 */
	public RsDate getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date.
	 * @param creationDate the creation date to set
	 */
	public void setCreationDate(RsDate creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Returns the change date.
	 * @return the change date
	 */
	public RsDate getChangeDate() {
		return changeDate;
	}

	/**
	 * Sets the change date.
	 * @param changeDate the change date to set
	 */
	public void setChangeDate(RsDate changeDate) {
		this.changeDate = changeDate;
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
		this.name = name;
	}

}
