/**
 * 
 */
package rs.data.hibernate.type.crypto;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import rs.baselib.io.ConverterUtils;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class UrlEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public UrlEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return URL.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) throws UnsupportedEncodingException, MalformedURLException {
		return ConverterUtils.toURL(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) throws UnsupportedEncodingException {
		return ConverterUtils.toBytes((URL)value);
	}

}
