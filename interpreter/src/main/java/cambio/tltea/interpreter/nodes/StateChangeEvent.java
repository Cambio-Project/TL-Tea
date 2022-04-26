package cambio.tltea.interpreter.nodes;

import cambio.tltea.parser.core.temporal.ITemporalValue;

/**
 * @author Lion Wagner
 */
public record StateChangeEvent<T>(StateChangedPublisher<T> publisher,
                                  T newValue, T oldValue, ITemporalValue when) {


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
