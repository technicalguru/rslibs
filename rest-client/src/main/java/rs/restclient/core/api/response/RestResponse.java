/**
 * 
 */
package rs.restclient.core.api.response;

import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;

import rs.jackson.Json;
import rs.jackson.Json2;
import rs.restclient.core.api.request.RestRequest;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;

/**
 * A wrapper for the response.
 */
public class RestResponse {

//	private Target                         target;
	private RestRequest                    request;
	private MultiValuedMap<String, String> headers;
	private int                            statusCode;
	private String                         statusMessage;
	private Optional<String>               body;

	private JsonMapper                     jsonMapper;
	private com.fasterxml.jackson.databind.json.JsonMapper jsonMapper2;
	private Json                           json;
	private Json2                          json2;

	protected RestResponse(RestRequest request, MultiValuedMap<String, String> headers, int statusCode, String statusMessage, Optional<String> body) {
		this.request       = request;
		this.headers       = headers;
		this.statusCode    = statusCode;
		this.statusMessage = statusMessage;
		this.body          = body;
	}
	
	/**
	 * Returns the request.
	 * @return the request
	 */
	public RestRequest getRequest() {
		return request;
	}

	/**
	 * Returns the headers.
	 * @return the headers
	 */
	public final MultiValuedMap<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Returns the statusCode.
	 * @return the statusCode
	 */
	public final int getStatusCode() {
		return statusCode;
	}

	/**
	 * Returns the statusMessage.
	 * @return the statusMessage
	 */
	public final String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Returns the body.
	 * @return the body or null if no body is available.
	 */
	public final String getBody() {
		return body.orElse(null);
	}
	
	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(Class<T> responseType) {
		return getJson().fromJson(getBody(), responseType);
	}
	
	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(JavaType responseType) {
		return getJson().fromJson(getBody(), responseType);
	}
	
	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(TypeReference<T> responseType) {
		return getJson().fromJson(getBody(), responseType);
	}

	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(com.fasterxml.jackson.databind.JavaType responseType) {
		return getJson2().fromJson(getBody(), responseType);
	}
	
	/**
	 * Returns the response as given type.
	 * @param <T> the type to return
	 * @param responseType the response type
	 * @return the response as given type
	 */
	public <T> T as(com.fasterxml.jackson.core.type.TypeReference<T> responseType) {
		return getJson2().fromJson(getBody(), responseType);
	}

	/**
	 * Returns the Json object (Jackson 3) for mapping ease of use.
	 * @return the Json object (Jackson 3)
	 */
	public Json getJson() {
		if (json == null) {
			Json.Builder builder = Json.builder();
			JsonMapper   mapper  = getJsonMapper();
			if (mapper != null) builder = builder.with(mapper);
			json = builder.build();
		}
		return json;
	}

	/**
	 * Returns the Json object (Jackson 2) for mapping ease of use.
	 * @return the Json object (Jackson 2)
	 */
	public Json2 getJson2() {
		if (json2 == null) {
			Json2.Builder builder = Json2.builder();
			com.fasterxml.jackson.databind.json.JsonMapper   mapper2  = getJsonMapper2();
			if (mapper2 != null) builder = builder.with(mapper2);
			json2 = builder.build();
		}
		return json2;
	}

	/**
	 * Returns the jsonMapper.
	 * @return the jsonMapper
	 */
	public JsonMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = getRequest().getConfiguration().getMapper();
		}
		return jsonMapper;
	}

	/**
	 * Returns the jsonMapper.
	 * @return the jsonMapper
	 */
	public com.fasterxml.jackson.databind.json.JsonMapper getJsonMapper2() {
		if (jsonMapper2 == null) {
			jsonMapper2 = getRequest().getConfiguration().getMapper2();
		}
		return jsonMapper2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "RestResponse [request=" + getRequest() + ", statusCode=" + getStatusCode() + ", statusMessage=" + getStatusMessage() +
				", headers=" + getHeaders() + ", body=" + getBody() + "]";
	}

	/**
	 * Creates the builder for a response.
	 * @return the response builder
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Response builder class.
	 */
	public static class Builder {
		
		private RestRequest                    request;
		private MultiValuedMap<String, String> headers;
		private Integer                        statusCode;
		private String                         statusMessage;
		private Optional<String>               body;
		
		protected Builder() {
		}
		
		/**
		 * Assign the given request object.
		 * @param request the request object
		 * @return this builder for method chaining
		 */
		public Builder with(RestRequest request) {
			this.request = request;
			return this;
		}
		
		/**
		 * Assign the given response headers.
		 * @param headers the headers from the response
		 * @return this builder for method chaining
		 */
		public Builder with(MultiValuedMap<String, String> headers) {
			this.headers = headers;
			return this;
		}
		
		/**
		 * Assign the given response status.
		 * @param statusCode status code, e.g. 200
		 * @param statusMessage status message, e.g. "OK"
		 * @return this builder for method chaining
		 */
		public Builder withStatus(int statusCode, String statusMessage) {
			this.statusCode    = statusCode;
			this.statusMessage = statusMessage;
			return this;
		}
		
		/**
		 * Assign the given response body.
		 * @param body the response body
		 * @return this builder for method chaining
		 */
		public Builder with(Optional<String> body) {
			this.body = body;
			return this;
		}
		
		/**
		 * Build the response object.
		 * @return the response built from previous configuration
		 */
		public RestResponse build() {
			return new RestResponse(request, headers, statusCode, statusMessage, body);
		}
	}
}
