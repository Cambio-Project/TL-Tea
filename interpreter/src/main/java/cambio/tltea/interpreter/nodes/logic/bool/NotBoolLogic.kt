package cambio.tltea.interpreter.nodes.logic.bool

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TimeInstance

class NotBoolLogic(brokers: Brokers) : AbstractBoolLogic(1, brokers) {

    override fun evaluateBool(at: TimeInstance) {
        val children = node.getChildren()
        val a = children[0].getNodeLogic().getState(at)
        val state = !a
        updateState(state, at)
    }
    /*
    lateinit var child: INode<Boolean, *>

    override fun on(event: InitializeNodeEvent) {
        val children = node.getChildren()
        assert(children.size == 1)

        this.child = children[0]
        satisfactionState.currentTempSatisfied = !child.getNodeLogic().getLatestState()
    }

    override fun on(event: StateChangeNodeEvent) {
        assert(child == event.publishingNode)
        assert(event.newValue != event.oldValue)

        satisfactionState.currentTempSatisfied = !event.newValue
    }

     */

}