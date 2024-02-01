package cambio.tltea.interpreter.nodes.logic.util

import cambio.tltea.parser.core.temporal.TimeInstance
import java.util.*

class TimeEvent(val time: TimeInstance, val value: Boolean) : Comparable<TimeEvent> {
    companion object {
        fun start(time: TimeInstance): TimeEvent = TimeEvent(time, true)
        fun end(time: TimeInstance): TimeEvent = TimeEvent(time, false)
    }

    override fun compareTo(other: TimeEvent): Int {
        return time.compareTo(other.time)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false;
        if (this.javaClass != other.javaClass) return false;
        other as TimeEvent
        return this.time == other.time && this.value == other.value
    }

    override fun hashCode(): Int {
        return Objects.hash(time, value)
    }
}