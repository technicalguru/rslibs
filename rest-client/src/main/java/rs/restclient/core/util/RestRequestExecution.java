package rs.restclient.core.util;

import java.io.IOException;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * Implements the interceptor chaining.
 * @author ralph
 *
 */
@FunctionalInterface
public interface RestRequestExecution {

	/**
	 * Pass the request to next interceptor and return the reponse when done.
	 * @param request request to be executed
	 * @return the response
	 * @throws IOException when request execution failed
	 */
	RestResponse execute(RestRequest request) throws IOException;
}
