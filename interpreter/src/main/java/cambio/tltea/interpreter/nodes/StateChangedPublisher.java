package cambio.tltea.interpreter.nodes;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Lion Wagner
 */
public abstract class StateChangedPublisher<T> {
    protected final Collection<StateChangeListener<T>> subscribers = new HashSet<>();

    public final void subscribe(StateChangeListener<T> listener) {
        subscribers.add(listener);
    }

    public final void unsubscribe(StateChangeListener<T> listener) {
        subscribers.remove(listener);
    }

    protected final void notifySubscribers(StateChangeEvent<T> event) {
        for (StateChangeListener<T> listener : subscribers) {
            listener.onEvent(event);
        }
    }
}
