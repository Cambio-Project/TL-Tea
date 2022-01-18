package cambio.tltea.interpreter.nodes.requirements;

public abstract class IValueDescription<T> extends InteractionNode<T> {
    public abstract T getValue();
}
