package model.dao.impl;

import database.DataBase;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Dish;
import model.enums.TypeFood;

/**
 *
 * @author Andres Cardenas
 */
public class DAODishImpl extends DataBase implements DAORestaurantDb<Dish> {

    private static DAODishImpl instance;

    private DAODishImpl() {

    }

    public static DAODishImpl getInstance() {
        if (instance == null) {
            instance = new DAODishImpl();
        }

        return instance;
    }

    @Override
    public void save(Dish data) throws Exception {
        String sql = "{CALL sp_registrar_plato(?, ?, ?, ?)}";
        this.connect();

        try (CallableStatement cs = this.connection.prepareCall(sql)) {
            cs.setString(1, data.getName());
            cs.setString(2, data.getDescription());
            cs.setFloat(3, data.getPrice());
            cs.setString(4, String.valueOf(data.getTypeDish()));
            cs.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public synchronized Dish getData(int code) throws Exception {
        Dish dish = null;
        String sql = "SELECT * FROM vista_platos_disponibles WHERE codigo = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeFood type = TypeFood.valueOf(rs.getString("tipo_plato"));

                    dish = new Dish() {
                        @Override
                        public TypeFood getTypeDish() {
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
        String sql = "SELECT * FROM vista_platos_disponibles WHERE nombre = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setString(1, name);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeFood type = TypeFood.valueOf(rs.getString("tipo_plato"));

                    dish = new Dish() {
                        @Override
                        public TypeFood getTypeDish() {
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
        String sql = "SELECT * FROM vista_platos_disponibles;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    TypeFood type = TypeFood.valueOf(rs.getString("tipo_plato"));

                    Dish dish = new Dish() {
                        @Override
                        public TypeFood getTypeDish() {
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
        dish.setCode(rs.getInt("codigo"));
        dish.setName(rs.getString("nombre"));
        dish.setDescription(rs.getString("descripcion"));
        dish.setPrice(rs.getFloat("precio"));
    }

    @Override
    public void modify(Dish data) throws Exception {
        String sql = "UPDATE platos SET nombre = ?, descripcion = ?, precio = ? WHERE codigo = ?;";
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
        String sql = "DELETE FROM platos WHERE codigo = ?;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

}
