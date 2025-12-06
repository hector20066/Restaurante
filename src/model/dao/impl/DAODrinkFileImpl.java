package model.dao.impl;

import model.dao.interfaces.DAORestaurantFile;
import model.entities.Drink;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author hectoracevedo
 */
public class DAODrinkFileImpl implements DAORestaurantFile<Drink> {

    private String filePath;
    private static DAODrinkFileImpl instance;

    private DAODrinkFileImpl() {

    }

    public static DAODrinkFileImpl getInstance() {
        if (instance == null) {
            instance = new DAODrinkFileImpl();
        }

        return instance;
    }

    @Override
    public void setFilePath(String path) {
        this.filePath = path;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void createFile(String filePath) throws IOException, ClassNotFoundException {
        this.filePath = filePath;
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    public void save(Drink data) throws IOException, ClassNotFoundException {
        File file = new File(getFilePath());
        BufferedWriter writer;

        if (file.exists()) {
            writer = new BufferedWriter(new FileWriter(file, true));
        } else {
            writer = new BufferedWriter(new FileWriter(file));
        }

        writerData(data, writer);
        writer.close();
    }

    @Override
    public synchronized Drink getData(String name) throws IOException, ClassNotFoundException {
        List<Drink> list = getData();
        Drink drink = null;

        for (Drink drinks : list) {
            if (drinks.getName().equals(name)) {
                drink = drinks;
                break;
            }
        }

        return drink;
    }

    @Override
    public synchronized List<Drink> getData() throws IOException, ClassNotFoundException {
        List<Drink> list = new ArrayList<>();
        File file = new File(getFilePath());
        FileReader reader = new FileReader(file);

        try (BufferedReader read = new BufferedReader(reader)) {
            String line;

            while ((line = read.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ";");

                if (st.countTokens() >= 5) {
                    int code = Integer.parseInt(st.nextToken().trim());
                    String name = st.nextToken().trim();
                    String description = st.nextToken().trim();
                    String typeDish = st.nextToken().trim();
                    float price = Float.parseFloat(st.nextToken().trim());

                    Drink drink = new Drink() {
                        @Override
                        public String getTypeDrink() {
                            return typeDish;
                        }
                    };

                    assignValuesDrink(drink, code, name, description, price);
                    list.add(drink);
                }
            }
        }

        return list;
    }

    private void assignValuesDrink(Drink drink, int code, String name, String description, float price) {
        drink.setCode(code);
        drink.setName(name);
        drink.setDescription(description);
        drink.setPrice(price);
    }

    @Override
    public void delete(String name) throws IOException, ClassNotFoundException {
        List<Drink> list = getData();
        boolean found = false;

        for (Drink drink : list) {
            if (drink.getName().equals(name)) {
                list.remove(drink);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IOException("No se ha encontrado el plato");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Drink drink : list) {
                writerData(drink, writer);
            }
        }
    }

    private void writerData(Drink data, BufferedWriter writer) throws IOException {
        writer.write(data.getCode() + ";");
        writer.write(data.getName() + ";");
        writer.write(data.getDescription() + ";");
        writer.write(data.getTypeDrink() + ";");
        writer.write(String.valueOf(data.getPrice()));
        writer.newLine();
    }

}
