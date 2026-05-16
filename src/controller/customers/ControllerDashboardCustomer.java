package controller.customers;

import model.dao.impl.DAODishImpl;
import model.dao.impl.DAODrinkImpl;
import model.dao.impl.DAOQualificationDishDB;
import model.dao.impl.DAOQualificationDrinkImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.IDAO;
import model.entities.*;
import observer.Observer;
import observer.Subject;
import view.customers.DashboardCustomers;

import javax.swing.*;
import java.util.List;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerDashboardCustomer implements Observer {

    private final DashboardCustomers dashboardCustomers;

    private final IDAO<QualificationDrink> daoQualificationDrink;
    private final IDAO<QualificationDish> daoQualificationDish;
    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;

    public ControllerDashboardCustomer(DashboardCustomers dashboardCustomers, Subject subject) {
        this.dashboardCustomers = dashboardCustomers;

        this.daoQualificationDrink = DAOQualificationDrinkImpl.getInstance();
        this.daoQualificationDish = DAOQualificationDishDB.getInstance();
        this.daoDish = DAODishImpl.getInstance();
        this.daoDrink = DAODrinkImpl.getInstance();

        subject.register(this);
    }

    @Override
    public void update() {
        loadRatings();
    }

    private void loadRatings() {
        new Thread(()-> {
            try {
                dashboardCustomers.cleanPanel();

                // Cargar calificaciones de platos
                List<QualificationDish> dishRatings = daoQualificationDish.getData();
                for (QualificationDish qualification : dishRatings) {
                    if (qualification.getStars() >= 3) {
                        Dish dish = daoDish.getData(qualification.getIdDish());
                        if (dish != null) {
                            String dishName = dish.getName();
                            SwingUtilities.invokeLater(() -> {
                                dashboardCustomers.addRatingPanelDish(dishName, qualification.getStars());
                            });
                        }
                    }
                }

                // Cargar calificaciones de bebidas
                List<QualificationDrink> drinkRatings = daoQualificationDrink.getData();
                for (QualificationDrink qualification : drinkRatings) {
                    if (qualification.getStars() >= 3) {
                        Drink drink = daoDrink.getData(qualification.getIdDrink());
                        if (drink != null) {
                            String drinkName = drink.getName();
                            SwingUtilities.invokeLater(() -> {
                                dashboardCustomers.addRatingPanelDrink(drinkName, qualification.getStars());
                            });
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
