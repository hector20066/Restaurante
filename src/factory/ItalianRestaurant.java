package factory;

import model.entities.Dish;
import model.entities.Drink;
import model.entities.ItalianDish;
import model.entities.ItalianDrink;

/**
 *
 * @author Andres Cardenas
 */
public class ItalianRestaurant implements AbstractFactory {

    @Override
    public Dish createDish() {
        return new ItalianDish();
    }

    @Override
    public Drink createDrink() {
        return new ItalianDrink();
    }

}
