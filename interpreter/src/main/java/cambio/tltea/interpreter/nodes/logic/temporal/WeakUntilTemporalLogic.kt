package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.nodes.events.*
import cambio.tltea.interpreter.nodes.logic.ILogic
import cambio.tltea.interpreter.nodes.logic.util.TimeEvent
import cambio.tltea.interpreter.nodes.logic.util.TimeEventLog
import cambio.tltea.interpreter.nodes.structure.INode
import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance
import java.sql.Time
import java.util.Optional

class WeakUntilTemporalLogic(
    temporalInterval: TemporalInterval = TemporalInterval(0.0, Double.POSITIVE_INFINITY), brokers: Brokers
) : AbstractTemporalLogic(temporalInterval, brokers), INode<Boolean, Boolean> {
    private val infinityTimeEvent = TimeEvent(TimeInstance(Double.POSITIVE_INFINITY), true)
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
        val alwaysEvents = alwaysOperatorEvents.findTimeEvents(fromTime, toTime).iterator()
        val untilEvents = untilOperatorEvents.findTimeEvents(fromTime, toTime).iterator()
        val iterator = SortedIterator(alwaysEvents, untilEvents)

        while (iterator.hasNext()) {
            val (source, event) = iterator.next()
            if (event.value) {
                satisfactionState.add(event)
            } else {
                if (source == alwaysEvents) {
                    if (!untilOperatorEvents.evaluate(event.time)) {
                        satisfactionState.add(event)
                    }
                } else {
                    if (!alwaysOperatorEvents.evaluate(event.time)) {
                        satisfactionState.add(event)
                    }
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

    /**
     * Merges two (sorted) time event iterators and always returns the chronological next event.
     */
    inner class SortedIterator(
        private val firstIterator: Iterator<TimeEvent>,
        private val secondIterator: Iterator<TimeEvent>
    ) : Iterator<Pair<Iterator<TimeEvent>, TimeEvent>> {
        private var firstNext: Optional<TimeEvent> = Optional.empty()
        private var secondNext: Optional<TimeEvent> = Optional.empty()

        init {
            getFirstNext()
            getSecondNext()
        }

        override fun hasNext(): Boolean {
            return firstNext.isPresent || secondNext.isPresent
        }

        override fun next(): Pair<Iterator<TimeEvent>, TimeEvent> {
            val firstTime = firstNext.orElse(infinityTimeEvent)
            val secondTime = secondNext.orElse(infinityTimeEvent)
            val next: TimeEvent
            val nextSource: Iterator<TimeEvent>
            if (firstTime <= secondTime) {
                next = firstNext.orElse(null)
                nextSource = firstIterator
                getFirstNext()
            } else {
                next = secondNext.orElse(null)
                nextSource = secondIterator
                getSecondNext()
            }
            return Pair(nextSource, next)
        }

        private fun getFirstNext() {
            firstNext = if (firstIterator.hasNext()) {
                Optional.of(firstIterator.next())
            } else {
                Optional.empty()
            }
        }

        private fun getSecondNext() {
            secondNext = if (secondIterator.hasNext()) {
                Optional.of(secondIterator.next())
            } else {
                Optional.empty()
            }
        }
    }

}