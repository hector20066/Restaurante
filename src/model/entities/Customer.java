package model.entities;

/**
 *
 * @author hectoracevedo
 */
public class Customer {

    private String name;
    private int id;
    private String email;
    private String address;

    public Customer () {

    }

    public void setName (String name) {
        this.name = name;
    }

    public void setId (int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress (String address) {
        this.address = address;
    }

    public String getName () {
        return name;
    }

    public int getId () {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress () {
        return address;
    }

}
