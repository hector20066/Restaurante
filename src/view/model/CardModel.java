package view.model;

import javax.swing.*;

/**
 *
 * @author hectoracevedo
 */
public class CardModel {
    
    private Icon icon;
    private String title;
    private String values;
    private String description;
    
    public CardModel(Icon icon, String titulo, String valores, String descripcion){
        this.icon = icon;
        this.title = titulo;
        this.values = valores;
        this.description = descripcion;
    }
    
    public CardModel(){
        
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getValues() {
        return values;
    }

    public String getDescription() {
        return description;
    }
    
}
