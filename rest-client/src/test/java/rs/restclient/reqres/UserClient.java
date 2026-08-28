/**
 * 
 */
package rs.restclient.reqres;

import rs.restclient.core.api.RestClient;
import rs.restclient.core.api.Target;
import rs.restclient.core.api.request.Entity;
import rs.restclient.core.api.request.MediaType;
import tools.jackson.core.type.TypeReference;

/**
 * The client that fetches users.
 * Demonstrates usage of sub client.
 */
public class UserClient extends RestClient {

	public UserClient(Target parent) {
		super(parent.path("users"));
	}
	
	public User get(long id) {
		return getTarget().path(""+id).request().get().as(new TypeReference<ReqResResponse<User>>() {}).getData();
	}
	
	public User create(User user) {
		Entity<User> entity = Entity.entity(user, MediaType.APPLICATION_JSON);
		return getTarget().request().post(entity).as(User.class);
	}
}
