package cambio.tltea.interpreter.connector.value

class MetricDescriptor(val architectureIdentifier: String, val metricIdentifier: String, val boolean: Boolean = false) {

    override fun equals(other: Any?) =
        (other is MetricDescriptor) && architectureIdentifier == other.architectureIdentifier && metricIdentifier == other.metricIdentifier

    override fun hashCode() = (architectureIdentifier + metricIdentifier).hashCode()
}