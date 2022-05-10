package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.parser.core.temporal.ITemporalValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lion Wagner
 */
public final class EventActivationListener extends ValueProvider<Boolean> {

    private final String eventName;

    public EventActivationListener(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public boolean isActivated() {
        return currentValue;
    }


    public void setActivated(ITemporalValue time) {
        activate(time);
    }

    public void activate(ITemporalValue time) {
        changeStateAndNotify(true, time);
    }

    public void reset(ITemporalValue time) {
        deactivate(time);
    }

    public void deactivate(ITemporalValue time) {
        changeStateAndNotify(false, time);
    }

    public @NotNull EventActivationListener clone() {
        return new EventActivationListener(eventName);
    }
}
