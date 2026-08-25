/**
 * 
 */
package rs.restclient.example.reqres;

/**
 * CTA in Meta Data response.
 */
public class CtaData {

    private String label;
    private String url;

    public CtaData() {}

	/**
	 * Returns the label.
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

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

	@Override
	public String toString() {
		return "CtaData [label=" + label + ", url=" + url + "]";
	}
    
    
}
