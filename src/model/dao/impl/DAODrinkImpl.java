package model.dao.impl;

import database.DataBase;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Drink;
import model.enums.TypeFood;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andres Cardenas
 */
public class DAODrinkImpl extends DataBase implements DAORestaurantDb<Drink> {

    private static DAODrinkImpl instance;

    private DAODrinkImpl() {

    }

    public static DAODrinkImpl getInstance() {
        if (instance == null) {
            instance = new DAODrinkImpl();
        }

        return instance;
    }

    @Override
    public void save(Drink data) throws Exception {
        String sql = "INSERT INTO drinks(name, description, price, typeDrink) VALUES (?, ?, ?, ?);";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, data.getName());
            st.setString(2, data.getDescription());
            st.setFloat(3, data.getPrice());
            st.setString(4, String.valueOf(data.getTypeDrink()));
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized Drink getData(int code) throws Exception {
        Drink drink = null;
        String sql = "SELECT * FROM drinks WHERE code = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeFood typeDrink = TypeFood.valueOf(rs.getString("typeDrink"));

                    drink = new Drink() {
                        @Override
                        public TypeFood getTypeDrink() {
                            return typeDrink;
                        }
                    };

                    resultQuery(drink, rs);
                }
            }
        } finally {
            this.close();
        }

        return drink;
    }

    @Override
    public synchronized Drink getData(String name) throws Exception {
        Drink drink = null;
        String sql = "SELECT * FROM drinks WHERE name = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, name);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeFood typeDrink = TypeFood.valueOf(rs.getString("typeDrink"));

                    drink = new Drink() {
                        @Override
                        public TypeFood getTypeDrink() {
                            return typeDrink;
                        }
                    };

                    resultQuery(drink, rs);
                }
            }
        } finally {
            this.close();
        }

        return drink;
    }

    @Override
    public synchronized List<Drink> getData() throws Exception {
        List<Drink> list;
        String sql = "SELECT * FROM drinks;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    TypeFood typeDrink = TypeFood.valueOf(rs.getString("typeDrink"));

                    Drink drink = new Drink() {
                        @Override
                        public TypeFood getTypeDrink() {
                            return typeDrink;
                        }
                    };

                    resultQuery(drink, rs);
                    list.add(drink);
                }
            }
        } finally {
            this.close();
        }

        return list;
    }

    private void resultQuery(Drink drink, ResultSet rs) throws SQLException {
        drink.setCode(rs.getInt("code"));
        drink.setName(rs.getString("name"));
        drink.setDescription(rs.getString("description"));
        drink.setPrice(rs.getFloat("price"));
    }

    @Override
    public void modify(Drink data) throws Exception {
        String sql = "UPDATE drinks SET name = ?, description = ?, price = ? WHERE code = ?";
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
        String sql = "DELETE FROM drinks WHERE code = ?";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

}
