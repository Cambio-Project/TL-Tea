package cambio.tltea.interpreter.simulator

import cambio.tltea.interpreter.connector.time.ITimedEventSchedulerFactory
import cambio.tltea.interpreter.connector.time.TimedEvent

class TestTimedEventSchedulerFactory(private val simulator: TestEventSimulator):ITimedEventSchedulerFactory<TestTimedEventScheduler> {
    override fun createInstance(event: TimedEvent): TestTimedEventScheduler {
        return TestTimedEventScheduler(event, simulator)
    }
}