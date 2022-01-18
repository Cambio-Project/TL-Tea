package cambio.tltea.interpreter.nodes;

/**
 * @author Lion Wagner
 */
public record StateChangeEvent<T>(StateChangedPublisher<T> publisher,
                                  T newValue, T oldValue) {

    public T getNewValue() {
        return newValue;
    }

    public T getOldValue() {
        return oldValue;
    }

    public StateChangedPublisher<T> getPublisher() {
        return publisher;
    }
}
