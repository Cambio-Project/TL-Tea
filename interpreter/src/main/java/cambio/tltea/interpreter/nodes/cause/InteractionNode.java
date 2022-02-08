package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangedPublisher;

/**
 * @author Lion Wagner
 */
public abstract class InteractionNode<T> extends StateChangedPublisher<T> {
    public abstract T getValue();
}
