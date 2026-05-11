package model.dao.impl;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.DataBase;
import model.dao.interfaces.DAOWithAutoIncrement;
import model.entities.Sale;

/**
 *
 * @author Andres Cardenas
 */
public class DAOSalesImpl extends DataBase implements DAOWithAutoIncrement<Sale> {

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
        saveAndGetId(data);
    }

    @Override
    public int saveAndGetId(Sale data) throws Exception {
        String sql = "{CAll sp_registrar_venta(?, ?, ?)}";
        this.connect();
        int num_venta = 0;

        try (CallableStatement cs = this.connection.prepareCall(sql)) {
            cs.setInt(1, data.getClientId());
            cs.setDate(2, Date.valueOf(data.getDate()));
            cs.setFloat(3, data.getTotal());

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    num_venta = rs.getInt("num_venta");
                }
            }
        } finally {
            this.close();
        }

        return num_venta;
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
                    sale.setSaleNumber(rs.getInt("num_venta"));
                    sale.setClientId(rs.getInt("id_cliente"));
                    sale.setDate(rs.getDate("fecha").toLocalDate());
                    sale.setTotal(rs.getFloat("total"));
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
        String sql = "SELECT * FROM venta;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Sale sale = new Sale();
                    sale.setSaleNumber(rs.getInt("num_venta"));
                    sale.setClientId(rs.getInt("id_cliente"));
                    sale.setDate(rs.getDate("fecha").toLocalDate());
                    sale.setTotal(rs.getFloat("total"));
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

}
