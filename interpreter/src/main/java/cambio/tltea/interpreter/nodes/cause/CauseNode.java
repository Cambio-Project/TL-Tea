package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangedPublisher;

/**
 * @author Lion Wagner
 */
public abstract class CauseNode extends StateChangedPublisher<Boolean> {

    public abstract Boolean getCurrentValue();

    public CauseNode() {
    }
}
