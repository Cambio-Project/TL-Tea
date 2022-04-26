package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.nodes.consequence.ConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.EventActivationConsequenceNode
import cambio.tltea.interpreter.nodes.consequence.EventPreventionConsequenceNode
import cambio.tltea.interpreter.utils.ASTManipulator
import cambio.tltea.parser.core.*
import cambio.tltea.parser.core.temporal.*


//until => left is true till right is true
class ConsequenceInterpreter private constructor(private val triggerNotifier: TriggerNotifier) {

    constructor() : this(TriggerNotifier())


    fun interpretAsMTL(mtlRoot: ASTNode): ConsequenceDescription {

        val consequenceDescription = ConsequenceDescription()
        //gather initial temporal info
        val temporalContext = if (mtlRoot is ITemporalOperationInfoHolder) mtlRoot.toTemporalOperatorInfo()
        else TemporalOperatorInfo(OperatorToken.GLOBALLY, TemporalInterval(0.0, Double.POSITIVE_INFINITY))

        val consequnceAST = interpret(mtlRoot, temporalContext, consequenceDescription)
        consequenceDescription.consequenceAST = consequnceAST
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
            is TemporalBinaryOperationASTNode -> interpret(node, temporalContext, consequenceDescription)
            is TemporalUnaryOperationASTNode -> interpret(node, temporalContext, consequenceDescription)
            is BinaryOperationASTNode -> interpret(node, temporalContext, consequenceDescription)
            is UnaryOperationASTNode -> interpret(node, temporalContext, consequenceDescription)
            is ValueASTNode -> interpret(node, temporalContext, consequenceDescription)
            else -> {
                throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
            }
        }

    }


    private fun interpret(
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


    }

    private fun interpret(
        node: TemporalUnaryOperationASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (node.child is ITemporalOperationInfoHolder) {
            throw IllegalArgumentException("Cannot interpret two consecutive temporal operators")
        }
        //Prophecy, Globally, Finally, Next

    }


    private fun interpret(
        node: BinaryOperationASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (node is TemporalBinaryOperationASTNode) {
            return interpret(node, temporalContext, consequenceDescription)
        } else if (OperatorToken.ComparisonOperatorTokens.contains(node.operator)) {
            //create consequnce comparison node
        } else when (node.operator) {
            OperatorToken.IMPLIES -> {
                //create implication node
                return interpretImplication(node, temporalContext, consequenceDescription)
            }
            OperatorToken.AND -> {
                //create consequence and node
            }
            OperatorToken.OR -> {
                //create consequence or node
            }
            OperatorToken.IFF -> {
                //split <-> into <- and ->
                return interpret(ASTManipulator.splitIFF(node), temporalContext, consequenceDescription)
            }
            else -> {
                throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
            }
        }

    }

    private fun interpret(
        node: UnaryOperationASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (node.operator == OperatorToken.NOT) {
            val child = node.child
            if (child is ValueASTNode) {
                var value = child.value;
                if (child.value.startsWith("(") && child.value.endsWith(")")) {
                    value = child.value.substring(1, child.value.length - 1)
                }
                return EventPreventionConsequenceNode(triggerNotifier, temporalContext, value)
            } else if (child is UnaryOperationASTNode && child.operator == OperatorToken.NOT) {
                interpret(ASTManipulator.removeDoubleNot(node), temporalContext, consequenceDescription)
            } else {
                interpret(ASTManipulator.applyNot(node), temporalContext, consequenceDescription)
            }

        } else {
            throw IllegalArgumentException("Cannot  interpret node of type ${node.javaClass.simpleName}")
        }
    }

    private fun interpret(
        node: ValueASTNode,
        temporalContext: TemporalOperatorInfo,
        consequenceDescription: ConsequenceDescription
    ): ConsequenceNode {
        if (node.value.startsWith("(") && node.value.endsWith(")")) {
            val value = node.value.substring(1, node.value.length - 1)
            return EventActivationConsequenceNode(triggerNotifier, temporalContext, value)
        } else {
            throw IllegalArgumentException("Cannot interpret node of type ${node.javaClass.simpleName}")
        }
    }

    private fun interpretImplication(
        root: BinaryOperationASTNode,
        temporalContext: TemporalOperatorInfo
    ): ConsequenceNode {
        if (root.operator != OperatorToken.IMPLIES) {
            throw IllegalArgumentException("Expected operator ${OperatorToken.IMPLIES}, but found ${root.operator}")
        }

        val left = root.leftChild
        val right = root.rightChild

        val cause = CauseInterpreter().interpretLTL(left)
        val consequence = ConsequenceInterpreter(triggerNotifier).interpretAsMTL(right)

        return ImplicationNode(
            cause,
            consequence,
            temporalContext,
            triggerNotifier
        )
    }
}