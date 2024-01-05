package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.interpreter.nodes.structure.INode

class SatisfactionState(private val node: INode<*, *>) {
    var satisfiable: Boolean = true

    // TODO: needed?
    var currentSatisfied: Boolean = true
    var currentTempSatisfied: Boolean = true
        set(value) {
            if (value != field) {
                val oldValue = field
                field = value
                node.getParent()?.handle(StateChangeNodeEvent(node, false, node.getNodeLogic().getCurrentTime()))
                //node.getParent()?.handle(StateChangeNodeEvent(node, value, oldValue, false, node.getNodeLogic().getCurrentTime()))
            }
        }
}