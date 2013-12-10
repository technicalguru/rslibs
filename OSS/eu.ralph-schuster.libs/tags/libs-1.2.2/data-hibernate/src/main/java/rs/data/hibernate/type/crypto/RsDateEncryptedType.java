/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;
import rs.baselib.util.RsDate;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class RsDateEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public RsDateEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return RsDate.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toRsDate(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((RsDate)value);
	}

}
