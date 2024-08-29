package rs.baselib.collection;

import java.util.Collection;

/**
 * A helper class that uses a parameter to retrieve the collection.
 * @param <T> the type of object in collections
 * @param <X> the type of parameter that is required to produce the collection
 */
public abstract class AbstractSyncHelper2<T,X> implements SyncHelper<T> {

	/**
	 * Constructor.
	 */
	protected AbstractSyncHelper2() {
	}

	/**
	 * Synchronized the collection with the given new content.
	 * <p>The method will call {@link #getCollection(Object)} to retrieve the correct collection.
	 * @param parameter a custom parameter that controls the suppply of the collection.
	 * @param newCollection the new collection
	 * @return the new collection
	 * @throws SyncException when synchronization fails
	 */
	public Collection<T> sync(X parameter, Collection<T> newCollection) throws SyncException {
		return sync(getCollection(parameter), newCollection);
	}
	
	/**
	 * Returns the collection based on the parameter.
	 * @param parameter a custom parameter that controls the suppply of the collection.
	 * @return the collection to be synchronized
	 */
	protected abstract Collection<T> getCollection(X parameter);
	
	
}
