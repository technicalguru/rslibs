/**
 * 
 */
package rs.restclient.core.api;

import java.lang.reflect.Constructor;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.restclient.core.api.auth.AuthorizationStrategy;

/**
 * Abstract implementation of a client. The API that will be interacted with by your users..
 * <p>Derive all your clients from this class. Your main entry point shall use the
 * {@link #RestClient(TargetImplementation, RestClientConfiguration)} or {@link #RestClient(Target.Builder)}
 * constructor, whereas all sub-clients must use {@link #RestClient(Target)} as constructor.
 */
public abstract class RestClient {

	private   Target                target;
	protected Logger                log;
	
	/**
	 * The main constructor for your main client implementation. This client has no parent.
	 * @param implementation specific implementation to be used
	 * @param configuration  configuration of your client.
	 */
	protected RestClient(TargetImplementation implementation, RestClientConfiguration configuration) {
		this(Target.builder().with(implementation).with(configuration));
	}
	
	/**
	 * The main constructor for your main client implementation. This client has no parent.
	 * @param targetBuilder the builder for the main target.
	 */
	protected RestClient(Target.Builder targetBuilder) {
		this(targetBuilder.build());
	}
	
	/**
	 * Constructor for all sub-clients.
	 * <p>When deriving, call this with a specific sub-path, e.g.
	 * <pre>
	 *
	 *    MySubClient(Target target) {
	 *       super(target.path("users"));
	 *    }
	 *
	 * </pre>
	 * 
	 * @param target the target to be used.
	 */
	protected RestClient(Target target) {
		if (target == null) throw new RestClientException("target must not be null");
		this.target                = target;
		this.log                   = LoggerFactory.getLogger(getClass());
		if (target.getAuthorizationStrategy() == null) {
			target.setAuthorizationStrategy(createAuthorizationStrategy());
		}
	}
	
	/**
	 * Creates the authorization strategy for this client.
	 * <p>Notice that a strategy is automatically propagated to its sub-targets. That's why
	 * you only need to create your strategy in your main client. If your strategy is
	 * different for any sub-client, then call {@link Target#setAuthorizationStrategy(AuthorizationStrategy)}
	 * in that client's constructor.
	 * <p>Default implementation returns NULL.
	 * @return
	 */
	protected AuthorizationStrategy createAuthorizationStrategy() {
		return null;
	}
	
	/**
	 * Returns the target of this client.
	 * @return the target
	 */
	public final Target getTarget() {
		return target;
	}
	
	/**
	 * Creates a sub-client based on this client.
	 * @param <T> the client class type
	 * @param clientClass the client class
	 * @return new instance of your sub-client (called with target of this class)
	 */
	protected <T extends RestClient> T createClient(Class<T> clientClass) {
		// TODO check whether we already have such a client
		try {
			return clientClass.getConstructor(Target.class).newInstance(target);
		} catch (Throwable t) {
			throw new RestClientException("Cannot create client.", t);
		}
	}

	public static <T extends RestClient> Builder<T> builder(Class<T> clientClass) {
		return new Builder<>(clientClass);
	}
	
	public static class Builder<T extends RestClient> {
		
		private Target.Builder targetBuilder;
		private Class<T>       clientClass;
		
		private Function<RestClient, AuthorizationStrategy> authorizationFunction;
		
		/**
		 * Create the builder with this target builder.
		 * @param targetBuilder
		 */
		protected Builder(Class<T> clientClass) {
			this.clientClass   = clientClass;
		}

		public Builder<T> with(Target.Builder targetBuilder) {
			this.targetBuilder = targetBuilder;
			return this;
		}
		
		public Builder<T> withAuthorization(Function<RestClient, AuthorizationStrategy> authorizationFunction) {
			this.authorizationFunction = authorizationFunction;
			return this;
		}
		
		/**
		 * Builds the client using {@link #buildClient()} and configures
		 * {@link AuthorizationStrategy} for it if required.
		 * @return the client built and configured
		 */
		public T build() {
			T rc = buildClient();
			
			// Check if we have an authorization strategy set.
			Target target = rc.getTarget();
			if (target.getAuthorizationStrategy() == null) {
				if (authorizationFunction != null) {
					target.setAuthorizationStrategy(authorizationFunction.apply(rc));
				}
			}
			return rc;
		}
		
		/**
		 * Builds the client.
		 * @return the client built from target builder
		 */
		protected T buildClient() {
			try {
				Constructor<T> constructor = clientClass.getConstructor(Target.Builder.class);
				return constructor.newInstance(targetBuilder);
			} catch (Throwable t) {
				throw new RestClientException("Cannot create RestClient", t);
			}
		}
	}
}
