package cambio.tltea.interpreter.nodes.requirements;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Lion Wagner
 */
public class OrInteractionNode extends InteractionNode<Boolean> implements InteractionListener<Boolean> {

    private boolean state = false;

    private final LinkedList<StateChangedPublisher<Boolean>> children = new LinkedList<>();

    private final AtomicInteger trueCount = new AtomicInteger(0);

    public OrInteractionNode(InteractionNode<Boolean>... children) {
        for (var child : children) {
            this.children.add(child);
            child.subscribe(this);

            if (child.getValue()) {
                trueCount.incrementAndGet();
            }
        }
        state = trueCount.get() > 0;
    }

    @Override
    public void onEvent(StateChangeEvent<Boolean> event) {
        if (event.getNewValue() != event.getOldValue()) {
            if (event.getNewValue()) {
                trueCount.incrementAndGet();
            } else {
                trueCount.decrementAndGet();
            }
            boolean oldSate = state;
            state = trueCount.get() > 0;

            if (oldSate != state) {
                notifySubscribers(new StateChangeEvent<>(this, true, false));
            }
        }
    }

    @Override
    public Boolean getValue() {
        return state;
    }
}
