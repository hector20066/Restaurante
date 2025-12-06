package controller.admin;

import factory.*;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.DAORestaurantFile;
import model.entities.Dish;
import model.enums.InstanceTypeDB;
import model.enums.InstanceTypeFile;
import model.strategy.DBDAOStrategy;
import model.strategy.FileDAOStrategy;
import model.strategy.RestaurantDAOManager;
import model.strategy.Strategy;
import observer.Subject;
import utils.MessageUtils;
import view.administrators.RegisterDish;

import javax.swing.*;

/**
 *
 * @author hectoracevedo
 */
public class ControllerRegisterDish {

    private int codeDish;
    private boolean modify;

    private final RegisterDish registerDish;
    private final Subject subject;
    private final DAORestaurantDb<Dish> daoRestaurantDb;
    private final DAORestaurantFile<Dish> daoRestaurantFile;

    AbstractFactory factory;
    Strategy<Dish> strategy;
    RestaurantDAOManager<Dish> restaurantDAOManager;

    public ControllerRegisterDish(RegisterDish registerDish, Subject subject) {
        this.registerDish = registerDish;
        this.subject = subject;

        this.daoRestaurantDb = FactoryMethod.getInstanceDB(InstanceTypeDB.DAODISHDB);
        this.daoRestaurantFile = FactoryMethod.getInstanceFile(InstanceTypeFile.DAODISHFILE);

        connectEvents();
    }

    private void connectEvents() {
        registerDish.setControllerRegister(e -> register());
    }

    private void register() {
        try {
            String typeDish = registerDish.getCbo_typeDish().getSelectedItem().toString();
            String nameDish = registerDish.getTxt_name().getText();
            String description = registerDish.getTxt_description().getText();
            String price = registerDish.getTxt_price().getText().trim();
            int waySave = registerDish.getCbo_waySave().getSelectedIndex();

            if (isInvalidData(nameDish, description, price)) {
                MessageUtils.warningMessage("Por favor ingrese toda la informacion solicitada.");
                return;
            }

            if (typeDish.equals("Colombiano")) {
                registerColombianDish(nameDish, description, Float.parseFloat(price), waySave);
            } else {
                if (typeDish.equals("Italiano")) {
                    registerItalianDish(nameDish, description, Float.parseFloat(price), waySave);
                } else {
                    registerChineseDish(nameDish, description, Float.parseFloat(price), waySave);
                }
            }
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al realizar la accion");
        }
    }

    private boolean isInvalidData(String nameDish, String description, String price) {
        return nameDish.isBlank() || description.isBlank() || price.isBlank();
    }

    private void registerColombianDish(String nameDish, String description, float price, int waySave) {
        factory = new ColombianRestaurant();
        Dish colombianDish = factory.createDish();
        assignValuesDish(nameDish, description, price, waySave, colombianDish);

        new Thread(() -> {
            registerOfModify(colombianDish);
            SwingUtilities.invokeLater(() -> runSwingUtilities("Colombiano"));
        }).start();
    }

    private void registerItalianDish(String nameDish, String description, float price, int waySave) {
        factory = new ItalianRestaurant();
        Dish italianDish = factory.createDish();
        assignValuesDish(nameDish, description, price, waySave, italianDish);

        new Thread(() -> {
            registerOfModify(italianDish);
            SwingUtilities.invokeLater(() -> runSwingUtilities("Italiano"));
        }).start();
    }

    private void registerChineseDish(String nameDish, String description, float price, int waySave) {
        factory = new ChineseRestaurant();
        Dish chineseDish = factory.createDish();
        assignValuesDish(nameDish, description, price, waySave, chineseDish);

        new Thread(() -> {
            registerOfModify(chineseDish);
            SwingUtilities.invokeLater(() -> runSwingUtilities("Chino"));
        }).start();
    }

    private void assignValuesDish(String nameDish, String description, float price, int waySave, Dish dish) {
        dish.setName(nameDish);
        dish.setDescription(description);
        dish.setPrice(price);

        if (waySave == 0) { // Save to Data Base
            strategy = new DBDAOStrategy<>(daoRestaurantDb);
        } else { // Safe to file
            strategy = new FileDAOStrategy<>(daoRestaurantFile);
        }
    }

    private void registerOfModify(Dish dish) {
        try {
            restaurantDAOManager = new RestaurantDAOManager<>(strategy);

            if (isModify()) {
                dish.setCode(codeDish);
                restaurantDAOManager.modify(dish);
            } else {
                restaurantDAOManager.save(dish);
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
