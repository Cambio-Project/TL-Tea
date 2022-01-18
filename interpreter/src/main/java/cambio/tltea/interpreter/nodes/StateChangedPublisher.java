package cambio.tltea.interpreter.nodes;

import cambio.tltea.interpreter.nodes.requirements.InteractionListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Lion Wagner
 */
public abstract class StateChangedPublisher<T> {
    protected final Collection<InteractionListener<T>> subscribers = new HashSet<>();

    public final void subscribe(InteractionListener<T> listener) {
        subscribers.add(listener);
    }

    public final void unsubscribe(InteractionListener<T> listener) {
        subscribers.remove(listener);
    }

    protected final void notifySubscribers(StateChangeEvent<T> event) {
        for (InteractionListener<T> listener : subscribers) {
            listener.onEvent(event);
        }
    }
}
