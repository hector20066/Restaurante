package builder.controllers;

import controller.admin.ControllerAdminPanel;
import controller.admin.ControllerRegisterDish;
import controller.admin.ControllerRegisterDrink;
import view.LogIn;
import view.administrators.*;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerAdminPanelBuilder {

    private AdminPanel adminPanel;
    private DashboardAdmin dashboard;
    private LogIn login;
    private RegisterDish registerDish;
    private RegisterDrink registerDrink;
    private ViewLogsDB viewLogsDB;
    private ControllerRegisterDish controllerRegisterDish;
    private ControllerRegisterDrink controllerRegisterDrink;

    public ControllerAdminPanelBuilder withViewAdminPanel(AdminPanel adminPanel) {
        this.adminPanel = adminPanel;
        return this;
    }

    public ControllerAdminPanelBuilder withViewDashboard(DashboardAdmin dashboard) {
        this.dashboard = dashboard;
        return this;
    }

    public ControllerAdminPanelBuilder withViewLogin(LogIn login) {
        this.login = login;
        return this;
    }

    public ControllerAdminPanelBuilder withViewRegisterDish(RegisterDish registerDish) {
        this.registerDish = registerDish;
        return this;
    }

    public ControllerAdminPanelBuilder withViewRegisterDrink(RegisterDrink registerDrink) {
        this.registerDrink = registerDrink;
        return this;
    }

    public ControllerAdminPanelBuilder withViewLogsDB(ViewLogsDB viewLogsDB) {
        this.viewLogsDB = viewLogsDB;
        return this;
    }

    public ControllerAdminPanelBuilder withControllerRegisterDish(ControllerRegisterDish controllerRegisterDish) {
        this.controllerRegisterDish = controllerRegisterDish;
        return this;
    }

    public ControllerAdminPanelBuilder withControllerRegisterDrink(ControllerRegisterDrink controllerRegisterDrink) {
        this.controllerRegisterDrink = controllerRegisterDrink;
        return this;
    }

    public ControllerAdminPanel build() {
        return new ControllerAdminPanel(this);
    }

    public AdminPanel getAdminPanel() {
        return adminPanel;
    }

    public DashboardAdmin getDashboard() {
        return dashboard;
    }

    public LogIn getLogin() {
        return login;
    }

    public RegisterDish getRegisterDish() {
        return registerDish;
    }

    public RegisterDrink getRegisterDrink() {
        return registerDrink;
    }

    public ViewLogsDB getViewLogsDB() {
        return viewLogsDB;
    }

    public ControllerRegisterDish getControllerRegisterDish() {
        return controllerRegisterDish;
    }

    public ControllerRegisterDrink getControllerRegisterDrink() {
        return controllerRegisterDrink;
    }

}
