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
	 * @param url the URL of the object
	 * @param key the key of the object
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
