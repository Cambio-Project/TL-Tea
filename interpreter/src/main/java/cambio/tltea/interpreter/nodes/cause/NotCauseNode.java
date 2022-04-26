package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangeListener;
import cambio.tltea.interpreter.nodes.TriggerNotifier;

/**
 * @author Lion Wagner
 */
public final class NotCauseNode extends CauseNode implements StateChangeListener<Boolean> {

    private final CauseNode child;

    public NotCauseNode(CauseNode child) {
        this.child = child;
        child.subscribe(this);
    }

    @Override
    public Boolean getCurrentValue() {
        return !child.getCurrentValue();
    }

    @Override
    public void onEvent(StateChangeEvent<Boolean> event) {
        this.notifySubscribers(new StateChangeEvent<>(this, !event.getNewValue(), !event.getOldValue(), event.when()));
    }
}
