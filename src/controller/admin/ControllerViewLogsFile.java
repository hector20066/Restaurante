package controller.admin;

import factory.FactoryMethod;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.DAORestaurantFile;
import model.entities.Dish;
import model.entities.Drink;
import model.enums.InstanceTypeDB;
import model.enums.InstanceTypeFile;
import observer.Observer;
import observer.Subject;
import utils.MessageUtils;
import view.administrators.ViewLogsFile;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;

/**
 *
 * @author hectoracevedo
 */
public class ControllerViewLogsFile implements Observer {

    private final ViewLogsFile viewLogsFile;
    private final Subject subject;

    private final DAORestaurantFile<Drink> daoDrinkFile;
    private final DAORestaurantFile<Dish> daoDishFile;

    private final DAORestaurantDb<Drink> daoDrinkDB;
    private final DAORestaurantDb<Dish> daoDishDB;

    public ControllerViewLogsFile(ViewLogsFile viewLogsFile, Subject subject) {
        this.viewLogsFile = viewLogsFile;
        this.subject = subject;

        this.daoDishFile = FactoryMethod.getInstanceFile(InstanceTypeFile.DAODISHFILE);
        this.daoDrinkFile = FactoryMethod.getInstanceFile(InstanceTypeFile.DAODRINKFILE);
        this.daoDishDB = FactoryMethod.getInstanceDB(InstanceTypeDB.DAODISHDB);
        this.daoDrinkDB = FactoryMethod.getInstanceDB(InstanceTypeDB.DAODRINKDB);

        this.subject.register(this);

        connectEvents();
    }

    private void connectEvents() {
        addDocumentSearchDish();
        addListenerSearchDish();

        addDocumentSearchDrink();
        addListenerSearchDrink();

        viewLogsFile.setControllerAddDBDish(e -> addDishDB());
        viewLogsFile.setControllerAddDBDrink(e -> addDrinkDB());
        viewLogsFile.setControllerDeleteDish(e -> deleteDish());
        viewLogsFile.setControllerDeleteDrink(e -> deleteDrink());
    }

    private void addDocumentSearchDish() {
        viewLogsFile.setControllerSearchDishDocument(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(viewLogsFile.getTxt_searchDish(), viewLogsFile.getJtbl_dishes(), "Buscar Plato");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(viewLogsFile.getTxt_searchDish(), viewLogsFile.getJtbl_dishes(), "Buscar Plato");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(viewLogsFile.getTxt_searchDish(), viewLogsFile.getJtbl_dishes(), "Buscar Plato");
            }
        });
    }

    private void addDocumentSearchDrink() {
        viewLogsFile.setControllerSearchDrinkDocument(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(viewLogsFile.getTxt_searchDrink(), viewLogsFile.getJtbl_drinks(), "Buscar Bebida");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(viewLogsFile.getTxt_searchDrink(), viewLogsFile.getJtbl_drinks(), "Buscar Bebida");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(viewLogsFile.getTxt_searchDrink(), viewLogsFile.getJtbl_drinks(), "Buscar Bebida");
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
        viewLogsFile.setControllerSearchDishFocus(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (viewLogsFile.getTxt_searchDish().getText().isBlank()) {
                    viewLogsFile.getTxt_searchDish().setText("Buscar Plato");
                }
            }
        });

        viewLogsFile.setControllerSearchDishMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewLogsFile.getTxt_searchDish().getText().equals("Buscar Plato")) {
                    viewLogsFile.getTxt_searchDish().setText("");
                }
            }
        });
    }

    private void addListenerSearchDrink() {
        viewLogsFile.setControllerSearchDrinkFocus(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (viewLogsFile.getTxt_searchDrink().getText().isBlank()) {
                    viewLogsFile.getTxt_searchDrink().setText("Buscar Bebida");
                }
            }
        });

        viewLogsFile.setControllerSearchDrinkMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (viewLogsFile.getTxt_searchDrink().getText().equals("Buscar Bebida")) {
                    viewLogsFile.getTxt_searchDrink().setText("");
                }
            }
        });
    }

    @Override
    public void update() {
        new Thread(() -> {
            loadDishes();
            loadDrinks();
        }).start();
    }

    private void addDishDB() {
        if (verifySelectedRows(viewLogsFile.getJtbl_dishes())) {
            String name = getDataFromTable(viewLogsFile.getJtbl_dishes(), 0);

            new Thread(() -> {
                try {
                    Dish dish = daoDishFile.getData(name);

                    if (dish != null) {
                        daoDishDB.save(dish);
                        daoDishFile.delete(name);
                    }

                    SwingUtilities.invokeLater(() -> {
                        MessageUtils.informationMessage("Se ha registrado el plato en la base de datos");
                        subject.nofifyObserver();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            MessageUtils.warningMessage("Por favor seleccione el plato");
        }
    }

    private void addDrinkDB() {
        if (verifySelectedRows(viewLogsFile.getJtbl_drinks())) {
            String name = getDataFromTable(viewLogsFile.getJtbl_drinks(), 0);

            new Thread(() -> {
                try {
                    Drink drink = daoDrinkFile.getData(name);

                    if (drink != null) {
                        daoDrinkDB.save(drink);
                        daoDrinkFile.delete(name);
                    }

                    SwingUtilities.invokeLater(() -> {
                        MessageUtils.informationMessage("Se ha registrado la bebida en la base de datos");
                        subject.nofifyObserver();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            MessageUtils.warningMessage("Por favor seleccione la bebida");
        }
    }

    public void deleteDish() {
        try {
            if (verifySelectedRows(viewLogsFile.getJtbl_dishes())) {
                String name = getDataFromTable(viewLogsFile.getJtbl_dishes(), 0);

                runDelete(() -> {
                    try {
                        daoDishFile.delete(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                MessageUtils.warningMessage("Por favor seleccione el registro a eliminar");
            }
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al tratar de eliminar el registro");
            e.printStackTrace();
        }
    }

    public void deleteDrink() {
        try {
            if (verifySelectedRows(viewLogsFile.getJtbl_drinks())) {
                String name = getDataFromTable(viewLogsFile.getJtbl_drinks(), 0);

                runDelete(() -> {
                    try {
                        daoDrinkFile.delete(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                MessageUtils.warningMessage("Por favor seleccione el registro a eliminar");
            }
        } catch (Exception e) {
            MessageUtils.errorMessage("Ha ocurrido un error al tratar de eliminar el registro");
        }
    }

    private boolean verifySelectedRows(JTable table) {
        return table.getSelectedRow() >= 0;
    }

    private String getDataFromTable(JTable table, int column) {
        return String.valueOf(table.getValueAt(table.getSelectedRow(), column));
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

    private void loadDrinks() {
        try {
            ControllerViewLogsDB.loadDrinks(daoDrinkFile.getData(), viewLogsFile.getModelDrinks());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDishes() {
        try {
            ControllerViewLogsDB.loadDishes(daoDishFile.getData(), viewLogsFile.getModelDishes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
