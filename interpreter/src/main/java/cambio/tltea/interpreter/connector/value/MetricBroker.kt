package cambio.tltea.interpreter.connector.value

import cambio.tltea.interpreter.nodes.cause.ValueListener

class MetricBroker() {
    private val registeredMetricListeners: MutableMap<MetricDescriptor, IMetricListener<*>> = HashMap()
    val listenerFactory: SimpleMetricListenerFactory = SimpleMetricListenerFactory()

    fun <T: Comparable<T>> register(metricDescriptor: MetricDescriptor, valueListener: ValueListener<T>) {
        var listener: IMetricListener<*>? = registeredMetricListeners[metricDescriptor]
        if (listener == null) {
            listener = listenerFactory.createInstance(metricDescriptor)
            registeredMetricListeners[metricDescriptor] = listener
        }
        // TODO: Temporary fix until type description is implemented
        listener as IMetricListener<T>
        listener.subscribe(valueListener)
        valueListener.metricListener = listener
    }

}