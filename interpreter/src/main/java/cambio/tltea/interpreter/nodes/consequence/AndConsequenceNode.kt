@file:JvmName("AndConsequence")

package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class AndConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo,
    children: Collection<ConsequenceNode>
) :
    ChildrenOwningConsequenceNode(triggerNotifier, temporalContext, children) {
    override fun activateConsequence() {
        children.forEach { it.activateConsequence() }
    }

    override fun deactivateConsequence() {
        children.forEach { it.deactivateConsequence() }
    }
}