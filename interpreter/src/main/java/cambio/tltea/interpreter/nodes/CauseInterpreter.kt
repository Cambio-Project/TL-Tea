package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.cause.*
import cambio.tltea.interpreter.utils.ASTManipulator
import cambio.tltea.parser.core.*
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import cambio.tltea.parser.core.temporal.TemporalUnaryOperationASTNode

class CauseInterpreter() {

    private val listeners = mutableListOf<ValueListener<*>>()

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
                return if (unNode.child is ValueASTNode) {
                    NotCauseNode(
                        interpretAsCauseEvent(unNode.child as ValueASTNode, temporalContext),
                        temporalContext
                    )
                } else (interpretAsCause(ASTManipulator.applyNot(unNode), temporalContext))
            }

            else -> {
                throw UnsupportedOperationException("Operator not supported for cause description (left side of implication): " + unNode.getOperator());
            }
        }
    }

    private fun interpretAsCauseEvent(valueNode: ValueASTNode, temporalContext: TemporalOperatorInfo): CauseNode {
        if (valueNode.containsEventName()) {
            val eventName = valueNode.eventName
            val eventActivationListener: ValueListener<Boolean>
            val eventTextLowercase = valueNode.eventName.lowercase()

            if (eventTextLowercase.startsWith("event")) {
                // handle named events
                val regex = Regex("event\\[(.+)]", RegexOption.IGNORE_CASE)
                val match = regex.matchEntire(eventName) ?: throw IllegalArgumentException(
                    "Invalid load modification description string '$eventName'.\n" +
                            "Expected format 'load[x<float>[:<type>]:<endpoint name>]'.\n" +
                            "The 'x' in front of the float is optional to select between factor or fixed value. "
                )
                val extractedEventName = match.groupValues[1]
                eventActivationListener = EventActivationListener("event.$extractedEventName")
                listeners.add(eventActivationListener)
            } else {
                eventActivationListener = EventActivationListener(eventName)
                listeners.add(eventActivationListener)
            }
            //wrap event activation in a ==True comparison
            return ComparisonCauseNode(
                OperatorToken.EQ,
                temporalContext,
                eventActivationListener,
                ConstantValueProvider(true)
            )
        }
        throw UnsupportedOperationException(
            "Value %s cannot be interpreted as event Activation. Try wrapping it in parenthesis like '(%s)'.".format(
                valueNode.value,
                valueNode.value
            )
        )
    }

    private fun interpretAsValueProvider(valueNode: ValueASTNode): ValueListener<*> {
        if (valueNode.containsPropertyAccess()) {
            val listener = ValueListener<Int>(valueNode.value)
            listeners.add(listener)
            return listener
        }
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
                        interpretAsValueProvider(binaryNode.leftChild as ValueASTNode),
                        interpretAsValueProvider(binaryNode.rightChild as ValueASTNode)
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