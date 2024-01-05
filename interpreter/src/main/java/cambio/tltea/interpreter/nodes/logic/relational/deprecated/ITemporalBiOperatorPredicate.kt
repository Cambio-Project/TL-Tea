package cambio.tltea.interpreter.nodes.logic.relational.deprecated

import cambio.tltea.parser.core.temporal.TimeInstance

interface ITemporalBiOperatorPredicate<T:Comparable<T>> : ITemporalPredicate<T> {
    fun setLeftValue(value: T, time: TimeInstance): ITemporalBiOperatorPredicate<T>
    fun setRightValue(value: T, time: TimeInstance): ITemporalBiOperatorPredicate<T>
}