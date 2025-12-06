package model.strategy;

/**
 *
 * @author hectoracevedo
 */
public interface Strategy <T> {
    
    void save(T data) throws Exception;
    void modify(T data) throws Exception;

    String nameStrategy();
    
}
