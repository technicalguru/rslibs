/**
 * 
 */
package rs.data.impl.dto;

import java.io.Serializable;

import rsbaselib.util.RsDate;

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

	
}
