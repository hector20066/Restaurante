package controller.customers;

import model.entities.Dish;
import model.entities.Drink;
import model.enums.TypeFood;
import observer.Subject;
import view.customers.ColombianFood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerColombianFood extends AbstractFoodController  {

    private final ColombianFood colombianFood;
    private Subject subject;

    public ControllerColombianFood(ColombianFood colombianFood, Subject subject) {
        this.colombianFood = colombianFood;
        this.subject = subject;

        this.subject.register(this);

        connectEvents();
    }

    @Override
    protected void connectEvents() {
        colombianFood.setControllerAddOrder(e -> addOrder());
        colombianFood.setControllerRegisterSale(e -> registerSale());
        colombianFood.setControllerCbo_dish(e -> showDishInfo());
        colombianFood.setControllerCbo_drink(e -> showDrinkInfo());
    }

    @Override
    protected TypeFood getTypeFood() {
        return TypeFood.COLOMBIAN;
    }

    @Override
    protected JTextField getTxt_amountDish() {
        return colombianFood.getTxt_amountDish();
    }

    @Override
    protected JTextField getTxt_priceDish() {
        return colombianFood.getTxt_priceDish();
    }

    @Override
    protected JTextField getTxt_amountDrink() {
        return colombianFood.getTxt_amountDrink();
    }

    @Override
    protected JTextField getTxt_priceDrink() {
        return colombianFood.getTxt_priceDrink();
    }

    @Override
    protected JTextField getTxt_descriptionDish() {
        return colombianFood.getTxt_descriptionDish();
    }

    @Override
    protected JTextField getTxt_descriptionDrink() {
        return colombianFood.getTxt_descriptionDrink();
    }

    @Override
    protected JTextField getTxt_totalSale() {
        return colombianFood.getTxt_totalSale();
    }

    @Override
    protected JTextField getTxt_customerName() {
        return colombianFood.getTxt_customerName();
    }

    @Override
    protected JTextField getTxt_customerId() {
        return colombianFood.getTxt_customerId();
    }

    @Override
    protected JTextField getTxt_customerEmail() {
        return colombianFood.getTxt_customerEmail();
    }

    @Override
    protected JTextField getTxt_customerAddress() {
        return colombianFood.getTxt_customerAddress();
    }

    @Override
    protected JComboBox<String> getCbo_drink() {
        return colombianFood.getCbo_drink();
    }

    @Override
    protected JComboBox<String> getCbo_dish() {
        return colombianFood.getCbo_dish();
    }

    @Override
    protected JTable getJtbl_food() {
        return colombianFood.getJtbl_order();
    }

    @Override
    protected DefaultTableModel getTableModelFood() {
        return colombianFood.getTableModel();
    }

    @Override
    protected void loadDishData(Dish colombianDish) {
        colombianFood.getTxt_descriptionDish().setText(colombianDish.getDescription());
        colombianFood.getTxt_priceDish().setText(String.valueOf(colombianDish.getPrice()));
        dishCode = colombianDish.getCode();
    }

    @Override
    protected void loadDrinkData(Drink colombianDrink) {
        colombianFood.getTxt_descriptionDrink().setText(colombianDrink.getDescription());
        colombianFood.getTxt_priceDrink().setText(String.valueOf(colombianDrink.getPrice()));
        drinkCode = colombianDrink.getCode();
    }

}
