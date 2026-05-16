package observer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andres Cardenas
 */
public class ConcreteSubject implements Subject {

    private static ConcreteSubject instance;
    private final List<Observer> list = new ArrayList<>();

    private ConcreteSubject() {

    }

    public static ConcreteSubject getInstance() {
        if (instance == null) {
            instance = new ConcreteSubject();
        }

        return instance;
    }

    @Override
    public void register(Observer observer) {
        list.add(observer);
    }

    @Override
    public void delete(Observer observer) {
        list.remove(observer);
    }

    @Override
    public void notifyObserver() {
        list.forEach(Observer::update);
    }

}
