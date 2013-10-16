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
 * Abstract Implementation for Data Access Objects that can create BO instances.
 * This implementation assumes that the BO implementation derives from {@link AbstractGeneralBO}.
 * @param <K> type of primary key
 * @param <B> type of Business Object Implementation
 * @param <C> type of Business Object Interface
 * @author ralph
 *
 */
public abstract class AbstractGeneralDAO<K extends Serializable, B extends AbstractGeneralBO<K>, C extends IGeneralBO<K>> extends AbstractBasicDAO<K, C> {

	/** The persistent class to manage */
	private Class<B> boImplementationClass;

	/**
	 * Constructor.
	 */
	public AbstractGeneralDAO() {
		init();
	}

	/**
	 * Initializes / called from constructor.
	 */
	@SuppressWarnings("unchecked")
	protected void init() {
		super.init();
		List<Class<?>> classes = LangUtils.getTypeArguments(AbstractGeneralDAO.class, getClass());
		this.boImplementationClass = (Class<B>) classes.get(1);		
	}

	/**
	 * Returns the boImplementationClass.
	 * @return the boImplementationClass
	 */
	protected Class<B> getBoImplementationClass() {
		return boImplementationClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected C _newInstance() {
		try {
			return (C)getBoImplementationClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create Business Object", e);
		}
	}

	
}
