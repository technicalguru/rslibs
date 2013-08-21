/**
 * 
 */
package rs.data.file.bo;

import java.io.IOException;
import java.io.Serializable;

/**
 * Stores the data in a properties file.
 * @author ralph
 *
 */
public abstract class AbstractPropertiesFileBO<K extends Serializable> extends AbstractHashMapFileBO<K> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public AbstractPropertiesFileBO() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load() throws IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void persist() throws IOException {
		// TODO Auto-generated method stub
		
	}

	
}
