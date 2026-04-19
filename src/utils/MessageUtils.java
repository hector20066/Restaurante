package utils;

import javax.swing.*;

/**
 *
 * @author Andres Cardenas
 */
public class MessageUtils {

    public static void informationMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void errorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void warningMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public static void plainMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.PLAIN_MESSAGE);
    }
    
}
