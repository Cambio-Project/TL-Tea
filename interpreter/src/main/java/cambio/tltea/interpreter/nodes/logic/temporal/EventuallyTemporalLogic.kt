package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

open class EventuallyTemporalLogic(
    temporalInterval: TemporalInterval = TemporalInterval(0.0, Double.POSITIVE_INFINITY), brokers: Brokers
) :
    AbstractTemporalLogic(temporalInterval, brokers) {

    override fun on(event: InitializeNodeEvent) {
        val childState = node.getChildren()[0].getNodeLogic().getLatestState()
        if (childState) {
            onChildSatisfied(TimeInstance(0))
        } else {
            onChildUnsatisfied(TimeInstance(0))
        }
    }

    override fun evaluate(changePoint: TimeEvent) {
        val time = changePoint.time
        evaluate(time, changePoint.value)
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

        if (!time.subtractOverflow(temporalInterval.start)) {
            val updateUntil = time.subtract(TimeInstance(temporalInterval.start))
            updateCurrentTime(updateUntil)
        }
    }

    private fun onChildUnsatisfied(time: TimeInstance) {
        val endTime = time.subtract(temporalInterval.start)
        satisfactionState.delayEvent(endTime, false)

        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(temporalInterval.end)
            updateCurrentTime(updateUntil)
        }
    }

}