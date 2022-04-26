package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeListener;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;
import cambio.tltea.interpreter.nodes.TriggerNotifier;

/**
 * @author Lion Wagner
 */
public abstract class CauseNode extends StateChangedPublisher<Boolean> {

    public abstract Boolean getCurrentValue();

    public CauseNode() {
    }
}
