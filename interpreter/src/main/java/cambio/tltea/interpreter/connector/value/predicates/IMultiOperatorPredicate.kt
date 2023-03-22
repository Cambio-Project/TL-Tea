package cambio.tltea.interpreter.connector.value.predicates

interface IMultiOperatorPredicate<T: Comparable<T>> : IPredicate<T> {
    fun setValue(index: Int, value: T): IMultiOperatorPredicate<T>
}