package cambio.tltea.interpreter.factories

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.logic.temporal.AlwaysTemporalLogic
import cambio.tltea.interpreter.nodes.logic.temporal.EventuallyTemporalLogic
import cambio.tltea.interpreter.nodes.logic.temporal.NextTemporalLogic
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.interpreter.nodes.structure.TemporalStateNode
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import cambio.tltea.parser.core.temporal.TemporalUnaryOperationASTNode
import kotlin.reflect.KClass

class TemporalUnaryOperationASTNodeFactory(private val brokers: Brokers) : INodeFactory {

    override fun getSupportedASTNodeType(): KClass<out ASTNode> {
        return TemporalUnaryOperationASTNode::class
    }

    override fun interpretAsCause(
        unNode: ASTNode, interpreterRoot: INode<Boolean, Boolean>, temporalContext: TemporalOperatorInfo
    ): INode<Boolean, Boolean> {
        unNode as TemporalUnaryOperationASTNode
        val temporalInterval = unNode.temporalValue
        temporalInterval as TemporalInterval
        return when (unNode.operator) {
            OperatorToken.FINALLY -> {
                TemporalStateNode(interpreterRoot, EventuallyTemporalLogic(temporalInterval, brokers))
            }

            OperatorToken.GLOBALLY -> {
                TemporalStateNode(interpreterRoot, AlwaysTemporalLogic(temporalInterval, brokers))
            }

            OperatorToken.NEXT -> {
                TemporalStateNode(interpreterRoot, NextTemporalLogic(brokers))
            }

            else -> {
                throw UnsupportedOperationException("Operator not supported for cause description (left side of implication): " + unNode.operator);
            }
        }
    }
}