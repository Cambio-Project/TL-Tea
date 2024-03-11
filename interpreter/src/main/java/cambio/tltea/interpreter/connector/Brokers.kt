package cambio.tltea.interpreter.connector

import cambio.tltea.interpreter.connector.time.TimeManager
import cambio.tltea.interpreter.connector.time.TimedEventManager
import cambio.tltea.interpreter.connector.value.MetricBroker

class Brokers {
    val timeManager : TimeManager = TimeManager()
    val metricBroker: MetricBroker = MetricBroker(timeManager)
    val timedEventManager : TimedEventManager = TimedEventManager()
}