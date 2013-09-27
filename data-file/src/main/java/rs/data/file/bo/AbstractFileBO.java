/**
 * 
 */
package rs.data.file.bo;

import java.io.File;
import java.io.Serializable;

import rs.data.impl.bo.AbstractHashMapBO;

/**
 * A Business Object with a File underneath.
 * @author ralph
 *
 */
public abstract class AbstractFileBO<K extends Serializable> extends AbstractHashMapBO<K> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;
	/** The file that we are connected with */
	private File file;
	
	/**
	 * Constructor.
	 */
	public AbstractFileBO() {
	}

	/**
	 * Returns the {@link #file}.
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the {@link #file}.
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
