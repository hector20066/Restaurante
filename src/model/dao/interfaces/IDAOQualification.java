package model.dao.interfaces;

/**
 *
 * @author Andres Cardenas
 * @param <T>
 */
public interface IDAOQualification<T> extends IDAO<T> {
    
    T findByCustomerAndProduct(int idCustomer, int idProduct) throws Exception;
    
    void modify(T data) throws Exception;
    
}
