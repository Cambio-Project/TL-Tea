package cambio.tltea.interpreter.connector.value.predicates

interface IUniOperatorPredicate<T:Comparable<T>> : IPredicate<T> {
    fun setValue(value: T): IUniOperatorPredicate<T>
}