package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

class IdentityTemporalLogic(brokers: Brokers) :
    AbstractTemporalLogic(TemporalInterval(0.0, Double.POSITIVE_INFINITY), brokers) {
    lateinit var child: INode<Boolean, *>

    override fun on(event: InitializeNodeEvent) {
        val children = node.getChildren()
        assert(children.size >= 0)

        child = children.first()
        if (child.getNodeLogic().getLatestState()) {
            satisfactionState.add(TimeEvent.start(TimeInstance(0)))
            // satisfactionState.addStartEvent(TimeInstance(0))  TODO: remove
        }
    }

    override fun evaluate(stateChange: TimeEvent) {
        val time = stateChange.time
        if (stateChange.value) {
            onChildSatisfied(time)
        } else {
            onChildUnsatisfied(time)
        }
        updateCurrentTime(time)
    }

    private fun onChildSatisfied(time: TimeInstance) {
        satisfactionState.add(TimeEvent.start(time))
        // satisfactionState.addStartEvent(time, !delayed) TODO: remove
    }

    private fun onChildUnsatisfied(time: TimeInstance) {
        satisfactionState.add(TimeEvent.end(time))
        // satisfactionState.addEndEvent(time, delayed) TODO: remove
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