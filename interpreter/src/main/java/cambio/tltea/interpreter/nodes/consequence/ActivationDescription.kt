package cambio.tltea.interpreter.nodes.consequence

import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo


abstract class ActivationDescription(
    open val temporalContext: TemporalOperatorInfo,
    open val activationNode: ConsequenceNode
)

data class OrActivationDescription(
    override val temporalContext: TemporalOperatorInfo,
    override val activationNode: OrConsequenceNode,
    val children: List<ConsequenceNode>
) : ActivationDescription(temporalContext, activationNode)


class ComparisonActivationDescription(
    override val temporalContext: TemporalOperatorInfo,
    override val activationNode: ComparisonConsequenceNode,
    val operator: OperatorToken,
    val left: ValueEventConsequenceNode<*>,
    val right: ValueEventConsequenceNode<*>
) : ActivationDescription(temporalContext, activationNode) {

}

class EventActivationDescription(
    override val temporalContext: TemporalOperatorInfo,
    override val activationNode: ConsequenceNode,
    val eventName: String
) : ActivationDescription(temporalContext, activationNode) {
}

class EventPreventionActivationDescription(
    override val temporalContext: TemporalOperatorInfo,
    override val activationNode: ConsequenceNode,
    val eventName: String
) : ActivationDescription(temporalContext, activationNode) {
}