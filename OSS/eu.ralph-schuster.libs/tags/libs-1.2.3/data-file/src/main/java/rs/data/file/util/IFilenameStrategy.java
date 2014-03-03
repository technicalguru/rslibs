/*
 * This file is part of RS Library (Data File Library).
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
package rs.data.file.util;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

/**
 * Interface that defines a filename strategy for deriving filenames from keys.
 * @param <K> type of ID this strategy converts to file names
 * @author ralph
 *
 */
public interface IFilenameStrategy<K extends Serializable> {

	/** The default data directory */
	public static final String DEFAULT_DATA_DIR = "data";	
	/** The default suffix */
	public static final String DEFAULT_DATA_SUFFIX = ".data";

	/**
	 * Compute the filename from the key.
	 * @param key key
	 * @return filename for key
	 */
	public File getFile(K key);
	
	/**
	 * Returns all existing files that the strategy knows about.
	 * @return all existing files
	 */
	public Collection<File> getFiles();
}
