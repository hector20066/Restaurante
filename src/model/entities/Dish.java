package model.entities;

import model.enums.TypeFood;

/**
 *
 * @author Andres Cardenas
 */
public abstract class Dish {

    protected String name;
    protected int code;
    protected String description;
    protected float price;

    public Dish() {

    }

    public Dish(String name, int code, String description, float price) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public abstract TypeFood getTypeDish();

}
