package model.dao.impl;

import database.DataBase;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.dao.interfaces.IDAO;
import model.entities.QualificationDrink;

/**
 *
 * @author Andres Cardenas
 */
public class DAOQualificationDrinkImpl extends DataBase implements IDAO<QualificationDrink> {

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
        String sql = "INSERT INTO calificacion_bebida (num_estrellas, comentario, idBebida, idCliente, fecha) VALUES (?, ?, ?, ?, ?);";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, data.getStars());
            st.setString(2, data.getComment());
            st.setInt(3, data.getIdDrink());
            st.setInt(4, data.getIdCustomer());
            st.setDate(5, Date.valueOf(data.getDate() != null ? data.getDate() : LocalDate.now()));
            st.executeUpdate();
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
                    resultQuery(rs, qualification);
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
                    resultQuery(rs, qualification);
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

    private void resultQuery(ResultSet rs, QualificationDrink qualification) throws SQLException {
        qualification.setCode(rs.getInt("id"));
        qualification.setStars(rs.getInt("num_estrellas"));
        qualification.setComment(rs.getString("comentario"));
        qualification.setIdDrink(rs.getInt("idBebida"));
        qualification.setIdCustomer(rs.getInt("idCliente"));
        qualification.setDate(rs.getDate("fecha").toLocalDate());
    }

}
