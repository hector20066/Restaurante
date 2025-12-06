package model.strategy;

import model.dao.interfaces.DAORestaurantDb;

/**
 *
 * @author hectoracevedo
 */
public class DBDAOStrategy<T> implements Strategy<T> {

    private final DAORestaurantDb<T> daoDb;

    public DBDAOStrategy (DAORestaurantDb<T> daoDb) {
        this.daoDb = daoDb;
    }

    @Override
    public void save(T data) throws Exception {
        daoDb.save(data);
    }

    @Override
    public void modify(T data) throws Exception {
        daoDb.modify(data);
    }
    
    @Override
    public String nameStrategy() {
        return "Data Base";
    }
    
}
