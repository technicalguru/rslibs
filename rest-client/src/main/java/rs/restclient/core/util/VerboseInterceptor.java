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
public class VerboseInterceptor extends AbstractRequestInterceptor {

	private static Logger log = LoggerFactory.getLogger(VerboseInterceptor.class);
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestRequest request) throws IOException {
		log.info("Request:        {} {}", request.getMethod(), request.getTarget().getUri());
		log.info("Request header: {}",    request.getHeaders());
		if (request.getEntity() != null) log.info("Request entity: {}",    request.getEntity());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestRequest request, RestResponse response) throws IOException {
		log.info("");
		log.info("Response:       {}", response);
//		log.debug("Response header: {}", response.getHeaders());
//		try (InputStreamReader in = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)) {
//			String body = new BufferedReader(in).lines().collect(Collectors.joining("\n"));
//			log.debug("Response body:   {}", body);
//		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "VerboseInterceptor []";
	}

	
}
