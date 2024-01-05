package cambio.tltea.interpreter.connector.time;

/**
 * This interface must be implemented by a data source to capture temporal behavior. A scheduler is responsible for
 * handling a {@link TimedEvent}.
 */
public interface ITimedEventScheduler {
    /**
     * Schedules a {@link TimedEvent} acoording to its specified time. Should only be called the first time the event
     * gets scheduled.
     */
    void schedule();

    /**
     * Schedules a (previously scheduled) {@link TimedEvent} at the new specified time.
     */
    void reschedule();

    /**
     * Removes the {@link TimedEvent} from the schedule.
     */
    void cancel();
}
