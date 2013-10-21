/**
 * 
 */
package rs.data.impl.bo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import rs.baselib.bean.NamedObject;
import rs.baselib.bean.NoCopy;
import rs.baselib.lang.LangUtils;
import rs.baselib.util.RsDate;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.CID;

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
	private static final long serialVersionUID = 1L;
	
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
		init();
		if (transferObject == null) try {
			transferObject = getTransferClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create empty instance: ", e);
		}
		setTransferObject(transferObject);
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
	@Transient
	public Class<T> getTransferClass() {
		return transferClass;
	}

	/**
	 * Returns the Transfer Object.
	 * @return transfer object
	 */
	@Transient
	@NoCopy
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
		if (id != null) setCID(new CID(getInterfaceClass(), id));
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
	public void setId(K id) {
		getTransferObject().setId(id);
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
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		if (!(this instanceof NamedObject)) throw new IllegalArgumentException("This is not a NamedObject");
		return getTransferObject().getName();
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		String oldValue = getName();
		getTransferObject().setName(name);
		firePropertyChange(NamedObject.PROPERTY_NAME, oldValue, name);
	}
	
}
