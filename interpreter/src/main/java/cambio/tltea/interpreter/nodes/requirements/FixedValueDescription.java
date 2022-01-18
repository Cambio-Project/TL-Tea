package cambio.tltea.interpreter.nodes.requirements;

/**
 * @author Lion Wagner
 */
public class FixedValueDescription<T>  extends IValueDescription<T> {

    protected final T value;

    public FixedValueDescription(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

}
