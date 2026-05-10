package controller.customers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.dao.impl.DAOCustomersImpl;
import model.dao.impl.DAODishImpl;
import model.dao.impl.DAODrinkImpl;
import model.dao.impl.DAOSaleDetailImpl;
import model.dao.impl.DAOSalesImpl;
import model.dao.interfaces.DAORestaurantDb;
import model.dao.interfaces.DAOWithAutoIncrement;
import model.dao.interfaces.IDAO;
import model.entities.Customer;
import model.entities.Dish;
import model.entities.Drink;
import model.entities.Sale;
import model.entities.SaleDetail;
import model.enums.TypeFood;
import observer.Observer;

/**
 *
 * @author Andres Cardenas
 */
public abstract class AbstractFoodController implements Observer {

    protected Dish dish;
    protected Drink drink;
    protected float totalSale;

    private final DAORestaurantDb<Dish> daoDish;
    private final DAORestaurantDb<Drink> daoDrink;
    private final IDAO<Customer> daoCustomer;
    private final DAOWithAutoIncrement<Sale> daoSales;
    private final IDAO<SaleDetail> daoSaleDetail;

    private static class OrderData {
        final String dishName;
        final int dishAmount;
        final float dishPrice;
        final String drinkName;
        final int drinkAmount;
        final float drinkPrice;

        OrderData(String dishName, int dishAmount, float dishPrice, 
                 String drinkName, int drinkAmount, float drinkPrice) {
            this.dishName = dishName;
            this.dishAmount = dishAmount;
            this.dishPrice = dishPrice;
            this.drinkName = drinkName;
            this.drinkAmount = drinkAmount;
            this.drinkPrice = drinkPrice;
        }
    }

    public AbstractFoodController() {
        this.daoDish = DAODishImpl.getInstance();
        this.daoDrink = DAODrinkImpl.getInstance();
        this.daoCustomer = DAOCustomersImpl.getInstance();
        this.daoSales = DAOSalesImpl.getInstance();
        this.daoSaleDetail = DAOSaleDetailImpl.getInstance();
    }

    @Override
    public void update() {
        new Thread(() -> {
            try {
                List<Dish> dishes = daoDish.getData();
                List<Drink> drinks = daoDrink.getData();

                SwingUtilities.invokeLater(() -> {
                    loadDishes(dishes, getTypeFood());
                    loadDrinks(drinks, getTypeFood());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    protected abstract JPanel getJpl_contentFood();

    protected final void addOrder() {
        String dishAmount = getTxt_amountDish().getText().trim();
        String drinkAmount = getTxt_amountDrink().getText().trim();

        if (!validateOrderSelection()) {
            return;
        }

        if (!validateAmounts(dishAmount, drinkAmount)) {
            return;
        }

        OrderData orderData = prepareOrderData(dishAmount, drinkAmount);
        loadOrderInBackground(orderData);
    }

    private boolean validateOrderSelection() {
        boolean dishSelected = !noDishSelected();
        boolean drinkSelected = !noDrinkSelected();

        if (!dishSelected && !drinkSelected) {
            showWarningMessage("Seleccione un plato o una bebida.");
            return false;
        }
        return true;
    }

    private boolean validateAmounts(String dishAmount, String drinkAmount) {
        boolean dishSelected = !noDishSelected();
        boolean drinkSelected = !noDrinkSelected();

        if (dishSelected && dishAmount.isBlank()) {
            showWarningMessage("Ingrese la cantidad del plato.");
            return false;
        }

        if (drinkSelected && drinkAmount.isBlank()) {
            showWarningMessage("Ingrese la cantidad de la bebida.");
            return false;
        }
        return true;
    }

    private OrderData prepareOrderData(String dishAmount, String drinkAmount) {
        boolean dishSelected = !noDishSelected();
        boolean drinkSelected = !noDrinkSelected();

        String dishName = null;
        int dishAmountValue = 0;
        float dishPrice = 0;

        if (dishSelected) {
            dishName = (String) getCbo_dish().getSelectedItem();
            dishAmountValue = Integer.parseInt(dishAmount);
            dishPrice = Float.parseFloat(getTxt_priceDish().getText().trim());
        }

        String drinkName = null;
        int drinkAmountValue = 0;
        float drinkPrice = 0;

        if (drinkSelected) {
            drinkName = (String) getCbo_drink().getSelectedItem();
            drinkAmountValue = Integer.parseInt(drinkAmount);
            drinkPrice = Float.parseFloat(getTxt_priceDrink().getText().trim());
        }

        return new OrderData(dishName, dishAmountValue, dishPrice, drinkName, drinkAmountValue, drinkPrice);
    }

    private void loadOrderInBackground(OrderData orderData) {
        new Thread(() -> {
            try {
                Dish dish = orderData.dishName != null ? daoDish.getData(orderData.dishName) : null;
                Drink drink = orderData.drinkName != null ? daoDrink.getData(orderData.drinkName) : null;

                SwingUtilities.invokeLater(() -> {
                    processLoadedOrder(dish, orderData.dishAmount, orderData.dishPrice, drink, orderData.drinkAmount, orderData.drinkPrice);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void processLoadedOrder(Dish dish, int dishAmount, float dishPrice, 
                                    Drink drink, int drinkAmount, float drinkPrice) {
        if (isDishOrDrinkAlreadyAdded(dish, drink)) {
            showDuplicateWarning(dish, drink);
            return;
        }

        loadDataTable(dish, dishAmount, dishPrice, drink, drinkAmount, drinkPrice);
    }

    private void showDuplicateWarning(Dish dish, Drink drink) {
        String errorMsg = "";

        if (dish != null && isDishAlreadyInTable(dish.getCode())) {
            errorMsg = "El plato \"" + dish.getName() + "\" ya se encuentra en la tabla de pedidos.";
        } else if (drink != null && isDrinkAlreadyInTable(drink.getCode())) {
            errorMsg = "La bebida \"" + drink.getName() + "\" ya se encuentra en la tabla de pedidos.";
        }

        showWarningMessage(errorMsg);
    }

    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(getJpl_contentFood(), message, null, JOptionPane.WARNING_MESSAGE);
    }

    protected boolean noDishSelected() {
        return getCbo_dish().getSelectedIndex() == 0;
    }

    protected boolean noDrinkSelected() {
        return getCbo_drink().getSelectedIndex() == 0;
    }

    protected final void loadDataTable(Dish dish, int amountDish, float priceDish, Drink drink, int amountDrink, float priceDrink) {
        float totalOrder = (amountDish * priceDish) + (amountDrink * priceDrink);

        int dishCode = dish != null ? dish.getCode() : 0;
        String dishName = dish != null ? dish.getName() : "No seleccionado";

        int drinkCode = drink != null ? drink.getCode() : 0;
        String drinkName = drink != null ? drink.getName() : "No seleccionada";

        Object[] data = new Object[]{dishName, dishCode, amountDish, drinkName, drinkCode, amountDrink, totalOrder};
        getTableModelFood().addRow(data);
        this.totalSale += totalOrder;
        getTxt_totalSale().setText(String.valueOf(this.totalSale));
    }

    protected boolean isDishOrDrinkAlreadyAdded(Dish dish, Drink drink) {
        if (dish != null && isDishAlreadyInTable(dish.getCode())) {
            return true;
        }

        if (drink != null && isDrinkAlreadyInTable(drink.getCode())) {
            return true;
        }

        return false;
    }

    protected boolean isDishAlreadyInTable(int dishCode) {
        int rows = getJtbl_food().getRowCount();

        for (int i = 0; i < rows; i++) {
            int currentDishCode = Integer.parseInt(getJtbl_food().getValueAt(i, 1).toString());

            if (currentDishCode == dishCode && dishCode != 0) {
                return true;
            }
        }

        return false;
    }

    protected boolean isDrinkAlreadyInTable(int drinkCode) {
        int rows = getJtbl_food().getRowCount();

        for (int i = 0; i < rows; i++) {
            int currentDrinkCode = Integer.parseInt(getJtbl_food().getValueAt(i, 4).toString());

            if (currentDrinkCode == drinkCode && drinkCode != 0) {
                return true;
            }
        }

        return false;
    }

    protected void registerSale() {
        try {
            carryOutTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final void carryOutTransaction() {
        /*String customerName = getTxt_customerName().getText();
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
        }).start();*/
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
            int currentDishCode = Integer.parseInt(getJtbl_food().getValueAt(i, 1).toString());
            amountDish = getJtbl_food().getValueAt(i, 2).toString();
            drinkName = getJtbl_food().getValueAt(i, 3).toString();
            int currentDrinkCode = Integer.parseInt(getJtbl_food().getValueAt(i, 4).toString());
            amountDrink = getJtbl_food().getValueAt(i, 5).toString();
            float saleSubtotal = Float.parseFloat(getJtbl_food().getValueAt(i, 6).toString());

            addDetailsIfValid(details, dishName, amountDish, "No seleccionado");
            addDetailsIfValid(details, drinkName, amountDrink, "No seleccionada");

            addSale(customerId, currentDishCode, Integer.parseInt(amountDish), currentDrinkCode, Integer.parseInt(amountDrink), saleSubtotal);
        }
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
                int numSale = daoSales.saveAndGetId(sale);
                saveSaleDetail(sale.getSaleNumber(), dishCode, amountDish, drinkCode, amountDrink, saleSubtotal);
                SwingUtilities.invokeLater(this::clearDataSale);
            } catch (Exception e) {
                e.printStackTrace();
            }
         }).start();
    }

    protected final void saveSaleDetail(int saleId, int dishCode, int amountDish, int drinkCode, int amountDrink, float saleSubtotal) {
        try {
            SaleDetail saleDetail = new SaleDetail();
            saleDetail.setSaleId(saleId);
            saleDetail.setDishId(dishCode > 0 ? dishCode : null);
            saleDetail.setDrinkId(drinkCode > 0 ? drinkCode : null);
            saleDetail.setDishAmount(amountDish > 0 ? amountDish : null);
            saleDetail.setDrinkAmount(amountDrink > 0 ? amountDrink : null);
            saleDetail.setDishPrice(new java.math.BigDecimal(saleSubtotal));
            daoSaleDetail.save(saleDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void assignValuesSale(Sale sale, String customerId, int dishCode, int amountDish, int drinkCode, int amountDrink, float saleSubtotal) {
        sale.setClientId(Integer.parseInt(customerId));
        sale.setTotal(saleSubtotal);
        sale.setDate(LocalDate.now());
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
