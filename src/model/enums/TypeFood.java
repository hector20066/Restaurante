package model.enums;

public enum TypeFood {

    CHINESE("China"),
    ITALIAN("Italiana"),
    COLOMBIAN("Colombiana");

    private final String value;

    TypeFood(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
