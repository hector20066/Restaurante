package model.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DataBase;
import model.dao.interfaces.IDAO;
import model.entities.SaleDetail;

/**
 *
 * @author Andres Cardenas
 */
public class DAOSaleDetailImpl extends DataBase implements IDAO<SaleDetail> {

    private static DAOSaleDetailImpl instance;

    private DAOSaleDetailImpl() {

    }

    public static DAOSaleDetailImpl getInstance() {
        if (instance == null) {
            instance = new DAOSaleDetailImpl();
        }

        return instance;
    }

    @Override
    public void save(SaleDetail data) throws Exception {
        String sql = "INSERT INTO detalle_venta(idVenta, idPlato, idBebida, cantPlato, cantBebida, precioPlato, precioBebida) VALUES (?, ?, ?, ?, ?, ?, ?);";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, data.getSaleId());
            st.setObject(2, data.getDishId());
            st.setObject(3, data.getDrinkId());
            st.setObject(4, data.getDishAmount());
            st.setObject(5, data.getDrinkAmount());
            st.setObject(6, data.getDishPrice());
            st.setObject(7, data.getDrinkPrice());
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized SaleDetail getData(int code) throws Exception {
        SaleDetail saleDetail = null;
        String sql = "SELECT * FROM detalle_venta WHERE idDetalle = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    saleDetail = new SaleDetail();
                    resultQuery(rs, saleDetail);
                }
            }
        } finally {
            this.close();
        }

        return saleDetail;
    }

    @Override
    public synchronized List<SaleDetail> getData() throws Exception {
        List<SaleDetail> list;
        String sql = "SELECT * FROM detalle_venta";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    SaleDetail saleDetail = new SaleDetail();
                    resultQuery(rs, saleDetail);
                    list.add(saleDetail);
                }
            }
        } finally {
            this.close();
        }

        return list;
    }

    @Override
    public boolean exists(int id) throws Exception {
        String sql = "SELECT 1 FROM detalle_venta WHERE idDetalle = ?";
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

    private void resultQuery(ResultSet rs, SaleDetail saleDetail) throws SQLException {
        saleDetail.setDetailId(rs.getInt("idDetalle"));
        saleDetail.setSaleId(rs.getInt("idVenta"));
        saleDetail.setDishId((Integer) rs.getObject("idPlato"));
        saleDetail.setDrinkId((Integer) rs.getObject("idBebida"));
        saleDetail.setDishAmount((Integer) rs.getObject("cantPlato"));
        saleDetail.setDrinkAmount((Integer) rs.getObject("cantBebida"));
        saleDetail.setDishPrice(rs.getBigDecimal("precioPlato"));
        saleDetail.setDrinkPrice(rs.getBigDecimal("precioBebida"));
    }

}
