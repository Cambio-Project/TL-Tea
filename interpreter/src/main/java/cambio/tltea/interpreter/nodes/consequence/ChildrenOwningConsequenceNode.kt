package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

abstract class ChildrenOwningConsequenceNode(
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo,
    protected val children: Collection<ConsequenceNode>
) : ConsequenceNode(triggerManager, temporalContext)