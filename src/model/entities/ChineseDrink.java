package model.entities;

import model.enums.TypeFood;

/**
 *
 * @author Andres Cardenas
 */
public class ChineseDrink extends Drink {

    public ChineseDrink () {

    }

    public ChineseDrink (String name, int code, String description, float price) {
        super(name, code, description, price);
    }

    @Override
    public TypeFood getTypeDrink() {
        return TypeFood.CHINESE;
    }

}
