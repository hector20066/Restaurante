package controller.customers;

import java.time.LocalDate;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.dao.impl.DAOCustomersImpl;
import model.dao.impl.DAODishImpl;
import model.dao.impl.DAODrinkImpl;
import model.dao.impl.DAOQualificationDishDB;
import model.dao.impl.DAOQualificationDrinkImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.IDAO;
import model.dao.interfaces.IDAOQualification;
import model.entities.Customer;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.QualificationDish;
import model.entities.QualificationDrink;
import model.enums.TypeFood;
import observer.Subject;
import view.customers.RatingsPanel;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerRatingPanel {

    private final RatingsPanel ratingsPanel;
    private final Subject subject;

    private final IDAOQualification<QualificationDish> daoQualificationDish;
    private final IDAOQualification<QualificationDrink> daoQualificationDrink;
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

        this.daoQualificationDish = DAOQualificationDishDB.getInstance();
        this.daoQualificationDrink = DAOQualificationDrinkImpl.getInstance();
        this.daoCustomer = DAOCustomersImpl.getInstance();
        this.daoDish = DAODishImpl.getInstance();
        this.daoDrink = DAODrinkImpl.getInstance();

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
                JOptionPane.showMessageDialog(ratingsPanel, "Por favor ingrese su cedula para continuar con la calificacion.", null, JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (int i = 0; i < starsDishes.length; i++) {
                if (starsDishes[i].isSelected()) {
                    selectedStars = i + 1;
                }
            }

            if (selectedStars == 0) {
                JOptionPane.showMessageDialog(ratingsPanel, "Por favor indique cuantas estrellas desea calificar", null, JOptionPane.WARNING_MESSAGE);
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
                int idCustomerInt = Integer.parseInt(idCustomer);

                QualificationDish existingQualification = daoQualificationDish.findByCustomerAndProduct(idCustomerInt, idDish);

                if (existingQualification != null) {
                    existingQualification.setStars(selectedStars);
                    existingQualification.setComment(comment);
                    existingQualification.setDate(LocalDate.now());
                    daoQualificationDish.modify(existingQualification);
                } else {
                    QualificationDish qualification = new QualificationDish();
                    assignValuesQualificationDish(qualification, idCustomer, idDish, selectedStars, comment);
                    daoQualificationDish.save(qualification);
                }

                SwingUtilities.invokeLater(this::refreshWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void assignValuesQualificationDish(QualificationDish qualification, String idCustomer, int idDish, int selectedStars, String comment) {
        qualification.setStars(selectedStars);
        qualification.setComment(comment);
        qualification.setIdDish(idDish);
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
                JOptionPane.showMessageDialog(ratingsPanel, "Por favor ingrese su cedula para continuar con la calificacion.", null, JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (int i = 0; i < starsDrinks.length; i++) {
                if (starsDrinks[i].isSelected()) {
                    selectedStars = i + 1;
                }
            }

            if (selectedStars == 0) {
                JOptionPane.showMessageDialog(ratingsPanel, "Por favor indique cuantas estrellas desea calificar", null, JOptionPane.WARNING_MESSAGE);
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
                int idCustomerInt = Integer.parseInt(idCustomer);

                QualificationDrink existingQualification = daoQualificationDrink.findByCustomerAndProduct(idCustomerInt, idDrink);

                if (existingQualification != null) {
                    existingQualification.setStars(selectedStars);
                    existingQualification.setComment(comment);
                    existingQualification.setDate(LocalDate.now());
                    daoQualificationDrink.modify(existingQualification);
                } else {
                    QualificationDrink qualification = new QualificationDrink();
                    assignValuesQualificationDrink(qualification, idCustomer, idDrink, selectedStars, comment);
                    daoQualificationDrink.save(qualification);
                }

                SwingUtilities.invokeLater(this::refreshWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void assignValuesQualificationDrink(QualificationDrink qualification, String idCustomer, int idDrink, int selectedStars, String comment) {
        qualification.setStars(selectedStars);
        qualification.setComment(comment);
        qualification.setIdDrink(idDrink);
        qualification.setIdCustomer(Integer.parseInt(idCustomer));
        qualification.setDate(LocalDate.now());
    }

    private void messageInvalidUser() {
        JOptionPane.showMessageDialog(ratingsPanel,
                "Debe haber realizado una compra antes para poder realizar una calificacion.\nNo se encontraron registros de este numero de cedula en el sistema",
                null, JOptionPane.WARNING_MESSAGE);
    }

    private void refreshWindow() {
        JOptionPane.showMessageDialog(ratingsPanel, "Gracias por su calificación.", null, JOptionPane.INFORMATION_MESSAGE);
        clearFieldsDrinks();
        clearFieldsDish();
        subject.notifyObserver();
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
            String type = (String) ratingsPanel.getCbo_typeDish().getSelectedItem();
            TypeFood typeDish = null;

            switch (type) {
                case "Colombiano" -> {
                    typeDish = TypeFood.COLOMBIAN;
                }
                case "Italiano" -> {
                    typeDish = TypeFood.ITALIAN;
                }
                case "Chino" -> {
                    typeDish = TypeFood.CHINESE;
                }
            }

            loadDishType(typeDish);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ratingsPanel, "Ha ocurrido un error al cargar los platos.\nInténtelo nuevamente", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDishType(TypeFood type) {
        new Thread(() -> {
            try {
                List<Dish> listDishes = daoDish.getData();

                SwingUtilities.invokeLater(() -> {
                    loadDishes(listDishes, type);
                });
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ratingsPanel, "Error al cargar los platos", null, JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void loadDishes(List<Dish> dishes, TypeFood type) {
        ratingsPanel.getCbo_dish().removeAllItems();
        ratingsPanel.getCbo_dish().addItem("Seleccione un plato");

        for (Dish dish : dishes) {
            String nameDish = dish.getName();
            TypeFood typeDish = dish.getTypeDish();

            if (type.equals(typeDish)) {
                ratingsPanel.getCbo_dish().addItem(nameDish);
            }
        }
    }

    private void showDrinksType() {
        try {
            String type = ratingsPanel.getCbo_typeDrink().getSelectedItem().toString();
            TypeFood typeDrink = null;

            switch (type) {
                case "Colombiana" -> {
                    typeDrink = TypeFood.COLOMBIAN;
                }
                case "Italiana" -> {
                    typeDrink = TypeFood.ITALIAN;
                }
                case "China" -> {
                    typeDrink = TypeFood.CHINESE;
                }
            }

            loadDrinkType(typeDrink);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ratingsPanel, "Ha ocurrido un error al cargar las bebidas.\nIntentelo nuevamente", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDrinkType(TypeFood type) {
        new Thread(() -> {
            try {
                List<Drink> listDrinks = daoDrink.getData();

                SwingUtilities.invokeLater(() -> {
                    loadDrinks(listDrinks, type);
                });
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ratingsPanel, "Error al cargar las bebidas", null, JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void loadDrinks(List<Drink> drinks, TypeFood type) {
        ratingsPanel.getCbo_drink().removeAllItems();
        ratingsPanel.getCbo_drink().addItem("Seleccione la bebida");

        for (Drink drink : drinks) {
            String nameDrink = drink.getName();
            TypeFood typeDrink = drink.getTypeDrink();

            if (type.equals(typeDrink)) {
                ratingsPanel.getCbo_drink().addItem(nameDrink);
            }
        }
    }

}
