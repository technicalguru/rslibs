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

	RestResponse execute(RestRequest request, byte[] body) throws IOException;
}
