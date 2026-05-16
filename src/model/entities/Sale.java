package model.entities;

import java.time.LocalDate;

/**
 *
 * @author Andres Cardenas
 */
public class Sale {

    private int saleNumber;
    private int clientId;
    private LocalDate date;
    private float total;

    public Sale() {

    }

    public int getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(int saleNumber) {
        this.saleNumber = saleNumber;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

}
