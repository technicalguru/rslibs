/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rsbaselib.io.ConverterUtils;

/**
 * Handles Short encryption.
 * @author ralph
 *
 */
public class ShortEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public ShortEncryptedType() {
		super(Short.class, Short.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toShortObject(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Short)value);
	}

}
