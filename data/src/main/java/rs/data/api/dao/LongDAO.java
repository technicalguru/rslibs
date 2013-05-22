/**
 * 
 */
package rs.data.api.dao;

import rs.data.api.bo.LongBO;

/**
 * Data Access Object interface for {@link Long} key-based DTOs and BOs.
 * @param <B> type of Business Object
 * @author ralph
 *
 */
public interface LongDAO<B extends LongBO> extends GeneralDAO<Long, B> {

}
