package controller.admin;

import factory.AbstractFactory;
import factory.ChineseRestaurant;
import factory.ColombianRestaurant;
import factory.ItalianRestaurant;
import model.dao.impl.DAODrinkImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Drink;
import observer.Subject;
import view.administrators.RegisterDrink;

import javax.swing.*;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerRegisterDrink {

    private final RegisterDrink registerDrink;
    private final Subject subject;
    private final DAORestaurantDb<Drink> daoRestaurantDb;
    private int codeDrink;
    private boolean modify;
    private AbstractFactory factory;

    public ControllerRegisterDrink(RegisterDrink registerDrink, Subject subject) {
        this.registerDrink = registerDrink;
        this.subject = subject;

        this.daoRestaurantDb = DAODrinkImpl.getInstance();

        connectEvents();
    }

    private void connectEvents() {
        registerDrink.setControllerRegister(e -> register());
    }

    private void register() {
        try {
            String typeDrink = (String) registerDrink.getCbo_typeDrink().getSelectedItem();
            String nameDrink = registerDrink.getTxt_name().getText();
            String description = registerDrink.getTxt_description().getText();
            String price = registerDrink.getTxt_price().getText().trim();

            if (isInvalidData(nameDrink, description, price, typeDrink)) {
                JOptionPane.showMessageDialog(registerDrink, "Por favor ingrese toda la información solicitada.", "", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (typeDrink.equals("Colombiana")) {
                registerColombianDrink(nameDrink, description, Float.parseFloat(price));
            } else {
                if (typeDrink.equals("Italiana")) {
                    registerItalianDrink(nameDrink, description, Float.parseFloat(price));
                } else {
                    registerChineseDrink(nameDrink, description, Float.parseFloat(price));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(registerDrink, "Ha ocurrido un error al tratar de registrar la bebida", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isInvalidData(String nameDrink, String description, String price, String typeDrink) {
        return nameDrink.isBlank() || description.isBlank() || price.isBlank() || typeDrink.isBlank();
    }

    private void registerColombianDrink(String nameDrink, String description, float price) throws Exception {
        factory = new ColombianRestaurant();
        Drink colombianDrink = factory.createDrink();
        assignValuesDrink(nameDrink, description, price, colombianDrink);

        try {
            registerOfModify(colombianDrink);
            runSwingUtilities("Colombiana");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(registerDrink, "No se ha podido registrar la bebida Colombiana", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerItalianDrink(String nameDrink, String description, float price) {
        factory = new ItalianRestaurant();
        Drink italianDrink = factory.createDrink();
        assignValuesDrink(nameDrink, description, price, italianDrink);

        try {
            registerOfModify(italianDrink);
            runSwingUtilities("Italiana");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(registerDrink, "No se ha podido registrar la bebida Italiana", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerChineseDrink(String nameDrink, String description, float price) {
        factory = new ChineseRestaurant();
        Drink chineseDrink = factory.createDrink();
        assignValuesDrink(nameDrink, description, price, chineseDrink);

        try {
            registerOfModify(chineseDrink);
            runSwingUtilities("China");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(registerDrink, "No se ha podido registrar la bebida China", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignValuesDrink(String nameDrink, String description, float price, Drink drink) {
        drink.setName(nameDrink);
        drink.setDescription(description);
        drink.setPrice(price);
    }

    private void registerOfModify(Drink drink) throws Exception {
        if (isModify()) {
            drink.setCode(codeDrink);
            daoRestaurantDb.modify(drink);
        } else {
            daoRestaurantDb.save(drink);
        }
    }

    private void runSwingUtilities(String typeDrink) {
        JOptionPane.showMessageDialog(registerDrink, "Se ha registrado la bebida " + typeDrink + " exitosamente.", "", JOptionPane.INFORMATION_MESSAGE);
        registerDrink.getTxt_name().setText("");
        registerDrink.getTxt_description().setText("");
        registerDrink.getTxt_price().setText("");
        subject.notifyObserver();
    }

    public void setCodeDrink(int codeDrink) {
        this.codeDrink = codeDrink;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

}
