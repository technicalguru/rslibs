/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;

/**
 * Handles Byte encryption.
 * @author ralph
 *
 */
public class ByteEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public ByteEncryptedType() {
		super(Byte.class, Byte.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toByteObject(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Byte)value);
	}

}
