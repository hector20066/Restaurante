package model.dao.interfaces;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author hectoracevedo
 * @param <T>
 */
public interface DAORestaurantFile <T> {

    void setFilePath (String path);
    String getFilePath ();
    void createFile (String filePath) throws IOException, ClassNotFoundException;
    void save (T data) throws IOException, ClassNotFoundException;
    T getData (String name) throws IOException, ClassNotFoundException;
    List<T> getData () throws IOException, ClassNotFoundException;
    void delete (String name) throws IOException, ClassNotFoundException;
    
}
