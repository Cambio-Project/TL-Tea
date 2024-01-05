package cambio.tltea.interpreter.simulator

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.interpreter.connector.time.TimedEvent
import cambio.tltea.interpreter.connector.value.IMetricListener
import cambio.tltea.interpreter.connector.value.MetricDescriptor
import cambio.tltea.parser.core.temporal.TimeInstance
import kotlin.collections.HashMap

class TestEventSimulator(private val brokers: Brokers) {
    private val listeners: HashMap<MetricDescriptor, IMetricListener<Double>> = HashMap()
    private val listenersBoolean: HashMap<MetricDescriptor, IMetricListener<Boolean>> = HashMap()
    private val events: EventList = EventList()
    private var currentTime: TimeInstance = TimeInstance(0)
    private var lastUpdate: TimeInstance = TimeInstance(0)

    fun addListener(descriptor: MetricDescriptor, listener: IMetricListener<Double>) {
        listeners[descriptor] = listener
    }

    fun addBooleanListener(descriptor: MetricDescriptor, listener: IMetricListener<Boolean>) {
        listenersBoolean[descriptor] = listener
    }

    fun addEvent(time: TimeInstance, event: Event) {
        events.addEvent(time, event)
    }

    fun viewNext(): Event {
        return events.viewNext()
    }

    fun handleNext() {
        val firstEntry = events.pollNextEvent() ?: return
        val eventTime = firstEntry.first
        checkAndTriggerEndOfRound(eventTime)
        when (val event = firstEntry.second) {
            is TestTimedEvent -> handleTimedEvent(event)
            is TestValueEvent -> handleValueEvent(eventTime, event.descriptor, event.value)
        }
        this.lastUpdate = this.currentTime
    }

    fun forceHandle(time: TimeInstance, descriptor: MetricDescriptor, value: Double) {
        handleValueEvent(time, descriptor, value)
    }

    fun forceHandle(time: TimeInstance, descriptor: MetricDescriptor, value: Boolean) {
        handleValueEvent(time, descriptor, value)
    }

    fun forceEndRound() {
        brokers.timeManager.triggerTimeInstanceEnded()
    }

    fun forceEndExperiment(time: TimeInstance) {
        brokers.timeManager.triggerExperimentEnded(time)
    }

    private fun checkAndTriggerEndOfRound(time: TimeInstance) {
        if (time != this.lastUpdate) {
            brokers.timeManager.triggerTimeInstanceEnded()
        }
    }

    private fun handleValueEvent(time: TimeInstance, descriptor: MetricDescriptor, value: Double) {
        currentTime = time
        val listener = listeners[descriptor]!!
        listener.update(value, currentTime)
    }

    private fun handleValueEvent(time: TimeInstance, descriptor: MetricDescriptor, value: Boolean) {
        currentTime = time
        val listenerBoolean = listenersBoolean[descriptor]!!
        listenerBoolean.update(value, currentTime)
    }

    private fun handleTimedEvent(event: TestTimedEvent) {
        event.timedEvent.fire()
    }

    fun schedule(event: TimedEvent) {
        events.addEvent(event.time, TestTimedEvent(event))
    }

    fun reschedule(oldTime: TimeInstance, event: TimedEvent) {
        val eventToReschedule = TestTimedEvent(event)
        events.removeEvent(oldTime, eventToReschedule)
        events.addEvent(event.time, eventToReschedule)
    }

    fun cancel(oldTime: TimeInstance, event: TimedEvent) {
        events.removeEvent(oldTime, TestTimedEvent(event))
    }

    open class Event {

    }

    class TestTimedEvent(val timedEvent: TimedEvent) : Event() {
        override fun equals(other: Any?): Boolean {
            if (other is TestTimedEvent) {
                return timedEvent == other.timedEvent
            }
            return false
        }
    }

    class TestValueEvent(val descriptor: MetricDescriptor, val value: Double) : Event() {}
}