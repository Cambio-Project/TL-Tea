package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

abstract class EventConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo
) : ConsequenceNode(triggerNotifier, temporalContext)

class EventActivationConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo,
    private val eventName: String,
) : EventConsequenceNode(triggerNotifier, temporalContext) {
    override fun activateConsequence() {
        TODO("Not yet implemented")
    }
}

class EventPreventionConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo,
    private val eventName: String,
) : ConsequenceNode(triggerNotifier, temporalContext) {
    override fun activateConsequence() {
        TODO("Not yet implemented")
    }
}