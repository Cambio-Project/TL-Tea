package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;
import cambio.tltea.parser.core.temporal.ITemporalValue;

/**
 * @author Lion Wagner
 */
public final class EventActivationListener extends StateChangedPublisher<Boolean> {

    private final String eventName;

    private boolean activated = false;

    public EventActivationListener(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public boolean isActivated() {
        return activated;
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

    private void notifyAndChangeState(boolean activated, ITemporalValue time) {
        subscribers.forEach(listener -> listener.onEvent(new StateChangeEvent<>(this,
                                                                                activated,
                                                                                this.activated,
                                                                                time)));
        this.activated = activated;
    }
}
