/**
 * 
 */
package rs.data.hibernate.bo;

import java.io.Serializable;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import rs.data.hibernate.HibernateDaoMaster;
import rs.data.impl.bo.AbstractBO;
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
