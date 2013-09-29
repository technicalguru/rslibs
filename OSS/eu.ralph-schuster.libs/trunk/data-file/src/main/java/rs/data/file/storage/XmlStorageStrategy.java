/**
 * 
 */
package rs.data.file.storage;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.XMLConfiguration;

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
		try {
			XMLConfiguration cfg = new XMLConfiguration(specifier);
			for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(bo.getClass())) {
				Class<?> type = desc.getPropertyType();	
				String value = cfg.getString(desc.getName()+"(0)");
				if ((value != null) && !value.isEmpty()) {
					//				if (IConfigurable.class.isAssignableFrom(type)) {
					//					SubnodeConfiguration subConfig = cfg.configurationAt(desc.getName()+"(0)");
					//					ConfigurationUtils.configure((IConfigurable)bo, subConfig);
					//				} else 
					if (Serializable.class.isAssignableFrom(type)) {
						bo.set(desc.getName(), unserialize(value));
					} else {
						throw new RuntimeException("Cannot unserialize \""+value+"\"");
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
		FileWriter out = null;
		try {
			out = new FileWriter(specifier);
			out.write("<?xml >\n");
			out.write("<object class=\""+bo.getClass().getName()+"\">\n");
			for (PropertyDescriptor desc : PropertyUtils.getPropertyDescriptors(bo.getClass())) {
				Class<?> type = desc.getPropertyType();			
				Object value = PropertyUtils.getProperty(bo, desc.getName());
				if (value != null) {
					out.write("   <"+desc.getName()+" class=\""+type.getName()+"\">");
					//				if (IConfigurable.class.isAssignableFrom(type)) {
					//					SubnodeConfiguration subConfig = cfg.configurationAt(desc.getName()+"(0)");
					//					ConfigurationUtils.configure((IConfigurable)bo, subConfig);
					//				} else 
					if (Serializable.class.isAssignableFrom(type)) {
						out.write(serialize(value));
					} else {
						if (value != null) {
							out.write("<[[CDATA[");
							out.write(value.toString());
							out.write("]]>");
						}
					}
					out.write("</"+desc.getName()+">\n");
				} else {
					out.write("   <"+desc.getName()+" class=\""+type.getName()+"\"/>\n");
				}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends IGeneralBO<?>> void refresh(T bo, File specifier) throws IOException {
		load(bo, specifier);
	}

}
