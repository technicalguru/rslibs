/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;
import rs.baselib.util.CommonUtils;

/**
 * Handles Float encryption.
 * @author ralph
 *
 */
public class StringEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public StringEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return String.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		try {
			return ConverterUtils.toString(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert to string: "+CommonUtils.toString(bytes), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		try {
			return ConverterUtils.toBytes((String)value);
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert string: "+value, e);
		}
	}

}
