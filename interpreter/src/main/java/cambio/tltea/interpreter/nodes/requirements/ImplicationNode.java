package cambio.tltea.interpreter.nodes.requirements;

import cambio.tltea.interpreter.nodes.StateChangeEvent;

/**
 * @author Lion Wagner
 */
public class ImplicationNode extends InteractionNode<Boolean> implements InteractionListener<Boolean> {

    private final InteractionNode<Boolean> requirement;
    private final String consequence;
    private final TriggerNotifier notifier;

    public ImplicationNode(InteractionNode<Boolean> requirement, String consequence, TriggerNotifier notifier) {
        this.requirement = requirement;
        this.consequence = consequence;
        this.notifier = notifier;

        requirement.subscribe(this);
    }

    @Override
    public void onEvent(StateChangeEvent<Boolean> event) {
        if (event.getNewValue()) {
            notifier.trigger(consequence);
        }
    }

    /**
     * @return whether the requirement is satisfied
     */
    @Override
    public Boolean getValue() {
        return requirement.getValue();
    }
}
