package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.InitializeNodeEvent
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

class UntilTemporalLogic(
    temporalInterval: TemporalInterval = TemporalInterval(0.0, Double.POSITIVE_INFINITY), brokers: Brokers
) : AbstractTemporalLogic(temporalInterval, brokers) {
    private lateinit var conditionNode: INode<Boolean, Boolean>
    private lateinit var releaseNode: INode<Boolean, Boolean>
    private var tempConditionActive = false
    private var conditionActive = false
    private var conditionLongActive = false
    private var conditionActiveSince = TimeInstance(0)
    private var tempConditionActiveSince = TimeInstance(0)
    private var releaseActive = false
    private var tempReleaseActive = false

    public override fun on(event: InitializeNodeEvent) {
        conditionNode = node.getChildren()[0]
        releaseNode = node.getChildren()[1]

        val conditionNodeState = conditionNode.getNodeLogic().getLatestState()
        val releaseNodeState = releaseNode.getNodeLogic().getLatestState()

        if (conditionNodeState) {
            setConditionActive(TimeInstance(0))
        }
        if (releaseNodeState) {
            setReleaseActive()
        }
    }

    private fun updateState() {
        releaseActive = tempReleaseActive
        conditionActive = tempConditionActive
        conditionActiveSince = tempConditionActiveSince
    }

    /*
    override fun on(event: EndOfRoundNodeEvent) {
        val time = this.getCurrentTime()//event.getTime()
        prepareEndOfRound(time)

        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(temporalInterval.end)
            //publishUpdates(updateUntil)
            updateCurrentTime(updateUntil)
        }
    }*/

    private fun prepareEndOfRound(time: TimeInstance) {
        val conditionActiveChanged = tempConditionActive != conditionActive
        val releaseActiveChanged = tempReleaseActive != releaseActive

        updateState()
        checkAndApplyLateActivation(time, conditionActiveChanged)

        if (releaseActiveChanged) {
            if (releaseActive) {
                onReleaseSatisfied(time, conditionActiveChanged)
            } else {
                onReleaseUnsatisfied(time, conditionActiveChanged)
            }
        }
        if (conditionActiveChanged) {
            if (conditionActive) {
                onConditionSatisfied(time)
            } else {
                onConditionUnsatisfied(time, conditionActiveChanged)
            }
        }
    }

    private fun checkAndApplyLateActivation(time: TimeInstance, conditionActiveChanged: Boolean) {
        val oldConditionLongActive = conditionLongActive
        checkAndSetConditionActiveLong(time, conditionActiveChanged)

        if (conditionActive && releaseActive && !oldConditionLongActive && conditionLongActive) {
            val startTime = conditionActiveSince.add(temporalInterval.start)
            satisfactionState.add(TimeEvent.start(startTime))
            //satisfactionState.addStartEvent(startTime) TODO: remove
        }
    }

    /*
    override fun on(event: StateChangeNodeEvent) {
        when (event.publishingNode) {
            conditionNode -> {
                onConditionChanged(event)
            }

            releaseNode -> {
                onReleaseChanged(event)
            }

            else -> {
                throw IllegalArgumentException("Publishing node is not a child")
            }
        }

    }*/

    public override fun evaluate(stateChange: TimeEvent) {
        val time = stateChange.time
        val stateChangeRelease = this.node.getChildren()[1].getNodeLogic().getStateChange(time)
        if (stateChangeRelease != null) {
            onReleaseChanged(stateChangeRelease)
        }
        val stateChangeCondition = this.node.getChildren()[0].getNodeLogic().getStateChange(time)
        if (stateChangeCondition != null) {
            onConditionChanged(stateChangeCondition)
        }

        prepareEndOfRound(time)
        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(temporalInterval.end)
            //publishUpdates(updateUntil)
            updateCurrentTime(updateUntil)
        }
    }

    private fun onConditionChanged(stateChange: TimeEvent) {
        if (stateChange.value) {
            setConditionActive(stateChange.time)
        } else {
            setConditionInactive()
        }
        /*
        val time = event.getTime()
        if (event.newValue) {
            setConditionActive(time)
        } else {
            setConditionInactive()
        }
        */
    }

    private fun onConditionSatisfied(time: TimeInstance) {
        //setConditionActive(time)
    }

    private fun onConditionUnsatisfied(time: TimeInstance, conditionActiveChanged: Boolean) {
        if (releaseActive && (conditionActive || conditionActiveChanged) && conditionLongActive) {
            val endTime = time.subtract(temporalInterval.start)
            satisfactionState.add(TimeEvent.end(TimeInstance(endTime, true)))
            // satisfactionState.addEndEvent(endTime, true) TODO: remove
        }
    }

    private fun setConditionActive(time: TimeInstance) {
        if (!tempConditionActive) {
            tempConditionActive = true
            tempConditionActiveSince = time
        }
    }

    private fun setReleaseActive() {
        tempReleaseActive = true
    }

    private fun setReleaseInactive() {
        tempReleaseActive = false
    }

    private fun checkAndSetConditionActiveLong(time: TimeInstance, conditionActiveChanged: Boolean) {
        conditionLongActive = isConditionActiveLong(time, conditionActiveChanged)
    }

    private fun isConditionActiveLong(time: TimeInstance, conditionActiveChanged: Boolean): Boolean {
        return (conditionActive || conditionActiveChanged) && time >= conditionActiveSince.add(
            temporalInterval.start
        )
    }

    private fun setConditionInactive() {
        tempConditionActive = false
    }

    private fun onReleaseChanged(stateChange: TimeEvent) {
        if (stateChange.value) {
            setReleaseActive()
        } else {
            setReleaseInactive()
        }
        /*
        if (event.newValue) {
            setReleaseActive()
        } else {
            setReleaseInactive()
        }
        */
    }

    private fun onReleaseSatisfied(time: TimeInstance, conditionChanged: Boolean) {
        val earliestStartTime = time.subtract(temporalInterval.end)
        val startTime: TimeInstance = if ((conditionChanged || conditionActive) && conditionLongActive) {
            if (conditionActiveSince < earliestStartTime) {
                earliestStartTime
            } else {
                conditionActiveSince
            }
        } else if (temporalInterval.start.time == 0.0) {
            time
        } else {
            return
        }

        val startTimeEvent = TimeEvent.start(startTime)
        val maxEndTime = time.subtract(temporalInterval.start)
        satisfactionState.deleteEvent(startTime, maxEndTime, false)
        satisfactionState.add(startTimeEvent)
        // satisfactionState.addStartEvent(startTime) TODO: remove
    }

    private fun onReleaseUnsatisfied(time: TimeInstance, conditionActiveChanged: Boolean) {
        if ((conditionActive || conditionActiveChanged) && conditionLongActive) {
            val endTime = time.subtract(temporalInterval.start)
            satisfactionState.delayEvent(endTime, false)
            //satisfactionState.addEndEvent(endTime)
        } else if (temporalInterval.start.time == 0.0) {
            satisfactionState.delayEvent(time, false)
            //satisfactionState.addEndEvent(time)
        }

    }

}