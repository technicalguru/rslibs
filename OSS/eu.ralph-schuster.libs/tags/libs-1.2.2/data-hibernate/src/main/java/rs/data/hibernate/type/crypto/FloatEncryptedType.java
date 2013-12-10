/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;

/**
 * Handles Float encryption.
 * @author ralph
 *
 */
public class FloatEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public FloatEncryptedType() {
		super(Float.class, Float.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toFloatObject(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Float)value);
	}

}
