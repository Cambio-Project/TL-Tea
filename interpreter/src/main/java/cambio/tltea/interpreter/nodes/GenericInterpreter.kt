package cambio.tltea.interpreter.nodes

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.factories.NodeFactoryProvider
import cambio.tltea.interpreter.nodes.cause.ValueListener
import cambio.tltea.interpreter.nodes.logic.temporal.IdentityTemporalLogic
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.interpreter.nodes.structure.TemporalStateNode
import cambio.tltea.parser.core.ASTNode
import cambio.tltea.parser.core.OperatorToken
import cambio.tltea.parser.core.ValueASTNode
import cambio.tltea.parser.core.temporal.TemporalOperatorInfo
import java.util.*

class GenericInterpreter(val brokers: Brokers) {
    private val listeners = mutableListOf<ValueListener<*>>()
    private val nodeFactoryProvider = NodeFactoryProvider(brokers, listeners)
    private val nodes = Stack<NodeInformation>()

    fun interpretMTLCause(
        root: ASTNode,
        temporalContext: TemporalOperatorInfo = TemporalOperatorInfo(OperatorToken.GLOBALLY, "[0,inf]")
    ): TreeDescription {
        val interpretedRoot: INode<Boolean, *> = interpretTree(root, temporalContext)
        return TreeDescription(interpretedRoot, listeners)
    }

    private fun interpretTree(root: ASTNode, temporalContext: TemporalOperatorInfo): INode<Boolean, Boolean> {
        val rootNode = createRootNode()
        brokers.timeManager.initialize(rootNode)
        nodes.push(NodeInformation(root, rootNode, temporalContext))
        transformNodes()
        return rootNode
    }

    private fun createRootNode(): INode<Boolean, Boolean> {
        return TemporalStateNode(null, IdentityTemporalLogic(brokers))
    }

    private fun transformNodes() {
        while (nodes.isNotEmpty()) {
            val currentNodeInformation = nodes.pop()
            val nodeFactory = nodeFactoryProvider.provideFactory(currentNodeInformation.nodeToTransform)
                ?: throw IllegalArgumentException("Unsupported ASTNode type: ${currentNodeInformation.nodeToTransform.javaClass.name}")
            val newNode = nodeFactory.interpretAsCause(
                currentNodeInformation.nodeToTransform,
                currentNodeInformation.transformedParent,
                currentNodeInformation.temporalContext
            )
            for (child in currentNodeInformation.nodeToTransform.children.reversed()) {
                if (child !is ValueASTNode) {
                    nodes.push(NodeInformation(child, newNode, currentNodeInformation.temporalContext))
                }
            }
        }
    }

    data class NodeInformation(
        val nodeToTransform: ASTNode,
        val transformedParent: INode<Boolean, Boolean>,
        val temporalContext: TemporalOperatorInfo
    )

}