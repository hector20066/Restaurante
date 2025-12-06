package model.dao.impl;

import database.DataBase;
import model.dao.interfaces.IDAO;
import model.entities.Sale;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hectoracevedo
 */
public class IDAOSalesImpl extends DataBase implements IDAO<Sale> {

    private static IDAOSalesImpl instance;

    private IDAOSalesImpl() {

    }

    public static IDAOSalesImpl getInstance() {
        if (instance == null) {
            instance = new IDAOSalesImpl();
        }

        return instance;
    }

    @Override
    public void save(Sale data) throws Exception {
        String sql = "INSERT INTO sales(dishCode, amountDish, drinkCode, amountDrink, clientId, totalSale, date) VALUES (?, ?, ?, ?, ?, ?, ?);";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            if (data.getDishCode() != 0) {
                st.setInt(1, data.getDishCode());
                st.setInt(2, data.getAmountDish());
            } else {
                st.setNull(1, java.sql.Types.INTEGER);
                st.setNull(2, java.sql.Types.INTEGER);
            }

            if (data.getDrinkCode() != 0) {
                st.setInt(3, data.getDrinkCode());
                st.setInt(4, data.getAmountDrink());
            } else {
                st.setNull(3, java.sql.Types.INTEGER);
                st.setNull(4, java.sql.Types.INTEGER);
            }

            st.setInt(5, data.getClientId());
            st.setFloat(6, data.getTotalSale());
            st.setDate(7, Date.valueOf(data.getDate()));
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized Sale getData(int code) throws Exception {
        Sale sale = null;
        String sql = "SELECT * FROM sales WHERE numberSale = ? LIMIT 1;";
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
        String sql = "SELECT * FROM sales";
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
        String sql = "SELECT 1 FROM sales WHERE saleNumber = ?";
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
        sale.setSaleNumber(rs.getInt("numberSale"));
        Integer dish = rs.getObject("dishCode", Integer.class);
        Integer amountDish = rs.getObject("amountDish", Integer.class);
        Integer drink = rs.getObject("drinkCode", Integer.class);
        Integer amountDrink = rs.getObject("amountDrink", Integer.class);

        sale.setDishCode(dish == null ? 0 : dish);
        sale.setAmountDish(amountDish == null ? 0 : amountDish);

        sale.setDrinkCode(drink == null ? 0 : drink);
        sale.setAmountDrink(amountDrink == null ? 0 : amountDrink);

        sale.setClientId(rs.getInt("clientId"));
        sale.setTotalSale(rs.getFloat("totalSale"));
        sale.setDate(rs.getDate("date").toLocalDate());
    }

}
