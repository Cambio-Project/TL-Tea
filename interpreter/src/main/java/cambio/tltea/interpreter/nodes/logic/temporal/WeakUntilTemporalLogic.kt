package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.*
import cambio.tltea.interpreter.nodes.logic.ILogic
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.interpreter.nodes.logic.util.TimeEventLog
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

class WeakUntilTemporalLogic(
    temporalInterval: TemporalInterval = TemporalInterval(0.0, Double.POSITIVE_INFINITY), brokers: Brokers
) : AbstractTemporalLogic(temporalInterval, brokers), INode<Boolean, Boolean> {
    private val untilOperator = UntilTemporalLogic(temporalInterval, brokers)
    private val alwaysOperator = AlwaysTemporalLogic(temporalInterval, brokers)
    private val untilOperatorEvents = TimeEventLog()
    private val alwaysOperatorEvents = TimeEventLog()
    private var activeEventLog: TimeEventLog = untilOperatorEvents
    private var lastUpdateTime: TimeInstance = TimeInstance(-1)
    private var lastAlwaysUpdate: TimeInstance = TimeInstance(-1)
    private var lastUntilUpdate: TimeInstance = TimeInstance(-1)

    init {
        untilOperator.initialize(this)
        alwaysOperator.initialize(this)
    }

    private fun getUntilOperator(): UntilTemporalLogic {
        activeEventLog = untilOperatorEvents
        return untilOperator
    }

    private fun getAlwaysOperator(): AlwaysTemporalLogic {
        activeEventLog = alwaysOperatorEvents
        return alwaysOperator
    }

    override fun on(event: InitializeNodeEvent) {
        getUntilOperator().on(event)
        getAlwaysOperator().on(event)
        val initializeTime = TimeInstance(0)
        if (getUntilOperator().getState(initializeTime) || getAlwaysOperator().getState(initializeTime)) {
            satisfactionState.add(TimeEvent.start(initializeTime))
        }
    }

    override fun on(event: EndOfExperimentNodeEvent) {
        getUntilOperator().handle(event)
        getAlwaysOperator().handle(event)
        super.on(event)
    }

    override fun evaluate(changePoint: TimeEvent) {
        val time = changePoint.time

        getUntilOperator().evaluate(changePoint)
        // For always: only evaluate changes on first part
        val stateChangeCondition = this.node.getChildren()[0].getNodeLogic().getStateChange(time)
        if (stateChangeCondition != null) {
            getAlwaysOperator().evaluate(stateChangeCondition)
        }

        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(temporalInterval.end)
            updateCurrentTime(updateUntil)
        }
    }

    override fun handle(event: INodeEvent) {
        if (event is StateChangeNodeEvent) {
            val stateChanges: List<TimeEvent>
            if (activeEventLog == untilOperatorEvents) {
                stateChanges = untilOperator.getStateChanges(lastUntilUpdate, event.getTime())
                lastUntilUpdate = event.getTime()
            } else {
                stateChanges = alwaysOperator.getStateChanges(lastAlwaysUpdate, event.getTime())
                lastAlwaysUpdate = event.getTime()
            }
            for (stateChange in stateChanges) {
                activeEventLog.add(stateChange)
            }
        } else {
            throw IllegalStateException("Weak Until Logic should never receive non-StateChangeNodeEvent $event")
        }
    }

    override fun updateCurrentTime(currentTime: TimeInstance) {
        val alwaysTime = alwaysOperator.getCurrentTime()
        val untilTime = untilOperator.getCurrentTime()
        val timeToUpdateTo = if (alwaysTime > untilTime) {
            untilTime
        } else {
            alwaysTime
        }
        updateSatisfactionState(lastUpdateTime, timeToUpdateTo)
        if (timeToUpdateTo > lastUpdateTime) {
            lastUpdateTime = timeToUpdateTo
        }
        super.updateCurrentTime(timeToUpdateTo)
    }

    private fun updateSatisfactionState(fromTime: TimeInstance, toTime: TimeInstance) {
        val happenedEvents = ArrayList<TimeEvent>()
        happenedEvents.addAll(alwaysOperatorEvents.findTimeEvents(fromTime, toTime))
        happenedEvents.addAll(untilOperatorEvents.findTimeEvents(fromTime, toTime))
        happenedEvents.sort()
        for (event in happenedEvents) {
            if (event.value) {
                satisfactionState.add(event)
            } else {
                // TODO: consider including properly
                if ((untilOperatorEvents.contains(event) && !alwaysOperatorEvents.evaluate(event.time)) || (alwaysOperatorEvents.contains(
                        event
                    ) && !untilOperatorEvents.evaluate(event.time))
                ) {
                    satisfactionState.add(event)
                }
            }
        }
    }

    override fun getChildren(): MutableList<INode<Boolean, Boolean>> {
        return node.getChildren()
    }

    override fun getParent(): INode<Boolean, Boolean> {
        return this // reroutes updates to parent to this instance
    }

    override fun getNodeLogic(): ILogic {
        return this
    }

}