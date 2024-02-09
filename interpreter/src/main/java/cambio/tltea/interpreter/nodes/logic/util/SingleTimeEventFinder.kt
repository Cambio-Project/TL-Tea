package cambio.tltea.interpreter.nodes.logic.util

import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Finds the event exactly at a time point.
 */
class SingleTimeEventFinder(events: List<TimeEvent>) :
    TimeEventFinder(events) {
    private val nullEvent = TimeEvent(TimeInstance(0), true)
    private lateinit var time: TimeInstance

    fun find(time: TimeInstance): Pair<Int, TimeEvent> {
        this.time = time
        return find()
    }

    override fun prepareEvaluation(index: Int, event: TimeEvent) {
        // do nothing
    }

    override fun searchLower(index: Int, event: TimeEvent): Boolean {
        return event.time > time
    }

    override fun searchHigher(index: Int, event: TimeEvent): Boolean {
        return event.time < time
    }

    override fun isSearchEvent(index: Int, event: TimeEvent): Boolean {
        return event.time == time
    }

    override fun returnNotFoundDefault(): TimeEvent {
        return nullEvent
    }


}