package utils;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Andres Cardenas
 */
public class NavigatorUtils {
    
    public static void showFormManagement(JPanel content, JComponent component) {
        content.removeAll();
        content.setLayout(new BorderLayout());
        content.add(component, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }
    
}
