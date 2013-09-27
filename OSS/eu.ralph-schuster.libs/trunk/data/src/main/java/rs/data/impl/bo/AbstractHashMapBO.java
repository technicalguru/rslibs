/**
 * 
 */
package rs.data.impl.bo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import rs.baselib.util.RsDate;

/**
 * A simplified BO storing its data in a HashMap.
 * The save/load mechanism of this BO implementation is externalized and used e.g. for
 * file-based storage. 
 * @author ralph
 *
 */
public abstract class AbstractHashMapBO<K extends Serializable> extends AbstractGeneralBO<K> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;
	/** The map data */
	private Map<String, Object> dataMap;
	
	/**
	 * Constructor.
	 */
	public AbstractHashMapBO() {
		dataMap = createDataMap();
	}

	/**
	 * Creates a new data map on request.
	 * @return a new data map.
	 */
	protected Map<String, Object> createDataMap() {
		return new HashMap<String, Object>();
	}
	
	/**
	 * Returns the data from the map.
	 * @param key key of data
	 * @return data value
	 */
	protected Object getData(String key) {
		return dataMap.get(key);
	}
	
	/**
	 * Sets the data value in the map and fires the property change.
	 * @param key key of data
	 * @param value value of data
	 */
	protected void setData(String key, Object value) {
		Object oldValue = getData(key);
		dataMap.put(key, value);
		firePropertyChange(key, oldValue, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public K getId() {
		return (K)getData("id");
	}

	/**
	 * Sets the id.
	 * @param id id of object
	 */
	public void setId(K id) {
		setData("id", id);
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
