package cambio.tltea.interpreter.connector.time

import cambio.tltea.interpreter.connector.IAction
import cambio.tltea.parser.core.temporal.TimeInstance
import java.util.*

class TimedEvent(
    private val timedEventManager: TimedEventManager,
    var time: TimeInstance,
    private val action: IAction,
) {
    private val scheduler: ITimedEventScheduler = timedEventManager.getTimedEventScheduler(this)
    private var done: Boolean = false

    init {
        scheduler.schedule()
    }

    constructor(oldEvent: TimedEvent, time: TimeInstance) : this(oldEvent.timedEventManager, time, oldEvent.action);

    // TODO: Is this fixed or relative time?

    fun set(time: TimeInstance) {
        if (done) {
            throw IllegalStateException()
        }
        Objects.requireNonNull(time)
        this.time = time
        scheduler.reschedule()
    }

    public fun fire() {
        if (done) {
            throw IllegalStateException()
        }
        action.perform()
        this.done = true
    }

    public fun cancel() {
        scheduler.cancel()
        this.done = true
    }

}