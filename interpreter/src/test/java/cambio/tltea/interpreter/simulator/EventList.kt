package cambio.tltea.interpreter.simulator

import cambio.tltea.parser.core.temporal.TimeInstance
import java.util.*

class EventList {
    private val events: TreeMap<TimeInstance, MutableList<TestEventSimulator.Event>> = TreeMap()

    fun addEvent(time: TimeInstance, event: TestEventSimulator.Event) {
        val valueList: MutableList<TestEventSimulator.Event>
        if (events.containsKey(time)) {
            valueList = events[time]!!
        } else {
            valueList = mutableListOf()
            events[time] = valueList
        }
        valueList.add(event)
    }

    fun removeEvent(time: TimeInstance, event: TestEventSimulator.TestTimedEvent) {
        val eventsAtTime = events[time]!!
        if (eventsAtTime.contains(event)) {
            if (eventsAtTime.size == 1) {
                events.remove(time)
            } else {
                eventsAtTime.remove(event)
            }
        }
    }

    fun viewNext(): TestEventSimulator.Event {
        return events.firstEntry().value.first()
    }

    fun pollNextEvent(): Pair<TimeInstance, TestEventSimulator.Event>? {
        val firstEntry = events.firstEntry()
        if (events.keys.isNotEmpty()) {
            val firstEventList = firstEntry.value
            val firstEvent = firstEventList.first()
            if (firstEventList.size == 1) {
                events.remove(firstEntry.key)
            } else {
                firstEventList.removeAt(0)
            }
            return Pair(firstEntry.key, firstEvent)
        }
        return null
    }

}