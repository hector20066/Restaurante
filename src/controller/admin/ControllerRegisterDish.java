package controller.admin;

import factory.*;
import model.dao.impl.DAODishImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Dish;
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

    private AbstractFactory factory;

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

            if (isInvalidData(nameDish, description, price, typeDish)) {
                JOptionPane.showMessageDialog(registerDish, "Por favor ingrese toda la informacion solicitada.", "", JOptionPane.WARNING_MESSAGE);
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(registerDish, "Ha ocurrido un error al realizar la acción", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isInvalidData(String nameDish, String description, String price, String typeDish) {
        return nameDish.isBlank() || description.isBlank() || price.isBlank() || typeDish.isBlank();
    }

    private void registerColombianDish(String nameDish, String description, float price) {
        factory = new ColombianRestaurant();
        Dish colombianDish = factory.createDish();
        assignValuesDish(nameDish, description, price, colombianDish);

        try {
            registerOfModify(colombianDish);
            runSwingUtilities("Colombiano");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(registerDish, "No se ha podido registrar el plato Colombiano", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerItalianDish(String nameDish, String description, float price) {
        factory = new ItalianRestaurant();
        Dish italianDish = factory.createDish();
        assignValuesDish(nameDish, description, price, italianDish);

        try {
            registerOfModify(italianDish);
            runSwingUtilities("Italiano");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(registerDish, "No se ha podido registrar el plato Italiano", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerChineseDish(String nameDish, String description, float price) {
        factory = new ChineseRestaurant();
        Dish chineseDish = factory.createDish();
        assignValuesDish(nameDish, description, price, chineseDish);

        try {
            registerOfModify(chineseDish);
            runSwingUtilities("Chino");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(registerDish, "No se ha podido registrar el plato Chino", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignValuesDish(String nameDish, String description, float price, Dish dish) {
        dish.setName(nameDish);
        dish.setDescription(description);
        dish.setPrice(price);
    }

    private void registerOfModify(Dish dish) throws Exception {
        if (isModify()) {
            dish.setCode(codeDish);
            daoRestaurantDb.modify(dish);
        } else {
            daoRestaurantDb.save(dish);
        }
    }

    private void runSwingUtilities(String typeDish) {
        JOptionPane.showMessageDialog(registerDish, "Se ha registrado el plato " + typeDish + " correctamente.", "", JOptionPane.INFORMATION_MESSAGE);
        registerDish.getTxt_name().setText("");
        registerDish.getTxt_description().setText("");
        registerDish.getTxt_price().setText("");
        subject.notifyObserver();
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
