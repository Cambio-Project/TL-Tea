package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.*
import cambio.tltea.interpreter.utils.ASTManipulator
import cambio.tltea.parser.core.*

class CauseInterpreter {

    private val listeners = mutableListOf<EventActivationListener>()

    fun interpretLTL(root: ASTNode): CauseDescription {
        val interpretedRoot: CauseNode = interpretAsCause(root)
        return CauseDescription(interpretedRoot, listeners)
    }


    private fun interpretAsCause(root: ASTNode): CauseNode {
        return when (root) {
            is ValueASTNode -> {
                interpretAsCauseEvent(root)
            }
            is UnaryOperationASTNode -> {
                interpretAsCause(root)
            }
            is BinaryOperationASTNode -> {
                interpretAsCause(root)
            }
            else -> {
                throw IllegalArgumentException("Unsupported ASTNode type: ${root.javaClass.name}");
            }
        }
    }

    private fun interpretAsCause(
        unNode: UnaryOperationASTNode
    ): CauseNode {

        when (unNode.operator) {
            OperatorToken.NOT -> {
                if (unNode.child is ValueASTNode) {
                    @Suppress("UNCHECKED_CAST")
                    return NotCauseNode(interpretAsCauseEvent(unNode.child as ValueASTNode))
                } else return (interpretAsCause(ASTManipulator.applyNot(unNode)))
            }
            else -> {
                throw UnsupportedOperationException("Operator not supported for cause description (left side of implication): " + unNode.getOperator());
            }
        }
    }

    private fun interpretAsCauseEvent(valueNode: ValueASTNode): CauseNode {
        if (valueNode.value.startsWith("(") && valueNode.value.endsWith(")")) {
            val eventActivationNode = EventActivationNode(valueNode.value)
            listeners.add(eventActivationNode.eventListener)

            //wrap event activation in a ==True comparison
            return ComparisonCauseNode(OperatorToken.EQ, null, eventActivationNode, ConstantValueProvider(true))
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


    private fun interpretAsCause(binaryNode: BinaryOperationASTNode): CauseNode {
        return when (binaryNode.operator) {
            OperatorToken.AND -> {
                val children = flattenCause(binaryNode)
                AndCauseNode(*children.toTypedArray())
            }
            OperatorToken.OR -> {
                val children = flattenCause(binaryNode)
                OrCauseNode(*children.toTypedArray())
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

    private fun flattenCause(root: BinaryOperationASTNode): List<CauseNode> {
        val children = mutableListOf<CauseNode>()

        val leftChild = root.leftChild
        if (leftChild is BinaryOperationASTNode && leftChild.operator == root.operator) {
            children.addAll(flattenCause(leftChild))
        } else {
            children.add(interpretAsCause(leftChild))
        }

        val rightChild = root.rightChild
        if (rightChild is BinaryOperationASTNode && rightChild.operator == root.operator) {
            children.addAll(flattenCause(rightChild))
        } else {
            children.add(interpretAsCause(rightChild))
        }

        return children
    }

}