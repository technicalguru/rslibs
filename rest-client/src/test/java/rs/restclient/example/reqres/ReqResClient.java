/**
 * 
 */
package rs.restclient.example.reqres;

import rs.restclient.core.api.RestClient;
import rs.restclient.core.api.Target;

/**
 * Demonstrates how to create a main client (at parent target)
 */
public class ReqResClient extends RestClient {

	public ReqResClient(Target.Builder targetBuilder) {
		super(targetBuilder);
	}
	
	public UserClient users() {
		return createClient(UserClient.class);
	}
}
