package rs.restclient.springboot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Logs the request when verbose was configured.
 * @author ralph
 *
 */
public class VerboseRequestInterceptor implements ClientHttpRequestInterceptor {
	
    private static final Logger log = LoggerFactory.getLogger(VerboseRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,  ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        var response = execution.execute(request, body);
        return logResponse(request, response);
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.info("Request: {} {}", request.getMethod(), request.getURI());
        logHeaders(request.getHeaders(), "Request Headers");
        if (body != null && body.length > 0) {
            log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
        }
    }

    private ClientHttpResponse logResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
        log.info("Response status: {}", response.getStatusCode());
        logHeaders(response.getHeaders(), "Response Headers");
        
        byte[] responseBody = response.getBody().readAllBytes();
        if (responseBody.length > 0) {
            log.info("Response body: {}", 
                new String(responseBody, StandardCharsets.UTF_8));
        }
        
        // Return wrapped response to allow reading the body again
        return new BufferingClientHttpResponseWrapper(response, responseBody);
    }

    private void logHeaders(HttpHeaders headers, String title) {
    	log.info("{}:", title);
    	for (String header : headers.headerNames()) {
			if (!":status".equals(header)) {
	    		for (String value : headers.get(header)) {
	    			log.info("    {}: {}", header, value);
	    		}
			}
    	}
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

}
