/**
 * 
 */
package rs.data.hibernate.type.crypto;

import java.sql.Timestamp;

import rs.baselib.io.ConverterUtils;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class TimestampEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public TimestampEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return Timestamp.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		return ConverterUtils.toTimestamp(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		return ConverterUtils.toBytes((Timestamp)value);
	}

}
