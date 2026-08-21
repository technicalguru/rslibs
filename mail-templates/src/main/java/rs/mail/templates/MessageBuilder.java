/**
 * 
 */
package rs.mail.templates;

import org.apache.commons.lang3.StringUtils;

/**
 * Builds message objects from {@link Template}s.
 * <p>The interface is agnostic against the specific type of message to be built. It is
 *    up to implementations to return the respective object.</p>
 *    
 * @param <T> the type of message this builder produces
 * 
 * @author ralph
 *
 */
public interface MessageBuilder<T> {

	/**
	 * Use the given context.
	 * <p>The given context parameters will be used for this builder. Previous builder settings will be deleted.</p>
	 * @param context - the new context
	 * @return this message builder for chaining.
	 */
	default public MessageBuilder<T> withContext(TemplateContext context) {
		return withContext(context, true);
	}

	/**
	 * Use the given context.
	 * <p>The given context parameters will be used for this builder. Previous settings will be amended/overwritten
	 * or removed.</p>
	 * @param context - the context to take parameters from
	 * @param replace - if {@code true}: delete any previous settings from builder, otherwise override/amend them. 
	 * @return this message builder for chaining.
	 */
	public MessageBuilder<T> withContext(TemplateContext context, boolean replace);

	/**
	 * Sets the template name for the body.
	 * @param templateName - name of template to be used for the message body
	 * @return this message builder for chaining.
	 */
	public MessageBuilder<T> withBodyTemplate(String templateName);
	
	/**
	 * Sets the template name for the body.
	 * @param templateName - name of template to be used for the message subject
	 * @return this message builder for chaining.
	 */
	public MessageBuilder<T> withSubjectTemplate(String templateName);
	
	/**
	 * Sets the translations.
	 * @param i18nName - name of translations to be used for the message subject
	 * @return this message builder for chaining.
	 */
	public MessageBuilder<T> withI18n(String i18nName);
	
	/**
	 * Adds a value object to the builder. The name will be the simple class name of that object.
	 * @param o    - the object to be used  
	 * @return this message builder for chaining.
	 */
	default public MessageBuilder<T> withValue(Object o) {
		return withValue(StringUtils.uncapitalize(o.getClass().getSimpleName()), o);
	}
	
	/**
	 * Adds a value object to the builder.
	 * @param name - name to be used for that object
	 * @param o    - the object to be used 
	 * @return this message builder for chaining.
	 */
	public MessageBuilder<T> withValue(String name, Object o);
	
	/**
	 * Adds the template resolvers to the builder.
	 * @param templateResolvers - new template resolvers to be added
	 * @return this message builder for chaining.
	 */
	public MessageBuilder<T> withResolver(TemplateResolver ...templateResolvers);
	
	/**
	 * Adds the translation resolvers to the builder.
	 * @param i18nResolvers - new i18n resolvers to be added
	 * @return this message builder for chaining.
	 */
	public MessageBuilder<T> withResolver(I18nResolver ...i18nResolvers);
	
	/**
	 * Produces the message.
	 * @return the message containing subject and body.
	 * @throws BuilderException when building the message fails
	 */
	public T build() throws BuilderException;
	
}
