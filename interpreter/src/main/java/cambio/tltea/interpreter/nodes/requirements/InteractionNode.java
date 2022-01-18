package cambio.tltea.interpreter.nodes.requirements;

import cambio.tltea.interpreter.nodes.StateChangedPublisher;

/**
 * @author Lion Wagner
 */
public abstract class InteractionNode<T> extends StateChangedPublisher<T> {
    public abstract T getValue();
}
