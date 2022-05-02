package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class ValueEventConsequenceNode<T : Any>(
    triggerNotifier: TriggerNotifier,
    val value: T,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerNotifier, temporalContext) {

    override fun activateConsequence() {
        throw UnsupportedOperationException("Value consequence nodes cannot be activated")

    }

    fun getType(): Class<out T> {
        return value::class.javaObjectType
    }
}
