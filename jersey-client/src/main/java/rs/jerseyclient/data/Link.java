package rs.jerseyclient.data;

/**
 * A link as defined by HATEOAS responses.
 * <p>The class is usually required for correct JSON (de)serialization.</p>
 * @author ralph
 *
 */
public class Link {
	
	private String href;

	/**
	 * Default constructor.
	 */
	public Link() {}
	
	/**
	 * Returns the HREF attribute (URL) of the link.
	 * @return the HREF attribute (URL) of the link.
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Sets the HREF attribute (URL) of the link.
	 * @param href the HREF attribute (URL) of the link.
	 */
	public void setHref(String href) {
		this.href = href;
	}
	
}