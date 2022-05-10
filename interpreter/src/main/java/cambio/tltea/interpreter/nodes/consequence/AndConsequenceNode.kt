@file:JvmName("AndConsequence")

package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class AndConsequenceNode(
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo,
    children: Collection<ConsequenceNode>
) :
    ChildrenOwningConsequenceNode(triggerManager, temporalContext, children) {
    override fun activateConsequence() {
        children.forEach { it.activateConsequence() }
    }

    override fun deactivateConsequence() {
        children.forEach { it.deactivateConsequence() }
    }
}