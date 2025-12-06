package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hectoracevedo
 */
public class DataBase {
    
    protected Connection connection;
    protected final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    protected final String DB_URL = "jdbc:mysql://www.hldevworks.com:3306/u311612716_restaurante?useSSL=false&requireSSL=false&allowPublicKeyRetrieval=true";
    
    private final String user = "u311612716_restaurante";
    private final String pass = "Restaurant$202627";
    
    public void connect() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void close() throws SQLException {
        if(connection != null) {
            connection.close();
        }
    }
    
}
