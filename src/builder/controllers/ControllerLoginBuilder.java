package builder.controllers;

import controller.ControllerLogin;
import controller.admin.ControllerAdminPanel;
import controller.customers.ControllerUsersPanel;
import observer.Subject;
import view.LogIn;
import view.administrators.AdminPanel;
import view.customers.UserPanel;

/**
 *
 * @author hectoracevedo
 */
public class ControllerLoginBuilder {

    private LogIn login;
    private AdminPanel adminPanel;
    private UserPanel userPanel;
    private ControllerAdminPanel controllerAdminPanel;
    private ControllerUsersPanel controllerUsersPanel;
    private Subject subject;

    public ControllerLoginBuilder withViewLogin(LogIn login) {
        this.login = login;
        return this;
    }

    public ControllerLoginBuilder withViewAdminPanel(AdminPanel adminPanel) {
        this.adminPanel = adminPanel;
        return this;
    }

    public ControllerLoginBuilder withViewUserPanel(UserPanel userPanel) {
        this.userPanel = userPanel;
        return this;
    }

    public ControllerLoginBuilder withControllerAdminPanel(ControllerAdminPanel controllerAdminPanel) {
        this.controllerAdminPanel = controllerAdminPanel;
        return this;
    }

    public ControllerLoginBuilder withControllerUserPanel(ControllerUsersPanel controllerUsersPanel) {
        this.controllerUsersPanel =  controllerUsersPanel;
        return this;
    }

    public ControllerLoginBuilder withSubject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public ControllerLogin build() {
        return new ControllerLogin(this);
    }

    public LogIn getLogin() {
        return login;
    }

    public AdminPanel getAdminPanel() {
        return adminPanel;
    }

    public UserPanel getUserPanel() {
        return userPanel;
    }

    public ControllerAdminPanel getControllerAdminPanel() {
        return controllerAdminPanel;
    }

    public ControllerUsersPanel getControllerUsersPanel() {
        return controllerUsersPanel;
    }

    public Subject getSubject() {
        return subject;
    }

}
