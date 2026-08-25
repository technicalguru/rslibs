/**
 * 
 */
package rs.restclient.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;
import rs.restclient.core.util.AbstractRequestInterceptor;

/**
 * Logs request and response.
 */
public class LoggingFilter extends AbstractRequestInterceptor {

	private static Logger log = LoggerFactory.getLogger(LoggingFilter.class);
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestRequest request) throws IOException {
		log.debug("Request:        {} {}", request.getMethod(), request.getTarget().getUri());
		log.debug("Request header: {}",    request.getHeaders());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestRequest request, RestResponse response) throws IOException {
		log.debug("");
		log.debug("Response:        {}", response);
//		log.debug("Response header: {}", response.getHeaders());
//		try (InputStreamReader in = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)) {
//			String body = new BufferedReader(in).lines().collect(Collectors.joining("\n"));
//			log.debug("Response body:   {}", body);
//		}
	}

}
