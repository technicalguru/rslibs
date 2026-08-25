package rs.restclient.core.api;

import java.io.IOException;

import org.springframework.util.Assert;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;
import rs.restclient.core.util.RestRequestExecution;

/**
 * Intercept requests.
 * 
 * @author ralph
 *
 */
@FunctionalInterface
public interface RequestInterceptor {

	/**
	 * Intercept the request and modify it if required
	 * @param request the request
	 * @param body the body (if available)
	 * @param execution the execution
	 * @return the response
	 * @throws IOException when an error occurs
	 */
	RestResponse intercept(RestRequest request, byte[] body, RestRequestExecution execution) throws IOException;
	
	/**
	 * Return a new interceptor that invokes {@code this} interceptor first, and
	 * then the one that's passed in.
	 * @param interceptor the next interceptor
	 * @return a new interceptor that chains the two
	 * @since 7.0
	 */
	default RequestInterceptor andThen(RequestInterceptor interceptor) {
		Assert.notNull(interceptor, "RequestInterceptor must not be null");
		return (request, body, execution) -> {
			RestRequestExecution nextExecution =
					(nextRequest, nextBody) -> interceptor.intercept(nextRequest, nextBody, execution);
			return intercept(request, body, nextExecution);
		};
	}

	/**
	 * Return a new execution that invokes {@code this} interceptor, and then
	 * delegates to the given execution.
	 * @param execution the execution to delegate to
	 * @return a new execution instance
	 * @since 7.0
	 */
	default RestRequestExecution apply(RestRequestExecution execution) {
		Assert.notNull(execution, "RestRequestExecution must not be null");
		return (request, body) -> intercept(request, body, execution);
	}

}
