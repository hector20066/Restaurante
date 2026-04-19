package model.entities;

/**
 *
 * @author Andres Cardenas
 */
public class ColombianDrink extends Drink {

    public ColombianDrink () {

    }

    public ColombianDrink (String name, int code, String description, float price) {
        super(name, code, description, price);
    }

    @Override
    public String getTypeDrink() {
        return "Bebida Colombiana";
    }

}
