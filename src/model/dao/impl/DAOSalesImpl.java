package model.dao.impl;

import database.DataBase;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dao.interfaces.IDAO;
import model.entities.Sale;

/**
 *
 * @author Andres Cardenas
 */
public class DAOSalesImpl extends DataBase implements IDAO<Sale> {

    private static DAOSalesImpl instance;

    private DAOSalesImpl() {

    }

    public static DAOSalesImpl getInstance() {
        if (instance == null) {
            instance = new DAOSalesImpl();
        }

        return instance;
    }

    @Override
    public void save(Sale data) throws Exception {
        String sql = "INSERT INTO venta(id_cliente, fecha, total) VALUES (?, ?, ?);";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, data.getClientId());
            st.setDate(2, Date.valueOf(data.getDate()));
            st.setFloat(3, data.getTotal());
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized Sale getData(int code) throws Exception {
        Sale sale = null;
        String sql = "SELECT * FROM venta WHERE num_venta = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    sale = new Sale();
                    resultQuery(rs, sale);
                }
            }
        } finally {
            this.close();
        }

        return sale;
    }

    @Override
    public synchronized List<Sale> getData() throws Exception {
        List<Sale> list;
        String sql = "SELECT * FROM venta";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Sale sale = new Sale();
                    resultQuery(rs, sale);
                    list.add(sale);
                }
            }
        } finally {
            this.close();
        }

        return list;
    }

    @Override
    public boolean exists(int id) throws Exception {
        String sql = "SELECT 1 FROM venta WHERE num_venta = ?";
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

    private void resultQuery(ResultSet rs, Sale sale) throws SQLException {
        sale.setSaleNumber(rs.getInt("num_venta"));
        sale.setClientId(rs.getInt("id_cliente"));
        sale.setDate(rs.getDate("fecha").toLocalDate());
        sale.setTotal(rs.getFloat("total"));
    }

}
