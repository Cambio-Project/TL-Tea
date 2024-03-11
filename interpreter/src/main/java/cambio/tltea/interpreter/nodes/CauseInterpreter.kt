package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.interpreter.nodes.cause.*
import cambio.tltea.interpreter.utils.ASTManipulator
import cambio.tltea.parser.core.*
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import cambio.tltea.parser.core.temporal.TemporalUnaryOperationASTNode

class CauseInterpreter(val brokers: Brokers) {

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
            val listener = ValueListener<Int>(valueNode.value, 0)
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

    // TODO: refactor
    private fun interpretAsValueProviders(binaryNode : BinaryOperationASTNode): ComparisonCauseNode<*> {
        val left = binaryNode.leftChild as ValueASTNode
        val right = binaryNode.rightChild as ValueASTNode

        if (left.containsPropertyAccess() && right.containsPropertyAccess()) {
            val listener = ValueListener(left.eventName,0.0)
            val listener2 = ValueListener(right.eventName,0.0)

            // TODO: this is highly fragile code
            val parts = left.eventName.split("\$")
            val parts2 = right.eventName.split("\$")

            val descriptor = MetricDescriptor(parts[0], parts[1])
            val descriptor2 = MetricDescriptor(parts2[0], parts2[1])

            brokers.metricBroker.register(descriptor, listener)
            brokers.metricBroker.register(descriptor2, listener2)

            listeners.add(listener)
            listeners.add(listener2)
            return ComparisonCauseNode(
                binaryNode.operator,
                null,
                listener,
                listener2
            )

        }else{
            try {
                val d = left.value.toDouble()
                val d2 = right.value.toDouble()

                return ComparisonCauseNode(
                    binaryNode.operator,
                    null,
                    ConstantValueProvider(d),
                    ConstantValueProvider(d2)
                )
            } catch (e: NumberFormatException) {
                return ComparisonCauseNode(
                    binaryNode.operator,
                    null,
                    ConstantValueProvider(left.value),
                    ConstantValueProvider(right.value)
                )
            }
        }



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
                    return interpretAsValueProviders(binaryNode)
                    /*return ComparisonCauseNode(
                        binaryNode.operator,
                        null,
                        interpretAsValueProvider(binaryNode.leftChild as ValueASTNode),
                        interpretAsValueProvider(binaryNode.rightChild as ValueASTNode)
                    )*/
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