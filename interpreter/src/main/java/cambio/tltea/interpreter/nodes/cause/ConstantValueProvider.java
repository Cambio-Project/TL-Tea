package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.TriggerNotifier;

/**
 * @author Lion Wagner
 */
public class ConstantValueProvider<T>  extends ValueProvider<T> {

    protected final T value;

    public ConstantValueProvider(T value) {
        super();
        this.value = value;
    }

    @Override
    public T getCurrentValue() {
        return value;
    }

}
