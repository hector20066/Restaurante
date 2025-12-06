package service;

/**
 *
 * @author hectoracevedo
 */
public class LoginService {
    
    private final String userAdmin = "Admin123";
    private final String passwordAdmin = "Admin";
    
    private static LoginService instance;
    
    private LoginService () {
        
    }
    
    public static LoginService getInstance () {
        if (instance == null) {
            instance = new LoginService();
        }
        
        return instance;
    }
    
    public boolean login(String user, String password) {
        return userAdmin.equals(user) && passwordAdmin.equals(password);
    }
    
}
