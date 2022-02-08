package cambio.tltea.interpreter.nodes;

import cambio.tltea.interpreter.nodes.StateChangeEvent;

@FunctionalInterface
public interface StateChangeListener<T>{
    void onEvent(StateChangeEvent<T> event);
}
