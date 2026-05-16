package model.dao.impl;

import database.DataBase;
import model.dao.SaleDetailsDTO;
import model.dao.interfaces.IDAOSalesDetails;
import model.entities.SaleDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andres Cardenas
 */
public class DAOSaleDetailImpl extends DataBase implements IDAOSalesDetails {

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
    public synchronized SaleDetail getData(int idVenta) throws Exception {
        SaleDetail saleDetail = null;
        String sql = "SELECT * FROM detalle_venta WHERE idVenta = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, idVenta);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    saleDetail = new SaleDetail();
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
                    saleDetail.setDetailId(rs.getInt("idDetalle"));
                    saleDetail.setSaleId(rs.getInt("idVenta"));
                    saleDetail.setDishId((Integer) rs.getObject("idPlato"));
                    saleDetail.setDrinkId((Integer) rs.getObject("idBebida"));
                    saleDetail.setDishAmount((Integer) rs.getObject("cantPlato"));
                    saleDetail.setDrinkAmount((Integer) rs.getObject("cantBebida"));
                    saleDetail.setDishPrice(rs.getBigDecimal("precioPlato"));
                    saleDetail.setDrinkPrice(rs.getBigDecimal("precioBebida"));
                    list.add(saleDetail);
                }
            }
        } finally {
            this.close();
        }

        return list;
    }

    @Override
    public List<SaleDetailsDTO> getAllData() throws Exception {
        List<SaleDetailsDTO> list;
        String sql = """
                SELECT
                   v.num_venta,
                   c.nombre AS cliente,
                   p.nombre AS plato,
                   d.cantPlato,
                   b.nombre AS bebida,
                   d.cantBebida,
                   v.fecha,
                   v.total
                FROM detalle_venta d
                INNER JOIN venta v ON d.idVenta = v.num_venta
                INNER JOIN cliente c ON v.id_cliente = c.id
                LEFT JOIN platos p ON d.idPlato = p.codigo
                LEFT JOIN bebidas b ON d.idBebida = b.codigo;
                """;
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    SaleDetailsDTO sale = new SaleDetailsDTO();
                    sale.setNumSale(rs.getInt("num_venta"));
                    sale.setCustomer(rs.getString("cliente"));
                    sale.setDish(rs.getString("plato"));
                    sale.setQuantityDishesSold(rs.getInt("cantPlato"));
                    sale.setDrink(rs.getString("bebida"));
                    sale.setQuantityDrinksSold(rs.getInt("cantBebida"));
                    sale.setDate(rs.getDate("fecha").toLocalDate());
                    sale.setTotalSale(rs.getFloat("total"));
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

}
