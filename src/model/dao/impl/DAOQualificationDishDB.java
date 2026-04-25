package model.dao.impl;

import database.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.dao.interfaces.IDAO;
import model.entities.QualificationDish;

/**
 *
 * @author Andres Cardenas
 */
public class DAOQualificationDishDB extends DataBase implements IDAO<QualificationDish> {

    private static DAOQualificationDishDB instance;

    private DAOQualificationDishDB() {

    }

    public static DAOQualificationDishDB getInstance() {
        if (instance == null) {
            instance = new DAOQualificationDishDB();
        }
        return instance;
    }

    @Override
    public void save(QualificationDish data) throws Exception {
        String sql = "{CALL sp_calificar_plato(?, ?, ?, ?)}";
        this.connect();

        try (CallableStatement cs = this.connection.prepareCall(sql)) {
            cs.setInt(1, data.getStars());
            cs.setString(2, data.getComment());
            cs.setInt(3, data.getIdDish());
            cs.setInt(4, data.getIdCustomer());
            cs.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized QualificationDish getData(int code) throws Exception {
        QualificationDish qualification = null;
        String sql = "SELECT id, num_estrellas, comentario, idPlato, idCliente, fecha FROM calificacion_plato WHERE id = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    qualification = new QualificationDish();
                    qualification.setCode(rs.getInt("id"));
                    qualification.setStars(rs.getInt("num_estrellas"));
                    qualification.setComment(rs.getString("comentario"));
                    qualification.setIdDish(rs.getInt("idPlato"));
                    qualification.setIdCustomer(rs.getInt("idCliente"));
                    qualification.setDate(rs.getDate("fecha").toLocalDate());
                }
            }
        } finally {
            this.close();
        }

        return qualification;
    }

    @Override
    public synchronized List<QualificationDish> getData() throws Exception {
        List<QualificationDish> list;
        String sql = "SELECT id, num_estrellas, comentario, idPlato, idCliente, fecha FROM calificacion_plato ORDER BY num_estrellas DESC;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    QualificationDish qualification = new QualificationDish();
                    qualification.setCode(rs.getInt("id"));
                    qualification.setStars(rs.getInt("num_estrellas"));
                    qualification.setComment(rs.getString("comentario"));
                    qualification.setIdDish(rs.getInt("idPlato"));
                    qualification.setIdCustomer(rs.getInt("idCliente"));
                    qualification.setDate(rs.getDate("fecha").toLocalDate());
                    list.add(qualification);
                }
            }
        } finally {
            this.close();
        }

        return list;
    }

    @Override
    public boolean exists(int id) throws Exception {
        String sql = "SELECT 1 FROM calificacion_plato WHERE id = ?";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } finally {
            this.close();
        }
    }

}
