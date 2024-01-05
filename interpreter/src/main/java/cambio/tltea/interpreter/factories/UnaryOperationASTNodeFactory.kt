package cambio.tltea.interpreter.factories

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.logic.bool.NotBoolLogic
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.interpreter.nodes.structure.StateNode
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.UnaryOperationASTNode
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import kotlin.reflect.KClass

class UnaryOperationASTNodeFactory(private val brokers: Brokers) : INodeFactory {
    override fun getSupportedASTNodeType(): KClass<out ASTNode> {
        return UnaryOperationASTNode::class
    }

    override fun interpretAsCause(
        unNode: ASTNode, interpreterRoot: INode<Boolean, Boolean>, temporalContext: TemporalOperatorInfo
    ): INode<Boolean, Boolean> {
        unNode as UnaryOperationASTNode
        when (unNode.operator) {
            OperatorToken.NOT -> {
                return StateNode(interpreterRoot, NotBoolLogic(brokers))
            }

            else -> {
                throw UnsupportedOperationException("Operator not supported for cause description (left side of implication): " + unNode.operator);
            }
        }
    }
}