/**
 * 
 */
package rs.data.hibernate.type.crypto;

import rsbaselib.io.ConverterUtils;
import rsbaselib.util.CommonUtils;

/**
 * Handles Float encryption.
 * @author ralph
 *
 */
public class CharEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public CharEncryptedType() {
		super(Character.class, Character.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		try {
			return ConverterUtils.toCharObject(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert to character: "+CommonUtils.toString(bytes), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		try {
			return ConverterUtils.toBytes((Character)value);
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert string: "+value, e);
		}
	}

}
