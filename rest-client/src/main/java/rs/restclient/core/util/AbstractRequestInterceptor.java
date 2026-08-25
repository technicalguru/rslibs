package rs.restclient.core.util;

import java.io.IOException;

import rs.restclient.core.api.RequestInterceptor;
import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * @author ralph
 *
 */
public class AbstractRequestInterceptor implements RequestInterceptor {

	/**
	 * The main implementation that directs to the intercept methods.
	 */
	@Override
	public final RestResponse intercept(RestRequest request, byte[] body, RestRequestExecution execution) throws IOException {
		intercept(request, body);
		RestResponse rc = execution.execute(request, body);
		intercept(rc);
		return rc;
	}

	/**
	 * The default implementation does nothing.
	 * @param request request before it is being sent
	 * @param body body that shall be sent
	 * @throws IOException when failures occur
	 */
	protected void intercept(RestRequest request, byte[] body) throws IOException {
	}
	
	/**
	 * The default implementation does nothing
	 * @param response the response recieved
	 * @throws IOException when failures occur
	 */
	protected void intercept(RestResponse response) throws IOException {
	}
	

}
