package controller.admin;

import model.dao.impl.DAOSaleDetailImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.DAOWithAutoIncrement;
import model.dao.interfaces.IDAO;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.Sale;
import model.entities.SaleDetail;
import model.enums.TypeFood;
import observer.Observer;
import observer.Subject;
import view.administrators.DashboardAdmin;
import view.model.ModelChart;

import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import model.dao.impl.DAODishImpl;
import model.dao.impl.DAODrinkImpl;
import model.dao.impl.DAOSalesImpl;

import javax.swing.*;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerDashboard implements Observer {

    private final DashboardAdmin dashboard;

    private final DAOWithAutoIncrement<Sale> daoSale;
    private final IDAO<SaleDetail> daoSaleDetails;
    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;

    public ControllerDashboard(DashboardAdmin dashboard, Subject subject) {
        this.dashboard = dashboard;

        this.daoSale = DAOSalesImpl.getInstance();
        this.daoSaleDetails = DAOSaleDetailImpl.getInstance();
        this.daoDish = DAODishImpl.getInstance();
        this.daoDrink = DAODrinkImpl.getInstance();

        subject.register(this);
    }

    @Override
    public void update() {
        new Thread(() -> {
            try {
                dashboard.getChart().clear();
                dashboard.getChart().addLegend("Comida Colombiana", new Color(255, 209, 102));
                dashboard.getChart().addLegend("Comida Italiana", new Color(6, 214, 160));
                dashboard.getChart().addLegend("Comida China", new Color(239, 71, 111));
                loadGraphicalData();
                loadCardsData();
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(dashboard, "Ha ocurrido un error al cargar la información del dashboard", "", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void loadGraphicalData() throws Exception {
        List<Sale> listSales = daoSale.getData();

        int[][] data = buildMonthlyData(listSales);
        setupChartLegend();
        loadLastSixMonths(data);
    }

    private int[][] buildMonthlyData(List<Sale> sales) throws Exception {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        int[] colombian = new int[12];
        int[] italian = new int[12];
        int[] chinese = new int[12];

        for (Sale sale : sales) {
            SaleDetail saleDetail = daoSaleDetails.getData(sale.getSaleNumber());
            LocalDate date = sale.getDate();
            if (date.getYear() != currentYear) continue;

            int month = date.getMonthValue() - 1;

            processDish(saleDetail, month, colombian, italian, chinese);
            processDrink(saleDetail, month, colombian, italian, chinese);
        }

        return new int[][]{ colombian, italian, chinese };
    }

    private void processDish(SaleDetail saleDetail, int month, int[] colombian, int[] italian, int[] chinese) throws Exception {
        if (saleDetail.getDishId() == 0) return;

        Dish dish = daoDish.getData(saleDetail.getDishId());

        if (dish == null) return;

        switch (dish.getTypeDish()) {
            case TypeFood.COLOMBIAN -> colombian[month] += saleDetail.getDishAmount();
            case TypeFood.ITALIAN -> italian[month] += saleDetail.getDishAmount();
            case TypeFood.CHINESE -> chinese[month] += saleDetail.getDishAmount();
        }
    }

    private void processDrink(SaleDetail saleDetail, int month, int[] colombian, int[] italian, int[] chinese) throws Exception {
        if (saleDetail.getDrinkId() == 0) return;

        Drink drink = daoDrink.getData(saleDetail.getDrinkId());
        if (drink == null) return;

        switch (drink.getTypeDrink()) {
            case TypeFood.COLOMBIAN -> colombian[month] += saleDetail.getDrinkAmount();
            case TypeFood.ITALIAN -> italian[month] += saleDetail.getDrinkAmount();
            case TypeFood.CHINESE -> chinese[month] += saleDetail.getDrinkAmount();
        }
    }

    private void setupChartLegend() {
        dashboard.getChart().clear();
        dashboard.getChart().addLegend("Comida Colombiana", new Color(255,209,102));
        dashboard.getChart().addLegend("Comida Italiana", new Color(6,214,160));
        dashboard.getChart().addLegend("Comida China", new Color(239,71,111));
    }

    private void loadLastSixMonths(int[][] data) {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();

        int[] colombian = data[0];
        int[] italian = data[1];
        int[] chinese = data[2];

        for (int i = 0; i < 6; i++) {
            int monthIndex = (currentMonth - 6 + i + 12) % 12;
            String monthName = Month.of(monthIndex + 1).getDisplayName(TextStyle.FULL, Locale.of("es"));

            double[] values = new double[]{colombian[monthIndex], italian[monthIndex], chinese[monthIndex]};
            dashboard.getChart().addData(new ModelChart(monthName, values));
        }
    }

    private void loadCardsData() throws Exception {
        dashboard.updateCards(totalDishesSold(), totalDrinksSold(), totalProfit());
    }

    private int totalDishesSold() throws Exception {
        List<SaleDetail> sales = daoSaleDetails.getData();
        int amount = 0;

        for (SaleDetail sale : sales) {
            amount += sale.getDishAmount();
        }

        return amount;
    }

    private int totalDrinksSold() throws Exception {
        List<SaleDetail> sales = daoSaleDetails.getData();
        int amount = 0;

        for (SaleDetail sale : sales) {
            amount += sale.getDrinkAmount();
        }

        return amount;
    }

    private float totalProfit() throws Exception {
        List<Sale> sales = daoSale.getData();
        float totalSales = 0;

        for (Sale sale : sales) {
            totalSales += sale.getTotal();
        }

        return totalSales;
    }

}
