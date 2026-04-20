package model.entities;

import model.enums.TypeFood;

/**
 *
 * @author Andres Cardenas
 */
public class ItalianDrink extends Drink {

    public ItalianDrink () {

    }

    public ItalianDrink (String name, int code, String description, float price) {
        super(name, code, description, price);
    }

    @Override
    public TypeFood getTypeDrink() {
        return TypeFood.ITALIAN;
    }

}
