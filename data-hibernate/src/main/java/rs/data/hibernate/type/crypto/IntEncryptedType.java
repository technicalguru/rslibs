/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rsbaselib.io.ConverterUtils;

/**
 * Handles Integer encryption.
 * @author ralph
 *
 */
public class IntEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public IntEncryptedType() {
		super(Integer.class, Integer.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toIntObject(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Integer)value);
	}

}
