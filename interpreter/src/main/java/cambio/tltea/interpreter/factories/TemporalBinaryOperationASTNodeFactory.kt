package cambio.tltea.interpreter.factories

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.logic.temporal.UntilTemporalLogic
import cambio.tltea.interpreter.nodes.logic.temporal.WeakUntilTemporalLogic
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.interpreter.nodes.structure.TemporalStateNode
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.temporal.TemporalBinaryOperationASTNode
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import kotlin.reflect.KClass

class TemporalBinaryOperationASTNodeFactory(private val brokers: Brokers) : INodeFactory {

    override fun getSupportedASTNodeType(): KClass<out ASTNode> {
        return TemporalBinaryOperationASTNode::class
    }

    override fun interpretAsCause(
        binNode: ASTNode, interpreterRoot: INode<Boolean, Boolean>, temporalContext: TemporalOperatorInfo
    ): INode<Boolean, Boolean> {
        binNode as TemporalBinaryOperationASTNode
        val temporalInterval = binNode.temporalValue
        temporalInterval as TemporalInterval
        return when (binNode.operator) {
            OperatorToken.UNTIL -> {
                TemporalStateNode(interpreterRoot, UntilTemporalLogic(temporalInterval, brokers))
            }

            OperatorToken.WEAKUNTIL -> {
                TemporalStateNode(interpreterRoot, WeakUntilTemporalLogic(temporalInterval, brokers))
            }

            else -> {
                throw UnsupportedOperationException("Operator not supported for cause description (left side of implication): " + binNode.operator);
            }
        }
    }
}