/**
 * 
 */
package rs.restclient.reqres;

/**
 * The main class a response is coming as.
 */
public class ReqResResponse<T> {

	private T           data;
	private SupportData support;
	private MetaData    meta;
	
	public ReqResResponse() {}

	/**
	 * Returns the data.
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Returns the support.
	 * @return the support
	 */
	public SupportData getSupport() {
		return support;
	}

	/**
	 * Sets the support.
	 * @param support the support to set
	 */
	public void setSupport(SupportData support) {
		this.support = support;
	}

	/**
	 * Returns the meta.
	 * @return the meta
	 */
	public MetaData getMeta() {
		return meta;
	}

	/**
	 * Sets the meta.
	 * @param meta the meta to set
	 */
	public void setMeta(MetaData meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		return "ReqResResponse [data=" + data + ", support=" + support + ", meta=" + meta + "]";
	}
	
	
}
