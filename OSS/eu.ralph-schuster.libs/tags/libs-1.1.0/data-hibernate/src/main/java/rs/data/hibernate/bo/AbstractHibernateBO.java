/**
 * 
 */
package rs.data.hibernate.bo;

import java.io.Serializable;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import rs.data.api.IDaoFactory;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;
import rs.data.hibernate.HibernateDaoMaster;
import rs.data.impl.bo.AbstractBO;
import rs.data.impl.dao.AbstractDAO;
import rs.data.impl.dto.GeneralDTO;

/**
 * Abstract implementation for hibernate BO.
 * @author ralph
 *
 */
public abstract class AbstractHibernateBO<K extends Serializable, T extends GeneralDTO<K>> extends AbstractBO<K, T> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -248221407299395538L;

	/**
	 * Constructor.
	 */
	public AbstractHibernateBO() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public AbstractHibernateBO(T transferObject) {
		super(transferObject);
	}

	/**
	 * Returns the DAO factory.
	 * @return the DAO factory
	 */
	protected IDaoFactory getFactory() {
		return getDao().getFactory();
	}
	
	/**
	 * Returns the DAO master.
	 * @return the DAO master
	 */
	protected HibernateDaoMaster getDaoMaster() {
		return (HibernateDaoMaster)getDao().getDaoMaster();
	}
	
	/**
	 * Returns a Hibernate session.
	 * @return the session
	 */
	protected Session getSession() {
		return getDaoMaster().getSession();
	}
	
	/**
	 * Begins a TX.
	 * @throws SystemException
	 * @throws NotSupportedException
	 */
	protected void beginTx() {
		getFactory().begin();
	}
	
	/**
	 * Commits a TX.
	 * @throws SystemException
	 * @throws HeuristicRollbackException
	 * @throws HeuristicMixedException
	 * @throws RollbackException
	 */
	protected void commitTx() {
		getFactory().commit();
	}
	
	/**
	 * Rollsback the TX.
	 * @throws SystemException
	 */
	protected void rollbackTx() {
		getFactory().rollback();
	}
	
	/**
	 * Returns the transferObject.
	 * @return the transferObject
	 */
	public T getTransferObject() {
		boolean needsInit = false;
		T t = super.getTransferObject();
		if (t instanceof HibernateProxy) {
			LazyInitializer initializer = ((HibernateProxy)t).getHibernateLazyInitializer();
			if (initializer != null) needsInit = initializer.isUninitialized();
		}
		if (needsInit) t = initialize();
		return t;
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
	 * Returns the session-attached transferObject.
	 * @return the session attached transferObject
	 */
	@SuppressWarnings("unchecked")
	protected T getAttachedTransferObject() {
		// First check whether T is still attached
		T t = super.getTransferObject();
		if (t instanceof HibernateProxy) {
			LazyInitializer initializer = ((HibernateProxy)t).getHibernateLazyInitializer();
			if (initializer != null) {
				if (!initializer.getSession().isOpen()) {
					return (T)getSession().get(getTransferClass(), getId());
				}
			}
			return t;
		}
		return (T)getSession().get(getTransferClass(), getId());
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
	 * Initializes the DTO.
	 */
	@SuppressWarnings("unchecked")
	protected T initialize() {
		T t = super.getTransferObject();
		try {
			beginTx();
			if (t instanceof HibernateProxy) {
				LazyInitializer initializer = ((HibernateProxy)t).getHibernateLazyInitializer();
				if (initializer != null) {
					if (initializer.getSession() == null || !initializer.getSession().isOpen()) {
						t = (T)getSession().get(getTransferClass(), getId());
					}
				}
			}
			commitTx();
		} catch (Exception e) {
			getLog().error("init problem:", e);
			try {
				rollbackTx();
			} catch (Exception e2) {
				throw new RuntimeException("Cannot rollback exception", e2);
			}
			throw new RuntimeException("Cannot initialize DTO", e);
		}
		return t;
	}
	

	@Override
	public void refresh() {
		try {
			beginTx();
			T t = getAttachedTransferObject();
			getSession().refresh(t, LockOptions.NONE);
			commitTx();
		} catch (Exception e) {
			try {
				rollbackTx();
			} catch (Exception e2) {
				throw new RuntimeException("Cannot rollback exception", e2);
			}
			throw new RuntimeException("Cannot initialize DTO", e);
		}
	}

}
