package model.dao.interfaces;

import java.util.List;

/**
 *
 * @author Andres Cardenas
 * @param <T>
 */
public interface IDAO <T> {
    
    void save (T data) throws Exception;
    T getData (int code) throws Exception;
    List<T> getData () throws Exception;
    boolean exists(int id) throws Exception;
    
}
