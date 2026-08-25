/**
 * 
 */
package rs.restclient.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a client. The API that will be interacted with by your users..
 * <p>Derive all your clients from this class. Your main entry point shall use the
 * {@link #RestClient(TargetImplementation, RestClientConfiguration)} or {@link #RestClient(Target.Builder)}
 * constructor, whereas all sub-clients must use {@link #RestClient(Target)} as constructor.
 */
public abstract class RestClient {

	private   Target target;
	protected Logger log;
	
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
		this.target = target;
		this.log    = LoggerFactory.getLogger(getClass());
	}
	
	/**
	 * Returns the target of this client.
	 * @return the target
	 */
	protected final Target getTarget() {
		return target;
	}
	
	/**
	 * Creates a sub-client based on this client.
	 * @param <T> the client class type
	 * @param clientClass the client class
	 * @return new instance of your sub-client (called with target of this class)
	 */
	protected <T extends RestClient> T createClient(Class<T> clientClass) {
		try {
			return clientClass.getConstructor(Target.class).newInstance(target);
		} catch (Throwable t) {
			throw new RestClientException("Cannot create client.", t);
		}
	}

}
