package controller.customers;

import model.entities.Dish;
import model.entities.Drink;
import model.enums.TypeFood;
import observer.Subject;
import view.customers.ChineseFood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerChineseFood extends AbstractFoodController {

    private final ChineseFood chineseFood;

    public ControllerChineseFood(ChineseFood chineseFood, Subject subject) {
        super();
        this.chineseFood = chineseFood;
        subject.register(this);

        connectEvents();
    }

    @Override
    protected void connectEvents() {
        chineseFood.setControllerAddOrder(e -> addOrder());
        chineseFood.setControllerRegisterSale(e -> registerSale());
        chineseFood.setControllerCbo_dish(e -> showDishInfo());
        chineseFood.setControllerCbo_drink(e -> showDrinkInfo());
    }

    @Override
    protected TypeFood getTypeFood() {
        return TypeFood.CHINESE;
    }

    @Override
    protected JTextField getTxt_amountDish() {
        return chineseFood.getTxt_amountDish();
    }

    @Override
    protected JTextField getTxt_priceDish() {
        return chineseFood.getTxt_priceDish();
    }

    @Override
    protected JTextField getTxt_amountDrink() {
        return chineseFood.getTxt_amountDrink();
    }

    @Override
    protected JTextField getTxt_priceDrink() {
        return chineseFood.getTxt_priceDrink();
    }

    @Override
    protected JTextField getTxt_descriptionDish() {
        return chineseFood.getTxt_descriptionDish();
    }

    @Override
    protected JTextField getTxt_descriptionDrink() {
        return chineseFood.getTxt_descriptionDrink();
    }

    @Override
    protected JTextField getTxt_totalSale() {
        return chineseFood.getTxt_totalSale();
    }

    @Override
    protected JTextField getTxt_customerName() {
        return chineseFood.getTxt_customerName();
    }

    @Override
    protected JTextField getTxt_customerId() {
        return chineseFood.getTxt_customerId();
    }

    @Override
    protected JTextField getTxt_customerEmail() {
        return chineseFood.getTxt_customerEmail();
    }

    @Override
    protected JTextField getTxt_customerAddress() {
        return chineseFood.getTxt_customerAddress();
    }

    @Override
    protected JComboBox<String> getCbo_drink() {
        return chineseFood.getCbo_drink();
    }

    @Override
    protected JComboBox<String> getCbo_dish() {
        return chineseFood.getCbo_dish();
    }

    @Override
    protected JTable getJtbl_food() {
        return chineseFood.getJtbl_order();
    }

    @Override
    protected DefaultTableModel getTableModelFood() {
        return chineseFood.getTableModel();
    }

    @Override
    protected void loadDishData(Dish chineseDish) {
        chineseFood.getTxt_descriptionDish().setText(chineseDish.getDescription());
        chineseFood.getTxt_priceDish().setText(String.valueOf(chineseDish.getPrice()));
        dishCode = chineseDish.getCode();
    }

    @Override
    protected void loadDrinkData(Drink chineseDrink) {
        chineseFood.getTxt_descriptionDrink().setText(chineseDrink.getDescription());
        chineseFood.getTxt_priceDrink().setText(String.valueOf(chineseDrink.getPrice()));
        drinkCode = chineseDrink.getCode();
    }

}
