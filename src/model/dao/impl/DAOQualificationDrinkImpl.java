package model.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.DataBase;
import model.dao.interfaces.IDAOQualification;
import model.entities.QualificationDrink;

/**
 *
 * @author Andres Cardenas
 */
public class DAOQualificationDrinkImpl extends DataBase implements IDAOQualification<QualificationDrink> {

    private static DAOQualificationDrinkImpl instance;

    private DAOQualificationDrinkImpl() {

    }

    public static DAOQualificationDrinkImpl getInstance() {
        if (instance == null) {
            instance = new DAOQualificationDrinkImpl();
        }
        return instance;
    }

    @Override
    public void save(QualificationDrink data) throws Exception {
        String sql = "{CALL sp_registrar_calificacion_bebida(?, ?, ?, ?)}";
        this.connect();

        try (CallableStatement cs = this.connection.prepareCall(sql)) {
            cs.setInt(1, data.getStars());
            cs.setString(2, data.getComment());
            cs.setInt(3, data.getIdDrink());
            cs.setInt(4, data.getIdCustomer());
            cs.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized QualificationDrink getData(int code) throws Exception {
        QualificationDrink qualification = null;
        String sql = "SELECT id, num_estrellas, comentario, idBebida, idCliente, fecha FROM calificacion_bebida WHERE id = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    qualification = new QualificationDrink();
                    qualification.setCode(rs.getInt("id"));
                    qualification.setStars(rs.getInt("num_estrellas"));
                    qualification.setComment(rs.getString("comentario"));
                    qualification.setIdDrink(rs.getInt("idBebida"));
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
    public synchronized List<QualificationDrink> getData() throws Exception {
        List<QualificationDrink> list;
        String sql = "SELECT id, num_estrellas, comentario, idBebida, idCliente, fecha FROM calificacion_bebida ORDER BY num_estrellas DESC;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    QualificationDrink qualification = new QualificationDrink();
                    qualification.setCode(rs.getInt("id"));
                    qualification.setStars(rs.getInt("num_estrellas"));
                    qualification.setComment(rs.getString("comentario"));
                    qualification.setIdDrink(rs.getInt("idBebida"));
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
        String sql = "SELECT 1 FROM calificacion_bebida WHERE id = ?";
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

    @Override
    public synchronized QualificationDrink findByCustomerAndProduct(int idCustomer, int idProduct) throws Exception {
        QualificationDrink qualification = null;
        String sql = "SELECT id, num_estrellas, comentario, idBebida, idCliente, fecha FROM calificacion_bebida WHERE idCliente = ? AND idBebida = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, idCustomer);
            st.setInt(2, idProduct);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    qualification = new QualificationDrink();
                    qualification.setCode(rs.getInt("id"));
                    qualification.setStars(rs.getInt("num_estrellas"));
                    qualification.setComment(rs.getString("comentario"));
                    qualification.setIdDrink(rs.getInt("idBebida"));
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
    public void modify(QualificationDrink data) throws Exception {
        String sql = "UPDATE calificacion_bebida SET num_estrellas = ?, comentario = ?, fecha = ? WHERE id = ?";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, data.getStars());
            st.setString(2, data.getComment());
            st.setDate(3, java.sql.Date.valueOf(data.getDate()));
            st.setInt(4, data.getCode());
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

}
