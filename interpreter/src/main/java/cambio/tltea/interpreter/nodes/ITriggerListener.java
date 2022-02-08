package cambio.tltea.interpreter.nodes;

@FunctionalInterface
public interface ITriggerListener {
    void onTrigger(String eventName, Object... args);
}
