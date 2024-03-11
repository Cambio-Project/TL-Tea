package cambio.tltea.interpreter.nodes.logic.util

import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Finds the next event from a time point, excluding the time point itself.
 */
class NextEventExcludingFinder(private val events: List<TimeEvent>, private val startEvent: TimeEvent) :
    TimeEventFinder(events) {
    private val infinityEvent = TimeEvent(TimeInstance(Double.POSITIVE_INFINITY), true)
    private lateinit var fromPoint: TimeInstance
    private lateinit var previousEvent: TimeEvent

    fun find(fromPoint: TimeInstance): Pair<Int, TimeEvent> {
        this.fromPoint = fromPoint
        return find()
    }

    override fun prepareEvaluation(index: Int, event: TimeEvent) {
        previousEvent = if (index > 0) {
            events[index -1]
        } else {
            startEvent
        }
    }

    override fun searchLower(index: Int, event: TimeEvent): Boolean {
        return event.time > fromPoint && previousEvent.time > fromPoint
    }

    override fun searchHigher(index: Int, event: TimeEvent): Boolean {
        return event.time < fromPoint
    }

    override fun isSearchEvent(index: Int, event: TimeEvent): Boolean {
        return event.time > fromPoint && previousEvent.time <= fromPoint
    }

    override fun returnNotFoundDefault(): TimeEvent {
        return infinityEvent
    }


}