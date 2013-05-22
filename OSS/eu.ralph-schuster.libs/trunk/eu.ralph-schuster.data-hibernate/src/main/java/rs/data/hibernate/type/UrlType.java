/**
 * 
 */
package rs.data.hibernate.type;

import java.io.Serializable;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Loads URLs.
 * @author ralph
 *
 */
public class UrlType implements UserType {

	/** The database data types */
	private static final int SQL_TYPES[] = { Types.VARCHAR };

	/**
	 * Constructor.
	 */
	public UrlType() {
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
		return URL.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean equals(Object x, Object y) {
        if (x == y) return true;
        if (x == null || y == null) return false;
        return x.equals(y);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public Object deepCopy(Object value) {
    	try {
    		if (value == null) return null;
    		URL rc = new URL(((URL)value).toExternalForm());
    		return rc;
    	} catch (Exception e) {
    		throw new HibernateException("Cannot deep copy URL: "+value, e);
    	}
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public boolean isMutable() {
    	return true;
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("deprecation")
    public Object nullSafeGet(ResultSet rs, String names[], Object owner) throws HibernateException, SQLException {
    	String url = rs.getString(names[0]);
    	if (url == null) return null;
    	try {
    		return new URL(url);
    	} catch (Exception e) {
    		throw new HibernateException("Cannot convert URL: "+url, e);
    	}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("deprecation")
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
    	if (value == null) {
    		st.setString(index, null);
    	} else {
    		st.setString(index, ((URL)value).toExternalForm());
    	}
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
    	return (Serializable) value;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(Object component) throws HibernateException {
		return ((URL)component).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}
}
