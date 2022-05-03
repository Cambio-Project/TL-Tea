package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class EventPreventionConsequenceNode(
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo,
    private val eventName: String,
) : ActivationConsequenceNode(triggerManager, temporalContext) {
    override fun activateConsequence() {
        TODO("Not yet implemented")
    }

    override fun deactivateConsequence() {
        TODO("Not yet implemented")
    }
}