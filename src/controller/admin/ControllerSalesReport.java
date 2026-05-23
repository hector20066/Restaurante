package controller.admin;

import model.dao.SaleDetailsDTO;
import model.dao.impl.DAOSaleDetailImpl;
import model.dao.interfaces.IDAOSalesDetails;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import observer.Observer;
import observer.Subject;
import view.administrators.SalesReport;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
                JOptionPane.showMessageDialog(salesReport, "Ha ocurrido un error al cargar la informacion", null, JOptionPane.ERROR_MESSAGE);
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
        try {
            List<SaleDetailsDTO> listSales = daoSaleDetail.getAllData();
            ReportSummaryDTO summary = calculateMostSold(listSales);

            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/ReporteVentas.jrxml"));
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listSales);
            Map<String,Object> parameters = new HashMap<>();

            parameters.put("bestSellingDish", summary.getBestDish());
            parameters.put("quantityDishes", summary.getQtyDish());
            parameters.put("bestSellingDrink", summary.getBestDrink());
            parameters.put("quantityDrinks", summary.getQtyDrink());

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);

            String path = System.getProperty("user.home") + File.separator + "Descargas" + File.separator + "ReporteVentas.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, path);
            JOptionPane.showMessageDialog(salesReport, "Se ha exportado el reporte completo de ventas.\nUbicacion: " + path, null, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(salesReport, "Ha ocurrido un error al exportar el reporte.", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData(List<SaleDetailsDTO> list) {
        salesReport.getModel().setRowCount(0);

        list.forEach(dto -> {
            int numSale = dto.getNumSale();
            String customer = dto.getCustomer();
            String dish = dto.getDish();
            int amountDish = dto.getAmountDish();
            String drink = dto.getDrink();
            int amountDrink = dto.getAmountDrink();
            LocalDate date = dto.getDate();
            float total = dto.getTotalSale();

            Object[] data = new Object[]{numSale, customer, dish, amountDish, drink, amountDrink, date, total};
            salesReport.getModel().addRow(data);
        });
    }

    private void displayMostSold(List<SaleDetailsDTO> list) {
        ReportSummaryDTO summary = calculateMostSold(list);

        salesReport.updateCards(summary.getBestDish(), summary.getBestDrink(), summary.getQtyDish(), summary.getQtyDrink());
    }

    private ReportSummaryDTO calculateMostSold(List<SaleDetailsDTO> list) {
        Map<String,Integer> dishCount = new HashMap<>();
        Map<String,Integer> drinkCount = new HashMap<>();

        for (SaleDetailsDTO dto : list) {
            String dish = dto.getDish();
            int dishAmount = dto.getAmountDish();

            if (dish != null && !dish.isBlank() && dishAmount > 0) {
                dishCount.merge(dish, dishAmount, Integer::sum);
            }

            String drink = dto.getDrink();
            int drinkAmount = dto.getAmountDrink();

            if (drink != null && !drink.isBlank() && drinkAmount > 0) {
                drinkCount.merge(drink, drinkAmount, Integer::sum);
            }
        }

        String bestDish = dishCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin datos");

        int qtyDish = dishCount.getOrDefault(bestDish, 0);

        String bestDrink = drinkCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin datos");

        int qtyDrink = drinkCount.getOrDefault(bestDrink, 0);

        return new ReportSummaryDTO(bestDish, qtyDish, bestDrink, qtyDrink);
    }

}
