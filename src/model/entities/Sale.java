package model.entities;

import java.time.LocalDate;

/**
 *
 * @author hectoracevedo
 */
public class Sale {

    private int saleNumber;
    private Integer dishCode;
    private int amountDish;
    private Integer drinkCode;
    private int amountDrink;
    private int clientId;
    private float totalSale;
    private LocalDate date;

    public Sale () {

    }

    public void setSaleNumber (int saleNumber) {
        this.saleNumber = saleNumber;
    }

    public void setDishCode (Integer dishCode) {
        this.dishCode = dishCode;
    }

    public void setAmountDish(int amountDish) {
        this.amountDish = amountDish;
    }

    public void setDrinkCode (Integer drinkCode) {
        this.drinkCode = drinkCode;
    }

    public void setAmountDrink(int amountDrink) {
        this.amountDrink = amountDrink;
    }

    public void setClientId (int clientId) {
        this.clientId = clientId;
    }

    public void setTotalSale (float totalSale) {
        this.totalSale = totalSale;
    }

    public void setDate (LocalDate date) {
        this.date = date;
    }

    public int getSaleNumber () {
        return saleNumber;
    }

    public Integer getDishCode () {
        return dishCode;
    }

    public int getAmountDish() {
        return amountDish;
    }

    public Integer getDrinkCode () {
        return drinkCode;
    }

    public int getAmountDrink() {
        return amountDrink;
    }

    public int getClientId () {
        return clientId;
    }

    public float getTotalSale () {
        return totalSale;
    }

    public LocalDate getDate () {
        return date;
    }

}
