package cambio.tltea.interpreter.connector.value

import cambio.tltea.parser.core.temporal.TimeInstance

interface IMetricSubscriber<T> {
    fun update(value:T, time:TimeInstance)
}