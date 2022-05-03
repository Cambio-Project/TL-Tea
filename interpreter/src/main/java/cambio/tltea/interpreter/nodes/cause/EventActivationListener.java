package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.parser.core.temporal.ITemporalValue;

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
        notifyAndChangeState(true, time);
    }

    public void reset(ITemporalValue time) {
        deactivate(time);
    }

    public void deactivate(ITemporalValue time) {
        notifyAndChangeState(false, time);
    }
}
