package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;
import cambio.tltea.parser.core.temporal.ITemporalValue;

public abstract class ValueProvider<T> extends StateChangedPublisher<T> {
    private boolean isListening;
    protected T currentValue;

    protected void notifyAndChangeState(T newValue, ITemporalValue time) {
        if (!isListening) {
            return;
        }
        subscribers.forEach(listener -> listener.onEvent(new StateChangeEvent<>(this,
                                                                                newValue,
                                                                                currentValue,
                                                                                time)));
        this.currentValue = newValue;
    }


    public void startListening() {
        isListening = true;
    }

    public void stopListening() {
        isListening = false;
    }
}
