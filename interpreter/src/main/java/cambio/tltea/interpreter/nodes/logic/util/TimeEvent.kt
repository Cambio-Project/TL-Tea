package cambio.tltea.interpreter.nodes.logic.util

import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * A time event consisting of a point in time and a value representing a property satisfaction.
 * Such an event can be interpreted as the start of an interval until another event occurs.
 */
data class TimeEvent(val time: TimeInstance, val value: Boolean) : Comparable<TimeEvent> {
    companion object {
        fun start(time: TimeInstance): TimeEvent = TimeEvent(time, true)
        fun end(time: TimeInstance): TimeEvent = TimeEvent(time, false)
    }

    override fun compareTo(other: TimeEvent): Int {
        return time.compareTo(other.time)
    }
}