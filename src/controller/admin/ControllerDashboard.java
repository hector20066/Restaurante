package controller.admin;

import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.IDAO;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.Sale;
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
import model.dao.impl.DAODishDBImpl;
import model.dao.impl.DAODrinkDBImpl;
import model.dao.impl.IDAOSalesImpl;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerDashboard implements Observer {

    private final DashboardAdmin dashboard;
    private final Subject subject;

    private final IDAO<Sale> daoSale;
    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;

    public ControllerDashboard(DashboardAdmin dashboard, Subject subject) {
        this.dashboard = dashboard;
        this.subject = subject;

        this.daoSale = IDAOSalesImpl.getInstance();
        this.daoDish = DAODishDBImpl.getInstance();
        this.daoDrink = DAODrinkDBImpl.getInstance();

        this.subject.register(this);
    }

    @Override
    public void update() {
        // testDataChart();
        new Thread(() -> {
            loadGraphicalData();
            loadCardsData();
        }).start();
    }

    private void loadGraphicalData() {
        try {
            List<Sale> listSales = daoSale.getData();

            int[][] data = buildMonthlyData(listSales);
            setupChartLegend();
            loadLastSixMonths(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[][] buildMonthlyData(List<Sale> sales) throws Exception {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        int[] colombian = new int[12];
        int[] italian = new int[12];
        int[] chinese = new int[12];

        for (Sale sale : sales) {
            LocalDate date = sale.getDate();
            if (date.getYear() != currentYear) continue;

            int month = date.getMonthValue() - 1;

            processDish(sale, month, colombian, italian, chinese);
            processDrink(sale, month, colombian, italian, chinese);
        }

        return new int[][]{ colombian, italian, chinese };
    }

    private void processDish(Sale sale, int month, int[] colombian, int[] italian, int[] chinese) throws Exception {
        if (sale.getDishCode() == 0) return;

        Dish dish = daoDish.getData(sale.getDishCode());
        if (dish == null) return;

        switch (dish.getTypeDish()) {
            case "Plato Colombiano" -> colombian[month] += sale.getAmountDish();
            case "Plato Italiano" -> italian[month] += sale.getAmountDish();
            case "Plato Chino" -> chinese[month] += sale.getAmountDish();
        }
    }

    private void processDrink(Sale sale, int month, int[] colombian, int[] italian, int[] chinese) throws Exception {
        if (sale.getDrinkCode() == 0) return;

        Drink drink = daoDrink.getData(sale.getDrinkCode());
        if (drink == null) return;

        switch (drink.getTypeDrink()) {
            case "Bebida Colombiana" -> colombian[month] += sale.getAmountDrink();
            case "Bebida Italiano"  -> italian[month] += sale.getAmountDrink();
            case "Bebida China"     -> chinese[month] += sale.getAmountDrink();
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
            String monthName = Month.of(monthIndex + 1).getDisplayName(TextStyle.FULL, new Locale("es"));

            double[] values = new double[]{colombian[monthIndex], italian[monthIndex], chinese[monthIndex]};

            dashboard.getChart().addData(new ModelChart(monthName, values));
        }
    }

    private void testDataChart() {
        dashboard.getChart().clear();
        dashboard.getChart().addLegend("Comida Colombiana", new Color(255,209,102));
        dashboard.getChart().addLegend("Comida Italiana", new Color(6,214,160));
        dashboard.getChart().addLegend("Comida China", new Color(239,71,111));

        dashboard.getChart().addData(new ModelChart("Marzo", new double[]{150, 100, 350}));
        dashboard.getChart().addData(new ModelChart("Abril", new double[]{100, 160, 150}));
        dashboard.getChart().addData(new ModelChart("Mayo", new double[]{200, 320, 150}));
        dashboard.getChart().addData(new ModelChart("Junio", new double[]{250, 450, 150}));
        dashboard.getChart().addData(new ModelChart("Julio", new double[]{220, 480, 150}));
        dashboard.getChart().addData(new ModelChart("Agosto", new double[]{180, 230, 150}));
        dashboard.getChart().addData(new ModelChart("Septiembre", new double[]{130, 300, 150}));
        dashboard.getChart().addData(new ModelChart("Octubre", new double[]{290, 430, 150}));
        dashboard.getChart().addData(new ModelChart("Noviembre", new double[]{150, 130, 250}));
    }

    private void loadCardsData() {
        try {
            dashboard.updateCards(totalDishesSold(), totalDrinksSold(), totalProfit());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int totalDishesSold() throws Exception {
        List<Sale> sales = daoSale.getData();
        int amount = 0;

        for (Sale sale : sales) {
            amount += sale.getAmountDish();
        }

        return amount;
    }

    private int totalDrinksSold() throws Exception {
        List<Sale> sales = daoSale.getData();
        int amount = 0;

        for (Sale sale : sales) {
            amount += sale.getAmountDrink();
        }

        return amount;
    }

    private float totalProfit() throws Exception {
        List<Sale> sales = daoSale.getData();
        float totalSales = 0;

        for (Sale sale : sales) {
            totalSales += sale.getTotalSale();
        }

        return totalSales;
    }

}
