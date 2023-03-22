package cambio.tltea.interpreter.connector.value.predicates

interface IPredicate<T: Comparable<T>> {
    fun evaluate():Boolean
}