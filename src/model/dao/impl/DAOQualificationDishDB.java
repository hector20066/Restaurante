package model.dao.impl;

import model.dao.interfaces.IDAO;
import model.entities.QualificationDish;

import java.util.List;

public class DAOQualificationDishDB implements IDAO<QualificationDish> {

    @Override
    public void save(QualificationDish data) throws Exception {

    }

    @Override
    public QualificationDish getData(int code) throws Exception {
        return null;
    }

    @Override
    public List<QualificationDish> getData() throws Exception {
        return List.of();
    }

    @Override
    public boolean exists(int id) throws Exception {
        return false;
    }

}
