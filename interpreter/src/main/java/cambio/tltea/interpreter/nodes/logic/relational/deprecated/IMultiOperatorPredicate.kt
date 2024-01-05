package cambio.tltea.interpreter.nodes.logic.relational.deprecated

interface IMultiOperatorPredicate<T: Comparable<T>> : IPredicate<T> {
    fun setValue(index: Int, value: T): IMultiOperatorPredicate<T>
}