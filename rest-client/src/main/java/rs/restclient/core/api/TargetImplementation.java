/**
 * 
 */
package rs.restclient.core.api;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * A client implementation is the essential
 * object where the request is being executed 
 * and the response object created.
 */
public interface TargetImplementation {

	/**
	 * Executes the request.
	 * <p>The implementation MUST not throw exceptions based on any HTTP status code. Runtime
	 *    Exceptions must be thrown only when technical reasons prevent request or response
	 *    processing.
	 * @param request the request.
	 * @return the response
	 */
	public RestResponse execute(RestRequest request);
}
