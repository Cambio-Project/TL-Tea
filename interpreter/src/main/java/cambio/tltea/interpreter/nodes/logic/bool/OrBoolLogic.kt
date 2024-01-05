package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.parser.core.temporal.TimeInstance

class OrBoolLogic(brokers: Brokers) : AbstractBoolLogic(2, brokers) {


    /*
    private var falseCount: Int = 0

    override fun on(event: InitializeNodeEvent) {
        val children = node.getChildren()
        assert(children.size >= 2)

        for (child in children) {
            if (!child.getNodeLogic().getLatestState()) {
                falseCount++
            }
        }
        updateState()
    }

    override fun on(event: StateChangeNodeEvent) {
        assert(node.getChildren().contains(event.publishingNode))
        assert(event.newValue != event.oldValue)

        updateCounter(event.newValue)
        updateState()
    }

    private fun updateCounter(changeToTrue: Boolean) {
        if (changeToTrue) {
            falseCount--
        } else {
            falseCount++
        }
    }

    private fun updateState() {
        satisfactionState.currentTempSatisfied = (falseCount < node.getChildren().size)
    }
*/    override fun evaluateBool(at: TimeInstance) {
        val children = node.getChildren()
        val a = children[0].getNodeLogic().getState(at)
        val b = children[1].getNodeLogic().getState(at)
        val state = a || b
        updateState(state, at)
    }
}