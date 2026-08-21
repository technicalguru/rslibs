package rs.mail.templates.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import freemarker.cache.TemplateLoader;
import rs.mail.templates.ContentType;
import rs.mail.templates.ResolverException;
import rs.mail.templates.Template;

/**
 * Implements the loading mechanism for Freemarker implementation.
 * <p>The template loader must be set individually for each generation???</p>
 * 
 * @author ralph
 *
 */
public class FreemarkerTemplateLoader implements TemplateLoader {

	private FreemarkerMessageBuilder<?> builder;
	private ContentType              contentType;
	private long                     lastModifiedCount = 0;
	
	/**
	 * Constructor.
	 * @param builder the message builder this loader belongs to.
	 */
	public FreemarkerTemplateLoader(FreemarkerMessageBuilder<?> builder) {
		this.builder     = builder;
		this.contentType = ContentType.HTML;
	}
	
	/**
	 * Returns the contentType.
	 * @return the contentType
	 */
	public ContentType getContentType() {
		return contentType;
	}


	/**
	 * Sets the contentType.
	 * @param contentType the contentType to set
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object findTemplateSource(String name) throws IOException {
		try {
			return builder.resolve(name);
		} catch (ResolverException e) {
			throw new IOException("Cannot resolve the template", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLastModified(Object templateSource) {
		return lastModifiedCount++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		Template template = (Template)templateSource;
		return new StringReader(template.getTemplate(contentType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
	}

}
