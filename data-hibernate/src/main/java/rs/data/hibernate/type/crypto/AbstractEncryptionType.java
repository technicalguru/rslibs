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
package rs.data.hibernate.type.crypto;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.crypto.DefaultCryptingDelegateFactory;
import rs.baselib.crypto.ICryptingDelegate;
import rs.baselib.crypto.ICryptingDelegateFactory;
import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;

/**
 * Base class for encrypting data.
 * @author ralph
 *
 */
public abstract class AbstractEncryptionType implements UserType, ParameterizedType {

	private static Logger logger1 = LoggerFactory.getLogger(BasicBinder.class);
	private static Logger logger2 = LoggerFactory.getLogger(BasicExtractor.class);
	
	private static final int TYPES[] = new int[] { Types.VARBINARY };
	private ICryptingDelegateFactory cryptingDelegateFactory;

	/**
	 * Constructor.
	 */
	public AbstractEncryptionType() {
	}

	/**
	 * Returns the factory for the crypting delegate.
	 * @return factory
	 */
	public ICryptingDelegateFactory getCryptingDelegateFactory() {
		return cryptingDelegateFactory;
	}
	
	/**
	 * Sets the crypting delegate factory.
	 * @param factory factory
	 */
	public void setCryptingDelegateFactory(ICryptingDelegateFactory factory) {
		cryptingDelegateFactory = factory;
	}
	
	/**
	 * Returns the crypting delegate for this type.
	 * @return the crypting delegate
	 */
	protected ICryptingDelegate getCryptingDelegate() {
		return getCryptingDelegateFactory().getCryptingDelegate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] sqlTypes() {
		return TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x != null) return x.equals(y);
		if (y != null) return y.equals(x);
		return true; // bot are null
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(Object x) throws HibernateException {
		if (x != null) return x.hashCode();
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
		try {
			byte encrypted[] = rs.getBytes(names[0]);
			if (encrypted == null) {
				if (logger2.isTraceEnabled()) logger2.trace("found [null] as column ["+names[0]+"]");
				return null;
			}
			byte decrypted[] = decrypt(encrypted);
			Object rc = convert(decrypted);
			if (logger2.isTraceEnabled()) logger2.trace("found ["+rc+"] as column ["+names[0]+"] - ["+CommonUtils.toString(decrypted)+"] - ["+CommonUtils.toString(encrypted)+"]");
			return rc;
		} catch (SQLException e) {
			throw e;
		} catch (HibernateException e) {
			throw e;
		} catch (Exception e) {
			throw new HibernateException("Cannot read: "+CommonUtils.join(", ", names), e);
		}
	}

	/**
	 * Decrypts the given bytes and returns the original bytes.
	 * @param encrypted encrypted byte array
	 * @return decrypted bytes
	 */
	protected byte[] decrypt(byte encrypted[]) {
		try {
			return getCryptingDelegate().decrypt(encrypted);
		} catch (Exception e) {
			throw new HibernateException("Cannot decrypt bytes: "+CommonUtils.toString(encrypted), e);
		}
	}
	
	/**
	 * Subclasses must return the correct object from the byte representation. 
	 * @param bytes byte representation of object
	 * @return object
	 * @throws Exception - any exception that occurs while conversion
	 */
	protected abstract Object convert(byte bytes[]) throws Exception;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
		try {
			if (value != null) {
				byte decrypted[] = convert(value);
				byte encrypted[] = null;
				if (decrypted != null) encrypted = encrypt(decrypted);
	    		if (logger1.isTraceEnabled()) logger1.trace("binding parameter ["+index+"] as ["+returnedClass().getSimpleName()+"] - "+value+" - ["+CommonUtils.toString(decrypted)+"] - ["+CommonUtils.toString(encrypted)+"]");
				st.setBytes(index, encrypted);
			} else {
	    		if (logger1.isTraceEnabled()) logger1.trace("binding parameter ["+index+"] as ["+returnedClass().getSimpleName()+"] - null");
				st.setBytes(index, null);
			}
		} catch (SQLException e) {
			throw e;
		} catch (HibernateException e) {
			throw e;
		} catch (Exception e) {
			throw new HibernateException("Cannot write value: "+value, e);
		}
	}

	/**
	 * Encrypts the given bytes.
	 * @param bytes bytes to encrypt
	 * @return encrypted bytes
	 */
	protected byte[] encrypt(byte bytes[]) {
		try {
			return getCryptingDelegate().encrypt(bytes);
		} catch (Exception e) {
			throw new HibernateException("Cannot encrypt bytes: "+CommonUtils.toString(bytes), e);
		}
	}
	
	/**
	 * Subclasses must return the byte representation of the value.
	 * @param value value object (can be null)
	 * @return byte representation
	 * @throws Exception - any exception that occurs while conversion
	 */
	protected abstract byte[] convert(Object value) throws Exception;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object deepCopy(Object value) throws HibernateException {
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
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable)deepCopy(value);
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
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameterValues(Properties parameters) {
		String factoryClassName = null;
		if (parameters != null) factoryClassName = parameters.getProperty("cryptingDelegateFactoryClass");
		if (factoryClassName == null) factoryClassName = DefaultCryptingDelegateFactory.class.getName();
		try {
			Class<?> factoryClass = LangUtils.forName(factoryClassName);
			Method m = factoryClass.getMethod("getInstance");
			if ((m.getModifiers() & Modifier.STATIC) != 0) {
				setCryptingDelegateFactory((ICryptingDelegateFactory)m.invoke(null));
			} else {
				setCryptingDelegateFactory(DefaultCryptingDelegateFactory.INSTANCE);
			}
		} catch (Exception e) {
			throw new HibernateException("Cannot instantiate crypting delegate factory: "+factoryClassName, e);
		}
	}

	
}
