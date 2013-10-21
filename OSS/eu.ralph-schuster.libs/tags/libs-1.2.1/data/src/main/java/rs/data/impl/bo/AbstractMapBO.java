/**
 * 
 */
package rs.data.impl.bo;

import java.io.Serializable;

import rs.baselib.util.RsDate;
import rs.data.impl.dto.MapDTO;

/**
 * A simplified BO storing its data in a HashMap.
 * The save/load mechanism of this BO implementation is externalized and used e.g. for
 * file-based storage. 
 * @author ralph
 *
 */
public abstract class AbstractMapBO<K extends Serializable> extends AbstractBO<K, MapDTO<K>> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 */
	public AbstractMapBO() {
		this (new MapDTO<K>());
	}

	/**
	 * Constructor.
	 */
	public AbstractMapBO(MapDTO<K> transferObject) {
		super(transferObject);
	}

	/**
	 * Returns the data from the map.
	 * @param key key of data
	 * @return data value
	 */
	protected Object getData(String key) {
		return getTransferObject().getProperty(key);
	}
	
	/**
	 * Sets the data value in the map and fires the property change.
	 * @param key key of data
	 * @param value value of data
	 */
	protected void setData(String key, Object value) {
		Object oldValue = getData(key);
		getTransferObject().setProperty(key, value);
		firePropertyChange(key, oldValue, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getCreationDate() {
		return (RsDate)getData(PROPERTY_CREATION_DATE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationDate(RsDate creationDate) {
		setData(PROPERTY_CREATION_DATE, creationDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getChangeDate() {
		return (RsDate)getData(PROPERTY_CHANGE_DATE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChangeDate(RsDate changeDate) {
		setData(PROPERTY_CHANGE_DATE, changeDate);
	}

}
