package controller;

import builder.controllers.ControllerLoginBuilder;
import controller.admin.ControllerAdminPanel;
import controller.customers.ControllerUsersPanel;
import observer.Subject;
import service.LoginService;
import utils.MessageUtils;
import view.LogIn;
import view.administrators.AdminPanel;
import view.customers.UserPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerLogin {

    private final LogIn login;
    private final AdminPanel adminPanel;
    private final UserPanel userPanel;
    private final LoginService loginService;
    private final ControllerAdminPanel controllerAdminPanel;
    private final ControllerUsersPanel controllerUsersPanel;
    private final Subject subject;

    public ControllerLogin(ControllerLoginBuilder builder) {
        this.login = builder.getLogin();
        this.adminPanel = builder.getAdminPanel();
        this.userPanel = builder.getUserPanel();
        this.controllerAdminPanel = builder.getControllerAdminPanel();
        this.controllerUsersPanel = builder.getControllerUsersPanel();
        this.loginService = LoginService.getInstance();
        this.subject = builder.getSubject();

        connectEvents();
    }

    private void connectEvents() {
        login.setControllerLogin(e -> loginAdmin());
        login.setControllerLoginAsGuest(e -> loginGuest());
        login.setControllerSeePassword(e -> {
            if (login.getJrb_seePassword().isSelected()) {
                login.getJpld_password().setEchoChar((char) 0);
            } else {
                login.getJpld_password().setEchoChar('•');
            }
        });

        login.setControllerTxtUserMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mousePressedUser();
            }
        });

        login.setControllerJpldPasswordMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mousePressedPassword();
            }
        });
    }

    private void loginAdmin() {
        try {
            /*String user = login.getTxt_user().getText().trim();
            String password = new String(login.getJpld_password().getPassword());

            if (user.isBlank() || password.isBlank()) {
                MessageUtils.warningMessage("Por favor complete todos los campos");
                return;
            }

            if (loginService.login(user, password)) {
                showAdminWindow();
                subject.notifyObserver();
            } else {
                MessageUtils.errorMessage("No se ha encontrado la cuenta. Por favor si no es empleado\ninicie sesion como invitado");
            }*/
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al iniciar sesion.\nLamentamos los inconvenientes");
        }
    }

    private void showAdminWindow() {
        adminPanel.setVisible(true);
        adminPanel.setLocationRelativeTo(null);
        adminPanel.setResizable(false);
        controllerAdminPanel.showDashboard();
        login.dispose();
    }

    private void loginGuest() {
        try {
            showUserWindow();
            subject.notifyObserver();
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al iniciar sesion.\nLamentamos los inconvenientes");
        }
    }

    private void showUserWindow() {
        userPanel.setVisible(true);
        userPanel.setLocationRelativeTo(null);
        userPanel.setResizable(false);
        controllerUsersPanel.showDashboard();
    }

    private void mousePressedUser() {
        if (login.getTxt_user().getText().equals("Ingrese su usuario")) {
            login.getTxt_user().setText("");
            login.getTxt_user().setForeground(new Color(37,106,208));
        }

        if (String.valueOf(login.getJpld_password().getPassword()).isBlank()) {
            login.getJpld_password().setText("••••••••");
            login.getJpld_password().setForeground(Color.gray);
        }
    }

    private void mousePressedPassword() {
        if (login.getTxt_user().getText().isBlank()) {
            login.getTxt_user().setText("Ingrese su usuario");
            login.getTxt_user().setForeground(Color.gray);
        }

        if (String.valueOf(login.getJpld_password().getPassword()).equals("••••••••")) {
            login.getJpld_password().setText("");
            login.getJpld_password().setForeground(new Color(37,106,208));
        }
    }

}