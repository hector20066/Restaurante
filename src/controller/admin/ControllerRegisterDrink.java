package controller.admin;

import factory.*;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.DAORestaurantFile;
import model.entities.Drink;
import model.enums.InstanceTypeDB;
import model.enums.InstanceTypeFile;
import model.strategy.DBDAOStrategy;
import model.strategy.FileDAOStrategy;
import model.strategy.RestaurantDAOManager;
import model.strategy.Strategy;
import observer.Subject;
import utils.MessageUtils;
import view.administrators.RegisterDrink;

import javax.swing.*;

/**
 *
 * @author hectoracevedo
 */
public class ControllerRegisterDrink {

    private int codeDrink;
    private boolean modify;

    private final RegisterDrink registerDrink;
    private final Subject subject;
    private final DAORestaurantDb<Drink> daoRestaurantDb;
    private final DAORestaurantFile<Drink> daoRestaurantFile;

    AbstractFactory factory;
    Strategy<Drink> strategy;
    RestaurantDAOManager<Drink> restaurantDAOManager;

    public ControllerRegisterDrink(RegisterDrink registerDrink, Subject subject) {
        this.registerDrink = registerDrink;
        this.subject = subject;

        this.daoRestaurantDb = FactoryMethod.getInstanceDB(InstanceTypeDB.DAODRINKDB);
        this.daoRestaurantFile = FactoryMethod.getInstanceFile(InstanceTypeFile.DAODRINKFILE);

        connectEvents();
    }

    private void connectEvents() {
        registerDrink.setControllerRegister(e -> register());
    }

    private void register() {
        try {
            String typeDrink = registerDrink.getCbo_typeDrink().getSelectedItem().toString();
            String nameDrink = registerDrink.getTxt_name().getText();
            String description = registerDrink.getTxt_description().getText();
            String price = registerDrink.getTxt_price().getText().trim();
            int waySave = registerDrink.getCbo_waySave().getSelectedIndex(); // 0: DB ; 1: File

            if (isInvalidData(nameDrink, description, price)) {
                MessageUtils.warningMessage("Por favor ingrese toda la informacion solicitada.");
                return;
            }

            if (typeDrink.equals("Colombiana")) {
                registerColombianDrink(nameDrink, description, Float.parseFloat(price), waySave);
            } else {
                if (typeDrink.equals("Italiana")) {
                    registerItalianDrink(nameDrink, description, Float.parseFloat(price), waySave);
                } else {
                    registerChineseDrink(nameDrink, description, Float.parseFloat(price), waySave);
                }
            }
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al tratar de registrar la bebida");
            e.printStackTrace();
        }
    }

    private boolean isInvalidData(String nameDrink, String description, String price) {
        return nameDrink.isBlank() || description.isBlank() || price.isBlank();
    }

    private void registerColombianDrink(String nameDrink, String description, float price, int waySave) throws Exception {
        factory = new ColombianRestaurant();
        Drink colombianDrink = factory.createDrink();
        assignValuesDrink(nameDrink, description, price, waySave, colombianDrink);

        new Thread(() -> {
            registerOfModify(colombianDrink);
            SwingUtilities.invokeLater(() -> runSwingUtilities("Colombiana"));
        }).start();
    }

    private void registerItalianDrink(String nameDrink, String description, float price, int waySave) {
        factory = new ItalianRestaurant();
        Drink italianDrink = factory.createDrink();
        assignValuesDrink(nameDrink, description, price, waySave, italianDrink);

        new Thread(() -> {
            registerOfModify(italianDrink);
            SwingUtilities.invokeLater(() -> runSwingUtilities("Italiana"));
        }).start();
    }

    private void registerChineseDrink(String nameDrink, String description, float price, int waySave) {
        factory = new ChineseRestaurant();
        Drink chineseDrink = factory.createDrink();
        assignValuesDrink(nameDrink, description, price, waySave, chineseDrink);

        new Thread(() -> {
            registerOfModify(chineseDrink);
            SwingUtilities.invokeLater(() -> runSwingUtilities("China"));
        }).start();
    }

    private void assignValuesDrink(String nameDrink, String description, float price, int waySave, Drink drink) {
        drink.setName(nameDrink);
        drink.setDescription(description);
        drink.setPrice(price);

        if (waySave == 0) { // Save to Data Base
            strategy = new DBDAOStrategy<>(daoRestaurantDb);
        } else { // Save to File
            strategy = new FileDAOStrategy<>(daoRestaurantFile);
        }
    }

    private void registerOfModify(Drink drink) {
        try {
            restaurantDAOManager = new RestaurantDAOManager<>(strategy);

            if (isModify()) {
                drink.setCode(codeDrink);
                restaurantDAOManager.modify(drink);
            } else {
                restaurantDAOManager.save(drink);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runSwingUtilities(String typeDrink) {
        MessageUtils.informationMessage("Se ha registrado la bebida " + typeDrink + " exitosamente.");
        clearFields();
        subject.nofifyObserver();
    }

    private void clearFields() {
        registerDrink.getTxt_name().setText("");
        registerDrink.getTxt_description().setText("");
        registerDrink.getTxt_price().setText("");
    }

    public void setCodeDrink(int codeDrink) {
        this.codeDrink = codeDrink;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public boolean isModify() {
        return modify;
    }

}
