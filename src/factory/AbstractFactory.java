package factory;

import model.entities.Dish;
import model.entities.Drink;

/**
 *
 * @author Andres Cardenas
 */
public interface AbstractFactory {

    Dish createDish();
    Drink createDrink();

}
