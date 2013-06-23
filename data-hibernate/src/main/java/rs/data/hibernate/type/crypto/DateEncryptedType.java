/**
 * 
 */
package rs.data.hibernate.type.crypto;

import java.util.Date;

import rsbaselib.io.ConverterUtils;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class DateEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public DateEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return Date.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toDate(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Date)value);
	}

}
