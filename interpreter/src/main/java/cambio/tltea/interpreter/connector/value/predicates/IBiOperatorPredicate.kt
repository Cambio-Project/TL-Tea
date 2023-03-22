package cambio.tltea.interpreter.connector.value.predicates

interface IBiOperatorPredicate<T: Comparable<T>> : IPredicate<T> {
    fun setLeftValue(value: T): IBiOperatorPredicate<T>
    fun setRightValue(value: T): IBiOperatorPredicate<T>
}