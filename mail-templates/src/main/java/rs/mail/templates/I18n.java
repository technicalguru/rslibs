/**
 * 
 */
package rs.mail.templates;

import java.util.HashMap;
import java.util.Properties;

import rs.mail.templates.impl.ResolverId;

/**
 * Translations of a template.
 * 
 * An instantiation of this class provides translations.
 * 
 * @author ralph
 *
 */
public class I18n extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;
	/** ID of resolved object */
	private ResolverId id;

	/**
	 * Constructor.
	 * 
	 * @param id the resolver id
	 */
	public I18n(ResolverId id) {
		this(id, null);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id the resolver id
	 * @param properties a properties object to load translations from
	 */
	public I18n(ResolverId id, Properties properties) {
		this.id = id;
		if (properties != null) {
			for (Object k : properties.keySet()) {
				String key = (String)k;
				put(key, properties.getProperty(key));
			}
		}
	}
	
	/**
	 * Returns the ID of the translations.
	 * @return the id
	 */
	public ResolverId getId() {
		return id;
	}

}
