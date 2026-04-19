package model.entities;

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
    public String getTypeDrink() {
        return "Bebida Italiana";
    }

}
