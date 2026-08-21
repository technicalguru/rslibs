/**
 * 
 */
package rs.mail.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * The values of a mail generation.
 * 
 * Holds information about templates, resolvers, user objects and locale.
 * 
 * <p>A context will be "locked" when the template processing started. This means that 
 *    only values and locale can be changed but no other objects such as resolvers 
 *    or template names or message producers can be modified.</p>
 *    
 * @author ralph
 *
 */
public class TemplateContext {

	private Map<String, Object>    values;
	private List<TemplateResolver> resolvers;
	private List<I18nResolver>     i18nResolvers;
	private String                 subjectTemplateName;
	private String                 bodyTemplateName;
	private String                 i18nName;
	private Locale                 locale;
	private boolean                locked;
	
	/**
	 * Constructor.
	 */
	public TemplateContext() {
		values        = new HashMap<>();
		resolvers     = new ArrayList<>();
		i18nResolvers = new ArrayList<>();
		locked        = false;
		locale        = Locale.getDefault();
	}
	
	
	/**
	 * Returns whether the context is locked.
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Locks this context.
	 */
	public void lock() {
		this.locked = true;
	}

	/**
	 * Returns all data values from the context.
	 * @return the values
	 */
	public Map<String,Object> getValues() {
		return values;
	}
	
	/**
	 * Returns the value object with given name.
	 * @param name - name of object to be returned
	 * @return the object or {@code null} if it does not exist
	 */
	public Object getValue(String name) {
		return values.get(name);
	}
	
	/**
	 * Returns the object of given class.
	 * <p>The name will be derived from the simple class name</p>.
	 * @param <T> - the class type
	 * @param clazz - class of object to be returned
	 * @return the object or {@code null} if it does not exist
	 */
	public <T> T getValue(Class<T> clazz) {
		return getValue(getDefaultName(clazz), clazz);
	}
	
	/**
	 * Returns the object of given class.
	 * @param <T> - the class type
	 * @param name - name of object to be returned
	 * @param clazz - class of object to be returned
	 * @return the object or {@code null} if it does not exist
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(String name, Class<T> clazz) {
		return (T)values.get(name);
	}
	
	/**
	 * Returns all objects of given class.
	 * @param <T> - the class type
	 * @param clazz - class of objects to be returned
	 * @return a map containing all objects of given class along with their names (can be empty when nothing found)
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String,T> getAllValues(Class<T> clazz) {
		Map<String,T> rc = new HashMap<>();
		for (Map.Entry<String, Object> entry : values.entrySet()) {
			if (entry.getValue().getClass().equals(clazz)) rc.put(entry.getKey(), (T)entry.getValue());
		}
		return rc;
	}
	
	/**
	 * Adds an object under the given name.
	 * @param name - name of object in values
	 * @param o the object to be set
	 */
	public void setValue(String name, Object o) {
		values.put(name, o);
	}
	
	/**
	 * Adds an object under its simple class name (uncapitalized).
	 * @param o - object to be set
	 */
	public void addValue(Object o) {
		if (o != null) setValue(getDefaultName(o.getClass()), o);
	}
	
	/**
	 * Provides the name under which a certain class object can be found in the values.
	 * @param clazz - the clazz object
	 * @return the name of the class object in values
	 */
	protected String getDefaultName(Class<?> clazz) {
		return StringUtils.uncapitalize(clazz.getSimpleName());
	}
	
	/**
	 * Sets the name of the body template.
	 * @param templateName - the name of the template
	 */
	public void setBodyTemplate(String templateName) {
		if (isLocked()) throw new RuntimeException("Context is locked");
		this.bodyTemplateName = templateName;
	}

	/**
	 * Returns the name of the body template.
	 * @return the bodyTemplateName
	 */
	public String getBodyTemplateName() {
		return bodyTemplateName;
	}

	/**
	 * Sets the name of the subject template.
	 * @param templateName - the name of the template
	 */
	public void setSubjectTemplate(String templateName) {
		if (isLocked()) throw new RuntimeException("Context is locked");
		this.subjectTemplateName = templateName;
	}

	/**
	 * Returns the name of the subject template.
	 * @return the subjectTemplateName
	 */
	public String getSubjectTemplateName() {
		return subjectTemplateName;
	}

	/**
	 * Returns the name of the translations.
	 * @return the name of the translations
	 */
	public String getI18nName() {
		return i18nName;
	}


	/**
	 * Sets the name of the translations.
	 * @param i18nName the name of the translations to set
	 */
	public void setI18nName(String i18nName) {
		if (isLocked()) throw new RuntimeException("Context is locked");
		this.i18nName = i18nName;
	}

	/**
	 * Add template resolvers.
	 * @param templateResolvers - the resolvers to add
	 */
	public void addResolver(TemplateResolver... templateResolvers) {
		if (isLocked()) throw new RuntimeException("Context is locked");
		for (TemplateResolver resolver : templateResolvers) {
			this.resolvers.add(resolver);
		}
	}

	/**
	 * Removes template resolvers.
	 * @param templateResolvers - the resolvers to remove
	 */
	public void removeResolver(TemplateResolver... templateResolvers) {
		if (isLocked()) throw new RuntimeException("Context is locked");
		for (TemplateResolver resolver : templateResolvers) {
			this.resolvers.remove(resolver);
		}
	}

	/**
	 * Returns the resolvers.
	 * @return the list of resolvers
	 */
	public List<TemplateResolver> getResolvers() {
		return resolvers;
	}
	
	/**
	 * Add translation resolvers.
	 * @param i18nResolvers - the resolvers to add
	 */
	public void addI18nResolver(I18nResolver... i18nResolvers) {
		if (isLocked()) throw new RuntimeException("Context is locked");
		for (I18nResolver resolver : i18nResolvers) {
			this.i18nResolvers.add(resolver);
		}
	}

	/**
	 * Removes translation resolvers.
	 * @param i18nResolvers - the resolvers to remove
	 */
	public void removeI18nResolver(I18nResolver... i18nResolvers) {
		if (isLocked()) throw new RuntimeException("Context is locked");
		for (I18nResolver resolver : i18nResolvers) {
			this.i18nResolvers.remove(resolver);
		}
	}

	/**
	 * Returns the translation resolvers.
	 * @return the list of resolvers
	 */
	public List<I18nResolver> getI18nResolvers() {
		return i18nResolvers;
	}
	
	/**
	 * Returns the locale for the generation.
	 * @return the locale (can be {@code null})
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Sets the locale for the generation.
	 * @param locale the locale to set (can be {@code null})
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Adds all object from the other context.
	 * @param other other context
	 */
	public void add(TemplateContext other) {
		if (isLocked()) throw new RuntimeException("Context is locked");
		this.values.putAll(other.values);
		this.resolvers.addAll(other.resolvers);
		this.i18nResolvers.addAll(other.i18nResolvers);
		this.subjectTemplateName = other.subjectTemplateName;
		this.bodyTemplateName    = other.bodyTemplateName;
		this.i18nName            = other.i18nName;
		if (other.locale != null) this.locale = other.locale;
	}
}
