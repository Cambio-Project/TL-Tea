package cambio.tltea.interpreter.nodes.requirements;

import cambio.tltea.interpreter.nodes.StateChangeEvent;

@FunctionalInterface
public interface InteractionListener<T>{
    void onEvent(StateChangeEvent<T> event);
}
