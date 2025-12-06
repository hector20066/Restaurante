package controller;

import builder.controllers.ControllerAdminPanelBuilder;
import builder.controllers.ControllerLoginBuilder;
import builder.controllers.ControllerUserPanelBuilder;
import builder.controllers.ControllerViewLogsDbBuilder;
import controller.admin.*;
import controller.customers.*;
import factory.FactoryMethod;
import model.dao.interfaces.DAORestaurantFile;
import model.entities.Dish;
import model.entities.Drink;
import model.enums.InstanceTypeFile;
import observer.ConcreteSubject;
import view.LogIn;
import view.administrators.*;
import view.customers.*;

/**
 *
 * @author hectoracevedo
 */
public class Coordinator {

    private ConcreteSubject subject;
    private LogIn login;

    private AdminPanel adminPanel;
    private UserPanel userPanel;

    // Admin Panels
    private DashboardAdmin dashboardAdmin;
    private RegisterDish registerDish;
    private RegisterDrink registerDrink;
    private ViewLogsDB viewLogsDB;
    private ViewLogsFile viewLogsFile;

    // Users Panels
    private DashboardCustomers dashboardCustomers;
    private ChineseFood chineseFood;
    private ItalianFood italianFood;
    private ColombianFood colombianFood;
    private RatingsPanel ratingsPanel;

    // Drivers Admin
    private ControllerLogin controllerLogin;
    private ControllerAdminPanel controllerAdminPanel;
    private ControllerRegisterDish controllerRegisterDish;
    private ControllerRegisterDrink controllerRegisterDrink;
    private ControllerViewLogsDB controllerViewLogsDB;
    private ControllerViewLogsFile controllerViewLogsFile;
    private ControllerDashboard controllerDashboard;

    // Drivers Users
    private ControllerDashboardCustomer controllerDashboardCustomer;
    private ControllerUsersPanel controllerUsersPanel;
    private ControllerColombianFood controllerColombianFood;
    private ControllerChineseFood controllerChineseFood;
    private ControllerItalianFood controllerItalianFood;
    private ControllerRatingPanel controllerRatingPanel;

    public void startApp() {
        createFiles();
        startObserver();
        startViews();
        startDrivers();

        java.awt.EventQueue.invokeLater(() -> {
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            login.setResizable(false);
        });
    }

    private void startObserver() {
        subject = ConcreteSubject.getInstance();
    }

    private void startViews() {
        login = new LogIn();
        adminPanel = new AdminPanel();
        userPanel = new UserPanel();
        dashboardCustomers = new DashboardCustomers();
        dashboardAdmin = new DashboardAdmin();
        registerDish = new RegisterDish();
        registerDrink = new RegisterDrink();
        viewLogsDB = new ViewLogsDB();
        viewLogsFile = new ViewLogsFile();
        chineseFood = new ChineseFood();
        italianFood = new ItalianFood();
        colombianFood = new ColombianFood();
        ratingsPanel = new RatingsPanel();
    }

    private void startDrivers () {
        controllerRegisterDish = new ControllerRegisterDish(registerDish, subject);
        controllerRegisterDrink = new ControllerRegisterDrink(registerDrink, subject);
        controllerAdminPanel = new ControllerAdminPanelBuilder()
                .withViewAdminPanel(adminPanel)
                .withViewDashboard(dashboardAdmin)
                .withViewLogin(login)
                .withViewRegisterDish(registerDish)
                .withViewRegisterDrink(registerDrink)
                .withViewLogsDB(viewLogsDB)
                .withViewLogsFile(viewLogsFile)
                .withControllerRegisterDish(controllerRegisterDish)
                .withControllerRegisterDrink(controllerRegisterDrink)
                .build();

        controllerUsersPanel = new ControllerUserPanelBuilder()
                .withViewUserPanel(userPanel)
                .withViewDashboard(dashboardCustomers)
                .withViewChineseFood(chineseFood)
                .withViewColombianFood(colombianFood)
                .withViewItalianFood(italianFood)
                .withViewLogin(login)
                .withViewRatingPanel(ratingsPanel)
                .build();

        controllerLogin = new ControllerLoginBuilder()
                .withViewLogin(login)
                .withViewAdminPanel(adminPanel)
                .withViewUserPanel(userPanel)
                .withControllerAdminPanel(controllerAdminPanel)
                .withControllerUserPanel(controllerUsersPanel)
                .withSubject(subject)
                .build();

        startDriversAdmin();
        startDriversUser();
    }

    private void startDriversAdmin () {
        controllerViewLogsDB = new ControllerViewLogsDbBuilder()
                .withViewLogsDB(viewLogsDB)
                .withViewRegisterDish(registerDish)
                .withViewRegisterDrink(registerDrink)
                .withSubject(subject)
                .withControllerAdminPanel(controllerAdminPanel)
                .withControllerRegisterDish(controllerRegisterDish)
                .withControllerRegisterDrink(controllerRegisterDrink)
                .build();

        controllerViewLogsFile = new ControllerViewLogsFile(viewLogsFile, subject);
        controllerDashboard = new ControllerDashboard(dashboardAdmin, subject);
    }

    private void startDriversUser () {
        controllerChineseFood = new ControllerChineseFood(chineseFood, subject);
        controllerColombianFood = new ControllerColombianFood(colombianFood, subject);
        controllerItalianFood = new ControllerItalianFood(italianFood, subject);
        controllerRatingPanel = new ControllerRatingPanel(ratingsPanel, subject);
        controllerDashboardCustomer = new ControllerDashboardCustomer(dashboardCustomers, subject);
    }

    private void createFiles() {
        try {
            DAORestaurantFile<Dish> daoDish = FactoryMethod.getInstanceFile(InstanceTypeFile.DAODISHFILE);
            DAORestaurantFile<Drink> daoDrink = FactoryMethod.getInstanceFile(InstanceTypeFile.DAODRINKFILE);
            daoDrink.createFile("Drinks.txt");
            daoDish.createFile("Dishes.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
