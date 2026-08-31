/**
 * 
 */
package rs.restclient.core.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * Logs request and response.
 */
public class LoggingInterceptor extends AbstractRequestInterceptor {

	private static Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestRequest request) throws IOException {
		log.info("");;
		log.info("Request:");
		log.info("   {} {}", request.getMethod(), request.getUri());
		log.info("   {}",    request.getHeaders());
		if (request.getEntity() != null) {
			log.info("");;
			log.info("   {}",    request.getEntity());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestRequest request, RestResponse response) throws IOException {
		log.info("");;
		log.info("Response:");
		log.info("   {} {}", response.getStatusCode(), response.getStatusMessage());
		LoggingUtils.logHeaders(log, response.getHeaders());
		log.info("");
		log.info("   {}", response.getBody());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "VerboseInterceptor []";
	}

	
}
