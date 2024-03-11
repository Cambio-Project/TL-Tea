package cambio.tltea.interpreter.factories

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.nodes.cause.ConstantValueProvider
import cambio.tltea.interpreter.nodes.cause.ValueListener
import cambio.tltea.interpreter.nodes.logic.bool.AndBoolLogic
import cambio.tltea.interpreter.nodes.logic.bool.IffBoolLogic
import cambio.tltea.interpreter.nodes.logic.bool.ImplicationBoolLogic
import cambio.tltea.interpreter.nodes.logic.bool.OrBoolLogic
import cambio.tltea.interpreter.nodes.logic.relational.OperatorToRelationalLogicMapper
import cambio.tltea.interpreter.nodes.structure.RelationalNode
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.interpreter.nodes.structure.StateNode
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.BinaryOperationASTNode
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.ValueASTNode
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import kotlin.reflect.KClass

class BinaryOperationASTNodeFactory(
    private val brokers: Brokers, private val listeners: MutableList<ValueListener<*>>
) : INodeFactory {
    override fun getSupportedASTNodeType(): KClass<out ASTNode> {
        return BinaryOperationASTNode::class
    }

    private val booleanMapper = OperatorToRelationalLogicMapper<Boolean>(brokers)
    private val doubleMapper = OperatorToRelationalLogicMapper<Double>(brokers)

    override fun interpretAsCause(
        unNode: ASTNode, interpreterRoot: INode<Boolean, Boolean>, temporalContext: TemporalOperatorInfo
    ): INode<Boolean, Boolean> {
        unNode as BinaryOperationASTNode
        return when (unNode.operator) {
            OperatorToken.AND -> {
                StateNode(interpreterRoot, AndBoolLogic(brokers))
                // TODO: OPTIMIZE?
                // flattenCause(unNode, node, temporalContext)
            }

            OperatorToken.OR -> {
                StateNode(interpreterRoot, OrBoolLogic(brokers))
                // TODO: OPTIMIZE?
                // flattenCause(unNode, node, temporalContext)
            }

            OperatorToken.IMPLIES -> {
                StateNode(interpreterRoot, ImplicationBoolLogic(brokers))
            }

            OperatorToken.IFF -> {
                StateNode(interpreterRoot, IffBoolLogic(brokers))
            }

            else -> {
                if (OperatorToken.ComparisonOperatorTokens.contains(unNode.operator) && unNode.leftChild is ValueASTNode && unNode.rightChild is ValueASTNode) {
                    return interpretAsValueProviders(unNode, interpreterRoot)
                }

                throw UnsupportedOperationException(
                    "Operator not yet supported as cause description: " + unNode.operator + "\n(left side of implication)"
                )
            }
        }
    }


    // TODO: refactor
    private fun interpretAsValueProviders(
        binaryNode: BinaryOperationASTNode, interpreterRoot: INode<Boolean, Boolean>
    ): RelationalNode<*> {
        val left = binaryNode.leftChild as ValueASTNode
        val right = binaryNode.rightChild as ValueASTNode

        val isBoolean = isBooleanListener(left) || isBooleanListener(right)

        if (isBoolean) {
            val leftValue = if (left.containsPropertyAccess()) {
                handleBooleanListener(left)
            } else {
                ConstantValueProvider(left.value.replace("(", "").replace(")", "").toBoolean())
            }

            val rightValue = if (right.containsPropertyAccess()) {
                handleBooleanListener(right)
            } else {
                ConstantValueProvider(right.value.replace("(", "").replace(")", "").toBoolean())
            }

            val logic = booleanMapper.map(binaryNode.operator)
            return RelationalNode(interpreterRoot, logic, leftValue, rightValue)
        } else {
            val leftValue = if (left.containsPropertyAccess()) {
                handleDoubleListener(left)
            } else {
                ConstantValueProvider(left.value.toDouble())

            }

            val rightValue = if (right.containsPropertyAccess()) {
                handleDoubleListener(right)
            } else {
                ConstantValueProvider(right.value.toDouble())
            }

            val logic = doubleMapper.map(binaryNode.operator)
            return RelationalNode(interpreterRoot, logic, leftValue, rightValue)
        }
    }

    private fun handleDoubleListener(node: ValueASTNode): ValueListener<Double> {
        // TODO: this is highly fragile code
        val parts = node.eventName.split("\$")
        val architectureName = parts[0]
        val metricIdentifier = parts[1]
        val listener = ValueListener(metricIdentifier, 0.0)
        val descriptor = MetricDescriptor(architectureName, metricIdentifier, false)
        brokers.metricBroker.register(descriptor, listener)
        listeners.add(listener)
        return listener
    }

    private fun handleBooleanListener(node: ValueASTNode): ValueListener<Boolean> {
        // TODO: this is highly fragile code
        val parts = node.eventName.split("\$")
        val architectureName = parts[0]
        val metricIdentifier = parts[1].replace("b:", "")
        val listener = ValueListener(metricIdentifier, false)
        val descriptor = MetricDescriptor(architectureName, metricIdentifier, true)
        brokers.metricBroker.register(descriptor, listener)
        listeners.add(listener)
        return listener
    }

    private fun isBooleanListener(node: ValueASTNode): Boolean {
        return if (node.containsEventName()) {
            node.eventName.contains("b:")
        } else {
            false
        }
    }

    @Deprecated("Better do a late manipulation of the already created tree")
    private fun flattenCause(
        root: BinaryOperationASTNode, interpreterRoot: INode<Boolean, Boolean>, temporalContext: TemporalOperatorInfo
    ): List<INode<Boolean, Boolean>> {
        val children = mutableListOf<INode<Boolean, Boolean>>()

        val leftChild = root.leftChild
        if (leftChild is BinaryOperationASTNode && leftChild.operator == root.operator) {
            children.addAll(flattenCause(leftChild, interpreterRoot, temporalContext))
        } else {
            children.add(interpretAsCause(leftChild, interpreterRoot, temporalContext))
        }

        val rightChild = root.rightChild
        if (rightChild is BinaryOperationASTNode && rightChild.operator == root.operator) {
            children.addAll(flattenCause(rightChild, interpreterRoot, temporalContext))
        } else {
            children.add(interpretAsCause(rightChild, interpreterRoot, temporalContext))
        }

        return children
    }

}