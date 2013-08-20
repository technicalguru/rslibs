/**
 * 
 */
package rs.data.file.bo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import rs.data.impl.bo.AbstractGeneralBO;

/**
 * A Business Object with a File underneath.
 * @author ralph
 *
 */
public abstract class AbstractFileBO<K extends Serializable> extends AbstractGeneralBO<K> {

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

	/**
	 * Sets the id.
	 * @param id id of object
	 */
	public abstract void setId(K id);
	
	/**
	 * Loads the data.
	 */
	protected abstract void load() throws IOException;
	
	/**
	 * Persists the data.
	 * @param file file object to persist to
	 */
	protected abstract void persist() throws IOException;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh() {
		try {
			beginTx();
			load();
			commitTx();
		} catch (IOException e) {
			rollbackTx();
			throw new RuntimeException("Cannot refresh from file", e);
		}
	}
	
	
}
