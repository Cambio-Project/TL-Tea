package cambio.tltea.interpreter.connector.value.predicates

import cambio.tltea.parser.core.temporal.TimeInstance

interface ITemporalUniOperatorPredicate<T:Comparable<T>> : ITemporalPredicate<T> {
    fun setValue(value: T, time: TimeInstance): ITemporalUniOperatorPredicate<T>
}