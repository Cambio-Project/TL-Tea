package cambio.tltea.interpreter.connector.value

import cambio.tltea.interpreter.connector.time.TimeManager
import cambio.tltea.parser.core.temporal.TimeInstance

class SimpleMetricListener<T : Any>(private val timeManager : TimeManager) : IMetricListener<T> {
    private val subscribers: MutableSet<IMetricSubscriber<T>> = HashSet()

    override fun update(value: T, time: TimeInstance) {
        for (subscriber in subscribers) {
            subscriber.update(value, time)
        }
        timeManager.setLatestUpdateTime(time)
    }

    override fun subscribe(subscriber: IMetricSubscriber<T>) {
        subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: IMetricSubscriber<T>) {
        subscribers.remove(subscriber)
    }
}