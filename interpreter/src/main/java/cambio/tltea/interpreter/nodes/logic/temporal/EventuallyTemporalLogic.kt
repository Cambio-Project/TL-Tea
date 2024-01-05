package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
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

    override fun evaluate(at: TimeInstance) {
        val childLogic = this.node.getChildren()[0].getNodeLogic()
        val stateChange = childLogic.getStateChange(at)
        if (stateChange == null) {
            //evaluate(at, childLogic.getState(at))
            if(childLogic.getState(at)){
                onChildSatisfied(at)
            }
        } else {
            evaluate(at, stateChange.start)
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
        satisfactionState.addStartEvent(startTime)
        satisfactionState.delayEndEvent(startTime, time.subtract(temporalInterval.start), false)

        if (!time.subtractOverflow(temporalInterval.start)) {
            val updateUntil = time.subtract(TimeInstance(temporalInterval.start))
            updateCurrentTime(updateUntil)
        }
        //publishUpdates(updateUntil)
    }

    private fun onChildUnsatisfied(time: TimeInstance) {
        val endTime = time.subtract(temporalInterval.start)
        satisfactionState.addEndEvent(endTime)

        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(temporalInterval.end)
            updateCurrentTime(updateUntil)
        }
        //publishUpdates(updateUntil)
    }


}