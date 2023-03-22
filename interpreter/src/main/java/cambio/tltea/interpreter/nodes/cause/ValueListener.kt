package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;
import cambio.tltea.parser.core.temporal.ITemporalValue;

public class ValueListener<T> extends StateChangedPublisher<T> {
    private boolean isListening;
    protected T currentValue;

    protected final String valueOrEventName;

    public ValueListener(String valueOrEventName) {
        this.valueOrEventName = valueOrEventName;
    }

    protected void changeStateAndNotify(T newValue, ITemporalValue time) {
        if (!isListening) {
            return;
        }
        this.currentValue = newValue;
        subscribers.forEach(listener -> listener.onEvent(new StateChangeEvent<>(this,
                                                                                newValue,
                                                                                currentValue,
                                                                                time)));
    }


    public void updateValue(T value, ITemporalValue time) {
        changeStateAndNotify(value, time);
    }

    public void startListening() {
        isListening = true;
    }

    public void stopListening() {
        isListening = false;
    }

    public String getValueOrEventName() {
        return valueOrEventName;
    }

    public ValueListener<T> clone() {
        return new ValueListener<>(valueOrEventName);
    }
}
