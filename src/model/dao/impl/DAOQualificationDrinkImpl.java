package model.dao.impl;

import database.DataBase;
import model.dao.interfaces.IDAO;
import model.entities.Qualification;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andres Cardenas
 */
public class DAOQualificationImpl extends DataBase implements IDAO<Qualification> {

    private static DAOQualificationImpl instance;

    private DAOQualificationImpl() {

    }

    public static DAOQualificationImpl getInstance() {
        if (instance == null) {
            instance = new DAOQualificationImpl();
        }

        return instance;
    }

    @Override
    public void save(Qualification data) throws Exception {
        this.connect();

        try {
            if (data.getIdDish() != 0) {
                String sql = "INSERT INTO calificacion_plato (num_estrellas, comentario, idPlato, idCliente, fecha) VALUES (?, ?, ?, ?, ?);";
                try (PreparedStatement st = this.connection.prepareStatement(sql)) {
                    st.setInt(1, data.getStars());
                    st.setString(2, data.getComment());
                    st.setInt(3, data.getIdDish());
                    st.setInt(4, data.getIdCustomer());
                    st.setDate(5, Date.valueOf(data.getDate() != null ? data.getDate() : LocalDate.now()));
                    st.executeUpdate();
                }
            } else if (data.getIdDrink() != 0) {
                String sql = "INSERT INTO calificacion_bebida (num_estrellas, comentario, idBebida, idCliente, fecha) VALUES (?, ?, ?, ?, ?);";
                try (PreparedStatement st = this.connection.prepareStatement(sql)) {
                    st.setInt(1, data.getStars());
                    st.setString(2, data.getComment());
                    st.setInt(3, data.getIdDrink());
                    st.setInt(4, data.getIdCustomer());
                    st.setDate(5, Date.valueOf(data.getDate() != null ? data.getDate() : LocalDate.now()));
                    st.executeUpdate();
                }
            }
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized Qualification getData(int code) throws Exception {
        Qualification qualification = null;
        this.connect();

        try {
            // Intentar buscar en calificacion_plato primero
            String sql = "SELECT id, num_estrellas, comentario, idPlato, idCliente, fecha FROM calificacion_plato WHERE id = ? LIMIT 1;";
            try (PreparedStatement st = this.connection.prepareStatement(sql)) {
                st.setInt(1, code);

                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        qualification = new Qualification();
                        qualification.setCode(rs.getInt("id"));
                        qualification.setStars(rs.getInt("num_estrellas"));
                        qualification.setComment(rs.getString("comentario"));
                        qualification.setIdDish(rs.getInt("idPlato"));
                        qualification.setIdDrink(0);
                        qualification.setIdCustomer(rs.getInt("idCliente"));
                        qualification.setDate(rs.getDate("fecha").toLocalDate());
                        return qualification;
                    }
                }
            } = new ArrayList<>();
        this.connect();

        try {
            // Obtener calificaciones de platos
            String sql = "SELECT id, num_estrellas, comentario, idPlato, 0 as idBebida, idCliente, fecha FROM calificacion_plato " +
                         "UNION ALL " +
                         "SELECT id, num_estrellas, comentario, 0 as idPlato, idBebida, idCliente, fecha FROM calificacion_bebida " +
                         "ORDER BY num_estrellas DESC;";

            try (PreparedStatement st = this.connection.prepareStatement(sql)) {
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        Qualification qualification = new Qualification();
                        qualification.setCode(rs.getInt("id"));
                        qualification.setStars(rs.getInt("num_estrellas"));
                        qualification.setComment(rs.getString("comentario"));

                        Integer dishId = rs.getInt("idPlato");
                        Integer drinkId = rs.getInt("idBebida");

                        qualification.setIdDish(dishId != null && dishId != 0 ? dishId : 0);
                        qualification.setIdDrink(drinkId != null && drinkId != 0 ? drinkId : 0);
                        qualification.setIdCustomer(rs.getInt("idCliente"));
                        qualification.setDate(rs.getDate("fecha").toLocalDate());

                        list.add(qualification);
                    }rs(rs.getInt("num_esid"));
        qualification.setStars(rs.getInt("num_estrellas"));
        qualification.setComment(rs.getString("comentario"));

        Integer dishId = rs.getObject("idPlato", Integer.class);
        Integer drinkId = rs.getObject("idBebida", Integer.class);

        qualification.setIdDish(dishId != null ? dishId : 0);
        qualification.setIdDrink(drinkId != null ? drinkId : 0);
        qualification.setIdCustomer(rs.getInt("idCliente"));
        qualification.setDate(rs.getDate("fecha
        }

        return qualification;
    }

    @Override
    public synchronized List<Qualification> getData() throws Exception {
        List<Qualification> list;
        String sql = "SELECT * FROM qualifications ORDER BY stars DESC;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Qualification qualification = new Qualification();
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
        return false; // Not applicable in this case
    }

    private void resultQuery(ResultSet rs, Qualification qualification) throws SQLException {
        qualification.setCode(rs.getInt("code"));
        qualification.setStars(rs.getInt("stars"));
        qualification.setComment(rs.getString("comment"));

        Integer dishCode = rs.getObject("idDish", Integer.class);
        Integer drinkCode = rs.getObject("idDrink", Integer.class);

        qualification.setIdDish(dishCode == null ? 0 : dishCode);
        qualification.setIdDrink(drinkCode == null ? 0 : drinkCode);
        qualification.setIdCustomer(rs.getInt("idCustomer"));
        qualification.setDate(rs.getDate("date").toLocalDate());
    }

}
