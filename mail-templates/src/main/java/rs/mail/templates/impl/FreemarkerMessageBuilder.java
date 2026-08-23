/**
 * 
 */
package rs.mail.templates.impl;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.io.output.StringBuilderWriter;

import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import no.api.freemarker.java8.Java8ObjectWrapper;
import rs.mail.templates.BuilderException;
import rs.mail.templates.ContentType;
import rs.mail.templates.MessageBuilder;
import rs.mail.templates.TemplateContext;

/**
 * Default implementation with Freemarker.
 * <p>It needs to be noted that caching is handled at {@link MessageBuilder} level
 *    not at FreeMarker level. That's why the caching for Freemarker is disabled.</p>
 *    
 * @param <T> the type of the build result
 * 
 * @author ralph
 *
 */
public class FreemarkerMessageBuilder<T> extends AbstractMessageBuilder<T> {

	private Configuration            configuration;
	private FreemarkerTemplateLoader templateLoader;
	
	/**
	 * Constructor.
	 * @param messageCreator the producer of the message implementation
	 */
	public FreemarkerMessageBuilder(MessageCreator<T> messageCreator) {
		super(messageCreator);
		this.configuration  = null;
		this.templateLoader = null;
	}

	/**
	 * Returns or creates the configuration for Freemarker.
	 * 
	 * @return the freemarker configuration
	 */
	protected Configuration getFreemarkerConfiguration() {
		if (configuration == null) {
			configuration = createFreemarkerConfiguration();
		}
		return configuration;
	}
	
	/**
	 * Creates a configuration based on current builder status.
	 * @return a fresh configuration
	 */
	protected Configuration createFreemarkerConfiguration() {
		// Configuration
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
		cfg.setTemplateLoader(getFreemarkerTemplateLoader());
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		cfg.setFallbackOnNullLoopVariable(false);
		cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
		cfg.setCacheStorage(new NullCacheStorage());
		cfg.setObjectWrapper(new Java8ObjectWrapper(Configuration.VERSION_2_3_31));
		return cfg;
	}
	
	/**
	 * Returns or creates the template loader for Freemarker.
	 * 
	 * @return the template loader
	 */
	protected FreemarkerTemplateLoader getFreemarkerTemplateLoader() {
		if (templateLoader == null) {
			templateLoader = new FreemarkerTemplateLoader(this);
		}
		return templateLoader;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String build(String templateName, ContentType contentType, Map<String,String> translations) throws BuilderException {
		TemplateContext          context = getContext();
		Configuration            config  = getFreemarkerConfiguration();
		FreemarkerTemplateLoader loader  = getFreemarkerTemplateLoader();
		
		// Create the data model with values
		Map<String, Object> root = new HashMap<>(context.getValues());
		// Add translations
		root.put("i18n", translations);
		
		try {
			loader.setContentType(contentType);
			freemarker.template.Template temp = config.getTemplate(templateName);
			Writer out = new StringBuilderWriter();
			temp.process(root, out);
			return out.toString();
		} catch (Throwable t) {
			throw new BuilderException(this, "Cannot process template", t);
		}
	}

}
