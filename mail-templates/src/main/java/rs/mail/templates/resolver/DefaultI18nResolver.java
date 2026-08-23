package rs.mail.templates.resolver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import rs.mail.templates.I18n;
import rs.mail.templates.I18nResolver;
import rs.mail.templates.ResolverException;
import rs.mail.templates.TemplateContext;
import rs.mail.templates.cache.Cache;
import rs.mail.templates.cache.CacheFactory;
import rs.mail.templates.cache.CacheFactory.CacheBuilder;
import rs.mail.templates.cache.CacheStrategy;
import rs.mail.templates.impl.ResolverId;

/**
 * The default I18N resolver searches a specific directory (non-recursive)
 * for the locale-specific versions of properties:
 * <ol>
 * <li>&lt;directory&gt;/&lt;name&gt;.&lt;context.locale.language&gt;-&lt;context.locale.country&gt;.properties</li>
 * <li>&lt;directory&gt;/&lt;name&gt;.&lt;context.locale.language&gt;.properties</li>
 * <li>&lt;directory&gt;/&lt;name&gt;.properties</li>
 * <li>&lt;directory&gt;/i18n.properties</li>
 * </ol>
 * <p>Please notice that an additional file {@code <directory>/i18n.properties} will be returned. This allows a very general
 *    definition of translation keys independant of any name or language.</p>
 * 
 * <p>For a name {@code my-translations} in locale {@code de-DE} the following files are searched in order of priority</p>
 *
 * <ol>
 * <li>&lt;directory&gt;/my-translations.de-DE.properties</li>
 * <li>&lt;directory&gt;/my-translations.de.properties</li>
 * <li>&lt;directory&gt;/my-translations.properties</li>
 * <li>&lt;directory&gt;/i18n.properties</li>
 * </ol>
 * <p>The prioritized list of paths can be changed by overrriding {@link #getPriorityPaths(String, TemplateContext, String)}.</p>
 * 
 * <p>All properties files that exist will be loaded where definitions in files of higher priorities precede those in files with
 *    lower priority.</p>
 *    
 * @author ralph
 *
 */
public class DefaultI18nResolver extends AbstractFileResolver<I18n> implements I18nResolver {

	/**
	 * Constructor (which uses a LRU cache).
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultI18nResolver(File directory) throws IOException {
		this(directory, true);
	}
	
	/**
	 * Constructor to enable or disable the default LRU cache.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param enableCache whether to enable the Cache
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultI18nResolver(File directory, boolean enableCache) throws IOException {
		this(directory, enableCache? CacheFactory.newBuilder(ResolverId.class, I18n.class).with(CacheStrategy.LRU).build() : null);
	}
	
	/**
	 * Constructor to use a specific cache strategy.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cacheStrategy the cahce strategy to be used
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultI18nResolver(File directory, CacheStrategy cacheStrategy) throws IOException {
		this(directory, CacheFactory.newBuilder(ResolverId.class, I18n.class).with(cacheStrategy));
	}
	
	/**
	 * Constructor to use a custom {@link CacheBuilder}.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cacheBuilder the cache builder
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultI18nResolver(File directory, CacheBuilder<ResolverId,I18n> cacheBuilder) throws IOException {
		this(directory, cacheBuilder.build());
	}
	
	/**
	 * Constructor to use a custom cache.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cache the cache to be used (can be null)
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultI18nResolver(File directory, Cache<ResolverId, I18n> cache) throws IOException {
		super(directory, cache);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ResolverId getId(String name, TemplateContext context) {
		return new ResolverId(name, context.getLocale());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected I18n create(ResolverId id, String name, TemplateContext context) throws ResolverException {
		List<File> priorities = getPriorityPaths(name, context);
		
		// Load all from lowest to highest priority
		Properties properties = new Properties();
		try {
			for (int i=priorities.size()-1; i>=0; i--) {
				File file = priorities.get(i);
				if (file.exists() && file.isFile() && file.canRead()) {
					properties.load(new FileReader(file, getCharset()));
				}
			}
			return createI18n(id, properties);
		} catch (IOException e) {
			throw new ResolverException(this, "Cannot load translations", e);
		}
	}

	/**
	 * Returns the properties files to be checked in order of priority.
	 * @param name - the base name of the file
	 * @param context - the context
	 * @return the files to be checked
	 * @see #getFileVariants(TemplateContext)
	 */
	protected List<File> getPriorityPaths(String name, TemplateContext context) {
		List<File> rc = super.getPriorityPaths(name, context, ".properties");
		rc.add(new File(getDirectory(), "i18n.properties"));
		return rc;
	}

	/**
	 * Creates the template using the ID and the content.
	 * @param id - ID of template
	 * @param properties - the translation properties
	 * @return the created translations
	 */
	protected I18n createI18n(ResolverId id, Properties properties) {
		return new I18n(id, properties);
	}
	
}
