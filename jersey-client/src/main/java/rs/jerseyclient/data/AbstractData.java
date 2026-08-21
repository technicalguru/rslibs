/**
 * 
 */
package rs.jerseyclient.data;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract base for data classes providing the HATEOAS field {@code _links}.
 * <p>The class is usually required for correct JSON (de)serialization.</p>
 * 
 * @author ralph
 *
 */
public abstract class AbstractData {

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Map<String,Link> _links;

	/**
	 * Default constructor.
	 */
	public AbstractData() {}
	
	/**
	 * Returns the links map.
	 * @return the links map
	 */
	public Map<String, Link> get_links() {
		return _links;
	}

	/**
	 * Sets the links map.
	 * @param _links the links map
	 */
	public void set_links(Map<String, Link> _links) {
		this._links = _links;
	}

	
}
