package model.entities;

import java.math.BigDecimal;

/**
 *
 * @author Andres Cardenas
 */
public class SaleDetail {

    private int detailId;
    private int saleId;
    private Integer dishId;
    private Integer drinkId;
    private Integer dishAmount;
    private Integer drinkAmount;
    private BigDecimal dishPrice;
    private BigDecimal drinkPrice;

    public SaleDetail() {

    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public void setDrinkId(Integer drinkId) {
        this.drinkId = drinkId;
    }

    public void setDishAmount(Integer dishAmount) {
        this.dishAmount = dishAmount;
    }

    public void setDrinkAmount(Integer drinkAmount) {
        this.drinkAmount = drinkAmount;
    }

    public void setDishPrice(BigDecimal dishPrice) {
        this.dishPrice = dishPrice;
    }

    public void setDrinkPrice(BigDecimal drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

    public int getDetailId() {
        return detailId;
    }

    public int getSaleId() {
        return saleId;
    }

    public Integer getDishId() {
        return dishId;
    }

    public Integer getDrinkId() {
        return drinkId;
    }

    public Integer getDishAmount() {
        return dishAmount;
    }

    public Integer getDrinkAmount() {
        return drinkAmount;
    }

    public BigDecimal getDishPrice() {
        return dishPrice;
    }

    public BigDecimal getDrinkPrice() {
        return drinkPrice;
    }

}
