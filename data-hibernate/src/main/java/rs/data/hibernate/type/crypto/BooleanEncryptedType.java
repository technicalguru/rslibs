/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rsbaselib.io.ConverterUtils;

/**
 * Handles Boolean encryption.
 * @author ralph
 *
 */
public class BooleanEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public BooleanEncryptedType() {
		super(Boolean.class, Boolean.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toBooleanObject(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Boolean)value);
	}

}
