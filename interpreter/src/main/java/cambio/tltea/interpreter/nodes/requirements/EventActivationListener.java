package cambio.tltea.interpreter.nodes.requirements;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;

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


    public void setActivated() {
        activate();
    }

    public void activate() {
        notifyAndChangeState(true);
    }

    public void reset() {
        deactivate();
    }

    public void deactivate() {
        notifyAndChangeState(false);
    }

    private void notifyAndChangeState(boolean activated) {
        subscribers.forEach(listener -> listener.onEvent(new StateChangeEvent<>(this, activated, this.activated)));
        this.activated = activated;
    }
}
