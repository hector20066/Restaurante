package controller.customers;

import model.dao.impl.DAODishImpl;
import model.dao.impl.DAODrinkImpl;
import model.dao.impl.DAOCustomersImpl;
import model.dao.impl.DAOSalesImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.IDAO;
import model.entities.Customer;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.Sale;
import model.enums.TypeFood;
import observer.Observer;
import service.ServiceEmailjS;
import utils.MessageUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andres Cardenas
 */
public abstract class AbstractFoodController implements Observer {

    protected int dishCode;
    protected int drinkCode;
    protected float totalSale;

    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;
    private final IDAO<Customer> daoCustomer;
    private final IDAO<Sale> daoSales;

    public AbstractFoodController() {
        this.daoDish = DAODishImpl.getInstance();
        this.daoDrink = DAODrinkImpl.getInstance();
        this.daoCustomer = DAOCustomersImpl.getInstance();
        this.daoSales = DAOSalesImpl.getInstance();
    }

    @Override
    public void update() {
        new Thread(() -> {
            /*try {
                List<Dish> dishes = daoDish.getData();
                List<Drink> drinks = daoDrink.getData();

                SwingUtilities.invokeLater(() -> {
                    loadDishes(dishes, getTypeFood());
                    loadDrinks(drinks, getTypeFood());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }).start();
    }

    protected abstract TypeFood getTypeFood();
    protected abstract void connectEvents();

    protected abstract JTextField getTxt_amountDish();
    protected abstract JTextField getTxt_priceDish();
    protected abstract JTextField getTxt_amountDrink();
    protected abstract JTextField getTxt_priceDrink();
    protected abstract JTextField getTxt_descriptionDish();
    protected abstract JTextField getTxt_descriptionDrink();
    protected abstract JTextField getTxt_totalSale();
    protected abstract JTextField getTxt_customerName();
    protected abstract JTextField getTxt_customerId();
    protected abstract JTextField getTxt_customerEmail();
    protected abstract JTextField getTxt_customerAddress();

    protected abstract JComboBox<String> getCbo_drink();
    protected abstract JComboBox<String> getCbo_dish();

    protected abstract JTable getJtbl_food();
    protected abstract DefaultTableModel getTableModelFood();

    protected final void addOrder() {
        String amountDishString = getTxt_amountDish().getText().trim();
        String amountDrinkString = getTxt_amountDrink().getText().trim();

        if (noDishSelected() && noDrinkSelected() && amountDishString.isBlank() || amountDrinkString.isBlank()) {
            MessageUtils.warningMessage("Por favor ingrese la cantidad deseada.");
            return;
        }

        String nameDish = "No seleccionado";
        String priceDishString = getTxt_priceDish().getText().trim();
        int amountDish = 0;
        float priceDish = 0;

        if (!amountDishString.isBlank() && !priceDishString.isBlank()) {
            nameDish = (String) getCbo_dish().getSelectedItem();
            amountDish = Integer.parseInt(amountDishString);
            priceDish = Float.parseFloat(priceDishString);
        }

        String nameDrink = "No seleccionada";
        String priceDrinkString = getTxt_priceDrink().getText().trim();
        int amountDrink = 0;
        float priceDrink = 0;

        if (!amountDrinkString.isBlank() && !priceDrinkString.isBlank()) {
            nameDrink = (String) getCbo_drink().getSelectedItem();
            amountDrink = Integer.parseInt(amountDrinkString);
            priceDrink = Float.parseFloat(priceDrinkString);
        }

        loadDataTable(nameDish, amountDish, priceDish, nameDrink, amountDrink, priceDrink);
    }

    protected boolean noDishSelected() {
        return getCbo_dish().getSelectedIndex() == 0;
    }

    protected boolean noDrinkSelected() {
        return getCbo_drink().getSelectedIndex() == 0;
    }

    protected final void loadDataTable(String nameDish, int amountDish, float priceDish, String nameDrink, int amountDrink, float priceDrink) {
        float totalOrder = (amountDish * priceDish) + (amountDrink * priceDrink);

        Object[] data = new Object[]{nameDish, this.dishCode, amountDish, nameDrink, this.drinkCode, amountDrink, totalOrder};
        getTableModelFood().addRow(data);
        this.totalSale += totalOrder;
        getTxt_totalSale().setText(String.valueOf(this.totalSale));
    }

    protected void registerSale() {
        try {
            carryOutTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final void carryOutTransaction() {
        String customerName = getTxt_customerName().getText();
        String customerId = getTxt_customerId().getText().trim();
        String customerEmail = getTxt_customerEmail().getText().trim();
        String customerAddress = getTxt_customerAddress().getText();

        if (isInvalidCustomerData(customerName, customerId, customerEmail, customerAddress)) {
            MessageUtils.warningMessage("Por favor ingrese todos los datos solicitados");
            return;
        }

        Customer customer = new Customer();
        assignValuesCustomer(customer, customerName, customerId, customerEmail, customerAddress);

        new Thread(() -> {
            try {
                if (!daoCustomer.exists(customer.getId())) {
                    daoCustomer.save(customer);
                }

                SwingUtilities.invokeLater(() -> {
                    saveAllSale(customerId, customerName, customerEmail);
                    MessageUtils.informationMessage("Se ha registrado su compra\nEn un momento lo contactaremos para la entrega.");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected boolean isInvalidCustomerData(String customerName, String customerId, String customerEmail, String customerAddress) {
        return customerName.isBlank() || customerId.isBlank() || customerEmail.isBlank() || customerAddress.isBlank();
    }

    protected void assignValuesCustomer(Customer customer, String name, String id, String email, String address) {
        customer.setName(name);
        customer.setId(Integer.parseInt(id));
        customer.setEmail(email);
        customer.setAddress(address);
    }

    protected final void saveAllSale(String customerId, String customerName, String customerEmail) {
        int rows = getJtbl_food().getRowCount();
        String dishName;
        String amountDish;
        String drinkName;
        String amountDrink;
        List<String> details = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            dishName = getJtbl_food().getValueAt(i, 0).toString();
            int dishCode = Integer.parseInt(getJtbl_food().getValueAt(i, 1).toString());
            amountDish = getJtbl_food().getValueAt(i, 2).toString();
            drinkName = getJtbl_food().getValueAt(i, 3).toString();
            int drinkCode = Integer.parseInt(getJtbl_food().getValueAt(i, 4).toString());
            amountDrink = getJtbl_food().getValueAt(i, 5).toString();
            float saleSubtotal = Float.parseFloat(getJtbl_food().getValueAt(i, 6).toString());

            addDetailsIfValid(details, dishName, amountDish, "No seleccionado");
            addDetailsIfValid(details, drinkName, amountDrink, "No seleccionada");

            addSale(customerId, dishCode, Integer.parseInt(amountDish), drinkCode, Integer.parseInt(amountDrink), saleSubtotal);
        }

        sendConfirmationSale(customerName, customerEmail, details, String.valueOf(this.totalSale));
    }

    protected void addDetailsIfValid(List<String> details, String name, String amount, String invalidText) {
        if (!name.equals(invalidText) && amount != null && !amount.equals("0") && !amount.isBlank()) {
            details.add(name + " x " + amount);
        }
    }

    protected final void addSale(String customerId, int dishCode, int amountDish, int drinkCode, int amountDrink, float saleSubtotal) {
         Sale sale = new Sale();
         assignValuesSale(sale, customerId, dishCode, amountDish, drinkCode, amountDrink, saleSubtotal);

         new Thread(() -> {
            try {
                daoSales.save(sale);
                SwingUtilities.invokeLater(this::clearDataSale);
            } catch (Exception e) {
                e.printStackTrace();
            }
         }).start();
    }

    protected void assignValuesSale(Sale sale, String customerId, int dishCode, int amountDish, int drinkCode, int amountDrink, float saleSubtotal) {
        /*sale.setDishCode(dishCode);
        sale.setAmountDish(amountDish);
        sale.setDrinkCode(drinkCode);
        sale.setAmountDrink(amountDrink);*/
        sale.setClientId(Integer.parseInt(customerId));
        //sale.setTotalSale(saleSubtotal);
        sale.setDate(LocalDate.now());
    }

    protected final void sendConfirmationSale(String customerName, String customerEmail, List<String> details, String totalSale) {
        try {
            new Thread(() -> {
                ServiceEmailjS serviceEmailjS = new ServiceEmailjS();
                String response = serviceEmailjS.sendEmail(customerName, customerEmail, details, totalSale);
                IO.println(response);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void clearDataSale() {
        getTableModelFood().setRowCount(0);
        getTxt_totalSale().setText("");
        getTxt_customerId().setText("");
        getTxt_customerName().setText("");
        getTxt_customerAddress().setText("");
        getTxt_customerEmail().setText("");

        getTxt_descriptionDish().setText("");
        getTxt_descriptionDrink().setText("");
        getTxt_amountDish().setText("");
        getTxt_amountDrink().setText("");
        getTxt_priceDish().setText("");
        getTxt_priceDrink().setText("");
        getCbo_drink().setSelectedIndex(0);
        getCbo_dish().setSelectedIndex(0);

        totalSale = 0;
    }

    protected final void showDishInfo() {
        Object selectedItem = getCbo_dish().getSelectedItem();

        if (selectedItem == null || noDishSelected()) return;
        String dishName = (String) getCbo_dish().getSelectedItem();

        new Thread(() -> {
            try {
                Dish dish = daoDish.getData(dishName);

                if (dish == null) return;

                SwingUtilities.invokeLater(() -> {
                    loadDishData(dish);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected abstract void loadDishData(Dish dish);

    protected final void showDrinkInfo() {
        Object selectedItem = getCbo_drink().getSelectedItem();

        if (selectedItem == null || noDrinkSelected()) return;
        String drinkName = (String) getCbo_drink().getSelectedItem();

        new Thread(() -> {
           try {
               Drink drink = daoDrink.getData(drinkName);

               if (drink == null) return;

               SwingUtilities.invokeLater(() -> {
                   loadDrinkData(drink);
               });
           } catch (Exception e) {
               e.printStackTrace();
           }
        }).start();
    }

    protected abstract void loadDrinkData(Drink drink);

    protected final void loadDishes(List<Dish> dishes, TypeFood typeDish) {
        try {
            getCbo_dish().removeAllItems();
            getCbo_dish().addItem("Seleccione el plato");

            for (Dish dish : dishes) {
                String name = dish.getName();
                TypeFood type = dish.getTypeDish();

                if (typeDish.equals(type)) {
                    getCbo_dish().addItem(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final void loadDrinks(List<Drink> drinks, TypeFood typeDrink) {
        try {
            getCbo_drink().removeAllItems();
            getCbo_drink().addItem("Seleccione la bebida");

            for (Drink drink : drinks) {
                String name = drink.getName();
                TypeFood type = drink.getTypeDrink();

                if (typeDrink.equals(type)) {
                    getCbo_drink().addItem(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
