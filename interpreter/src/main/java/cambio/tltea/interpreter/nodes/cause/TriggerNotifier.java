package cambio.tltea.interpreter.nodes.cause;

import java.util.Collection;
import java.util.HashSet;

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

    void trigger(String eventName, Object... args) {
        for (ITriggerListener listener : subscribers) {
            listener.onTrigger(eventName, args);
        }
    }
}
