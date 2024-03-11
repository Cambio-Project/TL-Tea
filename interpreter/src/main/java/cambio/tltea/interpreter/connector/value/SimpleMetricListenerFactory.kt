package cambio.tltea.interpreter.connector.value

import cambio.tltea.interpreter.connector.time.TimeManager

/**
 * Responsible for creating and registering new instances of [SimpleMetricListener].
 */
class SimpleMetricListenerFactory(private val timeManager: TimeManager) {
    var registrationStrategy: IMetricRegistrationStrategy = NullMetricRegistrationStrategy()

    fun createDoubleInstance(descriptor: MetricDescriptor): SimpleMetricListener<Double> {
        val listener = SimpleMetricListener<Double>(timeManager)
        this.registrationStrategy.register(listener, descriptor)
        return listener
    }

    fun createBooleanInstance(descriptor: MetricDescriptor): SimpleMetricListener<Boolean> {
        val listener = SimpleMetricListener<Boolean>(timeManager)
        this.registrationStrategy.register(listener, descriptor)
        return listener
    }

    class NullMetricRegistrationStrategy : IMetricRegistrationStrategy {
        override fun register(listener: IMetricListener<*>, descriptor: MetricDescriptor) {
            // do nothing
        }

    }
}