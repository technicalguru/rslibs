/*
 * This file is part of RS Library (Data File Library).
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
package rs.data.file.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.Charsets;

import rs.baselib.bean.IBean;
import rs.baselib.lang.LangUtils;
import rs.data.api.IDaoFactory;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;

/**
 * Storage strategy for XML files.
 * @param <K> type of ID for Business Objects to be managed
 * @param <T> type of Business Object the strategy manages
 * @author ralph
 *
 */
public class XmlStorageStrategy<K extends Serializable, T extends IGeneralBO<K>> extends AbstractStorageStrategy<K, T, File> {

	/**
	 * Constructor.
	 */
	public XmlStorageStrategy(IDaoFactory daoFactory) {
		super(daoFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(T bo, K id, File specifier) throws IOException {
		// Find all relevant property names
		Collection<String> propertyNames = new ArrayList<String>(getPropertyNames(bo));
		propertyNames.add("id");
		try {
			XMLConfiguration cfg = new XMLConfiguration();
			cfg.setListDelimiter((char)0);
			cfg.load(specifier);
			for (String name : propertyNames) {
				try {
					SubnodeConfiguration subConfig = cfg.configurationAt(name+"(0)");
					bo.set(name, loadValue(subConfig));
				} catch (IllegalArgumentException e) {
					bo.set(name,  null);
				}
			}
		} catch (Exception e) {
			throw new IOException("Cannot load XML file", e);
		}
	}

	/**
	 * Internal helper to load a sub configuration.
	 * <p>Descendants shall not override this but several specific helper methods.</p>
	 * @param cfg the tag to be loaded
	 * @return th eobject represented by this tag
	 * @see #loadBusinessObject(Class, HierarchicalConfiguration)
	 * @see #loadCollection(Class, HierarchicalConfiguration)
	 * @see #loadMap(Class, HierarchicalConfiguration)
	 * @see #loadBean(Class, HierarchicalConfiguration)
	 * @see #loadSerialized(Class, HierarchicalConfiguration)
	 */
	protected Object loadValue(HierarchicalConfiguration cfg) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String className = cfg.getString("[@class]");
		if ((className != null) && !className.isEmpty()) {
			Class<?> clazz = LangUtils.forName(className);
			Object rc = null;
			if (IGeneralBO.class.isAssignableFrom(clazz)) {
				rc = loadBusinessObject(clazz, cfg);
			} else if (Collection.class.isAssignableFrom(clazz)) {
				rc = loadCollection(clazz, cfg);
			} else if (Map.class.isAssignableFrom(clazz)) {
				rc = loadMap(clazz, cfg);
			} else if (IBean.class.isAssignableFrom(clazz)) {
				rc = loadBean(clazz, cfg);
			} else {
				rc = loadSerialized(clazz, cfg);
				if (rc == null) {
					throw new RuntimeException("Cannot unserialize property: "+className+": "+cfg.getString(""));
				}
			}
			return rc;
		} else {
			return null;
		}

	}
	
	/**
	 * Loads a {@link IGeneralBO business object} from the XML tag.
	 * @param clazz class to be loaded
	 * @param cfg configuration of class
	 * @return the loaded object
	 */
	@SuppressWarnings("unchecked")
	protected IGeneralBO<?> loadBusinessObject(Class<?> clazz, HierarchicalConfiguration cfg) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Serializable id = (Serializable)loadValue(cfg.configurationAt("refid(0)"));
		IDaoFactory factory = getDaoFactory();
		if (factory != null) {
			IGeneralDAO<?, ? extends IGeneralBO<? extends Serializable>> dao = factory.getDaoFor((Class<? extends IGeneralBO<? extends Serializable>>)clazz);
			if (dao != null) return dao.findById(id);
		}
		return null;
	}
	
	/**
	 * Loads a collection from the XML tag.
	 * @param clazz class to be loaded
	 * @param cfg configuration of class
	 * @return the loaded collection
	 */
	@SuppressWarnings("unchecked")
	protected Collection<?> loadCollection(Class<?> clazz, HierarchicalConfiguration cfg) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Collection<Object> collection = (Collection<Object>)clazz.getConstructor().newInstance();
		for (HierarchicalConfiguration subConfig : cfg.configurationsAt("item")) {
			collection.add(loadValue(subConfig));
		}
		return collection;
	}
	
	/**
	 * Loads a map from the XML tag.
	 * @param clazz class to be loaded
	 * @param cfg configuration of class
	 * @return the loaded map
	 */
	protected Map<?,?> loadMap(Class<?> clazz, HierarchicalConfiguration cfg) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map<Object,Object> map = new HashMap<Object, Object>();
		for (HierarchicalConfiguration subConfig : cfg.configurationsAt("item")) {
			Object key = loadValue(subConfig.configurationAt("key(0)"));
			Object value = loadValue(subConfig.configurationAt("value(0)"));
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * Loads a {@link IBean} from the XML tag.
	 * @param clazz class to be loaded
	 * @param cfg configuration of class
	 * @return the loaded bean
	 */
	protected IBean loadBean(Class<?> clazz, HierarchicalConfiguration cfg) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		IBean bean = (IBean)clazz.getConstructor().newInstance();
		for (String name : getBeanPropertyNames(clazz)) {
			bean.set(name, loadValue(cfg.configurationAt(name+"(0)")));
		}
		return bean;
	}
	
	/**
	 * Loads a {@link Serializable} from the XML tag.
	 * @param clazz class to be loaded
	 * @param cfg configuration of class
	 * @return the loaded object
	 */
	protected Object loadSerialized(Class<?> clazz, HierarchicalConfiguration cfg) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String valueString = cfg.getString(""); // ?
		return unserialize(clazz.getName(), valueString);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(T bo, File specifier) throws IOException {
		// Find all relevant property names
		Collection<String> propertyNames = new ArrayList<String>(getPropertyNames(bo));
		propertyNames.add("id");
		Writer out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(specifier), Charsets.toCharset(getEncoding()));
			out.write("<?xml version=\"1.0\" encoding=\""+getEncoding()+"\"?>\n");
			out.write("<object class=\""+bo.getClass().getName()+"\">\n");
			for (String name : propertyNames) {
				Object value = PropertyUtils.getProperty(bo, name);
				writeValue(out, 1, value, name);
			}
			out.write("</object>");
		} catch (Exception e) {
			throw new IOException("Cannot load XML file", e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * Internal helper to write a value to the XML stream.
	 * @param out the stream
	 * @param indent the indentation to be used
	 * @param value the value to be written
	 * @param tagName the tag name to be used
	 */
	protected void writeValue(Writer out, int indent, Object value, String tagName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		StringBuilder foo = new StringBuilder();
		for (int i=0; i<indent; i++) foo.append("   ");
		String indentS = foo.toString();
		
		if (value != null) {
			out.write(indentS+"<"+tagName+" class=\""+value.getClass().getName()+"\">");
			if (value instanceof IGeneralBO) {
				out.write("\n");
				writeValue(out, indent+1, ((IGeneralBO<?>) value).getId(), "refid");
				out.write(indentS);
			} else if (value instanceof Collection) {
				out.write("\n");
				// We need to split this up
				for (Object o : (Collection<?>)value) {
					writeValue(out, indent+1, o, "item");
				}
				out.write(indentS);
			} else if (value instanceof Map) {
				out.write("\n");
				// We need to split this up
				for (Map.Entry<?,?> entry : ((Map<?,?>)value).entrySet()) {
					out.write(indentS+"   <item>\n");
					writeValue(out, indent+2, entry.getKey(), "key");
					writeValue(out, indent+2, entry.getValue(), "value");
					out.write(indentS+"   </item>\n");
				}
				out.write(indentS);
			} else if (value instanceof IBean) {
				out.write("\n");
				for (String name : getBeanPropertyNames(value)) {
					Object v = PropertyUtils.getProperty(value, name);
					writeValue(out, indent+1, v, name);
				}
				out.write(indentS);
			} else {
				String s = serialize(value);
				if (s != null) {
					if (isXmlCompatible(s)) {
						out.write(s);
					} else {
						out.write("<![CDATA[");
						out.write(s);
						out.write("]]>");
					}
				} else {
					out.write("<![CDATA[");
					out.write(value.toString());
					out.write("]]>");
				}
			}
			out.write("</"+tagName+">\n");
		} else {
			out.write(indentS+"<"+tagName+"/>\n");
		}

	}
	
	/**
	 * Returns whether the string is XML compatible.
	 * @param s string to be evaluated
	 * @return <code>true</code when string can be used as content in XML
	 */
	protected boolean isXmlCompatible(String s) {
		return (s.indexOf("&") < 0) && (s.indexOf(">") < 0) && (s.indexOf("<") < 0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh(T bo, File specifier) throws IOException {
		load(bo, bo.getId(), specifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<K, File> getList(Collection<File> specifiers) throws IOException {
		Map<K, File> rc = new HashMap<K, File>();
		// Read the ID of each file
		for (File f : specifiers) {
			try {
				XMLConfiguration cfg = new XMLConfiguration();
				cfg.setListDelimiter((char)0);
				cfg.load(f);
					try {
						SubnodeConfiguration subConfig = cfg.configurationAt("id(0)");
						K id = (K)loadValue(subConfig);
						rc.put(id, f);
					} catch (IllegalArgumentException e) {
						// Ignore
					}
			} catch (Exception e) {
				throw new IOException("Cannot load XML file", e);
			}
		}
		
		return rc;
	}

}
