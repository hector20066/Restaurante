/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    private int quantityDishesSold;
    private int quantityDrinksSold;
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

    public int getQuantityDishesSold() {
        return quantityDishesSold;
    }

    public void setQuantityDishesSold(int quantityDishesSold) {
        this.quantityDishesSold = quantityDishesSold;
    }

    public int getQuantityDrinksSold() {
        return quantityDrinksSold;
    }

    public void setQuantityDrinksSold(int quantityDrinksSold) {
        this.quantityDrinksSold = quantityDrinksSold;
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
