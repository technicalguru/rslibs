/**
 * 
 */
package rs.data.impl.dao;

import java.io.Serializable;
import java.util.List;

import rs.baselib.lang.LangUtils;
import rs.data.api.bo.IGeneralBO;
import rs.data.impl.bo.AbstractGeneralBO;

/**
 * Abstract Implementation for Data Access Objects.
 * This implementation assumes that the BO implementation derives from {@link AbstractGeneralBO}.
 * @param <K> type of primary key
 * @param <B> type of Business Object Implementation
 * @param <C> type of Business Object Interface
 * @author ralph
 *
 */
public abstract class AbstractExtendedGeneralDAO<K extends Serializable, B extends AbstractGeneralBO<K>, C extends IGeneralBO<K>> extends AbstractGeneralDAO<K, C> {

	/** The persistent class to manage */
	private Class<B> boImplementationClass;

	/**
	 * Constructor.
	 */
	public AbstractExtendedGeneralDAO() {
		init();
	}

	/**
	 * Initializes / called from constructor.
	 */
	@SuppressWarnings("unchecked")
	protected void init() {
		super.init();
		List<Class<?>> classes = LangUtils.getTypeArguments(AbstractExtendedGeneralDAO.class, getClass());
		this.boImplementationClass = (Class<B>) classes.get(1);		
	}

	/**
	 * Returns the boImplementationClass.
	 * @return the boImplementationClass
	 */
	protected Class<B> getBoImplementationClass() {
		return boImplementationClass;
	}

	/************************* INSTANTIATION ************************/

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public C newInstance() {
		try {
			B rc = getBoImplementationClass().newInstance();
			rc.setDao(this);
			afterNewInstance((C)rc);
			return (C)rc;
		} catch (IllegalAccessException e) {
			log.error("Error creating new object: ", e);
		} catch (InstantiationException e) {
			log.error("Error creating new object: ", e);
		}
		return null;
	}

	/**
	 * Called after a new instance was created.
	 * @param object object being created
	 */
	protected void afterNewInstance(C object) {		
	}

	/************************* CREATION ************************/

}
