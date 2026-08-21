/**
 * 
 */
package rs.jerseyclient.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.client.WebTarget;

/**
 * A helper class to maintain (sub) clients within a client.
 * 
 * @author ralph
 *
 */
public class ClientHolder {

	private Map<Class<?>, Object> clients;
	private WebTarget             target;
	
	/**
	 * Constructor.
	 * @param target - the target this object serves the subclients for
	 */
	public ClientHolder(WebTarget target) {
		this.target = target;
		clients  = new HashMap<>();
	}

	/**
	 * Returns the subclient of the given type.
	 * <p>Be aware that subclients use relative REST API paths.</p>
	 * @param <T>   - Type of subclient
	 * @param clazz - class of subclient
	 * @return new or existing instance of subclient
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractClient> T get(Class<T> clazz) {
		T rc = (T)clients.get(clazz);
		if (rc == null) {
			try {
				Constructor<T> c = clazz.getConstructor(WebTarget.class);
				rc = c.newInstance(target);
				clients.put(clazz, rc);
			} catch (Exception e) {
				throw new RuntimeException("Cannot create subclient", e);
			}
		}
		return rc;
	}

	/**
	 * Returns the target.
	 * @return the target
	 */
	public WebTarget getTarget() {
		return target;
	}

}