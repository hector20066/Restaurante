package controller.customers;

import factory.FactoryMethod;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.IDAO;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.Qualification;
import model.enums.InstanceTypeDB;
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
    private final Subject subject;

    private final IDAO<Qualification> daoQualification;
    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;

    public ControllerDashboardCustomer(DashboardCustomers dashboardCustomers, Subject subject) {
        this.dashboardCustomers = dashboardCustomers;
        this.subject = subject;

        this.daoQualification = FactoryMethod.getInstanceDB(InstanceTypeDB.QUALIFICATIONDB);
        this.daoDish = FactoryMethod.getInstanceDB(InstanceTypeDB.DAODISHDB);
        this.daoDrink = FactoryMethod.getInstanceDB(InstanceTypeDB.DAODRINKDB);

        this.subject.register(this);
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
