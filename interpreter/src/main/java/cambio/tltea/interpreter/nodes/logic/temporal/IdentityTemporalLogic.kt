package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance
import java.util.concurrent.Delayed

class IdentityTemporalLogic(brokers: Brokers) :
    AbstractTemporalLogic(TemporalInterval(0.0, Double.POSITIVE_INFINITY), brokers) {
    lateinit var child: INode<Boolean, *>

    override fun on(event: InitializeNodeEvent) {
        val children = node.getChildren()
        assert(children.size >= 0)

        child = children.first()
        if (child.getNodeLogic().getLatestState()) {
            satisfactionState.addStartEvent(TimeInstance(0))
        }
    }

    override fun evaluate(at: TimeInstance) {
        val stateChange = this.node.getChildren()[0].getNodeLogic().getStateChange(at) ?: return
        if (stateChange.start) {
            onChildSatisfied(at, stateChange.isDelayed())
        } else {
            onChildUnsatisfied(at, stateChange.isDelayed())
        }
        updateCurrentTime(at)
    }

    private fun onChildSatisfied(time: TimeInstance, delayed: Boolean) {
        satisfactionState.addStartEvent(time, !delayed)
    }

    private fun onChildUnsatisfied(time: TimeInstance, delayed: Boolean) {
        satisfactionState.addEndEvent(time, delayed)
    }

    /*
    override fun on(event: StateChangeNodeEvent) {
        // TODO: handle end of round
        if (event.newValue) {
            satisfactionState.addStartEvent(event.getTime(), !event.delayed)
        } else {
            satisfactionState.addEndEvent(event.getTime(), event.delayed)
        }
    }
    */

}