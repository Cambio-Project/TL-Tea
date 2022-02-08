package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangeListener;

/**
 * @author Lion Wagner
 */
public final class NotInteractionNode extends InteractionNode<Boolean> implements StateChangeListener<Boolean> {

    private final InteractionNode<Boolean> child;

    public NotInteractionNode(InteractionNode<Boolean> child) {
        this.child = child;
        child.subscribe(this);
    }

    @Override
    public Boolean getValue() {
        return !child.getValue();
    }

    @Override
    public void onEvent(StateChangeEvent<Boolean> event) {
        this.notifySubscribers(new StateChangeEvent<>(this, !event.getNewValue(), !event.getOldValue()));
    }
}
