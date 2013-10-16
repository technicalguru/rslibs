/**
 * 
 */
package rs.data.event;

import java.util.EventObject;

import rs.data.api.IDaoFactory;

/**
 * Event notification from Dao Factory.
 * @author ralph
 *
 */
public class DaoFactoryEvent extends EventObject {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3614048885876975805L;

	/** Type of factory event */
	public static enum Type {
		/** Transaction was started */
		TRANSACTION_STARTED,
		/** Transaction is committing but did not finish yet */
		TRANSACTION_COMMITTING,
		/** Transaction has been committed */
		TRANSACTION_COMMITTED,
		/** Transaction is rolling back but did not finish yet */
		TRANSACTION_ROLLING_BACK,
		/** Transaction has been rolled back */
		TRANSACTION_ROLLED_BACK,
		/** A change was made to the data model. (Details should be caught via {@link DaoEvent}). */
		MODEL_CHANGED
	}
	
	private Type type;
	
	/**
	 * Constructor.
	 * @param source factory
	 */
	public DaoFactoryEvent(IDaoFactory source, Type type) {
		super(source);
		this.type = type;
	}

	/**
	 * Returns the type.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName()+"[source="+getSource().getClass().getSimpleName()+";type="+getType().name()+"]";
	}

	
}
