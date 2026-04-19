package factory;

import model.entities.ColombianDish;
import model.entities.ColombianDrink;
import model.entities.Dish;
import model.entities.Drink;

/**
 *
 * @author Andres Cardenas
 */
public class ColombianRestaurant implements AbstractFactory {

    @Override
    public Dish createDish() {
        return new ColombianDish();
    }

    @Override
    public Drink createDrink() {
        return new ColombianDrink();
    }

}
