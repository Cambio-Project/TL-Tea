package cambio.tltea.interpreter.nodes.consequence.activation

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.ITemporalValue
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import cambio.tltea.parser.core.temporal.TemporalPropositionParser

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

    //expected format kill[<service name>,<instance count>]
    private val regex = Regex("kill\\[([^,]*)(,([0-9]+))?]", RegexOption.IGNORE_CASE)
    private val match = regex.matchEntire(data_str) ?: throw IllegalArgumentException(
        "Invalid service failure string '$data_str'.\n" +
                "Expected format 'kill[<service name>,<instance count>]'."
    )

    val serviceName: String = match.groupValues[1]
    private val countString: String = match.groupValues[3]
    val count: Int = if (!countString.isEmpty()) {
        countString.toInt()
    } else {
        Int.MAX_VALUE
    }

    override fun activateConsequence() {
        triggerManager.trigger(ServiceFailureEventData(serviceName, count, temporalContext))
    }

    override fun deactivateConsequence() {}
}

internal class ServiceStopConsequenceNode(
    private val data_str: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    //expected format stop[<service name>,<instance count>]
    private val regex = Regex("stop\\[([^,]*)(,([0-9]+))?]", RegexOption.IGNORE_CASE)
    private val match = regex.matchEntire(data_str) ?: throw IllegalArgumentException(
        "Invalid service failure string '$data_str'.\n" +
                "Expected format 'stop[<service name>,<instance count>]'."
    )

    val serviceName: String = match.groupValues[1]
    private val countString: String = match.groupValues[3]
    val count: Int = if (!countString.isEmpty()) {
        countString.toInt()
    } else {
        Int.MAX_VALUE
    }

    override fun activateConsequence() {
        triggerManager.trigger(ServiceStopEventData(serviceName, count, temporalContext))
    }

    override fun deactivateConsequence() {}
}


internal class ServiceStartConsequenceNode(
    private val data_str: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    //expected format start[<service name>,<instance count>]
    private val regex = Regex("start\\[([^,]*)(,([0-9]+))?]", RegexOption.IGNORE_CASE)
    private val match = regex.matchEntire(data_str) ?: throw IllegalArgumentException(
        "Invalid service failure string '$data_str'.\n" +
                "Expected format 'start[<service name>,<instance count>]'."
    )

    val serviceName: String = match.groupValues[1]
    private val countString: String = match.groupValues[3]
    val count: Int = if (!countString.isEmpty()) {
        countString.toInt()
    } else {
        Int.MAX_VALUE
    }

    override fun activateConsequence() {
        triggerManager.trigger(ServiceStartEventData(serviceName, count, temporalContext))
    }

    override fun deactivateConsequence() {}

}

internal class LoadModificationConsequenceNode(
    private val data_str: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    val endpointName: String
    val functionType: String
    val loadModifier: Double
    val isFactor: Boolean
    val duration: ITemporalValue

    init {
        //expected format load[x?[+-]<float>[:<type>]:<endpoint name>]
        val regex = Regex("load(\\[.*])\\[(x?)([+-]?[0-9]+|[0-9]*.[0-9]+)(:(.+))?:(.+)]", RegexOption.IGNORE_CASE)
        val match = regex.matchEntire(data_str) ?: throw IllegalArgumentException(
            "Invalid load modification description string '$data_str'.\n" +
                    "Expected format 'load[x<float>[:<type>]:<endpoint name>]'.\n" +
                    "The 'x' in front of the float is optional to select between factor or fixed value. "
        )

        try {
            this.duration = TemporalPropositionParser.parse(match.groupValues[1])
            this.isFactor = match.groupValues[2].isNotBlank()
            this.loadModifier = match.groupValues[3].toDouble()
            this.functionType = match.groupValues[5]
            this.endpointName = match.groupValues[6]

        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid number format in load modification description string '$data_str'")
        }
    }

    override fun activateConsequence() {
        triggerManager.trigger(
            LoadModificationEventData(
                endpointName,
                loadModifier,
                duration,
                functionType,
                isFactor,
                temporalContext
            )
        )
    }

    override fun deactivateConsequence() {}
}

internal class HookEventConsequenceNode(
    private val data_str: String,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {
    val name: String

    init {
        val regex = Regex("event\\[(.+)]", RegexOption.IGNORE_CASE)
        val match = regex.matchEntire(data_str) ?: throw IllegalArgumentException(
            "Invalid hook event description string '$data_str'.\n" +
                    "Expected format 'event[<name>]'.\n"
        )
        this.name = match.groupValues[1]
    }

    override fun activateConsequence() {
        triggerManager.trigger(
            HookEventData(
                name,
                temporalContext
            )
        )
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