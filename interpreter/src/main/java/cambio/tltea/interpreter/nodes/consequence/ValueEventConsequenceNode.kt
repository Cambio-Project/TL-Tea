package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerNotifier
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class ValueEventConsequenceNode<T : Any>(
    triggerNotifier: TriggerNotifier,
    val targetProperty: String,
    val targetValue: T,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerNotifier, temporalContext) {

    override fun activateConsequence() {
        TODO("Not yet implemented")
    }

    override fun deactivateConsequence() {
        TODO("Not yet implemented")
    }

    fun getType(): Class<out T> {
        return targetValue::class.javaObjectType
    }
}
