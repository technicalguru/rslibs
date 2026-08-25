/**
 * 
 */
package rs.restclient.core.api;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * A client implementation is the main entrance point
 * for a Client. It can produce the 
 * builders for the specific implementation details.
 */
public interface TargetImplementation {

	/**
	 * Executes the request.
	 * @param request the request.
	 * @return the response
	 */
	public RestResponse execute(RestRequest request);
}
