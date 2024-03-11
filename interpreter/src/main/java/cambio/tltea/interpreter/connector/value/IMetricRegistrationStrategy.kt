package cambio.tltea.interpreter.connector.value

/**
 * Registers a [IMetricListener] at a data source metric. Has to be implemented by the data source.
 */
interface IMetricRegistrationStrategy {

    /**
     * Registers a [IMetricListener] at the source described by a [MetricDescriptor].
     */
    fun register(listener: IMetricListener<*>, descriptor: MetricDescriptor)
}