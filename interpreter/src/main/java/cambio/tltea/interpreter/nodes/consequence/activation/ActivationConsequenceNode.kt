package cambio.tltea.interpreter.nodes.consequence.activation

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.interpreter.nodes.consequence.ConsequenceNode
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

abstract class ActivationConsequenceNode(
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ConsequenceNode(triggerManager, temporalContext)