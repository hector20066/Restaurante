package model.entities;

import model.enums.TypeFood;

/**
 *
 * @author Andres Cardenas
 */
public class ColombianDish extends Dish {

    public ColombianDish() {

    }

    public ColombianDish(String name, int code, String description, float price) {
        super(name, code, description, price);
    }

    @Override
    public TypeFood getTypeDish() {
        return TypeFood.COLOMBIAN;
    }

}
