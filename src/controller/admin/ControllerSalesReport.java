package controller.admin;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import model.dao.SaleDetailsDTO;
import model.dao.impl.DAOSaleDetailImpl;
import model.dao.interfaces.IDAOSalesDetails;
import observer.Observer;
import observer.Subject;
import view.administrators.SalesReport;

/**
 *
 * @author Mi PC
 */
public class ControllerSalesReport implements Observer {

    private final SalesReport salesReport;
    private final IDAOSalesDetails daoSaleDetail;

    public ControllerSalesReport(SalesReport salesReport, Subject subject) {
        this.salesReport = salesReport;

        subject.register(this);
        this.daoSaleDetail = DAOSaleDetailImpl.getInstance();

        connectEvents();
    }

    private void connectEvents() {
        salesReport.setControllerExport(e -> export());
        salesReport.setControllerSearchDocument(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        salesReport.setControllerSearchFocus(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (salesReport.getTxt_search().getText().isBlank()) {
                    salesReport.getTxt_search().setText("Buscar Venta");
                }
            }
        });

        salesReport.setControllerSearchMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (salesReport.getTxt_search().getText().equals("Buscar Venta")) {
                    salesReport.getTxt_search().setText("");
                }
            }
        });
    }

    @Override
    public void update() {
        new Thread(() -> {
            try {
                List<SaleDetailsDTO> saleDetails = daoSaleDetail.getAllData();

                SwingUtilities.invokeLater(() -> {
                    loadData(saleDetails);
                    displayMostSold(saleDetails);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void filterTable() {
        if (!salesReport.getTxt_search().getText().equals("Buscar Venta")) {
            String texto = salesReport.getTxt_search().getText();
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(salesReport.getJtbl_salesReport().getModel());
            salesReport.getJtbl_salesReport().setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
        }
    }

    private void export() {

    }

    private void loadData(List<SaleDetailsDTO> list) {
        list.forEach(dto -> {
            int numSale = dto.getNumSale();
            String customer = dto.getCustomer();
            String dish = dto.getDish();
            int amountDish = dto.getQuantityDishesSold();
            String drink = dto.getDrink();
            int amountDrink = dto.getQuantityDrinksSold();
            LocalDate date = dto.getDate();
            float total = dto.getTotalSale();

            Object[] data = new Object[]{numSale, customer, dish, amountDish, drink, amountDrink, date, total};
            salesReport.getModel().addRow(data);
        });
    }

    private void displayMostSold(List<SaleDetailsDTO> list) {
        Map<String, Integer> dishCount = new HashMap<>();
        Map<String, Integer> drinkCount = new HashMap<>();

        for (SaleDetailsDTO dto : list) {
            String dish = dto.getDish();
            int amount = dto.getQuantityDishesSold();

            if (dish != null && !dish.isBlank() && amount > 0) {
                dishCount.put(dish, dishCount.getOrDefault(dish, 0) + amount);
            }
        }

        for (SaleDetailsDTO dto : list) {
            String drink = dto.getDrink();
            int amount = dto.getQuantityDrinksSold();

            if (drink != null && !drink.isBlank() && amount > 0) {
                drinkCount.put(drink, drinkCount.getOrDefault(drink, 0) + amount);
            }
        }

        String mostSoldDish = "Sin datos";
        int totalAmountDish = 0;

        if (!dishCount.isEmpty()) {
            mostSoldDish = dishCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Sin datos");
            
            Integer dishAmount = dishCount.get(mostSoldDish);
            
            if (dishAmount != null) {
                totalAmountDish = dishAmount;
            }
        }

        String mostSoldDrink = "Sin datos";
        int totalAmountDrink = 0;

        if (!drinkCount.isEmpty()) {
            mostSoldDrink = drinkCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Sin datos");
            
            Integer drinkAmount = drinkCount.get(mostSoldDrink);
            
            if (drinkAmount != null) {
                totalAmountDrink = drinkAmount;
            }
        }

        salesReport.updateCards(mostSoldDish, mostSoldDrink, totalAmountDish, totalAmountDrink);
    }

}
