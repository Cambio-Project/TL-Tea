package cambio.tltea.interpreter.nodes.logic.util

import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Finds the event before a time point, excluding the time point itself.
 */
class PreviousEventIncludingFinder(private val events: List<TimeEvent>, private val startEvent: TimeEvent) :
    TimeEventFinder(events) {
    private val infinityEvent = TimeEvent(TimeInstance(Double.POSITIVE_INFINITY), true)
    private lateinit var toPoint: TimeInstance
    private lateinit var nextEvent: TimeEvent

    fun find(toPoint: TimeInstance): Pair<Int, TimeEvent> {
        this.toPoint = toPoint
        return find()
    }

    override fun prepareEvaluation(index: Int, event: TimeEvent) {
        nextEvent = if (index < events.size - 1) {
            events[index + 1]
        } else {
            infinityEvent
        }
    }

    override fun searchLower(index: Int, event: TimeEvent): Boolean {
        return event.time > toPoint
    }

    override fun searchHigher(index: Int, event: TimeEvent): Boolean {
        return event.time <= toPoint && nextEvent.time <= toPoint
    }

    override fun isSearchEvent(index: Int, event: TimeEvent): Boolean {
        return event.time <= toPoint && nextEvent.time > toPoint
    }

    override fun returnNotFoundDefault(): TimeEvent {
        return startEvent
    }


}