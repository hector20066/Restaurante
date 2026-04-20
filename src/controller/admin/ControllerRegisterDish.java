package controller.admin;

import factory.*;
import model.dao.impl.DAODishImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Dish;
import utils.MessageUtils;
import view.administrators.RegisterDish;

import javax.swing.*;
import observer.Subject;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerRegisterDish {

    private int codeDish;
    private boolean modify;

    private final RegisterDish registerDish;
    private final Subject subject;
    private final DAORestaurantDb<Dish> daoRestaurantDb;

    AbstractFactory factory;

    public ControllerRegisterDish(RegisterDish registerDish, Subject subject) {
        this.registerDish = registerDish;
        this.subject = subject;

        this.daoRestaurantDb = DAODishImpl.getInstance();

        connectEvents();
    }

    private void connectEvents() {
        registerDish.setControllerRegister(e -> register());
    }

    private void register() {
        try {
            String typeDish = (String) registerDish.getCbo_typeDish().getSelectedItem();
            String nameDish = registerDish.getTxt_name().getText();
            String description = registerDish.getTxt_description().getText();
            String price = registerDish.getTxt_price().getText().trim();

            if (isInvalidData(nameDish, description, price)) {
                MessageUtils.warningMessage("Por favor ingrese toda la informacion solicitada.");
                return;
            }

            if (typeDish.equals("Colombiano")) {
                registerColombianDish(nameDish, description, Float.parseFloat(price));
            } else {
                if (typeDish.equals("Italiano")) {
                    registerItalianDish(nameDish, description, Float.parseFloat(price));
                } else {
                    registerChineseDish(nameDish, description, Float.parseFloat(price));
                }
            }
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al realizar la accion");
        }
    }

    private boolean isInvalidData(String nameDish, String description, String price) {
        return nameDish.isBlank() || description.isBlank() || price.isBlank();
    }

    private void registerColombianDish(String nameDish, String description, float price) {
        factory = new ColombianRestaurant();
        Dish colombianDish = factory.createDish();
        assignValuesDish(nameDish, description, price, colombianDish);

        new Thread(() -> {
            registerOfModify(colombianDish);
            SwingUtilities.invokeLater(() -> runSwingUtilities("Colombiano"));
        }).start();
    }

    private void registerItalianDish(String nameDish, String description, float price) {
        factory = new ItalianRestaurant();
        Dish italianDish = factory.createDish();
        assignValuesDish(nameDish, description, price, italianDish);

        new Thread(() -> {
            registerOfModify(italianDish);
            SwingUtilities.invokeLater(() -> runSwingUtilities("Italiano"));
        }).start();
    }

    private void registerChineseDish(String nameDish, String description, float price) {
        factory = new ChineseRestaurant();
        Dish chineseDish = factory.createDish();
        assignValuesDish(nameDish, description, price, chineseDish);

        new Thread(() -> {
            registerOfModify(chineseDish);
            SwingUtilities.invokeLater(() -> runSwingUtilities("Chino"));
        }).start();
    }

    private void assignValuesDish(String nameDish, String description, float price, Dish dish) {
        dish.setName(nameDish);
        dish.setDescription(description);
        dish.setPrice(price);
    }

    private void registerOfModify(Dish dish) {
        try {

            if (isModify()) {
                dish.setCode(codeDish);
                daoRestaurantDb.modify(dish);
            } else {
                daoRestaurantDb.save(dish);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runSwingUtilities(String typeDish) {
        MessageUtils.informationMessage("Se ha registrado el plato " + typeDish + " correctamente.");
        clearFields();
        subject.nofifyObserver();
    }

    private void clearFields() {
        registerDish.getTxt_name().setText("");
        registerDish.getTxt_description().setText("");
        registerDish.getTxt_price().setText("");
    }

    public void setCodeDish(int codeDish) {
        this.codeDish = codeDish;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public boolean isModify() {
        return modify;
    }

}
