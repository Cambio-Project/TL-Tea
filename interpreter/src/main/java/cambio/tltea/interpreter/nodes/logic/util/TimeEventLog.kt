package cambio.tltea.interpreter.nodes.logic.util

import cambio.tltea.parser.core.temporal.TemporalInterval
import cambio.tltea.parser.core.temporal.TimeInstance

/**
 * Manages [time events][TimeEvent] in a kind of (sorted) log.
 * This log keeps track of when a property has been (dis)satisfied. In particular, the log provides means to efficiently
 * [add][add] events and [evaluate][evaluate] the state for points in time.
 */
open class TimeEventLog {
    protected val events: MutableList<TimeEvent> = mutableListOf()
    private val startTime = TimeEvent(TimeInstance(0, false), false)
    private val infinityTime = TimeInstance(Double.POSITIVE_INFINITY)

    private val previousEventIncludingFinder = PreviousEventIncludingFinder(events, startTime)
    private val previousEventExcludingFinder = PreviousEventExcludingFinder(events, startTime)
    private val nextEventIncludingFinder = NextEventIncludingFinder(events, startTime)
    private val nextEventExcludingFinder = NextEventExcludingFinder(events, startTime)
    private val singleTimeEventFinder = SingleTimeEventFinder(events)

    fun contains(event: TimeEvent): Boolean {
        return events.contains(event)
    }

    fun evaluate(time: TimeInstance): Boolean {
        val previousEvent = findPreviousEventIncluding(time)
        return previousEvent.second.value
    }

    /**
     * Adds a whole interval.
     * Note that the end of the interval must be after the start and their satisfaction values have to differ.
     */
    fun addInterval(start: TimeEvent, end: TimeEvent) {
        if (end < start) {
            throw IllegalArgumentException("Start must occur before end.")
        }
        if (end.value == start.value) {
            throw IllegalArgumentException("Interval satisfaction values must be different for start and end")
        }
        if (zeroTimeInterval(start, end)) {
            return // do not add empty intervals
        }

        val timeInstancesToRemove = findTimeEventIndices(start.time, end.time)
        for (index in timeInstancesToRemove) {
            events.removeAt(index)
        }

        add(start)
        add(end)
    }

    fun add(event: TimeEvent) {
        var eventIndex = setupEvent(event)
        eventIndex = handleEventBeforeNewEvent(eventIndex, event)
        if (eventIndex >= 0) {
            handleEventAfterNewEvent(eventIndex, event)
        }
    }

    /**
     * Moves the last event from the specified time point to the specified time point if it has the specified satisfaction value.
     */
    fun delayEvent(from: TimeInstance, to: TimeInstance, value: Boolean) {
        val (index, event) = findNextEventWithValueIncluding(from, value)
        val foundEvent = index >= 0
        val eventIsInInterval = event.time <= to
        if (foundEvent && eventIsInInterval) {
            events.removeAt(index)
            add(TimeEvent(to, value))

        }
    }

    /**
     * Delays the previous event before the specified time point to the specified time  point if it has the specified satisfaction value.
     */
    fun delayEvent(to: TimeInstance, value: Boolean) {
        val (previousIndex, previousEvent) = findPreviousEventExcluding(to)
        if (previousIndex >= 0) {
            if (previousEvent.value == value) {
                events.removeAt(previousIndex)
            }
        }
        add(TimeEvent(to, value))
    }

    private fun handleEventBeforeNewEvent(eventIndex: Int, event: TimeEvent): Int {
        if (eventIndex > 0) {
            val previousEventIndex = eventIndex - 1
            val previousEvent = events[previousEventIndex]
            if (previousEvent.value == event.value) {
                events.removeAt(eventIndex)
                return -1
            }
        }
        return eventIndex
    }

    private fun handleEventAfterNewEvent(eventIndex: Int, event: TimeEvent) {
        if (eventIndex < events.lastIndex) {
            val nextEventIndex = eventIndex + 1
            val nextEvent = events[nextEventIndex]
            if (nextEvent.value == event.value) {
                events.removeAt(nextEventIndex)
            }
        }
    }

    /**
     * Deletes events between to time points including both time points.
     * Only deletes events with the specified satisfaction values.
     */
    fun deleteEvent(from: TimeInstance, to: TimeInstance, value: Boolean) {
        for (eventIndex in findTimeEventIndices(from, to, value)) {
            events.removeAt(eventIndex)
        }
    }

    fun findPositiveIntervals(): List<TemporalInterval> {
        val intervalPoints: MutableList<TimeEvent> = events.toMutableList()

        if (intervalPoints.isNotEmpty()) {
            if (intervalPoints.last().value) {
                intervalPoints.add(TimeEvent(infinityTime, false))
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
                    endEvent.time.isPlusEpsilon
                )
            )
        }
        return intervals
    }

    /**
     * Tries to find a time event at the specified time point.
     */
    fun findTimeEvent(at: TimeInstance): TimeEvent? {
        val (index, event) = singleTimeEventFinder.find(at)
        return if (index >= 0) {
            event
        } else {
            null
        }
    }

    /**
     * Finds all time instance within two time points, including the time points.
     */
    fun findTimeEvents(from: TimeInstance, to: TimeInstance): MutableList<TimeEvent> {
        val list = mutableListOf<TimeEvent>()
        if (from > to) {
            return list
        }

        val lastIndex = findPreviousEventIncluding(to).first
        if (lastIndex < 0) {
            return list
        }

        for (currentIndex: Int in lastIndex downTo 0) {
            val event = events[currentIndex]
            if (event.time <= to) {
                if (event.time < from) {
                    break
                } else {
                    list.add(0, event)
                }
            }
        }
        return list
    }

    // delivered in reverse order!
    private fun findTimeEventIndices(from: TimeInstance, to: TimeInstance, value: Boolean): List<Int> {
        val list = mutableListOf<Int>()
        if (from > to) {
            return list
        }

        val lastIndex = findPreviousEventIncluding(to).first
        if (lastIndex < 0) {
            return list
        }

        for (currentIndex: Int in lastIndex downTo 0) {
            val event = events[currentIndex]
            if (event.time <= to) {
                if (event.time < from) {
                    break
                } else {
                    if (event.value == value) {
                        list.add(currentIndex)
                    }
                }
            }
        }
        return list
    }


    // delivered in reverse order!
    private fun findTimeEventIndices(from: TimeInstance, to: TimeInstance): List<Int> {
        val list = mutableListOf<Int>()
        if (from > to) {
            return list
        }

        val lastIndex = findPreviousEventIncluding(to).first
        if (lastIndex < 0) {
            return list
        }

        for (currentIndex: Int in lastIndex downTo 0) {
            val event = events[currentIndex]
            if (event.time <= to) {
                if (event.time < from) {
                    break
                } else {
                    list.add(currentIndex)
                }
            }
        }
        return list
    }

    private fun findPreviousEventIncluding(toPoint: TimeInstance): Pair<Int, TimeEvent> {
        return previousEventIncludingFinder.find(toPoint)
    }

    private fun findPreviousEventExcluding(toPoint: TimeInstance): Pair<Int, TimeEvent> {
        return previousEventExcludingFinder.find(toPoint)
    }

    // after from point
    fun findNextEventWithSameValueExcluding(fromPoint: TimeInstance, value: Boolean): Pair<Int, TimeEvent> {
        val nextIndex = nextEventExcludingFinder.find(fromPoint).first
        if (nextIndex < 0) {
            return Pair(-1, TimeEvent(infinityTime, value))
        }
        return findFrom(nextIndex, value)
    }

    private fun findNextEventWithValueIncluding(fromPoint: TimeInstance, value: Boolean): Pair<Int, TimeEvent> {
        val nextIndex = nextEventIncludingFinder.find(fromPoint).first
        if (nextIndex < 0) {
            return Pair(-1, TimeEvent(infinityTime, value))
        }
        return findFrom(nextIndex, value)
    }

    /**
     * Finds next event from (including) the specified position.
     * Searches until an event with the specified value is found.
     */
    private fun findFrom(index: Int, value: Boolean): Pair<Int, TimeEvent> {
        var nextIndex = index
        while (nextIndex <= events.lastIndex) {
            val nextEvent = events[nextIndex]
            if (nextEvent.value == value) {
                return Pair(nextIndex, nextEvent)
            }
            nextIndex++
        }
        return Pair(-1, TimeEvent(infinityTime, value))
    }


    private fun setupEvent(event: TimeEvent): Int {
        for (currentEvent in events.asReversed().withIndex()) {
            if (currentEvent.value <= event) {
                return if (currentEvent.value.time == event.time) {
                    val positionToReplace = events.size - currentEvent.index - 1
                    events[positionToReplace] = event
                    positionToReplace
                } else {
                    val positionToAddAt = events.size - currentEvent.index
                    events.add(positionToAddAt, event)
                    positionToAddAt
                }
            }
        }
        events.add(0, event)
        return 0
    }

    private fun zeroTimeInterval(start: TimeEvent, end: TimeEvent): Boolean {
        return start.time == end.time
    }

}