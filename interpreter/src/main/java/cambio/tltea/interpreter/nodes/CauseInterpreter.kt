package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.*
import cambio.tltea.interpreter.utils.ASTManipulator
import cambio.tltea.parser.core.*
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import cambio.tltea.parser.core.temporal.TemporalUnaryOperationASTNode

class CauseInterpreter {

    private val listeners = mutableListOf<EventActivationListener>()

    fun interpretMTLCause(
        root: ASTNode,
        temporalContext: TemporalOperatorInfo = TemporalOperatorInfo(OperatorToken.GLOBALLY, "[0,inf]")
    ): CauseDescription {
        val interpretedRoot: CauseNode = interpretAsCause(root, temporalContext)
        return CauseDescription(interpretedRoot, listeners)
    }


    private fun interpretAsCause(root: ASTNode, temporalContext: TemporalOperatorInfo): CauseNode {
        return when (root) {
            is TemporalUnaryOperationASTNode -> {
                return interpretAsCause(root.child, root.toTemporalOperatorInfo())
            }
            is ValueASTNode -> {
                interpretAsCauseEvent(root, temporalContext)
            }
            is UnaryOperationASTNode -> {
                interpretAsCause(root, temporalContext)
            }
            is BinaryOperationASTNode -> {
                interpretAsCause(root, temporalContext)
            }
            else -> {
                throw IllegalArgumentException("Unsupported ASTNode type: ${root.javaClass.name}");
            }
        }
    }

    private fun interpretAsCause(
        unNode: UnaryOperationASTNode, temporalContext: TemporalOperatorInfo
    ): CauseNode {

        when (unNode.operator) {
            OperatorToken.NOT -> {
                if (unNode.child is ValueASTNode) {
                    @Suppress("UNCHECKED_CAST")
                    return NotCauseNode(interpretAsCauseEvent(unNode.child as ValueASTNode, temporalContext), temporalContext)
                } else return (interpretAsCause(ASTManipulator.applyNot(unNode), temporalContext))
            }
            else -> {
                throw UnsupportedOperationException("Operator not supported for cause description (left side of implication): " + unNode.getOperator());
            }
        }
    }

    private fun interpretAsCauseEvent(valueNode: ValueASTNode, temporalContext: TemporalOperatorInfo): CauseNode {
        if (valueNode.containsEventName()) {
            val eventActivationListener = EventActivationListener(valueNode.eventName)
            listeners.add(eventActivationListener)

            //wrap event activation in a ==True comparison
            return ComparisonCauseNode(OperatorToken.EQ, temporalContext, eventActivationListener, ConstantValueProvider(true))
        } else if (valueNode.value.contains("$")) {
            TODO("A value watcher cannot be created yet.")
        }
        throw UnsupportedOperationException(
            "Value %s cannot be interpreted as event Activation. Try wrapping it in parenthesis like '(%s)'.".format(
                valueNode.value,
                valueNode.value
            )
        )
    }

    private fun interpretAsValue(valueNode: ValueASTNode): ValueProvider<*> {
        try {
            val d = valueNode.value.toDouble()
            return ConstantValueProvider(d)
        } catch (e: NumberFormatException) {
        }
        return ConstantValueProvider(valueNode.value)
    }


    private fun interpretAsCause(binaryNode: BinaryOperationASTNode, temporalContext: TemporalOperatorInfo): CauseNode {
        return when (binaryNode.operator) {
            OperatorToken.AND -> {
                val children = flattenCause(binaryNode, temporalContext)
                AndCauseNode(temporalContext, *children.toTypedArray())
            }
            OperatorToken.OR -> {
                val children = flattenCause(binaryNode, temporalContext)
                OrCauseNode(temporalContext, *children.toTypedArray())
            }
            else -> {
                if (OperatorToken.ComparisonOperatorTokens.contains(binaryNode.operator)
                    && binaryNode.leftChild is ValueASTNode
                    && binaryNode.rightChild is ValueASTNode
                ) {
                    return ComparisonCauseNode(
                        binaryNode.operator,
                        null,
                        interpretAsValue(binaryNode.leftChild as ValueASTNode),
                        interpretAsValue(binaryNode.rightChild as ValueASTNode)
                    )
                }

                throw UnsupportedOperationException(
                    "Operator not yet supported as cause description: "
                            + binaryNode.operator
                            + "\n(left side of implication)"
                )
            }
        }
    }

    private fun flattenCause(root: BinaryOperationASTNode, temporalContext: TemporalOperatorInfo): List<CauseNode> {
        val children = mutableListOf<CauseNode>()

        val leftChild = root.leftChild
        if (leftChild is BinaryOperationASTNode && leftChild.operator == root.operator) {
            children.addAll(flattenCause(leftChild, temporalContext))
        } else {
            children.add(interpretAsCause(leftChild, temporalContext))
        }

        val rightChild = root.rightChild
        if (rightChild is BinaryOperationASTNode && rightChild.operator == root.operator) {
            children.addAll(flattenCause(rightChild, temporalContext))
        } else {
            children.add(interpretAsCause(rightChild, temporalContext))
        }

        return children
    }

}