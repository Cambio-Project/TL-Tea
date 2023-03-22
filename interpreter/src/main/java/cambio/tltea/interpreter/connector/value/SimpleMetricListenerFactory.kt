package cambio.tltea.interpreter.connector.value

/**
 * Responsible for creating and registering new instances of [SimpleMetricListener].
 */
class SimpleMetricListenerFactory(
) {
    var registrationStrategy: IMetricRegistrationStrategy = NullMetricRegistrationStrategy()

    fun createInstance(descriptor: MetricDescriptor): SimpleMetricListener<Double> {
        val listener = SimpleMetricListener<Double>()
        this.registrationStrategy.register(listener, descriptor)
        return listener
    }

    class NullMetricRegistrationStrategy : IMetricRegistrationStrategy {
        override fun register(listener: IMetricListener<Double>, descriptor: MetricDescriptor) {
            // do nothing
        }

    }
}