package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Lion Wagner
 */
public class AndInteractionNode extends InteractionNode<Boolean> implements InteractionListener<Boolean> {
    private boolean state = false;

    private final LinkedList<StateChangedPublisher<Boolean>> children = new LinkedList<>();

    private final AtomicInteger trueCount = new AtomicInteger(0);

    public AndInteractionNode(InteractionNode<Boolean>... children) {
        for (InteractionNode<Boolean> child : children) {
            this.children.add(child);
            child.subscribe(this);

            if (child.getValue()) {
                trueCount.incrementAndGet();
            }
        }
        state = trueCount.get() == this.children.size();
    }

    @Override
    public void onEvent(StateChangeEvent<Boolean> event) {
        if (event.getNewValue() != event.getOldValue()) { //stop bounce if there is no change
            if (event.getNewValue()) {
                trueCount.incrementAndGet();
            } else {
                trueCount.decrementAndGet();
            }
            boolean oldSate = state;
            state = trueCount.get() == this.children.size();

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
