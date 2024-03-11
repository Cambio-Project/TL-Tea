package cambio.tltea.interpreter.connector.time;

    public interface ITimedEventSchedulerFactory<T extends ITimedEventScheduler> {
        T createInstance(TimedEvent event);
    }
    
