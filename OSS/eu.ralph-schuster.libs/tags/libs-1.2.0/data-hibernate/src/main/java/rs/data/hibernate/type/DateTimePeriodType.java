/**
 * 
 */
package rs.data.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import rs.baselib.util.DateTimePeriod;
import rs.baselib.util.RsDate;

/**
 * Reads {@link DateTimePeriod}) objects.
 * @author ralph
 *
 */
public class DateTimePeriodType implements UserType {

	/** The database data types */
	private static final int SQL_TYPES[] = { Types.TIMESTAMP, Types.TIMESTAMP };
	
	/**
	 * Default Constructor.
	 */
	public DateTimePeriodType() {
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
    	return DateTimePeriod.class;
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
    	if (value == null) return null;
    	DateTimePeriod src = (DateTimePeriod)value;
    	return src.deepCopy();
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
    public Object nullSafeGet(ResultSet rs, String names[], SessionImplementor session, Object owner) throws HibernateException, SQLException {
    	RsDate from = RsDate.get(rs.getTimestamp(names[0]));
    	RsDate until = RsDate.get(rs.getTimestamp(names[1]));
    	return new DateTimePeriod(from, until);
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    	if (value != null) {
    		DateTimePeriod period = (DateTimePeriod)value;
    		RsDate from = period.getFrom();
        	st.setTimestamp(index, from != null ? new Timestamp(from.getTimeInMillis()) : null);
    		RsDate until = period.getUntil();
        	st.setTimestamp(index+1, until != null ? new Timestamp(until.getTimeInMillis()) : null);
    	} else {
    		st.setNull(index, Types.TIMESTAMP);
    		st.setNull(index+1, Types.TIMESTAMP);
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
		return ((DateTimePeriod)component).hashCode();
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}


}
