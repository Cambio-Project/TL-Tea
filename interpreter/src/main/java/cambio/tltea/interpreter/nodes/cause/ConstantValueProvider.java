package cambio.tltea.interpreter.nodes.cause;

/**
 * @author Lion Wagner
 */
public class ConstantValueProvider<T> extends ValueProvider<T> {

    public ConstantValueProvider(T value) {
        super();
        this.currentValue = value;
    }

}
