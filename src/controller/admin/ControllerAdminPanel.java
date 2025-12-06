package controller.admin;

import builder.controllers.ControllerAdminPanelBuilder;
import utils.MessageUtils;
import utils.NavigatorUtils;
import view.LogIn;
import view.administrators.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author hectoracevedo
 */
public class ControllerAdminPanel {

    private boolean expandedMenu = true;
    private Timer animation;
    
    private final AdminPanel adminPanel;
    private final DashboardAdmin dashboard;
    private final LogIn login;
    private final RegisterDish registerDish;
    private final RegisterDrink registerDrink;
    private final ViewLogsDB viewLogsDB;
    private final ViewLogsFile viewLogsFile;
    private final ControllerRegisterDish controllerRegisterDish;
    private final ControllerRegisterDrink controllerRegisterDrink;

    public ControllerAdminPanel(ControllerAdminPanelBuilder builder) {
        this.adminPanel = builder.getAdminPanel();
        this.dashboard = builder.getDashboard();
        this.login = builder.getLogin();
        this.registerDish = builder.getRegisterDish();
        this.registerDrink = builder.getRegisterDrink();
        this.viewLogsDB = builder.getViewLogsDB();
        this.viewLogsFile = builder.getViewLogsFile();
        this.controllerRegisterDish = builder.getControllerRegisterDish();
        this.controllerRegisterDrink = builder.getControllerRegisterDrink();

        connectEvents();
    }

    private void connectEvents() {
        adminPanel.setControllerDashboard(e -> showDashboard());
        adminPanel.setControllerRegisterDish(e -> showRegisterDish());
        adminPanel.setControllerRegisterDrink(e -> showRegisterDrink());
        adminPanel.setControllerSeeAvailableMeals(e -> seeAvailableMeals());
        adminPanel.setControllerSeeInternalRecords(e -> seeInternalRecords());
        adminPanel.setControllerMenu(e -> actionMenu());
        adminPanel.setControllerExit(e -> logout());
    }

    private void actionMenu() {
        try{
            int initialWidth = adminPanel.getJpl_menu().getWidth();
            int finalWidth = expandedMenu ? 60 : 245;
            int passed = (finalWidth - initialWidth) / 14;

            if(animation != null && animation.isRunning()){
                return;
            }

            animation = new Timer(15, null);
            animation.addActionListener(new ActionListener() {
                int currentWidth = initialWidth;

                @Override
                public void actionPerformed(ActionEvent e) {
                    currentWidth += passed;

                    boolean endAnimation = (passed > 0 && currentWidth >= finalWidth) || (passed < 0 && currentWidth <= finalWidth);

                    if(endAnimation){
                        currentWidth = finalWidth;
                        animation.stop();
                        expandedMenu = !expandedMenu;

                        updateTexts(expandedMenu);
                    }

                    adminPanel.getJpl_menu().setPreferredSize(new Dimension(currentWidth, adminPanel.getJpl_menu().getHeight()));
                    adminPanel.getJpl_menu().revalidate();
                    adminPanel.getJpl_menu().repaint();
                }
            });

            animation.start();
        }catch(Exception e){
            MessageUtils.errorMessage("Ha ocurrido un error al ocultar el menu");
        }
    }

    private void updateTexts(boolean expandedMenu) {
        adminPanel.getBtn_dashboard().setText(expandedMenu ? "  DASHBOARD" : "");
        adminPanel.getBtn_registerDish().setText(expandedMenu ? "  REGISTRAR PLATOS" : "");
        adminPanel.getBtn_registerDrinks().setText(expandedMenu ? "  REGISTRAR BEBIDAS" : "");
        adminPanel.getBtn_seeAvailableMeals().setText(expandedMenu ? "  COMIDAS DISPONIBLES" : "");
        adminPanel.getBtn_seeInternalRecords().setText(expandedMenu ? "  REGISTROS INTERNOS" : "");
        adminPanel.getBtn_exit().setText(expandedMenu ? "  SALIR" : "");
    }

    public void showDashboard() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(AdminPanel.getJpl_content(), dashboard);
        adminPanel.getBtn_dashboard().setIcon(new ImageIcon(getClass().getResource("/view/image/dashboardSelected.png")));
        adminPanel.getBtn_dashboard().setBackground(new Color(187,222,251));
        adminPanel.getBtn_dashboard().setForeground(new Color(0, 49, 245));
    }

    public void showRegisterDish() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(AdminPanel.getJpl_content(), registerDish);
        adminPanel.getBtn_registerDish().setIcon(new ImageIcon(getClass().getResource("/view/image/saladSelected.png")));
        adminPanel.getBtn_registerDish().setBackground(new Color(187,222,251));
        adminPanel.getBtn_registerDish().setForeground(new Color(0, 49, 245));
        controllerRegisterDish.setModify(false);
        registerDish.getBtn_register().setText("REGISTRAR PLATO");
        registerDish.getCbo_waySave().setEnabled(true);
        registerDish.getTxt_name().setText("");
        registerDish.getTxt_description().setText("");
        registerDish.getTxt_price().setText("");
        registerDish.getCbo_typeDish().setSelectedIndex(0);
    }

    public void showRegisterDrink() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(AdminPanel.getJpl_content(), registerDrink);
        adminPanel.getBtn_registerDrinks().setIcon(new ImageIcon(getClass().getResource("/view/image/wineSelected.png")));
        adminPanel.getBtn_registerDrinks().setBackground(new Color(187,222,251));
        adminPanel.getBtn_registerDrinks().setForeground(new Color(0, 49, 245));
        controllerRegisterDrink.setModify(false);
        registerDrink.getBtn_register().setText("REGISTRAR BEBIDA");
        registerDrink.getCbo_waySave().setEnabled(true);
        registerDrink.getTxt_name().setText("");
        registerDrink.getTxt_description().setText("");
        registerDrink.getTxt_price().setText("");
        registerDrink.getCbo_typeDrink().setSelectedIndex(0);
    }

    private void seeAvailableMeals() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(AdminPanel.getJpl_content(), viewLogsDB);
        adminPanel.getBtn_seeAvailableMeals().setIcon(new ImageIcon(getClass().getResource("/view/image/databaseSelected.png")));
        adminPanel.getBtn_seeAvailableMeals().setBackground(new Color(187,222,251));
        adminPanel.getBtn_seeAvailableMeals().setForeground(new Color(0, 49, 245));
    }

    private void seeInternalRecords() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(AdminPanel.getJpl_content(), viewLogsFile);
        adminPanel.getBtn_seeInternalRecords().setIcon(new ImageIcon(getClass().getResource("/view/image/fileSelected.png")));
        adminPanel.getBtn_seeInternalRecords().setBackground(new Color(187,222,251));
        adminPanel.getBtn_seeInternalRecords().setForeground(new Color(0, 49, 245));
    }

    private void resetColors() {
        Color normal = new Color(255,255,255);
        adminPanel.getBtn_dashboard().setBackground(normal);
        adminPanel.getBtn_registerDish().setBackground(normal);
        adminPanel.getBtn_registerDrinks().setBackground(normal);
        adminPanel.getBtn_seeAvailableMeals().setBackground(normal);
        adminPanel.getBtn_seeInternalRecords().setBackground(normal);
        adminPanel.getBtn_exit().setBackground(normal);
    }

    private void resetIcons() {
        adminPanel.getBtn_dashboard().setIcon(new ImageIcon(getClass().getResource("/view/image/dashboard.png")));
        adminPanel.getBtn_registerDish().setIcon(new ImageIcon(getClass().getResource("/view/image/salad.png")));
        adminPanel.getBtn_registerDrinks().setIcon(new ImageIcon(getClass().getResource("/view/image/wine.png")));
        adminPanel.getBtn_seeAvailableMeals().setIcon(new ImageIcon(getClass().getResource("/view/image/database.png")));
        adminPanel.getBtn_seeInternalRecords().setIcon(new ImageIcon(getClass().getResource("/view/image/file.png")));
        adminPanel.getBtn_exit().setIcon(new ImageIcon(getClass().getResource("/view/image/log-out.png")));
    }

    private void resetForeground() {
        Color normal = new Color(33,37,41);
        adminPanel.getBtn_dashboard().setForeground(normal);
        adminPanel.getBtn_registerDish().setForeground(normal);
        adminPanel.getBtn_registerDrinks().setForeground(normal);
        adminPanel.getBtn_seeAvailableMeals().setForeground(normal);
        adminPanel.getBtn_seeInternalRecords().setForeground(normal);
        adminPanel.getBtn_exit().setForeground(normal);
    }

    private void logout() {
        login.setVisible(true);
        login.setLocationRelativeTo(null);
        login.setResizable(false);
        adminPanel.dispose();
    }

}
