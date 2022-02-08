package cambio.tltea.interpreter.nodes.cause;

@FunctionalInterface
public interface ITriggerListener {
    void onTrigger(String eventName, Object... args);
}
