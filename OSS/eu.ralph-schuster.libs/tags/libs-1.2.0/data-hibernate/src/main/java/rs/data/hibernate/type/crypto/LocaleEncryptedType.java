/**
 * 
 */
package rs.data.hibernate.type.crypto;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class LocaleEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public LocaleEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return Locale.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) throws UnsupportedEncodingException {
		//return ConverterUtils.toLocale(bytes);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) throws UnsupportedEncodingException {
		//return ConverterUtils.toBytes((Locale)value);
		return null;
	}

}
