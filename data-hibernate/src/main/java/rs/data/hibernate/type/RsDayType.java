/*
 * This file is part of RS Library (Data Hibernate Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.data.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import rs.baselib.util.RsDay;

/**
 * Loads RsDay.
 * @author ralph
 *
 */
public class RsDayType implements UserType {
	
	/** The database data types */
	private static final int SQL_TYPES[] = { Types.VARCHAR };

	/**
	 * Constructor.
	 */
	public RsDayType() {
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
		return RsDay.class;
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
    	RsDay rc = new RsDay();
    	rc.setTimeInMillis(((RsDay)value).getTimeInMillis());
    	rc.setTimeZone(((RsDay)value).getTimeZone());
    	return rc;
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
    	String key = rs.getString(names[0]);
    	if (key == null) return null;
    	return RsDay.getDay(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    	if (value == null) {
    		st.setString(index, null);
    	} else {
    		st.setString(index, ((RsDay)value).getKey());
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
		return ((RsDay)component).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}

}
