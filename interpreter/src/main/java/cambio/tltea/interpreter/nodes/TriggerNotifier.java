package cambio.tltea.interpreter.nodes;

import cambio.tltea.interpreter.nodes.cause.EventActivationListener;
import cambio.tltea.interpreter.nodes.cause.EventActivationNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author Lion Wagner
 */
public class TriggerNotifier {

    protected final Collection<ITriggerListener> subscribers = new HashSet<>();

    public final void subscribe(ITriggerListener listener) {
        subscribers.add(listener);
    }

    public final void unsubscribe(ITriggerListener listener) {
        subscribers.remove(listener);
    }

    public void trigger(String eventName, Object... args) {
        for (ITriggerListener listener : subscribers) {
            listener.onTrigger(eventName, args);
        }
    }

    public void activateListeners(@NotNull List<EventActivationListener> listeners) {

    }
}
