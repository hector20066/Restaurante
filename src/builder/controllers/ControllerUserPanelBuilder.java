package builder.controllers;

import controller.customers.ControllerUsersPanel;
import view.LogIn;
import view.customers.*;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerUserPanelBuilder {

    private UserPanel userPanel;
    private DashboardCustomers dashboard;
    private ChineseFood chineseFood;
    private ItalianFood italianFood;
    private ColombianFood colombianFood;
    private LogIn login;
    private RatingsPanel ratingsPanel;

    public ControllerUserPanelBuilder withViewUserPanel(UserPanel userPanel) {
        this.userPanel = userPanel;
        return this;
    }

    public ControllerUserPanelBuilder withViewDashboard(DashboardCustomers dashboard) {
        this.dashboard = dashboard;
        return this;
    }

    public ControllerUserPanelBuilder withViewChineseFood(ChineseFood chineseFood) {
        this.chineseFood = chineseFood;
        return this;
    }

    public ControllerUserPanelBuilder withViewItalianFood(ItalianFood italianFood) {
        this.italianFood = italianFood;
        return this;
    }

    public ControllerUserPanelBuilder withViewColombianFood(ColombianFood colombianFood) {
        this.colombianFood = colombianFood;
        return this;
    }

    public ControllerUserPanelBuilder withViewLogin(LogIn login) {
        this.login = login;
        return this;
    }

    public ControllerUserPanelBuilder withViewRatingPanel(RatingsPanel ratingsPanel) {
        this.ratingsPanel = ratingsPanel;
        return this;
    }

    public ControllerUsersPanel build() {
        return new ControllerUsersPanel(this);
    }

    public UserPanel getUserPanel() {
        return userPanel;
    }

    public DashboardCustomers getDashboard() {
        return dashboard;
    }

    public ChineseFood getChineseFood() {
        return chineseFood;
    }

    public ItalianFood getItalianFood() {
        return italianFood;
    }

    public ColombianFood getColombianFood() {
        return colombianFood;
    }

    public LogIn getLogin() {
        return login;
    }

    public RatingsPanel getRatingsPanel() {
        return ratingsPanel;
    }

}
