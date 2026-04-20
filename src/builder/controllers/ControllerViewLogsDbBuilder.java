package builder.controllers;

import controller.admin.ControllerAdminPanel;
import controller.admin.ControllerRegisterDish;
import controller.admin.ControllerRegisterDrink;
import controller.admin.ControllerViewLogs;
import observer.Subject;
import view.administrators.RegisterDish;
import view.administrators.RegisterDrink;
import view.administrators.ViewLogsDB;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerViewLogsDbBuilder {

    private ViewLogsDB viewLogsDB;
    private RegisterDish registerDish;
    private RegisterDrink registerDrink;
    private ControllerAdminPanel controllerAdminPanel;
    private ControllerRegisterDish controllerRegisterDish;
    private ControllerRegisterDrink controllerRegisterDrink;
    private Subject subject;

    public ControllerViewLogsDbBuilder withViewLogsDB(ViewLogsDB viewLogsDB) {
        this.viewLogsDB = viewLogsDB;
        return this;
    }

    public ControllerViewLogsDbBuilder withViewRegisterDish(RegisterDish registerDish) {
        this.registerDish = registerDish;
        return this;
    }

    public ControllerViewLogsDbBuilder withViewRegisterDrink(RegisterDrink registerDrink) {
        this.registerDrink = registerDrink;
        return this;
    }

    public ControllerViewLogsDbBuilder withControllerAdminPanel(ControllerAdminPanel controllerAdminPanel) {
        this.controllerAdminPanel = controllerAdminPanel;
        return this;
    }

    public ControllerViewLogsDbBuilder withControllerRegisterDish(ControllerRegisterDish controllerRegisterDish) {
        this.controllerRegisterDish = controllerRegisterDish;
        return this;
    }

    public ControllerViewLogsDbBuilder withControllerRegisterDrink(ControllerRegisterDrink controllerRegisterDrink) {
        this.controllerRegisterDrink = controllerRegisterDrink;
        return this;
    }

    public ControllerViewLogsDbBuilder withSubject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public ControllerViewLogs build() {
        return new ControllerViewLogs(this);
    }

    public ViewLogsDB getViewLogsDB() {
        return viewLogsDB;
    }

    public RegisterDish getRegisterDish() {
        return registerDish;
    }

    public RegisterDrink getRegisterDrink() {
        return registerDrink;
    }

    public ControllerAdminPanel getControllerAdminPanel() {
        return controllerAdminPanel;
    }

    public ControllerRegisterDish getControllerRegisterDish() {
        return controllerRegisterDish;
    }

    public ControllerRegisterDrink getControllerRegisterDrink() {
        return controllerRegisterDrink;
    }

    public Subject getSubject() {
        return subject;
    }

}
