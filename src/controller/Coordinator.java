package controller;

import builder.controllers.ControllerAdminPanelBuilder;
import builder.controllers.ControllerLoginBuilder;
import builder.controllers.ControllerUserPanelBuilder;
import builder.controllers.ControllerViewLogsDbBuilder;
import controller.admin.*;
import controller.customers.*;
import observer.ConcreteSubject;
import view.LogIn;
import view.administrators.*;
import view.customers.*;

/**
 *
 * @author Andres Cardenas
 */
public class Coordinator {

    private ConcreteSubject subject;

    private LogIn login;
    private AdminPanel adminPanel;
    private UserPanel userPanel;
    private DashboardAdmin dashboardAdmin;
    private RegisterDish registerDish;
    private RegisterDrink registerDrink;
    private ViewLogsDB viewLogsDB;
    private DashboardCustomers dashboardCustomers;
    private ChineseFood chineseFood;
    private ItalianFood italianFood;
    private ColombianFood colombianFood;
    private RatingsPanel ratingsPanel;
    private SalesReport salesReport;
    private ShowQualifications showQualifications;

    private ControllerLogin controllerLogin;
    private ControllerAdminPanel controllerAdminPanel;
    private ControllerRegisterDish controllerRegisterDish;
    private ControllerRegisterDrink controllerRegisterDrink;
    private ControllerViewLogs controllerViewLogsDB;
    private ControllerDashboard controllerDashboard;
    private ControllerDashboardCustomer controllerDashboardCustomer;
    private ControllerUsersPanel controllerUsersPanel;
    private ControllerColombianFood controllerColombianFood;
    private ControllerChineseFood controllerChineseFood;
    private ControllerItalianFood controllerItalianFood;
    private ControllerRatingPanel controllerRatingPanel;
    private ControllerSalesReport controllerSalesReport;
    private ControllerShowQualifications controllerShowQualifications;

    public void startApp() {
        subject = ConcreteSubject.getInstance();
        startViews();
        startDrivers();

        login.setVisible(true);
        login.setLocationRelativeTo(null);
        login.setResizable(false);
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
        chineseFood = new ChineseFood();
        italianFood = new ItalianFood();
        colombianFood = new ColombianFood();
        ratingsPanel = new RatingsPanel();
        salesReport = new SalesReport();
        showQualifications = new ShowQualifications();
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
                .withViewSalesReport(salesReport)
                .withViewShowQualifications(showQualifications)
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

        controllerDashboard = new ControllerDashboard(dashboardAdmin, subject);
        controllerSalesReport = new ControllerSalesReport(salesReport, showQualifications, subject);
        controllerShowQualifications = new ControllerShowQualifications(showQualifications, subject);
    }

    private void startDriversUser () {
        controllerChineseFood = new ControllerChineseFood(chineseFood, subject);
        controllerColombianFood = new ControllerColombianFood(colombianFood, subject);
        controllerItalianFood = new ControllerItalianFood(italianFood, subject);
        controllerRatingPanel = new ControllerRatingPanel(ratingsPanel, subject);
        controllerDashboardCustomer = new ControllerDashboardCustomer(dashboardCustomers, subject);
    }

}
