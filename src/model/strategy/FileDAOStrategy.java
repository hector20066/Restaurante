package model.strategy;

import model.dao.interfaces.DAORestaurantFile;

/**
 *
 * @author hectoracevedo
 */
public class FileDAOStrategy<T> implements Strategy<T> {

    private final DAORestaurantFile<T> daoFile;

    public FileDAOStrategy (DAORestaurantFile<T> daoFile) {
        this.daoFile = daoFile;
    }

    @Override
    public void save(T data) throws Exception {
        daoFile.save(data);
    }

    @Override
    public void modify(T data) {
        // Not applicable in this case
    }

    @Override
    public String nameStrategy() {
        return "File";
    }

}
