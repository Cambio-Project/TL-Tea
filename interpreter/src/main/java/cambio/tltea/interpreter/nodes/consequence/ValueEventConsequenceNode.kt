package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.interpreter.nodes.TriggerManager
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo

class ValueEventConsequenceNode<T : Any>(
    val targetProperty: String,
    val targetValue: T,
    val operator: OperatorToken,
    triggerManager: TriggerManager,
    temporalContext: TemporalOperatorInfo
) : ActivationConsequenceNode(triggerManager, temporalContext) {

    override fun activateConsequence() {
        triggerManager.trigger(ValueEventActivationData(targetProperty, targetValue, operator, true, temporalContext))
    }

    override fun deactivateConsequence() {
        triggerManager.trigger(ValueEventActivationData(targetProperty, targetValue, operator, false, temporalContext))
    }

    fun getType(): Class<out T> {
        return targetValue::class.javaObjectType
    }
}
