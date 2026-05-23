package model.dao;

import java.time.LocalDate;

/**
 *
 * @author Mi PC
 */
public class SaleDetailsDTO {

    private int numSale;
    private String customer;
    private String dish;
    private String drink;
    private int amountDish;
    private int amountDrink;
    private LocalDate date;
    private float totalSale;

    public SaleDetailsDTO() {

    }

    public int getNumSale() {
        return numSale;
    }

    public void setNumSale(int numSale) {
        this.numSale = numSale;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public int getAmountDish() {
        return amountDish;
    }

    public void setAmountDish(int amountDish) {
        this.amountDish = amountDish;
    }

    public int getAmountDrink() {
        return amountDrink;
    }

    public void setAmountDrink(int amountDrink) {
        this.amountDrink = amountDrink;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(float totalSale) {
        this.totalSale = totalSale;
    }

}
