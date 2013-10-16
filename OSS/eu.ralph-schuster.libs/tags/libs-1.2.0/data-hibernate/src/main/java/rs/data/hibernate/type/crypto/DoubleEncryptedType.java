/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;

/**
 * Handles Double encryption.
 * @author ralph
 *
 */
public class DoubleEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public DoubleEncryptedType() {
		super(Double.class, Double.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toDoubleObject(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Double)value);
	}

}
