package rs.baselib.collection;

import java.util.Collection;

/**
 * A helper class providing a straight-forward collection.
 * @param <T> the type of object in collections
 */
public abstract class AbstractSyncHelper<T> implements SyncHelper<T> {

	private Collection<T> collection;
	
	/**
	 * Constructor.
	 * @param collection the collection
	 */
	protected AbstractSyncHelper(Collection<T> collection) {
		this.collection = collection;
	}

	/**
	 * Synchronized the collection with the given new content.
	 * <p>The method will call {@link #getCollection()} to retrieve the collection.
	 * @param newCollection the new collection
	 * @return the new collection after synchronization
	 * @throws SyncException when synchronization fails
	 */
	public Collection<T> sync(Collection<T> newCollection) throws SyncException {
		return sync(getCollection(), newCollection);
	}
	
	/**
	 * Returns the collection.
	 * <p>The collection must be set by the constructor or produced by overriding this method.</p>
	 * @return the collection to be synchronized
	 */
	protected Collection<T> getCollection() {
		if (collection == null) throw new RuntimeException("No collection available to sync");
		return collection;
	}
	
	
}
