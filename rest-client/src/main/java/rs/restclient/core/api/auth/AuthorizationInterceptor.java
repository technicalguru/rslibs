package rs.restclient.core.api.auth;

import java.util.Base64;

import rs.restclient.core.util.AddHeaderInterceptor;

/**
 * Implements simple authorization scheme from fixed user/password or key values.
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
		
		public String getHeaderName() {
			return headerName;
		}
		
		public String getValue(String ...values) {
			if (valueFunction != null) {
				return valueFunction.apply(values);
			}
			return values[0];
		}
	}
	
	@FunctionalInterface
	private static interface ValueFunction {
		
		String apply(String ...values);
	}
	
	public static String encodeAuthorization(String ...values) {
		String rc = values[0];
		if (values.length > 1) rc += ":"+values[1];
		return new String(Base64.getEncoder().encode(rc.getBytes()));
	}
}
