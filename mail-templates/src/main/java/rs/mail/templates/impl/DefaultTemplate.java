package rs.mail.templates.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import rs.mail.templates.ContentType;
import rs.mail.templates.Template;

/**
 * A default implementation of a template.
 * 
 * @author ralph
 *
 */
public class DefaultTemplate implements Template {

	private ResolverId id;
	private Map<ContentType,String> content;
	
	/**
	 * Default constructor.
	 * @param id ID of template
	 */
	public DefaultTemplate(ResolverId id) {
		this(id, (String)null, (String)null);
	}

	/**
	 * Constructor for {@link URL} loading.
	 * @param id ID of template
	 * @param htmlContent - the HTML content URL
	 * @param textContent - the TEXT content URL
	 * @param charset - the charset to load with
	 * @throws IOException when loading the templates fails
	 */
	public DefaultTemplate(ResolverId id, URL htmlContent, URL textContent, Charset charset) throws IOException {
		content = new HashMap<>();
		this.id = id;
		setContent(ContentType.HTML, htmlContent, charset);
		setContent(ContentType.TEXT, textContent, charset);
	}
	
	/**
	 * Constructor for {@link File} loading.
	 * @param id ID of template
	 * @param htmlContent - the HTML content file
	 * @param textContent - the TEXT content file
	 * @param charset - the charset to load with
	 * @throws IOException when loading the templates fails
	 */
	public DefaultTemplate(ResolverId id, File htmlContent, File textContent, Charset charset) throws IOException {
		content = new HashMap<>();
		this.id = id;
		setContent(ContentType.HTML, htmlContent, charset);
		setContent(ContentType.TEXT, textContent, charset);
	}
	
	/**
	 * Constructor for {@link InputStream} loading.
	 * @param id ID of template
	 * @param htmlContent - the HTML content input stream
	 * @param textContent - the TEXT content input stream
	 * @param charset - the charset to load with
	 * @throws IOException when loading the templates fails
	 */
	public DefaultTemplate(ResolverId id, InputStream htmlContent, InputStream textContent, Charset charset) throws IOException {
		content = new HashMap<>();
		this.id = id;
		setContent(ContentType.HTML, htmlContent, charset);
		setContent(ContentType.TEXT, textContent, charset);
	}
	
	/**
	 * Constructor for {@link Reader} loading.
	 * @param id ID of template
	 * @param htmlContent - the HTML content reader
	 * @param textContent - the TEXT content reader
	 * @throws IOException when loading the templates fails
	 */
	public DefaultTemplate(ResolverId id, Reader htmlContent, Reader textContent) throws IOException {
		content = new HashMap<>();
		this.id = id;
		setContent(ContentType.HTML, htmlContent);
		setContent(ContentType.TEXT, textContent);
	}
	
	/**
	 * Constructor.
	 * @param id ID of template
	 * @param htmlContent - the HTML content
	 * @param textContent - the TEXT content
	 */
	public DefaultTemplate(ResolverId id, String htmlContent, String textContent) {
		content = new HashMap<>();
		this.id = id;
		setContent(ContentType.HTML, htmlContent);
		setContent(ContentType.TEXT, textContent);
	}
	
	/**
	 * Returns the id.
	 * @return the id
	 */
	public ResolverId getId() {
		return id;
	}

	/**
	 * Set the content for a content type.
	 * @param contentType - the content type
	 * @param content - the content
	 */
	public void setContent(ContentType contentType, String content) {
		this.content.put(contentType, content);
	}
	
	/**
	 * Load the content for a content type from URL.
	 * @param contentType - the content type
	 * @param url - the URL to load from
	 * @param charset - the charset to be used to load from the URL
	 * @throws IOException when loading the template fails
	 */
	public void setContent(ContentType contentType, URL url, Charset charset) throws IOException {
		if (charset == null) charset = getDefaultCharset();
		if (url != null) setContent(contentType, IOUtils.toString(url, charset));
	}
	
	/**
	 * Load the content for a content type from a {@link File}.
	 * @param contentType - the content type
	 * @param file - the file to load from
	 * @param charset - the charset to be used to load from the file
	 * @throws IOException when loading the template fails
	 */
	public void setContent(ContentType contentType, File file, Charset charset) throws IOException {
		if (charset == null) charset = getDefaultCharset();
		if (file != null) setContent(contentType, IOUtils.toString(new FileInputStream(file), charset));
	}
	
	/**
	 * Load the content for a content type from an {@link InputStream}.
	 * @param contentType - the content type
	 * @param inputStream - the input stream to load from
	 * @param charset - the charset to be used to load from the input stream
	 * @throws IOException when loading the template fails
	 */
	public void setContent(ContentType contentType, InputStream inputStream, Charset charset) throws IOException {
		if (charset == null) charset = getDefaultCharset();
		if (inputStream != null) setContent(contentType, IOUtils.toString(inputStream, charset));
	}
	
	/**
	 * Returns the default charset.
	 * @return the default charset of the system.
	 */
	protected Charset getDefaultCharset() {
		return Charset.defaultCharset();
	}
	
	/**
	 * Load the content for a content type from a {@link Reader}.
	 * @param contentType - the content type
	 * @param reader - the reader to load from
	 * @throws IOException when loading the template fails
	 */
	public void setContent(ContentType contentType, Reader reader) throws IOException {
		if (reader != null) setContent(contentType, IOUtils.toString(reader));
	}
	
	/**
	 * Returns the content for a content type.
	 * @param contentType - the content type
	 * @return the content if it exists
	 */
	public String getContent(ContentType contentType) {
		return this.content.get(contentType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTemplate(ContentType type) {
		return getContent(type);
	}
	
	
}
