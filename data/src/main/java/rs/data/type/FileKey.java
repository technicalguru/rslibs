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

import java.io.File;
import java.io.Serializable;

/**
 * A key based on a file.
 * @param <K> the type of key that is combines with the File
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
