package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangeListener;
import cambio.tltea.interpreter.nodes.TriggerNotifier;

/**
 * @author Lion Wagner
 */
public class ImplicationNode extends InteractionNode<Boolean> implements StateChangeListener<Boolean> {

    private final InteractionNode<Boolean> cause;
    private final String consequence;
    private final TriggerNotifier notifier;

    public ImplicationNode(InteractionNode<Boolean> cause, String consequence, TriggerNotifier notifier) {
        this.cause = cause;
        this.consequence = consequence;
        this.notifier = notifier;

        cause.subscribe(this);
    }

    @Override
    public void onEvent(StateChangeEvent<Boolean> event) {
        if (event.getNewValue()) {
            notifier.trigger(consequence);
        }
    }

    /**
     * @return whether the cause expression is satisfied
     */
    @Override
    public Boolean getValue() {
        return cause.getValue();
    }
}
