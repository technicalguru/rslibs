/**
 * 
 */
package rs.mail.templates.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import rs.mail.templates.ContentType;
import rs.mail.templates.Template;

/**
 * Abstract base implementation for templates.
 * 
 * @author ralph
 *
 */
public abstract class AbstractTemplate implements Template {

	private Map<ContentType, String>    content = new HashMap<>();
	
	/**
	 * Default constructor.
	 */
	protected AbstractTemplate() {
		addContent(ContentType.TEXT, "<not-set>");
	}
	
	/**
	 * Adds the template for the specified content type.
	 * @param contentType - the content type
	 * @param template - the template for the content type
	 */
	protected void addContent(ContentType contentType, CharSequence template) {
		content.put(contentType, template.toString());
	}
	
	/**
	 * Adds the template for the specified content type.
	 * @param contentType - the content type
	 * @param template - the template file for the content type
	 * @param charset -  the name of the requested charset, null means platform default
	 * @throws IOException - when the file cannot be read
	 */
	protected void addContent(ContentType contentType, File template, Charset charset) throws IOException {
		addContent(contentType, FileUtils.readFileToString(template, charset));
	}
	
	/**
	 * Adds the template for the specified content type.
	 * <p>This method buffers the input internally, so there is no need to use a BufferedInputStream.</p>
	 * @param contentType - the content type
	 * @param template - the template input stream for the content type
	 * @param charset -  the name of the requested charset, null means platform default
	 * @throws IOException - when the input stream cannot be read
	 */
	protected void addContent(ContentType contentType, InputStream template, Charset charset) throws IOException {
		addContent(contentType, IOUtils.toString(template, charset));
	}
	
	/**
	 * Adds the template for the specified content type.
	 * @param contentType - the content type
	 * @param template - the URI of the template for the content type
	 * @param encoding - The encoding name for the URI contents.
	 * @throws IOException - when the URI cannot be requested and read
	 */
	protected void addContent(ContentType contentType, URI template, Charset encoding) throws IOException {
		addContent(contentType, IOUtils.toString(template, encoding));
	}
	
	/**
	 * Adds the template for the specified content type.
	 * @param contentType - the content type
	 * @param template - the URL of the template for the content type
	 * @param encoding - The encoding name for the URL contents.
	 * @throws IOException - when the URL cannot be requested and read
	 */
	protected void addContent(ContentType contentType, URL template, Charset encoding) throws IOException {
		addContent(contentType, IOUtils.toString(template, encoding));
	}
	
	/**
	 * Adds the template for the specified content type.
	 * <p>This method buffers the input internally, so there is no need to use a BufferedReader.</p>
	 * @param contentType - the content type
	 * @param template - the template reader for the content type
	 * @throws IOException - when the reader produces an error
	 */
	protected void addContent(ContentType contentType, Reader template) throws IOException {
		addContent(contentType, IOUtils.toString(template));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTemplate(ContentType type) {
		return content.get(type);
	}
	
}
