package model.dao.impl;

import database.DataBase;
import model.dao.interfaces.IDAO;
import model.entities.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hectoracevedo
 */
public class IDAOCustomersImpl extends DataBase implements IDAO<Customer> {

    private static IDAOCustomersImpl instance;

    private IDAOCustomersImpl() {

    }

    public static IDAOCustomersImpl getInstance() {
        if (instance == null) {
            instance = new IDAOCustomersImpl();
        }

        return instance;
    }

    @Override
    public void save(Customer data) throws Exception {
        String sql = "INSERT INTO customer (id, name, address, email) VALUES (?, ?, ?, ?);";
        this.connect();

        try (PreparedStatement st = this.connection.prepareStatement(sql)) {
            st.setInt(1, data.getId());
            st.setString(2, data.getName());
            st.setString(3, data.getAddress());
            st.setString(4, data.getEmail());
            st.executeUpdate();
        } finally {
            this.close();
        }
    }

    @Override
    public Customer getData(int code) throws Exception {
        Customer customer = null;
        String sql = "SELECT * FROM customer WHERE id = ? LIMIT 1;";
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
    public synchronized List<Customer> getData() throws Exception {
        List<Customer> list;
        String sql = "SELECT * FROM customer";
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
        String sql = "SELECT 1 FROM customer WHERE id = ?";
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
        customer.setName(rs.getString("name"));
        customer.setAddress(rs.getString("address"));
        customer.setEmail(rs.getString("email"));
    }

}
