/**
 * 
 */
package rs.jerseyclient.data;

import java.util.List;


/**
 * The response according to HATEOAS paged responses.
 * <p>The class is usually required for correct JSON (de)serialization.</p>
 * 
 * @param <T> the type of result
 *
 * @author ralph
 *
 */
public class HateOasPagedList<T> extends AbstractData {

	private EmbeddedResultList<T> _embedded;
	private PageInfo page;
	
	/**
	 * Default constructor.
	 */
	public HateOasPagedList() {}
	
	/**
	 * Returns the embedded result list.
	 * @return the embedded result list
	 */
	public EmbeddedResultList<T> get_embedded() {
		return _embedded;
	}

	/**
	 * Returns the page information object
	 * @return the page information object.
	 */
	public PageInfo getPage() {
		return page;
	}

	/**
	 * Sets the embedded result list.
	 * @param _embedded the embedded result list
	 */
	public void set_embedded(EmbeddedResultList<T> _embedded) {
		this._embedded = _embedded;
	}

	/**
	 * Sets the page information object
	 * @param page the page information object.
	 */
	public void setPage(PageInfo page) {
		this.page = page;
	}

	/**
	 * Embedded class for the result list.
	 * 
	 * @author ralph
	 *
	 * @param <T> the type of result
	 */
	public static class EmbeddedResultList<T> {
		
		private List<T> results;

		/**
		 * Returns the result list.
		 * @return the result list.
		 */
		public List<T> getResults() {
			return results;
		}

		/**
		 * Sets the result list.
		 * @param results the result list
		 */
		public void setResults(List<T> results) {
			this.results = results;
		}
		
	}
}
