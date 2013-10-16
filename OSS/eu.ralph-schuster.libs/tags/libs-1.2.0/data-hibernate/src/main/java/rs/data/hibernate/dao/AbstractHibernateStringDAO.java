/**
 * 
 */
package rs.data.hibernate.dao;

import rs.data.api.bo.IGeneralBO;
import rs.data.hibernate.bo.AbstractHibernateBO;
import rs.data.impl.dto.GeneralDTO;

/**
 * Abstract implementation for String based BOs.
 * @author ralph
 *
 */
public class AbstractHibernateStringDAO<T extends GeneralDTO<String>, B extends  AbstractHibernateBO<String,T>, C extends IGeneralBO<String>> extends AbstractHibernateDAO<String, T, B, C> {

	/**
	 * Constructor.
	 */
	public AbstractHibernateStringDAO() {
	}

}
