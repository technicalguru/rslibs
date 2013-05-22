/**
 * 
 */
package rs.data.hibernate.type.crypto;

import java.io.UnsupportedEncodingException;
import java.util.TimeZone;

import rsbaselib.io.ConverterUtils;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class TimezoneEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public TimezoneEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return TimeZone.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) throws UnsupportedEncodingException {
		return ConverterUtils.toTimeZone(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) throws UnsupportedEncodingException {
		return ConverterUtils.toBytes((TimeZone)value);
	}

}
