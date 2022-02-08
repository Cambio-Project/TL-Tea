package cambio.tltea.interpreter.nodes.cause;


import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangeListener;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lion Wagner
 */
public final class ActivatableEvent extends IValueDescription<Boolean> implements StateChangeListener<Boolean> {

    private final EventActivationListener listener;

    public ActivatableEvent(String eventName) {
        listener = new EventActivationListener(eventName);
        listener.subscribe(this);
    }

    public EventActivationListener getEventListener() {
        return listener;
    }

    @Override
    public @NotNull Boolean getValue() {
        return listener.isActivated();
    }

    @Override
    public void onEvent(StateChangeEvent<Boolean> event) {
        notifySubscribers(new StateChangeEvent<>(this, event.getNewValue(), event.getOldValue()));
    }
}
