package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.AndConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.ConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.OrConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.ActivationConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.activation.ValueEventConsequenceNode
import cambio.tltea.interpreter.utils.ASTManipulator
import cambio.tltea.parser.core.*
import cambio.tltea.parser.core.temporal.*


/**
 * Expects a future MTL formula.
 * <p>
 * Operators G[...] - Globally/Always F[...] - Finally/Eventually N[...] - Next
 * <p>
 * Binary R[...] - Release U[...] - Until
 * <p>
 * Not Allowed:
 * <p>
 * S[...] - Since H[...] - History (always in the past) P[...] - Past (Once in the Past) Back to
 * <p>
 * <p>
 * <p>
 * Simplifications:
 * <p>
 * F[...] α = TRUE U[...] β R[...] α = NOT β U[...] NOT α N[TimeInstance] α = FALSE U[...] α
 * <p>
 * <p>
 * NOT G NOT α = F α NOT F NOT α = G α
 *
 * @author Lion Wagner
 */

//until[x,y] => left is true till right is true
//Roughly, but not exactly G[x,y](!right -> left)
class ConsequenceInterpreter {

    /**
     * @param mtlRoot the root of the parsed MTL formula
     * @param triggerManager this parameter can be used to combine multiple {@link TriggerNotifier}s from previous interpretations
     */
    @JvmOverloads
    fun interpretAsMTL(mtlRoot: ASTNode, triggerManager: TriggerManager = TriggerManager()): ConsequenceDescription {

        val consequenceDescription = ConsequenceDescription(triggerManager)
        //gather initial temporal info
        val temporalContext = if (mtlRoot is ITemporalOperationInfoHolder) mtlRoot.toTemporalOperatorInfo()
        else TemporalOperatorInfo(OperatorToken.GLOBALLY, TemporalInterval(0.0, Double.POSITIVE_INFINITY))

        val consequenceAST = interpret(mtlRoot, temporalContext, consequenceDescription)
        consequenceDescription.consequenceAST = consequenceAST
        return consequenceDescription
    }


    /**
     * General matcher function
     */
    private fun interpret(
        node: ASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        return when (node) {
            is TemporalBinaryOperationASTNode -> interpretTemporalBinaryOperation(
                node,
                temporalContext,
                consequenceDescription
            )
            is TemporalUnaryOperationASTNode -> interpretTemporalUnaryOperation(
                node,
                temporalContext,
                consequenceDescription
            )
            is BinaryOperationASTNode -> interpretBinaryOperation(
                node,
                temporalContext,
                consequenceDescription
            )
            is UnaryOperationASTNode -> interpretUnaryOperation(
                node,
                temporalContext,
                consequenceDescription
            )
            is ValueASTNode -> interpretValueNode(node, temporalContext, consequenceDescription)
            else -> {
                throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
            }
        }

    }


    private fun interpretTemporalBinaryOperation(
        node: TemporalBinaryOperationASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        val temporalContext = node.toTemporalOperatorInfo()
        if (node.leftChild is ITemporalOperationInfoHolder || node.rightChild is ITemporalOperationInfoHolder) {
            throw IllegalArgumentException("Cannot interpret two consecutive temporal operators")
        }
        if (node.operator != OperatorToken.UNTIL) {
            throw IllegalArgumentException("Cannot interpret temporal operator ${node.operator}")
        }
        // UNTIL

        TODO("Implement until node interpretation")

    }

    private fun interpretTemporalUnaryOperation(
        node: TemporalUnaryOperationASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (node.child is ITemporalOperationInfoHolder) {
            throw IllegalArgumentException("Cannot interpret two consecutive temporal operators")
        }
        //TODO: for now the temporal context is simply passed down. In the future multiple temporal contexts may need to interact.
        //Prophecy, Globally, Finally, Next
        val temporalContext = node.toTemporalOperatorInfo()
        return interpret(node.child, temporalContext, consequenceDescription)
    }


    private fun interpretBinaryOperation(
        node: BinaryOperationASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {

        if (node is TemporalBinaryOperationASTNode) {
            return interpretTemporalBinaryOperation(node, temporalContext, consequenceDescription)
        } else if (OperatorToken.ComparisonOperatorTokens.contains(node.operator)) {
            //create consequence comparison node
            val leftChild = node.leftChild as ValueASTNode
            val rightChild = node.rightChild as ValueASTNode

            var targetEventHolder: ValueASTNode = leftChild
            var targetValueHolder: ValueASTNode = rightChild

            if (rightChild.containsEventName()) {
                targetEventHolder = rightChild
                targetValueHolder = leftChild
            } else if (!leftChild.containsEventName()) {
                throw IllegalArgumentException("Cannot interpret value event. Neither left nor right child is a target property or event.")
            }

            val targetValue = try {
                targetValueHolder.value.toDouble()
            } catch (e: NumberFormatException) {
                targetValueHolder.value
            }

            return ValueEventConsequenceNode(
                targetEventHolder.eventName,
                targetValue,
                node.operator,
                consequenceDescription.triggerManager,
                temporalContext
            )

        } else {
            val interpretShorthand =
                { child: ASTNode -> interpret(child, temporalContext, consequenceDescription) }
            when (node.operator) {
                OperatorToken.IMPLIES -> {
                    //create implication node
                    return interpretImplication(node, temporalContext, consequenceDescription)
                }
                OperatorToken.AND -> {
                    //create consequence "and" node
                    val children = ASTManipulator.flatten(node)
                    return AndConsequenceNode(
                        consequenceDescription.triggerManager,
                        temporalContext,
                        children.map { interpretShorthand(it) })
                }
                OperatorToken.OR -> {
                    //create consequence "or" node
                    val children = ASTManipulator.flatten(node)
                    return OrConsequenceNode(
                        consequenceDescription.triggerManager,
                        temporalContext,
                        children.map { interpretShorthand(it) })
                }
                OperatorToken.IFF -> {
                    //split <-> into <- and ->
                    //this only works in rare cases
                    return interpretBinaryOperation(
                        ASTManipulator.splitIFF(node),
                        temporalContext,
                        consequenceDescription
                    )
                }
                else -> {
                    throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
                }
            }
        }

    }

    private fun interpretUnaryOperation(
        node: UnaryOperationASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (node.operator == OperatorToken.NOT) {
            val child = node.child
            if (child is ValueASTNode) {
                val value = if (child.containsEventName()) child.eventName else child.value
                return ActivationConsequenceNodeFactory.getActivationConsequenceNode(
                    value,
                    consequenceDescription.triggerManager,
                    temporalContext,
                    true
                )
            } else if (child is UnaryOperationASTNode && child.operator == OperatorToken.NOT) {
                return interpret(
                    ASTManipulator.removeDoubleNot(node),
                    temporalContext,
                    consequenceDescription
                )
            } else {
                return interpret(
                    ASTManipulator.applyNot(node),
                    temporalContext,
                    consequenceDescription
                )
            }

        } else {
            throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
        }
    }

    private fun interpretValueNode(
        node: ValueASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ActivationConsequenceNode {
        if (node.containsEventName()) {
            return ActivationConsequenceNodeFactory.getActivationConsequenceNode(
                node.eventName,
                consequenceDescription.triggerManager,
                temporalContext
            )
        } else {
            throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
        }
    }

    private fun interpretImplication(
        root: BinaryOperationASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (root.operator != OperatorToken.IMPLIES) {
            throw IllegalArgumentException("Expected operator ${OperatorToken.IMPLIES}, but found ${root.operator}")
        }

        val left = root.leftChild
        val right = root.rightChild

        val cause = CauseInterpreter().interpretMTLCause(left, temporalContext)
        val consequence = ConsequenceInterpreter().interpretAsMTL(right, consequenceDescription.triggerManager)

        consequenceDescription.triggerManager.eventActivationListeners.addAll(cause.listeners)


        return ImplicationNode(
            cause,
            consequence,
            temporalContext,
            consequenceDescription.triggerManager,
        )
    }
}