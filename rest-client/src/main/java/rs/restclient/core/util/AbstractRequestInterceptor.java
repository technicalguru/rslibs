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
	public final RestResponse intercept(RestRequest request, RestRequestExecution execution) throws IOException {
		intercept(request);
		RestResponse rc = execution.execute(request);
		intercept(request, rc);
		return rc;
	}

	/**
	 * The default implementation does nothing.
	 * @param request request before it is being sent
	 * @param body body that shall be sent
	 * @throws IOException when failures occur
	 */
	protected void intercept(RestRequest request) throws IOException {
	}
	
	/**
	 * The default implementation does nothing
	 * @param request request as it was sent
	 * @param response the response recieved
	 * @throws IOException when failures occur
	 */
	protected void intercept(RestRequest request, RestResponse response) throws IOException {
	}
	

}
