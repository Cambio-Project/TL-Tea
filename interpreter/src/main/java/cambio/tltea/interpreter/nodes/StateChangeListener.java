package cambio.tltea.interpreter.nodes;

import cambio.tltea.parser.core.temporal.ITemporalValue;

@FunctionalInterface
public interface StateChangeListener<T>{
    void onEvent(StateChangeEvent<T> event);
}
