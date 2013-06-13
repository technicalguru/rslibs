/**
 * 
 */
package rs.data.impl.bo;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.bean.AbstractBean;
import rs.baselib.bean.BeanSupport;
import rs.baselib.lang.LangUtils;
import rs.data.api.bo.GeneralBO;
import rs.data.api.dao.GeneralDAO;
import rs.data.util.CID;
import rs.data.util.LockInformation;

/**
 * Abstract Implementation for Business Objects that do not require a Transfer Object.
 * @param <K> type of primary key
 * @author ralph
 *
 */
public abstract class AbstractGeneralBO<K extends Serializable> extends AbstractBean implements GeneralBO<K> {

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
	
	private Logger log = null; // Will be created upon request only
	private boolean invalid;
	private CID cid;
	private GeneralDAO<K, ? extends GeneralBO<K>> dao;
	
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
		invalid = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeneralDAO<K, ? extends GeneralBO<K>> getDao() {
		return dao;
	}

	/**
	 * Sets the dao.
	 * @param dao the dao to set
	 */
	public void setDao(GeneralDAO<K, ? extends GeneralBO<K>> dao) {
		this.dao = dao;
	}

	/**
	 * Returns the keyClass.
	 * @return the keyClass
	 */
	public Class<K> getKeyClass() {
		return keyClass;
	}

	/**
	 * Returns the cid.
	 * @return the cid
	 */
	public CID getCID() {
		if (cid == null) {
			K id = getId();
			if (id != null) setCID(new CID(getClass(), id));
		}
		return cid;
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
	 * {@inheritDoc}
	 */
	@Override
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
		K otherId = ((GeneralBO<K>)o).getId();
		K thisId = getId();
		if ((otherId == null) || (thisId == null)) return false;
		return thisId.equals(otherId);
	}


}
