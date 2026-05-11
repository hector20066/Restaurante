package controller.admin;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import observer.Observer;
import observer.Subject;
import view.administrators.SalesReport;

/**
 *
 * @author Mi PC
 */
public class ControllerSalesReport implements Observer{

    private final SalesReport salesReport;
    
    public ControllerSalesReport(SalesReport salesReport, Subject subject) {
        this.salesReport = salesReport;
        
        subject.register(this);
        
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
    
}
