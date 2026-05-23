package controller.admin;

import builder.controllers.ControllerViewLogsDbBuilder;
import model.dao.impl.DAODishImpl;
import model.dao.impl.DAODrinkImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Dish;
import model.entities.Drink;
import model.enums.TypeFood;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import observer.Observer;
import observer.Subject;
import view.administrators.RegisterDish;
import view.administrators.RegisterDrink;
import view.administrators.ViewLogsDB;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerViewLogs implements Observer {

    private final ViewLogsDB viewLogs;
    private final RegisterDish registerDish;
    private final RegisterDrink registerDrink;
    private final ControllerAdminPanel controllerAdminPanel;
    private final ControllerRegisterDish controllerRegisterDish;
    private final ControllerRegisterDrink controllerRegisterDrink;
    private final Subject subject;

    private final DAORestaurantDb<Drink> daoDrink;
    private final DAORestaurantDb<Dish> daoDish;

    public ControllerViewLogs(ControllerViewLogsDbBuilder builder) {
        this.viewLogs = builder.getViewLogsDB();
        this.registerDish = builder.getRegisterDish();
        this.registerDrink = builder.getRegisterDrink();
        this.controllerAdminPanel = builder.getControllerAdminPanel();
        this.controllerRegisterDish = builder.getControllerRegisterDish();
        this.controllerRegisterDrink = builder.getControllerRegisterDrink();
        this.subject = builder.getSubject();

        this.daoDrink = DAODrinkImpl.getInstance();
        this.daoDish = DAODishImpl.getInstance();

        this.subject.register(this);

        connectEvents();
    }

    public static void loadDrinks(List<Drink> listDrinks, DefaultTableModel modelDrinks) {
        modelDrinks.setRowCount(0);

        for (Drink drink : listDrinks) {
            String name = drink.getName();
            int code = drink.getCode();
            float price = drink.getPrice();
            TypeFood type = drink.getTypeDrink();
            String description = drink.getDescription();

            Object[] data = new Object[]{name, code, price, type.getValue(), description};
            modelDrinks.addRow(data);
        }
    }

    public static void loadDishes(List<Dish> listDishes, DefaultTableModel modelDishes) {
        modelDishes.setRowCount(0);

        for (Dish dish : listDishes) {
            String name = dish.getName();
            int code = dish.getCode();
            float price = dish.getPrice();
            TypeFood type = dish.getTypeDish();
            String description = dish.getDescription();

            Object[] data = new Object[]{name, code, price, type.getValue(), description};
            modelDishes.addRow(data);
        }
    }

    private void connectEvents() {
        addDocumentSearchDish();
        addListenerSearchDish();

        addDocumentSearchDrink();
        addListenerSearchDrink();

        viewLogs.setControllerDeleteDish(e -> deleteDish());
        viewLogs.setControllerDeleteDrink(e -> deleteDrink());
        viewLogs.setControllerModifyDish(e -> modifyDish());
        viewLogs.setControllerModifyDrink(e -> modifyDrink());
        viewLogs.setControllerExportDishes(e -> exportDishes());
        viewLogs.setControllerExportDrinks(e -> exportDrinks());
    }

    private void addDocumentSearchDish() {
        viewLogs.setControllerSearchDishDocument(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(viewLogs.getTxt_searchDish(), viewLogs.getJtbl_dishes(), "Buscar Plato");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(viewLogs.getTxt_searchDish(), viewLogs.getJtbl_dishes(), "Buscar Plato");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(viewLogs.getTxt_searchDish(), viewLogs.getJtbl_dishes(), "Buscar Plato");
            }
        });
    }

    private void addDocumentSearchDrink() {
        viewLogs.setControllerSearchDrinkDocument(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(viewLogs.getTxt_searchDrink(), viewLogs.getJtbl_drinks(), "Buscar Bebida");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(viewLogs.getTxt_searchDrink(), viewLogs.getJtbl_drinks(), "Buscar Bebida");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(viewLogs.getTxt_searchDrink(), viewLogs.getJtbl_drinks(), "Buscar Bebida");
            }
        });
    }

    private void filterTable(JTextField textField, JTable table, String text) {
        if (!textField.getText().equals(text)) {
            String texto = textField.getText();
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
        }
    }

    private void addListenerSearchDish() {
        viewLogs.setControllerSearchDishFocus(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (viewLogs.getTxt_searchDish().getText().isBlank()) {
                    viewLogs.getTxt_searchDish().setText("Buscar Plato");
                }
            }
        });

        viewLogs.setControllerSearchDishMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewLogs.getTxt_searchDish().getText().equals("Buscar Plato")) {
                    viewLogs.getTxt_searchDish().setText("");
                }
            }
        });
    }

    private void addListenerSearchDrink() {
        viewLogs.setControllerSearchDrinkFocus(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (viewLogs.getTxt_searchDrink().getText().isBlank()) {
                    viewLogs.getTxt_searchDrink().setText("Buscar Bebida");
                }
            }
        });

        viewLogs.setControllerSearchDrinkMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewLogs.getTxt_searchDrink().getText().equals("Buscar Bebida")) {
                    viewLogs.getTxt_searchDrink().setText("");
                }
            }
        });
    }

    @Override
    public void update() {
        new Thread(() -> {
            try {
                List<Dish> dishes = daoDish.getData();
                List<Drink> drinks = daoDrink.getData();

                SwingUtilities.invokeLater(() -> {
                    loadDishes(dishes, viewLogs.getModelDishes());
                    loadDrinks(drinks, viewLogs.getModelDrinks());
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(viewLogs, "Error al cargar los platos en el reporte", "", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void deleteDish() {
        try {
            if (verifySelectedRows(viewLogs.getJtbl_dishes())) {
                int code = Integer.parseInt(getDataFromTable(viewLogs.getJtbl_dishes(), 1));

                runDelete(() -> {
                    try {
                        daoDish.delete(code);
                    } catch (Exception e) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(viewLogs, "Error al eliminar el plato", "", JOptionPane.ERROR_MESSAGE));
                    }
                });
            } else {
                JOptionPane.showMessageDialog(viewLogs, "Por favor seleccione el plato a eliminar", "", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(viewLogs, "Ha ocurrido un error al eliminar el plato", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDrink() {
        try {
            if (verifySelectedRows(viewLogs.getJtbl_drinks())) {
                int code = Integer.parseInt(getDataFromTable(viewLogs.getJtbl_drinks(), 1));

                runDelete(() -> {
                    try {
                        daoDrink.delete(code);
                    } catch (Exception e) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(viewLogs, "Error al eliminar la bebida", "", JOptionPane.ERROR_MESSAGE));
                    }
                });
            } else {
                JOptionPane.showMessageDialog(viewLogs, "Por favor seleccione el plato a eliminar", "", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(viewLogs, "Ha ocurrido un error al eliminar la bebida", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runDelete(Runnable deleteOperation) {
        deleteOperation.run();
        JOptionPane.showMessageDialog(viewLogs, "Se ha eliminado el registro", "", JOptionPane.INFORMATION_MESSAGE);
        subject.notifyObserver();
    }

    private void modifyDish() {
        if (verifySelectedRows(viewLogs.getJtbl_dishes())) {
            String name = getDataFromTable(viewLogs.getJtbl_dishes(), 0);
            String price = getDataFromTable(viewLogs.getJtbl_dishes(), 2);
            String description = getDataFromTable(viewLogs.getJtbl_dishes(), 4);
            String type = getDataFromTable(viewLogs.getJtbl_dishes(), 3);
            int code = Integer.parseInt(getDataFromTable(viewLogs.getJtbl_dishes(), 1));

            controllerAdminPanel.showRegisterDish();
            controllerRegisterDish.setCodeDish(code);
            controllerRegisterDish.setModify(true);

            loadDataFieldsDish(name, price, description, type);
        } else {
            JOptionPane.showMessageDialog(viewLogs, "Por favor seleccione el plato a modificar", "", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadDataFieldsDish(String name, String price, String description, String type) {
        registerDish.getTxt_name().setText(name);
        registerDish.getTxt_price().setText(price);
        registerDish.getTxt_description().setText(description);
        registerDish.getBtn_register().setText("MODIFICAR PLATO");

        if (type.equals("Colombiano")) {
            registerDish.getCbo_typeDish().setSelectedIndex(0);
        } else {
            if (type.equals("Italiano")) {
                registerDish.getCbo_typeDish().setSelectedIndex(1);
            } else {
                registerDish.getCbo_typeDish().setSelectedIndex(2);
            }
        }
    }

    private void modifyDrink() {
        if (verifySelectedRows(viewLogs.getJtbl_drinks())) {
            String name = getDataFromTable(viewLogs.getJtbl_drinks(), 0);
            String price = getDataFromTable(viewLogs.getJtbl_drinks(), 2);
            String description = getDataFromTable(viewLogs.getJtbl_drinks(), 4);
            String type = getDataFromTable(viewLogs.getJtbl_drinks(), 3);
            int code = Integer.parseInt(getDataFromTable(viewLogs.getJtbl_drinks(), 1));

            controllerAdminPanel.showRegisterDrink();
            controllerRegisterDrink.setModify(true);
            controllerRegisterDrink.setCodeDrink(code);

            loadDataFieldsDrink(name, description, price, type);
        } else {
            JOptionPane.showMessageDialog(viewLogs, "Por favor seleccione la bebida a modificar", "", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void exportDishes() {
        try {
            List<Dish> listDishes = daoDish.getData();
            String compileReport = "/reports/PlatosDisponibles.jrxml";
            String path = System.getProperty("user.home") + File.separator + "Descargas" + File.separator + "PlatosDisponibles.pdf";

            export(listDishes, compileReport, path);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(viewLogs, "Ha ocurrido un error al exportar el reporte.", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportDrinks() {
        try {
            List<Drink> listDrinks = daoDrink.getData();
            String compileReport = "/reports/BebidasDisponibles.jrxml";
            String path = System.getProperty("user.home") + File.separator + "Descargas" + File.separator + "BebidasDisponibles.pdf";

            export(listDrinks, compileReport, path);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(viewLogs, "Ha ocurrido un error al exportar el reporte.", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    private <T> void export(List<T> list, String compileReport, String path) throws Exception {
        JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream(compileReport));
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setVisible(true);

        JasperExportManager.exportReportToPdfFile(jasperPrint, path);
        JOptionPane.showMessageDialog(viewLogs, "Se ha exportado el reporte completo de ventas.\nUbicacion: " + path, null, JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean verifySelectedRows(JTable table) {
        return table.getSelectedRow() >= 0;
    }

    private String getDataFromTable(JTable table, int column) {
        return String.valueOf(table.getValueAt(table.getSelectedRow(), column));
    }

    private void loadDataFieldsDrink(String name, String description, String price, String type) {
        registerDrink.getTxt_name().setText(name);
        registerDrink.getTxt_description().setText(description);
        registerDrink.getTxt_price().setText(price);
        registerDrink.getBtn_register().setText("MODIFICAR BEBIDA");

        if (type.equals("Colombiana")) {
            registerDrink.getCbo_typeDrink().setSelectedIndex(0);
        } else {
            if (type.equals("Italiana")) {
                registerDrink.getCbo_typeDrink().setSelectedIndex(1);
            } else {
                registerDrink.getCbo_typeDrink().setSelectedIndex(2);
            }
        }
    }

}
