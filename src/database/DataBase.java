package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andres Cardenas
 */
public class DataBase {

    protected final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    protected final String DB_URL = "jdbc:mysql://localhost:3306/restaurante?serverTimezone=America/Bogota&useSSL=false&allowPublicKeyRetrieval=true";
    private final String user = "root";
    private final String pass = "272006";
    protected Connection connection;

    public void connect() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}
