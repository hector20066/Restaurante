package controller.customers;

import factory.FactoryMethod;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.IDAO;
import model.entities.Customer;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.Qualification;
import model.enums.InstanceTypeDB;
import observer.Subject;
import utils.MessageUtils;
import view.customers.RatingsPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author hectoracevedo
 */
public class ControllerRatingPanel {

    private final RatingsPanel ratingsPanel;
    private final Subject subject;

    private final IDAO<Qualification> daoQualification;
    private final IDAO<Customer> daoCustomer;
    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;

    private final JCheckBox[] starsDishes;
    private final JCheckBox[] starsDrinks;
    private final String icon = "/view/image/starEmpty.png";
    private final String selectedIcon = "/view/image/starQualification.png";

    public ControllerRatingPanel(RatingsPanel ratingsPanel, Subject subject) {
        this.ratingsPanel = ratingsPanel;
        this.subject = subject;

        this.daoQualification = FactoryMethod.getInstanceDB(InstanceTypeDB.QUALIFICATIONDB);
        this.daoCustomer = FactoryMethod.getInstanceDB(InstanceTypeDB.CUSTOMERDB);
        this.daoDish = FactoryMethod.getInstanceDB(InstanceTypeDB.DAODISHDB);
        this.daoDrink = FactoryMethod.getInstanceDB(InstanceTypeDB.DAODRINKDB);

        this.starsDishes = new JCheckBox[]{ratingsPanel.getCbx_starDish1(), ratingsPanel.getCbx_starDish2(), ratingsPanel.getCbx_starDish3(), ratingsPanel.getCbx_starDish4(), ratingsPanel.getCbx_starDish5()};
        this.starsDrinks = new JCheckBox[]{ratingsPanel.getCbx_starDrink1(), ratingsPanel.getCbx_starDrink2(), ratingsPanel.getCbx_starDrink3(), ratingsPanel.getCbx_starDrink4(), ratingsPanel.getCbx_starDrink5()};

        initStars(starsDishes);
        initStars(starsDrinks);
        connectEvents();
    }

    private void connectEvents() {
        ratingsPanel.setControllerQualificationDish(e -> qualificationDish());
        ratingsPanel.setControllerQualificationDrink(e -> qualificationDrink());
        ratingsPanel.setControllerCbo_typeDish(e -> showDishesType());
        ratingsPanel.setControllerCbo_typeDrink(e -> showDrinksType());

        addActionStars(starsDishes);
        addActionStars(starsDrinks);
    }

    private void initStars(JCheckBox[] stars) {
        for (JCheckBox star : stars) {
            star.setIcon(new ImageIcon(getClass().getResource(icon)));
            star.setSelectedIcon(new ImageIcon(getClass().getResource(selectedIcon)));
        }
    }

    private void addActionStars(JCheckBox[] stars) {
        for (int i = 0; i < stars.length; i++) {
            final int index = i;
            stars[i].addActionListener(e -> updateStars(index, stars));
        }
    }

    private void updateStars(int indexSelected, JCheckBox[] stars) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setSelected(i <= indexSelected);
        }
    }

    private void qualificationDish() {
        try {
            String idCustomer = ratingsPanel.getTxt_customerId().getText().trim();
            String dishName = ratingsPanel.getCbo_dish().getSelectedItem().toString();
            int selectedStars = 0;
            String comment = ratingsPanel.getTxa_opinionsDish().getText();

            if (idCustomer.isBlank()) {
                MessageUtils.warningMessage("Por favor ingrese su cedula para continuar con la calificacion.");
                return;
            }

            for (int i = 0; i < starsDishes.length; i++) {
                if (starsDishes[i].isSelected()) {
                    selectedStars = i + 1;
                }
            }

            if (selectedStars == 0) {
                MessageUtils.warningMessage("Por favor indique cuantas estrellas desea calificar");
                return;
            }

            saveRatingsDish(idCustomer, dishName, selectedStars, comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveRatingsDish(String idCustomer, String dishName, int selectedStars, String comment) {
        new Thread(() -> {
            try {
                if (!daoCustomer.exists(Integer.parseInt(idCustomer))) {
                    SwingUtilities.invokeLater(this::messageInvalidUser);
                    return;
                }

                Dish dish = daoDish.getData(dishName);
                int idDish = dish.getCode();

                Qualification qualification = new Qualification();
                assignValuesQualificationDish(qualification, idCustomer, idDish, selectedStars, comment);
                daoQualification.save(qualification);

                SwingUtilities.invokeLater(this::refreshWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void assignValuesQualificationDish(Qualification qualification, String idCustomer, int idDish, int selectedStars, String comment) {
        qualification.setStars(selectedStars);
        qualification.setComment(comment);
        qualification.setIdDish(idDish);
        qualification.setIdDrink(0);
        qualification.setIdCustomer(Integer.parseInt(idCustomer));
        qualification.setDate(LocalDate.now());
    }

    private void qualificationDrink() {
        try {
            String idCustomer = ratingsPanel.getTxt_customerId().getText().trim();
            String drinkName = ratingsPanel.getCbo_drink().getSelectedItem().toString();
            int selectedStars = 0;
            String comment = ratingsPanel.getTxa_opinionsDrink().getText();


            if (idCustomer.isBlank()) {
                MessageUtils.warningMessage("Por favor ingrese su cedula para continuar con la calificacion.");
                return;
            }

            for (int i = 0; i < starsDrinks.length; i++) {
                if (starsDrinks[i].isSelected()) {
                    selectedStars = i + 1;
                }
            }

            if (selectedStars == 0) {
                MessageUtils.warningMessage("Por favor indique cuantas estrellas desea calificar");
                return;
            }

            saveRatingsDrink(idCustomer, drinkName, selectedStars, comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveRatingsDrink(String idCustomer, String drinkName, int selectedStars, String comment) {
        new Thread(() -> {
            try {
                if (!daoCustomer.exists(Integer.parseInt(idCustomer))) {
                    SwingUtilities.invokeLater(this::messageInvalidUser);
                    return;
                }

                Drink drink = daoDrink.getData(drinkName);
                int idDrink = drink.getCode();

                Qualification qualification = new Qualification();
                assignValuesQualificationDrink(qualification, idCustomer, idDrink, selectedStars, comment);
                daoQualification.save(qualification);

                SwingUtilities.invokeLater(this::refreshWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void assignValuesQualificationDrink(Qualification qualification, String idCustomer, int idDrink, int selectedStars, String comment) {
        qualification.setStars(selectedStars);
        qualification.setComment(comment);
        qualification.setIdDish(0);
        qualification.setIdDrink(idDrink);
        qualification.setIdCustomer(Integer.parseInt(idCustomer));
        qualification.setDate(LocalDate.now());
    }

    private void messageInvalidUser() {
        MessageUtils.warningMessage("Debe haber realizado una compra antes para poder realizar una calificacion.\nNo se encontraron registros de este numero de cedula en el sistema");
    }

    private void refreshWindow() {
        MessageUtils.informationMessage("Gracias por su calificacion.");
        clearFieldsDrinks();
        clearFieldsDish();
        subject.nofifyObserver();
    }

    private void clearFieldsDish() {
        ratingsPanel.getTxt_customerId().setText("");
        ratingsPanel.getCbo_dish().setSelectedIndex(0);
        ratingsPanel.getCbo_typeDish().setSelectedIndex(0);
        ratingsPanel.getTxa_opinionsDish().setText("");

        for (JCheckBox starsDish : starsDishes) {
            starsDish.setSelected(false);
        }
    }

    private void clearFieldsDrinks() {
        ratingsPanel.getTxt_customerId().setText("");
        ratingsPanel.getCbo_drink().setSelectedIndex(0);
        ratingsPanel.getCbo_typeDrink().setSelectedIndex(0);
        ratingsPanel.getTxa_opinionsDrink().setText("");

        for (JCheckBox starsDrinks : starsDrinks) {
            starsDrinks.setSelected(false);
        }
    }

    private void showDishesType() {
        try {
            String type = ratingsPanel.getCbo_typeDish().getSelectedItem().toString();
            String typeDish = "";

            switch (type) {
                case "Colombiano" -> {
                    typeDish = "Plato Colombiano";
                }
                case "Italiano" -> {
                    typeDish = "Plato Italiano";
                }
                case "Chino" -> {
                    typeDish = "Plato Chino";
                }
            }

            loadDishType(typeDish);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.errorMessage("Ha ocurrido un error al cargar los platos.\nIntentelo nuevamente");
        }
    }

    private void loadDishType(String type) {
        new Thread(() -> {
            try {
                List<Dish> listDishes = daoDish.getData();

                SwingUtilities.invokeLater(() -> {
                    loadDishes(listDishes, type);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadDishes(List<Dish> dishes, String type) {
        ratingsPanel.getCbo_dish().removeAllItems();
        ratingsPanel.getCbo_dish().addItem("Seleccione un plato");

        for (Dish dish : dishes) {
            String nameDish = dish.getName();
            String typeDish = dish.getTypeDish();

            if (type.equals(typeDish)) {
                ratingsPanel.getCbo_dish().addItem(nameDish);
            }
        }
    }

    private void showDrinksType() {
        try {
            String type = ratingsPanel.getCbo_typeDrink().getSelectedItem().toString();
            String typeDrink = "";

            switch (type) {
                case "Colombiana" -> {
                    typeDrink = "Bebida Colombiana";
                }
                case "Italiana" -> {
                    typeDrink = "Bebida Italiana";
                }
                case "China" -> {
                    typeDrink = "Bebida China";
                }
            }

            loadDrinkType(typeDrink);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.errorMessage("Ha ocurrido un error al cargar las bebidas.\nIntentelo nuevamente");
        }
    }

    private void loadDrinkType(String type) {
        new Thread(() -> {
            try {
                List<Drink> listDrinks = daoDrink.getData();

                SwingUtilities.invokeLater(() -> {
                    loadDrinks(listDrinks, type);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadDrinks(List<Drink> drinks, String type) {
        ratingsPanel.getCbo_drink().removeAllItems();
        ratingsPanel.getCbo_drink().addItem("Seleccione la bebida");

        for (Drink drink : drinks) {
            String nameDrink = drink.getName();
            String typeDrink = drink.getTypeDrink();

            if (type.equals(typeDrink)) {
                ratingsPanel.getCbo_drink().addItem(nameDrink);
            }
        }
    }

}
