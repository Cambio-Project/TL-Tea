package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class OrConsequenceNode(
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo,
    children: List<ConsequenceNode>
) : ChildrenOwningConsequenceNode(triggerManager, temporalContext, children) {
    override fun activateConsequence() {
        TODO("Not yet implemented")
    }

    override fun deactivateConsequence() {
        TODO("Not yet implemented")
    }
}
