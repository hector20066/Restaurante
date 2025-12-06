package model.dao.interfaces;

import java.util.List;

/**
 *
 * @author hectoracevedo
 * @param <T>
 */
public interface DAORestaurantDb <T> {
    
    void save (T data) throws Exception;
    T getData (int code) throws Exception;
    T getData (String name) throws Exception;
    List<T> getData () throws Exception;
    void modify (T data) throws Exception;
    void delete (int code) throws Exception;
    
}
