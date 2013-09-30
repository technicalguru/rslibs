/**
 * 
 */
package rs.data.impl.bo;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.bean.AbstractBean;
import rs.baselib.bean.BeanSupport;
import rs.baselib.lang.LangUtils;
import rs.data.api.IDaoFactory;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.impl.dao.AbstractDAO;
import rs.data.impl.dto.GeneralDTO;
import rs.data.util.CID;
import rs.data.util.LockInformation;

/**
 * Abstract Implementation for Business Objects that do not require a Transfer Object.
 * @param <K> type of primary key
 * @author ralph
 *
 */
public abstract class AbstractGeneralBO<K extends Serializable> extends AbstractBean implements IGeneralBO<K> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1332731487212304311L;

	static {
		BeanSupport.INSTANCE.addForbiddenCopy(AbstractGeneralBO.class, PROPERTY_CREATION_DATE);
		BeanSupport.INSTANCE.addForbiddenCopy(AbstractGeneralBO.class, PROPERTY_CHANGE_DATE);
		BeanSupport.INSTANCE.addForbiddenCopy(AbstractGeneralBO.class, "changed");
		BeanSupport.INSTANCE.addForbiddenCopy(AbstractGeneralBO.class, "dao");
	}
	
	/** The persistent classes to manage */
	private Class<K> keyClass;
	/** The interface class of this BO. */
	private Class<? extends IGeneralBO<K>> interfaceClass;
	private Logger log = null; // Will be created upon request only
	private boolean invalid;
	private CID cid;
	private IGeneralDAO<K, ? extends IGeneralBO<K>> dao;
	
	/**
	 * Constructor.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public AbstractGeneralBO() {
		init();
	}

	/**
	 * Initializes this BO.
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		List<Class<?>> classes = LangUtils.getTypeArguments(AbstractGeneralBO.class, getClass());
		this.keyClass = (Class<K>) classes.get(0);
		
		for (Class<?> clazz : getClass().getInterfaces()) {
			if (IGeneralBO.class.isAssignableFrom(clazz)) {
				this.interfaceClass = (Class<? extends IGeneralBO<K>>)clazz;
			}
		}
		
		invalid = false;
	}
	
	/**
	 * Returns the DAO factory.
	 * @return the DAO factory
	 */
	protected IDaoFactory getFactory() {
		return getDao().getFactory();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transient
	public IGeneralDAO<K, ? extends IGeneralBO<K>> getDao() {
		return dao;
	}

	/**
	 * Sets the dao.
	 * @param dao the dao to set
	 */
	public void setDao(IGeneralDAO<K, ? extends IGeneralBO<K>> dao) {
		this.dao = dao;
	}

	/**
	 * Fetches the BO from the DAO factory.
	 * @param dto the DTO
	 * @return the BO
	 */
	@SuppressWarnings("unchecked")
	protected <X extends Serializable, Y extends GeneralDTO<X>, Z extends IGeneralBO<X>> Z getBusinessObject(Y dto) {
		if (dto == null) return null;
		IGeneralDAO<X, Z> dao = (IGeneralDAO<X, Z>)getFactory().getDaoFor(dto);
		if ((dao == null) || !(dao instanceof AbstractDAO)) throw new RuntimeException("Cannot find DAO for: "+dto);
		return ((AbstractDAO<X, Y, ?, Z>)dao).getBusinessObject(dto);
	}
	
	/**
	 * Returns the DTO from that BO (or null).
	 * @param o the BO
	 * @return the DTO underneath
	 */
	public <X extends Serializable, Y extends GeneralDTO<X>> Y getTransferObject(AbstractBO<X,Y> o) {
		if (o == null) return null;
		if (o instanceof AbstractBO) return o.getTransferObject();
		return null;
	}
	
	/**
	 * Returns the keyClass.
	 * @return the keyClass
	 */
	@Transient
	public Class<K> getKeyClass() {
		return keyClass;
	}

	/**
	 * Returns the cid.
	 * @return the cid
	 */
	@Transient
	public CID getCID() {
		if (cid == null) {
			K id = getId();
			if (id != null) setCID(new CID(getInterfaceClass(), id));
		}
		return cid;
	}

	/**
	 * Returns the interface class of this BO.
	 * @return the BO interface class
	 */
	@Transient
	public Class<? extends IGeneralBO<K>> getInterfaceClass() {
		return interfaceClass;
	}
	
	/**
	 * Sets the cid.
	 * @param cid the cid to set
	 */
	public void setCID(CID cid) {
		this.cid = cid;
	}

	/**
	 * Returns a logger.
	 * @return the logger
	 */
	protected Logger getLog() {
		if (log == null) {
			log = LoggerFactory.getLogger(getClass());
		}
		return log;
	}

	/**
	 * Begins a TX.
	 */
	protected void beginTx() {
		getFactory().begin();
	}
	
	/**
	 * Commits a TX.
	 */
	protected void commitTx() {
		getFactory().commit();
	}
	
	/**
	 * Rolls back the TX.
	 */
	protected void rollbackTx() {
		getFactory().rollback();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transient
	public boolean isChanged() {
		return isDirty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChanged(boolean changed) {
		setDirty(changed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNew() {
		return getId() == null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidate() {
		invalid = true;
		setChanged(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockInformation lock(int timeout) {
		return new LockInformation(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockInformation release() {
		return new LockInformation(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockInformation getLockInformation() {
		return new LockInformation(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInvalid() {
		return invalid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getClass().getSimpleName()+"[id="+getId()+"]";
	}

	/**
	 * {@inheritDoc}
	 * Hash code combining class and id.
	 */
	@Override
	public int hashCode() {
		K id = getId();
		int rc = 31 * getClass().hashCode();
		if (id != null) rc += ( id.hashCode() >>> 32);
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (this == o) return true;
		if (!getClass().equals(o.getClass())) return false;
		K otherId = ((IGeneralBO<K>)o).getId();
		K thisId = getId();
		if ((otherId == null) || (thisId == null)) return false;
		return thisId.equals(otherId);
	}


}
