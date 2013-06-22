/**
 * 
 */
package rs.data.api.dao;

import rs.data.api.bo.ILongBO;

/**
 * Data Access Object interface for {@link Long} key-based DTOs and BOs.
 * @param <B> type of Business Object
 * @author ralph
 *
 */
public interface ILongDAO<B extends ILongBO> extends IGeneralDAO<Long, B> {

}
