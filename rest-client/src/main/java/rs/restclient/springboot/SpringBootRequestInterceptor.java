package rs.restclient.springboot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import rs.restclient.core.util.LoggingUtils;

/**
 * Logs the request when verbose was configured.
 * @author ralph
 *
 */
public class SpringBootRequestInterceptor implements ClientHttpRequestInterceptor {
	
    private static final Logger log = LoggerFactory.getLogger(SpringBootRequestInterceptor.class);

    private boolean                        verbose;
    private HttpStatusCode                 statusCode;
    private String                         statusMessage;
    private MultiValuedMap<String, String> headers;
    
    /**
     * Constructor.
	 * @param verbose whether to log request and response
	 */
	public SpringBootRequestInterceptor(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Returns the statusCode.
	 * @return the statusCode
	 */
	public HttpStatusCode getStatusCode() {
		return statusCode;
	}

	/**
	 * Returns the statusMessage.
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Returns the headers.
	 * @return the headers
	 */
	public MultiValuedMap<String, String> getHeaders() {
		return headers;
	}

	@Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,  ClientHttpRequestExecution execution) throws IOException {
        if (verbose) logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        return logResponse(request, response);
    }

    private void logRequest(HttpRequest request, byte[] body) {
    	log.info("Request:");
        log.info("   {} {}", request.getMethod(), request.getURI());
    	LoggingUtils.logHeaders(log, convert(request.getHeaders()));
        if (body != null && body.length > 0) {
        	log.info("");
            log.info("   {}", new String(body, StandardCharsets.UTF_8));
        }
        log.info("");
    }

    private ClientHttpResponse logResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
        byte[] responseBody = response.getBody().readAllBytes();
        statusCode    = response.getStatusCode();
        statusMessage = response.getStatusText();
        headers       = convert(response.getHeaders());
        
        if (verbose) {
        	log.info("Response:");
        	log.info("   HTTP/1.0 {}", statusCode);
        	LoggingUtils.logHeaders(log, headers);
        
	        if (responseBody.length > 0) {
	        	log.info("");
	            log.info("   {}", new String(responseBody, StandardCharsets.UTF_8));
	        }
	        log.info("");
        }
        
        // Return wrapped response to allow reading the body again
        return new BufferingClientHttpResponseWrapper(response, responseBody);
    }

    private static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
    	
        private final ClientHttpResponse response;
        private final byte[] body;

        public BufferingClientHttpResponseWrapper(ClientHttpResponse response,  byte[] body) {
            this.response = response;
            this.body = body;
        }

        @Override
        public InputStream getBody() {
            return new ByteArrayInputStream(body);
        }

        // Delegate other methods to wrapped response
        @Override
        public HttpStatusCode getStatusCode() throws IOException {
            return response.getStatusCode();
        }

        /**
		 * {@inheritDoc}
		 */
		@Override
		public String getStatusText() throws IOException {
			return response.getStatusText();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() {
			response.close();
		}

		@Override
        public HttpHeaders getHeaders() {
            return response.getHeaders();
        }
    }

    private static MultiValuedMap<String, String> convert(HttpHeaders headers) {
    	MultiValuedMap<String, String> rc = new ArrayListValuedHashMap<>();
    	for (String header : headers.headerNames()) {
    		for (String value : headers.get(header)) {
    			rc.put(header, value);
    		}
    	}
    	return rc;
    }
}
