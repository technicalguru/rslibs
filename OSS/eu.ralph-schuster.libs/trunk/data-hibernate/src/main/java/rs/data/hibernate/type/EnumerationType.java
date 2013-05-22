/**
 * 
 */
package rs.data.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * Reads enumerations.
 * @author ralph
 *
 */
public class EnumerationType implements UserType, ParameterizedType {

	private static final int[] SQL_TYPES = {Types.VARCHAR}; 

	/**
	 * The class implementing the enumeration
	 */
	@SuppressWarnings("rawtypes")
	private Class<Enum> clazz = null; 

	/**
	 * Constructor
	 */
	public EnumerationType() { 
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] sqlTypes() { 
		return SQL_TYPES; 
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() { 
		return clazz; 
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException { 
		String name = resultSet.getString(names[0]);

		Enum<?> result = null; 
		if (!resultSet.wasNull()) {
			name = name.trim();

			// This is the name of the enum
			if (name.length() > 0) {
				result = Enum.valueOf(clazz, name); 
			}
		} 
		return result; 
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException { 
		if (null == value) { 
			preparedStatement.setNull(index, Types.VARCHAR);
		} else { 
			preparedStatement.setString(index, ((Enum<?>)value).name());
		} 
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object deepCopy(Object value) throws HibernateException{ 
		return value; 
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMutable() { 
		return false; 
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Serializable disassemble(Object value) throws HibernateException { 
		return (Serializable)value; 
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException { 
		return original; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(Object x) throws HibernateException { 
		return x.hashCode(); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object x, Object y) throws HibernateException { 
		if (x == y) return true; 
		if ((null == x) || (null == y)) return false; 
		return x.equals(y); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setParameterValues(Properties parameters) {
		String className = (String)parameters.get("class");
		if (className == null) {
			throw new MappingException("class parameter not specified");
		}

		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			if (loader != null) {
				clazz = (Class<Enum>)loader.loadClass(className);
			}
			if (clazz == null) {
				clazz = (Class<Enum>)Class.forName(className);
			}
		} catch (ClassNotFoundException e) {
			throw new MappingException("Cannot find class: "+className, e);
		}
	}

}
