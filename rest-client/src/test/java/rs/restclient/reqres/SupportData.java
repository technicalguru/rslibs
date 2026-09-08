/**
 * 
 */
package rs.restclient.reqres;

/**
 * Support data in a response.
 */
public class SupportData {

    private String url;
    private String text;
    
    public SupportData() {}

	/**
	 * Returns the url.
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Returns the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "SupportData [url=" + url + ", text=" + text + "]";
	}
    
}
