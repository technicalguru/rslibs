/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;
import rs.baselib.util.RsDay;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class RsDayEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public RsDayEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return RsDay.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toRsDay(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((RsDay)value);
	}

}
