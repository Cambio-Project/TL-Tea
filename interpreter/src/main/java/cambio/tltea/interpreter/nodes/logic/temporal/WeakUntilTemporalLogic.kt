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
            // satisfactionState.addStartEvent(initializeTime) TODO: remove
        }
    }

    /*
    override fun on(event: EndOfRoundNodeEvent) {
        getAlwaysOperator().handle(event)
        getUntilOperator().handle(event)
        val time = event.getTime()//this.getCurrentTime()

        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(temporalInterval.end)
            updateCurrentTime(updateUntil)
            //publishUpdates(updateUntil)
        }

    }
*/

    override fun on(event: EndOfExperimentNodeEvent) {
        getUntilOperator().handle(event)
        getAlwaysOperator().handle(event)
        super.on(event)
    }


    /*
    override fun on(event: StateChangeNodeEvent) {
        getUntilOperator().handle(event)
        getAlwaysOperator().handle(event)
    }
    */

    override fun evaluate(stateChange: TimeEvent) {
        val time = stateChange.time//this.getCurrentTime()

        getUntilOperator().evaluate(stateChange)
        // For always: only evaluate changes on first part
        val stateChangeCondition = this.node.getChildren()[0].getNodeLogic().getStateChange(time)
        if (stateChangeCondition != null) {
            getAlwaysOperator().evaluate(stateChangeCondition)
        }

        // Does nothing
        //getAlwaysOperator().handle(event)
        //getUntilOperator().handle(event)

        if (!time.subtractOverflow(temporalInterval.end)) {
            val updateUntil = time.subtract(temporalInterval.end)
            updateCurrentTime(updateUntil)
            //publishUpdates(updateUntil)
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
                /*
                if (stateChange.value) {
                    activeEventLog.addStartEvent(stateChange.time, !stateChange.isDelayed())
                } else {
                    activeEventLog.addEndEvent(stateChange.time, stateChange.isDelayed())
                }
                */

            }


            /*
            if (event.newValue != event.oldValue) {
                if (event.newValue) {
                    activeEventLog.addStartEvent(event.getTime(), !event.delayed)
                } else {
                    activeEventLog.addEndEvent(event.getTime(), event.delayed)
                }
            }
            */
        } else {
            throw IllegalStateException("Weak Until Logic should never receive non-StateChangeNodeEvent $event")
        }
    }

    override fun updateCurrentTime(time: TimeInstance) {
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

    /*
    override fun publishUpdates(fromTime: TimeInstance, toTime: TimeInstance) {
        updateSatisfactionState(fromTime, toTime)
        super.publishUpdates(fromTime, toTime)
    }
    */

    private fun updateSatisfactionState(fromTime: TimeInstance, toTime: TimeInstance) {
        val happenedEvents = ArrayList<TimeEvent>()
        happenedEvents.addAll(alwaysOperatorEvents.findTimeInstanceMarkers(fromTime, toTime))
        happenedEvents.addAll(untilOperatorEvents.findTimeInstanceMarkers(fromTime, toTime))
        happenedEvents.sort()
        for (event in happenedEvents) {
            if (event.value) {
                satisfactionState.add(event)
                // satisfactionState.addStartEvent(event.time, event.including) TODO: remove
            } else {
                // TODO: consider including properly
                if ((untilOperatorEvents.contains(event) && !alwaysOperatorEvents.evaluate(event.time)) || (alwaysOperatorEvents.contains(
                        event
                    ) && !untilOperatorEvents.evaluate(event.time))
                ) {
                    satisfactionState.add(event)
                    // satisfactionState.addEndEvent(event.time, event.including) TODO: remove
                }
            }
        }
    }

    override fun getChildren(): MutableList<INode<Boolean, Boolean>> {
        return node.getChildren()
    }

    override fun getParent(): INode<Boolean, Boolean>? {
        return this // reroutes updates to parent to this instance
    }

    override fun getNodeLogic(): ILogic {
        return this
    }

}