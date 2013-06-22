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

	public static enum Type {
		TRANSACTION_STARTED,
		TRANSACTION_COMMITTING,
		TRANSACTION_COMMITTED,
		TRANSACTION_ROLLING_BACK,
		TRANSACTION_ROLLED_BACK,
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
