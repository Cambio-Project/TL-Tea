package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TimeInstance

class AndBoolLogic(brokers: Brokers) : AbstractBoolLogic(2, brokers) {

    override fun evaluateBool(at: TimeInstance) {
        val children = node.getChildren()
        val a = children[0].getNodeLogic().getState(at)
        val b = children[1].getNodeLogic().getState(at)
        val state = a && b
        updateState(state, at)
    }

    /*
    private var falseCount: Int = 0

    override fun evaluate(at: TimeInstance) {
        updateCounterFromChildren(at)
        updateState(at)
        updateCurrentTime(at)
    }

    override fun on(event: InitializeNodeEvent) {
        val children = node.getChildren()
        assert(children.size == 2)

        for (child in children) {
            if (!child.getNodeLogic().getLatestState()) {
                falseCount++
            }
        }
        updateState(event.getTime())
    }


    private fun updateCounterFromChildren(time: TimeInstance) {
        val children = node.getChildren()
        for (child in children) {
            updateCounterFromChild(child, time)
        }
    }

    private fun updateCounterFromChild(child: INode<Boolean, Boolean>, time: TimeInstance) {
        val changePoint = child.getNodeLogic().getStateChange(time)
        if (changePoint != null) {
            updateCounter(changePoint.start)
        }
    }

    private fun updateCounter(changeToTrue: Boolean) {
        if (changeToTrue) {
            falseCount--
        } else {
            falseCount++
        }
    }

    private fun updateState(time: TimeInstance) {
        val state = falseCount == 0
        updateState(state, time)
    }
    */
    /*
            override fun on(event: StateChangeNodeEvent) {
                assert(node.getChildren().contains(event.publishingNode))
                if (event.valueChanged()) {
                    updateCounter(event.newValue)
                    updateState()
                }
            }
        */
}