/**
 * 
 */
package rs.data.file.bo;

import java.io.Serializable;

/**
 * Stores the data in an XML file.
 * @author ralph
 *
 */
public abstract class AbstractXmlFileBO<K extends Serializable> extends AbstractHashMapFileBO<K> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public AbstractXmlFileBO() {
	}

}
