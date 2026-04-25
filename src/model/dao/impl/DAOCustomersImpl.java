package model.dao.impl;

import database.DataBase;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.dao.interfaces.IDAO;
import model.entities.Customer;

/**
 *
 * @author Andres Cardenas
 */
public class DAOCustomersImpl extends DataBase implements IDAO<Customer> {

    private static DAOCustomersImpl instance;

    private DAOCustomersImpl() {

    }

    public static DAOCustomersImpl getInstance() {
        if (instance == null) {
            instance = new DAOCustomersImpl();
        }

        return instance;
    }

    @Override
    public void save(Customer data) throws Exception {
        String sql = "{CALL sp_registrar_cliente(?, ?, ?, ?)}";
        this.connect();

        try (CallableStatement cs = this.connection.prepareCall(sql)) {
            cs.setInt(1, data.getId());
            cs.setString(2, data.getName());
            cs.setString(3, data.getAddress());
            cs.setString(4, data.getEmail());
            cs.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public Customer getData(int code) throws Exception {
        Customer customer = null;
        String sql = "SELECT * FROM cliente WHERE id = ? LIMIT 1;";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, code);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    resultQuery(rs, customer);
                }
            }
        } finally {
            this.close();
        }

        return customer;
    }

    @Override
    public List<Customer> getData() throws Exception {
        List<Customer> list;
        String sql = "SELECT * FROM cliente";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            list = new ArrayList<>();

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    resultQuery(rs, customer);
                    list.add(customer);
                }
            }
        } finally {
            this.close();
        }

        return list;
    }

    @Override
    public boolean exists(int id) throws Exception {
        String sql = "SELECT 1 FROM cliente WHERE id = ?";
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

    private void resultQuery(ResultSet rs, Customer customer) throws SQLException {
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("nombre"));
        customer.setAddress(rs.getString("direccion"));
        customer.setEmail(rs.getString("correo"));
    }

}
