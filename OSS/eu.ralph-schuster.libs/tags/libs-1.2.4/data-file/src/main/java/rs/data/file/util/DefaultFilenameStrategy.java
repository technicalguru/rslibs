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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of a filename strategy.
 * @author ralph
 *
 */
public class DefaultFilenameStrategy<K extends Serializable> implements IFilenameStrategy<K> {

	/** The parent directory of a file */
	private File parentDir;
	/** The prefix of a filename */
	private String prefix;
	/** The suffix of a filename */
	private String suffix;

	/**
	 * Constructor.
	 * Data will be stored in current working directory, sub-directory data with suffix ".data".
	 */
	public DefaultFilenameStrategy() {
		this(new File(System.getProperty("user.dir"), DEFAULT_DATA_DIR), null, DEFAULT_DATA_SUFFIX);
	}

	/**
	 * Constructor.
	 */
	public DefaultFilenameStrategy(String parentDir, String prefix, String suffix) {
		this(new File(parentDir), prefix, suffix);
	}

	/**
	 * Constructor.
	 */
	public DefaultFilenameStrategy(File parentDir, String prefix, String suffix) {
		setParentDir(parentDir);
		setPrefix(prefix);
		setSuffix(suffix);
	}

	/**
	 * Returns the {@link #parentDir}.
	 * @return the parentDir
	 */
	public File getParentDir() {
		return parentDir;
	}

	/**
	 * Sets the {@link #parentDir}.
	 * @param parentDir the parentDir to set
	 */
	public void setParentDir(File parentDir) {
		this.parentDir = parentDir;
	}

	/**
	 * Returns the {@link #prefix}.
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the {@link #prefix}.
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Returns the {@link #suffix}.
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the {@link #suffix}.
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFile(K key) {
		File parentDir = getParentDir();
		if (!parentDir.exists()) {
			createParent(parentDir);
		}
		String filename = "";
		String prefix = getPrefix();
		if (prefix != null) filename += prefix;
		filename += key.toString();
		String suffix = getSuffix(); 
		if (suffix != null) filename += suffix;
		return new File(parentDir, filename);
	}

	/**
	 * Called when a new file is required to be created.
	 * Creates the parent dir.
	 * @param dir the directory to be created
	 */
	protected void createParent(File dir) {
		dir.mkdirs();
	}

	/**
	 * Returns whether the given file matches the strategy.
	 * @param file file to be checked
	 * @return <code>true</code> when strategy matches
	 */
	public boolean matches(File file) {
		if (!file.getParentFile().equals(getParentDir())) return false;
		return matchesFilename(file.getName());
	}

	/**
	 * Returns true when the given filename (last path component!) matches this strategy.
	 * @param filename filename to be matched
	 * @return <code>true</code> when strategy matches
	 */
	public boolean matchesFilename(String filename) {
		String prefix = getPrefix();
		if (prefix != null) {
			if (!filename.startsWith(prefix)) return false;
		}
		String suffix = getSuffix(); 
		if (suffix != null) {
			if (!filename.endsWith(suffix)) return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<File> getFiles() {
		List<File> rc = new ArrayList<File>();
		if (getParentDir().exists() && getParentDir().isDirectory()) { 
			for (File child : getParentDir().listFiles()) {
				if (matchesFilename(child.getName()) && child.canRead()) rc.add(child);
			}
			Collections.sort(rc);
		}
		return rc;
	}


}
