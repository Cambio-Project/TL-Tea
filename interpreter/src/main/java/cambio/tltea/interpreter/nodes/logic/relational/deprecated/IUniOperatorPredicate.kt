package cambio.tltea.interpreter.nodes.logic.relational.deprecated

interface IUniOperatorPredicate<T:Comparable<T>> : IPredicate<T> {
    fun setValue(value: T): IUniOperatorPredicate<T>
}