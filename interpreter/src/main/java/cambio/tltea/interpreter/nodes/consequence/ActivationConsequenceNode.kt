package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

abstract class ActivationConsequenceNode(
    triggerNotifier: TriggerNotifier,
    temporalContext: TemporalOperatorInfo
) : ConsequenceNode(triggerNotifier, temporalContext)