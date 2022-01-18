package cambio.tltea.interpreter.nodes.requirements;

@FunctionalInterface
public interface ITriggerListener {
    void onTrigger(String eventName, Object... args);
}
