/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rs.baselib.io.ConverterUtils;
import rs.baselib.util.RsYear;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class RsYearEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public RsYearEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return RsYear.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toRsYear(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((RsYear)value);
	}

}
