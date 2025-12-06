package model.dao.impl;

import database.DataBase;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Dish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hectoracevedo
 */
public class DAODishDBImpl extends DataBase implements DAORestaurantDb<Dish> {

    private static DAODishDBImpl instance;

    private DAODishDBImpl() {

    }

    public static DAODishDBImpl getInstance() {
        if (instance == null) {
            instance = new DAODishDBImpl();
        }

        return instance;
    }

    @Override
    public void save(Dish data) throws Exception {
        String sql = "INSERT INTO dishes(name, description, price, typeDish) VALUES (?, ?, ?, ?);";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, data.getName());
            st.setString(2, data.getDescription());
            st.setFloat(3, data.getPrice());
            st.setString(4, data.getTypeDish());
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized Dish getData(int code) throws Exception {
        Dish dish = null;
        String sql = "SELECT * FROM dishes WHERE code = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("typeDish");

                    dish = new Dish() {
                        @Override
                        public String getTypeDish() {
                            return type;
                        }
                    };

                    resultQuery(dish, rs);
                }
            }
        } finally {
            this.close();
        }

        return dish;
    }

    @Override
    public synchronized Dish getData(String name) throws Exception {
        Dish dish = null;
        String sql = "SELECT * FROM dishes WHERE name = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, name);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("typeDish");

                    dish = new Dish() {
                        @Override
                        public String getTypeDish() {
                            return type;
                        }
                    };

                    resultQuery(dish, rs);
                }
            }
        } finally {
            this.close();
        }

        return dish;
    }

    @Override
    public synchronized List<Dish> getData() throws Exception {
        List<Dish> list;
        String sql = "SELECT * FROM dishes;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("typeDish");

                    Dish dish = new Dish() {
                        @Override
                        public String getTypeDish() {
                            return type;
                        }
                    };

                    resultQuery(dish, rs);
                    list.add(dish);
                }
            }
        } finally {
            this.close();
        }

        return list;
    }

    private void resultQuery(Dish dish, ResultSet rs) throws SQLException {
        dish.setCode(rs.getInt("code"));
        dish.setName(rs.getString("name"));
        dish.setDescription(rs.getString("description"));
        dish.setPrice(rs.getFloat("price"));
    }

    @Override
    public void modify(Dish data) throws Exception {
        String sql = "UPDATE dishes SET name = ?, description = ?, price = ? WHERE code = ?;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, data.getName());
            st.setString(2, data.getDescription());
            st.setFloat(3, data.getPrice());
            st.setInt(4, data.getCode());
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public void delete(int code) throws Exception {
        String sql = "DELETE FROM dishes WHERE code = ?;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

}
