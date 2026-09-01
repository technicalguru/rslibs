package rs.restclient.core.api.auth;

import java.util.Base64;

import rs.restclient.core.util.AddHeaderInterceptor;

/**
 * Implements simple authorization scheme from fixed user/password or key values.
 * <p>You shall use the {@link FixedHeaderAuthorizationStrategy} instead of this
 * interceptor. However, you can build your own strategies and use this interceptor
 * for implementing it.
 * @author ralph
 *
 */
public class AuthorizationInterceptor extends AddHeaderInterceptor {

	/**
	 * Convenience constructor.
	 * @param authorizationType the type of authorization
	 * @param values values for authorization type (see your type for further details)
	 */
	public AuthorizationInterceptor(AuthorizationType authorizationType, String ...values) {
		super(authorizationType.getHeaderName(), authorizationType.getValue(values));
	}

	/**
	 * Authorization Type.
	 * @author ralph
	 *
	 */
	public static enum AuthorizationType {
		
		/** Basic Authorization Scheme. Arguments are either "user:password" or "user" and "password" */
		BASIC_AUTH("Authorization",       (values) -> "Basic "+encodeAuthorization(values)),
		/** Proxy Authorization Scheme. Arguments are either "user:password" or "user" and "password" */
		PROXY_AUTH("Proxy-Authorization", (values) -> "Basic "+encodeAuthorization(values)),
		/** Bearer Token Authorization. Single argument is the token itself */
		BEARER_TOKEN("Authorization",       (values) -> "Bearer "+values[0]),
		/** Simple API-Key. Argument is a single value to be sent. Any encoding must be applied by the caller. */
		X_API_KEY("X-Api-Key"),
		/** Simple API-Key. Argument is a single value to be sent. Any encoding must be applied by the caller. */
		API_KEY("Api-Key"),
		/** Simple Token Authorization. Argument is a single value to be sent. Any encoding must be applied by the caller. */
		X_AUTH_TOKEN("X-Auth-Token"),
		/** Simple Token Authorization. Argument is a single value to be sent. Any encoding must be applied by the caller. */
		X_SESSION_TOKEN("X-Session-Token");
		
		private String        headerName;
		private ValueFunction valueFunction;

		private AuthorizationType(String headerName) {
			this(headerName, null);
		}
		
		private AuthorizationType(String headerName, ValueFunction valueFunction) {
			this.headerName    = headerName;
			this.valueFunction = valueFunction;
		}
		
		/**
		 * Returns the header name to be used.
		 */
		public String getHeaderName() {
			return headerName;
		}
		
		/**
		 * Produces the header value from given value(s).
		 * @param values values to be used in the header value
		 * @return the header value
		 */
		public String getValue(String ...values) {
			if (valueFunction != null) {
				return valueFunction.apply(values);
			}
			return values[0];
		}
	}
	
	/**
	 * The function that makes the header value out of the values
	 * for a specific {@link AuthorizationType}.
	 */
	@FunctionalInterface
	private static interface ValueFunction {
		/**
		 * Convert th evalue(s) to the header value.
		 * @param values values to be used and optionally encoded
		 * @return the correct header value to be used
		 */
		String apply(String ...values);
	}
	
	/**
	 * Base64 encoding of first two values.
	 * @param values values (max 2)
	 * @return the encoded Base64 encoding of the complete Authorization value
	 */
	public static String encodeAuthorization(String ...values) {
		String rc = values[0];
		if (values.length > 1) rc += ":"+values[1];
		return new String(Base64.getEncoder().encode(rc.getBytes()));
	}
}
