package model.entities;

/**
 *
 * @author Andres Cardenas
 */
public class ChineseDish extends Dish {

    public ChineseDish () {

    }

    public ChineseDish (String name, int code, String description, float price) {
        super(name, code, description, price);
    }

    @Override
    public String getTypeDish() {
        return "Plato Chino";
    }

}
