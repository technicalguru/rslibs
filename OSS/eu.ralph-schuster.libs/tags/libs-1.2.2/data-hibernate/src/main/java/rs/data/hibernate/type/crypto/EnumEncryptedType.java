/**
 * 
 */
package rs.data.hibernate.type.crypto;

import java.util.Properties;

import org.hibernate.MappingException;

import rs.baselib.io.ConverterUtils;
import rs.baselib.lang.LangUtils;

/**
 * Reads enumerations.
 * @author ralph
 *
 */
public class EnumEncryptedType extends AbstractEncryptionType {

	/**
	 * The class implementing the enumeration
	 */
	@SuppressWarnings("rawtypes")
	private Class<Enum> clazz = null; 

	/**
	 * Constructor
	 */
	public EnumEncryptedType() { 
	} 

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Class<?> returnedClass() { 
		return clazz; 
	} 

	/**
	 * 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object convert(byte[] bytes) throws Exception {
		return Enum.valueOf(clazz, ConverterUtils.toString(bytes));
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) throws Exception {
		return ConverterUtils.toBytes(((Enum<?>)value).name());
	}

	/**
	 * Sets the default value from HBM config parameters.
	 * @param parameters
	 */
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public void setParameterValues(Properties parameters) {
		 super.setParameterValues(parameters);
		 String className = (String)parameters.get("class");
		 if (className == null) {
			 throw new MappingException("class parameter not specified");
		 }

		 try {
			 clazz = (Class<Enum>)LangUtils.forName(className);
		 } catch (ClassNotFoundException e) {
			 throw new MappingException("Cannot find class: "+className, e);
		 }
	 }

}
