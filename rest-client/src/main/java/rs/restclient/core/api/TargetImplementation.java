/**
 * 
 */
package rs.restclient.core.api;

/**
 * A client implementation is the main entrance point
 * for a Client. It can produce the 
 * builders for the specific implementation details.
 */
public interface TargetImplementation {

	/**
	 * Returns the request builder
	 * @return the request builder
	 */
	public RequestBuilder requestBuilder();
}
