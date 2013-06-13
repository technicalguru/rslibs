/**
 * 
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
	 */
	public Manifest getManifest() throws IOException {
		return jarFile.getManifest();
	}
	
	/**
	 * Closes the file.
	 * @throws IOException
	 */
	public void close() throws IOException {
		jarFile.close();
	}
	
	/**
	 * Returns the URL prefix for the JAR.
	 * @return URL prefix
	 */
	public String getUrlPrefix() throws IOException {
		return "jar:file:"+file.getCanonicalPath()+"!/";
	}
}
