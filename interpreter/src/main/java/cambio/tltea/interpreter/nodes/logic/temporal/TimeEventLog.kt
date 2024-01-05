package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

open class TimeEventLog {
    protected val events: MutableList<RangeTimeInstance> = mutableListOf()

    fun contains(event: RangeTimeInstance): Boolean {
        return events.contains(event)
    }

    fun evaluate(time: TimeInstance): Boolean {
        val previousEvent = findPreviousEvent(time)
        return previousEvent.start
    }

    fun addStartEvent(time: TimeInstance) {
        addStartEvent(time, true)
    }

    fun addStartEvent(time: TimeInstance, including: Boolean) {
        val event = RangeTimeInstance(time, true, including)
        var startEventIndex = setupEvent(event)
        startEventIndex = handleEqualEventBefore(startEventIndex)
        handleEqualEventAfter(startEventIndex)
        handleEventBeforeNewStart(startEventIndex)
        startEventIndex = events.indexOf(event)
        if (startEventIndex >= 0) {
            handleEventAfterNewStart(startEventIndex)
        }
    }

    fun delayEndEvent(from: TimeInstance, to: TimeInstance) {
        val rangeTimeInstance = findNextEnd(from)
        if (!isAfter(to, rangeTimeInstance)) {
            events.remove(rangeTimeInstance)
            addEndEvent(to, rangeTimeInstance.including)
        }
    }

    fun delayEndEvent(from: TimeInstance, to: TimeInstance, including: Boolean) {
        val rangeTimeInstance = findNextEnd(from)
        if (!isAfter(to, rangeTimeInstance)) {
            events.remove(rangeTimeInstance)
            addEndEvent(to, including)
        }
    }

    private fun handleEventBeforeNewStart(startEventIndex: Int) {
        if (startEventIndex > 0) {
            val previousEvent = events[startEventIndex - 1]
            if (previousEvent.start) {
                events.removeAt(startEventIndex)
            }
        }
    }

    private fun handleEventAfterNewStart(startEventIndex: Int) {
        val startEvent = events[startEventIndex]
        if (startEventIndex < events.lastIndex) {
            val nextEvent = events[startEventIndex + 1]
            if (nextEvent.start) {
                events.removeAt(startEventIndex + 1)
            } else if (zeroTimeInterval(startEvent, nextEvent)) {
                events.removeAt(startEventIndex)
                events.removeAt(startEventIndex)
            }
        }
    }

    fun addEndEvent(time: TimeInstance) {
        addEndEvent(time, false)
    }

    fun addEndEvent(time: TimeInstance, including: Boolean) {
        val event = RangeTimeInstance(time, false, including)
        var endEventIndex = setupEvent(event)
        endEventIndex = handleEqualEventBefore(endEventIndex)
        handleEqualEventAfter(endEventIndex)
        handleEventBeforeNewEnd(endEventIndex)
        endEventIndex = events.indexOf(event)
        if (endEventIndex >= 0) {
            handleEventAfterNewEnd(endEventIndex)
        }
    }

    fun deleteEvent(time: TimeInstance, start: Boolean) {
        for (event in findRangeTimeInstances(time, time)) {
            if (event.start == start) {
                events.remove(event)
            }
        }
    }

    private fun handleEqualEventBefore(eventIndex: Int): Int {
        val event = events[eventIndex]
        if (eventIndex > 0) {
            val previousEvent = events[eventIndex - 1]

            if (previousEvent.compareTo(event) == 0) {
                events.removeAt(eventIndex - 1)
                return eventIndex - 1
            }
        }
        return eventIndex
    }

    private fun handleEventBeforeNewEnd(endEventIndex: Int) {
        val endEvent = events[endEventIndex]
        if (endEventIndex > 0) {
            val previousEvent = events[endEventIndex - 1]

            if (!previousEvent.start) {
                events.removeAt(endEventIndex - 1)
            } else if (zeroTimeInterval(previousEvent, endEvent)) {
                events.removeAt(endEventIndex - 1)
                events.removeAt(endEventIndex - 1)
            }
        }
    }

    private fun handleEqualEventAfter(eventIndex: Int) {
        val event = events[eventIndex]
        if (eventIndex < events.lastIndex) {
            val nextEvent = events[eventIndex + 1]
            if (nextEvent.compareTo(event) == 0) {
                events.removeAt(eventIndex)
            }
        }
    }

    private fun handleEventAfterNewEnd(endEventIndex: Int) {
        if (endEventIndex < events.lastIndex) {
            val nextEvent = events[endEventIndex + 1]
            if (!nextEvent.start) {
                events.removeAt(endEventIndex)
            }
        }
    }

    fun findIntervals(): List<TemporalInterval> {
        val intervalPoints: MutableList<RangeTimeInstance> = events.toMutableList()

        if (intervalPoints.isNotEmpty()) {
            //if (!intervalPoints.first().start) {
            //    intervalPoints.add(0, RangeTimeInstance(TimeInstance(0), true, true))
            //}
            if (intervalPoints.last().start) {
                intervalPoints.add(RangeTimeInstance(TimeInstance(Double.POSITIVE_INFINITY), false, true))
            }
        }

        val intervals: MutableList<TemporalInterval> = mutableListOf()
        for (i in 1..intervalPoints.lastIndex step 2) {
            val startEvent = intervalPoints[i - 1]
            val endEvent = intervalPoints[i]
            assert(startEvent.start)
            assert(!endEvent.start)
            intervals.add(
                TemporalInterval(
                    startEvent.time.time,
                    endEvent.time.time,
                    startEvent.including,
                    endEvent.including
                )
            )
        }
        return intervals
    }

    fun isActiveAt(pointInTime: TimeInstance): Boolean {
        val intervals = this.findIntervals()
        for (interval in intervals) {
            if (interval.contains(pointInTime)) {
                return true
            }
        }
        return false
    }

    fun findRangeTimeInstance(at: TimeInstance): RangeTimeInstance? {
        for (event in events) {
            if (event.time == at) {
                return event
            }
        }
        return null
    }

    fun findRangeTimeInstances(from: TimeInstance, to: TimeInstance): List<RangeTimeInstance> {
        val list = ArrayList<RangeTimeInstance>()
        if (from >= to) {
            return list
        }
        for (event in events.reversed()) {
            if (isBeforeEquals(to, event)) {
                if (/*from.time != 0.0 &&*/ isBefore(from, event)) {
                    break
                } else {
                    list.add(event)
                }
            }
        }
        return list.reversed()
    }

    private fun findPreviousEvent(toPoint: TimeInstance): RangeTimeInstance {
        var previousTimeInstance = RangeTimeInstance(TimeInstance(0), false, false)
        for (currentTimeInstance in events) {
            if (!isBefore(toPoint, currentTimeInstance)) {
                break
            }
            previousTimeInstance = currentTimeInstance
        }
        return previousTimeInstance
    }

    fun findNextStart(fromPoint: TimeInstance): RangeTimeInstance {
        for (currentTimeInstance in events) {
            if (currentTimeInstance.start && isAfter(fromPoint, currentTimeInstance)) {
                return currentTimeInstance
            }
        }
        return RangeTimeInstance(TimeInstance(Double.POSITIVE_INFINITY), true, false)
    }

    fun findNextEnd(fromPoint: TimeInstance): RangeTimeInstance {
        for (currentTimeInstance in events) {
            if (!currentTimeInstance.start && isAfter(fromPoint, currentTimeInstance)) {
                return currentTimeInstance
            }
        }
        return RangeTimeInstance(TimeInstance(Double.POSITIVE_INFINITY), false, false)
    }

    private fun isAfter(fromPoint: TimeInstance, second: RangeTimeInstance): Boolean {
        return second.time > fromPoint || (second.time.compareTo(fromPoint) == 0 && ((second.start && !second.including) || (!second.start && second.including)))
    }

    private fun isBefore(fromPoint: TimeInstance, second: RangeTimeInstance): Boolean {
        return second.time < fromPoint || (second.time.compareTo(fromPoint) == 0 && ((second.start && second.including) || (!second.start && !second.including)))
    }

    private fun isBeforeEquals(fromPoint: TimeInstance, second: RangeTimeInstance): Boolean {
        return second.time <= fromPoint
    }

    private fun setupEvent(event: RangeTimeInstance): Int {
        events.add(event)
        events.sort()
        return events.indexOf(event)
    }

    private fun zeroTimeInterval(start: RangeTimeInstance, end: RangeTimeInstance): Boolean {
        return (start.time == end.time) && !(start.including && end.including)
    }

    class RangeTimeInstance(val time: TimeInstance, val start: Boolean, val including: Boolean) :
        Comparable<RangeTimeInstance> {

        fun isDelayed(): Boolean {
            return (start && !including) || (!start && including)
        }



        // TODO: Refactor
        override fun compareTo(other: RangeTimeInstance): Int {
            val timeComparison = this.time.compareTo(other.time)
            val equalTime = timeComparison == 0

            return if (equalTime) {
                // check point inclusion
                if (this.start && this.including && other.start && !other.including) {
                    1
                } else if (this.start && this.including && !other.start && other.including) {
                    -1
                } else if (this.start && this.including && !other.start && !other.including) {
                    0//1
                } else if (this.start && !this.including && other.start && other.including) {
                    1
                } else if (this.start && !this.including && !other.start && other.including) {
                    0//1
                } else if (this.start && !this.including && !other.start && !other.including) {
                    1
                } else if (!this.start && this.including && other.start && other.including) {
                    1
                } else if (!this.start && this.including && other.start && !other.including) {
                    0//-1
                } else if (!this.start && this.including && !other.start && !other.including) {
                    1
                } else if (!this.start && !this.including && other.start && other.including) {
                    0//-1
                } else if (!this.start && !this.including && other.start && !other.including) {
                    -1
                } else if (!this.start && !this.including && !other.start && other.including) {
                    -1
                } else {
                    0
                }
            } else {
                timeComparison
            }
        }

        override fun toString(): String {
            return "RangeTimeInstance(time=$time, start=$start, including=$including)"
        }

    }
}