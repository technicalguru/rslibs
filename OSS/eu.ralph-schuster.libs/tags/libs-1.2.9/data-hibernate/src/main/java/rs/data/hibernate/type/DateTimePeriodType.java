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
import java.sql.Timestamp;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import rs.baselib.util.DateTimePeriod;
import rs.baselib.util.RsDate;

/**
 * Reads {@link DateTimePeriod}) objects.
 * @author ralph
 *
 */
public class DateTimePeriodType implements CompositeUserType {

	/**
	 * Default Constructor.
	 */
	public DateTimePeriodType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getPropertyNames() {
        // ORDER IS IMPORTANT!  it must match the order the columns are defined in the property mapping
        return new String[] { "from", "until" };
    }
	
    /**
     * {@inheritDoc}
     */
	@Override
	public Type[] getPropertyTypes() {
        return new Type[] { TimestampType.INSTANCE, TimestampType.INSTANCE };
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
	public Object getPropertyValue(Object component, int propertyIndex) {
        if (component == null) return null;

        DateTimePeriod period = (DateTimePeriod) component;
        switch (propertyIndex) {
            case 0: return period.getFrom();
            case 1: return period.getUntil();
            default:
                throw new HibernateException( "Invalid property index [" + propertyIndex + "]" );
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void setPropertyValue(Object component, int propertyIndex, Object value) throws HibernateException {
        if (component == null) return;

        DateTimePeriod period = (DateTimePeriod) component;
        switch (propertyIndex) {
            case 0:
                period.setFrom((RsDate)value);
                break;
            case 1:
                period.setUntil((RsDate)value);
                break;
            default:
                throw new HibernateException( "Invalid property index [" + propertyIndex + "]" );
        }
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
	public int hashCode(Object component) throws HibernateException {
		return ((DateTimePeriod)component).hashCode();
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) return true;
        if (x == null || y == null) return false;
        return x.equals(y);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Object deepCopy(Object value) throws HibernateException {
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
	public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
    	return (Serializable) value;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
    	return cached;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
		return deepCopy(original);
	}

}
