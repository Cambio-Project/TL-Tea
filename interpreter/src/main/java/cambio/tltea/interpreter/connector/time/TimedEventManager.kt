package cambio.tltea.interpreter.connector.time

class TimedEventManager() {
    lateinit var schedulerFactory: ITimedEventSchedulerFactory<ITimedEventScheduler>

    fun register(schedulerFactory : ITimedEventSchedulerFactory<ITimedEventScheduler>){
        this.schedulerFactory = schedulerFactory
    }

    fun getTimedEventScheduler(event: TimedEvent): ITimedEventScheduler {
        return this.schedulerFactory.createInstance(event)
    }


}