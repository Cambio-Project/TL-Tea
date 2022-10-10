package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.activation.*
import cambio.tltea.interpreter.nodes.consequence.activation.EventActivationConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.EventPreventionConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.LoadModificationConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.ServiceFailureConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.ServiceStartConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.ServiceStopConsequenceNode
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

object ActivationConsequenceNodeFactory {

    fun getActivationConsequenceNode(
        eventText: String,
        triggerManager: TriggerManager,
        temporalContext: TemporalOperatorInfo,
        inverted: Boolean = false
    ): ActivationConsequenceNode {

        val eventTextLowercase = eventText.lowercase()
        try {
            return if (eventTextLowercase.startsWith("kill") && !inverted) {
                ServiceFailureConsequenceNode(eventText, triggerManager, temporalContext)
            } else if (eventTextLowercase.startsWith("start") && !inverted) {
                ServiceStartConsequenceNode(eventText, triggerManager, temporalContext)
            } else if (eventTextLowercase.startsWith("stop") && !inverted) {
                ServiceStopConsequenceNode(eventText, triggerManager, temporalContext)
            } else if (eventTextLowercase.startsWith("load")) {
                LoadModificationConsequenceNode(eventText, triggerManager, temporalContext)
            } else if (eventTextLowercase.startsWith("event")) {
                HookEventConsequenceNode(eventText, triggerManager, temporalContext)
            } else {
                throw IllegalArgumentException("Could not find an event activation mode. Returning EventActivationConsequenceNode")
            }
        } catch (e: Exception) {
            return if (!inverted)
                EventActivationConsequenceNode(eventText, triggerManager, temporalContext)
            else
                EventPreventionConsequenceNode(eventText, triggerManager, temporalContext)
        }
    }

}
