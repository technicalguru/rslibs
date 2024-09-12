package rs.baselib.collection;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;

import rs.baselib.function.ExceptionalFunction;

/**
 * Helper methods for synchronizing collections.
 */
public class SyncUtils {

	/**
	 * Synchronize a collection with a collection of to-be members.
	 * @param <T> the type of collection object
	 * @param collection the existing collection
	 * @param newValues the collection holding all future members
	 * @param existsFunction the function that checks whether a value exists in existing collection
	 * @param addFunction the function that will be called for new members
	 * @param removeFunction the function that will be called for old members
	 * @return the new collection
	 * @throws SyncException when synchronization fails
	 */
	public static <T> Collection<T> sync(
			Collection<T>                       collection, 
			Collection<T>                       newValues, 
			BiFunction<Collection<T>,T,Boolean> existsFunction, 
			ExceptionalFunction<T,T>            addFunction,
			ExceptionalFunction<T,T>            removeFunction
	) throws SyncException {
		try {
			@SuppressWarnings("unchecked")
			Collection<T> rc = (Collection<T>)collection.getClass().getConstructor().newInstance();
			
			// Add new values
			if ((newValues != null) && !newValues.isEmpty()) {
				for (T value : newValues) {
					if (!existsFunction.apply(collection, value)) {
						value = addFunction.apply(value);
					}
					rc.add(value);
				}
			}
			
			// Remove values we did not see yet
			for (T value : CollectionUtils.subtract(collection, rc)) {
				removeFunction.apply(value);
			}
			
			return rc;
		} catch (IllegalAccessException|InstantiationException|NoSuchMethodException|InvocationTargetException e) {
			throw new SyncException("Cannot create new collection", e);
		} catch (Throwable t) {
			throw new SyncException("Cannot sync collection", t);
		}
	}
	
	/**
	 * Synchronize a collection with a collection of to-be members.
	 * @param <T> the type of collection object
	 * @param collection the existing collection
	 * @param newValues the collection holding all future members
	 * @param syncer the object providing the base methods such as exists, add and remove
	 * @return the new collection
	 * @throws SyncException when synchronization fails
	 */
	public static <T> Collection<T> sync(
			Collection<T>     collection, 
			Collection<T>     newValues,
			SyncHelper<T> syncer
	) throws SyncException {
		try {
			return sync(collection, newValues, syncer::exists, syncer::add, syncer::remove);
		} catch (Exception e) {
			throw new SyncException(e);
		}
	}

	/**
	 * Synchronize a collection with a collection of to-be members.
	 * @param <T> the type of collection object
	 * @param collectionSupplier the supplier of the existing collection
	 * @param newValues the collection holding all future members
	 * @param existsFunction the function that checks whether a value exists in existing collection
	 * @param addFunction the function that will be called for new members
	 * @param removeFunction the function that will be called for old members
	 * @return the new collection
	 * @throws SyncException when synchronization fails
	 */
	public static <T> Collection<T> sync(
			Supplier<Collection<T>>             collectionSupplier, 
			Collection<T>                       newValues, 
			BiFunction<Collection<T>,T,Boolean> existsFunction, 
			ExceptionalFunction<T,T>                       addFunction,
			ExceptionalFunction<T,T>                       removeFunction
	) throws SyncException {
		return sync(collectionSupplier.get(), newValues, existsFunction, addFunction, removeFunction);
	}
	
	/**
	 * Synchronize a collection with a collection of to-be members.
	 * @param <T> the type of collection object
	 * @param collectionSupplier the supplier of the existing collection
	 * @param newValues the collection holding all future members
	 * @param syncer the object providing the base methods such as exists, add and remove
	 * @return the new collection
	 * @throws SyncException when synchronization fails
	 */
	public static <T> Collection<T> sync(
			Supplier<Collection<T>>  collectionSupplier, 
			Collection<T>            newValues,
			SyncHelper<T>            syncer
	) throws SyncException {
		return sync(collectionSupplier.get(), newValues, syncer::exists, syncer::add, syncer::remove);
	}

}
