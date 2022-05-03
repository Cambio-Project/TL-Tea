package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

abstract class ChildrenOwningConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo,
    protected val children: Collection<ConsequenceNode>
) : ConsequenceNode(triggerNotifier, temporalContext)