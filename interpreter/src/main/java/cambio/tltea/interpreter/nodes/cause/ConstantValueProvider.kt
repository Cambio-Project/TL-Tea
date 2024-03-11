package cambio.tltea.interpreter.nodes.cause

/**
 * @author Lion Wagner
 */
class ConstantValueProvider<T : Comparable<T>>(value : T) : ValueListener<T>("CONSTANT", value) {

    override fun clone(): ConstantValueProvider<T> {
        return ConstantValueProvider(currentValue!!)
    }
}