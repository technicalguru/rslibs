package rs.baselib.collection;

import java.util.Collection;

/**
 * An interface to ease collection synchronization.
 * @param <T> the type of object in collections
 */
public interface SyncHelper<T> {

	/**
	 * Synchronizes the collection with the given new content.
	 * <p>The method will call {@link SyncUtils#sync(Collection, Collection, java.util.function.BiFunction, java.util.function.Function, java.util.function.Function)}
	 * using this object.
	 * @param collection the existing collection
	 * @param newValues the collection holding all future members
	 * @throws SyncException when syncing fails
	 */
	default Collection<T> sync(Collection<T> collection, Collection<T> newValues) throws SyncException {
		return SyncUtils.sync(collection, newValues, this);
	}
	
	/**
	 * Must return whether the old collection contains the given value.
	 * <p>The standard implementation will call {@link Collection#contains(Object)} which relies
	 * on proper {@link Object#hashCode()} and {@link Object#equals(Object)} implementations.</p>
	 * @param value the value to be checked in old collection
	 * @return {@code true} when the value already exists.
	 */
	default boolean exists(Collection<T> collection, T value) {
		return (collection != null) && collection.contains(value);
	}
	
	/**
	 * Perform the add action for the given value because it is new.
	 * @param value the value to add
	 * @throws SyncException when adding fails
	 */
	default T add(T value) {
		return value;
	}
	
	/**
	 * Perform the remove action for the given value because was removed.
	 * @param value the value to remove
	 * @throws SyncException when removing fails
	 */
	default T remove(T value) {
		return value;
	}
}
