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
 * @author hectoracevedo
 */
public class IDAOQualificationImpl extends DataBase implements IDAO<Qualification> {

    private static IDAOQualificationImpl instance;

    private IDAOQualificationImpl() {

    }

    public static IDAOQualificationImpl getInstance() {
        if (instance == null) {
            instance = new IDAOQualificationImpl();
        }

        return instance;
    }

    @Override
    public void save(Qualification data) throws Exception {
        String sql = "INSERT INTO qualifications (stars, comment, idDish, idDrink, idCustomer, date) VALUES (?, ?, ?, ?, ?, ?);";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, data.getStars());
            st.setString(2, data.getComment());

            if (data.getIdDish() != 0) {
                st.setInt(3, data.getIdDish());
            } else {
                st.setNull(3, Types.INTEGER);
            }

            if (data.getIdDrink() != 0) {
                st.setInt(4, data.getIdDrink());
            } else {
                st.setNull(4, Types.INTEGER);
            }

            st.setInt(5, data.getIdCustomer());
            st.setDate(6, Date.valueOf(LocalDate.now()));
            st.executeUpdate();
        }
    }

    @Override
    public synchronized Qualification getData(int code) throws Exception {
        Qualification qualification = null;
        String sql = "SELECT * FROM qualifications WHERE code = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    qualification = new Qualification();
                    resultQuery(rs, qualification);
                }
            }
        } finally {
            this.close();
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
