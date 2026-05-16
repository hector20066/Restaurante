package model.dao.impl;

import database.DataBase;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Drink;
import model.enums.TypeFood;

import java.sql.CallableStatement;
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
        String sql = "{CALL sp_registrar_bebida(?, ?, ?, ?)}";
        this.connect();

        try (CallableStatement cs = this.connection.prepareCall(sql)) {
            cs.setString(1, data.getName());
            cs.setString(2, data.getDescription());
            cs.setFloat(3, data.getPrice());
            cs.setString(4, String.valueOf(data.getTypeDrink()));
            cs.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized Drink getData(int code) throws Exception {
        Drink drink = null;
        String sql = "SELECT * FROM vista_bebidas_disponibles WHERE codigo = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeFood typeDrink = TypeFood.valueOf(rs.getString("tipo"));

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
        String sql = "SELECT * FROM vista_bebidas_disponibles WHERE nombre = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, name);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeFood typeDrink = TypeFood.valueOf(rs.getString("tipo"));

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
        String sql = "SELECT * FROM vista_bebidas_disponibles;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    TypeFood typeDrink = TypeFood.valueOf(rs.getString("tipo"));

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
        drink.setCode(rs.getInt("codigo"));
        drink.setName(rs.getString("nombre"));
        drink.setDescription(rs.getString("descripcion"));
        drink.setPrice(rs.getFloat("precio"));
    }

    @Override
    public void modify(Drink data) throws Exception {
        String sql = "UPDATE bebidas SET nombre = ?, descripcion = ?, precio = ? WHERE codigo = ?";
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
        String sql = "DELETE FROM bebidas WHERE codigo = ?";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

}
