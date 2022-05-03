package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.parser.core.temporal.ITemporalValue;

/**
 * @author Lion Wagner
 */
public class ValueListener<T> extends ValueProvider<T> {

    public void updateValue(T value, ITemporalValue time) {
        super.changeStateAndNotify(value, time);
    }
}
