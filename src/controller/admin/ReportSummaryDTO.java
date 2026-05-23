package controller.admin;

public class ReportSummaryDTO {

    private String bestDish;
    private int qtyDish;
    private String bestDrink;
    private int qtyDrink;

    public ReportSummaryDTO() {

    }

    public ReportSummaryDTO(String bestDish, int qtyDish, String bestDrink, int qtyDrink) {
        this.bestDish = bestDish;
        this.qtyDish = qtyDish;
        this.bestDrink = bestDrink;
        this.qtyDrink = qtyDrink;
    }

    public String getBestDish() {
        return bestDish;
    }

    public void setBestDish(String bestDish) {
        this.bestDish = bestDish;
    }

    public int getQtyDish() {
        return qtyDish;
    }

    public void setQtyDish(int qtyDish) {
        this.qtyDish = qtyDish;
    }

    public String getBestDrink() {
        return bestDrink;
    }

    public void setBestDrink(String bestDrink) {
        this.bestDrink = bestDrink;
    }

    public int getQtyDrink() {
        return qtyDrink;
    }

    public void setQtyDrink(int qtyDrink) {
        this.qtyDrink = qtyDrink;
    }

}
