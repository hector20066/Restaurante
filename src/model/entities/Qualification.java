package model.entities;

import java.time.LocalDate;

/**
 *
 * @author Andres Cardenas
 */
public class Qualification {

    private int code;
    private int stars;
    private String comment;
    private int idDish;
    private int idDrink;
    private int idCustomer;
    private LocalDate date;

    public Qualification() {

    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setIdDish(int idDish) {
        this.idDish = idDish;
    }

    public void setIdDrink(int idDrink) {
        this.idDrink = idDrink;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCode() {
        return code;
    }

    public int getStars() {
        return stars;
    }

    public String getComment() {
        return comment;
    }

    public int getIdDish() {
        return idDish;
    }

    public int getIdDrink() {
        return idDrink;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public LocalDate getDate() {
        return date;
    }

}
