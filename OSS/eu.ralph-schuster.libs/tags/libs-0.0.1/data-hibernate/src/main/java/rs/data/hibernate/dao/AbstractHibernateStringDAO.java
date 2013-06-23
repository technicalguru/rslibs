/**
 * 
 */
package rs.data.hibernate.dao;

import rs.data.api.bo.GeneralBO;
import rs.data.impl.bo.AbstractBO;
import rs.data.impl.dto.GeneralDTO;

/**
 * Abstract implementation for String based BOs.
 * @author ralph
 *
 */
public class AbstractHibernateStringDAO<T extends GeneralDTO<String>, B extends  AbstractBO<String,T>, C extends GeneralBO<String>> extends AbstractHibernateDAO<String, T, B, C> {

	/**
	 * Constructor.
	 */
	public AbstractHibernateStringDAO() {
	}

}
