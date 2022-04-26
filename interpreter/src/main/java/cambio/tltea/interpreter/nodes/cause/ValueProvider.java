package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangedPublisher;
import cambio.tltea.interpreter.nodes.TriggerNotifier;

public abstract class ValueProvider<T> extends StateChangedPublisher<T> {
    public abstract T getCurrentValue();

    public ValueProvider() {
    }
}
