package cambio.tltea.interpreter.nodes.cause;


import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangeListener;
import cambio.tltea.interpreter.nodes.TriggerNotifier;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lion Wagner
 */
public final class EventActivationNode extends ValueProvider<Boolean> implements StateChangeListener<Boolean> {

    private final EventActivationListener listener;

    public EventActivationNode(String eventName) {
        listener = new EventActivationListener(eventName);
        listener.subscribe(this);
    }

    public EventActivationListener getEventListener() {
        return listener;
    }

    @Override
    public @NotNull Boolean getCurrentValue() {
        return listener.isActivated();
    }

    @Override
    public void onEvent(StateChangeEvent<Boolean> event) {
        notifySubscribers(new StateChangeEvent<>(this, event.getNewValue(), event.getOldValue(), event.when()));
    }
}
