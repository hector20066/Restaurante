package main;

import com.formdev.flatlaf.FlatIntelliJLaf;
import controller.Coordinator;

import javax.swing.*;

/**
 *
 * @author Andres Cardenas
 */
public class Main {

    public static void main(String[] args) {
        try {
            FlatIntelliJLaf.setup();
            new Coordinator().startApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
