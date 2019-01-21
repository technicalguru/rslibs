/*
 * This file is part of RS Library (Base Library).
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
package rs.baselib.lang;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Describes a JAR file
 * @author ralph
 *
 */
public class JarDescriptor {

	private JarFile jarFile;
	private File file;
	
	/**
	 * Constructor.
	 * @param file the jar file
	 * @throws IOException when Jar cannot be opened
	 */
	public JarDescriptor(File file) throws IOException {
		setFile(file);
		setJarFile(new JarFile(file));
	}

	/**
	 * Returns the jarFile.
	 * @return the jarFile
	 */
	public JarFile getJarFile() {
		return jarFile;
	}

	/**
	 * Sets the jarFile.
	 * @param jarFile the jarFile to set
	 */
	protected void setJarFile(JarFile jarFile) {
		this.jarFile = jarFile;
	}

	/**
	 * Returns the file.
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the file.
	 * @param file the file to set
	 */
	protected void setFile(File file) {
		this.file = file;
	}

	/**
	 * Returns the manifest.
	 * @return the manifest.
	 * @throws IOException when Manifest file cannot be opened
	 */
	public Manifest getManifest() throws IOException {
		return jarFile.getManifest();
	}
	
	/**
	 * Closes the file.
	 * @throws IOException when file cannot be closed
	 */
	public void close() throws IOException {
		jarFile.close();
	}
	
	/**
	 * Returns the URL prefix for the JAR.
	 * @return URL prefix
	 * @throws IOException when file cannot be read
	 */
	public String getUrlPrefix() throws IOException {
		return "jar:file:"+file.getCanonicalPath()+"!/";
	}
}
