/**
 * 
 */
package rs.data.file.storage;

import java.io.IOException;

import rs.baselib.lang.LangUtils;

/**
 * Abstract implementation of a storage stratey.
 * @author ralph
 *
 */
public abstract class AbstractStorageStrategy<S> implements IStorageStrategy<S> {

	/**
	 * Constructor.
	 */
	public AbstractStorageStrategy() {
	}

	/**
	 * Serializes the given value.
	 * @param value value to be serialized
	 * @return the serialized string
	 */
	protected String serialize(Object value) throws IOException {
		return LangUtils.serializeBase64(value);
	}
	
	/**
	 * Unserializes the given value.
	 * @param value the serialized string
	 * @return the object (or null)
	 */
	protected Object unserialize(String value) throws ClassNotFoundException, IOException {
		return LangUtils.unserialize(value);
	}
}
