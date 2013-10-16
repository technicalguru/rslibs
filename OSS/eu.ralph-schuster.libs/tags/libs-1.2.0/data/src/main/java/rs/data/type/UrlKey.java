/**
 * 
 */
package rs.data.type;

import java.io.Serializable;
import java.net.URL;

/**
 * A key based on a URL.
 * @param <K> the type of key that is combines with the URL
 * @author ralph
 *
 */
public class UrlKey<K extends Serializable> extends CombinedKey {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public UrlKey(URL url, K key) {
		super(url, key);
	}

	/**
	 * Returns the file.
	 * @return the file
	 */
	public URL getURL() {
		return getKey(0);
	}

	/**
	 * Sets the URL.
	 * @param url the URL to set
	 */
	public void setURL(URL url) {
		setKey(0, url);
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
