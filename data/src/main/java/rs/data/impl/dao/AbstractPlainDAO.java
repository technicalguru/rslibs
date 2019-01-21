package rs.data.impl.dao;

import java.io.Serializable;

import rs.data.api.bo.IGeneralBO;
import rs.data.impl.bo.AbstractPlainBO;

/**
 * Abstract DAO implementation for plain BOs (BO without transfer objects).
 * @author ralph
 *
 * @param <K> type of primary key
 * @param <C> type of Business Object Interface
 */
public abstract class AbstractPlainDAO<K extends Serializable, C extends IGeneralBO<K>, B extends AbstractPlainBO<K>> extends AbstractGeneralDAO<K, B, C> {

	public AbstractPlainDAO() {
	}

}
