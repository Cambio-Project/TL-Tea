package cambio.tltea.interpreter.connector.value

import cambio.tltea.interpreter.connector.time.TimeManager
import cambio.tltea.interpreter.nodes.cause.ValueListener

class MetricBroker(private val timeManager: TimeManager) {
    private val registeredMetricListeners: MutableMap<MetricDescriptor, IMetricListener<*>> = HashMap()
    val listenerFactory: SimpleMetricListenerFactory = SimpleMetricListenerFactory(timeManager)

    fun <T: Comparable<T>> register(metricDescriptor: MetricDescriptor, valueListener: ValueListener<T>) {
        var listener: IMetricListener<*>? = registeredMetricListeners[metricDescriptor]
        if (listener == null) {
            listener = listenerFactory.createDoubleInstance(metricDescriptor)
            registeredMetricListeners[metricDescriptor] = listener
        }
        // TODO: Temporary fix until type description is implemented
        listener as IMetricListener<T>
        listener.subscribe(valueListener)
        valueListener.metricListener = listener
    }

}