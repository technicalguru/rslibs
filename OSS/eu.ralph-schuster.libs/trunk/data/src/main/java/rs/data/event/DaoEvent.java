/**
 * 
 */
package rs.data.event;

import java.util.EventObject;

import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;

/**
 * Event that will be fired from a DAO.
 * @author ralph
 *
 */
public class DaoEvent extends EventObject {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -2121712297338985716L;

	public static enum Type {
		OBJECT_CREATED,
		OBJECT_UPDATED,
		OBJECT_DELETED,
		ALL_DEFAULT_DELETED,
		ALL_DELETED
	};
	
	private Type type;
	private  IGeneralBO<?> object;
	
	/**
	 * Constructor for DELETE_ALL events.
	 */
	public DaoEvent(IGeneralDAO<?, ?> source, Type type) {
		this(source, type, null);
	}

	/**
	 * Constructor.
	 */
	public DaoEvent(IGeneralDAO<?, ?> source, Type type, IGeneralBO<?> object) {
		super(source);
		this.type = type;
		this.object = object;
	}

	/**
	 * Returns the type.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns the object.
	 * @return the object
	 */
	public IGeneralBO<?> getObject() {
		return object;
	}

	
}
