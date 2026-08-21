package rs.jerseyclient.data;

/**
 * The page information object as defined in HATEOAS paged response.
 * <p>The class is usually required for correct JSON (de)serialization.</p>
 * 
 * @author ralph
 *
 */
public class PageInfo {
	
	private int size;
	private int totalElements;
	private int totalPages;
	private int number;
	
	/**
	 * Default constructor.
	 */
	public PageInfo() {}
	
	/**
	 * Returns the size of a page.
	 * @return the size of a page
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Returns the total number of results available.
	 * @return the total number of results available
	 */
	public int getTotalElements() {
		return totalElements;
	}
	
	/**
	 * Returns the number of pages available.
	 * @return the number of pages available
	 */
	public int getTotalPages() {
		return totalPages;
	}
	
	/**
	 * Returns the number of this page.
	 * @return the number of this page
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the size of a page.
	 * @param size - the size of a page
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Sets the total number of results available.
	 * @param totalElements - the total number of results available
	 */
	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	/**
	 * Sets the number of pages available.
	 * @param totalPages - the number of pages available
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * Sets this page number.
	 * @param number - this page number
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
}