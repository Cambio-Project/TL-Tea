@file:JvmName("AndConsequence")

package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class AndConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo,
    children: List<ConsequenceNode>
) :
    ChildrenOwningConsequenceNode(triggerNotifier, temporalContext, children) {
    override fun activateConsequence() {
        children.forEach { it.activateConsequence() }
    }
}