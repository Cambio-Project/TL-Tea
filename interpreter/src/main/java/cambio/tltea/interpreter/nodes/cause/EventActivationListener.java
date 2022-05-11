package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.parser.core.temporal.ITemporalValue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Lion Wagner
 */
public final class EventActivationListener extends ValueListener<Boolean> {

    public EventActivationListener(String eventName) {
        super(eventName);
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

    @Override
    public ValueListener<Boolean> clone() {
        return new EventActivationListener(valueOrEventName);
    }
}
