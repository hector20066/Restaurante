package controller.customers;

import model.entities.Dish;
import model.entities.Drink;
import observer.Subject;
import view.customers.ItalianFood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hectoracevedo
 */
public class ControllerItalianFood extends AbstractFoodController {

    private final ItalianFood italianFood;
    private final Subject subject;

    public ControllerItalianFood(ItalianFood italianFood, Subject subject) {
        this.italianFood = italianFood;
        this.subject = subject;

        this.subject.register(this);

        connectEvents();
    }

    @Override
    protected void connectEvents() {
        italianFood.setControllerAddOrder(e -> addOrder());
        italianFood.setControllerRegisterSale(e -> registerSale());
        italianFood.setControllerCbo_dish(e -> showDishInfo());
        italianFood.setControllerCbo_drink(e -> showDrinkInfo());
    }

    @Override
    protected String getDishType() {
        return "Plato Italiano";
    }

    @Override
    protected String getDrinkType() {
        return "Bebida Italiana";
    }

    @Override
    protected JTextField getTxt_amountDish() {
        return italianFood.getTxt_amountDish();
    }

    @Override
    protected JTextField getTxt_priceDish() {
        return italianFood.getTxt_priceDish();
    }

    @Override
    protected JTextField getTxt_amountDrink() {
        return italianFood.getTxt_amountDrink();
    }

    @Override
    protected JTextField getTxt_priceDrink() {
        return italianFood.getTxt_priceDrink();
    }

    @Override
    protected JTextField getTxt_descriptionDish() {
        return italianFood.getTxt_descriptionDish();
    }

    @Override
    protected JTextField getTxt_descriptionDrink() {
        return italianFood.getTxt_descriptionDrink();
    }

    @Override
    protected JTextField getTxt_totalSale() {
        return italianFood.getTxt_totalSale();
    }

    @Override
    protected JTextField getTxt_customerName() {
        return italianFood.getTxt_customerName();
    }

    @Override
    protected JTextField getTxt_customerId() {
        return italianFood.getTxt_customerId();
    }

    @Override
    protected JTextField getTxt_customerEmail() {
        return italianFood.getTxt_customerEmail();
    }

    @Override
    protected JTextField getTxt_customerAddress() {
        return italianFood.getTxt_customerAddress();
    }

    @Override
    protected JComboBox<String> getCbo_drink() {
        return italianFood.getCbo_drink();
    }

    @Override
    protected JComboBox<String> getCbo_dish() {
        return italianFood.getCbo_dish();
    }

    @Override
    protected JTable getJtbl_food() {
        return italianFood.getJtbl_order();
    }

    @Override
    protected DefaultTableModel getTableModelFood() {
        return italianFood.getTableModel();
    }

    @Override
    protected void loadDishData(Dish italianDish) {
        italianFood.getTxt_descriptionDish().setText(italianDish.getDescription());
        italianFood.getTxt_priceDish().setText(String.valueOf(italianDish.getPrice()));
        dishCode = italianDish.getCode();
    }

    @Override
    protected void loadDrinkData(Drink italianDrink) {
        italianFood.getTxt_descriptionDrink().setText(italianDrink.getDescription());
        italianFood.getTxt_priceDrink().setText(String.valueOf(italianDrink.getPrice()));
        drinkCode = italianDrink.getCode();
    }

}
