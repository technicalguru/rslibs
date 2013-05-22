/**
 * 
 */
package rs.data.impl.bo;

import java.io.Serializable;
import java.util.List;

import rs.data.impl.dto.GeneralDTO;
import rs.data.util.CID;
import rsbaselib.lang.LangUtils;
import rsbaselib.util.RsDate;

/**
 * Abstract Implementation for Business Objects that require a Transfer Object.
 * @param <K> type of primary key
 * @param <T> type of Transfer Object Implementation
 * @author ralph
 *
 */
public abstract class AbstractBO<K extends Serializable, T extends GeneralDTO<K>> extends AbstractGeneralBO<K> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 2918149248925033131L;
	
	/** The persistent classes to manage */
	private Class<T> transferClass;
	private T transferObject;
	
	/**
	 * Constructor.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public AbstractBO() {
		this(null);
	}

	/**
	 * Constructor.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public AbstractBO(T transferObject) {
		if (transferObject == null) try {
			transferObject = getTransferClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create empty instance: ", e);
		}
		setTransferObject(transferObject);
		init();
	}

	/**
	 * Initializes this BO.
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		List<Class<?>> classes = LangUtils.getTypeArguments(AbstractBO.class, getClass());
		this.transferClass = (Class<T>) classes.get(1);
	}
	
	/**
	 * Returns the transferClass.
	 * @return the transferClass
	 */
	public Class<T> getTransferClass() {
		return transferClass;
	}

	/**
	 * Returns the Transfer Object.
	 * @return transfer object
	 */
	public T getTransferObject() {
		return transferObject;
	}
	
	/**
	 * Sets the transfer object.
	 * @param transferObject the object to be set.
	 */
	public void setTransferObject(T transferObject) {
		this.transferObject = transferObject;
		K id = transferObject.getId();
		if (id != null) setCID(new CID(getClass(), id));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public K getId() {
		return getTransferObject().getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getCreationDate() {
		return getTransferObject().getCreationDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationDate(RsDate creationDate) {
		RsDate oldValue = getCreationDate();
		getTransferObject().setCreationDate(creationDate);
		firePropertyChange(PROPERTY_CREATION_DATE, oldValue, creationDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getChangeDate() {
		return getTransferObject().getChangeDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChangeDate(RsDate changeDate) {
		RsDate oldValue = getChangeDate();
		getTransferObject().setChangeDate(changeDate);
		firePropertyChange(PROPERTY_CHANGE_DATE, oldValue, changeDate);
	}
	
}
