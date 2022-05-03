package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.*
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
     * @param triggerNotifier this parameter can be used to combine multiple {@link TriggerNotifier}s from previous interpretations
     */
    @JvmOverloads
    fun interpretAsMTL(mtlRoot: ASTNode, triggerNotifier: TriggerNotifier = TriggerNotifier()): ConsequenceDescription {

        val consequenceDescription = ConsequenceDescription(triggerNotifier)
        //gather initial temporal info
        val temporalContext = if (mtlRoot is ITemporalOperationInfoHolder) mtlRoot.toTemporalOperatorInfo()
        else TemporalOperatorInfo(OperatorToken.GLOBALLY, TemporalInterval(0.0, Double.POSITIVE_INFINITY))

        val consequenceAST = interpret(mtlRoot, triggerNotifier, temporalContext, consequenceDescription)
        consequenceDescription.consequenceAST = consequenceAST
        return consequenceDescription
    }


    /**
     * General matcher function
     */
    private fun interpret(
        node: ASTNode,
        triggerNotifier: TriggerNotifier,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        return when (node) {
            is TemporalBinaryOperationASTNode -> interpretTemporalBinaryOperation(
                node,
                triggerNotifier,
                temporalContext,
                consequenceDescription
            )
            is TemporalUnaryOperationASTNode -> interpretTemporalUnaryOperation(
                node,
                triggerNotifier,
                temporalContext,
                consequenceDescription
            )
            is BinaryOperationASTNode -> interpretBinaryOperation(
                node,
                triggerNotifier,
                temporalContext,
                consequenceDescription
            )
            is UnaryOperationASTNode -> interpretUnaryOperation(
                node,
                triggerNotifier,
                temporalContext,
                consequenceDescription
            )
            is ValueASTNode -> interpretValueNode(node, triggerNotifier, temporalContext, consequenceDescription)
            else -> {
                throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
            }
        }

    }


    private fun interpretTemporalBinaryOperation(
        node: TemporalBinaryOperationASTNode,
        triggerNotifier: TriggerNotifier,
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
        triggerNotifier: TriggerNotifier,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (node.child is ITemporalOperationInfoHolder) {
            throw IllegalArgumentException("Cannot interpret two consecutive temporal operators")
        }
        //TODO: for now the temporal context is simply passed down. In the future multiple temporal contexts may need to interact.
        //Prophecy, Globally, Finally, Next
        val temporalContext = node.toTemporalOperatorInfo()
        return interpret(node.child, triggerNotifier, temporalContext, consequenceDescription)
    }


    private fun interpretBinaryOperation(
        node: BinaryOperationASTNode,
        triggerNotifier: TriggerNotifier,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {

        if (node is TemporalBinaryOperationASTNode) {
            return interpretTemporalBinaryOperation(node, triggerNotifier, temporalContext, consequenceDescription)
        } else if (OperatorToken.ComparisonOperatorTokens.contains(node.operator)) {
            //create consequnce comparison node
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

            return ValueEventConsequenceNode(
                triggerNotifier,
                targetEventHolder.eventName,
                targetValueHolder.value,
                temporalContext
            )

        } else {
            val interpretShorthand =
                { child: ASTNode -> interpret(child, triggerNotifier, temporalContext, consequenceDescription) }
            when (node.operator) {
                OperatorToken.IMPLIES -> {
                    //create implication node
                    return interpretImplication(node, triggerNotifier, temporalContext)
                }
                OperatorToken.AND -> {
                    //create consequence "and" node
                    val children = ASTManipulator.flatten(node)
                    return AndConsequenceNode(triggerNotifier, temporalContext, children.map { interpretShorthand(it) })
                }
                OperatorToken.OR -> {
                    //create consequence "or" node
                    val children = ASTManipulator.flatten(node)
                    return OrConsequenceNode(triggerNotifier, temporalContext, children.map { interpretShorthand(it) })
                }
                OperatorToken.IFF -> {
                    //split <-> into <- and ->
                    //this only works in rare cases
                    return interpretBinaryOperation(
                        ASTManipulator.splitIFF(node),
                        triggerNotifier,
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
        triggerNotifier: TriggerNotifier,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (node.operator == OperatorToken.NOT) {
            val child = node.child
            if (child is ValueASTNode) {
                val value = if (child.containsEventName()) child.eventName else child.value
                return EventPreventionConsequenceNode(triggerNotifier, temporalContext, value)
            } else if (child is UnaryOperationASTNode && child.operator == OperatorToken.NOT) {
                return interpret(
                    ASTManipulator.removeDoubleNot(node),
                    triggerNotifier,
                    temporalContext,
                    consequenceDescription
                )
            } else {
                return interpret(
                    ASTManipulator.applyNot(node),
                    triggerNotifier,
                    temporalContext,
                    consequenceDescription
                )
            }

        } else {
            throw IllegalArgumentException("Cannot  interpret node of type ${node.javaClass.simpleName}")
        }
    }

    private fun interpretValueNode(
        node: ValueASTNode,
        triggerNotifier: TriggerNotifier,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ActivationConsequenceNode {
        if (node.containsEventName()) {
            return EventActivationConsequenceNode(triggerNotifier, temporalContext, node.eventName)
        } else {
            throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
        }
    }

    private fun interpretImplication(
        root: BinaryOperationASTNode,
        triggerNotifier: TriggerNotifier,
        temporalContext: TemporalOperatorInfo
    ): ConsequenceNode {
        if (root.operator != OperatorToken.IMPLIES) {
            throw IllegalArgumentException("Expected operator ${OperatorToken.IMPLIES}, but found ${root.operator}")
        }

        val left = root.leftChild
        val right = root.rightChild

        val cause = CauseInterpreter().interpretLTL(left)
        val consequence = ConsequenceInterpreter().interpretAsMTL(right, triggerNotifier)

        return ImplicationNode(
            cause,
            consequence,
            temporalContext,
            triggerNotifier
        )
    }
}