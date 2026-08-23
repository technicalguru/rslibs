/**
 * 
 */
package rs.mail.templates;

import rs.mail.templates.impl.ResolverId;

/**
 * Interface for a mail template.
 * 
 * A template provides a template string for a given content type.
 * <p>Note that different translations are provided through different templates. The 
 * build process will only fill in the value objects.</p> 
 * 
 * @author ralph
 *
 */
public interface Template {

	/**
	 * Returns the ID of the template.
	 * @return the id
	 */
	public ResolverId getId();

	/**
	 * Returns the template for the given content type.
	 * <p>Implementations must always return content for {@link ContentType#TEXT} argument.
	 * @param type - the content type to be delivered
	 * @return the template for the given content type or {code null} if not available.
	 */
	public String getTemplate(ContentType type);

}
