package model.strategy;

/**
 *
 * @author hectoracevedo
 */
public class RestaurantDAOManager<T> {

    private Strategy<T> strategy;

    public RestaurantDAOManager(Strategy<T> strategy) {
        this.strategy = strategy;
    }

    public void setStrategy (Strategy<T> strategy) {
        this.strategy = strategy;
    }

    public void save (T data) throws Exception {
        strategy.save(data);
    }

    public void modify(T data) throws Exception {
        strategy.modify(data);
    }

    public String nameStrategy() {
        return strategy.nameStrategy();
    }

}
