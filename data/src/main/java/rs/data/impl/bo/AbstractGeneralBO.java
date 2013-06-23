/**
 * 
 */
package rs.data.impl.bo;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.data.api.bo.GeneralBO;
import rs.data.api.dao.GeneralDAO;
import rs.data.util.CID;
import rs.data.util.LockInformation;
import rsbaselib.bean.AbstractBean;
import rsbaselib.lang.LangUtils;
import rsbaselib.util.CommonUtils;

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

	/** The persistent classes to manage */
	private Class<K> keyClass;
	
	private Logger log = null; // Will be created upon request only
	private boolean changed;
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
	protected void init() {
		List<Class<?>> classes = LangUtils.getTypeArguments(AbstractGeneralBO.class, getClass());
		
		this.keyClass = (Class<K>) classes.get(0);		
		changed = false;
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
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidate() {
		invalid = true;
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
	 * @return
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getSimpleName()+"[id="+getId()+"]";
	}

	/**
	 * Fires an event if property changed.
	 * @param evt event to be fired
	 */
	protected void firePropertyChange(PropertyChangeEvent evt) {
		if (!CommonUtils.equals(evt.getOldValue(), evt.getNewValue())) {
			setChanged(true);
			super.firePropertyChange(evt);
		}
	}

	/**
	 * @{inheritDoc}
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
	 * @{inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!getClass().equals(o.getClass())) return false;
		K otherId = ((GeneralBO<K>)o).getId();
		K thisId = getId();
		if ((otherId == null) || (thisId == null)) return false;
		return thisId.equals(otherId);
	}


}
