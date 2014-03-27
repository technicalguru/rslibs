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

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.lang.LangUtils;

/**
 * Load boolean from an INT column.
 * 
 * @author ralph
 *
 */
public class BooleanIntType implements UserType, ParameterizedType {

	private static Logger logger1 = LoggerFactory.getLogger(BasicBinder.class);
	private static Logger logger2 = LoggerFactory.getLogger(BasicExtractor.class);

	/** The database data types */
	private static final int SQL_TYPES[] = { Types.INTEGER };

	private boolean nullable = false;

	/**
	 * Constructor.
	 */
	public BooleanIntType() {
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
		return Boolean.TYPE;
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
		return ((Boolean)value).booleanValue() ? Boolean.TRUE : Boolean.FALSE;
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
		Integer i = rs.getInt(names[0]);
		if (i == null) {
			if (logger2.isTraceEnabled()) logger2.trace("found [null] as column ["+names[0]+"]");
			return null;
		}
		if (logger2.isTraceEnabled()) logger2.trace("found ["+i+"] as column ["+names[0]+"]");
		return i.intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		if ((value == null) && isNullable()) {
			st.setObject(index, null, Types.INTEGER);
			if (logger1.isTraceEnabled()) logger1.trace("binding parameter ["+index+"] as [INT] - null");
		} else {
			boolean b = LangUtils.getBoolean(value);
			st.setInt(index, b ? 1 : 0);
			if (logger1.isTraceEnabled()) logger1.trace("binding parameter ["+index+"] as [INT] - "+(b ? 1 : 0));
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
		return ((Date)component).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}

	/**
	 * Returns whether <code>null</code> can be set in the column.
	 * @return true or false
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * Sets the nullable.
	 * @param nullable the nullable to set
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameterValues(Properties parameters) {
		if (parameters != null) {
			setNullable(LangUtils.getBoolean(parameters.getProperty("nullable")));
		}
	}


}
