package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.parser.core.temporal.TimeInstance

class IffBoolLogic(brokers: Brokers) : AbstractBoolLogic(2, brokers) {

    /*
    override fun on(event: InitializeNodeEvent) {
        val children = node.getChildren()
        assert(children.size == 2)

        updateState()
    }

    override fun on(event: StateChangeNodeEvent) {
        assert(node.getChildren().contains(event.publishingNode))
        if (event.valueChanged()) {
            updateState()
        }
    }

    private fun updateState() {
        val children = node.getChildren()
        val leftChild = children[0].getNodeLogic().getLatestState()
        val rightChild = children[1].getNodeLogic().getLatestState()
        satisfactionState.currentTempSatisfied = (leftChild == rightChild)
    }
*/
    override fun evaluateBool(at: TimeInstance) {
        val children = node.getChildren()
        val a = children[0].getNodeLogic().getState(at)
        val b = children[1].getNodeLogic().getState(at)
        val state = a == b
        updateState(state, at)
    }
}