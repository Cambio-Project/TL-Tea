package cambio.tltea.interpreter.nodes.logic.relational.deprecated

import cambio.tltea.parser.core.temporal.TimeInstance

interface ITemporalMultiOperatorPredicate<T:Comparable<T>> : ITemporalPredicate<T> {
    fun setValue(index: Int, value: T, time:TimeInstance): ITemporalMultiOperatorPredicate<T>
}