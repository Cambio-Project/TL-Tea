package cambio.tltea.interpreter.simulator

import cambio.tltea.interpreter.connector.time.ITimedEventScheduler
import cambio.tltea.interpreter.connector.time.TimedEvent
import cambio.tltea.parser.core.temporal.TimeInstance

class TestTimedEventScheduler(private val event: TimedEvent, private val simulator: TestEventSimulator) :
    ITimedEventScheduler {
    private var oldTime: TimeInstance = event.time

    override fun schedule() {
        oldTime = event.time
        simulator.addEvent(oldTime, TestEventSimulator.TestTimedEvent(event))
    }

    override fun reschedule() {
        simulator.reschedule(oldTime, event)
        oldTime = event.time
    }

    override fun cancel() {
        simulator.cancel(oldTime, event)
    }
}