package model.dao.impl;

import model.dao.interfaces.DAORestaurantFile;
import model.entities.Dish;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author hectoracevedo
 */
public class DAODishFileImpl implements DAORestaurantFile<Dish> {

    private String filePath;
    private static DAODishFileImpl instance;

    private DAODishFileImpl() {

    }

    public static DAODishFileImpl getInstance() {
        if (instance == null) {
            instance = new DAODishFileImpl();
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
    public void save(Dish data) throws IOException, ClassNotFoundException {
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

    private void writerData(Dish data, BufferedWriter writer) throws IOException {
        writer.write(data.getCode() + ";");
        writer.write(data.getName() + ";");
        writer.write(data.getDescription() + ";");
        writer.write(data.getTypeDish() + ";");
        writer.write(String.valueOf(data.getPrice()));
        writer.newLine();
    }

    @Override
    public synchronized Dish getData(String name) throws IOException, ClassNotFoundException {
        List<Dish> list = getData();
        Dish dish = null;

        for (Dish dishes : list) {
            if (dishes.getName().equals(name)) {
                dish = dishes;
                break;
            }
        }

        return dish;
    }

    @Override
    public synchronized List<Dish> getData() throws IOException, ClassNotFoundException {
        List<Dish> list = new ArrayList<>();
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

                    Dish dish = new Dish() {
                        @Override
                        public String getTypeDish() {
                            return typeDish;
                        }
                    };

                    assignValuesDish(dish, code, name, description, price);
                    list.add(dish);
                }
            }
        }

        return list;
    }

    private void assignValuesDish(Dish dish, int code, String name, String description, float price) {
        dish.setCode(code);
        dish.setName(name);
        dish.setDescription(description);
        dish.setPrice(price);
    }

    @Override
    public void delete(String name) throws IOException, ClassNotFoundException {
        List<Dish> list = getData();
        boolean found = false;

        for (Dish dish : list) {
            if (dish.getName().equals(name)) {
                list.remove(dish);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IOException("No se ha encontrado el plato");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Dish dish : list) {
                writerData(dish, writer);
            }
        }
    }

}
