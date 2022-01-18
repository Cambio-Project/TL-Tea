package cambio.tltea.interpreter.nodes.requirements;

import cambio.tltea.interpreter.nodes.StateChangeEvent;

/**
 * @author Lion Wagner
 */
public final class NotInteractionNode extends InteractionNode<Boolean> implements InteractionListener<Boolean> {

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
