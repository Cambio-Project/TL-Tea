package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class EventActivationConsequenceNode(
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo,
    private val eventName: String,
) : ActivationConsequenceNode(triggerManager, temporalContext) {
    override fun activateConsequence() {
        triggerManager.trigger(EventActivationData(eventName, temporalContext))
    }

    override fun deactivateConsequence() {
        triggerManager.trigger(EventPreventionData(eventName, temporalContext))
    }
}