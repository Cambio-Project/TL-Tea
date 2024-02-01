package cambio.tltea.interpreter.nodes.logic.util

import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

open class TimeEventLog {
    protected val events: MutableList<TimeEvent> = mutableListOf()
    fun contains(event: TimeEvent): Boolean {
        return events.contains(event)
    }

    fun evaluate(time: TimeInstance): Boolean {
        val previousEvent = findPreviousEventIncluding(time)
        return previousEvent.value
    }

    fun addInterval(start: TimeEvent, end: TimeEvent) {
        if (end < start) {
            throw IllegalArgumentException("Start must occur before end.")
        }
        if (zeroTimeInterval(start, end)) {
            return // do not add empty intervals
        }

        val timeInstancesToRemove = findTimeInstanceMarkers(start.time, end.time)
        // TODO: Remove
        /*
        if (timeInstancesToRemove.isNotEmpty()) {
            val lastValue = timeInstancesToRemove.last().value
            if (end.value == lastValue) {
                timeInstancesToRemove.removeLast()
            }
        }
        */
        events.removeAll(timeInstancesToRemove)

        add(start)
        add(end)
    }

    fun add(event: TimeEvent) {
        var eventIndex = setupEvent(event)
        eventIndex = handleEqualEventBefore(eventIndex)
        handleEqualEventAfter(eventIndex)
        handleEventBeforeNewEvent(eventIndex)
        eventIndex = events.indexOf(event)
        if (eventIndex >= 0) {
            handleEventAfterNewEvent(eventIndex)
        }
    }

    fun delayEvent(from: TimeInstance, to: TimeInstance, value: Boolean) {
        val event = findNextEventWithValueIncluding(from, value)
        if (!event.time.isAfter(to)) {
            events.remove(event)
            add(TimeEvent(to, value))
        }
    }

    // TODO: align with previous
    fun delayEvent(to: TimeInstance, value: Boolean) {
        val previousEvent = findPreviousEventExcluding(to)
        if (previousEvent.value == value) {
            events.remove(previousEvent)
        }
        add(TimeEvent(to, value))
    }

    private fun handleEventBeforeNewEvent(eventIndex: Int) {
        val event = events[eventIndex]
        if (eventIndex > 0) {
            val previousEvent = events[eventIndex - 1]
            if (previousEvent.value == event.value) {
                events.removeAt(eventIndex)
            } else if (zeroTimeInterval(previousEvent, event)) {
                events.removeAt(eventIndex - 1)
            }
        }
    }

    private fun handleEventBeforeNewStart(startEventIndex: Int) {
        if (startEventIndex > 0) {
            val previousEvent = events[startEventIndex - 1]
            if (previousEvent.value) {
                events.removeAt(startEventIndex)
            }
        }
    }

    private fun handleEventBeforeNewEnd(endEventIndex: Int) {
        val endEvent = events[endEventIndex]
        if (endEventIndex > 0) {
            val previousEvent = events[endEventIndex - 1]

            if (!previousEvent.value) {
                events.removeAt(endEventIndex - 1)
            } else if (zeroTimeInterval(previousEvent, endEvent)) {
                events.removeAt(endEventIndex - 1)
                events.removeAt(endEventIndex - 1)
            }
        }
    }


    private fun handleEventAfterNewEvent(eventIndex: Int) {
        val event = events[eventIndex]
        if (eventIndex < events.lastIndex) {
            val nextEvent = events[eventIndex + 1]
            if (nextEvent.value == event.value) {
                events.removeAt(eventIndex + 1)
            } else if (zeroTimeInterval(event, nextEvent)) {
                events.removeAt(eventIndex + 1)
            }
        }
    }

    private fun handleEventAfterNewStart(startEventIndex: Int) {
        val startEvent = events[startEventIndex]
        if (startEventIndex < events.lastIndex) {
            val nextEvent = events[startEventIndex + 1]
            if (nextEvent.value) {
                events.removeAt(startEventIndex + 1)
            } else if (zeroTimeInterval(startEvent, nextEvent)) {
                events.removeAt(startEventIndex)
                events.removeAt(startEventIndex)
            }
        }
    }

    private fun handleEventAfterNewEnd(endEventIndex: Int) {
        if (endEventIndex < events.lastIndex) {
            val nextEvent = events[endEventIndex + 1]
            if (!nextEvent.value) {
                events.removeAt(endEventIndex)
            }
        }
    }

    /*
    fun deleteEqualEvent(event: TimeEvent) {
        for (foundEvent in findTimeInstanceMarker(event.time)) {
            if (foundEvent.value == event.value) {
                events.remove(event)
            }
        }
    }
     */

    // needs to be same!
    fun deleteEvent(event: TimeEvent) {
        events.remove(event)
    }

    // including
    fun deleteEvent(from: TimeInstance, to: TimeInstance, value: Boolean) {
        for (event in findTimeInstanceMarkers(from, to)) {
            if (event.value == value) {
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


    private fun handleEqualEventAfter(eventIndex: Int) {
        val event = events[eventIndex]
        if (eventIndex < events.lastIndex) {
            val nextEvent = events[eventIndex + 1]
            if (nextEvent.compareTo(event) == 0) {
                events.removeAt(eventIndex)
            }
        }
    }


    fun findPositiveIntervals(): List<TemporalInterval> {
        val intervalPoints: MutableList<TimeEvent> = events.toMutableList()

        if (intervalPoints.isNotEmpty()) {
            //if (!intervalPoints.first().start) {
            //    intervalPoints.add(0, RangeTimeInstance(TimeInstance(0), true, true))
            //}
            if (intervalPoints.last().value) {
                intervalPoints.add(TimeEvent(TimeInstance(Double.POSITIVE_INFINITY), false))
            }
        }

        val intervals: MutableList<TemporalInterval> = mutableListOf()
        for (i in 1..intervalPoints.lastIndex step 2) {
            val startEvent = intervalPoints[i - 1]
            val endEvent = intervalPoints[i]
            assert(startEvent.value)
            assert(!endEvent.value)
            intervals.add(
                TemporalInterval(
                    startEvent.time.time,
                    endEvent.time.time,
                    !startEvent.time.isPlusEpsilon,
                    endEvent.time.isPlusEpsilon // TODO: double check
                )
            )
        }
        return intervals
    }

    fun isActiveAt(pointInTime: TimeInstance): Boolean {
        val intervals = this.findPositiveIntervals()
        for (interval in intervals) {
            if (interval.contains(pointInTime)) {
                return true
            }
        }
        return false
    }

    fun findRangeTimeInstance(at: TimeInstance): TimeEvent? {
        for (event in events) {
            if (event.time == at) {
                return event
            }
        }
        return null
    }

    // including end? yes
    fun findTimeInstanceMarkers(from: TimeInstance, to: TimeInstance): MutableList<TimeEvent> {
        val list = mutableListOf<TimeEvent>()
        if (from > to) {
            return list
        }
        for (event in events.reversed()) {
            if (event.time.isBeforeEquals(to)) {
                if (event.time.isBefore(from)) {
                    break
                } else {
                    list.add(0, event)
                }
            }
        }
        return list
    }

    private fun findPreviousEventIncluding(toPoint: TimeInstance): TimeEvent {
        var previousTimeInstance = TimeEvent(TimeInstance(0, false), false)
        for (currentTimeInstance in events) {
            if (!currentTimeInstance.time.isBeforeEquals(toPoint)) {
                break
            }
            previousTimeInstance = currentTimeInstance
        }
        return previousTimeInstance
    }

    private fun findPreviousEventExcluding(toPoint: TimeInstance): TimeEvent {
        var previousTimeInstance = TimeEvent(TimeInstance(0, false), false)
        for (currentTimeInstance in events) {
            if (!currentTimeInstance.time.isBefore(toPoint)) {
                break
            }
            previousTimeInstance = currentTimeInstance
        }
        return previousTimeInstance
    }

    // after from point
    fun findNextEventWithSameValueExcluding(fromPoint: TimeInstance, value: Boolean): TimeEvent {
        for (currentTimeInstance in events) {
            if (currentTimeInstance.value == value && currentTimeInstance.time.isAfter(fromPoint)) {
                return currentTimeInstance
            }
        }
        return TimeEvent(TimeInstance(Double.POSITIVE_INFINITY), value)
    }

    fun findNextEventWithValueIncluding(fromPoint: TimeInstance, value: Boolean): TimeEvent {
        for (currentTimeInstance in events) {
            if (currentTimeInstance.value == value && currentTimeInstance.time.isAfterEquals(fromPoint)) {
                return currentTimeInstance
            }
        }
        return TimeEvent(TimeInstance(Double.POSITIVE_INFINITY), true)
    }

    private fun setupEvent(event: TimeEvent): Int {
        events.add(event)
        events.sort()
        return events.indexOf(event)
    }

    private fun zeroTimeInterval(start: TimeEvent, end: TimeEvent): Boolean {
        return start == end
    }

}