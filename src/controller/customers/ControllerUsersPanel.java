package controller.customers;

import builder.controllers.ControllerUserPanelBuilder;
import service.ServiceOpenWeather;
import utils.ExtractJsonValuesUtils;
import utils.MessageUtils;
import utils.NavigatorUtils;
import view.LogIn;
import view.customers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerUsersPanel {

    private boolean expandedMenu = true;
    private Timer animation;

    private final UserPanel userPanel;
    private final DashboardCustomers dashboard;
    private final ChineseFood chineseFood;
    private final ItalianFood italianFood;
    private final ColombianFood colombianFood;
    private final LogIn login;
    private final RatingsPanel ratingsPanel;

    public ControllerUsersPanel(ControllerUserPanelBuilder builder) {
        this.userPanel = builder.getUserPanel();
        this.dashboard = builder.getDashboard();
        this.chineseFood = builder.getChineseFood();
        this.italianFood = builder.getItalianFood();
        this.colombianFood = builder.getColombianFood();
        this.login = builder.getLogin();
        this.ratingsPanel = builder.getRatingsPanel();

        connectEvents();
    }

    private void connectEvents() {
        userPanel.setControllerDashboard(e -> showDashboard());
        userPanel.setControllerChineseFood(e -> showChineseFood());
        userPanel.setControllerItalianFood(e -> showItalianFood());
        userPanel.setControllerColombianFood(e -> showColombianFood());
        userPanel.setControllerMenu(e -> actionMenu());
        userPanel.setControllerRatingsPanel(e -> showRatingsPanel());
        userPanel.setControllerExit(e -> logout());
    }

    private void actionMenu() {
        try{
            int initialWidth = userPanel.getJpl_menu().getWidth();
            int finalWidth = expandedMenu ? 65 : 264;
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

                    userPanel.getJpl_menu().setPreferredSize(new Dimension(currentWidth, userPanel.getJpl_menu().getHeight()));
                    userPanel.getJpl_menu().revalidate();
                    userPanel.getJpl_menu().repaint();
                }
            });

            animation.start();
        }catch(Exception e){
            MessageUtils.errorMessage("Ha ocurrido un error al ocultar el menu");
        }
    }

    private void loadWeather(String city) {
        new Thread(() -> {
            ServiceOpenWeather serviceOpenWeather = new ServiceOpenWeather();
            String json = serviceOpenWeather.getClimate(city);
            String climateDescription = ExtractJsonValuesUtils.extractJsonValues(json, "description");
            String temperature = ExtractJsonValuesUtils.extractJsonValues(json, "temp");

            SwingUtilities.invokeLater(() -> {
                userPanel.getJlb_climateDescription().setText("Clima: " + climateDescription);
                userPanel.getJlb_temperature().setText("Temperatura: " + temperature + " °C");
            });
        }).start();
    }

    private void updateTexts(boolean expandedMenu) {
        userPanel.getBtn_dashboard().setText(expandedMenu ? "  DASHBOARD" : "");
        userPanel.getBtn_chineseFood().setText(expandedMenu ? "  COMIDA CHINA" : "");
        userPanel.getBtn_italianFood().setText(expandedMenu ? "  COMIDA ITALIANA" : "");
        userPanel.getBtn_colombianFood().setText(expandedMenu ? "  COMIDA COLOMBIANA" : "");
        userPanel.getBtn_exit().setText(expandedMenu ? "  SALIR" : "");
        userPanel.getBtn_ratings().setText(expandedMenu ? "  CALIFICAR" : "");
        userPanel.getJlb_subtitle().setText(expandedMenu ? "Realizar Compras" : "");
    }

    public void showDashboard() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(UserPanel.getJpl_content(), dashboard);
        userPanel.getBtn_dashboard().setBackground(new Color(187,222,251));
        userPanel.getBtn_dashboard().setForeground(new Color(0, 49, 245));
        userPanel.getBtn_dashboard().setIcon(new ImageIcon(getClass().getResource("/view/image/dashboardSelected.png")));
    }

    private void showChineseFood() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(UserPanel.getJpl_content(), chineseFood);
        userPanel.getBtn_chineseFood().setBackground(new Color(187,222,251));
        userPanel.getBtn_chineseFood().setForeground(new Color(0, 49, 245));
        userPanel.getBtn_chineseFood().setIcon(new ImageIcon(getClass().getResource("/view/image/soupSelected.png")));
        clearFieldsChineseFood();
    }

    private void clearFieldsChineseFood() {
        chineseFood.getTxt_totalSale().setText("");
        chineseFood.getTxt_customerId().setText("");
        chineseFood.getTxt_customerName().setText("");
        chineseFood.getTxt_customerAddress().setText("");
        chineseFood.getTxt_customerEmail().setText("");

        chineseFood.getTxt_descriptionDish().setText("");
        chineseFood.getTxt_descriptionDrink().setText("");
        chineseFood.getTxt_amountDish().setText("");
        chineseFood.getTxt_amountDrink().setText("");
        chineseFood.getTxt_priceDish().setText("");
        chineseFood.getTxt_priceDrink().setText("");
        chineseFood.getCbo_drink().setSelectedIndex(0);
        chineseFood.getCbo_dish().setSelectedIndex(0);
    }

    private void showItalianFood() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(UserPanel.getJpl_content(), italianFood);
        userPanel.getBtn_italianFood().setBackground(new Color(187,222,251));
        userPanel.getBtn_italianFood().setForeground(new Color(0, 49, 245));
        userPanel.getBtn_italianFood().setIcon(new ImageIcon(getClass().getResource("/view/image/pizzaSelected.png")));
        clearFieldsItalianFood();
    }

    private void clearFieldsItalianFood() {
        italianFood.getTxt_totalSale().setText("");
        italianFood.getTxt_customerId().setText("");
        italianFood.getTxt_customerName().setText("");
        italianFood.getTxt_customerAddress().setText("");
        italianFood.getTxt_customerEmail().setText("");

        italianFood.getTxt_descriptionDish().setText("");
        italianFood.getTxt_descriptionDrink().setText("");
        italianFood.getTxt_amountDish().setText("");
        italianFood.getTxt_amountDrink().setText("");
        italianFood.getTxt_priceDish().setText("");
        italianFood.getTxt_priceDrink().setText("");
        italianFood.getCbo_drink().setSelectedIndex(0);
        italianFood.getCbo_dish().setSelectedIndex(0);
    }

    private void showColombianFood() {
        resetColors();
        resetIcons();
        resetForeground();
        NavigatorUtils.showFormManagement(UserPanel.getJpl_content(), colombianFood);
        userPanel.getBtn_colombianFood().setBackground(new Color(187,222,251));
        userPanel.getBtn_colombianFood().setForeground(new Color(0, 49, 245));
        userPanel.getBtn_colombianFood().setIcon(new ImageIcon(getClass().getResource("/view/image/egg-friedSelected.png")));
        clearFieldsColombianFood();
    }

    private void clearFieldsColombianFood() {
        colombianFood.getTxt_totalSale().setText("");
        colombianFood.getTxt_customerId().setText("");
        colombianFood.getTxt_customerName().setText("");
        colombianFood.getTxt_customerAddress().setText("");
        colombianFood.getTxt_customerAddress().setText("");

        colombianFood.getTxt_descriptionDrink().setText("");
        colombianFood.getTxt_descriptionDish().setText("");
        colombianFood.getTxt_amountDish().setText("");
        colombianFood.getTxt_amountDrink().setText("");
        colombianFood.getTxt_priceDrink().setText("");
        colombianFood.getTxt_priceDish().setText("");
        colombianFood.getCbo_dish().setSelectedIndex(0);
        colombianFood.getCbo_drink().setSelectedIndex(0);
    }

    private void showRatingsPanel() {
        resetForeground();
        resetColors();
        resetIcons();
        NavigatorUtils.showFormManagement(UserPanel.getJpl_content(), ratingsPanel);
        userPanel.getBtn_ratings().setBackground(new Color(187,222,251));
        userPanel.getBtn_ratings().setForeground(new Color(0, 49, 245));
        userPanel.getBtn_ratings().setIcon(new ImageIcon(getClass().getResource("/view/image/starSelected.png")));
    }

    private void resetColors() {
        Color normal = new Color(255,255,255);
        userPanel.getBtn_dashboard().setBackground(normal);
        userPanel.getBtn_chineseFood().setBackground(normal);
        userPanel.getBtn_italianFood().setBackground(normal);
        userPanel.getBtn_colombianFood().setBackground(normal);
        userPanel.getBtn_ratings().setBackground(normal);
        userPanel.getBtn_exit().setBackground(normal);
    }

    private void resetIcons() {
        userPanel.getBtn_dashboard().setIcon(new ImageIcon(getClass().getResource("/view/image/dashboard.png")));
        userPanel.getBtn_chineseFood().setIcon(new ImageIcon(getClass().getResource("/view/image/soup.png")));
        userPanel.getBtn_italianFood().setIcon(new ImageIcon(getClass().getResource("/view/image/pizza.png")));
        userPanel.getBtn_colombianFood().setIcon(new ImageIcon(getClass().getResource("/view/image/egg-fried.png")));
        userPanel.getBtn_exit().setIcon(new ImageIcon(getClass().getResource("/view/image/log-out.png")));
        userPanel.getBtn_ratings().setIcon(new ImageIcon(getClass().getResource("/view/image/star.png")));
    }

    public void resetForeground() {
        Color normal = new Color(33,37,41);
        userPanel.getBtn_dashboard().setForeground(normal);
        userPanel.getBtn_chineseFood().setForeground(normal);
        userPanel.getBtn_italianFood().setForeground(normal);
        userPanel.getBtn_colombianFood().setForeground(normal);
        userPanel.getBtn_ratings().setForeground(normal);
        userPanel.getBtn_exit().setForeground(normal);
    }

    private void logout() {
        login.setVisible(true);
        login.setLocationRelativeTo(null);
        login.setResizable(false);
        userPanel.dispose();
    }

}
