package observer;

/**
 *
 * @author Andres Cardenas
 */
public interface Subject {

    void register(Observer observer);

    void delete(Observer observer);

    void notifyObserver();

}
