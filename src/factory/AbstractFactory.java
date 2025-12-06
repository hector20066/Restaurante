package factory;

import model.entities.Dish;
import model.entities.Drink;

/**
 *
 * @author hectoracevedo
 */
public interface AbstractFactory {

    Dish createDish();
    Drink createDrink();

}
