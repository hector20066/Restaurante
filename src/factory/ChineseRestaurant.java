package factory;

import model.entities.ChineseDish;
import model.entities.ChineseDrink;
import model.entities.Dish;
import model.entities.Drink;

/**
 *
 * @author Andres Cardenas
 */
public class ChineseRestaurant implements AbstractFactory {

    @Override
    public Dish createDish() {
        return new ChineseDish();
    }

    @Override
    public Drink createDrink() {
        return new ChineseDrink();
    }

}
