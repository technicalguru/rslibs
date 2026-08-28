/**
 * 
 */
package rs.restclient.reqres;

/**
 * 
 */
public class MetaData {

	private String  poweredBy;
	private String  docs_url;
	private String  upgrade_url;
	private String  example_url;
	private String  variant;
	private String  message;
	private String  context;
	private CtaData cta;

	public MetaData() {}

	/**
	 * Returns the poweredBy.
	 * @return the poweredBy
	 */
	public String getPoweredBy() {
		return poweredBy;
	}

	/**
	 * Sets the poweredBy.
	 * @param poweredBy the poweredBy to set
	 */
	public void setPoweredBy(String poweredBy) {
		this.poweredBy = poweredBy;
	}

	/**
	 * Returns the docs_url.
	 * @return the docs_url
	 */
	public String getDocs_url() {
		return docs_url;
	}

	/**
	 * Sets the docs_url.
	 * @param docs_url the docs_url to set
	 */
	public void setDocs_url(String docs_url) {
		this.docs_url = docs_url;
	}

	/**
	 * Returns the upgrade_url.
	 * @return the upgrade_url
	 */
	public String getUpgrade_url() {
		return upgrade_url;
	}

	/**
	 * Sets the upgrade_url.
	 * @param upgrade_url the upgrade_url to set
	 */
	public void setUpgrade_url(String upgrade_url) {
		this.upgrade_url = upgrade_url;
	}

	/**
	 * Returns the example_url.
	 * @return the example_url
	 */
	public String getExample_url() {
		return example_url;
	}

	/**
	 * Sets the example_url.
	 * @param example_url the example_url to set
	 */
	public void setExample_url(String example_url) {
		this.example_url = example_url;
	}

	/**
	 * Returns the variant.
	 * @return the variant
	 */
	public String getVariant() {
		return variant;
	}

	/**
	 * Sets the variant.
	 * @param variant the variant to set
	 */
	public void setVariant(String variant) {
		this.variant = variant;
	}

	/**
	 * Returns the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the context.
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Sets the context.
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * Returns the cta.
	 * @return the cta
	 */
	public CtaData getCta() {
		return cta;
	}

	/**
	 * Sets the cta.
	 * @param cta the cta to set
	 */
	public void setCta(CtaData cta) {
		this.cta = cta;
	}

	@Override
	public String toString() {
		return "MetaData [poweredBy=" + poweredBy + ", docs_url=" + docs_url + ", upgrade_url=" + upgrade_url
				+ ", example_url=" + example_url + ", variant=" + variant + ", message=" + message + ", context="
				+ context + ", cta=" + cta + "]";
	}
	
	
}
