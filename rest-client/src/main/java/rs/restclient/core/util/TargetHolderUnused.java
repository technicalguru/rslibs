/**
 * 
 */
package rs.restclient.core.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import rs.restclient.core.api.RestClient;
import rs.restclient.core.api.Target;

/**
 * A helper class to maintain (sub) clients within a client.
 * 
 * @author ralph
 *
 */
public class TargetHolderUnused {

	private Map<Class<?>, Object> clients;
	private Target                target;
	
	/**
	 * Constructor.
	 * @param target - the target this object serves the subclients for
	 */
	public TargetHolderUnused(Target target) {
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
	public <T extends RestClient> T get(Class<T> clazz) {
		T rc = (T)clients.get(clazz);
		if (rc == null) {
			try {
				Constructor<T> c = clazz.getConstructor(Target.class);
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
	public Target getTarget() {
		return target;
	}

}