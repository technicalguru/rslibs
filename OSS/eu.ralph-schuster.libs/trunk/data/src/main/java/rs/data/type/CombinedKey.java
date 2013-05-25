/**
 * 
 */
package rs.data.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A key combining multiple other serializables.
 * @author ralph
 *
 */
public abstract class CombinedKey implements Serializable {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/** The keys */
	private List<Serializable> keys = new ArrayList<Serializable>();
	/** The type of keys */
	private List<Class<? extends Serializable>> keyClasses = new ArrayList<Class<? extends Serializable>>();

	/**
	 * Constructor.
	 */
	public CombinedKey(Serializable ...keys) {
		setKeys(keys);
	}

	/**
	 * Constructor.
	 */
	public void setKeys(Serializable ...keys) {
		this.keys.clear();
		this.keyClasses.clear();
		for (Serializable key : keys) {
			addKey(key);
		}
	}

	/**
	 * Adds the given key to the end of keys.
	 * @param key key to be added
	 */
	private void addKey(Serializable key) {
		addKey(getSize(), key);
	}

	/**
	 * Adds the given key at given index
	 * @param index index at which to be inserted
	 * @param key key to be added
	 */
	private void addKey(int index, Serializable key) {
		this.keys.add(index, key);
		this.keyClasses.add(index, key.getClass());
	}
	
	/**
	 * Sets the given key at given index.
	 * @param index index to be set
	 * @param key key to be set
	 */
	protected void setKey(int index, Serializable key) {
		this.keys.add(index, key);
		this.keyClasses.add(index, key.getClass());
	}
	
	/**
	 * Returns the number of keys.
	 * @return number of keys
	 */
	protected int getSize() {
		return keys.size();
	}
	
	/**
	 * Returns the key at given index
	 * @param index the inndex
	 * @return the key
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Serializable> T getKey(int index) {
		return (T)keys.get(index);
	}
	
	/**
	 * Returns the key at given index
	 * @param index the inndex
	 * @return the key
	 */
	protected Class<? extends Serializable> getKeyClass(int index) {
		return keyClasses.get(index);
	}
}
