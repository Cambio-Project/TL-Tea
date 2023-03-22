package cambio.tltea.interpreter.connector.value

import cambio.tltea.parser.core.temporal.TimeInstance

class SimpleMetricListener<T : Any> : IMetricListener<T> {
    private val subscribers: MutableSet<IMetricSubscriber<T>> = HashSet()

    override fun update(value: T, time: TimeInstance) {
        for (subscriber in subscribers) {
            subscriber.update(value, time)
        }
    }

    override fun subscribe(subscriber: IMetricSubscriber<T>) {
        subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: IMetricSubscriber<T>) {
        subscribers.remove(subscriber)
    }
}