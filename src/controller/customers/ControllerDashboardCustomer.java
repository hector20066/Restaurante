package controller.customers;

import model.dao.impl.DAODishImpl;
import model.dao.impl.DAODrinkImpl;
import model.dao.impl.DAOQualificationImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.IDAO;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.Qualification;
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

    private final IDAO<Qualification> daoQualification;
    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;

    public ControllerDashboardCustomer(DashboardCustomers dashboardCustomers, Subject subject) {
        this.dashboardCustomers = dashboardCustomers;

        this.daoQualification = DAOQualificationImpl.getInstance();
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
                List<Qualification> ratings = daoQualification.getData();
                dashboardCustomers.cleanPanel();

                for (Qualification qualification : ratings) {
                    if (qualification.getStars() >= 3) {
                        Dish dish = daoDish.getData(qualification.getIdDish());
                        Drink drink = daoDrink.getData(qualification.getIdDrink());
                        String nameFood;

                        if (dish == null && drink != null) {
                            nameFood = drink.getName();
                        } else {
                            nameFood = dish.getName();
                        }

                        String finalNameFood = nameFood;
                        SwingUtilities.invokeLater(() -> {
                            dashboardCustomers.addRatingPanel(finalNameFood, qualification.getStars());
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
