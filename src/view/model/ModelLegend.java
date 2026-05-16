package view.model;

import java.awt.*;

/**
 *
 * @author Andres Cardenas
 */
public class ModelLegend {

    private String name;
    private Color color;

    public ModelLegend() {

    }

    public ModelLegend(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
