/**
 * 
 */
package rs.restclient.util;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Abstract Implementation of a filter so it is easier to manage requests and responses.
 */
public abstract class AbstractFilter implements ClientHttpRequestInterceptor {

	/**
	 * The main implementation that directs to the intercept methods.
	 */
	@Override
	public final ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		intercept(request, body);
		ClientHttpResponse rc = execution.execute(request, body);
		intercept(rc);
		return rc;
	}

	/**
	 * The default implementation does nothing.
	 * @param request request before it is being sent
	 * @param body body that shall be sent
	 * @throws IOException when failures occur
	 */
	protected void intercept(HttpRequest request, byte[] body) throws IOException {
	}
	
	/**
	 * The default implementation does nothing
	 * @param response the response recieved
	 * @throws IOException when failures occur
	 */
	protected void intercept(ClientHttpResponse response) throws IOException {
	}
	
}
