/**
 * 
 */
package rs.jerseyclient.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Logs request and response.
 */
public class LoggingFilter extends AbstractFilter {

	private static Logger log = LoggerFactory.getLogger(LoggingFilter.class);
	
	@Override
	protected void intercept(HttpRequest request, byte[] body) throws IOException {
		log.debug("Request:        {} {}", request.getMethod(), request.getURI());
		log.debug("Request header: {}",    request.getHeaders());
		log.debug("Request body:   {}",    new String(body, StandardCharsets.UTF_8));
	}

	@Override
	protected void intercept(ClientHttpResponse response) throws IOException {
		log.debug("");
		log.debug("Response:        {} {}", response.getStatusCode(), response.getStatusText());
		log.debug("Response header: {}",    response.getHeaders());
		try (InputStreamReader in = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)) {
			String body = new BufferedReader(in).lines().collect(Collectors.joining("\n"));
			log.debug("Response body:   {}", body);
		}
		
	}

	
}
