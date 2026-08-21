package rs.mail.templates.resolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import rs.mail.templates.ResolverException;
import rs.mail.templates.Template;
import rs.mail.templates.TemplateContext;
import rs.mail.templates.TemplateResolver;
import rs.mail.templates.cache.Cache;
import rs.mail.templates.cache.CacheFactory;
import rs.mail.templates.cache.CacheFactory.CacheBuilder;
import rs.mail.templates.cache.CacheStrategy;
import rs.mail.templates.impl.DefaultTemplate;
import rs.mail.templates.impl.ResolverId;

/**
 * The default template resolver searches a specific directory (non-recursive)
 * for the locale-specific versions of a template:
 * <ol>
 * <li>&lt;directory&gt;/&lt;template-name&gt;.&lt;context.locale.language&gt;-&lt;context.locale.country&gt;.&lt;content-type-suffix&gt;</li>
 * <li>&lt;directory&gt;/&lt;template-name&gt;.&lt;context.locale.language&gt;.&lt;content-type-suffix&gt;</li>
 * <li>&lt;directory&gt;/&lt;template-name&gt;.&lt;content-type-suffix&gt;</li>
 * </ol>
 * <p>where {@code content-type-suffix} is either {@code html} or {@code txt}. So for template {@code my-template} in 
 * locale {@code de-DE} the following files are searched in order of priority</p>
 *
 * <ol>
 * <li>&lt;directory&gt;/my-template.de-DE.html</li>
 * <li>&lt;directory&gt;/my-template.de-DE.txt</li>
 * <li>&lt;directory&gt;/my-template.de.html</li>
 * <li>&lt;directory&gt;/my-template.de.txt</li>
 * <li>&lt;directory&gt;/my-template.html</li>
 * <li>&lt;directory&gt;/my-template.txt</li>
 * </ol>
 * <p>The prioritized list of paths can be changed by overrriding {@link #getPriorityPaths(String, TemplateContext, String)}.</p>
 * 
 * <p>Please notice that an additiona file {@code <directory>/my-template.ftl} will be returned when the name was {@code my-template.ftl}
 *    (ends with {@code .ftl}). This allows an easier inclusion of general Freemarker libraries.</p>
 * @author ralph
 *
 */
public class DefaultTemplateResolver extends AbstractFileResolver<Template> implements TemplateResolver {

	/**
	 * Constructor (which uses a LRU cache).
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultTemplateResolver(File directory) throws IOException {
		this(directory, true);
	}
	
	/**
	 * Constructor to enable or disable the default LRU cache.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param enableCache whether to enable the Cache
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultTemplateResolver(File directory, boolean enableCache) throws IOException {
		this(directory, enableCache? CacheFactory.newBuilder(ResolverId.class, Template.class).with(CacheStrategy.LRU).build() : null);
	}
	
	/**
	 * Constructor to use a specific cache strategy.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cacheStrategy the cahce strategy to be used
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultTemplateResolver(File directory, CacheStrategy cacheStrategy) throws IOException {
		this(directory, CacheFactory.newBuilder(ResolverId.class, Template.class).with(cacheStrategy));
	}
	
	/**
	 * Constructor to use a custom {@link CacheBuilder}.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cacheBuilder the cache builder
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultTemplateResolver(File directory, CacheBuilder<ResolverId,Template> cacheBuilder) throws IOException {
		this(directory, cacheBuilder.build());
	}
	
	/**
	 * Constructor to use a custom cache.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cache the cache to be used (can be null)
	 * @throws IOException - when the directory is not accessible
	 */
	public DefaultTemplateResolver(File directory, Cache<ResolverId, Template> cache) throws IOException {
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
	protected Template create(ResolverId id, String name, TemplateContext context) throws ResolverException {
		List<File> htmlPriorities = getPriorityPaths(name, context, ".html");
		File       htmlFile       = findFile(htmlPriorities);
		List<File> textPriorities = getPriorityPaths(name, context, ".txt");
		File       textFile       = findFile(textPriorities);
		if ((htmlFile != null) || (textFile != null)) try {
			String htmlContent = htmlFile != null ? IOUtils.toString(new FileInputStream(htmlFile), getCharset(true)) : null;
			String textContent = textFile != null ? IOUtils.toString(new FileInputStream(textFile), getCharset(true)) : null;
			return createTemplate(id, htmlContent, textContent);
		} catch (IOException e) {
			throw new ResolverException(this, "Cannot load template", e);
		}
		return null;
	}

	/**
	 * Creates the template using the ID and the content.
	 * @param id - ID of template
	 * @param htmlContent - HTML content
	 * @param textContent - TEXT content
	 * @return the created template
	 */
	protected Template createTemplate(ResolverId id, String htmlContent, String textContent) {
		return new DefaultTemplate(id, htmlContent, textContent);
	}
	
	/**
	 * Returns the files to be checked in order of priority.
	 * @param name - the name of the template
	 * @param context - the context
	 * @param suffix - the suffix to be checked (usually .html and .txt)
	 * @return the files to be checked
	 * @see #getFileVariants(TemplateContext)
	 */
	protected List<File> getPriorityPaths(String name, TemplateContext context, String suffix) {
		List<File> rc = super.getPriorityPaths(name, context, suffix);
		if (name.endsWith(".ftl")) rc.add(new File(getDirectory(), name));
		return rc;
	}
	
}
