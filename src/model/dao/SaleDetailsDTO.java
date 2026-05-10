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

    public void setNumSale(int numSale) {
        this.numSale = numSale;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public void setQuantityDishesSold(int quantityDishesSold) {
        this.quantityDishesSold = quantityDishesSold;
    }

    public void setQuantityDrinksSold(int quantityDrinksSold) {
        this.quantityDrinksSold = quantityDrinksSold;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotalSale(float totalSale) {
        this.totalSale = totalSale;
    }

    public int getNumSale() {
        return numSale;
    }

    public String getCustomer() {
        return customer;
    }

    public String getDish() {
        return dish;
    }

    public String getDrink() {
        return drink;
    }

    public int getQuantityDishesSold() {
        return quantityDishesSold;
    }

    public int getQuantityDrinksSold() {
        return quantityDrinksSold;
    }

    public LocalDate getDate() {
        return date;
    }

    public float getTotalSale() {
        return totalSale;
    }
    
}
