/**
 * 
 */
package rs.data.type;

import java.io.File;
import java.io.Serializable;

/**
 * A key based on a file.
 * @author ralph
 *
 */
public class FileKey<K extends Serializable> extends CombinedKey {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public FileKey(File file, K key) {
		super(file, key);
	}

	/**
	 * Returns the file.
	 * @return the file
	 */
	public File getFile() {
		return getKey(0);
	}

	/**
	 * Sets the file.
	 * @param file the file to set
	 */
	public void setFile(File file) {
		setKey(0, file);
	}

	/**
	 * Returns the key.
	 * @return the key
	 */
	public K getKey() {
		return getKey(1);
	}

	/**
	 * Sets the key.
	 * @param key the key to set
	 */
	public void setKey(K key) {
		setKey(1, key);
	}

	/**
	 * Returns the keyClass.
	 * @return the keyClass
	 */
	@SuppressWarnings("unchecked")
	public Class<K> getKeyClass() {
		return (Class<K>)getKeyClass(1);
	}
	
	

}
