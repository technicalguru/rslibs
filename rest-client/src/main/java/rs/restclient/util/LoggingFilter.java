/**
 * 
 */
package rs.restclient.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
	protected void intercept(RestRequest request, byte[] body) throws IOException {
		log.debug("Request:        {} {}", request.getMethod(), request.getTarget().getUri());
		log.debug("Request header: {}",    request.getHeaders());
		log.debug("Request body:   {}",    new String(body, StandardCharsets.UTF_8));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestResponse response) throws IOException {
		log.debug("");
		log.debug("Response:        {}", response);
//		log.debug("Response header: {}", response.getHeaders());
//		try (InputStreamReader in = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)) {
//			String body = new BufferedReader(in).lines().collect(Collectors.joining("\n"));
//			log.debug("Response body:   {}", body);
//		}
	}

}
