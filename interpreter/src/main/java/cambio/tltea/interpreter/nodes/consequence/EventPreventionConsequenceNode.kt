package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class EventPreventionConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo,
    private val eventName: String,
) : ActivationConsequenceNode(triggerNotifier, temporalContext) {
    override fun activateConsequence() {
        TODO("Not yet implemented")
    }

    override fun deactivateConsequence() {
        TODO("Not yet implemented")
    }
}