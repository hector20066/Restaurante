package model.entities;

public class QualificationDish extends Qualification {

    private int idDish;

    public QualificationDish() {

    }

    @Override
    public int getIdDish() {
        return idDish;
    }

    @Override
    public void setIdDish(int idDish) {
        this.idDish = idDish;
    }

}
