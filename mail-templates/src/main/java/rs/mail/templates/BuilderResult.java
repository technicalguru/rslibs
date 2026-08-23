/**
 * 
 */
package rs.mail.templates;

import java.util.HashMap;
import java.util.Map;

/**
 * An result of the building process.
 * 
 * @author ralph
 *
 */
public class BuilderResult {

	private String subject;
	private Map<ContentType,String> content;
	
	/**
	 * Default constructor.
	 */
	public BuilderResult() {
		this(null, null, null);
	}
	
	/**
	 * Constructor.
	 * @param subject the result of building the subject string
	 * @param textContent the result of building the text content
	 * @param htmlContent the result of building the HTML content
	 */
	public BuilderResult(String subject, String textContent, String htmlContent) {
		this(subject, null);
		if (textContent != null) setContent(ContentType.TEXT, textContent);
		if (htmlContent != null) setContent(ContentType.HTML, htmlContent);
	}
	
	/**
	 * COnstructor.
	 * @param subject the result of building the subject string
	 * @param content the results of building the various content types
	 */
	public BuilderResult(String subject, Map<ContentType,String> content) {
		this.subject = subject;
		this.content = content != null ? content : new HashMap<>();
	}

	/**
	 * Returns the subject.
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Returns the content.
	 * @return the content
	 */
	public Map<ContentType, String> getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * @param content the content to set
	 */
	public void setContent(Map<ContentType, String> content) {
		this.content = content != null ? content : new HashMap<>();
	}
	
	/**
	 * Sets the content for a specific content type.
	 * @param contentType - the content type
	 * @param content - the content to set.
	 */
	public void setContent(ContentType contentType, String content) {
		this.content.put(contentType, content);
	}
	
	/**
	 * Returns the content for a given content type.
	 * @param contentType - the content type to retrieve
	 * @return the content or {@code null} if not available
	 */
	public String getContent(ContentType contentType) {
		return this.content.get(contentType);
	}
}
