package model.entities;

public class QualificationDrink extends Qualification {

    private int idDrink;

    public QualificationDrink() {

    }

    @Override
    public int getIdDrink() {
        return idDrink;
    }

    @Override
    public void setIdDrink(int idDrink) {
        this.idDrink = idDrink;
    }

}
