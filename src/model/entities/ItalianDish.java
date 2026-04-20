package model.entities;

import model.enums.TypeFood;

/**
 *
 * @author Andres Cardenas
 */
public class ItalianDish extends Dish {

    public ItalianDish () {

    }

    public ItalianDish (String name, int code, String description, float price) {
        super(name, code, description, price);
    }

    @Override
    public TypeFood getTypeDish() {
        return TypeFood.ITALIAN;
    }

}
