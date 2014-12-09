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
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Load a long array from an comma-separated string column.
 * 
 * @author ralph
 * @since 1.2.9
 *
 */
public class LongArrayType implements UserType, ParameterizedType {

	private static Logger logger1 = LoggerFactory.getLogger(BasicBinder.class);
	private static Logger logger2 = LoggerFactory.getLogger(BasicExtractor.class);

	/** The database data types */
	private static final int SQL_TYPES[] = { Types.VARCHAR };

	public static final long NULL_ARRAY[] = new long[0];
	
	private String splitChar = ",";

	/**
	 * Constructor.
	 */
	public LongArrayType() {
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
		return Long[].class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object x, Object y) {
		return ArrayUtils.isEquals(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object deepCopy(Object value) {
		if (value == null) return null;
		return ArrayUtils.clone((long[])value);
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
		String s = rs.getString(names[0]);
		if ((s == null) || s.isEmpty()) {
			if (logger2.isTraceEnabled()) logger2.trace("found [null] as column ["+names[0]+"]");
			return NULL_ARRAY;
		}
		String l[] = s.split(getSplitChar());
		if (logger2.isTraceEnabled()) logger2.trace("found ["+l.toString()+"] as column ["+names[0]+"]");
		long rc[] = new long[l.length];
		for (int i=0; i<rc.length; i++) {
			try {
				rc[i] = Long.parseLong(l[i]);
			} catch (Exception e) {
				
			}
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		long a[] = (long[])value;
		if ((a == null) || (a.length == 0)) {
			st.setString(index, null);
			if (logger1.isTraceEnabled()) logger1.trace("binding parameter ["+index+"] as [STRING] - null");
		} else {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<a.length; i++) {
				if (i>0) s.append(getSplitChar());
				s.append(""+a[i]);
			}
			st.setString(index, s.toString());
			if (logger1.isTraceEnabled()) logger1.trace("binding parameter ["+index+"] as [STRING] - "+s.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return deepCopy(cached);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) deepCopy(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(Object component) throws HibernateException {
		return component.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}

	/**
	 * Returns the split character.
	 * @return the split character
	 */
	public String getSplitChar() {
		return splitChar;
	}

	/**
	 * Sets the split character.
	 * @param splitChar the split character to set
	 */
	public void setSplitChar(String splitChar) {
		this.splitChar = splitChar != null ? splitChar : ",";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameterValues(Properties parameters) {
		if (parameters != null) {
			setSplitChar(parameters.getProperty("splitChar"));
		}
	}


}
