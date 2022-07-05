package cambio.tltea.interpreter.nodes.consequence.activation

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo


internal fun extractServiceName(keyword: String, data_str: String): String {
    val regex = Regex("$keyword\\[(.*)]", RegexOption.IGNORE_CASE)
    val match = regex.find(data_str)
        ?: throw IllegalArgumentException("Invalid data string: $data_str. Expected: '$keyword[<service name>]'")
    return match.groupValues[1]
}

internal class EventActivationConsequenceNode(
    private val eventName: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo,
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    override fun activateConsequence() {
        triggerManager.trigger(EventActivationData(eventName, temporalContext))
    }

    override fun deactivateConsequence() {
        triggerManager.trigger(EventPreventionData(eventName, temporalContext))
    }
}

internal class EventPreventionConsequenceNode(
    private val eventName: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo,
) : ActivationConsequenceNode(triggerManager, temporalContext) {
    override fun activateConsequence() {
        triggerManager.trigger(EventPreventionData(eventName, temporalContext))
    }

    override fun deactivateConsequence() {
        triggerManager.trigger(EventActivationData(eventName, temporalContext))
    }
}


internal class ServiceFailureConsequenceNode(
    private val data_str: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    val serviceName: String = extractServiceName("kill", data_str)

    override fun activateConsequence() {
        triggerManager.trigger(ServiceFailureEventData(serviceName, temporalContext))
    }

    override fun deactivateConsequence() {}
}

internal class ServiceStopConsequenceNode(
    private val data_str: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    val serviceName: String = extractServiceName("stop", data_str)

    override fun activateConsequence() {
        triggerManager.trigger(ServiceStopEventData(serviceName, temporalContext))
    }

    override fun deactivateConsequence() {}
}


internal class ServiceStartConsequenceNode(
    private val data_str: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    val serviceName: String = extractServiceName("start", data_str)

    override fun activateConsequence() {
        triggerManager.trigger(ServiceStartEventData(serviceName, temporalContext))
    }

    override fun deactivateConsequence() {}

}

internal class LoadModificationConsequenceNode(
    private val data_str: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    val endpointName: String
    val loadModifier: Double
    val isFactor: Boolean

    init {
        //expected format load[x?[+-]<float>:<endpoint name>]
        val regex = Regex("load\\[(x?)([+-]?[0-9]+|[0-9]*.[0-9]+):(.+)]", RegexOption.IGNORE_CASE)
        val match = regex.matchEntire(data_str) ?: throw IllegalArgumentException(
            "Invalid load modification description string '$data_str'.\n" +
                    "Expected format 'load[x<float>:<endpoint name>]'.\n" +
                    "The 'x' in front of the float is optional to select between factor or fixed value. "
        )

        try {
            this.isFactor = match.groupValues[1].isNotBlank()
            this.loadModifier = match.groupValues[2].toDouble()
            this.endpointName = match.groupValues[3]
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid number format in load modification description string '$data_str'")
        }
    }

    override fun activateConsequence() {
        triggerManager.trigger(LoadModificationEventData(endpointName, loadModifier, isFactor, temporalContext))
    }

    override fun deactivateConsequence() {}
}


internal class ValueEventConsequenceNode<T : Any>(
    val targetProperty: String,
    val targetValue: T,
    val operator: OperatorToken,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    override fun activateConsequence() {
        triggerManager.trigger(ValueEventActivationData(targetProperty, targetValue, operator, true, temporalContext))
    }

    override fun deactivateConsequence() {
        triggerManager.trigger(ValueEventActivationData(targetProperty, targetValue, operator, false, temporalContext))
    }

    fun getType(): Class<out T> {
        return targetValue::class.javaObjectType
    }
}