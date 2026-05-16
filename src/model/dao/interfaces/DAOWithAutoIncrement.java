package model.dao.interfaces;

public interface DAOWithAutoIncrement<T> extends IDAO<T> {

    int saveAndGetId(T data) throws Exception;

}
