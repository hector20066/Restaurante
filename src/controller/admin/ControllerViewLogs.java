package controller.admin;

import builder.controllers.ControllerViewLogsDbBuilder;
import model.dao.impl.DAODishDBImpl;
import model.dao.impl.DAODrinkDBImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.entities.Dish;
import model.entities.Drink;
import model.enums.TypeFood;
import observer.Observer;
import observer.Subject;
import utils.MessageUtils;
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
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Andres Cardenas
 */
public class ControllerViewLogs implements Observer {

    private final ViewLogsDB viewLogsDB;
    private final RegisterDish registerDish;
    private final RegisterDrink registerDrink;
    private final ControllerAdminPanel controllerAdminPanel;
    private final ControllerRegisterDish controllerRegisterDish;
    private final ControllerRegisterDrink controllerRegisterDrink;
    private final Subject subject;

    private final DAORestaurantDb<Drink> daoDrink;
    private final DAORestaurantDb<Dish> daoDish;

    public ControllerViewLogs(ControllerViewLogsDbBuilder builder) {
        this.viewLogsDB = builder.getViewLogsDB();
        this.registerDish = builder.getRegisterDish();
        this.registerDrink = builder.getRegisterDrink();
        this.controllerAdminPanel = builder.getControllerAdminPanel();
        this.controllerRegisterDish = builder.getControllerRegisterDish();
        this.controllerRegisterDrink = builder.getControllerRegisterDrink();
        this.subject = builder.getSubject();

        this.daoDrink = DAODrinkDBImpl.getInstance();
        this.daoDish = DAODishDBImpl.getInstance();

        this.subject.register(this);

        connectEvents();
    }

    private void connectEvents() {
        addDocumentSearchDish();
        addListenerSearchDish();

        addDocumentSearchDrink();
        addListenerSearchDrink();

        viewLogsDB.setControllerDeleteDish(e -> deleteDish());
        viewLogsDB.setControllerDeleteDrink(e -> deleteDrink());
        viewLogsDB.setControllerModifyDish(e -> modifyDish());
        viewLogsDB.setControllerModifyDrink(e -> modifyDrink());
    }

    private void addDocumentSearchDish() {
        viewLogsDB.setControllerSearchDishDocument(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(viewLogsDB.getTxt_searchDish(), viewLogsDB.getJtbl_dishes(), "Buscar Plato");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(viewLogsDB.getTxt_searchDish(), viewLogsDB.getJtbl_dishes(), "Buscar Plato");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(viewLogsDB.getTxt_searchDish(), viewLogsDB.getJtbl_dishes(), "Buscar Plato");
            }
        });
    }

    private void addDocumentSearchDrink() {
        viewLogsDB.setControllerSearchDrinkDocument(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(viewLogsDB.getTxt_searchDrink(), viewLogsDB.getJtbl_drinks(), "Buscar Bebida");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(viewLogsDB.getTxt_searchDrink(), viewLogsDB.getJtbl_drinks(), "Buscar Bebida");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(viewLogsDB.getTxt_searchDrink(), viewLogsDB.getJtbl_drinks(), "Buscar Bebida");
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
        viewLogsDB.setControllerSearchDishFocus(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (viewLogsDB.getTxt_searchDish().getText().isBlank()) {
                    viewLogsDB.getTxt_searchDish().setText("Buscar Plato");
                }
            }
        });

        viewLogsDB.setControllerSearchDishMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewLogsDB.getTxt_searchDish().getText().equals("Buscar Plato")) {
                    viewLogsDB.getTxt_searchDish().setText("");
                }
            }
        });
    }

    private void addListenerSearchDrink() {
        viewLogsDB.setControllerSearchDrinkFocus(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (viewLogsDB.getTxt_searchDrink().getText().isBlank()) {
                    viewLogsDB.getTxt_searchDrink().setText("Buscar Bebida");
                }
            }
        });

        viewLogsDB.setControllerSearchDrinkMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewLogsDB.getTxt_searchDrink().getText().equals("Buscar Bebida")) {
                    viewLogsDB.getTxt_searchDrink().setText("");
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
                    loadDishes(dishes, viewLogsDB.getModelDishes());
                    loadDrinks(drinks, viewLogsDB.getModelDrinks());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void deleteDish() {
        try {
            if (verifySelectedRows(viewLogsDB.getJtbl_dishes())) {
                int code = Integer.parseInt(getDataFromTable(viewLogsDB.getJtbl_dishes(), 1));

                runDelete(() -> {
                    try {
                        daoDish.delete(code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                MessageUtils.warningMessage("Por favor seleccione el plato a eliminar");
            }
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al eliminar el plato");
            e.printStackTrace();
        }
    }

    private void deleteDrink() {
        try {
            if (verifySelectedRows(viewLogsDB.getJtbl_drinks())) {
                int code = Integer.parseInt(getDataFromTable(viewLogsDB.getJtbl_drinks(), 1));

                runDelete(() -> {
                    try {
                        daoDrink.delete(code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                MessageUtils.warningMessage("Por favor seleccione el plato a eliminar");
            }
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al eliminar la bebida");
        }
    }

    private void runDelete(Runnable deleteOperation) {
        new Thread(() -> {
            deleteOperation.run();

            SwingUtilities.invokeLater(() -> {
                MessageUtils.informationMessage("Se ha eliminado el registro");
                subject.nofifyObserver();
            });
        }).start();
    }

    private void modifyDish() {
        if (verifySelectedRows(viewLogsDB.getJtbl_dishes())) {
            String name = getDataFromTable(viewLogsDB.getJtbl_dishes(), 0);
            String price = getDataFromTable(viewLogsDB.getJtbl_dishes(), 2);
            String description = getDataFromTable(viewLogsDB.getJtbl_dishes(), 4);
            String type = getDataFromTable(viewLogsDB.getJtbl_dishes(), 3);
            int code = Integer.parseInt(getDataFromTable(viewLogsDB.getJtbl_dishes(), 1));

            controllerAdminPanel.showRegisterDish();
            controllerRegisterDish.setCodeDish(code);
            controllerRegisterDish.setModify(true);

            loadDataFieldsDish(name, price, description, type);
        } else {
            MessageUtils.warningMessage("Por favor seleccione el plato a modificar");
        }
    }

    private void loadDataFieldsDish(String name, String price, String description, String type) {
        registerDish.getTxt_name().setText(name);
        registerDish.getTxt_price().setText(price);
        registerDish.getTxt_description().setText(description);
        registerDish.getBtn_register().setText("MODIFICAR PLATO");

        if (type.equals("Plato Colombiano")) {
            registerDish.getCbo_typeDish().setSelectedIndex(0);
        } else {
            if (type.equals("Plato Italiano")) {
                registerDish.getCbo_typeDish().setSelectedIndex(1);
            } else {
                registerDish.getCbo_typeDish().setSelectedIndex(2);
            }
        }
    }

    private void modifyDrink() {
        if (verifySelectedRows(viewLogsDB.getJtbl_drinks())) {
            String name = getDataFromTable(viewLogsDB.getJtbl_drinks(), 0);
            String price = getDataFromTable(viewLogsDB.getJtbl_drinks(), 2);
            String description = getDataFromTable(viewLogsDB.getJtbl_drinks(), 4);
            String type = getDataFromTable(viewLogsDB.getJtbl_drinks(), 3);
            int code = Integer.parseInt(getDataFromTable(viewLogsDB.getJtbl_drinks(), 1));

            controllerAdminPanel.showRegisterDrink();
            controllerRegisterDrink.setModify(true);
            controllerRegisterDrink.setCodeDrink(code);

            loadDataFieldsDrink(name, description, price, type);
        } else {
            MessageUtils.warningMessage("Por favor seleccione la bebida a modificar");
        }
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

        if (type.equals("Bebida Colombiana")) {
            registerDrink.getCbo_typeDrink().setSelectedIndex(0);
        } else {
            if (type.equals("Bebida Italiana")) {
                registerDrink.getCbo_typeDrink().setSelectedIndex(1);
            } else {
                registerDrink.getCbo_typeDrink().setSelectedIndex(2);
            }
        }
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

}
