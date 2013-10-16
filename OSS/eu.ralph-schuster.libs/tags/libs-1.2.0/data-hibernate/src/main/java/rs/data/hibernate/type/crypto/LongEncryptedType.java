/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class LongEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public LongEncryptedType() {
		super(Long.class, Long.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toLongObject(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Long)value);
	}

}
