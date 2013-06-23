/**
 * 
 */
package rs.data.hibernate.type.crypto;

import java.util.Properties;

/**
 * Handles automatically object and simple language types.
 * @author ralph
 *
 */
public abstract class AbstractLangEncryptionType extends AbstractEncryptionType {

	private Class<?> objectType;
	private Class<?> simpleType;
	private boolean simple;
	
	/**
	 * Constructor.
	 */
	public AbstractLangEncryptionType(Class<?> objectType, Class<?> simpleType) {
		this.objectType = objectType;
		this.simpleType = simpleType;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return simple ? simpleType : objectType;
	}

	/**
	 * Returns true if this is a simple type mapper.
	 * @return true or false
	 */
	protected boolean isSimple() {
		return simple;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameterValues(Properties parameters) {
		super.setParameterValues(parameters);
		String s = null;
		if (parameters != null) parameters.getProperty("simple");
		if (s == null) s = "true";
		simple = Boolean.parseBoolean(s);
	}
}
