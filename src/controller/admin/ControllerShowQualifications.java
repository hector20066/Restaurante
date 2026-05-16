package controller.admin;

import model.dao.impl.DAODishImpl;
import model.dao.impl.DAODrinkImpl;
import model.dao.impl.DAOQualificationDishDB;
import model.dao.impl.DAOQualificationDrinkImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.IDAO;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.QualificationDish;
import model.entities.QualificationDrink;
import observer.Observer;
import observer.Subject;
import view.administrators.ShowQualifications;

import javax.swing.*;
import java.util.List;

public class ControllerShowQualifications implements Observer {

    private final ShowQualifications showQualifications;

    private final IDAO<QualificationDrink> daoQualificationDrink;
    private final IDAO<QualificationDish> daoQualificationDish;
    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;

    public ControllerShowQualifications(ShowQualifications showQualifications, Subject subject) {
        this.showQualifications = showQualifications;

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
                showQualifications.cleanPanel();

                List<QualificationDish> dishRatings = daoQualificationDish.getData();

                for (QualificationDish qualification : dishRatings) {
                    Dish dish = daoDish.getData(qualification.getIdDish());

                    if (dish != null) {
                        String dishName = dish.getName();
                        int numStars = qualification.getStars();
                        String comment = qualification.getComment();

                        SwingUtilities.invokeLater(() -> showQualifications.addRatingPanelDish(dishName, numStars, comment));
                    }
                }

                List<QualificationDrink> drinkRatings = daoQualificationDrink.getData();

                for (QualificationDrink qualification : drinkRatings) {
                    Drink drink = daoDrink.getData(qualification.getIdDrink());

                    if (drink != null) {
                        String drinkName = drink.getName();
                        int numStars = qualification.getStars();
                        String comment = qualification.getComment();

                        SwingUtilities.invokeLater(() -> {
                            showQualifications.addRatingPanelDrink(drinkName, numStars, comment);
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
