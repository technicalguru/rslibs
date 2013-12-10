/**
 * 
 */
package rs.data.file.storage;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;

import rs.baselib.bean.BeanSupport;
import rs.baselib.lang.LangUtils;
import rs.data.api.IDaoFactory;
import rs.data.api.bo.IGeneralBO;

/**
 * Abstract implementation of a storage stratey.
 * @param <K> type of ID for Business Objects to be managed
 * @param <T> type of Business Object the strategy manages
 * @param <S> type of specifier, e.g. a file
 * @author ralph
 *
 */
public abstract class AbstractStorageStrategy<K extends Serializable, T extends IGeneralBO<K>, S> implements IStorageStrategy<K, T, S> {

	/** ICache of transient properties per class */
	private static Map<Class<?>, Set<String>> transientCache = new HashMap<Class<?>, Set<String>>();

	/** The encoding used */
	private String encoding = Charset.defaultCharset().name();
	
	/** A factory reference */
	private IDaoFactory daoFactory;
	
	/**
	 * Constructor.
	 */
	public AbstractStorageStrategy(IDaoFactory daoFactory) {
		setDaoFactory(daoFactory);
	}

	/**
	 * Returns the daoFactory.
	 * @return the daoFactory
	 */
	public IDaoFactory getDaoFactory() {
		return daoFactory;
	}

	/**
	 * Sets the daoFactory.
	 * @param daoFactory the daoFactory to set
	 */
	public void setDaoFactory(IDaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	/**
	 * {@inheritDoc}
	 * <p>The default implementation returns the number of specifiers.</p> 
	 */
	@Override
	public int getObjectCount(Collection<S> specifiers) throws IOException {
		return specifiers.size();
	}

	/**
	 * {@inheritDoc}
	 * <p>The default implementation forwards to {@link #getObjectCount(Collection)}.</p> 
	 */
	@Override
	public int getDefaultObjectCount(Collection<S> specifiers) throws IOException {
		return getObjectCount(specifiers);
	}

	/**
	 * {@inheritDoc}
	 * <p>The default implementation forwards to {@link #getList(Collection)}.</p> 
	 */
	@Override
	public Map<K, S> getDefaultList(Collection<S> specifiers) throws IOException {
		return getList(specifiers);
	}

	/**
	 * Serializes the given value.
	 * Collections, Maps and other non-serializable objects cannot be serialized by this method.
	 * @param value value to be serialized
	 * @return the serialized string or null if it cannot be serialized safely. Values encoded in BASE64 are
	 *    prefixed with <code>"BASE64"</code>.
	 */
	protected String serialize(Object value) throws IOException {
		if (value instanceof Number) {
			return value.toString();
		} else if (value instanceof Boolean) {
			return value.toString();
		} else if (value instanceof Enum) {
			return value.toString();
		} else if (value instanceof String) {
			// encode only when out of sight characters
			boolean isClean = true;
			for (char c : ((String) value).toCharArray()) {
				if (c > '\u007f') {
					isClean = false;
					break;
				}
			}
			if (isClean) {
				return value.toString();
			}
			return "BASE64:"+new String(Base64.encodeBase64(((String) value).getBytes(getEncoding())));
		} else if (value instanceof Serializable) {
			return "BASE64:"+LangUtils.serializeBase64(value);
		}
		return null;
	}
	
	/**
	 * Unserializes the given value.
	 * Collections, Maps and other non-serializable objects cannot be unserialized by this method.
	 * @param className the expected class to be produced
	 * @param value the serialized string. Values encoded in BASE64 must be prefixed with <code>"BASE64"</code>.
	 * @return the object (or null)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object unserialize(String className, String value) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> clazz = LangUtils.forName(className);
		if (Number.class.isAssignableFrom(clazz)) {
			return (Number)clazz.getConstructor(String.class).newInstance(value);
		} else if (Boolean.class.isAssignableFrom(clazz) ) {
			return Boolean.valueOf(value);
		} else if (clazz.isEnum()) {
			return Enum.valueOf((Class<Enum>)clazz, value);
		} else if (String.class.isAssignableFrom(clazz)) {
			if (value.startsWith("BASE64:")) return Base64.decodeBase64(value.substring(7));
			return value;
		} else if (Serializable.class.isAssignableFrom(clazz)) {
			return LangUtils.unserialize(value);
		}
		return null;
	}
	
	/**
	 * Returns all properties as defined in the BO interface.
	 * @param object the object
	 * @return the names of all relevant interfaces
	 */
	@SuppressWarnings("unchecked")
	protected Collection<String> getPropertyNames(Object object) {
		Set<String> rc = transientCache.get(object.getClass());
		if (rc == null) {
			Set<String> transients = new HashSet<String>();
			Set<String> nontransients = new HashSet<String>();
			// Collect all transient and persistent properties from interfaces
			for (Class<?> clazz2 : object.getClass().getInterfaces()) {
				transients.addAll(BeanSupport.INSTANCE.getTransientProperties(clazz2));
				nontransients.addAll(BeanSupport.INSTANCE.getNonTransientProperties(clazz2));
			}
			// Make difference of both sets
			;
			// Add all others to the return
			rc = new HashSet<String>(CollectionUtils.subtract(nontransients, transients));
			transientCache.put(object.getClass(), Collections.unmodifiableSet(rc));
		}
		return rc;
	}

	/**
	 * Returns all properties as defined in the BO interface.
	 * @param object the object
	 * @return the names of all relevant interfaces
	 */
	protected Collection<String> getBeanPropertyNames(Object object) {
		return BeanSupport.INSTANCE.getNonTransientProperties(object);
	}

	/**
	 * Returns the {@link #encoding}.
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Sets the {@link #encoding}.
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	
}
