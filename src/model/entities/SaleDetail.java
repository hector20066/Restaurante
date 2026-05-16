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

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public Integer getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(Integer drinkId) {
        this.drinkId = drinkId;
    }

    public Integer getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(Integer dishAmount) {
        this.dishAmount = dishAmount;
    }

    public Integer getDrinkAmount() {
        return drinkAmount;
    }

    public void setDrinkAmount(Integer drinkAmount) {
        this.drinkAmount = drinkAmount;
    }

    public BigDecimal getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(BigDecimal dishPrice) {
        this.dishPrice = dishPrice;
    }

    public BigDecimal getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(BigDecimal drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

}
