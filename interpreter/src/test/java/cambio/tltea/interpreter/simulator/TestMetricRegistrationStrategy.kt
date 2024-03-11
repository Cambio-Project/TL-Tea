package cambio.tltea.interpreter.simulator

import cambio.tltea.interpreter.connector.value.IMetricListener
import cambio.tltea.interpreter.connector.value.IMetricRegistrationStrategy
import cambio.tltea.interpreter.connector.value.MetricDescriptor

class TestMetricRegistrationStrategy(private val simulator: TestEventSimulator) : IMetricRegistrationStrategy {
    override fun register(listener: IMetricListener<*>, descriptor: MetricDescriptor) {
        if (descriptor.boolean) {
            simulator.addBooleanListener(descriptor, listener as IMetricListener<Boolean>)
        } else {
            simulator.addListener(descriptor, listener as IMetricListener<Double>)
        }
    }
}