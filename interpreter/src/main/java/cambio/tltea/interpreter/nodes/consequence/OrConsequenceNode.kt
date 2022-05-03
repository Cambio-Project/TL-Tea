package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class OrConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo,
    children: List<ConsequenceNode>
) : ChildrenOwningConsequenceNode(triggerNotifier, temporalContext, children) {
    override fun activateConsequence() {
        TODO("Not yet implemented")
    }

    override fun deactivateConsequence() {
        TODO("Not yet implemented")
    }
}
