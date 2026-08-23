/**
 * 
 */
package rs.mail.templates.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.mail.templates.BuilderException;
import rs.mail.templates.BuilderResult;
import rs.mail.templates.ContentType;
import rs.mail.templates.I18n;
import rs.mail.templates.I18nResolver;
import rs.mail.templates.MessageBuilder;
import rs.mail.templates.ResolverException;
import rs.mail.templates.Template;
import rs.mail.templates.TemplateContext;
import rs.mail.templates.TemplateResolver;

/**
 * Abstract base class for message builders.
 * 
 * @param <T> the type of message to be built
 * 
 * @author ralph
 *
 */
public abstract class AbstractMessageBuilder<T> implements MessageBuilder<T> {

	private Logger            log            = LoggerFactory.getLogger(getClass());
	private TemplateContext   context        = new TemplateContext();
	private MessageCreator<T> messageCreator;
	
	/**
	 * Creates the builder using the given creator.
	 * @param messageCreator - the message creator
	 */
	protected AbstractMessageBuilder(MessageCreator<T> messageCreator) {
		this.messageCreator = messageCreator;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageBuilder<T> withContext(TemplateContext context, boolean replace) {
		if (replace) this.context = new TemplateContext();
		this.context.add(context);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageBuilder<T> withBodyTemplate(String templateName) {
		context.setBodyTemplate(templateName);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageBuilder<T> withSubjectTemplate(String templateName) {
		context.setSubjectTemplate(templateName);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageBuilder<T> withI18n(String i18nName) {
		context.setI18nName(i18nName);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageBuilder<T> withValue(String name, Object o) {
		context.setValue(name, o);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageBuilder<T> withResolver(TemplateResolver... templateResolvers) {
		context.addResolver(templateResolvers);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageBuilder<T> withResolver(I18nResolver... i18nResolvers) {
		context.addI18nResolver(i18nResolvers);
		return this;
	}

	/**
	 * Returns the context.
	 * @return the context
	 */
	public TemplateContext getContext() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T build() throws BuilderException {
		try {
			BuilderResult result = buildContent();
			return messageCreator.create(result);
		} catch (BuilderException e) {
			throw e;
		} catch (Throwable t) {
			throw new BuilderException(this, "Cannot construct MIME message", t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public BuilderResult buildContent() throws BuilderException {
		// It is important to lock the context
		context.lock();
		
		// Issue a warning when the locale was undefined
		if ((context.getLocale() == null) || context.getLocale().toString().equals("")) {
			log.warn("Locale is undefined");
		}
		
		String subject = buildSubject();
		String html    = buildBody(ContentType.HTML);
		String text    = buildBody(ContentType.TEXT);

		if ((text == null) && (html != null)) {
			text = toText(html); 
		}
		if ((text != null) && (html == null)) {
			html = toHtml(text);
		}
		
		if ((text != null) || (html != null)) {
			return new BuilderResult(subject, text, html);
		}
		throw new BuilderException(this, "Cannot build message");
	}
	
	/**
	 * Fills the given template using the current context and given translations.
	 * <p>Implementors must override this method when they resolve templates in its
	 *    own way (e.g. they provide a sub-template feature with their own resolve
	 *    mechanism). If implementors do not need to adopt the resolve mechanism
	 *    then they shall override {@link #build(Template, ContentType, Map)} and use 
	 *    the resolved template directly.</p>
	 * <p>The default implementation resolves the template and calls
	 *    {@link #build(Template, ContentType, Map)}.</p>
	 *    
	 * @param templateName - the template name
	 * @param contentType - the content type to build
	 * @param translations - the translations map for the build
	 * @return the template filled with translations and context values
	 * @throws BuilderException when the build or resolving fails.
	 * @see #build(Template, ContentType, Map)
	 */
	protected String build(String templateName, ContentType contentType, Map<String,String> translations) throws BuilderException {
		try {
			Template template = resolve(templateName);
			return build(template, contentType, translations);
		} catch (ResolverException e) {
			throw new BuilderException(this, e.getMessage(), e);
		}
	}
	
	/**
	 * Fills the given template using the current context and given translations.
	 * <p>Implementors shall override this method when they do not need to adopt 
	 *    the resolve mechanism and just wanna use the resolved template directly.</p>
	 * <p>The default implementation does nothing but return the template's content.</p>
	 * 
	 * @param template - the template
	 * @param contentType - the content type to build
	 * @param translations - the translations map for the build
	 * @return the template filled with translations and context values
	 * @throws BuilderException when the build or resolving fails.
	 * @see #build(String, ContentType, Map)
	 */
	protected String build(Template template, ContentType contentType, Map<String,String> translations) throws BuilderException {
		return template.getTemplate(contentType);
	}

	/**
	 * Build the subject of the message
	 * @return the subject or {@code null} if no template available
	 * @throws BuilderException when building the subject fails
	 */
	protected String buildSubject() throws BuilderException {
		try {
			Map<String,String> i18n = getI18n(); 
			return build(context.getSubjectTemplateName(), ContentType.TEXT, i18n);
		} catch (ResolverException e) {
			throw new BuilderException(this, e.getMessage(), e);
		}
	}
	
	/**
	 * Build the body of the message.
	 * @param contentType the content type of the build
	 * @return the subject or {@code null} if no template available
	 * @throws BuilderException when building the body fails
	 */
	protected String buildBody(ContentType contentType) throws BuilderException {
		try {
			Map<String,String> i18n = getI18n(); 
			return build(context.getBodyTemplateName(), contentType, i18n);
		} catch (ResolverException e) {
			throw new BuilderException(this, e.getMessage(), e);
		}
	}
	
	/**
	 * Resolves the translations as defined in context.
	 * @return the translations to be used
	 * @throws ResolverException when resolving process fails
	 */
	protected Map<String,String> getI18n() throws ResolverException {
		Map<String,String> rc = resolveI18n(context.getI18nName());
		return rc;
	}
	
	/**
	 * Resolves the name of the template.
	 * @param name - name of template to find
	 * @return the template or {@code null} if not found.
	 * @throws ResolverException when resolving fails
	 */
	protected Template resolve(String name) throws ResolverException {
		if (name != null) {
			for (TemplateResolver resolver : context.getResolvers()) {
				Template rc = resolver.resolve(name, context);
				if (rc != null) return rc;
			}
		}
		return null;
	}
	
	/**
	 * Resolves the translations for the given name.
	 * <p>Translations from resolvers with higher priority precede translations from
	 *    resolvers with lower priority.</p>
	 * @param name - name of translations to find
	 * @return the translations, must not be {@code null}
	 * @throws ResolverException when resolving fails
	 */
	protected Map<String,String> resolveI18n(String name) throws ResolverException {
		Map<String,String> translations = new HashMap<>();
		List<I18nResolver> resolvers = context.getI18nResolvers();
		for (int i=resolvers.size()-1; i>= 0; i--) {
			I18nResolver resolver = resolvers.get(i);
			I18n             i18n = resolver.resolve(name, context);
			if (i18n != null) translations.putAll(i18n);
		}
		return translations;
	}
	
	/**
	 * Converts HTML to TEXT content by stripping off all tags using {@link Jsoup}.
	 * 
	 * @param html - the HTML content
	 * @return the text content
	 */
	protected String toText(String html) {
		return Jsoup.parse(html).wholeText();
	}
	
	/**
	 * Converts TEXT to HTML content by replacing all newlines with &lt;br&gt; tags.
	 * 
	 * @param text - the TEXT content
	 * @return the HTML content
	 */
	protected String toHtml(String text) {
		return text.replaceAll("\\n", "<br>");
	}
}
