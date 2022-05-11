package cambio.tltea.interpreter.nodes.cause;

/**
 * @author Lion Wagner
 */
public class ConstantValueProvider<T> extends ValueListener<T> {

    public ConstantValueProvider(T value) {
        super("CONSTANT");
        this.currentValue = value;
    }

    public ConstantValueProvider<T> clone() {
        return new ConstantValueProvider<>(currentValue);
    }
}
