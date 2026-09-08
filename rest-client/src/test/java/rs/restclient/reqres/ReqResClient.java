/**
 * 
 */
package rs.restclient.reqres;

import rs.restclient.core.api.RestClient;
import rs.restclient.core.api.Target;
import rs.restclient.core.api.auth.AuthorizationInterceptor.AuthorizationType;
import rs.restclient.core.api.auth.FixedHeaderAuthorizationStrategy;
import rs.restclient.core.api.request.Entity;
import rs.restclient.core.api.request.MediaType;

/**
 * Demonstrates how to create a main client.
 */
public class ReqResClient extends RestClient {

	public ReqResClient(Target.Builder targetBuilder) {
		super(targetBuilder);
	}

	@Override
	protected Target configureTarget(Target target) {
		return target.authorizationStrategy(new FixedHeaderAuthorizationStrategy(AuthorizationType.X_API_KEY, null));
	}
	
	public void setApiKey(String apiKey) {
		((FixedHeaderAuthorizationStrategy)getTarget().authorizationStrategy()).setValue(apiKey);
	}
	
	public UserClient users() {
		return createClient(UserClient.class);
	}

	public User httpbin(User user) {
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		return getTarget().request().queryParam("show_env", 1).post(entity).as(User.class);
	}

}
