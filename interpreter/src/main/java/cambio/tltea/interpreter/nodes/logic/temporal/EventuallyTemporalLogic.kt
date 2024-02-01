package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

class EventuallyTemporalLogic(
    temporalInterval: TemporalInterval = TemporalInterval(0.0, Double.POSITIVE_INFINITY), brokers: Brokers
) :
    AbstractTemporalLogic(temporalInterval, brokers) {
    //private val childSatisfaction = TimeEventLog()
    //private val eventualActivationPoints = TimeEventLog()
    //private val eventualCheckPoints = TimeEventLog()

    //private val eventualSatisfyPoints = TimeEventLog()

    override fun on(event: InitializeNodeEvent) {
        val childState = node.getChildren()[0].getNodeLogic().getLatestState()
        if (childState) {
            onChildSatisfied(TimeInstance(0))
        } else {
            onChildUnsatisfied(TimeInstance(0))
        }
    }

    /*
    override fun on(event: StateChangeNodeEvent) {
        if (event.newValue) {
            onChildSatisfied(event.getTime())
        } else {
            onChildUnsatisfied(event.getTime())
        }
    }
    */

    override fun evaluate(stateChange: TimeEvent) {
        // TODO: use event
        val time = stateChange.time
        val childLogic = this.node.getChildren()[0].getNodeLogic()
        if (stateChange == null) {
            //evaluate(at, childLogic.getState(at))
            if (childLogic.getState(time)) {
                onChildSatisfied(time)
            }
        } else {
            evaluate(time, stateChange.value)
        }
    }

    private fun evaluate(at: TimeInstance, childState: Boolean) {
        if (childState) {
            onChildSatisfied(at)
        } else {
            onChildUnsatisfied(at)
        }
    }

    private fun onChildSatisfied(time: TimeInstance) {
        val startTime = time.subtract(temporalInterval.end)
        val endTime = time.subtract(temporalInterval.start)

        satisfactionState.add(TimeEvent.start(startTime))
        satisfactionState.deleteEvent(startTime, endTime, false)
        // TODO: Remove
        // satisfactionState.addStartEvent(startTime)
        // satisfactionState.delayEndEvent(startTime, time.subtract(temporalInterval.start), false)

        if (!time.subtractOverflow(temporalInterval.start)) {
            val updateUntil = time.subtract(TimeInstance(temporalInterval.start))
            updateCurrentTime(updateUntil)
        }
        //publishUpdates(updateUntil)
    }

    private fun onChildUnsatisfied(time: TimeInstance) {
        val endTime = time.subtract(temporalInterval.start)
        satisfactionState.delayEvent(endTime, false)
        // satisfactionState.addEndEvent(endTime) TODO: Remove

        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(temporalInterval.end)
            updateCurrentTime(updateUntil)
        }
        //publishUpdates(updateUntil)
    }


}