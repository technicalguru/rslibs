/**
 * 
 */
package rs.data.file.storage;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.XMLConfiguration;

import rs.baselib.bean.IBean;
import rs.data.api.bo.IGeneralBO;

/**
 * Storage strategy for XML files.
 * @author ralph
 *
 */
public class XmlStorageStrategy extends AbstractStorageStrategy<File> {

	/**
	 * Constructor.
	 */
	public XmlStorageStrategy() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends IGeneralBO<?>> void load(T bo, File specifier) throws IOException {
		// Find all relevant property names
		Collection<String> propertyNames = getPropertyNames(bo);
		try {
			XMLConfiguration cfg = new XMLConfiguration(specifier);
			for (String name : propertyNames) {
				PropertyDescriptor desc = PropertyUtils.getPropertyDescriptor(bo, name);
				String value = cfg.getString(name+"(0)");
				if ((value != null) && !value.isEmpty()) {
					String className = cfg.getString(name+"(0)[@class]");
					Class<?> clazz = Class.forName(className);
					if (IGeneralBO.class.isAssignableFrom(clazz)) {
						// TODO getDao().findBy()
					} else if (Collection.class.isAssignableFrom(clazz)) {
						// TODO load each item
					} else if (Map.class.isAssignableFrom(clazz)) {
						// TODO load each item
					} else if (IBean.class.isAssignableFrom(clazz)) {
						// TODO	load each property	
					} else {
						Object o = unserialize(className, value);
						if (o != null) {
							bo.set(desc.getName(), o);
						} else {
							throw new RuntimeException("Cannot unserialize property: "+className+": "+value);
						}
					}
				} else {
					bo.set(desc.getName(), null);
				}
			}
		} catch (Exception e) {
			throw new IOException("Cannot load XML file", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends IGeneralBO<?>> void save(T bo, File specifier) throws IOException {
		// Find all relevant property names
		Collection<String> propertyNames = getPropertyNames(bo);
		FileWriter out = null;
		try {
			out = new FileWriter(specifier);
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
				out.close();
			}
		}
	}

	protected void writeValue(Writer out, int indent, Object value, String tagName) throws IOException, ReflectiveOperationException {
		String indentS = "";
		for (int i=0; i<indent; i++) indentS += "   ";
		
		if (value != null) {
			out.write(indentS+"<"+tagName+" class=\""+value.getClass().getName()+"\">");
			//				if (IConfigurable.class.isAssignableFrom(type)) {
			//					SubnodeConfiguration subConfig = cfg.configurationAt(desc.getName()+"(0)");
			//					ConfigurationUtils.configure((IConfigurable)bo, subConfig);
			//				} else
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
	public <T extends IGeneralBO<?>> void refresh(T bo, File specifier) throws IOException {
		load(bo, specifier);
	}

}
