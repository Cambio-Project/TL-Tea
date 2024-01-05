package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.EndOfExperimentNodeEvent
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.events.StateChangeNodeEvent
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

class AlwaysTemporalLogic(
    temporalInterval: TemporalInterval = TemporalInterval(0.0, Double.POSITIVE_INFINITY), brokers: Brokers
) : AbstractTemporalLogic(temporalInterval, brokers) {
    private var active = false
    private var longActive = false
    private var activeSince = TimeInstance(0)

    public override fun on(event: InitializeNodeEvent) {
        val childState = node.getChildren()[0].getNodeLogic().getLatestState()
        if (childState) {
            onChildSatisfied(TimeInstance(0))
        } else {
            onChildUnsatisfied(TimeInstance(0))
        }
    }

    /*
    override fun on(event: StateChangeNodeEvent) {
        handleChildUpdate(event.newValue, event.getTime())
    }

    private fun handleChildUpdate(childState: Boolean, time: TimeInstance) {
        if (childState) {
            onChildSatisfied(time)
        } else {
            onChildUnsatisfied(time)
        }
    }
    */

    public override fun evaluate(at: TimeInstance) {
        val stateChange = this.node.getChildren()[0].getNodeLogic().getStateChange(at) ?: return
        if (stateChange.start) {
            onChildSatisfied(at)
        } else {
            onChildUnsatisfied(at)
        }
    }

    override fun on(event: EndOfExperimentNodeEvent) {
        if (active) {
            if (duration == Double.POSITIVE_INFINITY) {
                onEndInfinityDuration(event.getTime())
            } else {
                onChildSatisfied(event.getTime())
            }
        }
        super.on(event)
    }

    private fun onEndInfinityDuration(time: TimeInstance) {
        val startTime = activeSince.subtract(temporalInterval.start)
        satisfactionState.addStartEvent(startTime)
    }

    private fun isLongActive(time: TimeInstance): Boolean {
        return time >= activeSince.add(TimeInstance(duration))
    }

    private fun handleStartEvent(time: TimeInstance) {
        if (!longActive && isLongActive(time)) {
            longActive = true
            val startTime = activeSince.subtract(temporalInterval.start)
            satisfactionState.addStartEvent(startTime)
        }
    }

    private fun handleEndEvent(time: TimeInstance) {
        if (longActive) {
            val endTime = time.subtract(temporalInterval.end)
            satisfactionState.addEndEvent(endTime)
        }
    }

    private fun setActive(time: TimeInstance) {
        if (!active) {
            active = true
            activeSince = time
        }
    }

    private fun setInactive() {
        active = false
        longActive = false
    }

    private fun onChildSatisfied(time: TimeInstance) {
        setActive(time)
        handleStartEvent(time)

        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(TimeInstance(temporalInterval.end))
            //publishUpdates(updateUntil)
            updateCurrentTime(updateUntil)
        }

    }

    private fun onChildUnsatisfied(time: TimeInstance) {
        handleStartEvent(time)
        handleEndEvent(time)
        setInactive()

        if (!time.subtractOverflow(temporalInterval.start)) {
            val updateUntil = time.subtract(temporalInterval.start)
            //publishUpdates(updateUntil)
            updateCurrentTime(updateUntil)
        }
    }

}