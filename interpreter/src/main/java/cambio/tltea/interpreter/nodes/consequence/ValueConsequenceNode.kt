package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

abstract class ValueConsequenceNode<T>(triggerNotifier: TriggerNotifier, val value: T,
                                       temporalContext: TemporalOperatorInfo
) :
    ConsequenceNode(triggerNotifier, temporalContext) {
    override fun activateConsequence() {
        throw UnsupportedOperationException("Value consequence nodes cannot be activated")
    }
}

class NumberConsequenceNode(triggerNotifier: TriggerNotifier, value: Double) :
    ValueConsequenceNode<Double>(triggerNotifier, value)

class StringConsequenceNode(triggerNotifier: TriggerNotifier, value: String) :
    ValueConsequenceNode<String>(triggerNotifier, value)

class BooleanConsequenceNode(triggerNotifier: TriggerNotifier, value: Boolean) :
    ValueConsequenceNode<Boolean>(triggerNotifier, value)
