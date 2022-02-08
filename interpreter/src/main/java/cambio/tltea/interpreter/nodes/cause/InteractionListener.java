package cambio.tltea.interpreter.nodes.cause;

import cambio.tltea.interpreter.nodes.StateChangeEvent;

@FunctionalInterface
public interface InteractionListener<T>{
    void onEvent(StateChangeEvent<T> event);
}
