/**
 * 
 */
package rs.data.impl.bo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import rs.data.impl.dto.GeneralDTO;

/**
 * A Business Object with a File underneath.
 * @author ralph
 *
 */
public abstract class AbstractFileBO<K extends Serializable,T extends GeneralDTO<K>> extends AbstractBO<K,T> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;
	/** The file that we are connected with */
	private File file;
	
	/**
	 * Constructor.
	 */
	public AbstractFileBO() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param transferObject
	 */
	public AbstractFileBO(T transferObject) {
		super(transferObject);
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
