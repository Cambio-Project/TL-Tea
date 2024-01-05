package cambio.tltea.interpreter.nodes.logic.relational.deprecated

import cambio.tltea.parser.core.temporal.TimeInstance

interface ITemporalUniOperatorPredicate<T:Comparable<T>> : ITemporalPredicate<T> {
    fun setValue(value: T, time: TimeInstance): ITemporalUniOperatorPredicate<T>
}