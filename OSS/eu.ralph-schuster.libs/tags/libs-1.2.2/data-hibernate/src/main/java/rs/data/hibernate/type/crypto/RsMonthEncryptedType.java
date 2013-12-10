/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;
import rs.baselib.util.RsMonth;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class RsMonthEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public RsMonthEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return RsMonth.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toRsMonth(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((RsMonth)value);
	}

}
